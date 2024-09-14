package com.csse3200.game.components.minigames.maze.mazegrid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.io.*;

/**
 * Represents a maze grid that is created from a file.
 * The grid contains cells of type MazeCell, which can either be Wall or NotWall.
 * The grid is built based on a text file where '1' represents a Wall and '0' represents a
 * NotWall.
 */
public class MazeGrid{


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
     * The maze is constructed by reading from the file, which contains '1's for walls and '0's
     * for paths.
     *
     * @param size  The size of the maze grid.
     * @param file   The file path to the maze text file.
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

    /***
     * Method to get the cell at a specific coordinate
     * @param row the row
     * @param col the column
     * @return
     */
    public MazeCell getCell(int row, int col) {
        return cells[row][col];
    }

    public MazeCell[][] getMaze() {
        return cells;
    }

    /**
     * Reads the file and constructs the maze by filling the cells array.
     * Each character in the file is read, where '1' corresponds to a Wall and '0' to a NotWall.
     */
    private void createMaze() {
        if (isLibGDXEnvironemnt()) {
            FileHandle fileHandle = Gdx.files.internal(file);  // Use Gdx.files to load the file
            String[] lines = fileHandle.readString().split("\n");  // Read the file content

            for (int row = 0; row < lines.length; row++) {
                String line = lines[row];
                for (int col = 0; col < line.length() && col < size; col++) {
                    char ch = line.charAt(col);
                    // Calculate the position for this cell
                    float x = gridX + col * cellSize;
                    float y = gridY + (size - row - 1) * cellSize;  // Correctly calculate the Y position
                    System.out.println(new Vector2(x,y));

                    if (ch == '1') {
                        cells[row][col] = new Wall(x, y, cellSize);
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
    private boolean isLibGDXEnvironemnt() {
        return Gdx.files != null;
    }

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
}
