package com.csse3200.game.components.mainmenu;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
  private GdxGame game;

  public MainMenuActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("load", this::onLoad);
    entity.getEvents().addListener("combatPop", this::onCombatpop);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("combat", this::onCombat);
    entity.getEvents().addListener("settings", this::onSettings);
    entity.getEvents().addListener("achievements", this::onAchievements);
    entity.getEvents().addListener("SnakeGame", this::onSnakeMiniGame);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");
    game.setScreen(GdxGame.ScreenType.ANIMAL_SELECTION);
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onLoad() {
    logger.info("Load game");
    SaveHandler.load(GameState.class, "saves");
    game.setScreen(GdxGame.ScreenType.ANIMAL_SELECTION);
  }

  /**
   * Opens a new combat screen.
   * The combat screen will not actually be called from the main menu screen.
   * This is for testing purposes only.
   */
  private void onCombat() {
    logger.info("Start combat");
    game.setScreen(GdxGame.ScreenType.COMBAT);
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    game.exit();
  }

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    game.setScreen(GdxGame.ScreenType.SETTINGS);
  }

  private void onCombatpop() {
    logger.info("Opening combat popup");
    game.setScreen(GdxGame.ScreenType.COMBAT_POPUP);
  }

  private void onAchievements() {
    logger.info("Launching achievements screen");
    game.setScreen(GdxGame.ScreenType.ACHIEVEMENTS);
  }

  private void onSnakeMiniGame() {
    logger.info("Launching settings screen");
    game.setScreen(GdxGame.ScreenType.MINI_GAME_MENU_SCREEN);
  }


}
