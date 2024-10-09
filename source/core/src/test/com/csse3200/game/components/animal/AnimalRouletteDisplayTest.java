package com.csse3200.game.components.animal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class AnimalRouletteDisplayTest {
    Stage stage;
    Entity ui;

    @Mock GdxGame game;
    @Mock Viewport viewport;
    @Mock SpriteBatch spriteBatch;
    @Spy AnimalRouletteDisplay display;

    @BeforeEach
    void beforeEach() {
        // set up stage mock
        stage = new Stage(viewport, spriteBatch);

        // register services
        ServiceLocator.registerEntityService(new EntityService());
        RenderService renderer = new RenderService();
        renderer.setStage(stage);
        ServiceLocator.registerRenderService(renderer);

        // create ui
        ui = new Entity();
        display = Mockito.spy(new AnimalRouletteDisplay());
        ui.addComponent(display);
        ServiceLocator.getEntityService().register(ui);

    }

    /**
     * This test is used to show that the dog is the default
     * animal displayed
     */
    @Test
    void defaultAnimalShouldBeDog() {
        // Default animal should be dog
        assertEquals("images/dog.png", display.getSelectedAnimal());
        assertEquals( 0, display.getCurrentAnimalIndex());
    }

    /**
     * This test is used to show that animals are cycle through
     * anti-clockwise when left button is pressed
     */
    @Test
    void leftButtonShouldCycleACW() {
        TextButton leftButton = display.getLeftButton();

        // Set up button touch event
        InputEvent touchEvent = new InputEvent();
        touchEvent.setType(InputEvent.Type.touchDown);

        // Press button (should now be bird)
        leftButton.fire(touchEvent);
        assertEquals( "images/bird.png", display.getSelectedAnimal());
        assertEquals(display.getCurrentAnimalIndex(), 2);

        // Press button again (should now be croc)
        leftButton.fire(touchEvent);
        assertEquals(display.getSelectedAnimal(), "images/croc.png");
        assertEquals(display.getCurrentAnimalIndex(), 1);

        // Press button again (should now be dog again)
        leftButton.fire(touchEvent);
        assertEquals(display.getSelectedAnimal(), "images/dog.png");
        assertEquals(display.getCurrentAnimalIndex(), 0);
    }

    /**
     * This test is used to show that animals are cycle through
     * clockwise when right button is pressed
     */
    @Test
    void rightButtonShouldCycleCW() {
        TextButton rightButton = display.getRightButton();

        // Set up button touch event
        InputEvent touchEvent = new InputEvent();
        touchEvent.setType(InputEvent.Type.touchDown);

        // Press button (should now be croc)
        rightButton.fire(touchEvent);
        assertEquals(display.getSelectedAnimal(), "images/croc.png");
        assertEquals(display.getCurrentAnimalIndex(), 1);

        // Press button again (should now be bird)
        rightButton.fire(touchEvent);
        assertEquals(display.getSelectedAnimal(), "images/bird.png");
        assertEquals(display.getCurrentAnimalIndex(), 2);

        // Press button again (should now be dog again)
        rightButton.fire(touchEvent);
        assertEquals(display.getSelectedAnimal(), "images/dog.png");
        assertEquals(display.getCurrentAnimalIndex(), 0);
    }
}
