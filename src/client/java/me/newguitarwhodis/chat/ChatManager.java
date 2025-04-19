package me.newguitarwhodis.chat;

import me.newguitarwhodis.ui.notifications.ScreenNotificationRenderer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import me.newguitarwhodis.database.StatsDatabase;
import me.newguitarwhodis.database.PlayerStats;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ChatManager {

    /**
     * Registers the chat event listener that handles kill messages
     * and triggers game start checks.
     */
    public static void register() {
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionSide) -> {
            String raw = message.getString().trim();
            handleKillMessage(raw);
            manageGameStart(raw);
        });
    }

    /**
     * Parses kill messages and updates player statistics accordingly.
     * Handles both "." and "!" terminated messages.
     */
    public static void handleKillMessage(String raw) {
        // Ignore obfuscated (invisible) messages
        if (raw.contains("§k")) return;

        String[] words = raw.split(" ");
        if (words.length < 3) return;

        String local = MinecraftClient.getInstance().player.getName().getString();

        // Kill message with period ending
        if (raw.endsWith(".")) {
            String victim = words[0];
            String killer = words[words.length - 1].replace(".", "");

            if (isOnlinePlayer(killer) && isOnlinePlayer(victim)) {
                updateStats(killer, victim, local);
            } else if (isOnlinePlayer(victim) && killer.equalsIgnoreCase("void")) {
                StatsDatabase.get(victim).voidDeaths++;
                StatsDatabase.save();
            }

            // Kill message with exclamation ending
        } else if (raw.endsWith("!")) {
            String victim = words[0];
            String killer = words[words.length - 3].replace(".", "");

            if (isOnlinePlayer(killer) && isOnlinePlayer(victim)) {
                updateStats(killer, victim, local);
            }
        }
    }

    /**
     * Updates the stats for the killer and victim, including
     * special stats for if the local player was involved.
     */
    private static void updateStats(String killer, String victim, String local) {
        PlayerStats killerStats = StatsDatabase.get(killer);
        PlayerStats victimStats = StatsDatabase.get(victim);

        killerStats.totalKills++;
        victimStats.totalDeaths++;

        if (victim.equals(local)) {
            killerStats.killsOnYou++;
        } else if (killer.equals(local)) {
            victimStats.deathsToYou++;
        }

        StatsDatabase.save();
    }

    /**
     * Handles the message that signals the game is about to start.
     * After a delay, checks which players have been previously encountered.
     * Displays a notification with known names.
     */
    public static void manageGameStart(String raw) {
        if (!raw.contains("The game starts in 1 second!")) return;

        MinecraftClient client = MinecraftClient.getInstance();

        // Delay task by 2 seconds without blocking the main thread
        CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS).execute(() -> {
            if (client.getNetworkHandler() == null || client.player == null) return;

            StringBuilder knownPlayers = new StringBuilder();

            for (var entry : client.getNetworkHandler().getPlayerList()) {
                String name = entry.getProfile().getName();
                if (!name.equals(client.player.getName().getString()) && StatsDatabase.contains(name)) {
                    if (!knownPlayers.isEmpty()) knownPlayers.append(", ");
                    knownPlayers.append(name);
                }
            }

            if (!knownPlayers.isEmpty()) {
                ScreenNotificationRenderer.INSTANCE.show(
                        100,
                        "You have played against:",
                        knownPlayers.toString(),
                        null
                );
            }
        });
    }

    /**
     * Checks whether a given player name is currently online.
     */
    public static boolean isOnlinePlayer(String name) {
        return MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream()
                .anyMatch(entry -> entry.getProfile().getName().equalsIgnoreCase(name));
    }
}
