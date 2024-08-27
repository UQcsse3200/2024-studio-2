package com.csse3200.game.components.quests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void getTaskName() {
        Task stepsTask = new Task("steps",
                "Take your first steps", "Just start moving!",
                1);
        assertEquals("steps", stepsTask.getTaskName());
    }

    @Test
    void getDescription() {
        Task stepsTask = new Task("steps",
                "Take your first steps", "Just start moving!",
                1);
        assertEquals("Take your first steps", stepsTask.getDescription());
    }

    @Test
    void getHint() {
        Task stepsTask = new Task("steps",
                "Take your first steps", "Just start moving!",
                1);
        assertEquals("Just start moving!", stepsTask.getHint());
    }

    @Test
    void handleEvents() {
        Task stepsTask = new Task("steps",
                "Take your first steps", "Just start moving!",
                1);
        assertFalse(stepsTask.isCompleted());
        assertEquals(0, stepsTask.getTriggerCount());
        stepsTask.handleEvent();
        assertTrue(stepsTask.isCompleted());
        assertEquals(1, stepsTask.getTriggerCount());
    }

    @Test
    void getRequiredTriggers() {
        Task stepsTask = new Task("steps",
                "Take your first steps", "Just start moving!",
                1);
        assertEquals(1, stepsTask.getRequiredTriggers());
    }

    @Test
    void failure() {
        Task stepsTask = new Task("steps",
                "Take your first steps", "Just start moving!",
                1);
        assertFalse(stepsTask.isFailed());
        stepsTask.failTask();
        assertTrue(stepsTask.isFailed());

    }
}