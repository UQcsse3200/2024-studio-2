package com.csse3200.game.ui.dialoguebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGameManager;
import com.csse3200.game.minigames.MiniGameNames;

import static com.csse3200.game.minigames.MiniGameNames.*;

/**
 * Represents a chat overlay UI component that displays a series of hint messages
 * and allows navigation between them using forward and backward buttons.
 */
public class DialogueBox {

    // Static resources
    private static final Skin SKIN = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    private static final Texture BACKGROUND_TEXTURE = new Texture(Gdx.files.internal("images/blue-bar.png"));
    private static final Texture BUTTON_IMAGE_TEXTURE = new Texture(Gdx.files.internal("images/blue-button.png"));
    private static final Texture BUTTON_HOVER_TEXTURE = new Texture(Gdx.files.internal("images/blue-b-hover.png"));

    private final Stage stage;
    private Label label;
    private Image backgroundImage;
    private TextButton forwardButton;
    private TextButton backwardButton;
    private TextButton playButton;
    private String[][] hints;
    private int currentHint;
    private int currentHintLine;
    private MiniGameNames currentMinigame;
    private TextButton[] optionButtons;
    private int screenWidth = Gdx.graphics.getWidth();
    private int screenHeight = Gdx.graphics.getHeight();
    private boolean isVisible;

    /**
     * Creates a new base DialogueBox with the given hint messages.
     */
    public DialogueBox(Stage stage) {
        this.stage = stage;
        dialogueBoxInitialisation(true);
    }

    public void dialogueBoxInitialisation(boolean hide) {
        this.hints = new String[][]{};
        this.currentHint = 0;
        this.currentHintLine = 0;
        this.currentMinigame = SNAKE;

        createBackgroundImage();
        createLabel();
        createForwardButton();
        createBackwardButton();
        createPlayButton();

        int totalOptions = 5;
        optionButtons = new TextButton[totalOptions];
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = createOptionButton(i); // Call createOptionButton with the index i
        }

        if (hide) {
            hideDialogueBox();
            isVisible = false;
        } else {
            isVisible = true;
        }

        stage.addActor(backgroundImage);
        stage.addActor(label);
        stage.addActor(forwardButton);
        stage.addActor(backwardButton);
        stage.addActor(playButton);
        for (TextButton button : optionButtons) {
            stage.addActor(button);
        }

        addButtonListeners();

