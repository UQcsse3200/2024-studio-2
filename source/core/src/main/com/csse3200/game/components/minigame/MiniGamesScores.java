package com.csse3200.game.components.minigame;

/**
 * Class to store games high scores
 */
public class MiniGamesScores {
    private int snakeHighScore;
    private int flappyBirdHighScore;
    private int mazeHighScore;
    private MiniGameMedals snakeMedal;
    private MiniGameMedals flappyBirdMedal;
    private MiniGameMedals mazeMedal;

    public MiniGamesScores() {
        snakeHighScore = 0;
        flappyBirdHighScore = 0;
        mazeHighScore = 0;
        snakeMedal = MiniGameMedals.FAIL;
        flappyBirdMedal = MiniGameMedals.FAIL;
        mazeMedal = MiniGameMedals.FAIL;
    }

    /**
     * Get the snake high score
     * @return the high score for the snake game
     */
    public int getSnakeHighScore() {
        return snakeHighScore;
    }

    /**
     * Get the snake high score medal
     * @return the snake high score medal
     */
    public MiniGameMedals getSnakeMedal() {
        return snakeMedal;
    }

    /**
     * Compares current high score and adjusts medals and high score appropriately
     * @param value the new score to be checked
     */
    public void checkAndSetSnakeScoreMedal(int value) {
        if (value > this.snakeHighScore) {
            if (value >= MiniGameConstants.SNAKE_GOLD_THRESHOLD) {
                // Gold medal
                this.snakeMedal = MiniGameMedals.GOLD;
            } else if (value >= MiniGameConstants.SNAKE_SILVER_THRESHOLD) {
                // Silver Medal
                this.snakeMedal = MiniGameMedals.SILVER;
            } else if (value >= MiniGameConstants.SNAKE_BRONZE_THRESHOLD) {
                // Bronze Medal
                this.snakeMedal = MiniGameMedals.BRONZE;
            } else {
                // Failed
                this.snakeMedal = MiniGameMedals.FAIL;
            }
            this.snakeHighScore = value;
        }
    }

    /**
     * Get the flappy bird high score
     * @return the high score for the flappy bird game
     */
    public int getFlappyBirdHighScore() {
        return flappyBirdHighScore;
    }

    /**
     * Get the flappy bird high score medal
     * @return the flappy bird high score medal
     */
    public MiniGameMedals getFlappyBirdMedal() {
        return flappyBirdMedal;

    }

    /**
     * Compares current high score and adjusts medals and high score appropriately
     * @param value the new score to be checked
     */
    public void checkAndSetFlappyBirdScoreMedal(int value) {
        if (value > this.flappyBirdHighScore) {
            if (value >= MiniGameConstants.FLAPPY_BIRD_GOLD_THRESHOLD) {
                // Gold medal
                this.flappyBirdMedal = MiniGameMedals.GOLD;
            } else if (value >= MiniGameConstants.FLAPPY_BIRD_SILVER_THRESHOLD) {
                // Silver Medal
                this.flappyBirdMedal = MiniGameMedals.SILVER;
            } else if (value >= MiniGameConstants.FLAPPY_BIRD_BRONZE_THRESHOLD) {
                // Bronze Medal
                this.flappyBirdMedal = MiniGameMedals.BRONZE;
            } else {
                // Failed
                this.flappyBirdMedal = MiniGameMedals.FAIL;
            }
            this.flappyBirdHighScore = value;
        }
    }

    /**
     * Get the maze high score
     * @return the maze high score
     */
    public int getMazeHighScore() {
        return mazeHighScore;
    }

    /**
     * Get the maze high score medal
     * @return the maze high score medal
     */
    public MiniGameMedals getMazeMedal() {
        return mazeMedal;
    }

    /**
     * Compares current high score and adjusts medals and high score appropriately
     * @param value the new score to be checked
     */
    public void checkAndSetMazeScoreMedal(int value) {
        if (value > this.mazeHighScore) {
            if (value >= MiniGameConstants.MAZE_GOLD_THRESHOLD) {
                // Gold medal
                this.mazeMedal = MiniGameMedals.GOLD;
            } else if (value >= MiniGameConstants.MAZE_SILVER_THRESHOLD) {
                // Silver Medal
                this.mazeMedal = MiniGameMedals.SILVER;
            } else if (value >= MiniGameConstants.MAZE_BRONZE_THRESHOLD) {
                // Bronze Medal
                this.mazeMedal = MiniGameMedals.BRONZE;
            } else {
                // Failed
                this.mazeMedal = MiniGameMedals.FAIL;
            }
            this.mazeHighScore = value;
        }
    }
}
