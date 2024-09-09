package com.csse3200.game.components.quests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AchievementManager {
    private static final String CONFIG_PATH = "configs/achievements.json";
    private static final String SAVE_PATH = "saves/achievements.json";
    private Array<Achievement> achievements;

    public AchievementManager() {
        Json json = new Json();

        // Load achievements from config
        FileHandle configFile = Gdx.files.local(CONFIG_PATH);
        Array<Achievement> configAchievements = json.fromJson(Array.class, Achievement.class, configFile);

        // Check if save file exists and is not empty
        FileHandle saveFile = Gdx.files.local(SAVE_PATH);
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

    private boolean containsAchievement(Array<Achievement> achievements, String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getQuestName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Array<Achievement> getAchievements() {
        return achievements;
    }

    /**
     * Function to save achievements to 'saves/achievements.json'.
     */
    public static void saveAchievements(Array<Achievement> achievements) {
        Json json = new Json();
        FileHandle saveFile = Gdx.files.local(SAVE_PATH);

        // Serialize the Array<Achievement> and write to file
        saveFile.writeString(json.prettyPrint(achievements), false);
    }

}
