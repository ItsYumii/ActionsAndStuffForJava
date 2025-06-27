package net.yumi.actionsandstuff.anim;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AnimationManager {
    private static final Map<String, Animation> animations = new HashMap<>();

    public static void loadAnimations() {
        Gson gson = new Gson();

        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(AnimationManager.class.getClassLoader()
                        .getResourceAsStream("assets/actionsandstuff/anim/anim.json")))) {

            Map<String, Animation> data = gson.fromJson(reader, new TypeToken<Map<String, Animation>>() {}.getType());

            if (data == null) {
                throw new IllegalStateException("Parsed anim.json is null.");
            }

            animations.putAll(data);
            System.out.println("Loaded animation keys: " + animations.keySet());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Animation getAnimation(String id) {
        return animations.get(id);
    }

    public class AnimationData {
        public Map<String, Animation> animations;
    }

    public class Animation {
        public Map<String, List<Keyframe>> layers = new HashMap<>();
        public float lockTime = 0;
    }

    public class Keyframe {
        public float time;
        public float[] rotation;
        public float[] position;
        public String easing = "linear";
    }
}