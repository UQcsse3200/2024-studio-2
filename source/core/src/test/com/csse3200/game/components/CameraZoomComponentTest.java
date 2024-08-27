package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class CameraZoomComponentTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerInputService(new InputService());
    }

    /**
     * This test is used to determine whether the camera zooms in when
     * the mouse is scrolled upwards.
     */
    @Test
    void cameraShouldZoomIn() {
        Entity entity = createEntityWithCamera();
        entity.update();
        Camera camera = entity.getComponent(CameraComponent.class).getCamera();
        InputService inputService = ServiceLocator.getInputService();
        Vector3 oldCameraPos = camera.position;
        float oldCameraViewWidth = camera.viewportWidth;
        float oldCameraViewHeight = camera.viewportHeight;
        float oldCameraViewRatio = oldCameraViewHeight / oldCameraViewWidth;
        float oldCameraViewArea = oldCameraViewWidth * oldCameraViewHeight;
        inputService.scrolled(0,-1); // scroll mouse up
        Vector3 newCameraPos = camera.position;
        float newCameraViewWidth = camera.viewportWidth;
        float newCameraViewHeight = camera.viewportHeight;
        float newCameraViewRatio = newCameraViewHeight / newCameraViewWidth;
        float newCameraViewArea = newCameraViewWidth * newCameraViewHeight;
        assertEquals(oldCameraViewRatio, newCameraViewRatio);
        assertEquals(oldCameraPos, newCameraPos);
        assertNotEquals(oldCameraViewWidth, newCameraViewWidth);
        //assertNotEquals(oldCameraViewHeight, newCameraViewHeight);
        //assertNotEquals(newCameraViewArea, newCameraViewArea);

    }

    /**
     * Create a fresh entity with a camera component, a camera zoom component and
     * an input component.
     *
     * @return the created entity
     */
    private Entity createEntityWithCamera() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForPlayer();
        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 20f);
        Entity entity = new Entity()
                .addComponent(cameraComponent)
                .addComponent(new CameraZoomComponent())
                .addComponent(inputComponent);
        entity.create();
        return entity;
    }
}
