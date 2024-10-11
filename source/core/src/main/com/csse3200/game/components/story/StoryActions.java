package com.csse3200.game.components.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and handles transitions between story screens.
 */
public class StoryActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(StoryActions.class);
    private final GdxGame game;
    private final Texture[] backgroundTextures;
    private final int finalScreen;
    private final String storyName;

    public StoryActions(GdxGame game, Texture[] backgroundTextures, String storyName) {
        this.game = game;
        this.backgroundTextures = backgroundTextures;
        finalScreen = backgroundTextures.length - 1;  // The last screen for the story
        this.storyName = storyName;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("next", this::onNext);
        entity.getEvents().addListener("back", this::onBack);
    }

    /**
     * Moves to the next story screen. If the current screen is the final screen, goes to the loading screen.
     *
     * @param screenNum The current screen number.
     */
    private void onNext(int screenNum) {
        if (screenNum >= finalScreen) {
            // If we are on the last screen, move to the loading screen or whatever comes next
            game.setScreen(GdxGame.ScreenType.LOADING_SCREEN);
            return;
        }

        // Move to the next screen
        screenNum += 1;
        logger.debug("Moving to next story screen: {}", screenNum);

        showStoryScreen(screenNum);
    }

    /**
     * Goes back to the previous story screen.
     *
     * @param screenNum The current screen number.
     */
    private void onBack(int screenNum) {
        if (screenNum <= 0) {
            // If we are at the first screen, don't go back further
            logger.debug("Already on the first story screen. Can't go back further.");
            return;
        }

        // Move to the previous screen
        screenNum -= 1;
        logger.debug("Moving to previous story screen: {}", screenNum);

        showStoryScreen(screenNum);
    }

    /**
     * Helper method to create and show the story screen with the given screen number.
     *
     * @param screenNum The screen number to display.
     */
    private void showStoryScreen(int screenNum) {
        logger.debug("Creating new UI for story screen: {}", screenNum);

        // Create the next story screen entity and display it
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new StoryDisplay(backgroundTextures, screenNum, storyName))  // Display the new screen
                .addComponent(new InputDecorator(stage, 10))  // Handle input for the new screen
                .addComponent(new StoryActions(game, backgroundTextures, storyName));  // Set up story actions
        ServiceLocator.getEntityService().register(ui);
    }
}
