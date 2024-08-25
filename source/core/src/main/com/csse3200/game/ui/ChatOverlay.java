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

/**
 * Represents a chat overlay UI component that displays a series of hint messages
 * and allows navigation between them using forward and backward buttons.
 */
public class ChatOverlay {

    private final Stage stage;
    private final Skin skin;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private final Label label;
    private TextButton forwardButton;
    private TextButton backwardButton;
    private final int screenWidth = Gdx.graphics.getWidth();

    private final String[] hints;
    private int currentHint;

    /**
     * Creates a new ChatOverlay with the given hint messages.
     *
     * @param labelText The array of hint messages to display.
     */
    public ChatOverlay(String[] labelText) {
        this.stage = ServiceLocator.getRenderService().getStage();
        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        this.hints = labelText;
        this.currentHint = 0;

        createBackgroundImage("images/blue-bar.png", 700);

        label = new Label(hints[currentHint], skin, "default-white");
        label.setFontScale(1.5f);

        float labelWidth = label.getPrefWidth();
        float labelHeight = label.getPrefHeight();
        float labelX = backgroundImage.getX() + (backgroundImage.getWidth() - labelWidth) / 2;
        float labelY = -220 + (backgroundImage.getHeight() - labelHeight) / 2;

        label.setPosition(labelX, labelY);
        stage.addActor(label);

        createButtons(labelY);
    }

    /**
     * Creates a background image for the chat overlay with the specified image path and height.
     *
     * @param backgroundPath The file path of the background image.
     * @param desiredHeight The desired height for the background image.
     */
    private void createBackgroundImage(String backgroundPath, float desiredHeight) {
        this.backgroundTexture = new Texture(Gdx.files.internal(backgroundPath));

        float imageWidth = this.backgroundTexture.getWidth();
        float imageHeight = this.backgroundTexture.getHeight();
        float ratio = imageWidth / imageHeight;
        float newWidth = desiredHeight * ratio;

        this.backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setSize(newWidth, desiredHeight);
        backgroundImage.setPosition((screenWidth - newWidth) / 2, -125);

        this.stage.addActor(backgroundImage);
    }

    /**
     * Creates forward and backward navigation buttons for cycling through hint messages.
     *
     * @param labelY The y-coordinate for button positioning relative to the label.
     */
    private void createButtons(float labelY) {
        // Load button images
        Texture buttonImageTexture = new Texture(Gdx.files.internal("images/blue-button.png"));
        Texture buttonHoverTexture = new Texture(Gdx.files.internal("images/blue-b-hover.png"));

        TextureRegionDrawable buttonImageDrawable = new TextureRegionDrawable(buttonImageTexture);
        TextureRegionDrawable buttonHoverDrawable = new TextureRegionDrawable(buttonHoverTexture);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = skin.getFont("button");
        buttonStyle.fontColor = skin.getColor("white");
        buttonStyle.downFontColor = skin.getColor("white");
        buttonStyle.overFontColor = skin.getColor("black");

        buttonStyle.up = buttonImageDrawable;
        buttonStyle.down = buttonImageDrawable;
        buttonStyle.over = buttonHoverDrawable;

        backwardButton = new TextButton("Back", buttonStyle);
        forwardButton = new TextButton("Continue", buttonStyle);
        backwardButton.padLeft(50f); // Align text in button
        forwardButton.padLeft(55f); // Align text in button

        forwardButton.getLabel().setAlignment(Align.center);

        float buttonWidth = forwardButton.getWidth();
        float centerX = (screenWidth - (2 * buttonWidth + 35)) / 2; // 35 is spacing between buttons

        forwardButton.setPosition(centerX + buttonWidth + 20, labelY - 275);
        backwardButton.setPosition(centerX, labelY - 275);

        stage.addActor(forwardButton);
        stage.addActor(backwardButton);

        // Add input listeners to the buttons for navigation
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
     * Handles the forward button click event to show the next hint message.
     * Cycles back to the first hint after reaching the end.
     */
    private void handleForwardButtonClick() {
        currentHint = (currentHint + 1) % (hints.length);
        label.setText(hints[currentHint]);
    }

    /**
     * Handles the backward button click event to show the previous hint message.
     * Cycles back to the last hint after reaching the beginning.
     */
    private void handleBackwardButtonClick() {
        currentHint = (currentHint - 1 + hints.length) % hints.length;
        label.setText(hints[currentHint]);
    }

    /**
     * Disposes of the resources used by the chat overlay, including textures and UI components.
     * Ensures that all resources are properly cleaned up to prevent memory leaks.
     */
    public void dispose() {
        if (backgroundImage != null) {
            backgroundImage.remove();
        }

        if (label != null) {
            label.remove();
        }

        if (forwardButton != null) {
            forwardButton.remove();
        }

        if (backwardButton != null) {
            backwardButton.remove();
        }

        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
