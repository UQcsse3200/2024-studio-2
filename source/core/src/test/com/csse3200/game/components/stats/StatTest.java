package com.csse3200.game.components.stats;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.food.Foods;
import com.csse3200.game.inventory.items.potions.HealingPotion;
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

        GameState.stats.stats.add(stat);

        SaveHandler.save(GameState.class, "test/saves/stat", FileLoader.Location.LOCAL);

        GameState.stats.stats = new Array<>();

        SaveHandler.load(GameState.class, "test/saves/stat", FileLoader.Location.LOCAL);

        assertEquals("ApplesCollected", GameState.stats.stats.get(0).getStatName());
        assertEquals(10, GameState.stats.stats.get(0).getStatMax());
        assertEquals(Stat.StatType.ITEM, GameState.stats.stats.get(0).getType());

        SaveHandler.delete(GameState.class, "test/saves/stat", FileLoader.Location.LOCAL);
    }

//    @Test
//    void testJsonWrite() {
//        // Mock Json write behaviour for verifying the output
//        stat.write(mockJson);
//
//        // Verify that the correct values are written to the Json object
//        verify(mockJson).writeValue("statName", "ApplesCollected");
//        verify(mockJson).writeValue("statDescription", "Number of apples collected");
//        verify(mockJson).writeValue("statCurrent", 0);
//        verify(mockJson).writeValue("statMax", 10);
//        verify(mockJson).writeValue("statHasMax", true);
//        verify(mockJson).writeValue("type", "ITEM");
//    }
//
//    @Test
//    void testJsonRead() {
//        // Mock the JsonValue object to return expected data for each key
//        when(mockJsonValue.getString("statName")).thenReturn("ApplesCollected");
//        when(mockJsonValue.getString("statDescription")).thenReturn("Number of apples collected");
//        when(mockJsonValue.getInt("statCurrent")).thenReturn(5);
//        when(mockJsonValue.getBoolean("statHasMax")).thenReturn(true);
//        when(mockJsonValue.getInt("statMax")).thenReturn(10);
//        when(mockJsonValue.getString("type")).thenReturn("ITEM");
//
//        // Read the mocked Json data
//        stat.read(mockJson, mockJsonValue);
//
//        // Verify that the stat object has been populated correctly
//        assertEquals("ApplesCollected", stat.getStatName());
//        assertEquals("Number of apples collected", stat.getStatDescription());
//        assertEquals(5, stat.getCurrent());
//        assertTrue(stat.hasMax());
//        assertEquals(10, stat.getStatMax());
//        assertEquals(Stat.StatType.ITEM, stat.getType());
//    }

    @Test
    void testToString() {
        // Verify the correct string representation of the stat object
        String expectedString = "Stat{statName='ApplesCollected', statDescription='Number of apples collected', statCurrent=0, statMax=10, statHasMax=true, type=ITEM}";
        assertEquals(expectedString, stat.toString());
    }
}
