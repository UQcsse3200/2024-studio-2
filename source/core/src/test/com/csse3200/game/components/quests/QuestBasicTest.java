package com.csse3200.game.components.quests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestBasicTest {
    private QuestBasic quest;

    @BeforeEach
    void setUp() {
        // Initialize the task with a required number of triggers
        Task stepsTask = new Task("steps", "Take your first steps", "Just start moving!", 5);

        // Create the quest with the task
        quest = new QuestBasic("First Steps","Take your first steps in this world!", List.of(stepsTask), true,false);
    }

    @Test
    void basicQuestSetup() {
        assertFalse(quest.isQuestCompleted());
        assertEquals("Take your first steps", quest.getCurrentTaskDescription());
        assertEquals("Just start moving!", quest.getCurrentTaskHint());
        assertEquals(0, quest.getProgression());
    }

    @Test
    void getQuestDescription() {
        assertEquals("First Steps", quest.getQuestName());
    }
}
