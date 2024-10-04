package com.csse3200.game.components.chatbot;

import com.csse3200.game.components.mainmenu.ChatbotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatbotServiceTest {

    private ChatbotService chatbotService;

    @BeforeEach
    public void setUp() {
        chatbotService = new ChatbotService();
    }

    @Test
    public void testResponseWithMatchingKeyword() {
        String userMessage = "Tell me about the objective of the game.";
        String expectedResponse = "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.";
        String actualResponse = chatbotService.getResponse(userMessage);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testResponseWithDifferentKeywordCasing() {
        String userMessage = "What are the CONTROLS?";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        String actualResponse = chatbotService.getResponse(userMessage);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testResponseWithNoMatchingKeyword() {
        String userMessage = "Tell me more about the game.";
        String expectedResponse = "I'm sorry, I don't understand. Can you ask something else?";
        String actualResponse = chatbotService.getResponse(userMessage);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testResponseWithNullInput() {
        String expectedResponse = "I didn't catch that. Can you say something?";
        String actualResponse = chatbotService.getResponse(null);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testResponseWithEmptyInput() {
        String userMessage = "";
        String expectedResponse = "I didn't catch that. Can you say something?";
        String actualResponse = chatbotService.getResponse(userMessage);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testResponseWithMultipleMatchingKeywords() {
        String userMessage = "Can you explain the objective and the kingdoms?";
        String expectedResponse = "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.";
        String actualResponse = chatbotService.getResponse(userMessage);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testResponseWithPartialKeywordMatch() {
        String userMessage = "How do I start the game?";
        String expectedResponse = "Simply click the \"Start\" button on the main menu to begin your adventure!";
        String actualResponse = chatbotService.getResponse(userMessage);

        assertEquals(expectedResponse, actualResponse);
    }
}
