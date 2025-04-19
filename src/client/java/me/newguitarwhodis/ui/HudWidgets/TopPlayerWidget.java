package me.newguitarwhodis.ui.HudWidgets;

import me.newguitarwhodis.database.PlayerStats;
import me.newguitarwhodis.database.StatsDatabase;
import me.newguitarwhodis.widgets.WidgetPosition;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

import java.util.Map;

/**
 * Renders the "Top Player" widget showing the highest K/D player.
 */
public class TopPlayerWidget implements HudRenderCallback {

    private static final int PADDING = 6;
    private static final int TITLE_HEIGHT = 12;
    private static final int LINE_HEIGHT = 10;

    private static String cachedStatLine = "SampleName (K/D: 99.99)";
    private static int cachedWidth = -1;

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (!WidgetManager.showTopPlayer) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Map<String, PlayerStats> allStats = StatsDatabase.getAll();
        String topPlayer = null;
        double topKD = -1;

        for (var entry : allStats.entrySet()) {
            PlayerStats stats = entry.getValue();
            if (stats.totalKills == 0 && stats.totalDeaths == 0) continue;

            double kd = stats.totalDeaths == 0 ? stats.totalKills : (double) stats.totalKills / stats.totalDeaths;

            if (kd > topKD) {
                topKD = kd;
                topPlayer = entry.getKey();
            }
        }

        if (topPlayer == null) return;

        int[] pos = WidgetPosition.get("top_player");
        int x = pos[0];
        int y = pos[1];

        String title = "Top Player:";
        cachedStatLine = topPlayer + " (K/D: " + String.format("%.2f", topKD) + ")";

        int textWidth = getAccurateWidth(client);
        int height = TITLE_HEIGHT + LINE_HEIGHT + PADDING * 2;

        // Draw background
        context.fill(x, y, x + textWidth, y + height, 0xAA000000);

        int textX = x + PADDING;
        int textY = y + PADDING;

        context.drawText(client.textRenderer, Text.literal(title), textX, textY, 0xFFFF55, false);
        context.drawText(client.textRenderer, Text.literal(cachedStatLine), textX, textY + TITLE_HEIGHT, 0xFFFFFF, false);
    }

    public static int getWidth(MinecraftClient client) {
        return getAccurateWidth(client);
    }

    public static int getHeight() {
        return TITLE_HEIGHT + LINE_HEIGHT + PADDING * 2;
    }

    private static int getAccurateWidth(MinecraftClient client) {
        int titleW = client.textRenderer.getWidth("Top Player:");
        int lineW = client.textRenderer.getWidth(cachedStatLine);
        cachedWidth = Math.max(titleW, lineW) + PADDING * 2;
        return cachedWidth;
    }
}
