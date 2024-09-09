package com.csse3200.game.ui;

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
import com.csse3200.game.components.minigames.MiniGameNames;

import static com.csse3200.game.components.minigames.MiniGameNames.*;

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
    private TextButton[] optionButtons;
    private final int screenWidth = Gdx.graphics.getWidth();
    private final int screenHeight = Gdx.graphics.getHeight();

    private String[][] hints;
    private int currentHint;
    private int currentHintLine;
    private MiniGameNames currentMinigame;

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

        this.backgroundImage = createBackgroundImage();
        this.label = createLabel();
        this.forwardButton = createForwardButton();
        this.backwardButton = createBackwardButton();
        this.playButton = createPlayButton();
        this.optionButtons = new TextButton[]{createOptionButton(0), createOptionButton(1), createOptionButton(2),
                createOptionButton(3), createOptionButton(4)};

        if (hide) {
            hideDialogueBox();
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
     *
     * @return The Image instance.
     */
    private Image createBackgroundImage() {
        float desiredHeight = 700;
        float imageWidth = BACKGROUND_TEXTURE.getWidth();
        float imageHeight = BACKGROUND_TEXTURE.getHeight();
        float ratio = imageWidth / imageHeight;
        float newWidth = desiredHeight * ratio;

        Image backgroundImage = new Image(new TextureRegionDrawable(BACKGROUND_TEXTURE));
        backgroundImage.setSize(newWidth, desiredHeight);
        backgroundImage.setPosition((screenWidth - newWidth) / 2, -125);

        return backgroundImage;
    }

    /**
     * Creates the label for displaying hints.
     *
     * @return The Label instance.
     */
    private Label createLabel() {
        if (hints.length > 0) {
            label = new Label(hints[currentHintLine][currentHint], SKIN, "default-white");
        } else {
            label = new Label("", SKIN, "default-white");
        }
        label.setFontScale(1.5f);

        updateLabelPosition();

        return label;
    }

    /**
     * Moves the labels position ot fit the dialogue box
     */
    private void updateLabelPosition() {
        float middle = stage.getWidth();
        float labelWidth = label.getPrefWidth();
        float labelHeight = label.getPrefHeight();
        float labelX = (middle - labelWidth) / 2;
        float labelY = -210 + (backgroundImage.getHeight() - labelHeight) / 2;
        label.setPosition(labelX, labelY, Align.center);
    }

    /**
     * Creates the forward button for navigating to the next hint.
     *
     * @return The TextButton instance.
     */
    private TextButton createForwardButton() {
        TextButton.TextButtonStyle buttonStyle = createButtonStyle();
        TextButton forwardButton = new TextButton("Continue", buttonStyle);
        forwardButton.getLabel().setAlignment(Align.center);

        float buttonWidth = forwardButton.getWidth();
        float centerX = (screenWidth - (2 * buttonWidth + 35)) / 2; // 35 is spacing between buttons

        forwardButton.setPosition(centerX + buttonWidth + 20, label.getY() - 100);

        return forwardButton;
    }

    /**
     * Creates the backward button for navigating to the previous hint.
     *
     * @return The TextButton instance.
     */
    private TextButton createBackwardButton() {
        TextButton.TextButtonStyle buttonStyle = createButtonStyle();
        TextButton backwardButton = new TextButton("Back", buttonStyle);
        backwardButton.getLabel().setAlignment(Align.center);

        float buttonWidth = backwardButton.getWidth();
        float centerX = (screenWidth - (2 * buttonWidth + 35)) / 2; // 35 is spacing between buttons

        backwardButton.setPosition(centerX, label.getY() - 100);

        return backwardButton;
    }

    /**
     * Creates the playButton for booting up mini-games
     *
     * @return the playButton instance.
     */
    public TextButton createPlayButton() {
        TextButton.TextButtonStyle buttonStyle = createButtonStyle();
        TextButton playButton = new TextButton("Play Game", buttonStyle);
        playButton.getLabel().setAlignment(Align.center);
        float buttonWidth = playButton.getWidth();
        float buttonHeight = playButton.getHeight();
        float centerX = (screenWidth - buttonWidth) / 2;
        float centerY = (screenHeight - buttonHeight) / 2;

        playButton.setPosition(centerX, centerY - 300);
        return playButton;
    }

    /**
     * Creates the optionButton.
     *
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
                handleForwardButtonClick();
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

        // Listener for the playButtton, will boot up a specific mini-game
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
                    // TODO: Implement bird game (sprint 3)
                } else if (currentMinigame == MAZE) {
                    // TODO: Implement underwater maze (sprint 4)
                }
                return true;
            }
        });

        optionButtons[0].addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleOptionButtonClick(0);
                return true;
            }
        });
        optionButtons[1].addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleOptionButtonClick(1);
                return true;
            }
        });
        optionButtons[2].addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleOptionButtonClick(2);
                return true;
            }
        });
        optionButtons[3].addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleOptionButtonClick(3);
                return true;
            }
        });
        optionButtons[4].addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleOptionButtonClick(4);
                return true;
            }
        });
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
        currentHint = (currentHint + 1) % hints[currentHintLine].length;
        String text = hints[currentHintLine][currentHint];
        text = minigameCheck(text);
        text = optionsCheck(text);
        label.setText(text);
        updateLabelPosition();
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
    }

    /**
     * Checks if the current text on the label contains either of the following flags at the
     * beginning of the string. This then shows the playButton and assigns the corresponding
     * mini-game to be played.
     *  /ms: minigame snake
     *  /mb: minigame birdie dash
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
            return options[0].substring(2);
        } else {
            for (TextButton button : optionButtons) {
                if (button != null) button.setVisible(false);
            }
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
        this.hints = hints;
        this.currentHint = 0; // Reset to the first hint
        this.currentHintLine = 0; // Reset to the first hint
        this.label.setText(hints[currentHintLine][currentHint]);
        updateLabelPosition(); // Update position after setting text
        if (hints[currentHintLine].length > 1) {
            if (forwardButton != null) forwardButton.setVisible(true);
            if (backwardButton != null) backwardButton.setVisible(true);
        } else {
            if (forwardButton != null) forwardButton.setVisible(false);
            if (backwardButton != null) backwardButton.setVisible(false);
        }
        if (backgroundImage != null) backgroundImage.setVisible(true);
        if (label != null) this.label.setVisible(true);
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
}