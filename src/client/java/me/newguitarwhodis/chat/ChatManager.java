package me.newguitarwhodis.chat;

import me.newguitarwhodis.ui.notifications.ScreenNotificationRenderer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import me.newguitarwhodis.database.StatsDatabase;
import me.newguitarwhodis.database.PlayerStats;
import net.minecraft.text.Text;

public class ChatManager {

    public static void register() {
        // Keep this if you want to catch any system messages that DO come through
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionSide) -> {
            handleKillMessage(message.getString());
            manageGameStart(message.getString());
        });
    }

    public static void handleKillMessage(String rawMessage) {
        String raw = rawMessage.trim();

        if (raw.endsWith(".")) {
            String[] words = raw.split(" ");
            if (words.length >= 3) {
                String victim = words[0];
                String killer = words[words.length - 1].replace(".", "");
                String local = MinecraftClient.getInstance().player.getName().getString();

                if (killer.contains("§k") || victim.contains("§k")) return;

//                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
//                        Text.literal("💀 Victim: " + victim + ", Killer: " + killer)
//                );

                if (isOnlinePlayer(killer) && isOnlinePlayer(victim)) {
                    // Always update killer/victim totals
                    PlayerStats killerStats = StatsDatabase.get(killer);
                    killerStats.totalKills++;

                    PlayerStats victimStats = StatsDatabase.get(victim);
                    victimStats.totalDeaths++;

                    // Only update "kills on you" and "deaths to you" if the local player is involved
                    if (victim.equals(local)) {
                        killerStats.killsOnYou++;
                    } else if (killer.equals(local)) {
                        victimStats.deathsToYou++;
                    }

//                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
//                            Text.literal("💀 Victim: " + victim + ", Killer: " + killer)
//                    );

                    StatsDatabase.save();
                }
                else if (isOnlinePlayer(victim) &&  (killer.equals("void"))) {
                    PlayerStats victimStats = StatsDatabase.get(victim);
                    victimStats.voidDeaths++;
                    StatsDatabase.save();
                }
                else {
//                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
//                        Text.literal("not a player name " + killer + " " + victim)
//                    );
                }
            }
        } else if (raw.endsWith("!")) {
            String[] words = raw.split(" ");
            if (words.length >= 3) {
                String victim = words[0];
                String killer = words[words.length - 3].replace(".", "");
                String local = MinecraftClient.getInstance().player.getName().getString();

                if (isOnlinePlayer(killer) && isOnlinePlayer(victim)) {
                    // Always update killer/victim totals
                    PlayerStats killerStats = StatsDatabase.get(killer);
                    killerStats.totalKills++;

                    PlayerStats victimStats = StatsDatabase.get(victim);
                    victimStats.totalDeaths++;

                    // Only update "kills on you" and "deaths to you" if the local player is involved
                    if (victim.equals(local)) {
                        killerStats.killsOnYou++;
                    } else if (killer.equals(local)) {
                        victimStats.deathsToYou++;
                    }
                }
            }
        }
    }

    public static void manageGameStart(String rawMessage) {
        MinecraftClient client = MinecraftClient.getInstance();
        String raw = rawMessage.trim();
        if (raw.contains("The game starts in 1 second!")) {

            // Run the task after 1 second (20 ticks)

            // FIX THIS AT SOME POINT IT CAUSES NETWORK ERRORS

//            client.execute(() -> {
//                try {
//                    Thread.sleep(2000); // wait 1 second
//                } catch (InterruptedException ignored) {
//                }
//
//                if (client.getNetworkHandler() == null || client.player == null) return;
//
//                StringBuilder knownPlayers = new StringBuilder();
//                for (var entry : client.getNetworkHandler().getPlayerList()) {
//                    String name = entry.getProfile().getName();
//                    if (!name.equals(client.player.getName().getString()) && StatsDatabase.contains(name)) {
//                        if (!knownPlayers.isEmpty()) knownPlayers.append(", ");
//                        knownPlayers.append(name);
//                    }
//                }
//
//                if (!knownPlayers.isEmpty()) {
//                    String title = "You have played against:";
//                    String message = knownPlayers.toString();
//
//                    // Display notification via your notification renderer
//                    ScreenNotificationRenderer.INSTANCE
//                            .show(100, title, message, null);
//                }
//            });
        }
    }


    public static boolean isOnlinePlayer(String name) {
        return MinecraftClient.getInstance().getNetworkHandler().getPlayerList().stream()
                .anyMatch(entry -> entry.getProfile().getName().equalsIgnoreCase(name));
    }
}
