package com.csse3200.game.components.stats;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StatTest {

    private Stat stat;

    // Mock objects to simulate dependencies
    @Mock
    private Json mockJson;

    @Mock
    private JsonValue mockJsonValue;

    @BeforeEach
    void setUp() {
        // Initialise the mocks before each test case
        MockitoAnnotations.openMocks(this);

        // Create an instance of Stat to test
        stat = new Stat("ApplesCollected", "Number of apples collected", 0, 10, true, Stat.StatType.ITEM);
    }

    @Test
    void testConstructorAndGetters() {
        // Check the initial values are correctly assigned by the constructor
        assertEquals("ApplesCollected", stat.getStatName());
        assertEquals("Number of apples collected", stat.getStatDescription());
        assertEquals(0, stat.getCurrent());
        assertEquals(10, stat.getStatMax());
        assertTrue(stat.hasMax());
        assertEquals(Stat.StatType.ITEM, stat.getType());
    }

    @Test
    void testSetCurrent() {
        // Test setting the current stat within the max limit
        stat.update("set", 5);
        assertEquals(5, stat.getCurrent());

        // Test setting the current stat beyond the max value (should cap at max)
        stat.update("set", 15);
        assertEquals(10, stat.getCurrent());
    }

    @Test
    void testAddValue() {
        // Test adding a value within the max limit
        stat.update("add", 3);
        assertEquals(3, stat.getCurrent());

        // Test adding a value beyond the max (should cap at max)
        stat.update("add", 10);
        assertEquals(10, stat.getCurrent());
    }

    @Test
    void testSubtractValue() {
        // Set the current value
        stat.update("set", 7);

        // Test subtracting a value within bounds
        stat.update("subtract", 3);
        assertEquals(4, stat.getCurrent());

        // Test subtracting a value that would go below 0 (should cap at 0)
        stat.update("subtract", 5);
        assertEquals(0, stat.getCurrent());
    }

    @Test
    void shouldSaveLoadStats() {
        GameState.clearState();

        GameState.clearState();

        GameState.stats.stats.add(stat);

        SaveHandler.getInstance().save(GameState.class, "test/saves/stat", FileLoader.Location.LOCAL);

        GameState.stats.stats = new Array<>();

        SaveHandler.getInstance().load(GameState.class, "test/saves/stat", FileLoader.Location.LOCAL);

        assertEquals("ApplesCollected", GameState.stats.stats.get(0).getStatName());
        assertEquals(10, GameState.stats.stats.get(0).getStatMax());
        assertEquals(Stat.StatType.ITEM, GameState.stats.stats.get(0).getType());

        SaveHandler.getInstance().delete(GameState.class, "test/saves/stat", FileLoader.Location.LOCAL);
    }

    @Test
    void testToString() {
        // Verify the correct string representation of the stat object
        String expectedString = "Stat{statName='ApplesCollected', statDescription='Number of apples collected', statCurrent=0, statMax=10, statHasMax=true, type=ITEM}";
        assertEquals(expectedString, stat.toString());
    }

    @Test
    void testInitializationWithoutMax() {
        Stat statNoMax = new Stat("StepsTaken", "Number of steps taken", 5, null, false, Stat.StatType.PLAYER);
        assertEquals("StepsTaken", statNoMax.getStatName());
        assertEquals("Number of steps taken", statNoMax.getStatDescription());
        assertEquals(5, statNoMax.getCurrent());
        assertEquals(-1, statNoMax.getStatMax());
        assertFalse(statNoMax.hasMax());
        assertEquals(Stat.StatType.PLAYER, statNoMax.getType());
    }

    @Test
    void testUpdateWithInvalidOperation() {
        // Assuming default behavior is to do nothing on invalid operation
        stat.update("invalid_operation", 5);
        assertEquals(0, stat.getCurrent(), "Stat should not change on invalid operation.");
    }

    @Test
    void testAddValueAtBoundary() {
        // Setting the current value to one less than max then adding 1
        stat.update("set", 9);
        stat.update("add", 1);
        assertEquals(10, stat.getCurrent(), "Stat should cap at max value.");

        // Adding 1 more should still cap at max
        stat.update("add", 1);
        assertEquals(10, stat.getCurrent(), "Stat should remain at max value.");
    }

    @Test
    void testSubtractValueAtBoundary() {
        // Setting the current value then subtracting down to 0
        stat.update("set", 2);
        stat.update("subtract", 2);
        assertEquals(0, stat.getCurrent(), "Stat should floor at 0.");

        // Subtracting more should still floor at 0
        stat.update("subtract", 1);
        assertEquals(0, stat.getCurrent(), "Stat should remain at 0.");
    }

}
