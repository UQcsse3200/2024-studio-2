package com.csse3200.game.components.minigames.maze.mazegrid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

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
        createMaze();
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
        // For in game
        if (isLibGDXEnvironemnt()) {
            FileHandle fileHandle = Gdx.files.internal(file);  // Use Gdx.files to load the file
            String[] lines = fileHandle.readString().split("\n");  // Read the file content

            for (int row = 0; row < lines.length; row++) {
                String line = lines[row];
                for (int col = 0; col < line.length() && col < size; col++) {
                    char ch = line.charAt(col);
                    if (ch == '1') {
                        cells[row][col] = new Wall(row, col);
                    } else {
                        cells[row][col] = new Water(row, col);
                    }
                }
            }
        }
        else {
            setUpInTest();
        }
    }

    /**
     * Private method to check if in game or junit test
     * @return whether it is game or junit environemnt
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
                    // Make a wall if it's a 1, otherwise water
                    if (ch == '1') {
                        cells[row][col] = new Wall(row, col);
                    } else {
                        cells[row][col] = new Water(row, col);
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
