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
    private final Texture backgroundTexture;
    private final Image backgroundImage;
    private final Label label;

    public ChatOverlay(String labelText) {
        // Initialize Stage and Skin
        stage = ServiceLocator.getRenderService().getStage();

        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        // Load and set up the background image
        backgroundTexture = new Texture(Gdx.files.internal("images/peach-bar.png"));

        // Original image dimensions
        float imageWidth = backgroundTexture.getWidth();
        float imageHeight = backgroundTexture.getHeight();

        float desiredHeight = 700; // to help with the sizing of the image
        float ratio = imageWidth / imageHeight;
        float newWidth = desiredHeight * ratio;

        // Image with the adjusted size (so it doesn't get stretched out and distorted)
        backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setSize(newWidth, desiredHeight);
        backgroundImage.setPosition((Gdx.graphics.getWidth() - newWidth) / 2, -125);

        // Add the background image to the stage
        stage.addActor(backgroundImage);

        // Create and position the label
        label = new Label(labelText, skin, "default-white");
        label.setFontScale(1.5f);

        // Calculate the center position for the text label
        float labelWidth = label.getPrefWidth();
        float labelHeight = label.getPrefHeight();
        float labelX = backgroundImage.getX() + (newWidth - labelWidth) / 2;
        float labelY = -220 + (desiredHeight - labelHeight) / 2;

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
