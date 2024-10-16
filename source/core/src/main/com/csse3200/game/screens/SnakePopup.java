package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.CustomButton;


public class SnakePopup {

    private final Stage stage;
    private final Skin skin;
    private final Window window;
    private final Texture texture;
    private final float windowWidth = 1000;
    private final float windowHeight = 800;
    private boolean isVisible = false;
    private final CustomButton continueButton;

    public SnakePopup(PausableScreen screen, String texturePath) {
        this(texturePath);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.removeOverlay();
                hide();
            }
        });
    }

    private SnakePopup(String texturePath) {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        texture = new Texture(Gdx.files.internal(texturePath));

        window = new Window("Help", skin);
        window.setSize(windowWidth, windowHeight);
        window.setModal(true);
        window.setPosition((Gdx.graphics.getWidth() - windowWidth) / 2, (Gdx.graphics.getHeight() - windowHeight) / 2);

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image image = new Image(texture);
        image.setScaling(Scaling.fit);
        window.add(image);

        continueButton = new CustomButton("Continue", skin);

        window.row();
        window.add(continueButton).width(150).height(50).padTop(10);

        stage.addActor(window);
    }

    public void show() {
        isVisible = true;
        window.setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    public void hide() {
        isVisible = false;
        window.setVisible(false);
        Gdx.input.setInputProcessor(null);
        if (ServiceLocator.getInputService() != null) {
            Gdx.input.setInputProcessor(ServiceLocator.getInputService());
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        window.setPosition((width - windowWidth) / 2, (height - windowHeight) / 2);
    }

    public void render() {
        if (isVisible) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
        texture.dispose();
    }
}