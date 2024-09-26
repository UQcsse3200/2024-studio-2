package com.csse3200.game.components.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class StatSaveManager {

    private static final String CONFIG_PATH = "configs/endgameStats.json";
    private static final String SAVE_PATH = "saves/endgameStats.json";
    private Array<Stat> stats;

    public StatSaveManager() {
        Json json = new Json();

        // Load endgame stats from config
        FileHandle configFile = Gdx.files.local(CONFIG_PATH);
        Array<Stat> configStats = json.fromJson(Array.class, Stat.class, configFile);

        // Check if the save directory exists, if not create it
        FileHandle saveDirectory = Gdx.files.local("saves/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs(); // Create the directory if it doesn't exist
        }

        // Check if the save file exists
        FileHandle saveFile = Gdx.files.local(SAVE_PATH);
        if (!saveFile.exists() || saveFile.length() == 0) {
            // Save file does not exist or is empty, initialize with config stats
            stats = configStats;
            saveFile.writeString(json.prettyPrint(stats), false);
        }
        else {
            // Load stats from save
            stats = json.fromJson(Array.class, Stat.class, saveFile);

            // Ensure all config stats are in save stats
            for (Stat configStat : configStats) {
                if (!containsStat(stats, configStat.getStatName())) {
                    stats.add(configStat);
                }
            }

            // Save the updated stats back to the save file
            saveFile.writeString(json.prettyPrint(stats), false);
        }
    }

    boolean containsStat(Array<Stat> stats, String name) {
        for (Stat stat : stats) {
            if (stat.getStatName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Array<Stat> getStats() {
        return stats;
    }

    /**
     * Function to save stats to 'saves/endgameStats.json'.
     */
    public static void saveStats(Array<Stat> stats) {
        Json json = new Json();
        FileHandle saveFile = Gdx.files.local(SAVE_PATH);

        // Serialize the Array<Stat> and write to file
        saveFile.writeString(json.prettyPrint(stats), false);
    }
}
