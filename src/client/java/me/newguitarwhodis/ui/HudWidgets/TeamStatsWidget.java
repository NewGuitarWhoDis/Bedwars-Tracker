package me.newguitarwhodis.ui.HudWidgets;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.SkinTextures;

public class TeamStatsWidget implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {


//        SkinTextures SkinTexture = MinecraftClient.getInstance().player.getSkinTextures();
//        PlayerSkinDrawer.draw(drawContext, SkinTexture, 0, 0, 16);
    }
}
