package me.newguitarwhodis.ui.screens;

import me.newguitarwhodis.widgets.WidgetPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class WidgetLayoutScreen extends Screen {
    private final Map<String, int[]> dragPositions = new HashMap<>();
    private String dragging = null;
    private int dragOffsetX, dragOffsetY;

    public WidgetLayoutScreen() {
        super(Text.literal("Move HUD Widgets"));
    }

    private int getWidgetWidth(String key) {
        MinecraftClient client = MinecraftClient.getInstance();
        return switch (key) {
            case "your_stats" -> me.newguitarwhodis.ui.HudWidgets.YourStatsWidget.getWidth(client);
            case "top_player" -> me.newguitarwhodis.ui.HudWidgets.TopPlayerWidget.getWidth(client);
            default -> 100;
        };
    }

    private int getWidgetHeight(String key) {
        return switch (key) {
            case "your_stats" -> me.newguitarwhodis.ui.HudWidgets.YourStatsWidget.getHeight();
            case "top_player" -> me.newguitarwhodis.ui.HudWidgets.TopPlayerWidget.getHeight();
            default -> 20;
        };
    }


    @Override
    protected void init() {
        for (Map.Entry<String, int[]> entry : WidgetPosition.positions.entrySet()) {
            dragPositions.put(entry.getKey(), entry.getValue().clone());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Map.Entry<String, int[]> entry : dragPositions.entrySet()) {
            int x = entry.getValue()[0];
            int y = entry.getValue()[1];
            if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 20) {
                dragging = entry.getKey();
                dragOffsetX = (int) mouseX - x;
                dragOffsetY = (int) mouseY - y;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (dragging != null) {
            dragPositions.get(dragging)[0] = (int) mouseX - dragOffsetX;
            dragPositions.get(dragging)[1] = (int) mouseY - dragOffsetY;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (Map.Entry<String, int[]> entry : dragPositions.entrySet()) {
            int x = entry.getValue()[0];
            int y = entry.getValue()[1];
            String label = entry.getKey().replace("_", " ").toUpperCase();

            int width = getWidgetWidth(entry.getKey());
            int height = getWidgetHeight(entry.getKey());
            context.fill(x, y, x + width, y + height, 0x88000000);
            context.drawText(this.textRenderer, Text.literal(label), x + 5, y + 6, 0xFFFFFF, false);

        }

        context.drawText(this.textRenderer, this.title, this.width / 2 - this.textRenderer.getWidth(this.title) / 2, 10, 0xFFFFFF, false);
    }

    @Override
    public void close() {
        // Save positions when closing
        for (Map.Entry<String, int[]> entry : dragPositions.entrySet()) {
            WidgetPosition.set(entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
        }
        MinecraftClient.getInstance().setScreen(null);
    }
}
