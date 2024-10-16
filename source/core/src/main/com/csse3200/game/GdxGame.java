package com.csse3200.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.csse3200.game.components.animal.AnimalRouletteDisplay1;
import com.csse3200.game.components.combat.CombatActions;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.Achievements;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.screens.*;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
    private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);

    private boolean enemyWasBeaten = false;

    @Override
    public void create() {
        logger.info("Creating game");
        loadSettings();

        SaveHandler.load(Achievements.class, "saves/achievement", FileLoader.Location.LOCAL);

        if(Achievements.checkState()) {
            Achievements.resetState();
        }

        // Assign the game to a singleton
        ServiceLocator.setGame(this);

        // Sets background to light yellow
        Gdx.gl.glClearColor(248f / 255f, 249 / 255f, 178 / 255f, 1);

        setScreen(ScreenType.MAIN_MENU);
    }

    /**
     * Loads the game's settings.
     */
    private void loadSettings() {
        logger.debug("Loading game settings");
        UserSettings.Settings settings = UserSettings.get();
        UserSettings.applySettings(settings);
    }

    /**
     * Sets the game's screen to a new screen of the provided type.
     *
     * @param screenType screen type
     */
    public void setScreen(ScreenType screenType) {
        logger.info("Setting screen to {}", screenType);
        Screen currentScreen = getScreen();
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        setScreen(newScreen(screenType, null, null, null, null));
    }

    /**
     * Changes to a screen that already exists, disposing of the current screen
     *
     * @param screen to be switched to
     */
    public void setOldScreen(Screen screen, ServiceContainer container) {
        logger.info("Setting old screen: {}", screen);
        Screen currentScreen = getScreen();
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        setScreen(screen);
        ServiceLocator.registerTimeSource(container.getTimeSource());
        ServiceLocator.registerPhysicsService(container.getPhysicsService());
        ServiceLocator.registerInputService(container.getInputService());
        ServiceLocator.registerResourceService(container.getResourceService());
        ServiceLocator.registerEntityService(container.getEntityService());
        ServiceLocator.registerRenderService(container.getRenderService());
        ServiceLocator.registerDialogueBoxService(container.getDialogueBoxService());
        ServiceLocator.registerLightingService(container.getLightingService());
        ServiceLocator.registerParticleService(container.getParticleService());
        screen.resume();
    }

    public void addCombatScreen(Entity enemy) {
        addScreen(ScreenType.COMBAT, getScreen(), null, enemy);
    }

    public void addBossCutsceneScreen(Entity player, Entity enemy) {
        addScreen(ScreenType.BOSS_CUTSCENE, getScreen(), player, enemy);
    }
    public void addEnemyCutsceneScreen(Entity player, Entity enemy) {
        addScreen(ScreenType.ENEMY_CUTSCENE, getScreen(), player, enemy);
    }

    public void enterCombatScreen(Entity player, Entity enemy) {
        addScreen(ScreenType.COMBAT, getScreen(), player, enemy);
    }

    public void enterSnakeScreen() {
        addScreen(ScreenType.SNAKE_MINI_GAME, getScreen(), null, null);
    }

    public void enterBirdieDashScreen() {
        addScreen(ScreenType.BIRD_MINI_GAME, getScreen(), null, null);
    }

    public void enterMazeGameScreen() {
        addScreen(ScreenType.MAZE_MINI_GAME, getScreen(), null, null);
    }

    /**
     * Makes a new snake screen (make to reduce circular dependencies)
     * @param oldScreen the screen the game came from (mini-game menu or main game)
     * @param oldScreenServices the screen services of the screen the game came from (mini-game menu or main game)
     */
    public void newSnakeScreen(Screen oldScreen, ServiceContainer oldScreenServices){
        this.setScreen(new SnakeScreen(this, oldScreen, oldScreenServices));
    }

    /**
     * Makes a new bird screen (make to reduce circular dependencies)
     * @param oldScreen the screen the game came from (mini-game menu or main game)
     * @param oldScreenServices the screen services of the screen the game came from (mini-game menu or main game)
     */
    public void newBirdScreen(Screen oldScreen, ServiceContainer oldScreenServices){
        this.setScreen(new BirdieDashScreen(this, oldScreen, oldScreenServices));
    }

    /**
     * Makes a new maze screen (make to reduce circular dependencies)
     * @param oldScreen the screen the game came from (mini-game menu or main game)
     * @param oldScreenServices the screen services of the screen the game came from (mini-game menu or main game)
     */
    public void newMazeScreen(Screen oldScreen, ServiceContainer oldScreenServices){
        this.setScreen(new MazeGameScreen(this, oldScreen, oldScreenServices));
    }

    /**
     * Overloaded to add new combat screen
     * Changes to a new screen, does NOT dispose of old screen
     *
     * @param screenType screen type
     * @param screen     Old screen if we want to remember/ return to it.
     */
    public void addScreen(ScreenType screenType, Screen screen, Entity player, Entity enemy) {
        logger.info("Add combat Screen: {}", screenType);
        screen.pause();
        ServiceContainer container = new ServiceContainer();

        ServiceLocator.clear();
        setScreen(newScreen(screenType, screen, container, player, enemy));
    }

    public void returnFromCombat (Screen screen, ServiceContainer container, Entity enemy) {
        setOldScreen(screen, container);
        if (enemyWasBeaten) {
            ((MainGameScreen) screen).getGameArea().spawnConvertedNPCs(enemy);
            enemyWasBeaten = false;
        }
        List<Entity> enemies = ((MainGameScreen) screen).getGameArea().getEnemies();
        for (Entity e : enemies) {
            if (e.equals(enemy)) {
                enemies.remove(e);
                break;
            }
        }
        AnimationRenderComponent animationRenderComponent = enemy.getComponent(AnimationRenderComponent.class);
        animationRenderComponent.stopAnimation();
        enemy.specialDispose();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing of current screen");
        getScreen().dispose();
    }

    /**
     * Create a new screen of the provided type.
     *
     * @param screenType screen type
     * @param screen     for returning to an old screen, may be null.
     * @param container  container for services, for returning to an old screen. may be null.
     * @param player     player entity to be passed into the new screen (null if not needed).
     * @param enemy      enemy entity to be passed into the new screen (null if not needed).
     * @return new screen
     */
    private Screen newScreen(ScreenType screenType, Screen screen, ServiceContainer container, Entity player, Entity enemy) {
        return switch (screenType) {
            case MAIN_MENU -> new MainMenuScreen(this);
            case MAIN_GAME -> new MainGameScreen(this);
            case COMBAT -> new CombatScreen(this, screen, container, player, enemy);
            case BOSS_CUTSCENE -> new BossCutsceneScreen(this, screen, container, player, enemy);
            case ENEMY_CUTSCENE -> new EnemyCutsceneScreen(this, screen, container, player, enemy);
            case ACHIEVEMENTS -> new AchievementsScreen(this);
            case MINI_GAME_MENU_SCREEN -> new MiniGameMenuScreen(this);
            case SNAKE_MINI_GAME -> new SnakeScreen(this, screen, container);
            case BIRD_MINI_GAME -> new BirdieDashScreen(this, screen, container);
            case MAZE_MINI_GAME -> new MazeGameScreen(this, screen, container);
            case LOADING_SCREEN -> new LoadingScreen(this);
            case ANIMAL_ROULETTE -> new AnimalRouletteScreen1(this);
            case END_GAME_STATS -> new EndGameStatsScreen(this);
            case GAME_OVER_LOSE -> new GameOverLoseScreen(this);
            case STORY -> {
                String selectedAnimal = "dog";
                yield new StoryScreen(this, selectedAnimal);
            }
            case CUTSCENE -> (new CutSceneScreen(this));
            case QUICK_TIME_EVENT -> new QuickTimeEventScreen(this);
            default -> null;
        };
    }

    /**
     * types of screens
     */
    public enum ScreenType {
        MAIN_MENU, MAIN_GAME, SETTINGS, MINI_GAME_MENU_SCREEN, LOADING_SCREEN,ANIMAL_ROULETTE,
        ACHIEVEMENTS, COMBAT, BOSS_CUTSCENE, ENEMY_CUTSCENE, GAME_OVER_LOSE, SNAKE_MINI_GAME,
        BIRD_MINI_GAME, MAZE_MINI_GAME, QUICK_TIME_EVENT, END_GAME_STATS, CUTSCENE, STORY
    }

    /**
     * Exit the game.
     */
    public void exit() {
        app.exit();
    }

    public void setEnemyWasBeaten(boolean value) {
        this.enemyWasBeaten = value;
    }
}