        playButton.setVisible(false);
        for (TextButton button : optionButtons) {
            button.setVisible(false);
        }
    }

    /**
     * Creates a new DialogueBox with the given hint messages.
     *
     * @param labelText The array of hint messages to display.
     */
    public DialogueBox(String[][] labelText) {
        this.stage = ServiceLocator.getRenderService().getStage();
        this.hints = labelText;
        dialogueBoxInitialisation(false);
    }

    /**
     * Creates the background image.
     */
    private void createBackgroundImage() {
        backgroundImage = new Image(new TextureRegionDrawable(BACKGROUND_TEXTURE));

        float desiredHeight = screenHeight * 0.08f;  // 7% of the screen height
        float newWidth = screenWidth;  // Full screen width

        backgroundImage.setSize(newWidth * 0.9f, desiredHeight);
        backgroundImage.setPosition(newWidth * 0.05f, screenHeight * 0.09f);
    }

    /**
     * Resizes the background image for formatting
     */
    public void resizeElements() {
        // resize background image
        if (ServiceLocator.getRenderService().getStage().getViewport() != null) {
            screenWidth = ServiceLocator.getRenderService().getStage().getViewport().getScreenWidth();
            screenHeight = ServiceLocator.getRenderService().getStage().getViewport().getScreenHeight();
        }

        float desiredHeight = screenHeight * 0.08f;  // 7% of the screen height
        float newWidth = screenWidth;  // Full screen width

        backgroundImage.setSize(newWidth * 0.9f, desiredHeight);
        backgroundImage.setPosition(newWidth * 0.05f, screenHeight * 0.09f);

        updateLabelPosition();

        // resize and replace the buttons
        desiredHeight = screenHeight * 0.05f;  // button image height
        newWidth = screenWidth * 0.10f;  // button image width

        forwardButton.setSize(newWidth, desiredHeight);
        forwardButton.setPosition((float) screenWidth / 2 + screenWidth * 0.1f, screenHeight * 0.03f);

        backwardButton.setSize(newWidth, desiredHeight);
        backwardButton.setPosition((float) screenWidth / 2 - screenWidth * 0.1f - backwardButton.getPrefWidth(), screenHeight * 0.03f);

        float buttonWidth = playButton.getWidth();
        float buttonHeight = playButton.getHeight();
        float centerX = (screenWidth - buttonWidth) / 2;
        float centerY = (screenHeight - buttonHeight) / 2;
        playButton.setPosition(centerX, centerY - screenHeight * 0.09f);
        playButton.setSize(newWidth, desiredHeight);

        resizeOptionButtons();
    }

    /**
     * Creates the label for displaying hints.
     */
    private void createLabel() {
        if (hints.length > 0) {
            label = new Label(hints[currentHintLine][currentHint], SKIN, "default-white");
        } else {
            label = new Label("", SKIN, "default-white");
        }
        label.setFontScale(1.5f);

        updateLabelPosition();
    }

    /**
     * Moves the labels position ot fit the dialogue box
     */
    private void updateLabelPosition() {
        float desiredHeight = screenHeight * 0.10f;  // Background image height
        float newWidth = screenWidth * 0.9f;  // Background image width
        label.setSize(newWidth, desiredHeight);
        label.setPosition((screenWidth - label.getWidth()) / 2, screenHeight * 0.08f);
        label.setAlignment(Align.center);
    }

    /**
     * Creates the forward button for navigating to the next hint.
     */
    private void createForwardButton() {
        float desiredHeight = screenHeight * 0.05f;  // button image height
        float newWidth = screenWidth * 0.10f;  // button image width

        TextButton.TextButtonStyle buttonStyle = createButtonStyle();
        forwardButton = new TextButton("Continue", buttonStyle);

        forwardButton.setSize(newWidth, desiredHeight);
        forwardButton.setPosition(((float) screenWidth / 2) + screenWidth * 0.1f, screenHeight * 0.03f);
        forwardButton.getLabel().setAlignment(Align.center);
    }

    /**
     * Creates the backward button for navigating to the previous hint.
     *
     */
    private void createBackwardButton() {
        float desiredHeight = screenHeight * 0.05f;  // button image height
        float newWidth = screenWidth * 0.10f;  // button image width

        TextButton.TextButtonStyle buttonStyle = createButtonStyle();
        backwardButton = new TextButton("Back", buttonStyle);

        backwardButton.setSize(newWidth, desiredHeight);
        backwardButton.setPosition(((float) screenWidth / 2) - screenWidth * 0.1f - backwardButton.getPrefWidth(), screenHeight * 0.03f);
        backwardButton.getLabel().setAlignment(Align.center);
    }

    /**
     * Creates the playButton for booting up mini-games
     */
    public void createPlayButton() {
        TextButton.TextButtonStyle buttonStyle = createButtonStyle();
        playButton = new TextButton("Play Game", buttonStyle);
        playButton.getLabel().setAlignment(Align.center);
        float buttonWidth = playButton.getWidth();
        float buttonHeight = playButton.getHeight();
        float centerX = (screenWidth - buttonWidth) / 2;
        float centerY = (screenHeight - buttonHeight) / 2;

        playButton.setPosition(centerX, centerY - screenHeight * 0.09f);
    }

    /**
     * Creates the optionButton.
     * @param index
     * @return the optionButton instance.
     */
    public TextButton createOptionButton(int index) {
        TextButton.TextButtonStyle buttonStyle = createButtonStyle();
        TextButton optionButton = new TextButton("Option" + Integer.toString(index), buttonStyle);
        optionButton.getLabel().setAlignment(Align.center);
        optionButton.setWidth(400);
        float buttonWidth = optionButton.getWidth();
        float buttonHeight = optionButton.getHeight();
        float centerX = (screenWidth - buttonWidth) / 2 + 400;
        float centerY = (screenHeight - buttonHeight - index * 75) / 2 - 200;

        optionButton.setPosition(centerX, centerY);
        return optionButton;
    }

    /**
     * Creates a style for the buttons.
     *
     * @return The TextButtonStyle instance.
     */
    private TextButton.TextButtonStyle createButtonStyle() {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = SKIN.getFont("button");
        buttonStyle.fontColor = SKIN.getColor("white");
        buttonStyle.downFontColor = SKIN.getColor("white");
        buttonStyle.overFontColor = SKIN.getColor("black");
        buttonStyle.up = new TextureRegionDrawable(BUTTON_IMAGE_TEXTURE);
        buttonStyle.down = new TextureRegionDrawable(BUTTON_IMAGE_TEXTURE);
        buttonStyle.over = new TextureRegionDrawable(BUTTON_HOVER_TEXTURE);
        return buttonStyle;
    }

    /**
     * Adds listeners to the forward, backward and play buttons.
     */
    private void addButtonListeners() {
        forwardButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleForwardButtonClick(); // Handle forward button click
                return true;
            }
        });


        backwardButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleBackwardButtonClick();
                return true;
            }
        });

        // Listener for the playButton, will boot up a specific mini-game
        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hideDialogueBox(); // hides dialogue when player returns to the screen
                if (playButton != null) playButton.setVisible(false);
                GdxGame gdxGame = GdxGameManager.getInstance();
                // Could potentially override snake hints here for post game messages
                if (currentMinigame == SNAKE) {
                    gdxGame.enterSnakeScreen();
                } else if (currentMinigame == BIRD) {
                    gdxGame.enterBirdieDashScreen();
                } else if (currentMinigame == MAZE) {
                    gdxGame.enterMazeGameScreen();
                }
                return true;
            }
        });

        for (int i = 0; i <= optionButtons.length - 1; i++) {
            final int iteration = i;
            optionButtons[iteration].addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    handleOptionButtonClick(iteration);
                    return true;
                }
            });
        }
    }

    /**
     * Returns the array of hint messages.
     *
     * @return An array of strings containing the hint messages.
     */
    public String[][] getHints() {
        return this.hints;
    }

    /**
     * Gets the option buttons.
     * @return the array of option buttons.
     */
    public TextButton[] getOptionButtons() {
        return optionButtons;
    }

    /**
     * Returns the index of the current hint being displayed.
     *
     * @return The index of the current hint.
     */
    public int getCurrentHint() {
        return this.currentHint;
    }

    /**
     * Returns the index of the current hint line being displayed.
     *
     * @return The index of the current hint line.
     */
    public int getCurrentHintLine() {
        return this.currentHintLine;
    }

    /**
     * Returns the Label object used for displaying hints.
     *
     * @return The Label instance for displaying hints.
     */
    public Label getLabel() {
        return this.label;
    }

    /**
     * Handles the forward button click event to navigate to the next hint.
     * Updates the label text to the next hint in the array and repositions the label.
     */
    public void handleForwardButtonClick() {
        for (TextButton button : optionButtons) {
            if (button != null) button.setVisible(false);
        }

        if (currentHint + 1 == hints[currentHintLine].length) {
            hideDialogueBox();
            return;
        }

        currentHint = (currentHint + 1) % hints[currentHintLine].length;

        String text = hints[currentHintLine][currentHint];
        text = minigameCheck(text);
        text = optionsCheck(text);
        label.setText(text);
        updateLabelPosition();
        showAppropriateButtons();
    }

    /**
     * Handles the backward button click event to navigate to the previous hint.
     * Updates the label text to the previous hint in the array and repositions the label.
     */
    public void handleBackwardButtonClick() {
        for (TextButton button : optionButtons) {
            if (button != null) button.setVisible(false);
        }
        currentHint = (currentHint - 1 + hints[currentHintLine].length) % hints[currentHintLine].length;
        String text = hints[currentHintLine][currentHint];
        text = minigameCheck(text);
        text = optionsCheck(text);
        label.setText(text);
        updateLabelPosition();
        showAppropriateButtons();
    }

    /**
     * Resizes and spaces the buttons based on the number of currently visible options.
     */
    public void resizeOptionButtons() {
        // Determine the number of visible buttons (i.e., buttons that have been set to visible in optionsCheck)
        int visibleButtonCount = 0;
        for (TextButton button : optionButtons) {
            if (button.isVisible()) {
                visibleButtonCount++;
            }
        }

        if (visibleButtonCount == 0) {
            return; // No buttons to resize or space if none are visible
        }

        // Get the background image dimensions and position
        float backgroundXPosition = screenWidth * 0.05f; // LHS of the background image
        float backgroundWidth = screenWidth * 0.9f;      // Width of the background image

        // Calculate the button width based on the number of visible buttons and available space
        float buttonWidth = (backgroundWidth * 0.8f) / visibleButtonCount; // Adjust width relative to background
        float spacing = (backgroundWidth - visibleButtonCount * buttonWidth) / (visibleButtonCount - 1); // Space between buttons

        // Set a fixed y position for the buttons above the background image
        float backgroundHeight = screenHeight * 0.08f;  // Height of the background image
        float backgroundYPosition = screenHeight * 0.09f; // Y position of the background image
        float buttonYPosition = backgroundYPosition + backgroundHeight + 10; // Spacing above background

        // Resize and reposition each visible button
        int buttonIndex = 0;
        for (TextButton optionButton : optionButtons) {
            if (optionButton.isVisible()) {
                // Set the new width for the button
                optionButton.setWidth(buttonWidth);

                // Calculate the x position for evenly spacing the buttons
                float xPosition = backgroundXPosition + buttonIndex * (buttonWidth + spacing);

                // Set the button's position
                optionButton.setPosition(xPosition, buttonYPosition);

                // Shrink label text if too large
                Label optionLabel = optionButton.getLabel();
                float newWidth = optionButton.getWidth() - 100; // Add padding to prevent text overflow at the edges

                if (optionLabel.getPrefWidth() > newWidth) {
                    // Scale down font size proportionally to fit the button width
                    float scaleFactor = newWidth / optionLabel.getPrefWidth();
                    optionLabel.setFontScale(scaleFactor);
                } else {
                    // Reset font scale to default if no shrinking is needed
                    optionLabel.setFontScale(1f);
                }

                buttonIndex++;
            }
        }
    }

    /**
     * Determines when to hide and show forward / backward buttons on clicks.
     */
    public void showAppropriateButtons() {
        // Hide the continue button if there are options to pick
        if (optionButtons[0].isVisible() && currentHint == hints[currentHintLine].length - 1) {
            forwardButton.setVisible(false);
        } else if (!forwardButton.isVisible()) {
            forwardButton.setVisible(true);
        }

        if (currentHint == 0) {
            backwardButton.setVisible(false);
        } else if (!backwardButton.isVisible()) {
            backwardButton.setVisible(true);
        }
    }

    /**
     * Handles the option button click event to navigate to the specific hint.
     * Updates the label text to the certain hint in the array and repositions the label.
     */
    public void handleOptionButtonClick(int index) {
        for (TextButton button : optionButtons) {
            if (button != null) button.setVisible(false);
        }
        currentHintLine = (currentHintLine + index + 1) % hints.length;
        currentHint = 0;
        String text = hints[currentHintLine][currentHint];
        text = minigameCheck(text);
        text = optionsCheck(text);
        label.setText(text);
        updateLabelPosition();
        showAppropriateButtons();
    }

    /**
     * Checks if the current text on the label contains either of the following flags at the
     * beginning of the string. This then shows the playButton and assigns the corresponding
     * mini-game to be played.
     *  /ms: mini-game snake
     *  /mb: mini-game birdie dash
     *  /mu: underwater maze
     * @param text the label text to be shown in the dialogue.
     * @return the altered text without the flag to be shown in the dialogue box.
     */
    public String minigameCheck(String text) {
        if (hints[currentHintLine][currentHint].startsWith("/ms")) {
            if (playButton != null) playButton.setVisible(true);
            currentMinigame = SNAKE;
            return text.substring(3);
        } else if (hints[currentHintLine][currentHint].startsWith("/mb")){
            if (playButton != null) playButton.setVisible(true);
            currentMinigame = BIRD;
            return text.substring(3);
        } else if (hints[currentHintLine][currentHint].startsWith("/mu")) {
            if (playButton != null) playButton.setVisible(true);
            currentMinigame = MAZE;
            return text.substring(3);
        } else {
            if (playButton != null) playButton.setVisible(false);
            return text;
        }
    }

    /**
     * Checks if the current text on the label contains either of the following flags at the
     * beginning of the string. This then shows the option buttons.
     *  /c: indicates that this is a dialogue with options
     *  /s00: string after this is one of the options user can choose. The number is replaced with what index the
     *      option takes the user to
     * @param text the label text to be shown in the dialogue.
     * @return the altered text without the flag to be shown in the dialogue box.
     */
    public String optionsCheck(String text) {
        if (hints[currentHintLine][currentHint].startsWith("/c")) {
            String[] options = text.split("/s");
            for (int i = 1; i < options.length; i++) {
                optionButtons[i - 1].setText(options[i].substring(2));
                optionButtons[i - 1].setVisible(true);
            }

            resizeOptionButtons();
            return options[0].substring(2);
        } else {
            for (TextButton button : optionButtons) {
                if (button != null) button.setVisible(false);
            }
            return text;
        }
    }

    /**
     * Hides the dialogue box by setting all its components (background image, label, and buttons) to invisible.
     */
    public void hideDialogueBox() {
        this.isVisible = false;
        if (backgroundImage != null) backgroundImage.setVisible(false);
        if (label != null) label.setVisible(false);
        if (forwardButton != null) forwardButton.setVisible(false);
        if (backwardButton != null) backwardButton.setVisible(false);
        if (playButton != null) playButton.setVisible(false);
        for (TextButton button : optionButtons) {
            if (button != null) button.setVisible(false);
        }
    }

    /**
     * Displays the dialogue box with the provided hint messages.
     * Sets the first hint message, shows all components (background image, label, and buttons),
     * and resets the current hint index to 0.
     *
     * @param hints An array of strings containing the hint messages to display.
     */
    public void showDialogueBox(String[][] hints) {
        this.isVisible = true;
        if ( ServiceLocator.getRenderService().getStage().getViewport() != null) {
            screenWidth = ServiceLocator.getRenderService().getStage().getViewport().getScreenWidth();
            screenHeight = ServiceLocator.getRenderService().getStage().getViewport().getScreenHeight();
        }

        this.hints = hints;
        this.currentHint = 0; // Reset to the first hint
        this.currentHintLine = 0; // Reset to the first hint
        this.label.setText(hints[currentHintLine][currentHint]);

        // Update positions in case screen resize
        updateLabelPosition();
        if (ServiceLocator.getRenderService().getStage().getViewport() != null) {
            resizeElements();
        }

        if (hints[currentHintLine].length > 1) {
            if (forwardButton != null) forwardButton.setVisible(true);
            if (backwardButton != null) backwardButton.setVisible(true);
        } else {
            if (forwardButton != null) forwardButton.setVisible(false);
            if (backwardButton != null) backwardButton.setVisible(false);
        }
        if (backgroundImage != null) {
            backgroundImage.setVisible(true);
        }
        if (label != null) this.label.setVisible(true);
        showAppropriateButtons();
    }

    /**
     * Removes all components of the dialogue box from the stage.
     * This should be called to clean up resources when the dialogue box is no longer needed.
     */
    public void dispose() {
        if (backgroundImage != null) backgroundImage.remove();
        if (label != null) label.remove();
        if (forwardButton != null) forwardButton.remove();
        if (backwardButton != null) backwardButton.remove();
        if (playButton != null) playButton.remove();
    }

    /**
     * Returns the TextButton instance used for the "Continue" (forward) action.
     *
     * @return The TextButton instance for the forward button.
     */
    public TextButton getForwardButton() {
        return forwardButton;
    }

    /**
     * Returns the TextButton instance used for the "Back" (backward) action.
     *
     * @return The TextButton instance for the backward button.
     */
    public TextButton getBackwardButton() {
        return backwardButton;
    }

    /**
     * Returns whether the dialogueBox is visible or not.
     *
     * @return boolean representing if the dialogue box is visible.
     */
    public boolean getIsVisible() {
       return isVisible;
    }
}
