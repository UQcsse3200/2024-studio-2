package com.csse3200.game.minigames.maze.old.mazegrid.mazegrid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.csse3200.game.minigames.Direction;
import com.csse3200.game.minigames.GridCell;

import java.io.*;
import java.util.*;

/**
 * Represents a maze grid that is created from a file.
 * The grid contains cells of type MazeCell, which can either be Wall or NotWall.
 * The grid is built based on a text file where '1' represents a Wall and '0' represents a
 * NotWall.
 */
public class MazeGrid{
    private final int size;
    private final MazeCell[][] cells; // 2D array representing the cells

    // Screen size
    private final float screenWidth = 1920f;
    private final float screenHeight = 1200f;
    private final float gridWidth = 1000;
    private final float gridHeight = 1000;
    private float gridX;
    private float gridY;
    private float cellSize;

    /**
     * Creates a new MazeGrid with the specified dimensions.
     * The maze is constructed by reading from the file, which contains '1's for walls and '0's
     * for paths.
     *
     * @param size  The size of the maze grid.
     * @param file   The file path to the maze text file.
     */
    public MazeGrid(int size, String file) {
        this.size = size;
        this.cells = new MazeCell[size][size];
        calculateCellDimensions();
        readMazeFromFile(file);
    }

    public MazeGrid(int size, int pathingSize) {
        this.size = size * (pathingSize + 1) + 1;
        this.cells = new MazeCell[this.size][this.size];
        calculateCellDimensions();
        generateRandomMaze(pathingSize, 10);
    }

    /**
     * Calculates the size a cell should be based on screen dimensions
     */
    private void calculateCellDimensions() {
        this.gridX = (screenWidth - gridWidth) / 2; // Center the grid horizontally
        this.gridY = (screenHeight - gridHeight) / 2; // Center the grid vertically
        this.cellSize = gridWidth / size;
    }

    /***
     * Method to get the cell at a specific coordinate
     * @param row the row
     * @param col the column
     * @return the cell of the maze at the coordinate
     */
    public MazeCell getCell(int row, int col) {
        return cells[row][col];
    }

    /**
     * Gets the maze
     * @return the maze
     */
    public MazeCell[][] getMaze() {
        return cells;
    }

