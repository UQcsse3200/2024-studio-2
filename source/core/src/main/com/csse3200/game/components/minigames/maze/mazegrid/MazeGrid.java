package com.csse3200.game.components.minigames.maze.mazegrid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.io.*;

/**
 * Represents a maze grid that is created from a file.
 * The grid contains cells of type MazeCell, which can either be Wall, Water, or Egg.
 * The grid is built based on a text file where '1' represents a Wall, '0' represents a Water,
 * and 'E' represents an Egg.
 */
public class MazeGrid {

    private final String file; // File path to the maze
    private final int size; // Size of the grid
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
     * The maze is constructed by reading from the file, which contains '1's for walls, '0's for paths (water), and 'E's for eggs.
     *
     * @param size  The size of the maze grid.
     * @param file  The file path to the maze text file.
     */
    public MazeGrid(int size, String file) {
        this.size = size;
        this.file = file;
        this.cells = new MazeCell[size][size];
        calculateCellDimensions();
        createMaze();
    }

    private void calculateCellDimensions() {
        this.gridX = (screenWidth - gridWidth) / 2; // Center the grid horizontally
        this.gridY = (screenHeight - gridHeight) / 2; // Center the grid vertically
        this.cellSize = gridWidth / size;
    }

    /**
     * Method to get the cell at a specific coordinate
     * @param row the row
     * @param col the column
     * @return the MazeCell at the specified position
     */
    public MazeCell getCell(int row, int col) {
        return cells[row][col];
    }

    public MazeCell[][] getMaze() {
        return cells;
    }

    /**
     * Reads the file and constructs the maze by filling the cells array.
     * Each character in the file is read, where '1' corresponds to a Wall, '0' to Water, and 'E' to an Egg.
     */
    private void createMaze() {
        if (isLibGDXEnvironment()) {
            FileHandle fileHandle = Gdx.files.internal(file);  // Use Gdx.files to load the file
            String[] lines = fileHandle.readString().split("\n");  // Read the file content

            for (int row = 0; row < lines.length; row++) {
                String line = lines[row];
                for (int col = 0; col < line.length() && col < size; col++) {
                    char ch = line.charAt(col);
                    // Calculate the position for this cell
                    float x = gridX + col * cellSize;
                    float y = gridY + (size - row - 1) * cellSize;
                    System.out.println(new Vector2(x, y));

                    if (ch == '1') {
                        cells[row][col] = new Wall(x, y, cellSize);
                    } else if (ch == 'E') {
                        cells[row][col] = new Egg(x, y, cellSize);  // Egg placement
                    } else {
                        cells[row][col] = new Water(x, y, cellSize);
                    }
                }
            }
        } else {
            setUpInTest();
        }
    }

    /**
     * Private method to check if in-game or JUnit test
     * @return whether it is game or test environment
     */
    private boolean isLibGDXEnvironment() {
        return Gdx.files != null;
    }

    /**
     * Sets up the maze grid in test environments by reading the maze file without LibGDX.
     */
    private void setUpInTest() {
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

                    // Making a wall if it's a '1', water if '0', or egg if 'E'
                    if (ch == '1') {
                        cells[row][col] = new Wall(x, y, cellSize);
                    } else if (ch == 'E') {
                        cells[row][col] = new Egg(x, y, cellSize);  // Egg placement
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
}
