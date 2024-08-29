package com.csse3200.game.entities;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 *
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class EntityService {
  private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
  private static final int INITIAL_CAPACITY = 16;

  private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);

  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   * @param entity new entity.
   */
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    entities.add(entity);
    entity.create();
  }

  /**
   * Unregister an entity with the entity service. The entity will be removed and stop updating.
   * @param entity entity to be removed.
   */
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    entities.removeValue(entity, true);
  }

  /**
   * Update all registered entities. Should only be called from the main game loop.
   */
  public void update() {
    for (Entity entity : entities) {
      entity.earlyUpdate();
      entity.update();
    }
  }

  /**
   * Dispose all entities.
   */
  public void dispose() {
    for (Entity entity : entities) {
      entity.dispose();
    }
  }

  /**
   * Pause (disable) all entities so update in the gameplay loop doesn't occur for
   * the event and its components.
   */
  public void restWholeScreen() {
    for (Entity entity : entities) {
      entity.setEnabled(false);
    }
  }

  /**
   * Play (enable) all entities so update in the gameplay loop occurs for
   * the event and its components.
   */
  public void wakeWholeScreen() {
    for (Entity entity : entities) {
      entity.setEnabled(true);
    }
  }


  /**
   * Get the oldest (lowest index entity in entities) entity that contains the given component.
   * This is used to search for very specific entities. e.g. Player
   *
   * @param componentType The component class, e.g. RenderComponent.class
   * @param <T> The component type, e.g. RenderComponent
   * @return The entity holding the component or null if nonexistent.
   */

  public <T extends Component> Entity getEntity(Class<T> componentType) {
    for (Entity entity : entities) {
      if (entity.getComponent(componentType) != null) {
        return entity;
      }
    }
    return null;
  }

  /**
   * Get the oldest (lowest index entity in entities) entity's component of a given component type.
   * This is used to search for very specific components. e.g. QuestManager
   *
   * @param componentType The component class, e.g. RenderComponent.class
   * @param <T> The component type, e.g. RenderComponent
   * @return The oldest entity's component or null if nonexistent.
   */
  public <T extends Component> Component getSpecificComponent(Class<T> componentType) {
    for (Entity entity : entities) {
      Component component = entity.getComponent(componentType);
      if (component != null) {
        return component;
      }
    }
    return null;
  }
}
