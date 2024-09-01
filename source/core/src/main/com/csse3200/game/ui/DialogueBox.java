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
import com.csse3200.game.concurrency.JobSystem;

/**
 * Represents a chat overlay UI component that displays a series of hint messages
 * and allows navigation between them using forward and backward buttons.
 */
public class DialogueBox {

    private static final Skin SKIN = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    private static final Texture BACKGROUND_TEXTURE = new Texture(Gdx.files.internal("images/blue-bar.png"));
    private static final Texture BUTTON_IMAGE_TEXTURE = new Texture(Gdx.files.internal("images/blue-button.png"));
    private static final Texture BUTTON_HOVER_TEXTURE = new Texture(Gdx.files.internal("images/blue-b-hover.png"));

    private Stage stage;
    private Label label;
    private Image backgroundImage;
    private TextButton forwardButton;
    private TextButton backwardButton;
    private final int screenWidth = Gdx.graphics.getWidth();

    private String[] hints;
    private int currentHint;

    /**
     * Creates a new DialogueBox with the given hint messages.
     *
     * @param labelText The array of hint messages to display.
     */
    public DialogueBox(String[] labelText) {
        this.hints = labelText;
        JobSystem.launch(this::screenInit);
    }

    /**
     * Initializes the screen components and returns Void.
     *
     * @return Void
     */
    private Void screenInit() {
        this.stage = ServiceLocator.getRenderService().getStage();
        this.currentHint = 0;

        // Create background image
        float desiredHeight = 700;
        float imageWidth = BACKGROUND_TEXTURE.getWidth();
        float imageHeight = BACKGROUND_TEXTURE.getHeight();
        float ratio = imageWidth / imageHeight;
        float newWidth = desiredHeight * ratio;

        this.backgroundImage = new Image(new TextureRegionDrawable(BACKGROUND_TEXTURE));
        backgroundImage.setSize(newWidth, desiredHeight);
        backgroundImage.setPosition((screenWidth - newWidth) / 2, -125);
        stage.addActor(backgroundImage);

        // Create label
        this.label = new Label(hints[currentHint], SKIN, "default-white");
        this.label.setFontScale(1.5f);

        float labelWidth = label.getPrefWidth();
        float labelHeight = label.getPrefHeight();
        float labelX = backgroundImage.getX() + (backgroundImage.getWidth() - labelWidth) / 2;
        float labelY = -220 + (backgroundImage.getHeight() - labelHeight) / 2;

        label.setPosition(labelX, labelY);
        stage.addActor(label);

        // Create buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = SKIN.getFont("button");
        buttonStyle.fontColor = SKIN.getColor("white");
        buttonStyle.downFontColor = SKIN.getColor("white");
        buttonStyle.overFontColor = SKIN.getColor("black");
        buttonStyle.up = new TextureRegionDrawable(BUTTON_IMAGE_TEXTURE);
        buttonStyle.down = new TextureRegionDrawable(BUTTON_IMAGE_TEXTURE);
        buttonStyle.over = new TextureRegionDrawable(BUTTON_HOVER_TEXTURE);

        this.backwardButton = new TextButton("Back", buttonStyle);
        this.forwardButton = new TextButton("Continue", buttonStyle);
        backwardButton.padLeft(50f);
        forwardButton.padLeft(55f);

        forwardButton.getLabel().setAlignment(Align.center);

        float buttonWidth = forwardButton.getWidth();
        float centerX = (screenWidth - (2 * buttonWidth + 35)) / 2; // 35 is spacing between buttons

        forwardButton.setPosition(centerX + buttonWidth + 20, labelY - 275);
        backwardButton.setPosition(centerX, labelY - 275);

        stage.addActor(forwardButton);
        stage.addActor(backwardButton);

        // Add input listeners
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

        return null;
    }

    public String[] getHints() {
        return this.hints;
    }

    public int getCurrentHint() {
        return this.currentHint;
    }

    public Label getLabel() {
        return this.label;
    }

    private void handleForwardButtonClick() {
        currentHint = (currentHint + 1) % (hints.length);
        label.setText(hints[currentHint]);
    }

    private void handleBackwardButtonClick() {
        currentHint = (currentHint - 1 + hints.length) % hints.length;
        label.setText(hints[currentHint]);
    }

    public void hideDialogueBox() {
        if (backgroundImage != null) backgroundImage.setVisible(false);
        if (label != null) label.setVisible(false);
        if (forwardButton != null) forwardButton.setVisible(false);
        if (backwardButton != null) backwardButton.setVisible(false);
    }

    public void showDialogueBox(String[] hints) {
        this.hints = hints;
        this.label.setText(hints[0]);
        if (backgroundImage != null) backgroundImage.setVisible(true);
        if (label != null) label.setVisible(true);
        if (forwardButton != null) forwardButton.setVisible(true);
        if (backwardButton != null) backwardButton.setVisible(true);
    }

    public void dispose() {
        if (backgroundImage != null) backgroundImage.remove();
        if (label != null) label.remove();
        if (forwardButton != null) forwardButton.remove();
        if (backwardButton != null) backwardButton.remove();
    }

    public TextButton getForwardButton() {return forwardButton;}

    public TextButton getBackwardButton() {
        return backwardButton;
    }
}
