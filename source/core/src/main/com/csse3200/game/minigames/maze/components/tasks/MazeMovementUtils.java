package com.csse3200.game.minigames.maze.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.minigames.maze.areas.MazeGameArea;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;

/**
 * Some utility functions to help with movement tasks in the maze.
 */
public class MazeMovementUtils {
    public static final float PADDING = 0.03f;
    private static final float MIN_LENGTH_FACE = MazeGameArea.WALL_THICKNESS;

    private MazeMovementUtils() {
        throw new IllegalArgumentException("Do not instantiate static util class!");
    }

    /**
     * Takes a position relative to the center of an entity and converts it to a position relative
     * to the bottom-left corner of the entity.
     *
     * @param pos Position relative to center of entity
     * @param entity The entity
     * @return Position relative to bottom-left of entity
     */
    public static Vector2 adjustPos(Vector2 pos, Entity entity) {
        return pos.cpy().sub(entity.getCenterPosition().sub(entity.getPosition()));
    }

    /**
     * Tests whether the line starting at from parameter and ending at to parameter intersects an obstacle.
     * Draws ray lines on the debug renderer.
     * @param from starting point of ray
     * @param to ending point of ray
     * @return true if intersecting, otherwise false
     */
    private static boolean testRay(Vector2 from, Vector2 to) {
        PhysicsEngine physics = ServiceLocator.getPhysicsService().getPhysics();
        DebugRenderer debugRenderer = ServiceLocator.getRenderService().getDebug();
        RaycastHit hit = new RaycastHit();
        if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(from, hit.getPoint().cpy());
            return true;
        }
        debugRenderer.drawLine(from, to);
        return false;
    }

    /**
     * Increment modulo 4
     * @param corner integer
     * @return the next corner mod 4
     */
    private static int nextCorner(int corner) {
        return (corner + 1) % 4;
    }

    /**
     * Decrement modulo 4
     * @param corner the previous corner
     * @return the previous corner mod 4
     */
    private static int prevCorner(int corner) {
        return (corner + 3) % 4;
    }

    /**
     * Gets the corners of a rectangle hitbox of an entity adding padding around the box.
     * @param entity the entity
     * @param padding the padding to add around the hitbox
     * @return an array of vertices representing the corners of the hitbox
     */
    public static Vector2[] getHitBoxCorners(Entity entity, float padding) {
        PolygonShape shape = (PolygonShape) entity.getComponent(HitboxComponent.class).getFixture().getShape();
        Vector2[] corners = new Vector2[4];
        for (int i = 0; i < 4; i++) {
            corners[i] = new Vector2();
            // box vertices start from bottom-left and go anti-clockwise, but we want top-right anti-clockwise
            shape.getVertex(i ^ 2, corners[i]);
            corners[i].add(entity.getPosition())
                    .add(((i & 1) ^ ((i & 2) >> 1)) == 0 ? padding : -padding, (i & 2) == 0 ? padding : -padding);
        }
        return corners;
    }

    /**
     * Checks whether an entity can beeline to a position considering hitbox collisions and
     * obstacles.
     * @param pos the target destination
     * @param entity the entity
     * @return whether the entity can beeline to the target position without hitting and obstacle.
     */
    public static boolean canBeeLineTo(Vector2 pos, Entity entity) {
        Vector2[] corners = getHitBoxCorners(entity, 0);

        Vector2 delta = pos.cpy().sub(entity.getPosition());

        // get quadrant of pos, anticlockwise with top-right = 0, bottom-right = 3
        int quadrant = (delta.x < 0 ? 1 : 0) ^ (delta.y < 0 ? 3 : 0);

        // get corner points that will be the edges of the projection of the entity in the direction
        // of delta
        int corner1 = nextCorner(quadrant);
        int corner2 = prevCorner(quadrant);

        // check intersections on the edges from corner1-quadrant-corner2
        if (testRay(corners[corner1].cpy().add(delta), corners[quadrant].cpy().add(delta)))
            return false;
        if (testRay(corners[quadrant].cpy().add(delta), corners[corner2].cpy().add(delta)))
            return false;

        // check rays spaced MIN_LENGTH_FACE apart within the path of the shape
        for (int c = corner2; ; c = nextCorner(c)) {
            int numRays = (int) (corners[c].dst(corners[nextCorner(c)]) / MIN_LENGTH_FACE) + 2;
            Vector2 from = corners[c].cpy();
            Vector2 to = from.cpy().add(delta);
            Vector2 inc = corners[nextCorner(c)].cpy().sub(from).scl(1f / (numRays - 1));
            for (int i = 0; i < numRays; i++) {
                if (testRay(from.cpy(), to.cpy())) return false;
                from.add(inc);
                to.add(inc);
            }
            if (c == quadrant) break;
        }
        return true;
    }
}
