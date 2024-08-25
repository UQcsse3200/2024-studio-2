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
        quest = new QuestBasic("First Steps","Take your first steps in this world!", List.of(stepsTask),
                true,false,null,null);
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
        assertTrue(quest.isAchievement());
        assertTrue(quest.isActive());
        assertFalse(quest.isSecret());

    }

    @Test
    void basicQuestFalseCompletion(){
        quest.progressQuest();
        assertFalse(quest.isQuestCompleted());
        assertEquals("First Steps", quest.getQuestName());
        assertEquals("Take your first steps in this world!", quest.getQuestDescription());
        assertEquals("QUEST NOT COMPLETED", quest.getCurrentTaskDescription());
        assertEquals("QUEST NOT COMPLETED", quest.getCurrentTaskHint());
        assertEquals(1, quest.getProgression());
        assertEquals(0, quest.getNumTasksToComplete());
        assertEquals(1, quest.getNumQuestTasks());
        assertTrue(quest.isAchievement());
        assertTrue(quest.isActive());
        assertFalse(quest.isSecret());
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
        assertTrue(quest.isAchievement());
        assertFalse(quest.isActive());
        assertFalse(quest.isSecret());
    }
}
