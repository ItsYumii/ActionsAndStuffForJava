package net.yumi.actionsandstuff.anim;

public class EasingFunctions {
    public static double sigmoid(double t) {
        return 1.0 / (1.0 + Math.exp(-t));
    }

    public static float linear(float t) {
        return t;
    }

    public static float easeInQuad(float t) {
        return t * t;
    }

    public static float easeOutQuad(float t) {
        return t * (2 - t);
    }

    public static float easeInOutBack(float t) {
        float c1 = 1.70158F;
        float c2 = c1 * 1.525F;

        return t < 0.5
                ? (float) (Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2
                : (float) (Math.pow(2 * t - 2, 2) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2;
    }

    public static float easeInBack(float t) {
        float c1 = 1.70158F;
        float c3 = c1 + 1;

        return c3 * t * t * t - c1 * t * t;
    }

    public static float easeOutBack(float t) {
        float c1 = 1.70158F;
        float c3 = c1 + 1;

        return (float) (1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2));
    }

    public static float easeInSine(float t) {
        return (float)(1 - Math.cos((t * Math.PI) / 2));
    }

    public static float easeOutSine(float t) {
        return (float)(Math.sin((t * Math.PI) / 2));
    }

    public static float easeInOutSine(float t) {
        return (float)(-(Math.cos(Math.PI * t) - 1) / 2);
    }

    // Add more as needed... oke :3
}

