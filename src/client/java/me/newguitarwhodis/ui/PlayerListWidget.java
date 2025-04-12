package me.newguitarwhodis.ui;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.realms.dto.PlayerInfo;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.UUID;

public class PlayerListWidget extends EntryListWidget<PlayerListWidget.PlayerEntry> {
    private final MinecraftClient client;

    public PlayerListWidget(MinecraftClient client, int width, int height, int top, int headerHeight, int itemHeight) {
        super(client, width, height, top, itemHeight, headerHeight);
        this.client = client;
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        // Accessibility support (optional)
    }

    // Updated method to support all 5 stats
    public void addPlayer(String name, int killsOnYou, int deathsToYou, int totalKills, int totalDeaths, int voidDeaths, String note) {
        this.addEntry(new PlayerEntry(name, killsOnYou, deathsToYou, totalKills, totalDeaths, voidDeaths, note));
    }

    public void sortByKD() {
        this.children().sort(Comparator.comparingDouble(entry -> -((PlayerEntry) entry).getKD()));
    }

    @Override
    public int getRowWidth() {
        return this.width - 40; // Wider rows
    }


    public class PlayerEntry extends Entry<PlayerEntry> {
        private final String name;
        private final int killsOnYou;
        private final int deathsToYou;
        private final int totalKills;
        private final int totalDeaths;
        private final int voidDeaths;
        private final String note;
        public PlayerEntry(String name, int killsOnYou, int deathsToYou, int totalKills, int totalDeaths, int voidDeaths, String note) {
            this.name = name;
            this.killsOnYou = killsOnYou;
            this.deathsToYou = deathsToYou;
            this.totalKills = totalKills;
            this.totalDeaths = totalDeaths;
            this.voidDeaths = voidDeaths;
            this.note = note;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean hovered, float tickDelta) {

            double kd = (totalDeaths == 0) ? totalKills : (double) totalKills / totalDeaths;
            int kdColor = kd >= 2.0 ? 0x55FF55 : kd >= 1.0 ? 0xFFFF55 : 0xFF5555;

            // Background box
            int boxPadding = 8;
            int boxLeft = x - boxPadding;
            int boxRight = x + entryWidth + boxPadding;
            context.fill(boxLeft, y, boxRight, y + entryHeight, 0x66000000);

            // Title row (name)
            context.drawText(client.textRenderer, Text.literal(name).formatted(Formatting.BOLD), boxLeft + 10, y + 4, 0xFFFFFF, false);

            // Divide the full row into columns
            int colCount = 6;
            int colSpacing = entryWidth / colCount;
            int statY1 = y + 18;
            int statY2 = y + 30;

            context.drawText(client.textRenderer, Text.literal("Kills on You: " + killsOnYou), x + 0 * colSpacing + 8, statY1, 0xFF5555, false);
            context.drawText(client.textRenderer, Text.literal("Deaths to You: " + deathsToYou), x + 1 * colSpacing + 8, statY1, 0x55FF55, false);
            context.drawText(client.textRenderer, Text.literal("Void Deaths: " + voidDeaths), x + 2 * colSpacing + 8, statY1, 0x6C3BAA, false);

            context.drawText(client.textRenderer, Text.literal("Total Kills: " + totalKills), x + 0 * colSpacing + 8, statY2, 0xAAAAFF, false);
            context.drawText(client.textRenderer, Text.literal("Total Deaths: " + totalDeaths), x + 1 * colSpacing + 8, statY2, 0xAAAAFF, false);
            context.drawText(client.textRenderer, Text.literal("K/D: " + String.format("%.2f", kd)), x + 2 * colSpacing + 8, statY2, kdColor, false);

            if (hovered && note != null && !note.isEmpty()) {
                context.drawTooltip(client.textRenderer, Text.literal(note), mouseX, mouseY);
            }
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) { // left click
                MinecraftClient.getInstance().setScreen(new NoteScreen(name));
                return true;
            }
            return false;
        }

        public double getKD() {
            return (totalDeaths == 0) ? totalKills : (double) totalKills / totalDeaths;
        }
    }
}
