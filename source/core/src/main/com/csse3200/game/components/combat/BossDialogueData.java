package com.csse3200.game.components.combat;

import java.util.HashMap;
import java.util.Map;

public class BossDialogueData {
    // Initialise hashmap to store dialogues
    private final Map<String, String[][]> dialogues = new HashMap<>();

    /**
     * Initialise dialogues for each boss entity
     */
    public BossDialogueData() {
        dialogues.put("KANGAROO", new String[][]{
                {"*Kanga limps forward*...",
                        "You may have defeated me *cough* ",
                        "The seas rage north from here... you won't last long!",
                        "*DIES*"},
                {"HAHAHAHAHAHA!!",
                        "You were weaker than I thought",
                        "Maybe level up or something",
                        "*Hops Away*"}
        });

        dialogues.put("WATER_BOSS", new String[][]{
                {"*flop* *flop*",
                    "ARRRGGGHH!! HOW CAN THIS BE?!??!!?",
                    "the world Eater must... rest now",
                    "*sinks to depths of ocean*"},
                {"I AM STILL HUNGRY",
                        "GIVE ME MORE!! I WANT MOOOOOOOOORE!!!!"}
        });

        dialogues.put("AIR_BOSS", new String[][]{
                {"*fwoosh*",
                        "*The mighty gryphon falls from the sky*",
                        "You think you have won?",
                        "Enjoy this empty world with your silly little friends",
                        "I need to find my feet anyway"},
                {"So close...",
                        "but now... so... so... far"}
        });
    }

    /**
     * Get the dialogue option for a specific boss. Returns dialogue depending on
     * win status
     *
     * @param entityName The boss entity name in string format
     * @param winStatus True if player won the battle, false otherwise
     * @return The dialogue string for post boss combat
     */
    public String[][] getDialogue(String entityName, boolean winStatus) {
        String[][] entityDialogues = dialogues.get(entityName);
        return new String[][]{winStatus ? entityDialogues[0] : entityDialogues[1]};
    }
}

