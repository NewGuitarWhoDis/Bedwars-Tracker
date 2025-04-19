package me.newguitarwhodis.widgets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages widget positions including saved data and temporary overrides.
 */
public class WidgetPosition {

    private static final Gson GSON = new Gson();
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "widget_positions.json");

    public static final Map<String, int[]> positions = new HashMap<>();
    private static final Map<String, int[]> overrides = new HashMap<>();

    static {
        // Default positions
        positions.put("your_stats", new int[]{10, 10});
        positions.put("top_player", new int[]{10, 80});

        load(); // Load saved positions from disk
    }

    public static int[] get(String key) {
        return overrides.getOrDefault(key, positions.getOrDefault(key, new int[]{10, 10}));
    }

    public static void set(String key, int x, int y) {
        positions.put(key, new int[]{x, y});
        save();
    }

    public static void override(String key, int[] pos) {
        overrides.put(key, pos);
    }

    public static void clearOverrides() {
        overrides.clear();
    }

    /**
     * Saves widget positions to disk as JSON.
     */
    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(positions, writer);
        } catch (Exception e) {
            System.err.println("Failed to save widget positions: " + e.getMessage());
        }
    }

    /**
     * Loads widget positions from disk.
     */
    public static void load() {
        if (!FILE.exists()) return;

        try (FileReader reader = new FileReader(FILE)) {
            Type type = new TypeToken<Map<String, int[]>>() {}.getType();
            Map<String, int[]> loaded = GSON.fromJson(reader, type);
            if (loaded != null) {
                positions.putAll(loaded);
            }
        } catch (Exception e) {
            System.err.println("Failed to load widget positions: " + e.getMessage());
        }
    }

    /**
     * Resets positions to default and saves.
     */
    public static void reset() {
        positions.clear();
        positions.put("your_stats", new int[]{10, 10});
        positions.put("top_player", new int[]{10, 80});
        save();
    }
}
