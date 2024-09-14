package com.csse3200.game.components.minigames.maze.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.minigames.MinigameRenderable;
import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.maze.MazeAssetPaths;
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

    // Screen dimensions
    private final float screenWidth = 1920f;
    private final float screenHeight = 1200f;

    // Desired grid area size
    private final float gridSize = 1000f;

    // Calculated starting positions for centering the grid
    private final float gridX = (screenWidth - gridSize) / 2; // Horizontal center
    private final float gridY = (screenHeight - gridSize) / 2; // Vertical center

    // The size of each cell
    private final float cellSize;

    public MazeGridRenderer(MazeGrid grid, MinigameRenderer renderer) {
        this.maze = grid.getMaze();
        this.renderer = renderer;
        this.cellSize = gridSize / grid.getMaze().length;  // Divide the grid size by the number of rows/columns
        loadAssets();
    }

    public void render() {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                MazeCell cell = maze[row][col];
                // Calculate position of each cell based on grid position and cell size
                float x = gridX + col * cellSize;
                float y = gridY + (maze.length - row - 1) * cellSize;  // Inverted Y-axis for LibGDX

                // Use renderer to draw the corresponding texture
                if (cell instanceof Wall) {
                    renderer.getSb().draw(wallTexture, x, y, cellSize, cellSize);
                } else {
                    renderer.getSb().draw(waterTexture, x, y, cellSize, cellSize);
                }
            }
        }
    }

    /**
     * load assets
     */
    private void loadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.loadTextures(new String[]{MazeAssetPaths.WATER, MazeAssetPaths.WALL});
        ServiceLocator.getResourceService().loadAll();
        waterTexture = rs.getAsset(MazeAssetPaths.WATER, Texture.class);
        wallTexture = rs.getAsset(MazeAssetPaths.WALL, Texture.class);
    }

    /**
     * unload assets
     */
    private void unloadAssets() {
        ResourceService rs = ServiceLocator.getResourceService();
        rs.unloadAssets(new String[]{MazeAssetPaths.WATER, MazeAssetPaths.WALL});
    }

    /**
     * dispose
     */
    public void dispose() {
        unloadAssets();
        waterTexture.dispose();
        wallTexture.dispose();
    }
}
