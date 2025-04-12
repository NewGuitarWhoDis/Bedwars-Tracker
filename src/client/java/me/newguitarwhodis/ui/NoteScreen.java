package me.newguitarwhodis.ui;

import me.newguitarwhodis.stats.StatsDatabase;
import me.newguitarwhodis.stats.PlayerStats;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NoteScreen extends Screen {
    private final String target;
    public NoteScreen(String name) {

        super(Text.literal("Note for " + name));
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

        textBox.setText(""); // Or preload existing note text

        this.addDrawableChild(textBox);

        // Buttons

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
            String Chosennote = textBox.getText();

            PlayerStats targetStats = StatsDatabase.get(target);
            targetStats.note = Chosennote;

            this.client.setScreen(new FullStatsScreen());

        }).dimensions(this.width / 2 + 15, this.height / 2 + 20, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> {

            this.client.setScreen(new StatScreen());

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
