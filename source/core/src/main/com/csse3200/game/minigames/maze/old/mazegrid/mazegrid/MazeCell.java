package com.csse3200.game.minigames.maze.old.mazegrid.mazegrid;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Class of maze cells for the maze game.
 */
public abstract class MazeCell {
    protected Vector2 position;
    protected Rectangle collisionBox;
    private float tileSize;

    public MazeCell(float x, float y, float size) {
        this.position = new Vector2(x, y);
        this.tileSize = size;
        // Arbitrary height and width for now. will change
        this.collisionBox = new Rectangle(x, y, tileSize, tileSize);
    }

    /**
     * Gets the size of each tile in the maze
     * @return the size of each tile in the maze
     */
    public float getSize() {
        return tileSize;
    }

    /**
     * set the position of the maze tile
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    /**
     * Gets the tiles position
     * @return
     */
    public Vector2 getPosition() {
        return this.position;
    }

    /**
     * Determines if this tile has a collision
     * @return tru if collision, otherwise false
     */
    public Rectangle getCollisionBox() {
        return this.collisionBox;
    }
}
