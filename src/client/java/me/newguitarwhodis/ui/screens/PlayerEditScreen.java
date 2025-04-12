package me.newguitarwhodis.ui.screens;

import me.newguitarwhodis.database.StatsDatabase;
import me.newguitarwhodis.database.PlayerStats;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class PlayerEditScreen extends Screen {
    private final String target;
    private final String returnScreen;
    private final String currentNote;
    public PlayerEditScreen(String name, String screen, String currentNote) {

        super(Text.literal("Note for " + name));
        this.currentNote = currentNote;
        this.returnScreen = screen;
        this.target = name;
    }

    @Override
    protected void init() {
        TextFieldWidget textBox;

        textBox = new TextFieldWidget(
                this.textRenderer,          // text renderer
                this.width / 2 - 115,       // x position
                this.height / 2  - 30,           // y position
                230,                        // width
                20,                         // height
                Text.literal("Note")        // optional placeholder (label)
        );

        textBox.setText(currentNote); // Or preload existing note text

        this.addDrawableChild(textBox);

        // Buttons

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
            String Chosennote = textBox.getText();

            PlayerStats targetStats = StatsDatabase.get(target);
            targetStats.note = Chosennote;

            if(returnScreen == "stats") {
                this.client.setScreen(new InGameStatsScreen());
            }
            else if(returnScreen == "full") {
                this.client.setScreen(new DatabaseScreen());
            }

        }).dimensions(this.width / 2 + 15, this.height / 2 + 20, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> {

            if(returnScreen == "stats") {
                this.client.setScreen(new InGameStatsScreen());
            }
            else if(returnScreen == "full") {
                this.client.setScreen(new DatabaseScreen());
            }

        }).dimensions(this.width / 2 - 115, this.height / 2 + 20 , 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Centered title
        int x = (this.width - this.textRenderer.getWidth(this.title)) / 2;
        context.drawText(this.textRenderer, this.title, x, 20, 0xFFFFFF, false);
    }
}
