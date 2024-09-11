package com.csse3200.game.components.stats;

import com.badlogic.gdx.utils.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatTest {

    private Stat stat;

    @BeforeEach
    void setUp() {
        stat = new Stat("Health", "The health of the player", 50, 100, true, Stat.StatType.PLAYER);
    }

    @Test
    void testGetters() {
        assertEquals("Health", stat.getStatName());
        assertEquals("The health of the player", stat.getStatDescription());
        assertEquals(50, stat.getCurrent());
        assertEquals(100, stat.getMax());
        assertTrue(stat.hasMax());
        assertEquals(Stat.StatType.PLAYER, stat.getType());
    }

    @Test
    void testUpdateSet() {
        stat.update("set", 75);
        assertEquals(75, stat.getCurrent());
        assertEquals(100, stat.getMax());
    }
    
    @Test
    void testUpdateSubtract() {
        stat = new Stat("Health", "The health of the player", 50, 100, true, Stat.StatType.PLAYER);
        stat.update("subtract", 20);
        assertEquals(30, stat.getCurrent()); // 50 - 20 = 30
        stat.update("subtract", 40);
        assertEquals(0, stat.getCurrent()); // Min value should be 0
    }

    @Test
    void testUpdateAdd() {
        stat.update("add", 30);
        assertEquals(80, stat.getCurrent()); // 50 + 30 = 80
        stat.update("add", 50);
        assertEquals(100, stat.getCurrent()); // Max is 100, so current should be 100
    }

    @Test
    void testUpdateWithoutMax() {
        Stat statWithoutMax = new Stat("Energy", "The energy of the player", 10, null, false, Stat.StatType.PLAYER);
        statWithoutMax.update("add", 20);
        assertEquals(30, statWithoutMax.getCurrent()); // No max limit
        statWithoutMax.update("subtract", 5);
        assertEquals(25, statWithoutMax.getCurrent()); // No max limit
    }

    @Test
    void testToString() {
        String expectedString = "Stat{statName='Health', statDescription='The health of the player', statCurrent=50, statMax=100, statHasMax=true, type=PLAYER}";
        assertEquals(expectedString, stat.toString());
    }
}
