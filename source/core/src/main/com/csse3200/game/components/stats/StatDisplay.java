package com.csse3200.game.components.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.quests.TabButton;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Display detailed stats after defeating the final boss of the game.
 */
public class StatDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(StatDisplay.class);
    private final GdxGame game;
    private Table rootTable;
    private final Array<Stat> stats;
    final Actor[] lastPressedButton = {null};
    private Float originalY;
    private static final String LOGBOOKSOUND = "sounds/logbook/select_005.ogg";

    /**
     * Array of texture paths used in the Achievements game screen.
     */
    private static final String[] StatTextures = {
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
            "images/logbook/lb-yellow-btn-pressed.png",
            "images/logbook/stats/lb-stats-land-tab.png",
            "images/logbook/stats/lb-stats-water-tab.png",
            "images/logbook/stats/lb-stats-air-tab.png",
            "images/logbook/stats/lb-stats-food-tab.png",
            "images/logbook/stats/lb-stats-potion-tab.png",
            "images/logbook/stats/lb-stats-minigame-tab.png",
            "images/logbook/stats/lb-stats-combat-tab.png"};


private static final String[] logbookSounds = {LOGBOOKSOUND,
            "sounds/logbook/select_004.ogg"};

public StatDisplay(GdxGame game) {
        super();
        this.game = game;
        this.stats = GameState.stats.stats;
    }

    /**
     * Creates and populates the UI with UI elements.
     */
    @Override public void create() {
        ServiceLocator.getResourceService().loadTextures(StatTextures);
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

        // Initialise tab stacks
        Stack tabContentStack = new Stack();
        Stack itemTypeStack = new Stack();
        Stack enemyTypeStack = new Stack();
        Stack playerTypeStack = new Stack();

        // Populate tables with stat data
        Table itemsTable = makeLogbookTable(Stat.StatType.ITEM);
        Table enemiesTable = makeLogbookTable(Stat.StatType.ENEMY);
        Table playerTable = makeLogbookTable(Stat.StatType.PLAYER);

        // Initialise placeholder logbook tables for enemy sub-tabs
        Table foodItemTable = makeLogbookTable(Stat.StatType.FOOD_ITEM);
        Table potionItemTable = makeLogbookTable(Stat.StatType.POTION_ITEM);

        // Initialise logbook tables for enemy sub-tabs
        Table landEnemyTable = makeLogbookTable(Stat.StatType.LAND_ENEMY);
        Table waterEnemyTable = makeLogbookTable(Stat.StatType.WATER_ENEMY);
        Table airEnemyTable = makeLogbookTable(Stat.StatType.AIR_ENEMY);

        // Initialise placeholder logbook tables for enemy sub-tabs
        Table playerMinigameTable = makeLogbookTable(Stat.StatType.PLAYER_MINIGAME);
        Table playerCombatTable = makeLogbookTable(Stat.StatType.PLAYER_COMBAT);

        // Create tables for tabs
        Table tabs = makeTabs(itemsTable, enemiesTable, playerTable);
        Table itemTabs = makeItemTabs(foodItemTable, potionItemTable);
        Table enemyTabs = makeEnemyTabs(landEnemyTable, waterEnemyTable, airEnemyTable);
        Table playerTabs = makePlayerTabs(playerMinigameTable, playerCombatTable);

        // Add content tables to the stack
        tabContentStack.add(itemsTable);
        tabContentStack.add(enemiesTable);
        tabContentStack.add(playerTable);

        itemsTable.setVisible(true);
        enemiesTable.setVisible(false);
        playerTable.setVisible(false);

        // Add the item tab content tables to the stack
        itemTypeStack.add(foodItemTable);
        itemTypeStack.add(potionItemTable);

        // Add the enemy tab content tables to the stack
        enemyTypeStack.add(landEnemyTable);
        enemyTypeStack.add(waterEnemyTable);
        enemyTypeStack.add(airEnemyTable);

        // Add the player tab content tables to the stack
        playerTypeStack.add(playerMinigameTable);
        playerTypeStack.add(playerCombatTable);

        // Initialize the visibility of the sub-tab content
        foodItemTable.setVisible(true);
        potionItemTable.setVisible(false);

        landEnemyTable.setVisible(true);
        waterEnemyTable.setVisible(false);
        airEnemyTable.setVisible(false);

        playerMinigameTable.setVisible(true);
        playerCombatTable.setVisible(false);

        // Populate the root table with tabs and content
        rootTable.add(tabs).fillX().row();
        rootTable.add(tabContentStack).expand().fill();
        rootTable.add(exitButton).top().right().pad(10);

        // Add sub-tabs to tables
        addSubTabs(itemsTable, itemTabs, itemTypeStack);
        addSubTabs(enemiesTable, enemyTabs, enemyTypeStack);
        addSubTabs(playerTable, playerTabs, playerTypeStack);

        stage.addActor(rootTable);
    }

    /**
     * Helper function to add sub tab to relevant logbook tab
     * @param table The parent table (ITEM,ENEMY,PLAYER)
     * @param tabs The tabs to add to table
     * @param stack The stack of table content
     */
    private void addSubTabs (Table table, Table tabs, Stack stack) {
        // Add sub-tabs to the enemies table
        table.add(tabs).colspan(4).fillX(); // Subtab layout
        table.row(); // Move to the next row for content
        // Add the enemy stack to the enemies table
        table.add(stack).colspan(4).fillX();
    }

    /**
     * Create base tabs for the end-game stats logbook
     * @param itemsTable The items table content
     * @param enemiesTable The enemt table content
     * @param playersTable The player table content
     * @return
     */
    Table makeTabs(Table itemsTable, Table enemiesTable, Table playersTable) {
        Table tabButtonTable = new Table().padLeft(50);

        // Background images for the tabs
        Texture itemBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-yellow-tab.png", Texture.class);
        Texture enemyBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-red-tab.png", Texture.class);
        Texture playerBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-blue-tab.png", Texture.class);

        // Labels for the tab titles
        TabButton itemButton = new TabButton("Items", skin, itemBG);
        TabButton enemyButton = new TabButton("Enemy", skin, enemyBG);
        TabButton playerButton = new TabButton("Player", skin, playerBG);

        // Add the button tables to the main table
        addButtonElevationEffect(itemButton);
        addButtonElevationEffect(enemyButton);
        addButtonElevationEffect(playerButton);
        tabButtonTable.left().top();
        tabButtonTable.add(itemButton).left();
        tabButtonTable.add(enemyButton).left();
        tabButtonTable.add(playerButton).left();

        // Add event listeners to simulate button clicks using ClickListener
        itemButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemsTable.setVisible(true);
                enemiesTable.setVisible(false);
                playersTable.setVisible(false);
            }
        });

        enemyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(true);
                playersTable.setVisible(false);
            }
        });

        playerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(false);
                playersTable.setVisible(true);
            }
        });
        // Set the initial hover effect
        this.originalY = itemButton.getY();
        return tabButtonTable;
    }

    /**
     * Create Item statistics sub-tabs to distinguish the type (FOOD, POTION)
     * of item.
     * @param foodItemTable The table displaying food item stats.
     * @param potionItemTable The table displaying potion item stats.
     * @return Tabs for land water and air enemies.
     */
    Table makeItemTabs(Table foodItemTable, Table potionItemTable) {
        Table tabButtonTable = new Table().padLeft(15);

        // Background images for the tabs
        Texture foodBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-food-tab.png", Texture.class);
        Texture potionBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-potion-tab.png", Texture.class);

        // Labels for the tab titles
        TabButton foodTypeButton = new TabButton("Food", skin, foodBG);
        TabButton potionTypeButton = new TabButton("Potion", skin, potionBG);

        // Add the button tables to the main table
        addButtonElevationEffect(foodTypeButton);
        addButtonElevationEffect(potionTypeButton);
        tabButtonTable.left().top().padBottom(20);
        tabButtonTable.add(foodTypeButton).left();
        tabButtonTable.add(potionTypeButton).left().padLeft(-25);

        // Add event listeners to simulate button clicks using ClickListener
        foodTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                foodItemTable.setVisible(true);
                potionItemTable.setVisible(false);
            }
        });

        potionTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                foodItemTable.setVisible(false);
                potionItemTable.setVisible(true);
            }
        });
        // Set the initial hover effect
        this.originalY = foodTypeButton.getY();
        return tabButtonTable;
    }

    /**
     * Create Enemy statistics sub-tabs to distinguish area (LAND, OCEAN, AIR)
     * the enemy is from.
     * @param landEnemyTable The table displaying land enemy stats.
     * @param waterEnemyTable The table displaying water enemy stats.
     * @param airEnemyTable The table displaying air enemy stats.
     * @return Tabs for land water and air enemies.
     */
    Table makeEnemyTabs(Table landEnemyTable, Table waterEnemyTable, Table airEnemyTable) {
        Table tabButtonTable = new Table().padLeft(15);

        // Background images for the tabs
        Texture landBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-land-tab.png", Texture.class);
        Texture waterBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-water-tab.png", Texture.class);
        Texture airBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-air-tab.png", Texture.class);

        // Labels for the tab titles
        TabButton landTypeButton = new TabButton("Land", skin, landBG);
        TabButton waterTypeButton = new TabButton("Water", skin, waterBG);
        TabButton airTypeButton = new TabButton("Air", skin, airBG);

        // Add the button tables to the main table
        addButtonElevationEffect(landTypeButton);
        addButtonElevationEffect(waterTypeButton);
        addButtonElevationEffect(airTypeButton);
        tabButtonTable.left().top().padBottom(20);
        tabButtonTable.add(landTypeButton).left();
        tabButtonTable.add(waterTypeButton).left().padLeft(-25);
        tabButtonTable.add(airTypeButton).left().padLeft(-25);

        // Add event listeners to simulate button clicks using ClickListener
        landTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                landEnemyTable.setVisible(true);
                waterEnemyTable.setVisible(false);
                airEnemyTable.setVisible(false);
            }
        });

        waterTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                landEnemyTable.setVisible(false);
                waterEnemyTable.setVisible(true);
                airEnemyTable.setVisible(false);
            }
        });

        airTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                landEnemyTable.setVisible(false);
                waterEnemyTable.setVisible(false);
                airEnemyTable.setVisible(true);
            }
        });
        // Set the initial hover effect
        this.originalY = landTypeButton.getY();
        return tabButtonTable;
    }

    /**
     * Create Enemy statistics sub-tabs to distinguish area (LAND, OCEAN, AIR)
     * the enemy is from.
     * @param playerMinigameTable The table displaying player minigame stats.
     * @param playerCombatTable The table displaying player combat stats.
     * @return Tabs for land water and air enemies.
     */
    Table makePlayerTabs(Table playerMinigameTable, Table playerCombatTable) {
        Table tabButtonTable = new Table().padLeft(15);

        // Background images for the tabs
        Texture minigameBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-minigame-tab.png", Texture.class);
        Texture combatBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-combat-tab.png", Texture.class);

        // Labels for the tab titles
        TabButton minigameTypeButton = new TabButton("Minigame", skin, minigameBG);
        TabButton combatTypeButton = new TabButton("Combat", skin, combatBG);

        // Add the button tables to the main table
        addButtonElevationEffect(minigameTypeButton);
        addButtonElevationEffect(combatTypeButton);
        tabButtonTable.left().top().padBottom(20);
        tabButtonTable.add(minigameTypeButton).left();
        tabButtonTable.add(combatTypeButton).left().padLeft(-25);

        // Add event listeners to simulate button clicks using ClickListener
        minigameTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerMinigameTable.setVisible(true);
                playerCombatTable.setVisible(false);
            }
        });

        combatTypeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerMinigameTable.setVisible(false);
                playerCombatTable.setVisible(true);
            }
        });
        // Set the initial hover effect
        this.originalY = minigameTypeButton.getY();
        return tabButtonTable;
    }

    /**
     * Creates the Table for the visual representation of end game statistics.
     * @return The Table showing stats.
     */
    private Table makeLogbookTable(Stat.StatType type) {
        Table table = new Table().left().top().padLeft(50);

        // Create and add the title label at the top of the table
        String modifiedType = type.name().charAt(0) + type.name().substring(1).toLowerCase();
        if (type.name().equals("ITEM") || type.name().equals("ENEMY") || type.name().equals("PLAYER")) {
            Label titleLabel = new Label(modifiedType + " Statistics", skin);
            titleLabel.setFontScale(2.0f); // Set the font size
            table.add(titleLabel).center().padTop(35).padBottom(20);
            table.row();
        } else {
            table.row();
        }
        int nextRow = 0;

        // Log two stats per row with a line separator between them
        for (Stat stat : stats) {
            // Log 2 stats per row
            logger.info("stat is: {}", stat);
            if (stat.getType() == type) {
                // Create labels to display stat information
                Label statNameLabel = new Label(String.valueOf(stat.getStatDescription()), skin);
                Label statCurrentLabel = new Label(String.valueOf(stat.getCurrent()), skin);

                // Style the labels
                statNameLabel.setFontScale(1.5f);
                statCurrentLabel.setFontScale(1.5f);

                // Add stat labels to the table
                table.add(statNameLabel).expandX().padBottom(5);
                table.add(statCurrentLabel).expandX().padBottom(15);
                nextRow++;

                if (nextRow % 2 == 0) {
                    logger.info("count is even");
                    table.row();
                    // Add a physical line as a separator
                    float lineHeight = 2f; // Set line height/thickness
                    float lineWidth = Gdx.graphics.getWidth();
                    Image separator = createSeparator(lineWidth, lineHeight, Color.BLACK); // Adjust width as needed
                    table.add(separator).colspan(4).fillX().padBottom(20);
                    table.row();
                }
            }
        }
        return table;
    }

    /**
     * Helper function to create a line separator between statistics
     * @param width the width of the separator
     * @param height the height of the separator
     * @param color the colour of the separator
     * @return the drawable image line separator
     */
    private Image createSeparator(float width, float height, Color color) {
        // Create a Pixmap for the line
        Pixmap pixmap = new Pixmap((int) width, (int) height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        // Create a drawable from the pixmap
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(pixmap));

        // Dispose of the pixmap to free up resources
        pixmap.dispose();

        return new Image(drawable);
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
     * Sets the current game screen back to the main menu.
     */
    void exitMenu() {
        SaveHandler.getInstance().save(GameState.class, "saves", FileLoader.Location.LOCAL);
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        SpriteBatch batchDupe = new SpriteBatch();
        batchDupe.begin();
        batchDupe.draw(new Texture("images/BackgroundSplash.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batchDupe.end();
    }
 
    /**
     * Clear the UI elements on the display
     */
    @Override
    public void dispose() {
        SaveHandler.getInstance().save(GameState.class, "saves", FileLoader.Location.LOCAL);
        rootTable.clear();
        ServiceLocator.getResourceService().unloadAssets(StatTextures);
        ServiceLocator.getResourceService().unloadAssets(logbookSounds);
        ServiceLocator.getResourceService().clearAllAssets();
        super.dispose();
    }

    /**
     * Adds an elevation effect to tables when hovered.
     */
    private void addButtonElevationEffect(Button button) {
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
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
