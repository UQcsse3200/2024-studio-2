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

        // Load end game stats from config
        FileHandle configFile = Gdx.files.local(CONFIG_PATH);
        Array<Stat> configStats = json.fromJson(Array.class, Stat.class, configFile);

        // Check if save file exists and is not empty
        FileHandle saveFile = Gdx.files.local(SAVE_PATH);
        if (!saveFile.exists() || saveFile.length() == 0) {
            // Copy config stats to save file
            saveFile.writeString(json.prettyPrint(configStats), false);
        } else {
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

    private boolean containsStat(Array<Stat> stats, String name) {
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


}
