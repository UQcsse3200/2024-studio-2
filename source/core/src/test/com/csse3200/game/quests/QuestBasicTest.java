package com.csse3200.game.quests;

import com.csse3200.game.components.quests.QuestBasic;
import com.csse3200.game.components.quests.Task;
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
        quest = new QuestBasic("First Steps", List.of(stepsTask));
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

    @Test
    void taskComplete() { // Simulate completing the task
        for (int i = 0; i < 5; i++) {
            quest.progressQuest(); // Simulate triggering the event
        }
        assertTrue(quest.isQuestCompleted());
        assertEquals("QUEST COMPLETED", quest.getCurrentTaskDescription());
        assertEquals("QUEST COMPLETED", quest.getCurrentTaskHint());
        assertEquals(1, quest.getProgression());
    }

    @Test
    void taskNotCompleted() { // Task should not complete before reaching required triggers
        for (int i = 0; i < 4; i++) {
            quest.progressQuest(); // Simulate triggering the event
        }
        assertFalse(quest.isQuestCompleted());
        assertEquals("Take your first steps", quest.getCurrentTaskDescription());
        assertEquals("Just start moving!", quest.getCurrentTaskHint());
        assertEquals(0, quest.getProgression());
    }

    @Test
    void completeMoreTasksThanExist() { // Ensure task does not increment past completion
        for (int i = 0; i < 5; i++) {
            quest.progressQuest(); // Simulate triggering the event
        }
        quest.progressQuest(); // Extra call should not affect completion
        assertTrue(quest.isQuestCompleted());
        assertEquals("QUEST COMPLETED", quest.getCurrentTaskDescription());
        assertEquals("QUEST COMPLETED", quest.getCurrentTaskHint());
        assertEquals(1, quest.getProgression());
    }
}
