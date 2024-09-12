package com.csse3200.game.components.quests;

import java.util.Objects;

/**
 * Key to identify dialogues based on the NPC's.
 */
public class DialogueKey {
        /** Name of each NPC*/
        private final String npcName;
        /** Progression level tracker. */
        private final Integer progressionLevel;

        /**
         * Constructs a new DialogueKey.
         * @param npcName The name of the NPC.
         * @param progressionLevel The progression level of dialogue.
         */
        public DialogueKey(String npcName, Integer progressionLevel) {
            this.npcName = npcName;
            this.progressionLevel = progressionLevel;
        }

        /**
         * Checks if this DialogueKey is equal to another object.
         * @param o The object to compare against.
         * @return True if object is equal to this DialogueKey.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DialogueKey dialogueKey = (DialogueKey) o;
            return Objects.equals(npcName, dialogueKey.npcName) && Objects.equals(progressionLevel, dialogueKey.progressionLevel);
        }

        /** Returns the hash code value for this DialogueKey */
        @Override
        public int hashCode() {
            return Objects.hash(npcName, progressionLevel);
        }

        /**Returns a string representation of this DialogueKey.*/
        @Override
        public String toString() {
            return String.format("TupleKey{str='%s', num=%d}", npcName, progressionLevel);
        }
}

