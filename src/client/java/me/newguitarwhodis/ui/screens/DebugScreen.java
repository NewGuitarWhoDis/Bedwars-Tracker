package me.newguitarwhodis.ui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class DebugScreen extends Screen {
    protected DebugScreen() {
        super(Text.literal("Debug"));
    }

    public void init() {

        // Test Kill Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Kill Test"), button -> {
            String playerName = MinecraftClient.getInstance().player.getName().getString();
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal( playerName + " was killed by " + playerName + "."));
        }).dimensions(this.width / 2 - 75, this.height / 2 - 30, 150, 20).build());

        // Test Final Kill Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Final Kill Test"), button -> {
            String playerName = MinecraftClient.getInstance().player.getName().getString();
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal( playerName + " was killed by " + playerName + "."));
        }).dimensions(this.width / 2 - 75, this.height / 2, 150, 20).build());

        // Test Final Kill With Stats Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Final Kill With Stats Test"), button -> {
            String playerName = MinecraftClient.getInstance().player.getName().getString();
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal( playerName + " was killed by " + playerName + " Final Kill!"));
        }).dimensions(this.width / 2 - 75, this.height / 2 + 30, 150, 20).build());

        // Test Void Death Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Void Death Test"), button -> {
            String playerName = MinecraftClient.getInstance().player.getName().getString();
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal( playerName + " was killed by the void."));
        }).dimensions(this.width / 2 - 75, this.height / 2 + 60, 150, 20).build());

        // Toggle Notification Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Toggle Notification Panel"), button -> {
            // Implement toggle notification logic here
        }).dimensions(this.width / 2 - 75, this.height / 2 + 90, 150, 20).build());

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            this.client.setScreen(new SettingsScreen());
        }).dimensions(10, this.height - 30, 80, 20).build());
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render your settings screen here
        super.render(context, mouseX, mouseY, delta);
    }
}
