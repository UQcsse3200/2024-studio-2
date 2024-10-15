package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.csse3200.game.areas.terrain.tiles.Tile;

import java.util.BitSet;

/**
 * The CCell class holds the possible tiles for the cell.
 */
public class CCell extends Cell {
    private BitSet possibleUp;
    private BitSet possibleDown;
    private BitSet possibleLeft;
    private BitSet possibleRight;

    private boolean isCollapsed;

    public CCell() {
        super();
        this.possibleUp = new BitSet();
        this.possibleDown = new BitSet();
        this.possibleLeft = new BitSet();
        this.possibleRight = new BitSet();
        this.isCollapsed = false;
    }

    /**
     * Get the possible tiles that can join with the top of this cell.
     *
     * @return The possible tiles for the cell
     */
    public BitSet getUp() {
        return this.possibleUp;
    }

    /**
     * Get the possible tiles that can join with the bottom of this cell.
     *
     * @return The possible tiles for the cell
     */
    public BitSet getDown() {
        return this.possibleDown;
    }

    /**
     * Get the possible tiles that can join with the left of this cell.
     *
     * @return The possible tiles for the cell
     */
    public BitSet getLeft() {
        return this.possibleLeft;
    }

    /**
     * Get the possible tiles that can join with the right of this cell.
     *
     * @return The possible tiles for the cell
     */
    public BitSet getRight() {
        return this.possibleRight;
    }

    /**
     * Set the tile and also mean the cell is collapsed and confirm all possible
     * tiles.
     *
     * @param tile            The tile to set
     * @param terrainResource The terrain resource to use for setting the tile
     * @return this cell object
     */
    public CCell setTile(Tile tile, TerrainResource terrainResource) {
        super.setTile(new TerrainTile(tile.getTexture()));

        this.possibleUp = tile.getUp();
        this.possibleDown = tile.getDown();
        this.possibleLeft = tile.getLeft();
        this.possibleRight = tile.getRight();

        this.isCollapsed = true;
        return this;
    }

    public boolean getIsCollapsed() {return this.isCollapsed;}
}
