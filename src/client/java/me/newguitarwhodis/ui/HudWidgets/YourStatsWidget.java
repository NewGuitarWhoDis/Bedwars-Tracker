package me.newguitarwhodis.ui.HudWidgets;

import me.newguitarwhodis.database.PlayerStats;
import me.newguitarwhodis.database.StatsDatabase;
import me.newguitarwhodis.widgets.WidgetPosition;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;

/**
 * Renders the "Your Stats" widget on the HUD.
 */
public class YourStatsWidget implements HudRenderCallback {

    private static final int PADDING = 6;
    private static final int LINE_HEIGHT = 10;
    private static final int TITLE_HEIGHT = 12;

    private static final String[] SAMPLE_LINES = {
            "Kills: 000",
            "Deaths: 000",
            "Void Deaths: 000",
            "K/D: 00.00"
    };

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (!WidgetManager.showStats) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        String name = client.player.getName().getString();
        PlayerStats stats = StatsDatabase.get(name);

        int[] pos = WidgetPosition.get("your_stats");
        int x = pos[0];
        int y = pos[1];

        String title = "Your Stats:";
        String[] lines = {
                "Kills: " + stats.totalKills,
                "Deaths: " + stats.totalDeaths,
                "Void Deaths: " + stats.voidDeaths,
                "K/D: " + String.format("%.2f", stats.totalDeaths == 0
                        ? stats.totalKills
                        : (double) stats.totalKills / stats.totalDeaths)
        };

        int maxWidth = getMaxLineWidth(client, lines, title);
        int boxWidth = maxWidth + PADDING * 2;
        int boxHeight = TITLE_HEIGHT + (lines.length * LINE_HEIGHT) + PADDING * 2;

        context.fill(x, y, x + boxWidth, y + boxHeight, 0xAA000000);

        int textX = x + PADDING;
        int textY = y + PADDING;

        context.drawText(client.textRenderer, Text.literal(title), textX, textY, 0xFFFF55, false);
        textY += TITLE_HEIGHT;

        for (String line : lines) {
            context.drawText(client.textRenderer, Text.literal(line), textX, textY, 0xFFFFFF, false);
            textY += LINE_HEIGHT;
        }
    }

    public static int getWidth(MinecraftClient client) {
        return getMaxLineWidth(client, SAMPLE_LINES, "Your Stats:") + PADDING * 2;
    }

    public static int getHeight() {
        return TITLE_HEIGHT + (4 * LINE_HEIGHT) + PADDING * 2;
    }

    private static int getMaxLineWidth(MinecraftClient client, String[] lines, String title) {
        int maxWidth = client.textRenderer.getWidth(title);
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, client.textRenderer.getWidth(line));
        }
        return maxWidth;
    }
}
