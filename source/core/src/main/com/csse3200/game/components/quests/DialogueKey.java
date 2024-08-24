package com.csse3200.game.components.quests;

import java.util.Objects;

public class DialogueKey {
        private final String npcName;
        private final Integer progressionLevel;

        public DialogueKey(String npcName, Integer progressionLevel) {
            this.npcName = npcName;
            this.progressionLevel = progressionLevel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DialogueKey dialogueKey = (DialogueKey) o;
            return Objects.equals(npcName, dialogueKey.npcName) && Objects.equals(progressionLevel, dialogueKey.progressionLevel);
        }

        @Override
        public int hashCode() {
            return Objects.hash(npcName, progressionLevel);
        }

        @Override
        public String toString() {
            return "TupleKey{" +
                    "str='" + npcName + '\'' +
                    ", num=" + progressionLevel +
                    '}';
        }
}

