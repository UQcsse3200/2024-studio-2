package com.csse3200.game.components.minigames.maze.mazegrid;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Class of maze cells for the maze game.
 */
public abstract class MazeCell {
    protected Vector2 position;
    protected Rectangle collisionBox;

    public MazeCell(int x, int y) {
        this.position = new Vector2(x, y);
        // Arbitrary height and width for now. will change
        this.collisionBox = new Rectangle(x, y, 50, 50);
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Rectangle getCollisionBox() {
        return this.collisionBox;
    }
}