    /**
     * Constructs a random maze using a recursive backtracking maze generator algorithm.
     * Assumes maze has not been generated or read from a file previously.
     */
    private void generateRandomMaze(int pathingSize, int numSpawns) {
        Random rand = new Random();
        int r = rand.nextInt(size/(pathingSize+1) - 1) * (pathingSize+1) + 1;
        int c = rand.nextInt(size/(pathingSize+1) - 1) * (pathingSize+1) + 1;

        recursiveBacktracking(r, c, pathingSize, rand);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j] == null) {
                    createCellAtRowCol(i, j, Wall::new);
                }
            }
        }

        List<GridCell> spanningTree = new ArrayList<>();
        spanningTree.add(new GridCell(c, r));

        for (int i = 0; i < numSpawns; i++) {
            breadthFirstSearchMaze bfs = new breadthFirstSearchMaze(spanningTree);
            GridCell mostDistant = bfs.getMostDistant();
            createCellAtRowCol(mostDistant.getY(), mostDistant.getX(), Spawn::new);
            List<GridCell> path = bfs.getShortestPath(bfs.getMostDistant());
            for (int cell = 1; cell < path.size(); cell++) {
                spanningTree.add(path.get(cell));
            }
        }
    }

    /**
     * Determines a position is in bounds of the maze
     * @param r row
     * @param c column
     * @return true if not in bounds, otherwise false
     */
    private boolean notInBounds(int r, int c) {
        return r < 0 || r >= size || c < 0 || c >= size;
    }

    @FunctionalInterface
    interface CellConstructor{
        MazeCell construct(float x, float y, float cellSize);
    }
    private void createCellAtRowCol(int r, int c, CellConstructor constructor) {
        float x = gridX + c * cellSize;
        float y = gridY + (size - r - 1) * cellSize;
        cells[r][c] = constructor.construct(x, y, cellSize);
    }

    private void recursiveBacktracking(int r, int c, int pathingSize, Random rand) {
        for (int i = 0; i < pathingSize; i++) {
            for (int j = 0; j < pathingSize; j++) {
                createCellAtRowCol(r+i, c+j, Water::new);
            }
        }
        List<Direction> directions = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
        Collections.shuffle(directions, rand);

        for (Direction d : directions) {
            int nr = r + d.dy * (pathingSize+1);
            int nc = c + d.dx * (pathingSize+1);
            if (notInBounds(nr, nc) || cells[nr][nc] != null) {
                continue;
            }
            for (int i = 0; i < pathingSize; i++) {
                switch (d) {
                    case UP -> createCellAtRowCol(r + pathingSize, c + i, Water::new);
                    case DOWN -> createCellAtRowCol(r - 1, c + i, Water::new);
                    case RIGHT -> createCellAtRowCol(r + i, c + pathingSize, Water::new);
                    case LEFT -> createCellAtRowCol(r + i, c - 1, Water::new);
                }
            }
            recursiveBacktracking(nr, nc, pathingSize, rand);
        }
    }

    /**
     * Class for bfs
     */
    class breadthFirstSearchMaze {
        int[][] distances;
        GridCell[][] previous;
        GridCell mostDistant;
        static final Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        breadthFirstSearchMaze(List<GridCell> startCells) {
            distances = new int[size][size];
            previous = new GridCell[size][size];
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    distances[r][c] = size*size;
                }
            }
            for (GridCell cell: startCells) {
                distances[cell.getY()][cell.getX()] = 0;
            }
            Queue<GridCell> queue = new ArrayDeque<>(startCells);
            GridCell cell = null;
            while (!queue.isEmpty()) {
                cell = queue.remove();
                for (Direction d : directions) {
                    int nr = cell.getY() + d.dy;
                    int nc = cell.getX() + d.dx;
                    int dst = distances[cell.getY()][cell.getX()] + 1;
                    if (notInBounds(nr, nc) || distances[nr][nc] <= dst || cells[nr][nc] instanceof Wall) {
                        continue;
                    }
                    distances[nr][nc] = dst;
                    previous[nr][nc] = cell;
                    queue.add(new GridCell(nc, nr));
                }
            }
            mostDistant = cell;
        }

        /**
         * get shorted path from initial grid cell to current
         * @param end the grid cell to search for
         * @return the shortest path
         */
        List<GridCell> getShortestPath(GridCell end) {
            List<GridCell> path = new ArrayList<>();
            while (end != null) {
                path.add(end);
                end = previous[end.getY()][end.getX()];
            }
            return path.reversed();
        }

        GridCell getMostDistant() {
            return mostDistant;
        }
    }

    /**
     * Reads the file and constructs the maze by filling the cells array.
     * Each character in the file is read, where '1' corresponds to a Wall and '0' to a NotWall.
     *
     * @param file   The file path to the maze text file.
     */
    private void readMazeFromFile(String file) {
        if (isLibGDXEnvironemnt()) {
            FileHandle fileHandle = Gdx.files.internal(file);  // Use Gdx.files to load the file
            String[] lines = fileHandle.readString().split("\n");  // Read the file content

            for (int row = 0; row < lines.length; row++) {
                String line = lines[row];
                for (int col = 0; col < line.length() && col < size; col++) {
                    char ch = line.charAt(col);
                    if (ch == '1') {
                        createCellAtRowCol(row, col, Wall::new);
                    } else {
                        createCellAtRowCol(row, col, Water::new);
                    }
                }
            }
        } else {
            setUpInTest(file);
        }
    }

    /**
     * Private method to check if in-game or JUnit test
     * @return whether it is game or test environment
     */
    private boolean isLibGDXEnvironemnt() {
        return Gdx.files != null;
    }

    private void setUpInTest(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int row = 0;
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                for (int col = 0; col < line.length() && col < size; col++) {
                    char ch = line.charAt(col);
                    // Calculate the position for this cell
                    float x = gridX + col * cellSize;
                    float y = gridY + (size - row - 1) * cellSize;

                    // Make a wall if it's a '1', otherwise Water cell
                    if (ch == '1') {
                        cells[row][col] = new Wall(x, y, cellSize);
                    } else {
                        cells[row][col] = new Water(x, y, cellSize);
                    }
                }
                row++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Trying to read file from: " + new File(file).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Can't read line");
        }
    }

    public int getSize() {
        return size;
    }
}
