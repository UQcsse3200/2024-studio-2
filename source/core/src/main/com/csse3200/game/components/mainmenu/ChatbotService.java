package com.csse3200.game.components.mainmenu;

import java.util.HashMap;
import java.util.Map;

public class ChatbotService {
    // Store predefined keyword-response pairs
    private final Map<String, String> responses;

    public ChatbotService() {
        responses = new HashMap<>();
        initializeResponses();  // Initialize predefined responses
    }

    // Initialize the keyword-response pairs
    private void initializeResponses() {
        responses.put("objective", "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.");
        responses.put("start", "Simply click the \"Start\" button on the main menu to begin your adventure!");
        responses.put("kingdoms", "The game features three unique kingdoms: the Air Kingdom, the Water Kingdom, and the Land Kingdom.");
        responses.put("animals", "Each kingdom is home to different creatures. In the Air Kingdom, you’ll encounter various types of birds. The Water Kingdom hosts animals like crocodiles, and in the Land Kingdom, you’ll face creatures like dogs.");
        responses.put("final boss", "The final boss of the game is a mighty kangaroo—defeat it to claim ultimate victory!");
        responses.put("defeat", "Engage in combat by using different combat keys. Each battle will test your skills!");
        responses.put("special abilities", "Yes, there are special abilities! You can find more details in the Help menu.");
        responses.put("switch kingdoms", "In this version, switching between kingdoms isn’t possible, but stay tuned for future updates!");
        responses.put("controls", "Use the WASD keys to move: W for up, A for left, S for down, and D for right.");
        responses.put("easter eggs", "Yes, there are! Keep an eye out for NPCs (non-playable characters) scattered throughout the game.");
        responses.put("save", "You can save your game by clicking the \"Save\" button in the pause menu.");
        responses.put("customize character", "Character customization isn’t available yet, but we have some awesome characters for you to choose from!");
        responses.put("defeat boss", "Once you defeat the final boss, you win the game and become the overlord of *Attack on Animals*!");
        responses.put("tips", "Yes! It’s always a good idea to check out the Help menu before starting the game—it can provide valuable insights.");
        responses.put("hello", "Hello there! Welcome to *Attack on Animals*! Ready for an adventure?");
        responses.put("attack", "To attack, press the space bar or use the designated attack key! Be quick and strategic!");
        responses.put("move", "Use the WASD keys to move your character: W to move up, A to move left, S to move down, and D to move right.");
        responses.put("leaderboard", "Play the mini-games and view the leaderboard on the main menu!");
        responses.put("mini-games", "There are 3 mini-games: birdie dash, underwater maze & snake. Have fun playing!");
    }

    /**
     * Processes user input and returns a corresponding response.
     * @param userMessage The message entered by the user.
     * @return The chatbot's response based on predefined keywords.
     */
    public String getResponse(String userMessage) {
        if (userMessage == null || userMessage.isEmpty()) {
            return "I didn't catch that. Can you say something?";
        }

        // Convert the message to lowercase for easier keyword matching
        String lowerCaseMessage = userMessage.toLowerCase();

        // Loop through the keyword-response pairs and find a matching keyword
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            if (lowerCaseMessage.contains(entry.getKey())) {
                return entry.getValue();  // Return the matched response
            }
        }

        // Default response if no keywords are matched
        return "I'm sorry, I don't understand. Can you ask something else?";
    }
}
