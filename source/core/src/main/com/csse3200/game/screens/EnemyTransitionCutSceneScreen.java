package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * Screen that handles the display of an enemy transition cutscene. Manages rendering the background
 * and UI elements, and acts as the entry point for the cutscene when triggered.
 * This class extends ScreenAdapter, which provides basic screen lifecycle methods.
 */

public class EnemyTransitionCutSceneScreen extends ScreenAdapter {
    private final GdxGame game;
    private final Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private Texture playerTexture;
    private Texture enemyTexture;
    private Image playerImage;
    private Image enemyImage;


    /**
     * Constructor for EnemyTransitionCutSceneScreen. Initializes the screen with necessary resources,
     * including the stage and background texture.
     */
    public EnemyTransitionCutSceneScreen(GdxGame game, Entity enemy) {
        this.game = game;
        this.stage = ServiceLocator.getRenderService().getStage();
        this.batch = new SpriteBatch();
        initializeSkin();
        initializeBackground();

        this.playerTexture = new Texture(GameState.player.selectedAnimalPath);
        getEnemy(enemy);
        displayTextures();
    }

    /**
     * Displays the images on the stage.
     */
    public void displayTextures() {
        this.playerImage = new Image();
        this.enemyImage = new Image(this.enemyTexture);

        TextureRegionDrawable drawablePlayer = new TextureRegionDrawable(this.playerTexture);
        playerImage.setDrawable(drawablePlayer);
        playerImage.setPosition(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.4f);
        playerImage.setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.2f);
        stage.addActor(playerImage);

        TextureRegionDrawable drawableEnemy = new TextureRegionDrawable(this.enemyTexture);
        enemyImage.setDrawable(drawableEnemy);
        enemyImage.setPosition(Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.40f);
        enemyImage.setSize(Gdx.graphics.getWidth() * 0.15f, Gdx.graphics.getHeight() * 0.2f);
        stage.addActor(enemyImage);
    }

    /**
     * Sets the texture for the enemy to be used.
     */
    public void getEnemy(Entity enemy) {
        switch (enemy.getEnemyType()) {
            case CHICKEN:
                enemyTexture = new Texture("images/chicken_idle.png");
                break;
            case FROG:
                enemyTexture = new Texture("images/frog_idle.png");
                break;
            case MONKEY:
                enemyTexture = new Texture("images/monkey_idle.png");
                break;
            case BEAR:
                enemyTexture = new Texture("images/bear_idle.png");
                break;
            case BEE:
                enemyTexture = new Texture("images/bee_idle.png");
                break;
            case PIGEON:
                enemyTexture = new Texture("images/pigeon_idle.png");
                break;
            case EEL:
                enemyTexture = new Texture("images/eel_idle.png");
                break;
            case OCTOPUS:
                enemyTexture = new Texture("images/octopus_idle.png");
                break;
            case BIGSAWFISH:
                enemyTexture = new Texture("images/bigsawfish_idle.png");
                break;
            case MACAW:
                enemyTexture = new Texture("images/macaw_idle.png");
                break;
            case JOEY:
                enemyTexture = new Texture("images/joey_idle.png");
                break;
            case KANGAROO:
                enemyTexture = new Texture("images/final_boss_kangaroo_idle.png");
                break;
            case WATER_BOSS:
                enemyTexture = new Texture("images/water_boss_idle.png");
                break;
            case AIR_BOSS:
                enemyTexture = new Texture("images/air_boss_idle.png");
                break;
            default:
                enemyTexture = new Texture("");
                break;
        }
    }

    /**
     * Resizes the screen, adjusting the viewport of the stage to match the new width and height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Disposes of resources when screen is no longer needed.
     */
    @Override
    public void dispose() {
        // Dispose of  resources
        stage.dispose();
        backgroundTexture.dispose();
        batch.dispose();
        skin.dispose();
    }

    private void initializeSkin() {
        skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    }

    private void initializeBackground() {
        backgroundTexture = new Texture(Gdx.files.internal("images/EnemyTransition.png"));
    }

    @Override
    public void show() {
        // This method is called when this screen becomes the current screen
    }

    @Override
    public void render(float delta) {
        // Begin drawing the background texture
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }
}

