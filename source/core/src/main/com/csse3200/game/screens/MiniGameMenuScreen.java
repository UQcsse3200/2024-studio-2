package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;

public class MiniGameMenuScreen implements Screen {

    private Stage stage;
    private BitmapFont font;
    private GdxGame game;
    private Skin skin;

    private Texture snakeTexture;
    private Texture skyTexture;
    private Texture waterTexture;

    public MiniGameMenuScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();


        font.getData().setScale(2);


        skin = new Skin(Gdx.files.internal("uiskin.json"));


        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = skin.newDrawable("default-round", Color.BLUE);
        buttonStyle.down = skin.newDrawable("default-round", Color.RED);
        buttonStyle.disabled = skin.newDrawable("default-round", Color.GRAY);


        snakeTexture = new Texture(Gdx.files.internal("images/minigames/Snake.png"));
        skyTexture = new Texture(Gdx.files.internal("images/minigames/Flappy_bird.png"));
        waterTexture = new Texture(Gdx.files.internal("images/minigames/Underwater_maze.png"));


        Image snakeImage = new Image(snakeTexture);
        Image skyImage = new Image(skyTexture);
        Image waterImage = new Image(waterTexture);


        TextButton snakeButton = new TextButton("Snake", buttonStyle);
        TextButton skyButton = new TextButton("Flappy bird", buttonStyle);
        TextButton waterButton = new TextButton("Underwater maze", buttonStyle);


        Table table = new Table();
        table.setFillParent(true);
        table.center();


        table.add(snakeImage).pad(10).row();
        table.add(snakeButton).pad(10).row();
        table.add(skyImage).pad(10).row();
        table.add(skyButton).pad(10).row();
        table.add(waterImage).pad(10).row();
        table.add(waterButton).pad(10).row();


        stage.addActor(table);


        snakeImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Change button color to green when clicked
                snakeImage.setColor(Color.GREEN);
                // Switch to SnakeScreen
                game.setScreen(new SnakeScreen(game));
            }
        });

        skyImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Change button color to green when clicked
                skyImage.setColor(Color.GREEN);
                // Switch to SkyScreen
              //  game.setScreen(new SkyScreen(game));
            }
        });

        waterImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Change button color to green when clicked
                waterImage.setColor(Color.GREEN);
                // Switch to WaterScreen
              //  game.setScreen(new WaterScreen(game));
            }
        });


        snakeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Change button color to green when clicked
                snakeButton.setColor(Color.GREEN);
                // Switch to SnakeScreen
                game.setScreen(new SnakeScreen(game));
            }
        });

        skyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Change button color to green when clicked
                skyButton.setColor(Color.GREEN);
                // Switch to SkyScreen
              //  game.setScreen(new SkyScreen(game));
            }
        });

        waterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Change button color to green when clicked
                waterButton.setColor(Color.GREEN);
                // Switch to WaterScreen
              //  game.setScreen(new WaterScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        // Set the background color to black
        Gdx.gl.glClearColor(0, 0, 0, 1); // RGBA values (black)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Handle pause
    }

    @Override
    public void resume() {
        // Handle resume
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        skin.dispose(); // Dispose of the skin when done
        snakeTexture.dispose(); // Dispose of the textures
        skyTexture.dispose();
        waterTexture.dispose();
    }
}
