package com.csse3200.game.components.quests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task stepsTask;

    @BeforeEach
    void setUp() {
        stepsTask = new Task("steps",
                "Take your first steps", "Just start moving!",
                1,0,false,false);
    }

    @Test
    void getTaskName() {
        assertEquals("steps", stepsTask.getTaskName());
    }

    @Test
    void getDescription() {
        assertEquals("Take your first steps", stepsTask.getDescription());
    }

    @Test
    void getHint() {
        assertEquals("Just start moving!", stepsTask.getHint());
    }

    @Test
    void handleEvents() {
        assertFalse(stepsTask.isCompleted());
        assertEquals(0, stepsTask.getTriggerCount());
        stepsTask.handleEvent();
        assertTrue(stepsTask.isCompleted());
        assertEquals(1, stepsTask.getTriggerCount());
    }

    @Test
    void getRequiredTriggers() {
        assertEquals(1, stepsTask.getRequiredTriggers());
    }

    @Test
    void failure() {
        assertFalse(stepsTask.isFailed());
        stepsTask.failTask();
        assertTrue(stepsTask.isFailed());

    }
}