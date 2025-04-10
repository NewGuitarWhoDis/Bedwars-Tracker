package me.newguitarwhodis.chatmanager;

import me.newguitarwhodis.BedwarsTracker;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import me.newguitarwhodis.stats.StatsDatabase;
import me.newguitarwhodis.stats.PlayerStats;
import net.minecraft.text.Text;

public class ChatHook {

    public static void register() {
        // Keep this if you want to catch any system messages that DO come through
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionSide) -> {
            handleKillMessage(message.getString());
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

//                MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(
//                        Text.literal("💀 Victim: " + victim + ", Killer: " + killer)
//                );

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

                StatsDatabase.save();
            }
        }
    }
}
