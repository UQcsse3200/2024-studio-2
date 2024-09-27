package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Game;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.gamestate.data.PlayerSave;
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

  private static boolean loaded = false;

  @Override
  public void create() {
    entity.getEvents().addListener("start", this::onStart);
    entity.getEvents().addListener("load", this::onLoad);
    entity.getEvents().addListener("combat", this::onCombat);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("achievements", this::onAchievements);
    entity.getEvents().addListener("stats", this::onStats);
    entity.getEvents().addListener("SnakeGame", this::onSnakeMiniGame);
  }

  /**
   * Swaps to the Main Game screen.
   */
  private void onStart() {
    logger.info("Start game");

    GameState.resetState();

    game.setScreen(GdxGame.ScreenType.ANIMAL_ROULETTE);
  }

  /**
   * Intended for loading a saved game state.
   * Load functionality is not actually implemented.
   */
  private void onLoad() {
    logger.info("Load game");

    SaveHandler.load(GameState.class, "saves");
//    if(GameState.player == null) {
//      GameState.player = new PlayerSave();
//    }
    if(GameState.checkState()) {
      GameState.resetState();
    }
    if(GameState.player.selectedAnimalPath == null) {
      game.setScreen(GdxGame.ScreenType.ANIMAL_ROULETTE);
    } else {
      game.setScreen(GdxGame.ScreenType.LOADING_SCREEN);
    }
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

  private void onAchievements() {
    logger.info("Launching achievements screen");
    game.setScreen(GdxGame.ScreenType.ACHIEVEMENTS);
  }

  /**
   * Shows the end game stats screen.
   */
  private void onStats() {
    logger.info("Launching achievements screen");
    game.setScreen(GdxGame.ScreenType.END_GAME_STATS);
  }

  private void onSnakeMiniGame() {
    logger.info("Launching settings screen");
    game.setScreen(GdxGame.ScreenType.MINI_GAME_MENU_SCREEN);
  }

  /**
   * Returns whether the current game instance loaded a save or not.
   * @return whether the game loaded a save.
   */
  public static boolean getGameLoaded() {
    return loaded;
  }

}
