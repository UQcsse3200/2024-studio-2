package com.csse3200.game.components.quests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DialogueKeyTest {

    @Test
    void testEquals() {
        DialogueKey key1 = new DialogueKey("Cow", 1);
        DialogueKey key2 = new DialogueKey("Cow", 1);
        DialogueKey key3 = new DialogueKey("Cow", 3);
        assertEquals(key1, key2);
        assertNotEquals(key1, key3);
    }

    @Test
    void testHashCode() {
        DialogueKey key1 = new DialogueKey("Cow", 1);
        DialogueKey key2 = new DialogueKey("Cow", 1);
        DialogueKey key3 = new DialogueKey("Cow", 3);
        assertEquals(key1.hashCode(),
                key2.hashCode(), "Hash codes should be equal.");
        assertNotEquals(key1.hashCode(),
                key3.hashCode());
    }

    @Test
    void testToString() {
        DialogueKey key1 = new DialogueKey("Cow", 1);
        DialogueKey key2 = new DialogueKey("Cow", 1);
        DialogueKey key3 = new DialogueKey("Cow", 3);
        assertEquals(key1.toString(), key2.toString());
        assertNotEquals(key1.toString(), key3.toString());
    }
}