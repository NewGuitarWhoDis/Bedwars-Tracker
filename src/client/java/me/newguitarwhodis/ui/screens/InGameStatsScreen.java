package me.newguitarwhodis.ui.screens;

import me.newguitarwhodis.database.StatsDatabase;
import me.newguitarwhodis.database.PlayerStats;
import me.newguitarwhodis.ui.components.PlayerListWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

public class InGameStatsScreen extends Screen {
    private PlayerListWidget playerList;

    public InGameStatsScreen() {
        super(Text.literal("Bedwars Stats Tracker"));
    }

    @Override
    protected void init() {
        playerList = new PlayerListWidget(this.client, this.width, this.height-80, 40, 2, 48);

        String localName = client.player.getName().getString();

        if (client.getNetworkHandler() != null) {
            for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
                String playerName = entry.getProfile().getName();

                // Skip yourself
                if (playerName.equals(localName)) continue;

                // Get saved stats from the database
                PlayerStats stats = StatsDatabase.get(playerName);

                // Add to list with real stats
                playerList.addPlayer(playerName, stats.killsOnYou, stats.deathsToYou, stats.totalKills, stats.totalDeaths, stats.voidDeaths, stats.note);
            }
        }

        playerList.sortByKD();
        this.addSelectableChild(playerList);

        // Database Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Full Stats"), button -> {
            assert this.client != null;
            this.client.setScreen(new DatabaseScreen());
        }).dimensions(this.width - 110, this.height - 30, 100, 20).build());

        // Settings Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Settings"), button -> {
            assert this.client != null;
            this.client.setScreen(new SettingsScreen());
        }).dimensions(this.width - 220, this.height - 30, 100, 20).build());

        // Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> {
            if (this.client != null) {
                this.client.setScreen(null);
            }
        }).dimensions(10, this.height - 30, 80, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        playerList.render(context, mouseX, mouseY, delta);

        // Centered title
        int x = (this.width - this.textRenderer.getWidth(this.title)) / 2;
        context.drawText(this.textRenderer, this.title, x, 20, 0xFFFFFF, false);

        drawSelfOverlay(context);
    }

    private void drawSelfOverlay(DrawContext context) {
        String localName = this.client.player.getName().getString();
        PlayerStats stats = StatsDatabase.get(localName);
        double kd = stats.totalDeaths == 0 ? stats.totalKills : (double) stats.totalKills / stats.totalDeaths;
        int kdColor = kd >= 2.0 ? 0x55FF55 : kd >= 1.0 ? 0xFFFF55 : 0xFF5555;

        int boxWidth = 140;
        int boxHeight = 70;
        int x = this.width - boxWidth - 10;
        int y = (this.height - boxHeight) / 2; // ✅ vertically centered

        // Background box
        context.fill(x, y, x + boxWidth, y + boxHeight, 0x66000000);

        // Player name (centered inside the box)
        int nameWidth = this.textRenderer.getWidth(localName);
        int nameX = x + (boxWidth - nameWidth) / 2;
        context.drawText(this.textRenderer, Text.literal(localName), nameX, y + 6, 0xFFFFFF, false);

        // Total stats
        context.drawText(this.textRenderer, Text.literal("Total Kills: " + stats.totalKills), x + 10, y + 24, 0xAAAAFF, false);
        context.drawText(this.textRenderer, Text.literal("Total Deaths: " + stats.totalDeaths), x + 10, y + 36, 0xAAAAFF, false);
        context.drawText(this.textRenderer, Text.literal("K/D: " + String.format("%.2f", kd)), x + 10, y + 48, kdColor, false);
    }
}
