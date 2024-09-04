package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.ForestGameArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: RENAME THIS TO TERRAIN LOADER!!!

// Handles when setting a new chunk
public class TerrainLoader {
    private static final Logger logger = LoggerFactory.getLogger(TerrainLoader.class);
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
        logger.debug("Setting chunks at ({}, {})", currentChunk.x, currentChunk.y);
        TerrainComponent.loadChunks(posToChunk(position));
    }

    public static GridPoint2 chunktoWorldPos(GridPoint2 pos) {
        return new GridPoint2(pos.x * TerrainFactory.CHUNK_SIZE,pos.y * TerrainFactory.CHUNK_SIZE);
    }

    public static GridPoint2 posToChunk(Vector2 pos) {
        return new GridPoint2((int) pos.x / TerrainFactory.CHUNK_SIZE,(int) pos.y / TerrainFactory.CHUNK_SIZE);
    }

    public TerrainLoader() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate static util class TerrainLoader!");
    }
}
