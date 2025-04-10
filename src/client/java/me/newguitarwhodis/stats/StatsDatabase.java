package me.newguitarwhodis.stats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StatsDatabase {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type MAP_TYPE = new TypeToken<Map<String, PlayerStats>>() {}.getType();
    private static final Path FILE_PATH = Path.of("config", "player_stats.json");

    private static Map<String, PlayerStats> database = new HashMap<>();

    public static void load() {
        try {
            if (Files.exists(FILE_PATH)) {
                try (Reader reader = Files.newBufferedReader(FILE_PATH)) {
                    database = GSON.fromJson(reader, MAP_TYPE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(FILE_PATH)) {
                GSON.toJson(database, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerStats get(String playerName) {
        return database.computeIfAbsent(playerName, k -> new PlayerStats());
    }

    public static Map<String, PlayerStats> getAll() {
        return database;
    }
}