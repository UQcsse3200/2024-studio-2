package com.csse3200.game.minigames.maze;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MazeTest {
    static final int WIDTH = 10;
    static final int HEIGHT = 12;

    private Maze maze;

    private int mostDistant;

    private boolean[][] reachable;

    @Before
    public void setup() {
        this.maze = new Maze(WIDTH, HEIGHT);
        mostDistant = 0;
        reachable = new boolean[WIDTH][HEIGHT];
    }

    /**
     * Tests if the square constructor correctly makes a square grid
     */
    @Test
    public void testConstructorSquare() {
        int size = 13;
        Maze test = new Maze(size);
        assertEquals(size, test.getHeight());
        assertEquals(size, test.getWidth());
    }

    /**
     * Tests if the maze correctly returns its width
     */
    @Test
    public void testGetWidth() {
        assertEquals(WIDTH, maze.getWidth());
    }

    /**
     * Tests if the maze correctly returns its height
     */
    @Test
    public void testGetHeight() {
        assertEquals(HEIGHT, maze.getHeight());
    }

    /**
     * Tests if the maze correctly identifies a point is in its bounds
     */
    @Test
    public void testInBounds() {
        GridPoint2 pointIn = new GridPoint2(WIDTH-1, HEIGHT-1);
        assertTrue(maze.inBounds(pointIn));
        assertTrue(maze.inBounds(pointIn.x, pointIn.y));
        pointIn.set(0, 0);
        assertTrue(maze.inBounds(pointIn));
        assertTrue(maze.inBounds(pointIn.x, pointIn.y));
    }

    /**
     * Tests if the maze correctly identifies a point is out of its bounds
     */
    @Test
    public void testOutBounds() {
        GridPoint2 pointOut = new GridPoint2(WIDTH, HEIGHT-1);
        assertFalse(maze.inBounds(pointOut));
        assertFalse(maze.inBounds(pointOut.x, pointOut.y));
        pointOut.set(WIDTH-1, HEIGHT);
        assertFalse(maze.inBounds(pointOut));
        assertFalse(maze.inBounds(pointOut.x, pointOut.y));
        pointOut.set(-1, 0);
        assertFalse(maze.inBounds(pointOut));
        assertFalse(maze.inBounds(pointOut.x, pointOut.y));
        pointOut.set(0, -1);
        assertFalse(maze.inBounds(pointOut));
        assertFalse(maze.inBounds(pointOut.x, pointOut.y));
    }

    /**
     * Tests if the maze correctly returns random points from the grid
     */
    @Test
    public void testGetRandomCell() {
        for (int i = 0; i < 10; i++) {
            assertTrue(maze.inBounds(maze.getRandomCell()));
        }
    }

    /**
     * Tests if the maze returns starting locations that are within bounds
     */
    @Test
    public void testGetNextStartLocation() {
        for (int i = 0; i < 10; i++) {
            assertTrue(maze.inBounds(maze.getNextStartLocation()));
        }
    }

    /**
     * Tests if the maze is consistent in reporting walls
     */
    @Test
    public void testIsWall() {
        for (int i = 0; i < 10; i++) {
            GridPoint2 cell = maze.getRandomCell();
            for (GridPoint2 blocked : maze.getNotMazeAdjacent(cell)) {
                assertTrue(maze.isWall(cell, blocked.cpy().sub(cell)));
            }
        }
    }

    void dfs(GridPoint2 current, GridPoint2 parent, int distance) {
        mostDistant = Math.max(distance, mostDistant);
        reachable[current.x][current.y] = true;
        for (GridPoint2 next : maze.getMazeAdjacent(current)) {
            if (!next.equals(parent)) {
                dfs(next, current, distance + 1);
            }
        }
    }

    /**
     * Tests if every cell in the randomly generated maze is reachable
     */
    @Test
    public void testReachable() {
        GridPoint2 cell = maze.getRandomCell();
        dfs(cell, cell, 0);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                assertTrue(reachable[x][y]);
            }
        }
    }

    /**
     * Tests if the most distant cell reported by bfs is correct
     */
    @Test
    public void testDistance() {
        GridPoint2 cell = maze.getRandomCell();
        dfs(cell, cell, 0);
        Maze.BreadthFirstSearch bfs = maze.new BreadthFirstSearch(cell);
        GridPoint2 mostDistantPoint = bfs.getMostDistant();
        assertEquals(mostDistant, bfs.distances[mostDistantPoint.x][mostDistantPoint.y]);
    }
}
