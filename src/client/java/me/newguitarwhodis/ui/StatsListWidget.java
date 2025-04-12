package me.newguitarwhodis.ui;

import me.newguitarwhodis.stats.PlayerStats;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import java.util.Comparator;
import java.util.Map;

import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;


public class StatsListWidget extends EntryListWidget<StatsListWidget.Entry> {
    public StatsListWidget(MinecraftClient client, int width, int height, int top, int itemHeight, int headerHeight) {
        super(client, width, height, top, itemHeight, headerHeight);
    }

    public void addStatEntry(String name, PlayerStats stats) {
        this.addEntry(new Entry(this.client, name, stats));
    }

    public void sortBy(Comparator<Entry> comparator) {
        this.children().sort(comparator);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    public void reloadAll(Map<String, PlayerStats> allStats) {
        this.clear();
        for (Map.Entry<String, PlayerStats> entry : allStats.entrySet()) {
            this.addStatEntry(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        this.children().clear();
    }

    public static class Entry extends EntryListWidget.Entry<Entry> {
        private final MinecraftClient client;
        final String name;
        final PlayerStats stats;

        public Entry(MinecraftClient client, String name, PlayerStats stats) {
            this.client = client;
            this.name = name;
            this.stats = stats;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            double kd = stats.totalDeaths == 0 ? stats.totalKills : (double) stats.totalKills / stats.totalDeaths;
            int kdColor = kd >= 2.0 ? 0x55FF55 : kd >= 1.0 ? 0xFFFF55 : 0xFF5555;

            int currentX = x + 10; // starting X position with left padding
            int yOffset = y + 3;

            context.drawText(client.textRenderer, Text.literal(name), currentX, yOffset, 0xFFFFFF, false);
            currentX += 175;

            context.drawText(client.textRenderer, Text.literal("Kills: " + stats.totalKills), currentX, yOffset, 0xAAAAFF, false);
            currentX += 70;

            context.drawText(client.textRenderer, Text.literal("Deaths: " + stats.totalDeaths), currentX, yOffset, 0xAAAAFF, false);
            currentX += 80;

            context.drawText(client.textRenderer, Text.literal("Void: " + stats.voidDeaths), currentX, yOffset, 0xAAAAFF, false);
            currentX += 70;

            context.drawText(client.textRenderer, Text.literal("K/D: " + String.format("%.2f", kd)), currentX, yOffset, kdColor, false);
            currentX += 100;

            context.drawText(client.textRenderer, Text.literal("Killed You: " + stats.killsOnYou), currentX, yOffset, 0xFF9999, false);
            currentX += 85;

            context.drawText(client.textRenderer, Text.literal("You Killed: " + stats.deathsToYou), currentX, yOffset, 0x99FF99, false);
            currentX += 90;

            if (stats.note != null && !stats.note.isEmpty()) {
                context.drawText(client.textRenderer, Text.literal("Note: " + stats.note), currentX, yOffset, 0xFFFFFF, false);
            }

            // Optional: hover highlight
            if (hovered) {
                context.fill(x, y, x + width, y + height + 2, 0x22FFFFFF);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) { // left click
                MinecraftClient.getInstance().setScreen(new NoteScreen(name, "full"));
                return true;
            }
            return false;
        }
    }
}
