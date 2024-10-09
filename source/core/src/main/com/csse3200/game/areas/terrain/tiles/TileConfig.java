package com.csse3200.game.areas.terrain.tiles;

import java.util.List;

public class TileConfig {
    public String id;
    public String fp;
    public List<String> edges;
    public String centre;

    public TileConfig(String id, String fp, List<String> edges, String centre) {
        this.id = id;
        this.fp = fp;
        this.edges = edges;
        this.centre = centre;
    }
}
