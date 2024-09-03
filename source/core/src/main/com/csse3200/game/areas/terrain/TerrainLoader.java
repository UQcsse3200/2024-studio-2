package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

// TODO: RENAME THIS TO TERRAIN LOADER!!!

// Handles when setting a new chunk
public class TerrainLoader {
    public static GridPoint2 currentChunk;

    public static boolean movedChunk(Vector2 position) {
        GridPoint2 chunk = posToChunk(position);
        if (!chunk.equals(currentChunk)) {
            TerrainComponent.loadChunks(chunk);
            currentChunk = chunk;
            return true;
        }
        return false;
    }

    // ONLY INTENDED TO BE SET AT START OF GAME OR DURING MAP SWITCHES ETC.
    public static void setChunk(Vector2 position) {
        currentChunk = posToChunk(position);
        TerrainComponent.loadChunks(posToChunk(position));
    }

    private static GridPoint2 posToChunk(Vector2 pos) {
        return new GridPoint2((int) pos.x / TerrainFactory.CHUNK_SIZE,(int) pos.y / TerrainFactory.CHUNK_SIZE);
    }

    public TerrainLoader() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate static util class TerrainLoader!");
    }
}
