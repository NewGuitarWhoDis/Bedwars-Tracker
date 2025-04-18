package me.newguitarwhodis;

import me.newguitarwhodis.interactions.KeyboardManager;
import me.newguitarwhodis.database.StatsDatabase;
import me.newguitarwhodis.ui.HudWidgets.TeamStatsWidget;
import me.newguitarwhodis.ui.HudWidgets.TopPlayerWidget;
import me.newguitarwhodis.ui.HudWidgets.YourStatsWidget;
import me.newguitarwhodis.ui.notifications.ScreenNotificationRenderer;
import me.newguitarwhodis.ui.screens.InGameStatsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class BedwarsTrackerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		StatsDatabase.load();
		KeyboardManager.register();

		// Hud Rendering
		HudRenderCallback.EVENT.register(new YourStatsWidget());
		HudRenderCallback.EVENT.register(new TopPlayerWidget());
		HudRenderCallback.EVENT.register(new TeamStatsWidget());

		Runtime.getRuntime().addShutdownHook(new Thread(StatsDatabase::save));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (KeyboardManager.OPEN_GUI.wasPressed()) {
				client.setScreen(new InGameStatsScreen());
			}
		});
	}
}