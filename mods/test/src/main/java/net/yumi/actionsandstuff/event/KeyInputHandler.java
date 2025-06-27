package net.yumi.actionsandstuff.event;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.JTextComponent;

public class KeyInputHandler {
    public static final String KEY_CATEGORY = "key.category.actionsandstuff";

    public static KeyBinding rotX;
    public static KeyBinding rotY;
    public static KeyBinding rotZ;
    public static KeyBinding moveX;
    public static KeyBinding moveY;
    public static KeyBinding moveZ;

    public static void register() {
        rotX = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.actionsandstuff.rotx",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                KEY_CATEGORY
        ));
        rotY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.actionsandstuff.roty",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KEY_CATEGORY
        ));
        rotZ = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.actionsandstuff.rotz",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                KEY_CATEGORY
        ));

        moveX = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.actionsandstuff.movex",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                KEY_CATEGORY
        ));
        moveY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.actionsandstuff.movey",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                KEY_CATEGORY
        ));
        moveZ = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.actionsandstuff.movez",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_SEMICOLON,
                KEY_CATEGORY
        ));
    }
}
