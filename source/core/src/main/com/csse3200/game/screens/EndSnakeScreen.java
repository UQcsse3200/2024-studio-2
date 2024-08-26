package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.GdxGame;

public class EndSnakeScreen extends ScreenAdapter {
    private final GdxGame game;
    private final int score;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private Stage stage;
    private Skin skin;

    public EndSnakeScreen(GdxGame game, int score) {
        this.game = game;
        this.score = score;

        this.spriteBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.setColor(Color.WHITE);
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        
        Gdx.input.setInputProcessor(stage);

        setupUI();
    }

    private void setupUI() {
        // Create the exit button
        TextButton exitButton = new TextButton("Exit", skin);

        // Set up the exit button click event
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Return to main menu and original screen colour
                Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Set up the table for UI layout
        Table table = new Table();
        table.setFillParent(true);
        table.top().right();
        table.add(exitButton).pad(10);

        // Add the table to the stage
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Set the background color based on the score
        setBackgroundColor();

        // Draw the exit button and other UI elements
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Render the game over messages
        renderEndMessage();

                // Key functionality for escape and restart
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {  // Restart game
            // Restart the game
            game.setScreen(new SnakeScreen(game));
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {  // Go to minigames menu
            Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
            game.setScreen(new MiniGameMenuScreen(game));
        }
    }

    private void setBackgroundColor() {
        if (score < 5) {
            Gdx.gl.glClearColor(50f / 255f, 82f / 255f, 29f / 255f, 1f);
        } else if (score < 15) {
            Gdx.gl.glClearColor(169f / 255f, 113f / 255f, 66f / 255f, 1f);
        } else if (score < 30) {
            Gdx.gl.glClearColor(115f / 255f, 122f / 255f, 140f / 255f, 1f);
        } else {
            Gdx.gl.glClearColor(173f / 255f, 162f / 255f, 114f / 255f, 1f);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void renderEndMessage() {
        spriteBatch.begin();

        int centerX = Gdx.graphics.getWidth() / 2;
        int centerY = Gdx.graphics.getHeight() / 2;

        font.getData().setScale(6.0f);
        String endGameText = "End of Mini-Game";
        font.draw(spriteBatch, endGameText, centerX - 300, centerY + 300);

        font.getData().setScale(5.0f);
        String scoreText = "Score: " + score;
        font.draw(spriteBatch, scoreText, centerX - 140, centerY + 5);

        String scoreFunnyText = getFunnyText();
        font.draw(spriteBatch, scoreFunnyText, centerX - 300, centerY - 150);

        spriteBatch.end();
    }

    private String getFunnyText() {
        if (score < 5) {
            return "Damn that was a small snake...";
        } else if (score < 15) {
            return "Nawww, look he's almost fully grown";
        } else if (score < 30) {
            return "That's a really big snake alright";
        } else {
            return "Snake king!";
        }
    }

    @Override
    public void dispose() {    
        
        spriteBatch.dispose();
        font.dispose();
        stage.dispose();
        skin.dispose(); 
    }
}
