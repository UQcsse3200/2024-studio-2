package com.csse3200.game.components.quests;

import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestTest {
    private Quest quest;

    @BeforeEach
    void setUp() {
        // Initialize the task with a required number of triggers
        Task stepsTask = new Task("steps", "Take your first steps", "Just start moving!", 5, 0, false, false);

        // Create the quest with the task
        quest = new Quest.QuestBuilder("First Steps")
                .addTask(stepsTask)
                .setDescription("Take your first steps in this world!")
                .setActive(true)
                .build();
    }

    @Test
    void basicQuestSetup() {
        assertFalse(quest.isQuestCompleted());
        assertEquals("First Steps", quest.getQuestName());
        assertEquals("Take your first steps in this world!", quest.getQuestDescription());
        assertEquals("Take your first steps", quest.getCurrentTaskDescription());
        assertEquals("Just start moving!", quest.getCurrentTaskHint());
        assertEquals(0, quest.getProgression());
        assertEquals(1, quest.getNumTasksToComplete());
        assertEquals(1, quest.getNumQuestTasks());
        assertTrue(quest.isActive());

    }

    @Test
    void basicQuestCompletion(){
        Entity player = new Entity();
        quest.progressQuest(player);
        assertTrue(quest.isQuestCompleted());
        assertEquals("First Steps", quest.getQuestName());
        assertEquals("Take your first steps in this world!", quest.getQuestDescription());
        assertNotEquals("QUEST NOT COMPLETED", quest.getCurrentTaskDescription());
        assertNotEquals("QUEST NOT COMPLETED", quest.getCurrentTaskHint());
        assertEquals(1, quest.getProgression());
        assertEquals(0, quest.getNumTasksToComplete());
        assertEquals(1, quest.getNumQuestTasks());
        assertFalse(quest.isActive());
    }

    @Test
    void basicQuestFailure(){
        quest.failQuest();
        assertFalse(quest.isQuestCompleted());
        assertEquals("First Steps", quest.getQuestName());
        assertEquals("Take your first steps in this world!", quest.getQuestDescription());
        assertEquals("Take your first steps", quest.getCurrentTaskDescription());
        assertEquals("Just start moving!", quest.getCurrentTaskHint());
        assertEquals(0, quest.getProgression());
        assertEquals(1, quest.getNumTasksToComplete());
        assertEquals(1, quest.getNumQuestTasks());
        assertTrue(quest.isFailed());
        assertFalse(quest.isActive());
    }
}
