package net.yumi.actionsandstuff.anim;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveAnimation {
    public String id;
    public AnimationManager.Animation animation;
    public boolean loop = true;
    public Map<String, Double> layerTimes = new HashMap<>();

    public ActiveAnimation(String id, AnimationManager.Animation animation, boolean loop) {
        this.id = id;
        this.animation = animation;
        this.loop = loop;
        for (String layer : animation.layers.keySet()) {
            layerTimes.put(layer, 0.0);
        }
    }

    public void update(double deltaTime) {
        for (String layer : animation.layers.keySet()) {
            double newTime = layerTimes.getOrDefault(layer, 0.0) + deltaTime;
            float maxTime = getMaxTime(animation.layers.get(layer));
            if (loop && newTime > maxTime) {
                newTime %= maxTime;
            }
            layerTimes.put(layer, newTime);
        }
    }

    public float[] getLayerPositionOffset(String layer) {
        return interpolateVec3(layerTimes.getOrDefault(layer, 0.0), animation.layers.get(layer), true);
    }

    public float[] getLayerRotationOffset(String layer) {
        return interpolateVec3(layerTimes.getOrDefault(layer, 0.0), animation.layers.get(layer), false);
    }

    private float[] interpolateVec3(double time, List<AnimationManager.Keyframe> keyframes, boolean position) {
        AnimationManager.Keyframe prev = null, next = null;

        for (AnimationManager.Keyframe kf : keyframes) {
            if (kf.time > time) {
                next = kf;
                break;
            }
            prev = kf;
        }

        if (prev == null) return position ? keyframes.get(0).position : keyframes.get(0).rotation;
        if (next == null) return position ? prev.position : prev.rotation;

        double rawT = (time - prev.time) / (next.time - prev.time);

        // Get easing function name from previous keyframe
        String easingName = prev.easing != null ? prev.easing : "linear";
        float easedT = applyEasing((float) rawT, easingName);

        float[] a = position ? prev.position : prev.rotation;
        float[] b = position ? next.position : next.rotation;

        return new float[] {
                lerp(a[0], b[0], easedT),
                lerp(a[1], b[1], easedT),
                lerp(a[2], b[2], easedT)
        };
    }

    private float applyEasing(float t, String easingName) {
        try {
            Method method = EasingFunctions.class.getMethod(easingName, float.class);
            return (float) method.invoke(null, t);
        } catch (Exception e) {
            System.out.println("Invalid easing function: " + easingName + ", defaulting to linear.");
            return EasingFunctions.linear(t);
        }
    }

    private float lerp(float a, float b, double t) {
        return (float) (a + (b - a) * t);
    }

    public void stopLayer(String layer) {
        layerTimes.remove(layer);
    }

    public float getMaxTime(List<AnimationManager.Keyframe> keyframes) {
        if (keyframes.isEmpty()) return 0;
        return keyframes.get(keyframes.size() - 1).time;
    }

    public boolean isFinished() {
        for (String layer : animation.layers.keySet()) {
            float maxTime = getMaxTime(animation.layers.get(layer));
            double time = layerTimes.getOrDefault(layer, 0.0);
            if (loop || time < maxTime) return false;
        }
        return true;
    }

    public void reset() {
        for (String layer : animation.layers.keySet()) {
            layerTimes.put(layer, 0.0);
        }
    }

    public void updateKeyframeValue(String layer, int keyframeIndex, float[] newPos, float[] newRot) {
        List<AnimationManager.Keyframe> keyframes = animation.layers.get(layer);
        if (keyframes == null || keyframeIndex >= keyframes.size()) return;

        AnimationManager.Keyframe kf = keyframes.get(keyframeIndex);
        if (newPos != null) kf.position = newPos;
        if (newRot != null) kf.rotation = newRot;
    }

}

