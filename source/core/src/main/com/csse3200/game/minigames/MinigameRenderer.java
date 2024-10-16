package com.csse3200.game.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to help render minigame things for snake and birdie dash. Includes spritebatch and camera.
 * The camera allows resizing to be done easily.
 */
public class MinigameRenderer {
    private static final float DEFAULT_WIDTH = 1920;
    private static final float DEFAULT_HEIGHT = 1200;
    private final SpriteBatch sb;
    private final OrthographicCamera cam;
    private final List<MinigameRenderable> renderables = new ArrayList<>();
    private final Texture background;

    public MinigameRenderer() {
        this.sb = new SpriteBatch();
        this.cam = new OrthographicCamera(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        cam.position.set(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2, 0);
        cam.update();
        this.background = new Texture("images/minigames/Background.png");
    }

    /**
     * Add a thing to render (class that implements MinigameRenderable with render method)
     * @param renderable class to render
     */
    public void addRenderable(MinigameRenderable renderable) {
        renderables.add(renderable);
    }

    /**
     * Render all classes
     */
    public void render() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        for(MinigameRenderable renderable : renderables) {
            renderable.render();
        }
        sb.end();
    }

    /**
     * Render background for sprite batch
     */
    public void renderBackground() {
        if(background != null) {
            sb.begin();
            sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            sb.end();
        }
    }

    /**
     * Resize the screen
     * @param width new width of the screen
     * @param height new height of the screen
     */
    public void resize(int width, int height) {
        float aspectRatio = (float) width / height;
        float viewportWidth = 800;
        float viewportHeight = viewportWidth / aspectRatio;
        cam.setToOrtho(false, viewportWidth, viewportHeight);
        cam.position.set(viewportWidth / 2f, viewportHeight / 2f, 0);
        cam.update();
        renderBackground();
    }

    /**
     * Getter for the sprite batch
     * @return the sprite batch
     */
    public SpriteBatch getSb() {
        return this.sb;
    }

    /**
     * Getter for the camera
     * @return the camera
     */
    public OrthographicCamera getCam() {
        return this.cam;
    }

    /**
     * Dispose of the sprite batch
     */
    public void dispose() {
        sb.dispose();
    }
}
