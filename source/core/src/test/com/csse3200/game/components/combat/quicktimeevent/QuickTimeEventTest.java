package com.csse3200.game.components.combat.quicktimeevent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class QuickTimeEventTest {
    Entity ui;
    QuickTimeEventDisplay display;
    QuickTimeEventActions actions;
    Stage stage;

    @Mock GdxGame game;
    @Mock GameTime gameTime;
    @Mock Viewport viewport;
    @Mock SpriteBatch spriteBatch;

    @BeforeEach
    void beforeEach() {
        // set up stage mock
        stage = new Stage(viewport, spriteBatch);

        // register services
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        RenderService renderer = new RenderService();
        renderer.setStage(stage);
        ServiceLocator.registerRenderService(renderer);

        // create ui
        ui = new Entity();
        display = new QuickTimeEventDisplay();
        actions = new QuickTimeEventActions(game);
        ui.addComponent(display).addComponent(actions);
        ServiceLocator.getEntityService().register(ui);

    }

    /**
     * This test is used to determine if the exit button
     * returns to the main menu
     */
    @Test
    void exitButtonShouldReturnToMenu() {
        ui.getEvents().trigger("exit");
        ui.update();
        Mockito.verify(game).setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    /**
     * This test is used to show that the start button both
     * starts and decrements the counter
     */
    @Test
    void startButtonShouldDecCounter() {
        when(gameTime.getTime()).thenReturn(0L);
        ui.getEvents().trigger("start");
        ui.update();
        // label is no longer empty
        assertNotEquals(display.getLabel().getText().toString(), "");
        int startCount = parseLabelTextToInt();
        // make 1 second pass
        when(gameTime.getTimeSince(0L)).thenReturn(1000L);
        ui.update();
        // counter has decremented by 1
        int nextCount = parseLabelTextToInt();
        assertNotEquals(startCount, nextCount);
        assertEquals(startCount, nextCount + 1);
    }

    /**
     * Parse label text to int
     *
     * @return the label text parsed as an integer
     */
    private int parseLabelTextToInt() {
        return Integer.parseInt(display.getLabel().getText().toString());
    }

}
