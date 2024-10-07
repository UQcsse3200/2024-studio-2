package com.csse3200.game.areas.terrain.tiles;

import java.util.ArrayList;
import java.util.Arrays;

public class ForestTileConfig {
  private static final String GRASS = "grass";
  private static final String SAND = "sand";

  private ForestTileConfig() {
    throw new IllegalArgumentException("Do not instantiate static util class");
  }

  public static class TileConfig {
    public String id;
    public String fp;
    public ArrayList<String> edges;
    public String centre;

    public TileConfig(String id, String fp, ArrayList<String> edges, String centre) {
      this.id = id;
      this.fp = fp;
      this.edges = edges;
      this.centre = centre;
    }
  }

  public static final TileConfig[] forestMapTiles = {
      new TileConfig(
          "grass2TL",
          "images/grass_tile_2_around_sand/upper_left_corner_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("AAA", "ABB", "ABB", "AAA")),
              GRASS),
      new TileConfig(
          "grass2TM",
          "images/grass_tile_2_around_sand/upper_middle_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("AAA", "ABB", "BBB", "ABB")),
              GRASS),
      new TileConfig(
          "grass2TR", 
          "images/grass_tile_2_around_sand/upper_right_corner_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("AAA", "AAA", "BBA", "ABB")),
              GRASS),
      new TileConfig("grass2ML",
          "images/grass_tile_2_around_sand/left_middle_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("ABB", "BBB", "ABB", "AAA")),
              GRASS),
      new TileConfig("grass2MM",
          "images/grass_tile_2_around_sand/middle_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("BBB", "BBB", "BBB", "BBB")),
              GRASS),
      new TileConfig("grass2MR",
          "images/grass_tile_2_around_sand/right_middle_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("BBA", "AAA", "BBA", "BBB")),
              GRASS),
      new TileConfig("grass2BL",
          "images/grass_tile_2_around_sand/lower_left_corner_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("ABB", "BBA", "AAA", "AAA")),
              GRASS),
      new TileConfig("grass2BM",
          "images/grass_tile_2_around_sand/lower_middle_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("BBB", "BBA", "AAA", "BBA")),
              GRASS),
      new TileConfig("grass2BR",
          "images/grass_tile_2_around_sand/lower_right_corner_grass_2_around_sand.jpg",
          new ArrayList<>(Arrays.asList("BBA", "AAA", "AAA", "BBA")),
              GRASS),
      new TileConfig("grass1TL",
          "images/grass_tile_1_around_sand/upper_left_corner_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("AAA", "AAB", "AAB", "AAA")),
              SAND),
      new TileConfig("grass1TM",
          "images/grass_tile_1_around_sand/upper_middle_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("AAA", "AAB", "BBB", "AAB")),
              SAND),
      new TileConfig("grass1TR",
          "images/grass_tile_1_around_sand/upper_right_corner_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("AAA", "AAA", "BAA", "AAB")),
              SAND),
      new TileConfig("grass1ML",
          "images/grass_tile_1_around_sand/left_middle_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("AAB", "BBB", "AAB", "AAA")),
              SAND),
      new TileConfig("grass1MM",
          "images/grass_tile_1_around_sand/middle_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("BBB", "BBB", "BBB", "BBB")),
            GRASS),
      new TileConfig("grass1MR",
          "images/grass_tile_1_around_sand/right_middle_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("BAA", "AAA", "BAA", "BBB")),
              SAND),
      new TileConfig("grass1BL",
          "images/grass_tile_1_around_sand/lower_left_corner_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("AAB", "BAA", "AAA", "AAA")),
              SAND),
      new TileConfig("grass1BM",
          "images/grass_tile_1_around_sand/lower_middle_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("BBB", "BAA", "AAA", "BAA")),
              SAND),
      new TileConfig("grass1BR",
          "images/grass_tile_1_around_sand/lower_right_corner_grass_1_around_sand.jpg",
          new ArrayList<>(Arrays.asList("BAA", "AAA", "AAA", "BAA")),
              SAND),
      new TileConfig("sand1MM",
          "images/sand_tile_1_around_grass/middle_sand_1_around_grass.jpg",
          new ArrayList<>(Arrays.asList("AAA", "AAA", "AAA", "AAA")),
              SAND),
      new TileConfig("gwsc",
          "images/tiles_with_corners/Grass_tile_with_sand_corner.png",
          new ArrayList<>(Arrays.asList("BBA", "ABB", "ABB", "BBA")),
            GRASS),
      new TileConfig("swgc",
          "images/tiles_with_corners/Sand_tile_with_grass_corner.png",
          new ArrayList<>(Arrays.asList("AAB", "BAA", "BAA", "AAB")),
              SAND)
  };
}

