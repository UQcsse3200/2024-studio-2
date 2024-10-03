package com.csse3200.game.components.quests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Manages the achievements in the game by loading them from a config file,
 */
public class AchievementManager {
    private String configPath;
    private String savePath;
    private Array<Achievement> achievements;

    // Default constructor with default paths
    public AchievementManager() {
        this("configs/achievements.json", "saves/achievements.json");
    }

    // Parameterized constructor
    AchievementManager(String configPath, String savePath){
        this.configPath = configPath;
        this.savePath = savePath;
        setup();

    }

    /**
     * Loads achievements from the config and saves them to the save.
     * If the save does not exist or is empty, it initializes it with the config achievements.
     * It also ensures all achievements in the config file are present in the save file.
     */
    public void setup() {
        Json json = new Json();

        // Load achievements from config
        FileHandle configFile = Gdx.files.local(configPath);
        Array<Achievement> configAchievements = json.fromJson(Array.class, Achievement.class, configFile);

        // Check if save file exists and is not empty
        FileHandle saveFile = Gdx.files.local(savePath);
        if (!saveFile.exists() || saveFile.length() == 0) {
            // Copy config achievements to save file
            saveFile.writeString(json.prettyPrint(configAchievements), false);
        }

            // Load achievements from save
            achievements = json.fromJson(Array.class, Achievement.class, saveFile);

            // Ensure all config achievements are in save achievements
            for (Achievement configAchievement : configAchievements) {
                if (!containsAchievement(achievements, configAchievement.getQuestName())) {
                    achievements.add(configAchievement);
                }
            }

            // Save the updated achievements back to the save file
            saveFile.writeString(json.prettyPrint(achievements), false);

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
    public Array<Achievement>getAchievements() {
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
