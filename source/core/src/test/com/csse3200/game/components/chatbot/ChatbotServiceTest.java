package com.csse3200.game.components.chatbot;

import com.csse3200.game.components.mainmenu.ChatbotService;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class ChatbotServiceTest {

    private ChatbotService chatbotService;

    @BeforeEach
    public void setUp() {
        chatbotService = new ChatbotService();
    }

    private void testResponse(String userMessage, String expectedResponse) {
        String actualResponse = chatbotService.getResponse(userMessage);
        assertEquals(expectedResponse, actualResponse);
    }

    // Existing Tests

    @Test
    void testResponseWithMatchingKeyword() {
        String userMessage = "Tell me about the objective of the game.";
        String expectedResponse = "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithDifferentKeywordCasing() {
        String userMessage = "What are the CONTROLS?";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithNoMatchingKeyword() {
        String userMessage = "Tell me more about the game.";
        String expectedResponse = "I'm sorry, I don't understand. Can you ask something else?";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithNullInput() {
        String expectedResponse = "I didn't catch that. Can you say something?";
        testResponse(null, expectedResponse);
    }

    @Test
    void testResponseWithEmptyInput() {
        String userMessage = "";
        String expectedResponse = "I didn't catch that. Can you say something?";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithMultipleMatchingKeywords() {
        String userMessage = "Can you explain the objective and the kingdoms?";
        String expectedResponse = "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithPartialKeywordMatch() {
        String userMessage = "How do I start the game?";
        String expectedResponse = "Simply click the \"Start\" button on the main menu to begin your adventure!";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithMixedContent() {
        String userMessage = "Start game controls?";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithSpecialCharacters() {
        String userMessage = "What! are the controls?";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithMultipleKeywordsButNoMatch() {
        String userMessage = "I want to know the game's controls and something.";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithExactKeywordButExtraWhitespace() {
        String userMessage = "   What are the controls   ?  ";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithNumbersInMessage() {
        String userMessage = "Tell me 1 thing about the objective of the game.";
        String expectedResponse = "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithConjunctionsInMessage() {
        String userMessage = "What are the controls and how do I move?";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseForUnusualMessageStructure() {
        String userMessage = "Controls, what are?";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseForExactKeywordAtEnd() {
        String userMessage = "Can you give me the information about controls?";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithUnrelatedQuestion() {
        String userMessage = "How's the weather?";
        String expectedResponse = "I'm sorry, I don't understand. Can you ask something else?";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithApostrophesInInput() {
        String userMessage = "What's the game's objective?";
        String expectedResponse = "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseForRepeatedKeywords() {
        String userMessage = "Tell me about the objective of the objective.";
        String expectedResponse = "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseForLongInputWithMultipleKeywords() {
        String userMessage = "Please, can you explain to me all the game controls and how do I move in the game?";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseForSingleWordKeyword() {
        String userMessage = "Objective?";
        String expectedResponse = "Your goal is to defeat all the animals in each kingdom and ultimately become the overlord.";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseWithRandomCharacters() {
        String userMessage = "asdfghjkl";
        String expectedResponse = "I'm sorry, I don't understand. Can you ask something else?";
        testResponse(userMessage, expectedResponse);
    }

    @Test
    void testResponseForQuestionWithoutEndingPunctuation() {
        String userMessage = "What are the controls";
        String expectedResponse = "Use the WASD keys to move: W for up, A for left, S for down, and D for right.";
        testResponse(userMessage, expectedResponse);
    }
}
