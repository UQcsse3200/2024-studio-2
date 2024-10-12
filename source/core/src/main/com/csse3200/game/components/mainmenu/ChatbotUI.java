package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
 */
public class ChatbotUI {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotUI.class);

    private Dialog chatbotDialog;
    private TextField userInputField;
    private Label chatbotResponseLabel;
    private List<String> predefinedQuestions;
    private boolean isChatbotDialogVisible = false;
    private final Stage stage;
    private final Skin skin;
    private final ChatbotService chatbotService; // ChatbotService instance

    public ChatbotUI(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
        this.chatbotService = new ChatbotService(); // Initialize ChatbotService
        this.setupPredefinedQuestions();
    }

    /**
     * Opens the chatbot dialog in the center of the screen.
     */
    public void openChatbotDialog() {
        if (chatbotDialog == null) {
            logger.info("Creating chatbot dialog.");
            createChatbotDialog();
        }
        chatbotDialog.show(stage, Actions.sequence(Actions.alpha(1f)));
        centerDialogOnScreen();
        isChatbotDialogVisible = true;
    }

    /**
     * Closes the chatbot dialog.
     */
    public void closeChatbotDialog() {
        if (chatbotDialog != null && isChatbotDialogVisible) {
            logger.info("Closing chatbot dialog.");
            chatbotDialog.hide();
            isChatbotDialogVisible = false;
        }
    }

    /**
     * Checks if the chatbot dialog is visible.
     */
    public boolean isChatbotDialogVisible() {
        return isChatbotDialogVisible;
    }

    /**
     * Create and configure the chatbot dialog UI.
     */
    private void createChatbotDialog() {
        int chatWidth = 600;
        chatbotDialog = new Dialog("", skin);

        final float DIALOG_WIDTH = Math.min(1000f, Gdx.graphics.getWidth() - 100f);
        final float DIALOG_HEIGHT = Math.min(800f, Gdx.graphics.getHeight() - 100f);
        chatbotDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/SettingBackground.png")));
        chatbotDialog.setBackground(backgroundDrawable);

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
        userInputField.setMessageText("Type your question...");
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
                closeChatbotDialog();
            }
        });

        Table contentTable = new Table();
        contentTable.add(titleLabel).padTop(20).center().row();
        contentTable.add(questionTable).expandX().fillX().padTop(30f).row();
        contentTable.add(userInputField).width(chatWidth).pad(10).row();
        contentTable.add(sendButton).pad(10).width(180f).height(45f).row();
        contentTable.add(chatbotResponseLabel).width(chatWidth).pad(10).row();
        contentTable.add(closeButton).pad(10).width(180f).height(45f).row();

        chatbotDialog.getContentTable().add(contentTable).expandX().fillX();
    }

    private void centerDialogOnScreen() {
        chatbotDialog.setPosition(
                (Gdx.graphics.getWidth() - chatbotDialog.getWidth()) / 2,
                (Gdx.graphics.getHeight() - chatbotDialog.getHeight()) / 2
        );
    }

    private void setupPredefinedQuestions() {
        predefinedQuestions = new ArrayList<>();
        predefinedQuestions.add("How do I move?");
        predefinedQuestions.add("How do I attack?");
        predefinedQuestions.add("What's the objective?");
        predefinedQuestions.add("How can I save my game?");
        predefinedQuestions.add("Hello");
    }

    /**
     * Processes the chat input and retrieves a response from the ChatbotService.
     * @param userInput The input from the user.
     */
    private void processChatInput(String userInput) {
        logger.info("Processing input: " + userInput);
        String chatbotResponse = chatbotService.getResponse(userInput);
        chatbotResponseLabel.setText(chatbotResponse); // Display the response in the chatbot UI
    }
}
