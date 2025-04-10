package me.newguitarwhodis.ui;

import me.newguitarwhodis.stats.StatsDatabase;
import me.newguitarwhodis.stats.PlayerStats;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

public class StatScreen extends Screen {
    private PlayerListWidget playerList;

    public StatScreen() {
        super(Text.literal("Bedwars Stats Tracker"));
    }

    @Override
    protected void init() {
        playerList = new PlayerListWidget(this.client, this.width, this.height, 40, 2, 48);

        String localName = client.player.getName().getString();

        if (client.getNetworkHandler() != null) {
            for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
                String playerName = entry.getProfile().getName();

                // Skip yourself
                if (playerName.equals(localName)) continue;

                // Get saved stats from the database
                PlayerStats stats = StatsDatabase.get(playerName);

                // Add to list with real stats
                playerList.addPlayer(playerName, stats.killsOnYou, stats.deathsToYou, stats.totalKills, stats.totalDeaths);
            }
        }

        playerList.sortByKD();
        this.addSelectableChild(playerList);

//        this.addDrawableChild(ButtonWidget.builder(Text.literal("Full Stats"), button -> {
//            assert this.client != null;
//            this.client.setScreen(new FullStatsScreen());
//        }).dimensions(this.width - 110, this.height - 30, 100, 20).build());

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        playerList.render(context, mouseX, mouseY, delta);

        // Centered title
        int x = (this.width - this.textRenderer.getWidth(this.title)) / 2;
        context.drawText(this.textRenderer, this.title, x, 20, 0xFFFFFF, false);
    }
}
