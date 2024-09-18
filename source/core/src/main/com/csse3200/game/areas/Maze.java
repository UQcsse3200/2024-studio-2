package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.utils.math.RandomUtils;

import java.util.*;

import static com.csse3200.game.utils.math.GridPoint2Utils.GRID_DIRECTIONS;

/**
 * Represents a maze grid that is created from a file.
 * The grid contains cells of type MazeCell, which can either be Wall or NotWall.
 * The grid is built based on a text file where '1' represents a Wall and '0' represents a
 * NotWall.
 */
public class Maze {
    private final int width;
    private final int height;
    public final List<GridPoint2>[][] adjacency;

    /**
     * Creates a new MazeGrid with the specified dimensions.
     * The maze is constructed by reading from the file, which contains '1's for walls and '0's
     * for paths.
     *
     * Odd cells are always pathing, even cells are walls that may or may not be broken.
     *
     * The first line of the file contains two integers - the width and
     *
     * @param file   The file path to the maze text file.
     */
    //public Maze(String file) {}

    public Maze(int size) {
        this(size, size);
    }

    public Maze(GridPoint2 size) {
        this(size.x, size.y);
    }

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        adjacency = new ArrayList[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                adjacency[x][y] = new ArrayList<>();
            }
        }
        generateRandomMaze();
    }

    /**
     * Method to get the grid cells adjacent to a specific cell (taking into account maze walls)
     * @param x x-position/column
     * @param y y-position/row
     * @return the cell of the maze at the coordinate
     */
    public List<GridPoint2> getMazeAdjacent(int x, int y) {
        return adjacency[x][y];
    }

    public List<GridPoint2> getMazeAdjacent(GridPoint2 cell) {
        return getMazeAdjacent(cell.x, cell.y);
    }

    /**
     * Method to get the grid cells not adjacent to a specific cell (taking into account walls)
     * @param x x-position/column
     * @param y y-position/row
     * @return the cell of the maze at the coordinate
     */
    public List<GridPoint2> getNotMazeAdjacent(int x, int y) {
        return getNotMazeAdjacent(new GridPoint2(x, y));
    }
    public List<GridPoint2> getNotMazeAdjacent(GridPoint2 cell) {
        List<GridPoint2> cells = new ArrayList<>();
        for (GridPoint2 adjacentCell : getMazeAdjacent(cell)) {
            if (!getMazeAdjacent(cell).contains(adjacentCell)) {
                cells.add(adjacentCell);
            }
        }
        return cells;
    }

    public boolean isWall(int x, int y, GridPoint2 direction) {
        return isWall(new GridPoint2(x, y), direction);
    }

    public boolean isWall(GridPoint2 cell, GridPoint2 direction) {
        return !getMazeAdjacent(cell).contains(cell.cpy().add(direction));
    }

    /**
     * Determines a position is in bounds of the maze
     * @param x x-position/column
     * @param y y-position/row
     * @return true if not in bounds, otherwise false
     */
    private boolean inBounds(int x, int y) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    private boolean inBounds(GridPoint2 cell) {
        return inBounds(cell.x, cell.y);
    }

    /**
     * Method to get the grid cells adjacent to a specific cell (ignoring maze walls)
     * @param x x-position/column
     * @param y y-position/row
     * @return the cell of the maze at the coordinate
     */
    public List<GridPoint2> getAdjacent(int x, int y) {
        return getAdjacent(new GridPoint2(x, y));
    }

    public List<GridPoint2> getAdjacent(GridPoint2 cell) {
        List<GridPoint2> adjacent = new ArrayList<>();
        for (GridPoint2 delta : GRID_DIRECTIONS) {
            GridPoint2 newCell = cell.cpy().add(delta);
            if (inBounds(newCell)) {
                adjacent.add(newCell);
            }
        }
        return adjacent;
    }

    /**
     * @return A random maze cell
     */
    public GridPoint2 getRandomCell() {
        return RandomUtils.random(new GridPoint2(0, 0),
                new GridPoint2(width-1, height-1));
    }

    /**
     * Constructs a random maze using a recursive backtracking maze generator algorithm.
     * Assumes maze has not been generated or read from a file previously.
     */
    private void generateRandomMaze() {
        recursiveBacktracking(getRandomCell(), new Random());
    }

    public List<GridPoint2> generateStartLocations(int count) {
        GridPoint2 start = getRandomCell();

        List<GridPoint2> spanningTree = new ArrayList<>();
        spanningTree.add(start);
        List<GridPoint2> startLocations = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            breadthFirstSearch bfs = new breadthFirstSearch(spanningTree);
            GridPoint2 mostDistant = bfs.getMostDistant();
            startLocations.add(mostDistant);
            List<GridPoint2> path = bfs.getShortestPath(bfs.getMostDistant());
            // don't add first cell of path because it is already in the tree
            for (int cell = 1; cell < path.size(); cell++) {
                spanningTree.add(path.get(cell));
            }
        }

        return startLocations;
    }

    private void recursiveBacktracking(GridPoint2 cell, Random rand) {
        List<GridPoint2> adjacent = getAdjacent(cell);
        Collections.shuffle(adjacent, rand);

        for (GridPoint2 newCell : adjacent) {
            if (getMazeAdjacent(newCell).isEmpty()) {
                getMazeAdjacent(cell).add(newCell);
                getMazeAdjacent(newCell).add(cell);
                recursiveBacktracking(newCell, rand);
            }
        }
    }

    /**
     * Class for bfs
     */
    class breadthFirstSearch {
        int[][] distances;
        GridPoint2[][] previous;
        GridPoint2 mostDistant;

        breadthFirstSearch(List<GridPoint2> startCells) {
            distances = new int[width][height];
            previous = new GridPoint2[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    distances[x][y] = width*height;
                }
            }
            for (GridPoint2 cell: startCells) {
                distances[cell.x][cell.y] = 0;
            }
            Queue<GridPoint2> queue = new ArrayDeque<>(startCells);
            GridPoint2 cell = null;
            while (!queue.isEmpty()) {
                cell = queue.remove();
                int dst = distances[cell.x][cell.y] + 1;
                for (GridPoint2 next : getMazeAdjacent(cell)) {
                    if (dst < distances[next.x][next.y]) {
                        distances[next.x][next.y] = dst;
                        previous[next.x][next.y] = cell;
                        queue.add(next);
                    }
                }
            }
            mostDistant = cell;
        }

        /**
         * get shorted path from initial grid cell to current
         * @param end the grid cell to search for
         * @return the shortest path
         */
        List<GridPoint2> getShortestPath(GridPoint2 end) {
            List<GridPoint2> path = new ArrayList<>();
            while (end != null) {
                path.add(end);
                end = previous[end.x][end.y];
            }
            return path.reversed();
        }

        GridPoint2 getMostDistant() {
            return mostDistant;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}