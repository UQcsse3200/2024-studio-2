package com.csse3200.game.components.quests;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LogButton extends ImageButton {

    public LogButton(Texture upTexture, Texture downTexture, Texture iconTexture) {
        super(createStyle(upTexture, downTexture, iconTexture));
    }

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
