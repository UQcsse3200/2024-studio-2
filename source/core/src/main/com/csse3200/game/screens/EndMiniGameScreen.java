package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.MapHandler;
import com.csse3200.game.components.inventory.PlayerInventoryDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.LootBoxFactory;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.inventory.items.lootbox.UniversalLootBox;
import com.csse3200.game.minigames.MiniGameConstants;
import com.csse3200.game.minigames.MiniGameMedals;
import com.csse3200.game.minigames.MiniGameNames;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceContainer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.slf4j.LoggerFactory;

/**
 * Makes a new screen when the snake game is over.
 * Displays the stats and add buttons to exit and restart.
 * Gives loot box and achievements to player based on the medal
 */
public class EndMiniGameScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(EndMiniGameScreen.class);

    // End game screen parameters
    private final GdxGame game;
    private final int score;
    private final MiniGameNames gameName;
    private final Stage stage;  // The stage to put elements onto
    private final Skin skin;  // The skin for buttons
    private float scale;  // Scale relative to screen size

    // fonts
    private final BitmapFont font18;
    private final BitmapFont font26;
    private final BitmapFont font32;
    private final Screen oldScreen;
    private final ServiceContainer oldScreenServices;  // The screen that the mini-game was called from
    private final Entity player;  // Player to add achievement sand loot boxes to
    private PlayerInventoryDisplay display;  // Inventory to add lootbox to
    private final Table contentTable;  // Table that contains all lables, button and images
    private final MiniGameMedals medal;  // Medal based on the score
    private Texture backgroundTexture;  // The background image texture
    private Image backgroundImage;  // The background image
    private Texture medalTexture; // Medal Image texture
    private static final String[] endMiniGameSounds = {  // Game sounds for each medal
            "sounds/minigames/fail.mp3",
            "sounds/minigames/bronze.mp3",
            "sounds/minigames/silver.mp3",
            "sounds/minigames/gold.mp3",
    };

    private final static String addItemsEvent = "addItem";
    private final static String errorMessage = "Unknown Mini-Game";

    public EndMiniGameScreen(GdxGame game, int score, MiniGameNames gameName, Screen screen, ServiceContainer container) {
        this.game = game;
        this.score = score;
        this.gameName = gameName;
        this.scale = 1;
        this.oldScreen = screen;
        this.oldScreenServices = container;
        this.medal = getMedal(this.score);

        this.stage = new Stage(new ScreenViewport());

        this.skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        this.font18 = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_18.fnt"));
        this.font26 = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_26.fnt"));
        this.font32 = new BitmapFont(Gdx.files.internal("flat-earth/skin/fonts/pixel_32.fnt"));

        Gdx.input.setInputProcessor(stage);  // Gets input from the stage

        ServiceLocator.registerResourceService(new ResourceService());  // For the sounds
        loadAssets();
        playSoundEffect();

        // Initialise contents Table
        contentTable = new Table();
        //contentTable.debug();  // Uncomment to visualise table on the screen
        setBackground();
        renderContents();

        // Rewarding achievement to player
        if (oldScreen instanceof MainGameScreen) {
            this.player = MapHandler.getCurrentMap().getPlayer();
            if (player != null) {
                this.display = player.getComponent(PlayerInventoryDisplay.class);
                logger.info("Achievement trigger {} {}", gameName.name(), medal.name());
                player.getEvents().trigger("miniGame", gameName, medal);
            }
        } else {
            this.player = null;
            this.display = null;
        }
    }

    /**
     * Renders the stage that includes the background image and the contentsTable
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        stage.act(delta);
        stage.draw();

        handleKeyPress();
    }

    /**
     * Loads game screen based on the gameName
     */
    private void setGameScreen() {
        dispose();
        switch (gameName) {
            case SNAKE -> game.newSnakeScreen(oldScreen, oldScreenServices);
            case BIRD -> game.newBirdScreen(oldScreen, oldScreenServices);
            case MAZE -> game.newMazeScreen(oldScreen, oldScreenServices);
        }
    }

    /**
     * Called when screen is initialised and resized
     * Clears the table and adds to the actor
     * Renders the medal Image, Title, Score, High-score, medal and personalised messages
     */
    private void renderContents() {

        contentTable.clear();
        contentTable.setFillParent(true);

        renderMedalImage();
        renderTitle();
        renderScoreLabels();
        renderMedalLabel();
        renderPersonalisedMessage();
        makeButtons();

        stage.addActor(contentTable);
    }

    /**
     * Adds the MedalImage to the content Tabel at the top of the screen if a medal has been achieved
     */
    private void renderMedalImage() {

        // Set the medal image based on the medal
        switch (medal) {
            case GOLD ->
                    this.medalTexture = new Texture(Gdx.files.internal("images/minigames/MiniGameGold.png"));
            case SILVER ->
                    this.medalTexture = new Texture(Gdx.files.internal("images/minigames/MiniGameSilver.png"));
            case BRONZE ->
                    this.medalTexture = new Texture(Gdx.files.internal("images/minigames/MiniGameBronze.png"));
            default -> this.medalTexture = null;
        }
        if (this.medalTexture != null) {
            // Add in the medal Image
            Image medalImage = new Image(medalTexture);
            medalImage.setScaling(Scaling.fit);  // Keep this to maintain the aspect ratio
            contentTable.add(medalImage).size(medalImage.getWidth() * 0.6f * scale, medalImage.getHeight() * 0.6f * scale).center().padBottom(20f).colspan(3).row();
        }
    }

    /**
     * Makes the title and puts into the contentTable
     */
    private void renderTitle() {
        // End of Mini-Game label
        font32.getData().setScale(3f * scale);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font32, Color.WHITE);
        Label endGameLabel = new Label("End of Mini-Game", labelStyle);
        contentTable.row().colspan(3);
        contentTable.add(endGameLabel).center().padBottom(80 * scale).row();
    }

    /**
     * Makes the medal text and puts onto the contentTable
     */
    private void renderMedalLabel() {
        Label.LabelStyle labelStyle;
        // Medal label
        if (medal == MiniGameMedals.FAIL) {
            font26.getData().setScale(2f * scale);
            labelStyle = new Label.LabelStyle(font26, Color.WHITE);
            Label medalLabel = new Label("You FAILED", labelStyle);
            contentTable.row().colspan(3);
            contentTable.add(medalLabel).center().padBottom(150 * scale).row();

        } else {
            font26.getData().setScale(2f * scale);
            labelStyle = new Label.LabelStyle(font26, Color.WHITE);
            Label medalLabel = new Label("You got a " + medal + " Medal :)", labelStyle);
            contentTable.row().colspan(3);
            contentTable.add(medalLabel).center().padBottom(50 * scale).row();
        }
    }

    /**
     * Makes the score and high score labels and adds them to the content Tabel
     */
    private void renderScoreLabels() {
        Label.LabelStyle labelStyle;
        // Score label
        font26.getData().setScale(2f * scale);
        labelStyle = new Label.LabelStyle(font26, Color.WHITE);
        Label scoreLabel = new Label("Score: " + score, labelStyle);
        contentTable.row().colspan(3);
        contentTable.add(scoreLabel).center().padBottom(20 * scale).row();

        // High-score label
        font26.getData().setScale(2f * scale);
        labelStyle = new Label.LabelStyle(font26, Color.WHITE);
        Label highscoreLabel = new Label("HighScore: " + GameState.minigame.getHighScore(gameName), labelStyle);
        contentTable.row().colspan(3);
        contentTable.add(highscoreLabel).center().padBottom(50 * scale).row();
    }

    /**
     * Adds the personalised message text to the contentTable
     */
    private void renderPersonalisedMessage() {
        Label.LabelStyle labelStyle;
        // Personalised message label
        font18.getData().setScale(2f * scale);
        labelStyle = new Label.LabelStyle(font18, Color.WHITE);
        String scoreMessage = getMessage();
        Label scoreMessageLabel = new Label(scoreMessage, labelStyle);
        contentTable.row().colspan(3);
        contentTable.add(scoreMessageLabel).center().padBottom(50 * scale);
    }

    /**
     * Makes the try again, menu and back to game buttons and adds them to the contents table
     */
    private void makeButtons() {
        // Make try again button
        TextButton tryAgainButton = new TextButton("Try Again", skin);
        tryAgainButton.getLabel().setFontScale(scale);
        tryAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameScreen();
            }
        });

        // Make Mini-Game Menu Button
        TextButton menuButton = new TextButton("Main Menu", skin);
        menuButton.getLabel().setFontScale(scale);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        // Make either "Return to Game" or "Mini_Game Menu" button (where the player came from)
        TextButton oldScreenButton;
        if (oldScreen instanceof MainGameScreen) {
            oldScreenButton = new TextButton("Return to Game", skin);
        } else {
            oldScreenButton = new TextButton("Mini-Game Menu", skin);
        }

        oldScreenButton.getLabel().setFontScale(scale);
        oldScreenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    giveLootBox();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                game.setOldScreen(oldScreen, oldScreenServices);
            }
        });

        // Align buttons in 1 row
        contentTable.row().expandX().fillX();
        contentTable.add(tryAgainButton).width(tryAgainButton.getWidth() * scale).height(tryAgainButton.getHeight() * scale).padLeft(500 * scale);
        contentTable.add(menuButton).width(menuButton.getWidth() * scale).height(menuButton.getHeight() * scale).center();
        contentTable.add(oldScreenButton).width(oldScreenButton.getWidth() * scale).height(oldScreenButton.getHeight() * scale).padRight(500 * scale);
    }

    /**
     * Changes the background colour based on sore/ medals (fail: green, bronze, silver and gold)
     */
    private void setBackground() {
        if (backgroundImage != null) {
            stage.getActors().removeValue(backgroundImage, true);
        }
        switch (gameName) {
            case SNAKE ->
                    backgroundTexture = new Texture(Gdx.files.internal("images/minigames/SnakeEndGameScreen.jpg"));
            case BIRD ->
                    backgroundTexture = new Texture(Gdx.files.internal("images/minigames/BirdEndGameScreen.png"));
            case MAZE ->
                    backgroundTexture = new Texture(Gdx.files.internal("images/minigames/MazeEndGameScreen2.png"));
            default -> throw new IllegalArgumentException(errorMessage);
        }

        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);  // Add background first
    }

    /**
     * Gives the player a loot box
     */
    private void giveLootBox() throws ClassNotFoundException {
        logger.info("Adding loot box to player's inventory.");
        LootBoxFactory lootBoxFactory = new LootBoxFactory();  // Create the factory to handle loot box creation

        // Determine the medal and get the corresponding loot box\
        if (player != null) {
            switch (medal) {
                case BRONZE -> {
                    // Create and add EarlyGameLootBox using the factory
                    UniversalLootBox earlyGameLootBox = lootBoxFactory.createLootBox("EarlyGameLootBox", player);
                    display.getEntity().getEvents().trigger(addItemsEvent, earlyGameLootBox);
                }
                case SILVER -> {
                    // Create and add MediumGameLootBox using the factory
                    UniversalLootBox mediumGameLootBox = lootBoxFactory.createLootBox("MediumGameLootBox", player);
                    display.getEntity().getEvents().trigger(addItemsEvent, mediumGameLootBox);
                }
                case GOLD -> {
                    // Create and add LateGameLootBox using the factory
                    UniversalLootBox lateGameLootBox = lootBoxFactory.createLootBox("LateGameLootBox", player);
                    display.getEntity().getEvents().trigger(addItemsEvent, lateGameLootBox);
                }
                default -> throw new IllegalArgumentException("Unknown medal");
            }
        }
    }

    /**
     * Changes the background colour based on sore/ medals (fail: green, bronze, silver and gold)
     */
    private void playSoundEffect() {
        switch (medal) {
            case FAIL ->
                // Failed
                    AudioManager.playSound("sounds/minigames/fail.mp3");
            case BRONZE ->
                // Bronze
                    AudioManager.playSound("sounds/minigames/bronze.mp3");
            case SILVER ->
                // Silver
                    AudioManager.playSound("sounds/minigames/silver.mp3");
            case GOLD ->
                // Gold
                    AudioManager.playSound("sounds/minigames/gold.mp3");
            default -> throw new IllegalArgumentException(errorMessage);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Gets the personalised massage based on score and mini-game
     *
     * @return the message
     */
    private String getMessage() {
        int bronzeMedalThreshold;
        int silverMedalThreshold;
        int goldMedalThreshold;
        String failMessage;
        String bronzeMessage;
        String silverMessage;
        String goldMessage;

        switch (gameName) {
            case SNAKE -> {
                bronzeMedalThreshold = MiniGameConstants.SNAKE_BRONZE_THRESHOLD;
                silverMedalThreshold = MiniGameConstants.SNAKE_SILVER_THRESHOLD;
                goldMedalThreshold = MiniGameConstants.SNAKE_GOLD_THRESHOLD;
                failMessage = "Damn that was a small snake...";
                bronzeMessage = "Nawww, look he's growing";
                silverMessage = "That's a really big snake alright";
                goldMessage = "Snake king!";
            }
            case BIRD -> {
                bronzeMedalThreshold = MiniGameConstants.BIRDIE_DASH_BRONZE_THRESHOLD;
                silverMedalThreshold = MiniGameConstants.BIRDIE_DASH_SILVER_THRESHOLD;
                goldMedalThreshold = MiniGameConstants.BIRDIE_DASH_GOLD_THRESHOLD;
                failMessage = "You're broke, maybe talk to Centerlink?";
                bronzeMessage = "Almost middle class";
                silverMessage = "Damn she rich";
                goldMessage = "Donate to a poor software engineering student? Please :)";
            }
            case MAZE -> {
                bronzeMedalThreshold = MiniGameConstants.MAZE_BRONZE_THRESHOLD;
                silverMedalThreshold = MiniGameConstants.MAZE_SILVER_THRESHOLD;
                goldMedalThreshold = MiniGameConstants.MAZE_GOLD_THRESHOLD;
                failMessage = "You single-handedly committed mass genocide";
                bronzeMessage = "At least you found Gerald";
                silverMessage = "Unfortunately, we lost Gerald and Bruce :(";
                goldMessage = "Yay! You got all the fishie babies!!";
            }
            default -> throw new IllegalArgumentException(errorMessage);
        }

        if (score < bronzeMedalThreshold) {
            return failMessage;
        } else if (score < silverMedalThreshold) {
            return bronzeMessage;
        } else if (score < goldMedalThreshold) {
            return silverMessage;
        } else {
            return goldMessage;
        }
    }

    /**
     * Dispose the fonts, skin and stage.
     */
    @Override
    public void dispose() {
        if (font18 != null) {font18.dispose();}
        if (font26 != null) {font26.dispose();}
        if (font32 != null) {font32.dispose();}
        if (stage != null) {stage.dispose();}
        if (skin != null) {skin.dispose();}
        if (backgroundTexture != null) {backgroundTexture.dispose();}
        if (medalTexture != null) {medalTexture.dispose();}
        unloadAssets();
        ServiceLocator.getResourceService().dispose();
    }

    /**
     * Resize function that automatically gets called when the screen is resized.
     * Resizes all components with a consistent scale to maintain the screen's
     * original design.
     *
     * @param width  The width of the resized screen.
     * @param height The height of the resized screen.
     */
    @Override
    public void resize(int width, int height) {
        // Update the stage viewport
        float baseWidth = 1920f;
        float baseHeight = 1200f;
        float scaleWidth = width / baseWidth;
        float scaleHeight = height / baseHeight;
        stage.getViewport().update(width, height, true);
        scale = Math.min(scaleWidth, scaleHeight);
        if (scale == 0) { // Screen is minimised
            scale = 1;
        }
        stage.clear();
        contentTable.clear();

        setBackground();
        renderContents();
    }

    /**
     * Changes the screen if backspace or R is pressed (to mini-games menu or back to game respectively)
     */
    private void handleKeyPress() {

        // Key functionality for escape and restart
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {  // Restart game
            setGameScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {  // Go to Mini-games menu
            Gdx.gl.glClearColor(248f / 255f, 249f / 255f, 178f / 255f, 1f);
            game.setOldScreen(oldScreen, oldScreenServices);
        }
    }

    /**
     * Get the medal associated with the players score for current game
     *
     * @param score: the players score
     * @return the medal associated with the score for each game
     */
    private MiniGameMedals getMedal(int score) {

        // Get the medal thresholds for each game
        int bronzeThreshold;
        int silverThreshold;
        int goldThreshold;
        switch (gameName) {
            case SNAKE -> {
                bronzeThreshold = MiniGameConstants.SNAKE_BRONZE_THRESHOLD;
                silverThreshold = MiniGameConstants.SNAKE_SILVER_THRESHOLD;
                goldThreshold = MiniGameConstants.SNAKE_GOLD_THRESHOLD;
            }
            case BIRD -> {
                bronzeThreshold = MiniGameConstants.BIRDIE_DASH_BRONZE_THRESHOLD;
                silverThreshold = MiniGameConstants.BIRDIE_DASH_SILVER_THRESHOLD;
                goldThreshold = MiniGameConstants.BIRDIE_DASH_GOLD_THRESHOLD;
            }
            case MAZE -> {
                bronzeThreshold = MiniGameConstants.MAZE_BRONZE_THRESHOLD;
                silverThreshold = MiniGameConstants.MAZE_SILVER_THRESHOLD;
                goldThreshold = MiniGameConstants.MAZE_GOLD_THRESHOLD;
            }
            default -> throw new IllegalArgumentException(errorMessage);
        }

        // Return the medal
        if (score < bronzeThreshold) {
            // Fail
            return MiniGameMedals.FAIL;
        } else if (score < silverThreshold) {
            // Bronze
            return MiniGameMedals.BRONZE;
        } else if (score < goldThreshold) {
            // Silver
            return MiniGameMedals.SILVER;
        } else {
            // Gold
            return MiniGameMedals.GOLD;
        }
    }

    /**
     * Loads the sound assets
     */
    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadSounds(endMiniGameSounds);
        ServiceLocator.getResourceService().loadAll();
    }

    /**
     * Unloads the sound assets
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(endMiniGameSounds);
    }
}
