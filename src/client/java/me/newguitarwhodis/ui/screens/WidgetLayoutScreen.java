package me.newguitarwhodis.ui.screens;

import me.newguitarwhodis.ui.HudWidgets.TopPlayerWidget;
import me.newguitarwhodis.ui.HudWidgets.YourStatsWidget;
import me.newguitarwhodis.widgets.WidgetPosition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Screen for dragging and positioning HUD widgets with real-time previews.
 */
public class WidgetLayoutScreen extends Screen {

    private final Map<String, int[]> dragPositions = new HashMap<>();
    private String dragging = null;
    private int dragOffsetX, dragOffsetY;

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 20;

    public WidgetLayoutScreen() {
        super(Text.literal("Move HUD Widgets"));
    }

    @Override
    protected void init() {
        // Clone current widget positions into a local drag map
        WidgetPosition.positions.forEach((key, pos) -> dragPositions.put(key, pos.clone()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Map.Entry<String, int[]> entry : dragPositions.entrySet()) {
            String key = entry.getKey();
            int[] pos = entry.getValue();
            int x = pos[0];
            int y = pos[1];
            int width = getWidgetWidth(key);
            int height = getWidgetHeight(key);

            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                dragging = key;
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
            int[] pos = dragPositions.get(dragging);
            int widgetWidth = getWidgetWidth(dragging);
            int widgetHeight = getWidgetHeight(dragging);

            int newX = (int) mouseX - dragOffsetX;
            int newY = (int) mouseY - dragOffsetY;

            // Clamp widget within screen bounds
            newX = Math.max(0, Math.min(newX, this.width - widgetWidth));
            newY = Math.max(0, Math.min(newY, this.height - widgetHeight));

            pos[0] = newX;
            pos[1] = newY;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Inject temporary override positions for live rendering
        for (Map.Entry<String, int[]> entry : dragPositions.entrySet()) {
            WidgetPosition.override(entry.getKey(), entry.getValue());
        }

        // Render real widget previews
        new YourStatsWidget().onHudRender(context, null);
        new TopPlayerWidget().onHudRender(context, null);

        // Draw screen title
        int titleX = this.width / 2 - this.textRenderer.getWidth(this.title) / 2;
        context.drawText(this.textRenderer, this.title, titleX, 10, 0xFFFFFF, false);

        // Remove overrides after rendering
        WidgetPosition.clearOverrides();
    }

    @Override
    public void close() {
        // Save the final positions to WidgetPosition storage
        dragPositions.forEach((key, pos) -> WidgetPosition.set(key, pos[0], pos[1]));
        MinecraftClient.getInstance().setScreen(null);
    }

    private int getWidgetWidth(String key) {
        return switch (key) {
            case "your_stats" -> YourStatsWidget.getWidth(MinecraftClient.getInstance());
            case "top_player" -> TopPlayerWidget.getWidth(MinecraftClient.getInstance());
            default -> DEFAULT_WIDTH;
        };
    }

    private int getWidgetHeight(String key) {
        return switch (key) {
            case "your_stats" -> YourStatsWidget.getHeight();
            case "top_player" -> TopPlayerWidget.getHeight();
            default -> DEFAULT_HEIGHT;
        };
    }
}
