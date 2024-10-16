package com.csse3200.game.components.quests;

import com.csse3200.game.gamestate.Achievements;
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
    public boolean containsAchievement(List<Achievement> achievements, String name) {
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

}
