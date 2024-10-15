package com.csse3200.game.components.quests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DialogueKeyTest {
    private DialogueKey key1;
    private DialogueKey key2;
    private DialogueKey key3;

    @BeforeEach
    void setUp() {
        String[][] stepsDialogue = {{"Start walking"}};
        String[][] guideDialogue = {{"Yep! Cow is the guide"}};
        String[][] potionDialogue = {{"Need 5 defense potions.", " Get to it!"}};
        key1 = new DialogueKey("Cow", stepsDialogue);
        key2 = new DialogueKey("Cow", guideDialogue);
        key3 = new DialogueKey("Cow", potionDialogue);
    }

    @Test
   void testEquals() {
        assertNotEquals(key1, key3);
    }

    @Test
    void testHashCode() {
        assertNotEquals(key1.hashCode(),
                key3.hashCode());
    }

    @Test
    void testToString() {
        assertNotEquals(key1.toString(), key3.toString());
    }
}