package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(GameExtension.class)
class CameraComponentTest {
    /**
     * This test is used to determine whether a camera attached to a
     * moving entity follows them.
     */
    @Test
    void cameraShouldFollowEntity() {
        Entity entity = createEntityWithCamera();
        // Ensure camera position and current entity (center) position are equal
        entity.update();
        Camera camera = entity.getComponent(CameraComponent.class).getCamera();
        Vector2 oldEntityPos = entity.getPosition();
        Vector2 oldEntityCenterPos = entity.getCenterPosition();
        Vector3 oldCameraPos = camera.position;
        assertTrue(arePositionsEqual(oldEntityCenterPos, oldCameraPos));
        // Ensure entity center position changes by same amount as entity position
        Vector2 newEntityPos = oldEntityPos.add(0.5f, 0.5f);
        entity.setPosition(newEntityPos);
        Vector2 newEntityCenterPos = entity.getCenterPosition();
        assertEquals(newEntityCenterPos, oldEntityCenterPos.add(0.5f, 0.5f));
        // Ensure camera position updates to equal new entity (center) position
        entity.update();
        Vector3 newCameraPos = camera.position;
        assertTrue(arePositionsEqual(newEntityCenterPos, newCameraPos));
    }

    /**
     * This test is used to determine whether a camera which has been
     * attached as a component to multiple entities only follows the latest one
     */
    @Test
    void cameraShouldFollowOnlyOneEntity() {
        Entity entityOne = createEntityWithCamera();
        entityOne.update();
        CameraComponent cameraComponent = entityOne.getComponent(CameraComponent.class);
        assertEquals(cameraComponent.getEntity(), entityOne);
        Vector2 entityOnePos = new Vector2(10f, 10f);
        entityOne.setPosition(entityOnePos);
        entityOne.update();
        assertTrue(
                arePositionsEqual(
                        entityOne.getCenterPosition(),
                        cameraComponent.getCamera().position));
        // camera switches to new entity
        Entity entityTwo = new Entity().addComponent(cameraComponent);
        entityTwo.create();
        assertEquals(cameraComponent.getEntity(), entityTwo);
        // camera follows new entity
        Vector2 entityTwoPos = new Vector2(15f, 15f);
        entityTwo.setPosition(entityTwoPos);
        entityTwo.update();
        assertTrue(
                arePositionsEqual(
                        entityTwo.getCenterPosition(),
                        cameraComponent.getCamera().position));
        // camera does not follow old entity
        Vector2 newEntityOnePos = entityOnePos.add(2.5f, 2.5f);
        entityOne.setPosition(newEntityOnePos);
        entityOne.update();
        assertFalse(
                arePositionsEqual(
                        entityOne.getCenterPosition(),
                        cameraComponent.getCamera().position));
    }

    /**
     * Create a fresh entity with a camera component
     *
     * @return the created entity
     */
    private Entity createEntityWithCamera() {
        Entity entity =
                new Entity().addComponent(new CameraComponent());
        entity.create();
        return entity;
    }

    /**
     * Determine whether a 2D player position and a 3D camera position are equal
     *
     * @param posOne the player's position
     * @param posTwo the camera's position
     *
     * @return true if the positions are equal. False otherwise.
     */
    private boolean arePositionsEqual(Vector2 posOne, Vector3 posTwo) {
        return posTwo.equals(new Vector3(posOne, 0f));
    }
}
