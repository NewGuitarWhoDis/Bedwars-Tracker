package me.newguitarwhodis.widgets;

import java.util.HashMap;
import java.util.Map;

public class WidgetPosition {
    public static final Map<String, int[]> positions = new HashMap<>();

    static {
        positions.put("your_stats", new int[] {10, 10});
        positions.put("top_player", new int[] {10, 80});
    }

    public static int[] get(String key) {
        return positions.getOrDefault(key, new int[] {10, 10});
    }

    public static void set(String key, int x, int y) {
        positions.put(key, new int[] {x, y});
    }
}
