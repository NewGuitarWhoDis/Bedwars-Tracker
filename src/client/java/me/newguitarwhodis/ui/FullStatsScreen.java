package me.newguitarwhodis.ui;

import me.newguitarwhodis.stats.PlayerStats;
import me.newguitarwhodis.stats.StatsDatabase;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Map;

public class FullStatsScreen extends Screen {
    public FullStatsScreen() {
        super(Text.literal("All Player Stats"));
    }

    @Override
    protected void init() {
        // Add back button to go to previous screen
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), btn -> {
            assert this.client != null;
            this.client.setScreen(new StatScreen()); // or your main screen
        }).dimensions(this.width / 2 - 40, this.height - 30, 80, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int y = 20;

        String titleText = this.title.getString();
        int titleWidth = this.textRenderer.getWidth(titleText);
        int x = (this.width - titleWidth) / 2;

        context.drawText(this.textRenderer, this.title, x, 8, 0xFFFFFF, false);


        for (Map.Entry<String, PlayerStats> entry : StatsDatabase.getAll().entrySet()) {
            String name = entry.getKey();
            PlayerStats stats = entry.getValue();
            double kd = stats.totalDeaths == 0 ? stats.totalKills : (double) stats.totalKills / stats.totalDeaths;

            String line = String.format("%s - Kills: %d, Deaths: %d, K/D: %.2f",
                    name, stats.totalKills, stats.totalDeaths, kd);

            context.drawText(this.textRenderer, Text.literal(line), 10, y, 0xCCCCCC, false);
            y += 12;
        }
    }
}
