package com.csse3200.game.components.minigames.maze.mazegrid;

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

    public float getSize() {
        return tileSize;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public Vector2 getPosition() {
        return this.position;
    }


    public Rectangle getCollisionBox() {
        return this.collisionBox;
    }
}
