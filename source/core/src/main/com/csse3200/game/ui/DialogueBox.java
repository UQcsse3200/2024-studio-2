package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private final int screenWidth = Gdx.graphics.getWidth();
    private final int screenHeight = Gdx.graphics.getHeight();

    private String[] hints;
    private int currentHint;

    /**
     * Creates a new base DialogueBox with the given hint messages.
     */
    public DialogueBox(Stage stage) {
        this.stage = stage;
        dialogueBoxInitialisation(true);
    }

    public void dialogueBoxInitialisation(boolean hide) {
        this.hints = new String[]{};
        this.currentHint = 0;

        this.backgroundImage = createBackgroundImage();
        this.label = createLabel();
        this.forwardButton = createForwardButton();
        this.backwardButton = createBackwardButton();
        this.playButton = createPlayButton();

        if (hide) {
            hideDialogueBox();
        }

        stage.addActor(backgroundImage);
        stage.addActor(label);
        stage.addActor(forwardButton);
        stage.addActor(backwardButton);

        addButtonListeners();

        createCenterButton();
        playButton.setVisible(false);
    }

    /**
     * Creates a new DialogueBox with the given hint messages.
     *
     * @param labelText The array of hint messages to display.
     */
    public DialogueBox(String[] labelText) {
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
            label = new Label(hints[currentHint], SKIN, "default-white");
        } else {
            label = new Label("", SKIN, "default-white");
        }
        label.setFontScale(1.5f);

        updateLabelPosition();

        return label;
    }

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
        forwardButton.padLeft(55f);
        forwardButton.getLabel().setAlignment(Align.center);

        float buttonWidth = forwardButton.getWidth();
        float centerX = (screenWidth - (2 * buttonWidth + 35)) / 2; // 35 is spacing between buttons

        forwardButton.setPosition(centerX + buttonWidth + 20, label.getY() - 290);

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
        backwardButton.padLeft(50f);

        float buttonWidth = backwardButton.getWidth();
        float centerX = (screenWidth - (2 * buttonWidth + 35)) / 2; // 35 is spacing between buttons

        backwardButton.setPosition(centerX, label.getY() - 290);

        return backwardButton;
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
     * Adds listeners to the forward and backward buttons.
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
    }

    /**
     * Returns the array of hint messages.
     *
     * @return An array of strings containing the hint messages.
     */
    public String[] getHints() {
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

        currentHint = (currentHint + 1) % hints.length;
        String text = hints[currentHint];
        text = minigameCheck(text);
        label.setText(text);
        updateLabelPosition();
    }

    public String minigameCheck(String text) {
        if (hints[currentHint].startsWith("/ms")) {
            playButton.setVisible(true);
            return text.substring(3);
        } else {
            playButton.setVisible((false));
            return text;
        }
    }

    public TextButton createPlayButton() {
        TextButton.TextButtonStyle buttonStyle = createButtonStyle();
        TextButton playButton = new TextButton("Play Game", buttonStyle);
        playButton.padLeft(55f);
        playButton.getLabel().setAlignment(Align.center);
        return playButton;
    }
    private void createCenterButton() {
        float buttonWidth = playButton.getWidth();
        float buttonHeight = playButton.getHeight();
        float centerX = (screenWidth - buttonWidth) / 2; // 35 is spacing between buttons
        float centerY = (screenHeight - buttonHeight) / 2;

        playButton.setPosition(centerX, centerY - 200);

        if (playButton != null) playButton.setVisible((true));

        // Add the button to the stage
        stage.addActor(playButton);

        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Dummy action for now
                System.out.println("Play Game button clicked!");
                hideDialogueBox();
                if (playButton != null) playButton.setVisible((false));
                GdxGame gdxGame = GdxGameManager.getInstance();
                gdxGame.enterSnakeScreen();
                return true;
            }
        });
    }

    /**
     * Handles the backward button click event to navigate to the previous hint.
     * Updates the label text to the previous hint in the array and repositions the label.
     */
    public void handleBackwardButtonClick() {
        currentHint = (currentHint - 1 + hints.length) % hints.length;
        String text = hints[currentHint];
        text = minigameCheck(text);
        label.setText(text);
        updateLabelPosition();
    }

    /**
     * Hides the dialogue box by setting all its components (background image, label, and buttons) to invisible.
     */
    public void hideDialogueBox() {
        if (backgroundImage != null) backgroundImage.setVisible(false);
        if (label != null) label.setVisible(false);
        if (forwardButton != null) forwardButton.setVisible(false);
        if (backwardButton != null) backwardButton.setVisible(false);
    }

    /**
     * Displays the dialogue box with the provided hint messages.
     * Sets the first hint message, shows all components (background image, label, and buttons),
     * and resets the current hint index to 0.
     *
     * @param hints An array of strings containing the hint messages to display.
     */
    public void showDialogueBox(String[] hints) {
        this.hints = hints;
        this.currentHint = 0; // Reset to the first hint
        this.label.setText(hints[currentHint]);
        updateLabelPosition(); // Update position after setting text
        if (hints.length > 1) {
            if (forwardButton != null) forwardButton.setVisible(true);
            if (backwardButton != null) backwardButton.setVisible(true);
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