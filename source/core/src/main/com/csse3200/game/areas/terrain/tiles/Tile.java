package com.csse3200.game.areas.terrain.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.BitSet;
import java.util.List;


/**
 * Tile class to store tile data.
 * A tile has a name, texture, and edge tiles.
 */
public class Tile {
    // data for wave function collapse
    private final TextureRegion texture;
    private final List<String> edgeTiles;
    private final String name;
    private final String centre;

    // all possible tiles
    private BitSet up = new BitSet();
    private BitSet right = new BitSet();
    private BitSet down = new BitSet();
    private BitSet left = new BitSet();

    private boolean isCollapsed = false;

    public Tile(String name, TextureRegion texture, List<String> edgeTiles, String centre) {
        this.name = name;
        this.texture = texture;
        this.edgeTiles = edgeTiles;
        this.centre = centre;
    }

    /**
     * Get the name of the tile.
     *
     * @return The name of the tile
     */
    public String getName() {
        return name;
    }

    /**
     * Get the texture of the tile.
     *
     * @return The texture of the tile
     */
    public TextureRegion getTexture() {
        return texture;
    }

    /**
     * Get the edge tiles of the tile.
     *
     * @return The edge tiles of the tile
     */
    public List<String> getEdgeTiles() {
        return edgeTiles;
    }

    /**
     * Get the centre tile of the tile.
     *
     * @return The centre tile of the tile
     */
    public String getCentre() {
        return centre;
    }

    /**
     * Get the up edge tiles of the tile.
     *
     * @return The up edge tiles of the tile
     */
    public BitSet getUp() {
        return up;
    }

    /**
     * Get the right edge tiles of the tile.
     *
     * @return The right edge tiles of the tile
     */
    public BitSet getRight() {
        return right;
    }

    /**
     * Get the down edge tiles of the tile.
     *
     * @return The down edge tiles of the tile
     */
    public BitSet getDown() {
        return down;
    }

    /**
     * Get the left edge tiles of the tile.
     *
     * @return The left edge tiles of the tile
     */
    public BitSet getLeft() {
        return left;
    }

    /**
     * Set the possible up edge tiles of the tile.
     *
     * @param up All up edge tiles of the tile
     */
    public void setPossibleUp(BitSet up) {
        this.up = up;
    }

    /**
     * Set the possible right edge tiles of the tile.
     *
     * @param right All right edge tiles of the tile
     */
    public void setPossibleRight(BitSet right) {
        this.right = right;
    }

    /**
     * Set the possible down edge tiles of the tile.
     *
     * @param down All down edge tiles of the tile
     */
    public void setPossibleDown(BitSet down) {
        this.down = down;
    }

    /**
     * Set the possible left edge tiles of the tile.
     *
     * @param left All left edge tiles of the tile
     */
    public void setPossibleLeft(BitSet left) {
            this.left = left;
        }

    public boolean isCollapsed() {return isCollapsed;}
    public void setIsCollapsed(boolean isCollapsed) {this.isCollapsed = isCollapsed;}
}
