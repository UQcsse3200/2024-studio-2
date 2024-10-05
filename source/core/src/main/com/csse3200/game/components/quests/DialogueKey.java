package com.csse3200.game.components.quests;


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
}

