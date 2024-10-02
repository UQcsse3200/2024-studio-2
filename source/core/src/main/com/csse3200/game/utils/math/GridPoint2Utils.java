package com.csse3200.game.utils.math;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Contains additional utility constants and functions for common GridPoint2 operations.
 */
public class GridPoint2Utils {
  public static final GridPoint2 ZERO = new GridPoint2(0, 0);
  public static final GridPoint2 UP = new GridPoint2(0, 1);
  public static final GridPoint2 DOWN = new GridPoint2(0, -1);
  public static final GridPoint2 LEFT = new GridPoint2(-1, 0);
  public static final GridPoint2 RIGHT = new GridPoint2(1, 0);
  public static final GridPoint2[] GRID_DIRECTIONS = {UP, DOWN, LEFT, RIGHT};

  private GridPoint2Utils() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
