package com.csse3200.game.minigames.maze.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.minigames.MinigameRenderable;
import com.csse3200.game.minigames.MinigameRenderer;
import com.csse3200.game.minigames.maze.MazeAssetPaths;
import com.csse3200.game.minigames.maze.mazegrid.MazeCell;
import com.csse3200.game.minigames.maze.mazegrid.MazeGrid;
import com.csse3200.game.minigames.maze.mazegrid.Spawn;
import com.csse3200.game.minigames.maze.mazegrid.Wall;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/**
 * Render for the underwater maze mini-game
 */
public class MazeGridRenderer implements MinigameRenderable {

    private final MazeCell[][] maze;
    private final MinigameRenderer renderer;
    private Texture waterTexture;
    private Texture wallTexture;
    private Texture spawnTexture;


    public MazeGridRenderer(MazeGrid grid, MinigameRenderer renderer) {
        this.maze = grid.getMaze();
        this.renderer = renderer;
        loadAssets();
    }

    /**
     * Renders the maze
     */
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
                } else if (cell instanceof Spawn) {
                    renderer.getSb().draw(spawnTexture,cell.getPosition().x,
                            cell.getPosition().y,
                            cell.getSize(),
                            cell.getSize());
                } else {
                    renderer.getSb().draw(waterTexture,cell.getPosition().x,
                            cell.getPosition().y,
                            cell.getSize(),
                            cell.getSize());
                }
            }
        }

    }

    /**
     * load assets
     */
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{MazeAssetPaths.WATER, MazeAssetPaths.WALL, MazeAssetPaths.SPAWN});
        ServiceLocator.getResourceService().loadAll();
        waterTexture = rs.getAsset(MazeAssetPaths.WATER, Texture.class);
        wallTexture = rs.getAsset(MazeAssetPaths.WALL, Texture.class);
        spawnTexture = rs.getAsset(MazeAssetPaths.SPAWN, Texture.class);
    }

    /**
     * unload assets
     */
    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{MazeAssetPaths.WATER, MazeAssetPaths.WALL, MazeAssetPaths.SPAWN});
    }

    /**
     * dispose
     */
    public void dispose() {
        unloadAssets();
        waterTexture.dispose();
        wallTexture.dispose();
        spawnTexture.dispose();
    }
}
