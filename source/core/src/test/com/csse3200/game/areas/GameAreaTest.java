package com.csse3200.game.areas;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
class GameAreaTest {
  @Test
  void shouldSpawnEntities() {
    TerrainFactory factory = mock(TerrainFactory.class);

    GameArea gameArea =
        new GameArea() {
          @Override
          public void create() {}

          @Override
          public Entity getPlayer() {
            return null;
          }

          @Override
          public void unloadAssets() {
          }

          @Override
          public void pauseMusic() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'pauseMusic'");
          }

          @Override
          public void playMusic() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'playMusic'");
          }

          @Override
          public List<Entity> getEnemies() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getEnemies'");
          }
        };

    ServiceLocator.registerEntityService(new EntityService());
    Entity entity = mock(Entity.class);

    gameArea.spawnEntity(entity);
    verify(entity).create();

    gameArea.dispose();
    verify(entity).dispose();
  }
}
