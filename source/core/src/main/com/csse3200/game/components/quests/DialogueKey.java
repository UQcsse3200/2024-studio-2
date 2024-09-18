package com.csse3200.game.components.quests;

import java.util.Objects;

/**
 * Key to identify dialogues based on the NPC's.
 */
public class DialogueKey {
        /** Name of each NPC*/
        private final String npcName;
        /** Progression level tracker. */

        private final String taskName;
        private final String questName;

        /**
         * Retrieves the name of the NPC.
         *
         * @return the name of the NPC.
         */
        public String getNpcName() {
            return this.npcName;
        }

        /**
         * Retrieves the name of the quest.
         *
         * @return the name of the quest.
         */
        public String getTaskName() {
            return this.taskName;
        }

        /**
         * Constructs a new DialogueKey.
         * @param npcName The name of the NPC.
        // * @param progressionLevel The progression level of dialogue.
         */
        public DialogueKey(String npcName, String questName, String taskName) {
            this.npcName = npcName;
            this.taskName = taskName;
            this.questName = questName;
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
            return Objects.equals(npcName, dialogueKey.npcName) && Objects.equals(taskName, dialogueKey.taskName);
        }

        /** Returns the hash code value for this DialogueKey */
        @Override
        public int hashCode() {
            return Objects.hash(npcName, this.taskName);
        }

        /**Returns a string representation of this DialogueKey.*/
        @Override
        public String toString() {
            return String.format("TupleKey{str='%s', str=%d}", npcName, questName);
        }

    public String getQuestName() {
        return questName;
    }
}

