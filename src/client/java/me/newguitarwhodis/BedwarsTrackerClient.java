package me.newguitarwhodis;

import me.newguitarwhodis.chatmanager.ChatHook;
import me.newguitarwhodis.keymapper.KeyManager;
import me.newguitarwhodis.playermanager.PlayerList;
import me.newguitarwhodis.stats.StatsDatabase;
import me.newguitarwhodis.ui.StatScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BedwarsTrackerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		StatsDatabase.load();
		PlayerList.printTabListPlayers();
		KeyManager.register();

		Runtime.getRuntime().addShutdownHook(new Thread(StatsDatabase::save));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyManager.OPEN_GUI.wasPressed()) {
				client.setScreen(new StatScreen());
			}
		});
	}
}