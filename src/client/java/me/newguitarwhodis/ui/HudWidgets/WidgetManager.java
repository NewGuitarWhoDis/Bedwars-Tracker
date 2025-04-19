package me.newguitarwhodis.ui.HudWidgets;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages which HUD widgets are shown.
 */
public class WidgetManager {

    public static boolean showStats = false;
    public static boolean showTopPlayer = false;

    private static final Gson GSON = new Gson();
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "widget_states.json");

    static {
        load();
    }

    /**
     * Loads widget states from disk.
     */
    public static void load() {
        if (!FILE.exists()) return;

        try (FileReader reader = new FileReader(FILE)) {
            Map<String, Boolean> map = GSON.fromJson(reader, Map.class);
            if (map != null) {
                showStats = map.getOrDefault("showStats", false);
                showTopPlayer = map.getOrDefault("showTopPlayer", false);
            }
        } catch (Exception e) {
            System.err.println("Failed to load widget states: " + e.getMessage());
        }
    }

    /**
     * Saves widget states to disk.
     */
    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            Map<String, Boolean> map = new HashMap<>();
            map.put("showStats", showStats);
            map.put("showTopPlayer", showTopPlayer);
            GSON.toJson(map, writer);
        } catch (Exception e) {
            System.err.println("Failed to save widget states: " + e.getMessage());
        }
    }

    /**
     * Sets widget state and saves automatically.
     */
    public static void set(String key, boolean value) {
        switch (key) {
            case "showStats" -> showStats = value;
            case "showTopPlayer" -> showTopPlayer = value;
        }
        save();
    }
}
