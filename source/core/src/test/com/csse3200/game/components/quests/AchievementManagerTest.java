package com.csse3200.game.components.quests;

import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.Achievements;
import com.csse3200.game.gamestate.SaveHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AchievementManagerTest {

    @BeforeEach
    void setup() {
        Achievements.clearState();
    }

    @Test
    void testAchievementsLoaded() {

        SaveHandler.getInstance().load(Achievements.class, "defaultsaves/achievement", FileLoader.Location.INTERNAL);

        AchievementManager achievementManager = new AchievementManager();
        // Load achievements from config

        // Verify that achievements are loaded correctly
        List<Achievement> loadedAchievements = achievementManager.getAchievements();
        assertNotNull(loadedAchievements);
        assertFalse(loadedAchievements.isEmpty());

        assertTrue(containsAchievement(loadedAchievements, "MONKEY"));
    }

    @Test
    void testAchievementsSavedAndLoadedCorrectly() {
        SaveHandler.getInstance().load(Achievements.class, "defaultsaves/achievement", FileLoader.Location.INTERNAL);

        AchievementManager achievementManager = new AchievementManager();
        // Create a new achievement and add it to the manager
        Achievement newAchievement = new Achievement("New Quest", "A new quest description", false, false, Achievement.AchievementType.ITEM, "path/to/icon");
        List<Achievement> achievements = achievementManager.getAchievements();
        achievements.add(newAchievement);

        // Save achievements
        SaveHandler.getInstance().save(Achievements.class, "test/saves/achievement", FileLoader.Location.LOCAL);

        Achievements.clearState();

        SaveHandler.getInstance().load(Achievements.class, "test/saves/achievement", FileLoader.Location.LOCAL);

        // Reinitialize manager to load from saved file
        AchievementManager newAchievementManager = new AchievementManager();
        List<Achievement> loadedAchievements = newAchievementManager.getAchievements();

        // Verify that the new achievement is in the loaded achievements
        assertTrue(containsAchievement(loadedAchievements, newAchievement.getQuestName()));

        SaveHandler.getInstance().delete(Achievements.class, "test/saves/achievement", FileLoader.Location.LOCAL);
    }

    private boolean containsAchievement(List<Achievement> achievements, String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getQuestName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}