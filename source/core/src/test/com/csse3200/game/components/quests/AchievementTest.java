package com.csse3200.game.components.quests;

import com.badlogic.gdx.utils.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AchievementTest {

    private Achievement achievement;

    @BeforeEach
    void setUp() {
        achievement = new Achievement("Quest 1", "Defeat 10 enemies", false, false, Achievement.AchievementType.ENEMY, "icons/enemy.png");
    }

    @Test
    void testGetQuestName() {
        assertEquals("Quest 1", achievement.getQuestName());
    }

    @Test
    void testGetQuestDescription() {
        assertEquals("Defeat 10 enemies", achievement.getQuestDescription());
    }

    @Test
    void testIsCompleted() {
        assertFalse(achievement.isCompleted());
        achievement.complete();
        assertTrue(achievement.isCompleted());
    }

    @Test
    void testComplete() {
        achievement.complete();
        assertTrue(achievement.isCompleted());
    }

    @Test
    void testIsSeen() {
        assertFalse(achievement.isSeen());
        achievement.setSeen();
        assertTrue(achievement.isSeen());
    }

    @Test
    void testSetSeen() {
        achievement.setSeen();
        assertTrue(achievement.isSeen());
    }

    @Test
    void testGetType() {
        assertEquals(Achievement.AchievementType.ENEMY, achievement.getType());
    }

    @Test
    void testGetIconPath() {
        assertEquals("icons/enemy.png", achievement.getPath());
    }

    @Test
    void testSerialization() {
        Json json = new Json();
        String serializedAchievement = json.toJson(achievement);

        Achievement deserializedAchievement = json.fromJson(Achievement.class, serializedAchievement);

        assertEquals(achievement.getQuestName(), deserializedAchievement.getQuestName());
        assertEquals(achievement.getQuestDescription(), deserializedAchievement.getQuestDescription());
        assertEquals(achievement.isCompleted(), deserializedAchievement.isCompleted());
        assertEquals(achievement.isSeen(), deserializedAchievement.isSeen());
        assertEquals(achievement.getType(), deserializedAchievement.getType());
        assertEquals(achievement.getPath(), deserializedAchievement.getPath());
    }

    @Test
    void testDeserialization() {
        Json json = new Json();
        String jsonData = "{\"questName\":\"Quest 2\",\"questDescription\":\"Collect 5 items\",\"completed\":true,\"seen\":false,\"type\":\"ITEM\",\"iconPath\":\"icons/item.png\"}";

        Achievement deserializedAchievement = json.fromJson(Achievement.class, jsonData);

        assertEquals("Quest 2", deserializedAchievement.getQuestName());
        assertEquals("Collect 5 items", deserializedAchievement.getQuestDescription());
        assertTrue(deserializedAchievement.isCompleted());
        assertFalse(deserializedAchievement.isSeen());
        assertEquals(Achievement.AchievementType.ITEM, deserializedAchievement.getType());
        assertEquals("icons/item.png", deserializedAchievement.getPath());
    }
}
