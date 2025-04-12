package me.newguitarwhodis.interactions;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyboardManager {
    public static KeyBinding OPEN_GUI;
    public static void register() {
        OPEN_GUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open GUI",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_APOSTROPHE,
                "Bedwars Tracker"));
    }
}
