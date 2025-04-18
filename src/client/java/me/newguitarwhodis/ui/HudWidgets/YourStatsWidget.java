package me.newguitarwhodis.ui.HudWidgets;

import me.newguitarwhodis.database.PlayerStats;
import me.newguitarwhodis.database.StatsDatabase;
import me.newguitarwhodis.widgets.WidgetPosition;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

public class YourStatsWidget implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if (!WidgetManager.showStats) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        String playerName = client.player.getName().getString();
        PlayerStats stats = StatsDatabase.get(playerName);

        // Text + box position
        int[] pos = WidgetPosition.get("your_stats");
        int x = pos[0];
        int y = pos[1];
        int lineHeight = 10;
        int padding = 6;

        String[] lines = {
                "Kills: " + stats.totalKills,
                "Deaths: " + stats.totalDeaths,
                "Void Deaths: " + stats.voidDeaths,
                "K/D: " + String.format("%.2f", (stats.totalDeaths == 0 ? stats.totalKills : (double) stats.totalKills / stats.totalDeaths))
        };

        String title = "Your Stats:";
        int titleHeight = 12;

        // Get the widest line for background
        int maxWidth = client.textRenderer.getWidth(title);
        for (String line : lines) {
            int width = client.textRenderer.getWidth(line);
            if (width > maxWidth) maxWidth = width;
        }

        // Draw background
        int boxWidth = maxWidth + padding * 2;
        int boxHeight = lines.length * lineHeight + titleHeight + padding * 2;
        drawContext.fill(x - padding, y - padding, x - padding + boxWidth, y - padding + boxHeight, 0xAA000000);

        // Draw title
        drawContext.drawText(client.textRenderer, Text.literal(title), x, y, 0xFFFF55, false);

        // Draw stat lines
        int textY = y + titleHeight;
        for (String line : lines) {
            drawContext.drawText(client.textRenderer, Text.literal(line), x, textY, 0xFFFFFF, false);
            textY += lineHeight;
        }
    }

    public static int getWidth(MinecraftClient client) {
        int padding = 6;
        String[] lines = {
                "Kills: 000",
                "Deaths: 000",
                "Void Deaths: 000",
                "K/D: 00.00"
        };

        int maxWidth = client.textRenderer.getWidth("Your Stats:");
        for (String line : lines) {
            int width = client.textRenderer.getWidth(line);
            if (width > maxWidth) maxWidth = width;
        }

        return maxWidth + padding * 2;
    }

    public static int getHeight() {
        return 12 + (4 * 10) + 12; // title + 4 lines + padding
    }

}
