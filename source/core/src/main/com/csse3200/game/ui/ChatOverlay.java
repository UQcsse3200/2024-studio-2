package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;



public class ChatOverlay {

    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Label label;
    private TextButton forwardButton;
    private TextButton backwardButton;
    private final int screenWidth = Gdx.graphics.getWidth();

    private String[] hints;
    private int currentHint;

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

        createButtons(labelX, labelY, backgroundImage.getWidth());
    }

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

    private void createButtons(float labelX, float labelY, float newWidth) {

        // load button image
        Texture buttonImageTexture = new Texture(Gdx.files.internal("images/blue-button.png"));
        Texture buttonHoverTexture = new Texture(Gdx.files.internal("images/blue-b-hover.png"));

        TextureRegionDrawable buttonImageDrawable = new TextureRegionDrawable(buttonImageTexture);
        TextureRegionDrawable buttonHoverDrawable = new TextureRegionDrawable(buttonHoverTexture);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = skin.getFont("button");
        buttonStyle.fontColor = skin.getColor("white");
        buttonStyle.downFontColor = skin.getColor("white");
        buttonStyle.overFontColor = skin.getColor("black");

        buttonStyle.up = skin.getDrawable("button-c");
        buttonStyle.down = skin.getDrawable("button-p");
        buttonStyle.over = skin.getDrawable("button-h");

        buttonStyle.up = buttonImageDrawable;
//        buttonStyle.up = skin.getDrawable("button-c");
//        buttonStyle.down = skin.getDrawable("button-p");
        buttonStyle.down = buttonImageDrawable;
//        buttonStyle.over = skin.getDrawable("button-h");
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

    private void handleForwardButtonClick() {
        currentHint = (currentHint + 1) % (hints.length);
        label.setText(hints[currentHint]);
    }

    private void handleBackwardButtonClick() {
        currentHint = (currentHint - 1 + hints.length) % hints.length;
        label.setText(hints[currentHint]);
    }

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
