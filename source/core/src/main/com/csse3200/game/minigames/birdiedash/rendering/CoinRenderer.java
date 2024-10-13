package com.csse3200.game.minigames.birdiedash.rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdiedash.entities.Coin;
import java.util.List;

/**
 * Renderer for the coins in the birdie dash mini-game
 */
public class CoinRenderer implements MinigameRenderable {
    private final List<Coin> coins;
    private final MinigameRenderer renderer;
    private Animation<TextureRegion> coinAnimation;
    private float stateTime; // To track animation time


    // The Constructor with default width and height
    public CoinRenderer(List<Coin> coins, MinigameRenderer renderer) {
        this.coins = coins;
        this.renderer = renderer;
        renderer.addRenderable(this);
        loadAssets();
    }

    /**
     * renders the coin
     */
    public void render() {

        stateTime += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = coinAnimation.getKeyFrame(stateTime, true);

        for (Coin coin : this.coins) {
            renderer.getSb().draw(currentFrame,
                    coin.getPosition().x - coin.getWidth()/2,
                    coin.getPosition().y - coin.getWidth()/2,
                    coin.getWidth(),
                    coin.getHeight());
        }
    }

    /**
     * Loads the assets
     */
    private void loadAssets() {
        TextureAtlas atlas = new TextureAtlas("images/minigames/coin.atlas");
        Array<TextureAtlas.AtlasRegion> frames = new Array<>();

        for (int i = 1; i <= 7; i++) {
            TextureAtlas.AtlasRegion region = atlas.findRegion("frame" + i);
            if (region != null) {
                frames.add(region);
            }
        }
        coinAnimation = new Animation<>(0.3f, frames, Animation.PlayMode.LOOP);
    }

    /**
     * dispose
     */
    public void dispose() {
        // Nothing to dispose here
    }
}

