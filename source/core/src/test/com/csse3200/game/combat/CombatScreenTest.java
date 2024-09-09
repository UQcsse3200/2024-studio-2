package com.csse3200.game.combat;

import com.badlogic.gdx.Screen;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EnemyFactory;
import com.csse3200.game.screens.CombatScreen;
import com.csse3200.game.screens.MainMenuScreen;
import com.csse3200.game.services.ServiceContainer;
import org.junit.jupiter.api.BeforeEach;

/**
 * Test to make sure combat screen switches back to main screen after exiting.
 */
public class CombatScreenTest {
    private GdxGame game;
    private CombatScreen combatScreen;
    private Screen oldScreen;
    private ServiceContainer container;
    private Entity player;
    private Entity enemy;

    @BeforeEach
    void setUp() {
        // Initialise temporary game, screens, and entities.
        this.game = new GdxGame();
        this.oldScreen = new MainMenuScreen(game);
        this.container = new ServiceContainer();
        //this.player = PlayerFactory.createPlayer(game);
        this.enemy = EnemyFactory.createKangaBossEntity(player);
        this.combatScreen = new CombatScreen(game, oldScreen, container, player, enemy);
    }

    /*
    @Test
    void testCorrectInitialise() {
    }

    @Test
    void testCombatExitToMainScreen() {
    }

    @Test
    void enemyIsDisposedOnWin() {
    }
    */
}
