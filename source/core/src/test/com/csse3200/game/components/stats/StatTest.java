package com.csse3200.game.components.stats;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatTest {
    private Stat stat;

    @Test
    void twoArgConstructor(){
        stat = new Stat("Tracking", "Stats are being tracked");
        assertFalse(stat.hasMax());
        assertEquals("Tracking", stat.getStatName());
        assertEquals("Stats are being tracked", stat.getStatDescription());
        assertEquals(0, stat.getCurrent());
        assertEquals(-1, stat.getMax());
    }

    @Test
    void threeArgConstructor(){
        stat = new Stat("Tracking", "Stats are being tracked", 10);
        assertTrue(stat.hasMax());
        assertEquals("Tracking", stat.getStatName());
        assertEquals("Stats are being tracked", stat.getStatDescription());
        assertEquals(0, stat.getCurrent());
        assertEquals(10, stat.getMax());
    }

    @Test
    void updateSet() {
        stat = new Stat("Tracking", "Stats are being tracked");
        stat.update("set", 5);
        assertEquals(5, stat.getCurrent());
        stat = new Stat("Tracking", "Stats are being tracked", 10);
        stat.update("set", 15);
        assertEquals(10, stat.getCurrent() );
    }

    @Test
    void updateAdd() {
        stat = new Stat("Tracking", "Stats are being tracked", 10);
        stat.update("add", 5);
        assertEquals(5, stat.getCurrent());
        stat.update("add", 15);
        assertEquals(10, stat.getCurrent());
        stat = new Stat("Tracking", "Stats are being tracked");
        stat.update("add", 5);
        assertEquals(5, stat.getCurrent());
    }

    @Test
    void updateSubtract() {
        stat = new Stat("Tracking", "Stats are being tracked");
        stat.update("add", 5);
        assertEquals(5, stat.getCurrent());
        stat.update("subtract", 3);
        assertEquals(2, stat.getCurrent());
        stat.update("subtract", 3);
        assertEquals(0, stat.getCurrent());
    }
}