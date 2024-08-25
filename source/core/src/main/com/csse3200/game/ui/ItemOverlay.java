package com.csse3200.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.services.ServiceLocator;

public class ItemOverlay {
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Label label;
    private final int screenWidth = Gdx.graphics.getWidth();
    private String[] hints;

    public ItemOverlay(String[] labelText) {
        this.stage = ServiceLocator.getRenderService().getStage();
        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        this.hints = labelText;

        createBackgroundImage("images/blue-bar.png", 700);

        label = new Label(hints[0], skin, "default-white");
        label.setFontScale(1.5f);

        float labelWidth = label.getPrefWidth();
        float labelHeight = label.getPrefHeight();
        float labelX = backgroundImage.getX() + (backgroundImage.getWidth() - labelWidth) / 2;
        float labelY = -220 + (backgroundImage.getHeight() - labelHeight) / 2;
        label.setPosition(labelX, labelY);
        stage.addActor(label);
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
