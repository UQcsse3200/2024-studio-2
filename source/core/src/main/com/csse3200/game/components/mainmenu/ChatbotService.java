package com.csse3200.game.components.mainmenu;

import java.util.HashMap;
import java.util.Map;

public class ChatbotService {

    // Store predefined keyword-response pairs
    private final Map<String, String> responses;

    public ChatbotService() {
        responses = new HashMap<>();
        initializeResponses();
    }

    // Initialize the keyword-response pairs
    private void initializeResponses() {
        responses.put("move", "You can move using the arrow keys or WASD.");
        responses.put("attack", "Press the spacebar to attack.");
        responses.put("objective", "The objective is to defeat all enemies and reach the final boss.");
        responses.put("hello", "Hello! How can I assist you?");
        responses.put("help", "Ask me about movement, attacking, or your objective.");
    }

    // Process user input and return a response
    public String getResponse(String userMessage) {
        if (userMessage == null || userMessage.isEmpty()) {
            return "I didn't catch that. Can you say something?";
        }

        // Convert the message to lowercase for easier keyword matching
        String lowerCaseMessage = userMessage.toLowerCase();

        // Define simple keyword-response pairs
        Map<String, String> responses = new HashMap<>();
        responses.put("move", "You can move using the arrow keys or WASD.");
        responses.put("attack", "Press the spacebar to attack.");
        responses.put("objective", "The objective is to defeat all enemies and reach the final boss.");
        responses.put("hello", "Hello! How can I assist you?");
        responses.put("help", "Ask me about movement, attacking, or your objective.");

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
