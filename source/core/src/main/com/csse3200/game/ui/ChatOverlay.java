package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.Texture;


public class ChatOverlay {

    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Label label;

    public ChatOverlay(String labelText) {
        // Initialize Stage and Skin
        stage = ServiceLocator.getRenderService().getStage();

        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Load and set up the background image
        backgroundTexture = new Texture(Gdx.files.internal("images/HintBox.png"));
        backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setSize(Gdx.graphics.getWidth(), 200);
        backgroundImage.setPosition(0, 0);

        // Add the background image to the stage
        stage.addActor(backgroundImage);

        // Calculate the center position
        float imageWidth = backgroundImage.getWidth();
        float imageHeight = backgroundImage.getHeight();

        // Calculate the center position of the background image
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float imageX = (screenWidth - imageWidth) / 2;
        float imageY = (screenHeight - imageHeight) / 2;

        // Create and position the label
        label = new Label(labelText, skin);
        label.setFontScale(1.5f);

        // Calculate the center position for the label
        float labelWidth = label.getPrefWidth();
        float labelHeight = label.getPrefHeight();
        float labelX = imageX + (imageWidth - labelWidth) / 2;
        float labelY = 20 + (imageHeight - labelHeight) / 2;

        label.setPosition(labelX, labelY);
        stage.addActor(label);
    }

    public void dispose() {
        if (backgroundImage != null) {
            backgroundImage.remove();
        }

        if (label != null) {
            label.remove();
        }

        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }

    }
}
