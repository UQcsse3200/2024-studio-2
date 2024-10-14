package com.csse3200.game.minigames.maze.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csse3200.game.rendering.TextureRenderComponent;

public class AlphaTextureRenderComponent extends TextureRenderComponent {
    private float alpha = 1f;

    /**
     * @param texturePath Internal path of static texture to render.
     *                    Will be scaled to the entity's scale.
     */
    public AlphaTextureRenderComponent(String texturePath) {
        super(texturePath);
    }

    /** @param texture Static texture to render. Will be scaled to the entity's scale. */
    //TODO: Can we delete this?
    public AlphaTextureRenderComponent(Texture texture) {
        super(texture);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    public void draw(SpriteBatch sb) {
        Color c = sb.getColor().cpy();
        sb.setColor(c.r, c.g, c.b, alpha);
        super.draw(sb);
        sb.setColor(c);
    }
}
