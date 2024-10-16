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

    private Table chatbotTable;
    private TextField userInputField;
    private Label chatbotResponseLabel;
    private Label instructionLabel; // New label for keyword instructions
    private List<String> predefinedQuestions;
    private boolean ischatbotTableVisible = false;
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
    public void openchatbotTable() {
        if (chatbotTable == null) {
            logger.info("Creating chatbot window.");
            createchatbotTable();
        }
        mainMenuDisplay.setMenuUntouchable();

        chatbotTable.clearActions();
        chatbotTable.setVisible(true);
        centerWindowOnScreen();
        if (!stage.getActors().contains(chatbotTable, true)) {
            stage.addActor(chatbotTable);
        }
        ischatbotTableVisible = true;
    }

    /**
     * Closes the chatbot window and makes the menu interactive again.
     */
    public void closechatbotTable() {
        if (chatbotTable != null && ischatbotTableVisible) {
            logger.info("Closing chatbot window.");
            chatbotTable.setVisible(false); // Instantly hide the window
            ischatbotTableVisible = false;  // Update the visibility flag
            mainMenuDisplay.setMenuTouchable();
        }
    }

    /**
     * Checks if the chatbot window is visible.
     *
     * @return true if the chatbot window is visible, false otherwise.
     */
    public boolean ischatbotTableVisible() {
        return ischatbotTableVisible;
    }

    /**
     * Creates and configures the chatbot window UI.
     * It includes a background, predefined questions as clickable buttons, and a text field for custom input.
     */
    private void createchatbotTable() {
        int chatWidth = 400;
        chatbotTable = new Table();

        chatbotTable.setSize(600, 770);

        // Use a texture that properly scales with the window
        Texture backgroundTexture = new Texture("images/backgrounds/LeaderboardBackground.png");
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear); // Ensure smooth scaling
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        chatbotTable.setBackground(backgroundDrawable);

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
            questionTable.add(questionButton).pad(3).width(chatWidth).height(50f).row();
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
        chatbotResponseLabel.setWidth(580);

        Button closeButton = new Button(new TextureRegionDrawable(new TextureRegion(new Texture("images/CloseButton.png"))));
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Close button clicked.");
                closechatbotTable();
            }
        });
        Table topTable = new Table();
        Table contentTable = new Table();

        topTable.top().padTop(10);
        topTable.add(titleLabel).expandX().center().padTop(5).top();
        topTable.row();
        topTable.add(closeButton).size(80, 80).right().expandX().padRight(-25).padTop(-110);

        contentTable.add(instructionLabel).top().padTop(50).width(chatWidth).center();  // Added instructionLabel here
        contentTable.row();
        contentTable.add(questionTable).expandX().fillX().padTop(30f);
        contentTable.row();
        contentTable.add(userInputField).width(chatWidth).pad(10);
        contentTable.row();
        contentTable.add(sendButton).bottom().pad(10).width(180f).height(45f);
        contentTable.row();
        contentTable.add(chatbotResponseLabel).width(chatWidth).pad(10).row();

        chatbotTable.add(topTable).expandX().fillX().padTop(-10).top();
        chatbotTable.row();
        chatbotTable.add(contentTable).expandX().expandY().padLeft(30f).padRight(30f).top().padTop(20);
        chatbotTable.row();

        // Force the window to maintain the size after all content is added
        chatbotTable.setSize(525, 625);
        chatbotTable.layout();

        // Center the window after size adjustment
        centerWindowOnScreen();
    }

    /**
     * Centers the chatbot window on the screen.
     */
    private void centerWindowOnScreen() {
        chatbotTable.setPosition(
                (Gdx.graphics.getWidth() - chatbotTable.getWidth()) / 2,
                (Gdx.graphics.getHeight() - chatbotTable.getHeight()) / 2
        );
    }

    /**
     * Sets up predefined questions that users can click on for quick chatbot responses.
     */
    private void setupPredefinedQuestions() {
        predefinedQuestions = new ArrayList<>();
        predefinedQuestions.add("How do I move?");
        predefinedQuestions.add("How do I attack?");
        predefinedQuestions.add("How can I save my game?");
        predefinedQuestions.add("Hello");
    }

    /**
     * Updates the position of the chatbot window when the screen size changes.
     */
    public void updatechatbotTablePosition() {
        if (chatbotTable != null) {
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();
            chatbotTable.setPosition(
                    (screenWidth - chatbotTable.getWidth()) / 2,
                    (screenHeight - chatbotTable.getHeight()) / 2
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