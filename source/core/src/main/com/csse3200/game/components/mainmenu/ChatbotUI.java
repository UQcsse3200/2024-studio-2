package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.components.login.PlayFab;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.CustomButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * A class that handles the chatbot UI and its functionality.
 * It includes methods to create, display, and process user input for the chatbot window.
 * The chatbot service can answer predefined questions and also provides a keyword-based input system.
 */
public class ChatbotUI {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotUI.class);

    private Window chatbotWindow;
    private TextField userInputField;
    private Label chatbotResponseLabel;
    private Label instructionLabel; // New label for keyword instructions
    private List<String> predefinedQuestions;
    private boolean isChatbotWindowVisible = false;
    private final Stage stage;
    private final Skin skin;
    private final ChatbotService chatbotService; // ChatbotService instance
    private MainMenuDisplay mainMenuDisplay;

    /**
     * Constructor to initialize the Chatbot UI.
     *
     * @param stage           The stage where the chatbot window will be added.
     * @param skin            The skin used to style the UI elements.
     * @param mainMenuDisplay The main menu display that handles menu visibility and interaction.
     */
    public ChatbotUI(Stage stage, Skin skin, MainMenuDisplay mainMenuDisplay) {
        this.stage = stage;
        this.skin = skin;
        this.chatbotService = new ChatbotService(); // Initialize ChatbotService
        this.setupPredefinedQuestions();
        this.mainMenuDisplay = mainMenuDisplay;
    }

    /**
     * Opens the chatbot window in the center of the screen.
     */
    public void openChatbotWindow() {
        if (chatbotWindow == null) {
            logger.info("Creating chatbot window.");
            createChatbotWindow();
        }
        mainMenuDisplay.setMenuUntouchable();

        chatbotWindow.clearActions();
        chatbotWindow.setVisible(true);
        centerWindowOnScreen();
        if (!stage.getActors().contains(chatbotWindow, true)) {
            stage.addActor(chatbotWindow);
        }
        isChatbotWindowVisible = true;
    }

    /**
     * Closes the chatbot window and makes the menu interactive again.
     */
    public void closeChatbotWindow() {
        if (chatbotWindow != null && isChatbotWindowVisible) {
            logger.info("Closing chatbot window.");
            chatbotWindow.setVisible(false); // Instantly hide the window
            isChatbotWindowVisible = false;  // Update the visibility flag
            mainMenuDisplay.setMenuTouchable();
        }
    }

    /**
     * Checks if the chatbot window is visible.
     *
     * @return true if the chatbot window is visible, false otherwise.
     */
    public boolean isChatbotWindowVisible() {
        return isChatbotWindowVisible;
    }

    /**
     * Creates and configures the chatbot window UI.
     * It includes a background, predefined questions as clickable buttons, and a text field for custom input.
     */
    private void createChatbotWindow() {
        int chatWidth = 600;
        chatbotWindow = new Window("", skin);

        final float WINDOW_WIDTH = Gdx.graphics.getWidth() * 0.9f;
        final float WINDOW_HEIGHT = Gdx.graphics.getHeight() * 0.9f;

        chatbotWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Use a texture that properly scales with the window
        Texture backgroundTexture = new Texture("images/SettingBackground.png");
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // Ensure smooth scaling
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        chatbotWindow.setBackground(backgroundDrawable);

        Label titleLabel = new Label("Chatbot", skin, "title-white");
        titleLabel.setAlignment(Align.center);

        Table questionTable = new Table();
        for (String question : predefinedQuestions) {
            CustomButton questionButton = new CustomButton(question, skin);
            questionButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logger.info("Question clicked: " + question);
                    processChatInput(question);
                }
            });
            questionTable.add(questionButton).pad(3).width(chatWidth).height(45f).row();
        }

        userInputField = new TextField("", skin);
        userInputField.setMessageText("Type keywords like: hello, move, attack, save, etc. OR click on common FAQs");
        userInputField.setAlignment(Align.center);

        CustomButton sendButton = new CustomButton("Send", skin);
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Send button clicked with input: " + userInputField.getText());
                processChatInput(userInputField.getText());
            }
        });

        chatbotResponseLabel = new Label("", skin);
        chatbotResponseLabel.setWrap(true);
        chatbotResponseLabel.setAlignment(Align.center);
        chatbotResponseLabel.setWidth(500);

        CustomButton closeButton = new CustomButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Close button clicked.");
                closeChatbotWindow();
            }
        });

        Table contentTable = new Table();
        contentTable.add(titleLabel).padTop(20).center().row();
        contentTable.add(instructionLabel).padTop(50).width(chatWidth).center().row();  // Added instructionLabel here
        contentTable.add(questionTable).expandX().fillX().padTop(30f).row();
        contentTable.add(userInputField).width(chatWidth).pad(10).row();
        contentTable.add(sendButton).pad(10).width(180f).height(45f).row();
        contentTable.add(chatbotResponseLabel).width(chatWidth).pad(10).row();
        contentTable.add(closeButton).pad(10).width(180f).height(45f).row();

        chatbotWindow.add(contentTable).expandX().fillX();

        // Force the window to maintain the size after all content is added
        chatbotWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        chatbotWindow.layout();

        // Center the window after size adjustment
        centerWindowOnScreen();
    }

    /**
     * Centers the chatbot window on the screen.
     */
    private void centerWindowOnScreen() {
        chatbotWindow.setPosition(
                (Gdx.graphics.getWidth() - chatbotWindow.getWidth()) / 2,
                (Gdx.graphics.getHeight() - chatbotWindow.getHeight()) / 2
        );
    }

    /**
     * Sets up predefined questions that users can click on for quick chatbot responses.
     */
    private void setupPredefinedQuestions() {
        predefinedQuestions = new ArrayList<>();
        predefinedQuestions.add("How do I move?");
        predefinedQuestions.add("How do I attack?");
        predefinedQuestions.add("What's the objective?");
        predefinedQuestions.add("How can I save my game?");
        predefinedQuestions.add("Hello");
    }

    /**
     * Updates the position of the chatbot window when the screen size changes.
     */
    public void updateChatbotWindowPosition() {
        if (chatbotWindow != null) {
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            chatbotWindow.setPosition(
                    (screenWidth - chatbotWindow.getWidth()) / 2,
                    (screenHeight - chatbotWindow.getHeight()) / 2
            );
        }
    }

    /**
     * Processes the chat input and retrieves a response from the ChatbotService.
     * The response is personalized using the player's username.
     *
     * @param userInput The input from the user.
     */
    private void processChatInput(String userInput) {
        logger.info("Processing input: " + userInput);

        // Get the player's username safely
        String username = PlayFab.getPlayerName();

        // Check if the username is null or empty and set to "Guest" if it is
        if (username == null || username.isEmpty()) {
            username = "Guest";  // Use a default value like "Guest" if no username is found
        }

        // Get the chatbot response from ChatbotService
        String chatbotResponse = chatbotService.getResponse(userInput);

        // Personalize the response by adding the username
        String personalizedResponse = "Hello, " + username + "! " + chatbotResponse;

        // Display the personalized chatbot response
        chatbotResponseLabel.setText(personalizedResponse);
    }
}