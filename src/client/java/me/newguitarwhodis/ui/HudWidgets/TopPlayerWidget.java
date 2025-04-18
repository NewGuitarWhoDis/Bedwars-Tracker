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

public class TopPlayerWidget implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (!WidgetManager.showTopPlayer) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Map<String, PlayerStats> allStats = StatsDatabase.getAll();
        String topPlayer = null;
        double topKD = -1;

        for (Map.Entry<String, PlayerStats> entry : allStats.entrySet()) {
            PlayerStats stats = entry.getValue();
            if (stats.totalDeaths == 0 && stats.totalKills == 0) continue; // skip players with no data
            double kd = (stats.totalDeaths == 0) ? stats.totalKills : (double) stats.totalKills / stats.totalDeaths;

            if (kd > topKD) {
                topKD = kd;
                topPlayer = entry.getKey();
            }
        }

        if (topPlayer == null) return; // no valid player found

        int[] pos = WidgetPosition.get("top_player");
        int x = pos[0];
        int y = pos[1];
        int padding = 6;

        String title = "Top Player";
        String statLine = topPlayer + " (K/D: " + String.format("%.2f", topKD) + ")";
        int width = Math.max(
                client.textRenderer.getWidth(title),
                client.textRenderer.getWidth(statLine)
        ) + padding * 2;

        int height = 30;

        // Draw background box
        context.fill(x, y, x + width, y + height, 0xAA000000);

        // Draw title + info
        context.drawText(client.textRenderer, Text.literal(title), x + padding, y + 4, 0xFFFF55, false);
        context.drawText(client.textRenderer, Text.literal(statLine), x + padding, y + 16, 0xFFFFFF, false);
    }

    public static int getWidth(MinecraftClient client) {
        String title = "Top Player";
        String sampleStatLine = "SampleName (K/D: 99.99)";
        int padding = 6;

        int titleWidth = client.textRenderer.getWidth(title);
        int lineWidth = client.textRenderer.getWidth(sampleStatLine);

        return Math.max(titleWidth, lineWidth) + padding * 2;
    }

    public static int getHeight() {
        return 12 + 10 + 8; // Title + stat line + some vertical spacing
    }
}
