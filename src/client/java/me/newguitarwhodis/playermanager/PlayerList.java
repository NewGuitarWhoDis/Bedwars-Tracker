package me.newguitarwhodis.playermanager;

import me.newguitarwhodis.BedwarsTracker;
import net.minecraft.client.MinecraftClient;

public class PlayerList {

    public static void printTabListPlayers() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null) {
            client.getNetworkHandler().getPlayerList().forEach(player -> {
                String playerName = player.getProfile().getName();
                BedwarsTracker.LOGGER.info("Player: " + playerName);
            });
        } else {
            BedwarsTracker.LOGGER.info("Not on server");
        }
    }
}
