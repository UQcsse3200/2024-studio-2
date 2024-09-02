package com.csse3200.game.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

public class CameraZoomComponent extends Component {
    private static final float ZOOM_AMOUNT = 1f;
    private float maxZoom = 30f;
    private float minZoom = 5f;
    private CameraComponent cameraComponent;

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void create() {
        cameraComponent = entity.getComponent(CameraComponent.class);
        entity.getEvents().addListener("cameraZoom", this::zoom);
    }

    /**
     * Set the maximum amount that the camera can zoom out
     *
     * @param maxZoom the maximum camera zoom amount
     */
    public void setMaxZoom(float maxZoom) {
        this.maxZoom = maxZoom;
    }

    /**
     * Set the minimum amount that the camera can zoom in
     *
     * @param minZoom the minimum camera zoom amount
     */
    public void setMinZoom(float minZoom) {
        this.minZoom = minZoom;
    }

    /**
     * Zooms the camera in or out depending on the value of amountY
     *
     * @param amountX the horizontal zoom amount
     * @param amountY the vertical zoom amount
     */
    public void zoom(float amountX, float amountY) {
        Camera camera = cameraComponent.getCamera();
        // Calculate new dimensions for camera viewport
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        float gameWidth = camera.viewportWidth;
        gameWidth = gameWidth + ZOOM_AMOUNT * amountY;
        // Ensure that camera doesn't exceed zoom amount
        gameWidth = Math.clamp(gameWidth, minZoom, maxZoom);
        cameraComponent.resize(screenWidth, screenHeight, gameWidth);
    }
}
