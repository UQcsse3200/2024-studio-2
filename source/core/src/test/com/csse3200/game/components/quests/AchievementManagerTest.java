package com.csse3200.game.components.quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AchievementManagerTest {

    private static final String CONFIG_PATH = "configs/achievements.json";
    private static final String SAVE_PATH = "test/achievements.json";
    private AchievementManager achievementManager;

    @BeforeEach
    void setUp() {
        // Ensure the save file does not exist before each test
        FileHandle saveFile = Gdx.files.local(SAVE_PATH);
        if (saveFile.exists()) {
            saveFile.delete();
        }

        // Initialize the AchievementManager
        achievementManager = new AchievementManager(CONFIG_PATH, SAVE_PATH);
    }

    @Test
    void testAchievementsLoaded() {
        // Load achievements from config
        Json json = new Json();
        FileHandle configFile = Gdx.files.internal(CONFIG_PATH);
        Array<Achievement> configAchievements = json.fromJson(Array.class, Achievement.class, configFile);

        // Verify that achievements are loaded correctly
        Array<Achievement> loadedAchievements = achievementManager.getAchievements();
        assertNotNull(loadedAchievements);
        assertFalse(loadedAchievements.isEmpty());
        assertEquals(configAchievements.size, loadedAchievements.size);

        for (Achievement achievement : configAchievements) {
            assertTrue(containsAchievement(loadedAchievements, achievement.getQuestName()));
        }
    }

    @Test
    void testAchievementsSavedAndLoadedCorrectly() {
        // Create a new achievement and add it to the manager
        Achievement newAchievement = new Achievement("New Quest", "A new quest description", false, false, Achievement.AchievementType.ITEM, "path/to/icon");
        Array<Achievement> achievements = achievementManager.getAchievements();
        achievements.add(newAchievement);

        // Save achievements
        AchievementManager.saveAchievements(achievements, SAVE_PATH);

        // Reinitialize manager to load from saved file
        AchievementManager newAchievementManager = new AchievementManager(CONFIG_PATH, SAVE_PATH);
        Array<Achievement> loadedAchievements = newAchievementManager.getAchievements();

        // Verify that the new achievement is in the loaded achievements
        assertTrue(containsAchievement(loadedAchievements, newAchievement.getQuestName()));
    }

    private boolean containsAchievement(Array<Achievement> achievements, String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getQuestName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}