package com.csse3200.game.areas.terrain.tiles;

import java.util.ArrayList;

public class TileConfig {
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
