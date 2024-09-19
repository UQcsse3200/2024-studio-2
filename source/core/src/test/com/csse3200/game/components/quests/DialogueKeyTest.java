package com.csse3200.game.components.quests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DialogueKeyTest {
    private DialogueKey key1;
    private DialogueKey key2;
    private DialogueKey key3;

    @BeforeEach
    void setUp() {
        key1 = new DialogueKey("Cow", "First Steps", "stepsTask");
        key2 = new DialogueKey("Cow", "Guide's Intro", "talkToGuide");
        key3 = new DialogueKey("Cow", "Potion Collection", "collectPotions");
    }


    //@Test
   /* void testEquals() {

        assertEquals(key1, key2);
        assertNotEquals(key1, key3);
    }

    @Test
    void testHashCode() {
        assertEquals(key1.hashCode(),
                key2.hashCode(), "Hash codes should be equal.");
        assertNotEquals(key1.hashCode(),
                key3.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(key1.toString(), key2.toString());
        assertNotEquals(key1.toString(), key3.toString());
    }*/
}