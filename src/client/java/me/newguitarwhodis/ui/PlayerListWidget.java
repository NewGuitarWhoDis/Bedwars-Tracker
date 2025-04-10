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
    public void addPlayer(String name, int killsOnYou, int deathsToYou, int totalKills, int totalDeaths) {
        this.addEntry(new PlayerEntry(name, killsOnYou, deathsToYou, totalKills, totalDeaths));
    }

    public void sortByKD() {
        this.children().sort(Comparator.comparingDouble(entry -> -((PlayerEntry) entry).getKD()));
    }

    public class PlayerEntry extends Entry<PlayerEntry> {
        private final String name;
        private final int killsOnYou;
        private final int deathsToYou;
        private final int totalKills;
        private final int totalDeaths;

        public PlayerEntry(String name, int killsOnYou, int deathsToYou, int totalKills, int totalDeaths) {
            this.name = name;
            this.killsOnYou = killsOnYou;
            this.deathsToYou = deathsToYou;
            this.totalKills = totalKills;
            this.totalDeaths = totalDeaths;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean hovered, float tickDelta) {

            double kd = (totalDeaths == 0) ? totalKills : (double) totalKills / totalDeaths;
            int kdColor = kd >= 2.0 ? 0x55FF55 : kd >= 1.0 ? 0xFFFF55 : 0xFF5555;

// Padding values
            int horizontalPadding = 8;
            int boxLeft = x - horizontalPadding;
            int boxRight = x + entryWidth + horizontalPadding;

// Background box with padding
            context.fill(boxLeft, y, boxRight, y + entryHeight, 0x66000000);

// Player name (left-aligned inside padded box)
            Text nameText = Text.literal(name).formatted(Formatting.BOLD);
            context.drawText(client.textRenderer, nameText, boxLeft + 10, y + 4, 0xFFFFFF, false);

// Define stat strings
            Text koyText = Text.literal("Kills on You: " + killsOnYou);
            Text dtyText = Text.literal("Deaths to You: " + deathsToYou);
            Text tkText  = Text.literal("Total Kills: " + totalKills);
            Text tdText  = Text.literal("Total Deaths: " + totalDeaths);
            String kdStr = "K/D: " + String.format("%.2f", kd);
            Text kdText  = Text.literal(kdStr);

// Layout settings
            int spacing = 20;
            int row1Y = y + 18;
            int row2Y = y + 30;
            int startX = boxLeft + 10;

// === Row 1 ===
            int textX = startX;
            context.drawText(client.textRenderer, koyText, textX, row1Y, 0xFF5555, false);
            textX += client.textRenderer.getWidth(koyText) + spacing;

            context.drawText(client.textRenderer, dtyText, textX, row1Y, 0x55FF55, false);

// === Row 2 ===
            textX = startX;
            context.drawText(client.textRenderer, tkText, textX, row2Y, 0xAAAAFF, false);
            textX += client.textRenderer.getWidth(tkText) + spacing;

            context.drawText(client.textRenderer, tdText, textX, row2Y, 0xAAAAFF, false);
            textX += client.textRenderer.getWidth(tdText) + spacing;

            context.drawText(client.textRenderer, kdText, textX, row2Y, kdColor, false);

        }


        public double getKD() {
            return (totalDeaths == 0) ? totalKills : (double) totalKills / totalDeaths;
        }
    }
}
