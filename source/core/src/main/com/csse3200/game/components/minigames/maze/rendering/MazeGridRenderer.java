package com.csse3200.game.components.minigames.maze.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.minigames.MinigameRenderable;
import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.maze.MazeAssetPaths;
import com.csse3200.game.components.minigames.maze.mazegrid.Egg;
import com.csse3200.game.components.minigames.maze.mazegrid.MazeCell;
import com.csse3200.game.components.minigames.maze.mazegrid.MazeGrid;
import com.csse3200.game.components.minigames.maze.mazegrid.Wall;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class MazeGridRenderer implements MinigameRenderable {

    private final MazeCell[][] maze;
    private final MinigameRenderer renderer;
    private Texture waterTexture;
    private Texture wallTexture;
    private Texture eggTexture; // Add texture for eggs

    public MazeGridRenderer(MazeGrid grid, MinigameRenderer renderer) {
        this.maze = grid.getMaze();
        this.renderer = renderer;
        loadAssets();
    }

    @Override
    public void render() {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                MazeCell cell = maze[row][col];

                // Use renderer to draw the corresponding texture
                if (cell instanceof Wall) {
                    renderer.getSb().draw(wallTexture, cell.getPosition().x,
                            cell.getPosition().y,
                            cell.getSize(),
                            cell.getSize());
                } else if (cell instanceof Egg) { // Checking for the Egg cell
                    renderer.getSb().draw(eggTexture, cell.getPosition().x,
                            cell.getPosition().y,
                            cell.getSize(),
                            cell.getSize());
                } else {
                    renderer.getSb().draw(waterTexture, cell.getPosition().x,
                            cell.getPosition().y,
                            cell.getSize(),
                            cell.getSize());
                }
            }
        }
    }

    /**
     * Load assets.
     */
    /*private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{MazeAssetPaths.WATER, MazeAssetPaths.WALL, MazeAssetPaths.EGG});
        ServiceLocator.getResourceService().loadAll();
        waterTexture = rs.getAsset(MazeAssetPaths.WATER, Texture.class);
        wallTexture = rs.getAsset(MazeAssetPaths.WALL, Texture.class);
        eggTexture = rs.getAsset(MazeAssetPaths.EGG, Texture.class); // Load egg texture
    }*/
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();

        // Loading the textures
        rs.loadTextures(new String[]{MazeAssetPaths.WATER, MazeAssetPaths.WALL, MazeAssetPaths.EGG});
        rs.loadAll();

        // Debugging
        System.out.println("Attempting to load textures...");

        // Water Texture
        try {
            if (rs.containsAsset(MazeAssetPaths.WATER, Texture.class)) {
                waterTexture = rs.getAsset(MazeAssetPaths.WATER, Texture.class);
                System.out.println("Water Texture Loaded: " + MazeAssetPaths.WATER);
            } else {
                System.out.println("Water texture not loaded.");
            }
        } catch (Exception e) {
            System.out.println("Failed to load water texture: " + e.getMessage());
        }

        // Wall Texture
        try {
            if (rs.containsAsset(MazeAssetPaths.WALL, Texture.class)) {
                wallTexture = rs.getAsset(MazeAssetPaths.WALL, Texture.class);
                System.out.println("Wall Texture Loaded: " + MazeAssetPaths.WALL);
            } else {
                System.out.println("Wall texture not loaded.");
            }
        } catch (Exception e) {
            System.out.println("Failed to load wall texture: " + e.getMessage());
        }

        // Egg Texture
        try {
            if (rs.containsAsset(MazeAssetPaths.EGG, Texture.class)) {
                eggTexture = rs.getAsset(MazeAssetPaths.EGG, Texture.class);
                System.out.println("Egg Texture Loaded: " + MazeAssetPaths.EGG);
            } else {
                System.out.println("Egg texture not loaded.");
            }
        } catch (Exception e) {
            System.out.println("Failed to load egg texture: " + e.getMessage());
        }
    }

    /**
     * Unload assets.
     */
    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{MazeAssetPaths.WATER, MazeAssetPaths.WALL, MazeAssetPaths.EGG});
    }

    /**
     * Dispose of assets.
     */
    //@Override
    public void dispose() {
        unloadAssets();
        waterTexture.dispose();
        wallTexture.dispose();
        eggTexture.dispose(); // Dispose of egg texture
    }
}
