package com.csse3200.game.components.quests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

class QuestHiddenTest {
    private QuestHidden achievement;

    @BeforeEach
    void setUp() {
        // Create the quest with the task
        achievement = new QuestHidden("Test Achievement","Lorem ipsum dolor");
    }

    @Test
    void checkGetters() {
        assertEquals("Test Achievement", achievement.getQuestName());
        assertEquals("Lorem ipsum dolor", achievement.getQuestDescription());
    }

    @Test
    void checkCompleted() {
        assertFalse(achievement.isCompleted());
        achievement.complete();
        assertTrue(achievement.isCompleted());
    }


}
