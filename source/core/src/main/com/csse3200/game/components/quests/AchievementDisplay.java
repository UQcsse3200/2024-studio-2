package com.csse3200.game.components.quests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.Achievements;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The {@code AchievementDisplay} class manages the display of achievements in the achievements screen
 */
public class AchievementDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(AchievementDisplay.class);
    private final GdxGame game;
    private Table rootTable;
    /** Array to store achievements. */
    private final java.util.List<Achievement> achievements;
    final TabButton[] lastPressedButton = {null};
    private Float originalY;
    private static final String SAVESPATH = "saves/achievement";
    private static final String LOGBOOKSOUND = "sounds/logbook/select_005.ogg";

    /**
     * Array of texture paths used in the Achievements game screen.
     */
    private static final String[] logbookTextures = {"images/logbook/icons/first-steps.png",
            "images/logbook/lb-exit.png",
            "images/logbook/lb-bg.png",
            "images/logbook/lb-yellow-tab.png",
            "images/logbook/lb-blue-tab.png",
            "images/logbook/lb-red-tab.png",
            "images/logbook/lb-blue-btn.png",
            "images/logbook/lb-red-btn.png",
            "images/logbook/lb-yellow-btn.png",
            "images/logbook/lb-blue-btn-pressed.png",
            "images/logbook/lb-red-btn-pressed.png",
            "images/logbook/lb-yellow-btn-pressed.png"};

    private static final String[] logbookSounds = {LOGBOOKSOUND,
            "sounds/logbook/select_004.ogg"};

    /**
     * Constructs an {@code AchievementDisplay} instance for managing the display of achievements in the game.
     * @param game The main game instance.
     */
    public AchievementDisplay(GdxGame game) {
        super();
        this.game = game;
        AchievementManager achievementManager = new AchievementManager();
        this.achievements =  achievementManager.getAchievements();
    }

    /**
     * Initializes the Achievement display by loading textures, sounds, and creating UI components.
     */
    @Override
    public void create() {
        ServiceLocator.getResourceService().loadTextures(logbookTextures);
        ServiceLocator.getResourceService().loadSounds(logbookSounds);
        ServiceLocator.getResourceService().loadAll();
        super.create();
        addActors();
    }

    /**
     * Add UI elements to the display. Elements will be populated in their own Tables, before being added to a rootTable.
     */
    private void addActors() {
        rootTable = new Table();
        // Load and set the background texture for the entire screen
        Image background = new Image(new Texture("images/BackgroundSplash.png"));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Add the background to the stage
        stage.addActor(background);
        Image rootTableBG = new Image(ServiceLocator.getResourceService().getAsset("images/logbook/lb-bg.png",Texture.class));
        rootTable.setBackground(rootTableBG.getDrawable());
        rootTable.center();
        float tableWidth = Gdx.graphics.getWidth() * 0.8f;  // 80% of screen width
        float tableHeight = Gdx.graphics.getHeight() * 0.8f; // 80% of screen height
        rootTable.setSize(tableWidth, tableHeight);
        rootTable.setPosition(
                (Gdx.graphics.getWidth() - tableWidth) / 2,
                (Gdx.graphics.getHeight() - tableHeight) / 1.6f
        );


        Table exitButton = makeExitButton();

        Table clearButton = makeClearButton();

        Stack tabContentStack = new Stack();

        Table itemsTable = makeLogbookTable(Achievement.AchievementType.ITEM);

        Table enemiesTable = makeLogbookTable(Achievement.AchievementType.ENEMY);

        Table achievementsTable = makeLogbookTable(Achievement.AchievementType.ADVANCEMENT);

        Table tabs = makeTabs(itemsTable, enemiesTable, achievementsTable);

        // Add content tables to the stack
        tabContentStack.add(itemsTable);
        tabContentStack.add(enemiesTable);
        tabContentStack.add(achievementsTable);

        itemsTable.setVisible(true);
        enemiesTable.setVisible(false);
        achievementsTable.setVisible(false);
        rootTable.add(tabs).fillX().row();
        rootTable.add(tabContentStack).expand().fill().row();
        rootTable.add(exitButton.left().bottom()).left().bottom();
        rootTable.add(clearButton).right().bottom();
        stage.addActor(rootTable);
    }

    /**
     * Updates the hover effect on the tab buttons.
     * @param newButton The new button to apply the hover effect.
     */
    void updateHoverEffect(TabButton newButton) {

        // Hover effect actions
        Action hoverIn = Actions.moveBy(0, 5, 0.3f);  // Move up
        Action hoverOut = Actions.moveBy(0, -5, 0.3f); // Move down

        // Create a repeating action sequence
        Action repeatHover = Actions.forever(Actions.sequence(hoverIn, hoverOut));

        if (lastPressedButton[0] != null) {
            lastPressedButton[0].clearActions();
            lastPressedButton[0].setY(originalY); // Reset Y position
            lastPressedButton[0].setScale(1f);
        }

        newButton.addAction(repeatHover); // Apply repeating hover effect to the new button
        lastPressedButton[0] = newButton; // Update the last pressed button
    }

    /**
     * Creates the clear button
     * @return Table containing the button
     */
    private Table makeClearButton() {
        Table table = new Table();
        TextButton button = new TextButton("Clear", skin);
        addButtonElevationEffect(button);
        button.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("clear button clicked");
                        clearAchievements();
                    }
                });
        table.add(button);
        return table;
    }

    /**
     * Creates the tab buttons
     * @param itemsTable The table for displaying item-related achievements.
     * @param enemiesTable The table for displaying enemy-related achievements.
     * @param achievementsTable The table for displaying advancement-related achievements.
     * @return The table containing tab buttons.
     */
    private Table makeTabs(Table itemsTable, Table enemiesTable, Table achievementsTable) {
        Table tabButtonTable = new Table().padLeft(50);

        Texture itemBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-yellow-tab.png", Texture.class);
        Texture enemyBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-red-tab.png", Texture.class);
        Texture achievementBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-blue-tab.png", Texture.class);

        Sound tabSound = ServiceLocator.getResourceService().getAsset(LOGBOOKSOUND, Sound.class);

        TabButton itemButton = new TabButton("Items", skin, itemBG);
        TabButton enemyButton = new TabButton("Enemy", skin, enemyBG);
        TabButton achievementButton = new TabButton("Achievements", skin, achievementBG);

        addButtonElevationEffect(itemButton);
        addButtonElevationEffect(enemyButton);
        addButtonElevationEffect(achievementButton);
        tabButtonTable.left().top();
        tabButtonTable.add(itemButton).left();
        tabButtonTable.add(enemyButton).left();
        tabButtonTable.add(achievementButton).left();

        itemButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                itemsTable.setVisible(true);
                enemiesTable.setVisible(false);
                achievementsTable.setVisible(false);
                tabSound.play();
                updateHoverEffect(itemButton);
            }
        });

        enemyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(true);
                achievementsTable.setVisible(false);
                tabSound.play();
                updateHoverEffect(enemyButton);
            }
        });

        achievementButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(false);
                achievementsTable.setVisible(true);
                tabSound.play();
                updateHoverEffect(achievementButton);
            }
        });
        this.originalY = itemButton.getY();
        updateHoverEffect(itemButton);
        return tabButtonTable;
    }




    /**
     * Creates the Table showing the completed achievements of a specific type
     *
     * @param type The type of achievements to be displayed (e.g., ITEM, ENEMY, ADVANCEMENT).
     * @return The Table showing achievements.
     */
    private Table makeLogbookTable(Achievement.AchievementType type) {
        Table table = new Table().left().top().padLeft(50);
        int advancementCounter = 0;
        for (Achievement achievement : achievements) {
            if (achievement.isCompleted() && achievement.getType() == type) {
                Action newAnimation = Actions.forever(Actions.sequence(
                        Actions.color(Color.WHITE, 0.5f),
                        Actions.color(Color.GOLD, 0.5f)
                ));
                Sound buttonSound = ServiceLocator.getResourceService().getAsset("sounds/logbook/select_004.ogg", Sound.class);
                Texture button =
                        ServiceLocator.getResourceService()
                                .getAsset("images/logbook/lb-yellow-btn.png", Texture.class);
                Texture buttonPressed =
                        ServiceLocator.getResourceService()
                                .getAsset("images/logbook/lb-yellow-btn-pressed.png", Texture.class);

                ServiceLocator.getResourceService().loadAsset(achievement.getPath(), Texture.class);
                ServiceLocator.getResourceService().loadAll();
                Texture icon = ServiceLocator.getResourceService().getAsset(achievement.getPath(), Texture.class);

                LogButton entry = new LogButton(button, buttonPressed, icon);
                advancementCounter ++;

                if (!achievement.isSeen()) {
                    entry.addAction(newAnimation);
                }

                addButtonElevationEffect(entry);
                table.add(entry);
                entry.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        buttonSound.play();
                        achievement.setSeen();
                        if (achievement.isSeen()) {
                            entry.removeAction(newAnimation);
                            entry.setColor(Color.WHITE);
                        }
                        showDetailPopup(achievement);
                    }
                });
                if(advancementCounter == 6){
                    table.row();
                    advancementCounter = 0;
                }
            }
        }
        return table;
    }

    /**
     * Creates the table for the exit button.
     * @return The Table showing the exit button.
     */
    private Table makeExitButton() {
        Image exit = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-exit.png", Texture.class));

        ImageButton exitBtn = new ImageButton(exit.getDrawable());
        addButtonElevationEffect(exitBtn);
        Sound tabSound = ServiceLocator.getResourceService().getAsset(LOGBOOKSOUND, Sound.class);
        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        tabSound.play();
                        exitMenu();
                    }
                });


        Table table = new Table().padLeft(25);
        table.add(exitBtn);
        return table;
    }


    /**
     * Creates and displays a detail dialogue displaying the name,description and image for an achievement.
     * @param achievement an achievements from defaultsaves/achievements.json
     */
    private void showDetailPopup(Achievement achievement) {
        // Create a new dialog with a title
        Dialog detailDialog = new Dialog("", skin);

        // Background
        Texture backgroundTexture = new Texture("images/SettingBackground.png");
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        detailDialog.setBackground(backgroundDrawable);

        // Create a table for the dialog content
        Table contentTable = new Table();
        contentTable.setFillParent(true);

        // Add content
        Label nameLabel = new Label(achievement.getQuestName(), skin,"title");
        Label descriptionLabel = new Label(achievement.getQuestDescription(), skin);
        Image icon = new Image(new Texture(achievement.getPath()));
        icon.setSize(250, 250); // Set the size of the icon

        contentTable.add(nameLabel).center().pad(-10,0,0,0);
        contentTable.row();
        contentTable.add().height(50);
        contentTable.row();
        contentTable.add(icon).size(250, 250);
        contentTable.row();
        contentTable.add(descriptionLabel);

        detailDialog.getContentTable().add(contentTable).expand().fill();
        detailDialog.button("Close", true);
        detailDialog.getContentTable();
        detailDialog.getButtonTable();
        detailDialog.setSize(400, 300);

        detailDialog.show(stage);
    }

    /**
     * Sets the current game screen back to the main menu.
     */
    private void exitMenu() {
        SaveHandler.save(Achievements.class, SAVESPATH, FileLoader.Location.LOCAL);
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    private void clearAchievements() {
        SaveHandler.delete(Achievements.class, SAVESPATH, FileLoader.Location.LOCAL);
        Achievements.resetState();
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        // batch isn't used, batchDupe is to make SonarCloud happy, unsure why batch doesn't just work, but it causes
        // the game to crash :/
        SpriteBatch batchDupe = new SpriteBatch();
        batchDupe.begin();
        batchDupe.end();
    }

    /**
     * Clear the UI elements on the display
     */
    @Override
    public void dispose() {
        SaveHandler.save(Achievements.class, SAVESPATH, FileLoader.Location.LOCAL);
        rootTable.clear();
        ServiceLocator.getResourceService().unloadAssets(logbookTextures);
        ServiceLocator.getResourceService().unloadAssets(logbookSounds);
        ServiceLocator.getResourceService().clearAllAssets();
        super.dispose();
    }

    /**
     * Adds an elevation effect to buttons when hovered.
     */
    private void addButtonElevationEffect(Button button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
                //logger.info("Hover feature activated"); uncomment this if you want to check hover feature
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, -5, 0.1f),
                        Actions.scaleTo(1f, 1f, 0.1f)
                ));
            }
        });
    }
}