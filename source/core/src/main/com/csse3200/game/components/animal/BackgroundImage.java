package com.csse3200.game.components.animal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Represents a background image to be displayed on the stage.
 * The background image fills the entire screen.
 */
public class BackgroundImage extends Image {

    /**
     * Constructs a BackgroundImage using the specified image path.
     * The image is set to fill the entire parent area.
     * @param imagePath The path to the background image.
     */
    public BackgroundImage(String imagePath) {
        super(new Texture(imagePath));
        setFillParent(true); // This makes the image fill the entire stage or parent
    }
}
