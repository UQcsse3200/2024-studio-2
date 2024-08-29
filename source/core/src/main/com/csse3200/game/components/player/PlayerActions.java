package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.overlays.Overlay.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;
  EventService eventService = ServiceLocator.getEventService();
  private static final Logger logger = LoggerFactory.getLogger(PlayerActions.class);
  private final Entity player;

  private final GdxGame game;

  public PlayerActions(GdxGame game, Entity player) {
    this.game = game;
    this.player = player;
  }

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("restMenu", this::restMenu);
    entity.getEvents().addListener("quest", this::quest);
    entity.getEvents().addListener("startCombat", this::startCombat);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;
    eventService.getGlobalEventHandler().trigger("Test Achievement");
    player.getEvents().trigger("steps");
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
    player.getEvents().trigger("attackTask");
  }

  private void restMenu() {
      logger.info("Sending Pause");
    eventService.getGlobalEventHandler().trigger("addOverlay", OverlayType.PAUSE_OVERLAY);
  }

  private void quest() {
    logger.debug("Triggering addOverlay for QuestOverlay");
    eventService.getGlobalEventHandler().trigger("addOverlay", OverlayType.QUEST_OVERLAY);
  }

  public void startCombat(Entity enemy){
    game.addCombatScreen(enemy);
  }
}
