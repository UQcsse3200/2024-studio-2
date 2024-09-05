package com.csse3200.game.components.minigames;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to help render minigame things. Includes spritebatch and camera.
 * The camera allows resizing to be done easily.
 */
public class MinigameRenderer {
    private final static float DEFAULT_WIDTH = 1920;
    private final static float DEFAULT_HEIGHT = 1200;
    private final SpriteBatch sb;
    private final OrthographicCamera cam;
    private List<MinigameRenderable> renderables = new ArrayList<>();

    public MinigameRenderer() {
        this.sb = new SpriteBatch();
        this.cam = new OrthographicCamera(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        cam.position.set(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2, 0);
        cam.update();
    }

    public void addRenderable(MinigameRenderable renderable) {
        renderables.add(renderable);
    }

    public void render() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        for(MinigameRenderable renderable : renderables) {
            renderable.render();
        }
        sb.end();
    }

    public void resize(int width, int height) {
        float aspectRatio = (float) width / height;
        float viewportWidth = 800;
        float viewportHeight = viewportWidth / aspectRatio;
        cam.setToOrtho(false, viewportWidth, viewportHeight);
        cam.position.set(viewportWidth / 2f, viewportHeight / 2f, 0);
        cam.update();
    }

    public SpriteBatch getSb() {
        return this.sb;
    }

    public OrthographicCamera getCam() {
        return this.cam;
    }

    public void dispose() {
        sb.dispose();
    }
}
