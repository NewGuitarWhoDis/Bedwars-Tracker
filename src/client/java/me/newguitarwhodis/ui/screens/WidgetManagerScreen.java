package me.newguitarwhodis.ui.screens;

import me.newguitarwhodis.ui.HudWidgets.WidgetManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class WidgetManagerScreen extends Screen {
    protected WidgetManagerScreen() {
        super(Text.literal("Widgets"));
    }

        public void init() {
            int centerX = this.width / 2;
            int startY = this.height / 4;
            int buttonWidth = 150;
            int buttonHeight = 20;
            int spacing = 24;

            // Toggle Your Stats
            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("Your Stats: " + (WidgetManager.showStats ? "ON" : "OFF")),
                    button -> {
                        WidgetManager.showStats = !WidgetManager.showStats;
                        button.setMessage(Text.literal("Your Stats: " + (WidgetManager.showStats ? "ON" : "OFF")));
                    }
            ).dimensions(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

            // Toggle Top Player
            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("Top Player: " + (WidgetManager.showTopPlayer ? "ON" : "OFF")),
                    button -> {
                        WidgetManager.showTopPlayer = !WidgetManager.showTopPlayer;
                        button.setMessage(Text.literal("Top Player: " + (WidgetManager.showTopPlayer ? "ON" : "OFF")));
                    }
            ).dimensions(centerX - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight).build());

            // Widget Layout Screen
            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("Widget Layout"),
                    button -> {
                        assert this.client != null;
                        this.client.setScreen(new WidgetLayoutScreen());
                    }
            ).dimensions(centerX - buttonWidth / 2, startY + spacing * 2, buttonWidth, buttonHeight).build());

        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            // Render your settings screen here
            super.render(context, mouseX, mouseY, delta);

            String title = "Widgets";
            int titleX = (this.width - this.textRenderer.getWidth(title)) / 2;
            context.drawText(this.textRenderer, Text.literal(title), titleX, 20, 0xFFFFFF, false);
        }

}
