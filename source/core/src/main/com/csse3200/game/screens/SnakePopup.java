package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SnakePopup {

    private Stage stage;
    private final Skin skin;
    private final SpriteBatch batch;
    private final Window window;
    private final Texture texture;

    public SnakePopup(String texturePath) {

        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));


        texture = new Texture(Gdx.files.internal(texturePath));


        window = new Window("Help", skin);
        //window.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      //  window.setPosition(0, 0); // Position at the top-left corner
        window.setSize(600, 400);
        window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2,
                (Gdx.graphics.getHeight() - window.getHeight()) / 2);
        window.setModal(true);


        Image image = new Image(texture);
        window.add(image).fill();


        stage.addActor(window);
    }

    public void show() {
        window.setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    public void hide() {
        window.setVisible(false);
        Gdx.input.setInputProcessor(null);
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
        texture.dispose();
    }
}
