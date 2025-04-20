package me.newguitarwhodis.ui.components;

import com.mojang.authlib.GameProfile;
import me.newguitarwhodis.ui.screens.PlayerEditScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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

    public void addPlayer(String name, int killsOnYou, int deathsToYou, int totalKills, int totalDeaths, int voidDeaths, String note) {
        this.addEntry(new PlayerEntry(name, killsOnYou, deathsToYou, totalKills, totalDeaths, voidDeaths, note));
    }

    public void sortByKD() {
        this.children().sort(Comparator.comparingDouble(entry -> -((PlayerEntry) entry).getKD()));
    }

    @Override
    public int getRowWidth() {
        return this.width - 40;
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

            int padding = 8;
            int boxLeft = x - padding;
            int boxRight = x + entryWidth + padding;
            context.fill(boxLeft, y, boxRight, y + entryHeight, 0x66000000);

            // Load skin
            GameProfile profile = client.getNetworkHandler()
                    .getPlayerList()
                    .stream()
                    .filter(entry -> entry.getProfile().getName().equalsIgnoreCase(name))
                    .map(PlayerListEntry::getProfile)
                    .findFirst()
                    .orElse(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()), name));

            SkinTextures skinTextures = client.getSkinProvider().getSkinTextures(profile);

            // Draw player head
            int headSize = entryHeight - 8; // 4px padding top and bottom
            int headX = boxLeft + 10;
            int headY = y + 4;

            context.drawTexture(texture -> RenderLayer.getGui(), skinTextures.texture(), headX, headY, 8f, 8f, headSize, headSize, 64, 64, 0xFFFFFFFF);
            context.drawTexture(texture -> RenderLayer.getGui(), skinTextures.texture(), headX, headY, 40f, 8f, headSize, headSize, 64, 64, 0xFFFFFFFF);

            // New left edge for text (after head)
            int textX = headX + headSize + 10;

            // Title row (name)
            context.drawText(client.textRenderer, Text.literal(name).formatted(Formatting.BOLD), textX, y + 4, 0xFFFFFF, false);

            // Original layout: 3 stats per row
            int colCount = 6;
            int colSpacing = entryWidth / colCount;
            int statY1 = y + 18;
            int statY2 = y + 30;

            context.drawText(client.textRenderer, Text.literal("Kills on You: " + killsOnYou), textX + 0 * colSpacing, statY1, 0xFF5555, false);
            context.drawText(client.textRenderer, Text.literal("Deaths to You: " + deathsToYou), textX + 1 * colSpacing, statY1, 0x55FF55, false);
            context.drawText(client.textRenderer, Text.literal("Void Deaths: " + voidDeaths), textX + 2 * colSpacing, statY1, 0x6C3BAA, false);

            context.drawText(client.textRenderer, Text.literal("Total Kills: " + totalKills), textX + 0 * colSpacing, statY2, 0xAAAAFF, false);
            context.drawText(client.textRenderer, Text.literal("Total Deaths: " + totalDeaths), textX + 1 * colSpacing, statY2, 0xAAAAFF, false);
            context.drawText(client.textRenderer, Text.literal("K/D: " + String.format("%.2f", kd)), textX + 2 * colSpacing, statY2, kdColor, false);

            if (hovered && note != null && !note.isEmpty()) {
                context.drawTooltip(client.textRenderer, Text.literal(note), mouseX, mouseY);
            }
        }



        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                MinecraftClient.getInstance().setScreen(new PlayerEditScreen(name, "stats", note));
                return true;
            }
            return false;
        }

        public double getKD() {
            return (totalDeaths == 0) ? totalKills : (double) totalKills / totalDeaths;
        }
    }
}
