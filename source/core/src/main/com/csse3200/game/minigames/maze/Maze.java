package com.csse3200.game.minigames.maze;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.utils.math.RandomUtils;

import java.util.*;

import static com.csse3200.game.utils.math.GridPoint2Utils.GRID_DIRECTIONS;

/**
 * Represents a maze on a 2d grid. Allows for generation of random mazes, finding reasonable spawn
 * locations of objects in the maze and querying shortest paths.
 */
public class Maze {
    private final int width;
    private final int height;
    public final List<GridPoint2>[][] adjacency;
    List<GridPoint2> startLocationSpanningTree;

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

        GridPoint2 start = getRandomCell();
        startLocationSpanningTree = new ArrayList<>();
        startLocationSpanningTree.add(start);
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
     * @param cell Grid cell
     * @return the cell of the maze at the coordinate
     */
    public List<GridPoint2> getNotMazeAdjacent(GridPoint2 cell) {
        List<GridPoint2> cells = new ArrayList<>();
        for (GridPoint2 adjacentCell : getAdjacent(cell)) {
            if (!getMazeAdjacent(cell).contains(adjacentCell)) {
                cells.add(adjacentCell);
            }
        }
        return cells;
    }

    public boolean isWall(int x, int y, GridPoint2 direction) {
        return isWall(new GridPoint2(x, y), direction);
    }


    /**
     * Method to check if moving 1 cell in a given direction from a grid cell will
     * run into a wall.
     * @param cell Grid cell
     * @param direction Direction to check
     * @return whether there is a wall in that direction from this grid cell.
     */
    public boolean isWall(GridPoint2 cell, GridPoint2 direction) {
        return !getMazeAdjacent(cell).contains(cell.cpy().add(direction));
    }

    /**
     * Determines a position is in bounds of the maze
     * @param x x-position/column
     * @param y y-position/row
     * @return true if not in bounds, otherwise false
     */
    public boolean inBounds(int x, int y) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    public boolean inBounds(GridPoint2 cell) {
        return inBounds(cell.x, cell.y);
    }

    /**
     * Method to get the grid cells adjacent to a specific cell (ignoring maze walls)
     * @param cell Grid cell
     * @return the cell of the maze at the coordinate
     */
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
     * Connect two cells by breaking the wall between then. Assumes they are not already connected.
     * @param u first cell
     * @param v second cell
     */
    public void connect(GridPoint2 u, GridPoint2 v) {
        getMazeAdjacent(u).add(v);
        getMazeAdjacent(v).add(u);
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

    /**
     * Find a new start location that is reasonably well spaced from all other
     * start locations.
     * Maintains a spanning tree connecting the current set of start locations. Then
     * uses BFS to find the most distant cell from this spanning tree and returns it.
     * I think I have a proof this approach constructs the set of n starting locations that
     * maximises the minimum time to walk between every location for a tree graph, but since our
     * maze has cycles, this is not guaranteed.
     * @return The starting location grid cell
     */
    public GridPoint2 getNextStartLocation() {

        breadthFirstSearch bfs = new breadthFirstSearch(startLocationSpanningTree);
        GridPoint2 mostDistant = bfs.getMostDistant();
        List<GridPoint2> path = bfs.getShortestPath(mostDistant);
        // don't add first cell of path because it is already in the tree
        for (int cell = 1; cell < path.size(); cell++) {
            startLocationSpanningTree.add(path.get(cell));
        }

        return mostDistant;
    }

    /**
     * Implements the recursive backtracking maze generation algorithm.
     * Recursively construct a maze by breaking walls to adjacent cells that
     * have not yet been explored and backtracking when no such cell exists.
     * @param cell Current cell
     * @param rand The random source for shuffling dfs order.
     */
    private void recursiveBacktracking(GridPoint2 cell, Random rand) {
        List<GridPoint2> adjacent = getAdjacent(cell);
        Collections.shuffle(adjacent, rand);

        for (GridPoint2 newCell : adjacent) {
            if (getMazeAdjacent(newCell).isEmpty()) {
                connect(cell, newCell);
                recursiveBacktracking(newCell, rand);
            }
        }
    }

    /**
     * Class for bfs, used for finding paths in the maze (including the angular fish path to the player)
     */
    public class breadthFirstSearch {
        int[][] distances;
        GridPoint2[][] previous;
        GridPoint2 mostDistant;


        public breadthFirstSearch(GridPoint2 startCell) {
            this(Collections.singletonList(startCell));
        }

        /**
         * bfs algorithm
         * @param startCells the initial position
         */
        public breadthFirstSearch(List<GridPoint2> startCells) {
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
         * Get the shortest path from initial grid cell to current
         * @param end the grid cell to search for
         * @return the shortest path
         */
        public List<GridPoint2> getShortestPath(GridPoint2 end) {
            List<GridPoint2> path = new ArrayList<>();
            while (end != null) {
                path.add(end);
                end = previous[end.x][end.y];
            }
            return path.reversed();
        }

        /**
         * @return The most distant grid cell from the set of start locations in the BFS
         */
        public GridPoint2 getMostDistant() {
            return mostDistant;
        }

    }

    /**
     * Break some walls in the maze to create cycles and multiple paths between cells.
     *
     * @param count the number of walls to break
     */
    public void breakWalls(int count) {
        Random rand = new Random();
        while (count > 0) {
            GridPoint2 cell = getRandomCell();
            List<GridPoint2> walledCells = getNotMazeAdjacent(cell);
            if (walledCells.isEmpty()) {
                continue;
            }
            GridPoint2 other = walledCells.get(rand.nextInt(walledCells.size()));
            connect(cell, other);
            count--;
        }
    }

    /**
     * Get the maze width
     * @return the maze width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the maze height
     * @return the maze height
     */
    public int getHeight() {
        return height;
    }
}