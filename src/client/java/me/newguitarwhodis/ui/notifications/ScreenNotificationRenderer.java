package me.newguitarwhodis.ui.notifications;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ScreenNotificationRenderer {
    public static final ScreenNotificationRenderer INSTANCE = new ScreenNotificationRenderer();

    private String line1 = null;
    private String line2 = null;
    private String line3 = null;
    private long popupExpireTime = 0;

    public void show(int durationTicks, String line1, String line2, String line3) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.popupExpireTime = System.currentTimeMillis() + durationTicks * 50;
    }

    public void render(DrawContext context, MinecraftClient client) {
        if (line1 == null || System.currentTimeMillis() > popupExpireTime) return;

        TextRenderer tr = client.textRenderer;
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // get the longest line width
        String widest = line1;
        if (line2 != null && line2.length() > widest.length()) widest = line2;
        if (line3 != null && line3.length() > widest.length()) widest = line3;

        int boxWidth = tr.getWidth(widest) + 20;
        int boxHeight = 10 + (line2 != null ? 10 : 0) + (line3 != null ? 10 : 0) + 10;

        int x = (width - boxWidth) / 2;
        int y = height - 60;

        context.fill(x, y, x + boxWidth, y + boxHeight, 0xAA000000);

        int textY = y + 5;
        context.drawText(tr, Text.literal(line1), x + 10, textY, 0xFFFFFF, false);
        if (line2 != null) context.drawText(tr, Text.literal(line2), x + 10, textY + 10, 0xCCCCCC, false);
        if (line3 != null) context.drawText(tr, Text.literal(line3), x + 10, textY + 20, 0xAAAAAA, false);
    }
}
