package com.csse3200.game.services;

import com.badlogic.gdx.Gdx;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.input.InputService;
import com.csse3200.game.lighting.LightingService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.ui.dialoguebox.DialogueBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.areas.GameArea;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services.
 * Warning: global access is a trap and should be used <i>extremely</i> sparingly.
 * Read the wiki for details (https://github.com/UQcsse3200/game-engine/wiki/Service-Locator).
 */
public class ServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
  private static EntityService entityService;
  private static RenderService renderService;
  private static PhysicsService physicsService;
  private static GameTime timeSource;
  private static InputService inputService;
  private static ResourceService resourceService;
  private static DialogueBoxService dialogueBoxService;
  private static LightingService lightingService;
  // static field for GameArea
  private static GameArea gameArea;
  private static GdxGame game;

  /**
   * Sets the GdxGame for any services that require it (should not be used unless necessary).
   * Errors if the game is set twice
   * @param g the instance of GdxGame that is running
   */
  public static void setGame(GdxGame g) {
    if (game != null) {
      throw new IllegalArgumentException("The GdxGame instance should be set only once!");
    }
    game = g;

    // Provide GdxGame instance to services which require it:
    DialogueBox.setGame(game);
  }

  public static DialogueBoxService getDialogueBoxService() {
    return dialogueBoxService;
  }

  public static EntityService getEntityService() {
    return entityService;
  }

  public static RenderService getRenderService() {
    return renderService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }

  public static GameTime getTimeSource() {
    return timeSource;
  }

  public static InputService getInputService() {
    return inputService;
  }

  public static ResourceService getResourceService() {
    return resourceService;
  }

  public static LightingService getLightingService() {
    return lightingService;
  }

  // Getter for GameArea
  public static GameArea getGameArea() {
    return gameArea;
  }

  // Registration method for GameArea
  public static void registerGameArea(GameArea area) {
    logger.debug("Registering game area {}", area);
    gameArea = area;
  }

  public static void registerEntityService(EntityService service) {
    logger.debug("Registering entity service {}", service);
    entityService = service;
  }

  public static void registerDialogueBoxService(DialogueBoxService service) {
    logger.debug("Registering entity chat service {}", service);
    dialogueBoxService = service;
  }

  public static void registerRenderService(RenderService service) {
    logger.debug("Registering render service {}", service);
    renderService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    logger.debug("Registering physics service {}", service);
    physicsService = service;
  }

  public static void registerTimeSource(GameTime source) {
    logger.debug("Registering time source {}", source);
    timeSource = source;
  }

  public static void registerInputService(InputService source) {
    logger.debug("Registering input service {}", source);
    inputService = source;
    Gdx.input.setInputProcessor(inputService);
  }

  public static void registerResourceService(ResourceService source) {
    logger.debug("Registering resource service {}", source);
    resourceService = source;
  }

  public static void registerLightingService(LightingService source) {
    logger.debug("Registering lighting service {}", source);
    lightingService = source;
  }

  public static void clear() {
    entityService = null;
    renderService = null;
    physicsService = null;
    lightingService = null;
    timeSource = null;
    inputService = null;
    resourceService = null;
    dialogueBoxService = null;
    gameArea = null;
  }

  private ServiceLocator() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
