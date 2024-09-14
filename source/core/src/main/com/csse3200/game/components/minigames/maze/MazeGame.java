package com.csse3200.game.components.minigames.maze;

import com.csse3200.game.components.minigames.MinigameRenderer;
import com.csse3200.game.components.minigames.maze.mazegrid.MazeGrid;
import com.csse3200.game.components.minigames.maze.rendering.MazeGridRenderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class MazeGame {

    private final MazeGrid grid;
    private final MinigameRenderer renderer;

    public MazeGame() {
        this.grid = new MazeGrid(31, MazeAssetPaths.MAZE_TWO);
        this.renderer = new MinigameRenderer();
        initRenderers();
    }

    private void initRenderers() {
        ServiceLocator.registerResourceService(new ResourceService());
        renderer.addRenderable(new MazeGridRenderer(grid, renderer));
    }

    public void render() {
        renderer.render();
    }
}

