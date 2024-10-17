package com.csse3200.game.components.story;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds story dialogue for different stories like Dog, Croc, and Bird.
 */
public class StoryDialogueData {
    private final Map<String, String[][]> dialogues = new HashMap<>();

    public StoryDialogueData() {
        // Dog's Story: 6 dialogue screens
        dialogues.put("dog", new String[][]{
                {"Spike was always a good boy and loved his family."},
                {"He used to love to play fetch and loved balls"},
                {"But in an instant, everything changed."},
                {"At first, Spike could not comprehend what had happened."},
                {"He travelled all over the world, looking for his family"},
                {"As he grew stronger, he realised, the world had changed."}
        });

        // Croc's Story: 7 dialogue screens
        dialogues.put("croc", new String[][]{
                {"In the Water Kingdom, a lonely crocodile named Crunch lived."},
                {"He was a nice croc, always looking out for others"},
                {"One day, he met Shellby the Turtle. Shellby was a braveheart."},
                {"They became close friends."},
                {"One day, Shellby was attacked by a piranha"},
                {"Crunch was angry. He wanted revenge."},
                {"Crunch was ready to give it his all."}
        });

        // Bird's Story: 5 dialogue screens
        dialogues.put("bird", new String[][]{
                {"In the sky, Sparky the sparrow lived. He idolized his brother."},
                {"Hawky helped everyone, and he liked collecting shiny coins."}, // links to flappy bird game lol
                {"All the ladies loved Hawky. They could not get enough of him."},
                {"Hawky protected the realm from dark evil creatures"},
                {"Sparky saw this and was inspired..."}
        });
    }

    /**
     * Gets the dialogue for a specific story and screen number.
     *
     * @param storyName the name of the story (dog, croc, bird)
     * @param screenNum the current screen number
     * @return a 2D array of dialogue for the specified screen of the story
     */
    public String[][] getDialogue(String storyName, int screenNum) {
        String[][] storyDialogues = dialogues.get(storyName.toLowerCase());
        if (storyDialogues == null || screenNum >= storyDialogues.length) {
            return new String[][]{{"Dialogue not available."}};  // Fallback for missing dialogue
        }
        return new String[][]{storyDialogues[screenNum]};  // Wrap the 1D array into a 2D array
    }
}
