package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class CameraZoomComponentTest {
    private static final int SCROLL_UP = -1;
    private static final int NO_SCROLL = 0;
    private static final int SCROLL_DOWN = 1;
    private static final List<Integer> SCROLL_ACTIONS =
            Arrays.asList(SCROLL_UP, NO_SCROLL, SCROLL_DOWN);
    private static final int LOOP_TIME_OUT = 1000;
    private CameraComponent cameraComponent;
    private CameraZoomComponent cameraZoomComponent;

    @Mock Graphics graphics;


    @BeforeEach
    void beforeEach() {
        Gdx.graphics = graphics;
        ServiceLocator.registerInputService(new InputService());
        cameraComponent = new CameraComponent();
        cameraZoomComponent = new CameraZoomComponent();
    }

    /**
     * This test is used to determine whether the camera zooms in or out
     * when the mouse wheel is scrolled
     */
    @Test
    void shouldZoomCameraOnScroll() {
        Entity entity = createEntityWithCamera();
        Camera camera = cameraComponent.getCamera();
        InputService inputService = ServiceLocator.getInputService();
        for (int action : SCROLL_ACTIONS) {
            // reset camera for test
            camera.position.set(entity.getCenterPosition(), 0f);
            resizeCamera();
            // get old camera data
            Vector3 oldCameraPos = camera.position;
            float oldCameraViewWidth = camera.viewportWidth;
            float oldCameraViewHeight = camera.viewportHeight;
            float oldCameraViewRatio = oldCameraViewHeight / oldCameraViewWidth;
            // scroll mouse wheel
            inputService.scrolled(0, action);
            // get new camera data
            Vector3 newCameraPos = camera.position;
            float newCameraViewWidth = camera.viewportWidth;
            float newCameraViewHeight = camera.viewportHeight;
            float newCameraViewRatio = newCameraViewHeight / newCameraViewWidth;
            // check that camera position and scale has not changed
            assertEquals(oldCameraViewRatio, newCameraViewRatio);
            assertEquals(oldCameraPos, newCameraPos);
            // check change in camera width and height
            switch(action) {
                case SCROLL_UP:
                    // check that the camera has zoomed in
                    assertTrue(oldCameraViewWidth > newCameraViewWidth);
                    assertTrue(oldCameraViewHeight > newCameraViewHeight);
                    break;
                case NO_SCROLL:
                    // check that the camera has not changed
                    assertEquals(oldCameraViewWidth, newCameraViewWidth);
                    assertEquals(oldCameraViewHeight, newCameraViewHeight);
                    break;
                case SCROLL_DOWN:
                    // check that the camera has zoomed out
                    assertTrue(oldCameraViewWidth < newCameraViewWidth);
                    assertTrue(oldCameraViewHeight < newCameraViewHeight);
                    break;
            }
        }
    }

    /**
     * This test is used to verify that there exists a maximum bound on
     * zooming the camera out
     */
    @Test
    void shouldConstrainZoomOut() {
        Entity entity = createEntityWithCamera();
        Camera camera = cameraComponent.getCamera();
        InputService inputService = ServiceLocator.getInputService();
        int i;
        // show that camera zoom out has limits
        for (i = 0; i < LOOP_TIME_OUT; i++) {
            float oldCameraWidth = camera.viewportWidth;
            inputService.scrolled(0, SCROLL_UP);
            float newCameraWidth = camera.viewportWidth;
            if (oldCameraWidth == newCameraWidth) {
                break; // can no longer zoom out
            }
        }
        assertTrue(i < LOOP_TIME_OUT);
    }

    /**
     * This test is used to verify that there exists a minimum bound on
     * zooming the camera in
     */
    @Test
    void shouldConstrainZoomIn() {
        Entity entity = createEntityWithCamera();
        Camera camera = cameraComponent.getCamera();
        InputService inputService = ServiceLocator.getInputService();
        int i;
        // show that camera zoom out has limits
        for (i = 0; i < LOOP_TIME_OUT; i++) {
            float oldCameraWidth = camera.viewportWidth;
            inputService.scrolled(0, SCROLL_DOWN);
            float newCameraWidth = camera.viewportWidth;
            if (oldCameraWidth == newCameraWidth) {
                break; // can no longer zoom out
            }
        }
        assertTrue(i < LOOP_TIME_OUT);
    }

    /**
     * Create a fresh entity with a camera component, a camera zoom component and
     * an input component.
     *
     * @return the created entity
     */
    private Entity createEntityWithCamera() {
        // create entity
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForPlayer();
        Entity entity = new Entity()
                .addComponent(cameraComponent)
                .addComponent(cameraZoomComponent)
                .addComponent(inputComponent);
        entity.create();
        // mock screen size and resize camera
        when(graphics.getWidth()).thenReturn(100);
        when(graphics.getHeight()).thenReturn(200);
        resizeCamera();
        return entity;
    }

    /**
     * Calls the resize method on the camera component
     */
    private void resizeCamera() {
        cameraComponent.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 10f);
    }
}
