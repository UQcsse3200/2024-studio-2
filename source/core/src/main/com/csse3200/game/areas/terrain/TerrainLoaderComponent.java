package com.csse3200.game.areas.terrain;

import java.awt.print.PrinterIOException;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

/**
 * Component for dynamically loading terrain chunks based on the player's position.
 * This component should be added to the player entity and will update the surrounding
 * terrain chunks as the player moves.
 */
public class TerrainLoaderComponent extends Component {
  private TerrainComponent terrain;
  private GridPoint2 previousChunk;

  /**
   * Constructs a TerrainLoaderComponent.
   * 
   * @param terrainFactory The factory used to generate or load terrain chunks.
   */
  public TerrainLoaderComponent(TerrainComponent terrain) {
    this.terrain = terrain;
  }

  @Override
  public void create() {
    // Initialize the player's starting chunk position and load the initial chunks.
    previousChunk = getPlayerChunk(entity.getPosition());
    System.out.println(previousChunk);
    loadInitialChunks();
  }

  @Override
  public void update() {
    Vector2 currentPosition = entity.getPosition();
    //System.out.println(currentPosition);
    System.out.println(previousChunk);
    GridPoint2 currentChunk = getPlayerChunk(currentPosition);

    // Only load new chunks if the player has moved to a different chunk.
    if (!currentChunk.equals(previousChunk)) {
      loadChunks(currentPosition);
      previousChunk = currentChunk;
    }
  }

  private void loadInitialChunks() {
    // Load the initial 3x3 grid of chunks around the player's starting position
    loadChunks(entity.getPosition());
  }

  private void loadChunks(Vector2 position) {

    terrain.fillChunk(getPlayerChunk(position));
  
    //TerrainComponent terrainComponent = terrainFactory.createTerrain(TerrainFactory.TerrainType.FOREST_DEMO, new GridPoint2((int) position.x, (int) position.y), new GridPoint2(5, 5));
    //
    //// Create an entity for the terrain and add it to the game world
    //Entity terrainEntity = new Entity().addComponent(terrainComponent);
    //ServiceLocator.getEntityService().register(terrainEntity);
  }


  private GridPoint2 getPlayerChunk(Vector2 position) {
    return new GridPoint2((int) position.x / TerrainFactory.CHUNK_SIZE, (int) position.y / TerrainFactory.CHUNK_SIZE);
  }
}
