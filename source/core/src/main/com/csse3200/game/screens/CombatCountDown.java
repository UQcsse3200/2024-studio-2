package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.GameTimer;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Screen that displays a countdown before entering a combat sequence.
 * The user is prompted with a dialog to confirm if they want to fight.
 */
public class CombatCountDown implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(CombatCountDown.class);
    private final GdxGame game;
    private final Stage stage;
    private final GameTimer gameTimer;
    private Label timerLabel;
    private Texture backgroundTexture;
    private final SpriteBatch batch;

    /**
     * Constructs the CombatCountDown screen with the game instance.
     * Initializes the stage, loads assets, and creates the UI.
     *
     * @param game The main game instance.
     */
    public CombatCountDown(GdxGame game) {
        this.game = game;
        this.gameTimer = new GameTimer(game);
        this.stage = new Stage(new ScreenViewport());
        this.batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        loadAssets();
        createUI();
    }

    /**
     * Loads the background texture and other assets needed for the screen.
     */
    private void loadAssets() {
        backgroundTexture = new Texture(Gdx.files.internal("images/combat_background.jpg"));
    }

    /**
     * Creates the user interface for the countdown screen, including the
     * confirmation dialog and timer label.
     */
    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle(UIComponent.skin.get(Label.LabelStyle.class));
        labelStyle.font.getData().setScale(1.5f);
        labelStyle.fontColor = Color.WHITE; // Set text color to white

        Label questionLabel = new Label("Do you want to fight?", labelStyle);
        questionLabel.setAlignment(Align.center);
        table.add(questionLabel).padBottom(40).colspan(2);
        table.row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(UIComponent.skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.font.getData().setScale(1.3f);
        buttonStyle.fontColor = Color.WHITE; // Set button text color to white

        // Create and add listeners to the "Yes" button
        TextButton yesButton = new TextButton("Yes", buttonStyle);
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.info("Starting combat in 3 seconds");
                startCountdown();
            }
        });

        // Create and add listeners to the "No" button
        TextButton noButton = new TextButton("No", buttonStyle);
        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.info("Returning to main menu");
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        table.add(yesButton).padRight(20).width(150).height(60);
        table.add(noButton).padLeft(20).width(150).height(60);
        table.row();

        timerLabel = new Label("", labelStyle);
        timerLabel.setAlignment(Align.center);
        table.add(timerLabel).colspan(2).padTop(40);
    }

    /**
     * Starts a 3-second countdown before transitioning to the combat screen.
     * Updates the UI with the remaining seconds and handles transition on finish.
     */
    private void startCountdown() {
        gameTimer.startCountdown(3, new GameTimer.CountdownCallback() {
            @Override
            public void onTick(int secondsLeft) {
                timerLabel.setText("Starting in: " + secondsLeft);
            }

            @Override
            public void onFinish() {
                // Uncomment and set the appropriate screen type to transition to the combat screen.
                // game.setScreen(GdxGame.ScreenType.LOADING_SCREEN);
            }
        });
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a dark grey color
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        batch.begin();
        // Set background opacity and draw the background texture
        float backgroundOpacity = 0.7f;
        batch.setColor(1, 1, 1, backgroundOpacity); // Set opacity for background
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1); // Reset to full opacity for other drawings
        batch.end();

        // Update and draw the stage (UI elements)
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport on window resize to maintain aspect ratio
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        // Dispose of assets and cancel any ongoing timers
        stage.dispose();
        gameTimer.cancel();
        backgroundTexture.dispose();
        batch.dispose();
    }
}