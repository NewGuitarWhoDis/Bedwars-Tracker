package me.newguitarwhodis.ui.screens;

import me.newguitarwhodis.ui.HudWidgets.WidgetManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Screen for toggling widget visibility and opening the layout editor.
 */
public class WidgetManagerScreen extends Screen {

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 24;

    public WidgetManagerScreen() {
        super(Text.literal("Widgets"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 4;
        int y = startY;

        // Toggle "Your Stats" widget
        addToggleButton("Your Stats", "showStats", WidgetManager.showStats, centerX, y);
        y += BUTTON_SPACING;

        // Toggle "Top Player" widget
        addToggleButton("Top Player", "showTopPlayer", WidgetManager.showTopPlayer, centerX, y);
        y += BUTTON_SPACING;

        // Open layout screen
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Widget Layout"),
                button -> {
                    assert this.client != null;
                    this.client.setScreen(new WidgetLayoutScreen());
                }
        ).dimensions(centerX - BUTTON_WIDTH / 2, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    /**
     * Adds a toggle button that controls a boolean field in WidgetManager.
     */
    private void addToggleButton(String label, String key, boolean initialState, int centerX, int y) {
        Text initialText = Text.literal(label + ": " + (initialState ? "ON" : "OFF"));

        this.addDrawableChild(ButtonWidget.builder(
                initialText,
                button -> {
                    boolean newState = !initialState;
                    WidgetManager.set(key, newState);
                    button.setMessage(Text.literal(label + ": " + (newState ? "ON" : "OFF")));
                    // Also update in-memory value for toggling again
                    if (key.equals("showStats")) WidgetManager.showStats = newState;
                    if (key.equals("showTopPlayer")) WidgetManager.showTopPlayer = newState;
                }
        ).dimensions(centerX - BUTTON_WIDTH / 2, y, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        String title = "Widgets";
        int titleX = (this.width - this.textRenderer.getWidth(title)) / 2;
        context.drawText(this.textRenderer, Text.literal(title), titleX, 20, 0xFFFFFF, false);
    }
}
