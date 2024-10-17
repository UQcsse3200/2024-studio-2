package com.csse3200.game.components.animal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.CustomButton;
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
        CustomButton leftButton = display.getLeftButton();
        // Create a touchDown event to simulate pressing the button
        InputEvent touchDownEvent = new InputEvent();
        touchDownEvent.setType(InputEvent.Type.touchDown);

        // Create a touchUp event to simulate releasing the button
        InputEvent touchUpEvent = new InputEvent();
        touchUpEvent.setType(InputEvent.Type.touchUp);

        // Fire the touchUp event
        leftButton.getButton().fire(touchDownEvent);
        leftButton.getButton().fire(touchUpEvent);
        assertEquals( "images/bird.png", display.getSelectedAnimal());
        assertEquals(2, display.getCurrentAnimalIndex());

        // Press button again (should now be croc)
        leftButton.getButton().fire(touchDownEvent);
        leftButton.getButton().fire(touchUpEvent);
        assertEquals("images/croc.png", display.getSelectedAnimal());
        assertEquals(1, display.getCurrentAnimalIndex());

        // Press button again (should now be dog again)
        leftButton.getButton().fire(touchDownEvent);
        leftButton.getButton().fire(touchUpEvent);
        assertEquals("images/dog.png", display.getSelectedAnimal());
        assertEquals(0, display.getCurrentAnimalIndex());
    }

    /**
     * This test is used to show that animals are cycle through
     * clockwise when right button is pressed
     */
    @Test
    void rightButtonShouldCycleCW() {
        CustomButton rightButton = display.getRightButton();

        // Create a touchDown event to simulate pressing the button
        InputEvent touchDownEvent = new InputEvent();
        touchDownEvent.setType(InputEvent.Type.touchDown);

        // Create a touchUp event to simulate releasing the button
        InputEvent touchUpEvent = new InputEvent();
        touchUpEvent.setType(InputEvent.Type.touchUp);

        // Press button (should now be croc)
        rightButton.getButton().fire(touchDownEvent);
        rightButton.getButton().fire(touchUpEvent);
        assertEquals("images/croc.png", display.getSelectedAnimal());
        assertEquals(1, display.getCurrentAnimalIndex());

        // Press button again (should now be bird)
        rightButton.getButton().fire(touchDownEvent);
        rightButton.getButton().fire(touchUpEvent);
        assertEquals("images/bird.png", display.getSelectedAnimal());
        assertEquals(2, display.getCurrentAnimalIndex());

        // Press button again (should now be dog again)
        rightButton.getButton().fire(touchDownEvent);
        rightButton.getButton().fire(touchUpEvent);
        assertEquals("images/dog.png", display.getSelectedAnimal());
        assertEquals(0, display.getCurrentAnimalIndex());
    }
}
