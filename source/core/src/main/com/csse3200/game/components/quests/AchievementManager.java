package com.csse3200.game.components.quests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.gamestate.Achievements;
import com.csse3200.game.gamestate.data.AchievementSave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Manages the achievements in the game by loading them from a config file,
 */
public class AchievementManager {
    private List<Achievement> achievements;

    // Default constructor
    AchievementManager(){
        achievements = Achievements.achievements.achievementList;
    }

    /**
     * Checks if the given list of achievements contains a specific achievement.
     * @param achievements The list of achievements to search in.
     * @param name The name of the quest to search for.
     * @return True if the achievement is found, false otherwise.
     */
    public boolean containsAchievement(Array<Achievement> achievements, String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getQuestName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the current save (list of all achievements).
     * @return achievements An Array of Achievement objects representing all achievements.
     */
    public List<Achievement>getAchievements() {
        return achievements;
    }

    /**
     * Saves the provided list of achievements to the save 'saves/achievements.json'.
     * @param achievements The list of achievements to be saved.
     */
    public static void saveAchievements(Array<Achievement> achievements, String savePath) {
        Logger logger = LoggerFactory.getLogger(AchievementManager.class);
        logger.info("saving achievement");
        Json json = new Json();
        FileHandle saveFile = Gdx.files.local(savePath);

        // Serialize the Array<Achievement> and write to file
        saveFile.writeString(json.prettyPrint(achievements), false);
    }
}
