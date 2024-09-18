package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.story.StoryActions;
import com.csse3200.game.components.story.StoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen for the introduction story. Calls StoryDisplay using createUI function.
 * This exists between the AnimalSelectionScreen and LoadingScreen.
 */
public class StoryScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(StoryScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    private Texture[] backgroundTextures;

    // Different story backgrounds for each animal
    private static final String[] DOG_STORY_PATHS = {
            "images/Story/DogStory1.png", "images/Story/DogStory2.png",
            "images/Story/DogStory3.png", "images/Story/DogStory4.png",
            "images/Story/DogStory5.png", "images/Story/DogStory6.png"
    };
    private static final String[] CROC_STORY_PATHS = {
            "images/croc.png", "images/croc.png",
    };
    private static final String[] BIRD_STORY_PATHS = {
            "images/bird.png", "images/bird.png",
    };



    public StoryScreen(GdxGame game, String selectedAnimal) {
        this.game = game;

        logger.debug("Initialising story screen services");
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        renderer = RenderFactory.createRenderer();
        // Load background textures based on selected animal
        backgroundTextures = loadStoryTextures(selectedAnimal);


        createUI();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void dispose() {
        logger.debug("Disposing story screen");

        renderer.dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getEntityService().dispose();

        for (Texture texture : backgroundTextures) {
            texture.dispose();
        }

        ServiceLocator.clear();
    }

    /**
     * Creates the story's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new StoryDisplay(backgroundTextures, 0))
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new StoryActions(game, backgroundTextures));
        ServiceLocator.getEntityService().register(ui);
    }
    /**
     * Loads the appropriate story textures based on the selected animal.
     *
     * @param selectedAnimal the animal chosen by the player
     * @return an array of background textures for the story
     */
    private Texture[] loadStoryTextures(String selectedAnimal) {
        String[] storyPaths;

        switch (selectedAnimal.toLowerCase()) {
            case "dog":
                storyPaths = DOG_STORY_PATHS;
                break;
            case "croc":
                storyPaths = CROC_STORY_PATHS;
                break;
            case "bird":
                storyPaths = BIRD_STORY_PATHS;
                break;
            default:
                // Default to dog story if no valid animal is selected
                logger.warn("Invalid animal selection: {}. Defaulting to dog story.", selectedAnimal);
                storyPaths = DOG_STORY_PATHS;
                break;
        }

        Texture[] textures = new Texture[storyPaths.length];
        for (int i = 0; i < storyPaths.length; i++) {
            textures[i] = new Texture(storyPaths[i]);
        }
        return textures;
    }



}
