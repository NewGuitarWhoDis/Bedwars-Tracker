package me.newguitarwhodis.ui;

import me.newguitarwhodis.stats.PlayerStats;
import me.newguitarwhodis.stats.StatsDatabase;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.Map;

public class FullStatsScreen extends Screen {
    private StatsListWidget listWidget;
    private TextFieldWidget searchBox;

    public FullStatsScreen() {
        super(Text.literal("All Player Stats"));
    }

    private void filterListBySearch(String query) {
        listWidget.clear();

        for (Map.Entry<String, PlayerStats> entry : StatsDatabase.getAll().entrySet()) {
            String name = entry.getKey();
            if (query == null || query.isEmpty() || name.toLowerCase().contains(query.toLowerCase())) {
                listWidget.addStatEntry(name, entry.getValue());
            }
        }
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return searchBox.charTyped(chr, keyCode) || super.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return searchBox.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    protected void init() {
        int top = 75;

        listWidget = new StatsListWidget(this.client, this.width, this.height - 115, top, 14, 2);
        this.addSelectableChild(listWidget);

        for (Map.Entry<String, PlayerStats> entry : StatsDatabase.getAll().entrySet()) {
            listWidget.addStatEntry(entry.getKey(), entry.getValue());
        }

        // Back button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), btn -> {
            assert this.client != null;
            this.client.setScreen(new StatScreen());
        }).dimensions(10, this.height - 30, 80, 20).build());

        int boxWidth = 120;
        int boxHeight = 20;
        int x = this.width - boxWidth - 10;
        int y = this.height - boxHeight - 10;

        searchBox = new TextFieldWidget(this.textRenderer, x, y, boxWidth, boxHeight, Text.literal("Search..."));
        searchBox.setMaxLength(50);
        searchBox.setChangedListener(this::filterListBySearch); // live update as user types

        this.addDrawableChild(searchBox);


        int buttonY = 50;
        int buttonWidth = 60;

        int currentX = 10;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Name"), b -> {
            listWidget.sortBy(Comparator.comparing(entry -> entry.name.toLowerCase()));
        }).dimensions(currentX, buttonY, buttonWidth, 20).build());
        currentX += 175;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Kills"), b -> {
            listWidget.sortBy(Comparator.comparingInt(entry -> -entry.stats.totalKills));
        }).dimensions(currentX, buttonY, buttonWidth, 20).build());
        currentX += 70;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Deaths"), b -> {
            listWidget.sortBy(Comparator.comparingInt(entry -> -entry.stats.totalDeaths));
        }).dimensions(currentX, buttonY, buttonWidth, 20).build());
        currentX += 80;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Void"), b -> {
            listWidget.sortBy(Comparator.comparingInt(entry -> -entry.stats.voidDeaths));
        }).dimensions(currentX, buttonY, buttonWidth, 20).build());
        currentX += 70;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("K/D"), b -> {
            listWidget.sortBy((a, b2) -> {
                double kdA = a.stats.totalDeaths == 0 ? a.stats.totalKills : (double) a.stats.totalKills / a.stats.totalDeaths;
                double kdB = b2.stats.totalDeaths == 0 ? b2.stats.totalKills : (double) b2.stats.totalKills / b2.stats.totalDeaths;
                return Double.compare(kdB, kdA);
            });
        }).dimensions(currentX, buttonY, buttonWidth, 20).build());
        currentX += 100;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Killed You"), b -> {
            listWidget.sortBy(Comparator.comparingInt(entry -> -entry.stats.killsOnYou));
        }).dimensions(currentX, buttonY, buttonWidth + 10, 20).build());
        currentX += 85;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("You Killed"), b -> {
            listWidget.sortBy(Comparator.comparingInt(entry -> -entry.stats.deathsToYou));
        }).dimensions(currentX, buttonY, buttonWidth + 10, 20).build());

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        listWidget.render(context, mouseX, mouseY, delta);

        // Title
        String title = "All Player Stats";
        int titleX = (this.width - this.textRenderer.getWidth(title)) / 2;
        context.drawText(this.textRenderer, Text.literal(title), titleX, 20, 0xFFFFFF, false);
    }
}
