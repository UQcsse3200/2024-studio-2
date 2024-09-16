package com.csse3200.game.components.minigames.maze.mazegrid;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents an Egg cell in the maze.
 * Loading the Egg Image for the maze.
 */
public class Egg extends MazeCell {

    private static final String TEXTURE_PATH = "images/minigames/egg.png";

    public Egg(float x, float y, float size) {
        super(x, y, size);
        this.texture = new Texture(TEXTURE_PATH); // Load the egg texture
    }
}