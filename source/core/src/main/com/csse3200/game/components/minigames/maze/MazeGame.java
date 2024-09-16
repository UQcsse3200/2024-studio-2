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
        this.grid = new MazeGrid(12, 5);
        this.renderer = new MinigameRenderer();
        initRenderers();
    }

    private void initRenderers() {
        renderer.addRenderable(new MazeGridRenderer(grid, renderer));
    }

    public void render() {
        renderer.render();
    }

    public void dispose() {
        renderer.dispose();
    }

    public MinigameRenderer getRenderer() {
        return renderer;
    }
}

