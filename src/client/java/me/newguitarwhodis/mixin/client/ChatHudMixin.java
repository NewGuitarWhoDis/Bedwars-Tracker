package me.newguitarwhodis.mixin.client;

import me.newguitarwhodis.chat.ChatManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "addMessage", at = @At("HEAD"))
    private void onChatMessage(Text message, CallbackInfo ci) {
        String raw = message.getString();
        ChatManager.handleKillMessage(raw); // reuse your logic!
    }
}
