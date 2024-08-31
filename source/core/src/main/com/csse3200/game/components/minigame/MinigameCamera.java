package com.csse3200.game.components.minigame;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CameraComponent;

/**
 * Specialised minigame cam class to set game area
 * Might not be useful on second thought
 */
public class MinigameCamera extends CameraComponent {
    private final Vector2 fixedPosition;

    public MinigameCamera(float x, float y) {
        super(new OrthographicCamera());
        this.fixedPosition = new Vector2(x, y);
    }

    @Override
    public void update() {
        Camera cam = getCamera();
        cam.position.set(fixedPosition, 0f);
        cam.update();
    }
}
