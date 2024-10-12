package com.csse3200.game.minigames.birdiedash.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.birdiedash.entities.Coin;
import com.csse3200.game.minigames.snake.AssetPaths;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * Renderer for the coins in the birdie dash mini-game
 */
public class CoinRenderer implements MinigameRenderable {
    private final List<Coin> coins;
    private final MinigameRenderer renderer;
    private Texture coinTexture;
    private TextureRegion coinRegion;

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
    public void render(){
        for (Coin coin : this.coins) {
            renderer.getSb().draw(coinRegion,
                    coin.getPosition().x - coin.getWidth()/2,
                    coin.getPosition().y - coin.getWidth()/2,
                    coin.getWidth(),
                    coin.getHeight());
        }
    }

    /**
     * Loads the asse
     */
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{AssetPaths.COIN});
        ServiceLocator.getResourceService().loadAll();
        coinTexture = rs.getAsset(AssetPaths.COIN, Texture.class);
        int centerX = coinTexture.getWidth() / 2;
        int centerY = coinTexture.getHeight() / 2;
        int regionWidth = 400;
        int regionHeight = 480;
        int x = centerX - regionWidth / 2;
        int y = centerY - regionHeight / 2;
        coinRegion = new TextureRegion(coinTexture, x, y, regionWidth, regionHeight);
    }

    /**
     * unloads assets
     */
    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{AssetPaths.COIN});
    }

    /**
     * dispose
     */
    public void dispose() {
        unloadAssets();
        coinTexture.dispose();
    }
}

