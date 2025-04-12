package me.newguitarwhodis.ui.notifications;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatNotificationRenderer {
    public static void renderNotification(String line1, String line2, String line3) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("------ [ Bedwars Tracker ] ------"));
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(""));
        if (line1 != null) addMessage(line1);
        if (line2 != null) addMessage(line2);
        if (line3 != null) addMessage(line3);
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(""));
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("---------------------------------"));

    }

    public static void addMessage(String message) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(message));
    }
}
