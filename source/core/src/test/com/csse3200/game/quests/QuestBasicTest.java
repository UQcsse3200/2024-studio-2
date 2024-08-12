package com.csse3200.game.quests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestBasicTest {
    QuestBasic quest = new QuestBasic();

    @Test
    void basicQuestSetup() {
        assertFalse(quest.isQuestCompleted());
        assertEquals("default", quest.getTaskDescriptions());
        assertEquals("default", quest.getTaskHint());
        assertEquals(0, quest.getNumTasksCompleted());
        assertEquals(1, quest.getNumQuestTasks());
    }

    @Test
    void getQuestDescription() {
        assertEquals("BASIC QUEST", quest.getQuestDescription());
    }

    @Test
    void taskComplete() { // Can complete a quest
        quest.taskCompleted();
        assertTrue(quest.isQuestCompleted());
        assertEquals("QUEST COMPLETED", quest.getTaskDescriptions());
        assertEquals("QUEST COMPLETED", quest.getTaskHint());
        assertEquals(1, quest.getNumTasksCompleted());
        assertEquals(1, quest.getNumQuestTasks());
    }

    @Test
    void taskCompleted() { // Cannot complete more tests than exist
        quest.taskCompleted();
        quest.taskCompleted();
        assertTrue(quest.isQuestCompleted());
        assertEquals("QUEST COMPLETED", quest.getTaskDescriptions());
        assertEquals("QUEST COMPLETED", quest.getTaskHint());
        assertEquals(1, quest.getNumTasksCompleted());
        assertEquals(1, quest.getNumQuestTasks());
    }

}