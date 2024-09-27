package com.csse3200.game.components.quests;

import java.util.Objects;

/**
 * Key to identify dialogues based on the NPC's.
 */
public class DialogueKey {
    /** Name of each NPC*/
    private final String npcName;
    /** Progression level tracker. */

    private final String[][] dialogue;

    /**
     * Retrieves the name of the NPC.
     *
     * @return the name of the NPC.
     */
    public String getNpcName() {
        return this.npcName;
    }
//    /**
//     * Retrieves the name of the quest.
//     *
//     * @return the name of the quest.
//     */
//    public String getTaskName() {
//        return this.taskName;
//    }

    /** Returns the dialogue for the dialogue task.*/
    public String[][] getDialogue() {
        return dialogue;
    }

    /**
     * Constructs a new DialogueKey.
     * @param npcName The name of the NPC.
     * @param dialogue The name of the dialogue task.
     */
    public DialogueKey(String npcName, String[][] dialogue) {
        this.npcName = npcName;
        this.dialogue = dialogue;
    }

    /**
     * Checks if this DialogueKey is equal to another object.
     * @param o The object to compare against.
     * @return True if object is equal to this DialogueKey.
     */
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        DialogueKey dialogueKey = (DialogueKey) o;
//        return Objects.equals(npcName, dialogueKey.npcName) && Objects.equals(taskName, dialogueKey.taskName);
//    }

    /** Returns the hash code value for this DialogueKey */
//    @Override
//    public int hashCode() {
//        return Objects.hash(npcName, this.taskName);
//    }
    /**Returns a string representation of this DialogueKey.*/
//    @Override
//    public String toString() {
//        return String.format("%s,%s", dialogue, npcName);
//    }
    /** Returns the name of the dialogue quest. */
//    public String getQuestName() {
//        return this.questName;
//    }
}

