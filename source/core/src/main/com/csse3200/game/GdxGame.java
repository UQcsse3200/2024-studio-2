package com.csse3200.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.csse3200.game.components.settingsmenu.UserSettings;
import com.csse3200.game.entities.Entity;
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

    @Override
    public void create() {
        logger.info("Creating game");
        loadSettings();
        // Assign the gdxgame to a singleton
        GdxGameManager.setInstance(this);

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
        List<Entity> enemies = ((MainGameScreen) screen).getGameArea().getEnemies();
        for (Entity e : enemies) {
            if (e.equals(enemy)) {
                enemies.remove(e);
                break;
            }
        }
        AnimationRenderComponent animationRenderComponent = enemy.getComponent(AnimationRenderComponent.class);
        animationRenderComponent.stopAnimation();
        enemy.dispose();
    }

    @Override
    public void dispose() {
        logger.debug("Disposing of current screen");
        getScreen().dispose();
    }

    private Screen newScreen(ScreenType screenType, Screen screen, ServiceContainer container) {
        return newScreen(screenType, screen, container, null, null);
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
        switch (screenType) {
            case MAIN_MENU:
                return new MainMenuScreen(this);
            case MAIN_GAME:
                return new MainGameScreen(this);
            case COMBAT:
                return new CombatScreen(this, screen, container, player, enemy);
            case BOSS_CUTSCENE:
                return new BossCutsceneScreen(this, screen, container, player, enemy);
            case ENEMY_CUTSCENE:
                return new EnemyCutsceneScreen(this, screen, container, player, enemy);
            case ACHIEVEMENTS:
                return new AchievementsScreen(this);
            case MINI_GAME_MENU_SCREEN:
                return new MiniGameMenuScreen(this);
            case SNAKE_MINI_GAME:
                return new SnakeScreen(this, screen, container);
            case BIRD_MINI_GAME:
                return new BirdieDashScreen(this, screen, container);
            case MAZE_MINI_GAME:
                return new MazeGameScreen(this, screen, container);
            case LOADING_SCREEN:
                return new LoadingScreen(this);
            case ANIMAL_SELECTION:
                return new LandAnimalSelectionScreen(this);
            case END_GAME_STATS:
                return new EndGameStatsScreen(this);
            case GAME_OVER_LOSE:
                return new GameOverLoseScreen(this);
            case STORY:
                String selectedAnimal = "dog";  // Replace with actual logic for getting selected animal
                return new StoryScreen(this, selectedAnimal);

            case QUICK_TIME_EVENT:
                return new QuickTimeEventScreen(this);
            default:
                return null;
        }
    }

    /**
     * types of screens
     */
    public enum ScreenType {
        MAIN_MENU, MAIN_GAME, SETTINGS, MINI_GAME_MENU_SCREEN, LOADING_SCREEN, ANIMAL_SELECTION,
        ACHIEVEMENTS, COMBAT, BOSS_CUTSCENE, ENEMY_CUTSCENE, GAME_OVER_LOSE, SNAKE_MINI_GAME,
        BIRD_MINI_GAME, MAZE_MINI_GAME, QUICK_TIME_EVENT, END_GAME_STATS, STORY
    }

    /**
     * Exit the game.
     */
    public void exit() {
        app.exit();
    }
}

