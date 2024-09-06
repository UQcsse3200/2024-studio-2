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
    private TextButton option1Button;
    private TextButton option2Button;
    private TextButton option3Button;
    private final int screenWidth = Gdx.graphics.getWidth();

    private String[][] hints;
    private int currentHint;
    private int currentHintLine;

    /**
     * Creates a new DialogueBox with the given hint messages.
     *
     * @param labelText The array of hint messages to display.
     */
    public DialogueBox(String[][] labelText) {
        this.hints = labelText;
        screenInit();
    }

    /**
     * Initializes the screen components and returns Void.
     *
     * @return Void
     */
    private void screenInit() {
        this.stage = ServiceLocator.getRenderService().getStage();
        this.currentHint = 0;
        this.currentHintLine = 0;

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
        String currentHintText = hints[currentHintLine][currentHint];
        if (currentHintText.startsWith("/c")) {
            currentHintText = currentHintText.substring(2);
            String[] options = currentHintText.split("/s");
            currentHintText = options[0];
        }
        this.label = new Label(currentHintText, SKIN, "default-white");
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

//        return null;
    }

    public String[][] getHints() {
        return this.hints;
    }

    public int getCurrentHint() {
        return this.currentHint;
    }

    public Label getLabel() {
        return this.label;
    }

    private void handleForwardButtonClick() {
        currentHint = (currentHint + 1) % (hints[currentHintLine].length);
        String currentHintText = hints[currentHintLine][currentHint];
        if (currentHintText.startsWith("/c")) {
            currentHintText = currentHintText.substring(2);
            String[] options = currentHintText.split("/s");
            currentHintText = options[0];

            float labelWidth = label.getPrefWidth();
            float labelHeight = label.getPrefHeight();
            float labelX = backgroundImage.getX() + (backgroundImage.getWidth() - labelWidth) / 2;
            float labelY = -220 + (backgroundImage.getHeight() - labelHeight) / 2;
            // Create buttons
            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.font = SKIN.getFont("button");
            buttonStyle.fontColor = SKIN.getColor("white");
            buttonStyle.downFontColor = SKIN.getColor("white");
            buttonStyle.overFontColor = SKIN.getColor("black");
            buttonStyle.up = new TextureRegionDrawable(BUTTON_IMAGE_TEXTURE);
            buttonStyle.down = new TextureRegionDrawable(BUTTON_IMAGE_TEXTURE);
            buttonStyle.over = new TextureRegionDrawable(BUTTON_HOVER_TEXTURE);

            this.option1Button = new TextButton(options[1], buttonStyle);
            this.option2Button = new TextButton(options[2], buttonStyle);
            this.option3Button = new TextButton(options[3], buttonStyle);
//            option1Button.padLeft(50f);
//            option1Button.padTop(50f);
//            option2Button.padLeft(55f);
//            option2Button.padTop(50f);
//            option3Button.padLeft(60f);
//            option3Button.padTop(50f);

            option1Button.getLabel().setAlignment(Align.center);

            float buttonWidth = option1Button.getWidth();
            float centerX = (screenWidth - (2 * buttonWidth + 35)) / 2; // 35 is spacing between buttons

            option1Button.setPosition(centerX + 400, labelY + 100);
            option2Button.setPosition(centerX + 400, labelY + 50);
            option3Button.setPosition(centerX + 400, labelY - 0);

            stage.addActor(option1Button);
            stage.addActor(option2Button);
            stage.addActor(option3Button);

            // Add input listeners
            option1Button.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    optionButtonClick(1);
                    return true;
                }
            });

            option2Button.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    optionButtonClick(2);
                    return true;
                }
            });

            option3Button.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    optionButtonClick(3);
                    return true;
                }
            });
        }
        label.setText(currentHintText);
    }

    private void handleBackwardButtonClick() {
        currentHint = (currentHint - 1 + hints[currentHintLine].length) % hints[currentHintLine].length;
        String currentHintText = hints[currentHintLine][currentHint];
        if (currentHintText.startsWith("/c")) {
            currentHintText = currentHintText.substring(2);
            String[] options = currentHintText.split("/s");
            currentHintText = options[0];
        }
        label.setText(currentHintText);
    }

    private void optionButtonClick(int index) {
        currentHintLine += index % hints.length;
        currentHint = 0;
        String currentHintText = hints[currentHintLine][currentHint];
        if (currentHintText.startsWith("/c")) {
            currentHintText = currentHintText.substring(2);
            String[] options = currentHintText.split("/s");
            currentHintText = options[0];
        }
        label.setText(currentHintText);
    }

    public void hideDialogueBox() {
        if (backgroundImage != null) backgroundImage.setVisible(false);
        if (label != null) label.setVisible(false);
        if (forwardButton != null) forwardButton.setVisible(false);
        if (backwardButton != null) backwardButton.setVisible(false);
    }

    public void showDialogueBox(String[][] hints) {
        this.hints = hints;
        this.label.setText(hints[currentHintLine][0]);
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

// test commit bc my git is cooked