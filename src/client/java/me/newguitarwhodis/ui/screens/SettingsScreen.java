package me.newguitarwhodis.ui.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingsScreen extends Screen {
    protected SettingsScreen() {
        super(Text.literal("Settings"));
    }

    public void init() {

        // Debug button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Debug"), btn -> {
            assert this.client != null;
            this.client.setScreen(new DebugScreen());
        }).dimensions(this.width / 2 - 75, this.height / 2 - 30, 150, 20).build());

        // Widget Manager button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Widget Manager"), btn -> {
            assert this.client != null;
            this.client.setScreen(new WidgetManagerScreen());
        }).dimensions(this.width / 2 - 75, this.height / 2, 150, 20).build());

        // Back Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> {
            this.client.setScreen(new InGameStatsScreen());
        }).dimensions(10, this.height - 30, 80, 20).build());
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render your settings screen here
        super.render(context, mouseX, mouseY, delta);

        String title = "Settings";
        int titleX = (this.width - this.textRenderer.getWidth(title)) / 2;
        context.drawText(this.textRenderer, Text.literal(title), titleX, 20, 0xFFFFFF, false);
    }
}
