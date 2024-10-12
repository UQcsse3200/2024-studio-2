package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.csse3200.game.GdxGame;

public class SnakePopup {

    private final Stage stage;
    private final Skin skin;
    private final Window window;
    private final Texture texture;
    private final GdxGame game;
    private final String gameType;

    public SnakePopup(GdxGame game, String texturePath, String gameType) {
        this.game = game;
        this.gameType = gameType;

        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        texture = new Texture(Gdx.files.internal(texturePath));

        window = new Window("Help", skin);
        window.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        window.setPosition(0, 0);
        window.setModal(true);

        Image image = new Image(texture);
        window.add(image).fill();


        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                switch (gameType) {
                    case "Snake":
                        game.enterSnakeScreen();
                        break;
                    case "Maze":
                        game.enterMazeGameScreen();
                        break;
                    case "Birdie":
                        game.enterBirdieDashScreen();
                        break;
                }
            }
        });

        window.row();
        window.add(continueButton).padTop(10);

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
