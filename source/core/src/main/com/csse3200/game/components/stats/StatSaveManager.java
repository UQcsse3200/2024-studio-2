package com.csse3200.game.components.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class StatSaveManager {
    private static final String SAVE_PATH = "saves/endgameStats.json";
    private Array<Stat> stats;


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
