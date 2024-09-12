package com.csse3200.game.components.quests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * A custom button class that extends {@link ImageButton}.
 * The button is created with an "up" texture, a "down" texture, and an icon texture that is
 * displayed centered on the button.
 */
public class LogButton extends ImageButton {

    /**
     * Constructs a new LogButton with specified textures for its up state, down state, and icon.
     * @param upTexture   The texture displayed when the button is not pressed.
     * @param downTexture The texture displayed when the button is pressed.
     * @param iconTexture The texture for the icon displayed at the center of the button.
     */
    public LogButton(Texture upTexture, Texture downTexture, Texture iconTexture) {
        super(createStyle(upTexture, downTexture, iconTexture));
    }

    /**
     * Forms the button style
     * @param upTexture   The texture for the button's up state.
     * @param downTexture The texture for the button's down state.
     * @param iconTexture The texture for the icon displayed at the center of the button.
     * @return The constructed {@link ImageButtonStyle}.
     */
    private static ImageButtonStyle createStyle(Texture upTexture, Texture downTexture, Texture iconTexture) {
        ImageButtonStyle style = new ImageButtonStyle();

        style.up = new TextureRegionDrawable(new TextureRegion(upTexture));
        style.down = new TextureRegionDrawable(new TextureRegion(downTexture));

        TextureRegion iconRegion = new TextureRegion(iconTexture);
        TextureRegionDrawable iconDrawable = new TextureRegionDrawable(iconRegion);

        float iconSize = Math.min(upTexture.getWidth(), upTexture.getHeight()) * 0.5f;
        iconDrawable.setMinWidth(iconSize);
        iconDrawable.setMinHeight(iconSize);

        style.imageDown = iconDrawable;
        style.imageUp = iconDrawable;
        return style;
    }
}
