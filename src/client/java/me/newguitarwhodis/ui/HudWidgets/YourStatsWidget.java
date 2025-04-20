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

    private static String[] cachedLines = {
            "Kills: 000",
            "Deaths: 000",
            "Void Deaths: 000",
            "K/D: 00.00"
    };

    private static final String TITLE = "Your Stats:";

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (!WidgetManager.showStats) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        String playerName = client.player.getName().getString();
        PlayerStats stats = StatsDatabase.get(playerName);

        // Cache real lines for width + rendering
        cachedLines = new String[] {
                "Kills: " + stats.totalKills,
                "Deaths: " + stats.totalDeaths,
                "Void Deaths: " + stats.voidDeaths,
                "K/D: " + String.format("%.2f", stats.totalDeaths == 0
                        ? stats.totalKills
                        : (double) stats.totalKills / stats.totalDeaths)
        };

        int[] pos = WidgetPosition.get("your_stats");
        int x = pos[0];
        int y = pos[1];

        int boxWidth = getWidth(client);
        int boxHeight = getHeight();

        // Draw background box at (x, y)
        context.fill(x, y, x + boxWidth, y + boxHeight, 0xAA000000);

        int textX = x + PADDING;
        int textY = y + PADDING;

        context.drawText(client.textRenderer, Text.literal(TITLE), textX, textY, 0xFFFF55, false);
        textY += TITLE_HEIGHT;

        for (String line : cachedLines) {
            context.drawText(client.textRenderer, Text.literal(line), textX, textY, 0xFFFFFF, false);
            textY += LINE_HEIGHT;
        }
    }

    public static int getWidth(MinecraftClient client) {
        int max = client.textRenderer.getWidth(TITLE);
        for (String line : cachedLines) {
            max = Math.max(max, client.textRenderer.getWidth(line));
        }
        return max + PADDING * 2;
    }

    public static int getHeight() {
        return TITLE_HEIGHT + (cachedLines.length * LINE_HEIGHT) + PADDING * 2;
    }
}
