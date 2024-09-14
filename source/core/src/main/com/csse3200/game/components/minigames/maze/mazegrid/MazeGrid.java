package com.csse3200.game.components.minigames.maze.mazegrid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

    /**
     * Reads the file and constructs the maze by filling the cells array.
     * Each character in the file is read, where '1' corresponds to a Wall and '0' to a NotWall.
     */
    private void createMaze() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int row = 0;
            // Read each line from the file
            while ((line = br.readLine()) != null) {
                for (int col = 0; col < line.length() && col < size; col++) {
                    char ch = line.charAt(col);
                    // Make a wall if it's a 1, otherwise normal path
                    if (ch == '1') {
                        cells[row][col] = new Wall(row, col);
                    } else {
                        cells[row][col] = new NotWall(row, col);
                    }
                }
                row++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("File path is wrong");
        } catch (IOException e) {
            System.out.println("Can't read line");
        }
    }
}
