package com.csse3200.game.components.stats;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.LayoutAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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
    /**
     * Array to store stats.
     */
    private final Array<Stat> stats;
    final Actor[] lastPressedButton = {null};
    private Float originalY;


    /**
     * Array of texture paths used in the Achievements game screen.
     */
    private static final String[] StatTextures = {
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
            "images/logbook/stats/lb-stats-air-tab.png"};

public StatDisplay(GdxGame game) {
        super();
        this.game = game;
        this.stats = GameState.stats.stats;
    }

    /**
     * Creates and populates the UI with UI elements.
     */
    @Override
    public void create() {
        ServiceLocator.getResourceService().loadTextures(StatTextures);
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

        Table menuBtns = makeMenuBtns();

        Stack tabContentStack = new Stack();
        Stack enemyTypeStack = new Stack();

        // Populate tables with stat data
        Table itemsTable = makeLogbookTable(Stat.StatType.ITEM);
        Table enemiesTable = makeLogbookTable(Stat.StatType.ENEMY);
        Table playerTable = makeLogbookTable(Stat.StatType.PLAYER);

        // Initialise placeholder logbook tables for enemy sub-tabs
        Table landEnemyTable = makeLogbookTable(Stat.StatType.ENEMY);
        Table waterEnemyTable = makeLogbookTable(Stat.StatType.ITEM);
        Table airEnemyTable = makeLogbookTable(Stat.StatType.PLAYER);

        // Create tables for tabs
        Table tabs = makeTabs(itemsTable, enemiesTable, playerTable);
        Table enemyTabs = makeEnemyTabs(landEnemyTable, waterEnemyTable, airEnemyTable);

        // Add content tables to the stack
        tabContentStack.add(itemsTable);
        tabContentStack.add(enemiesTable);
        tabContentStack.add(playerTable);

        itemsTable.setVisible(false);
        enemiesTable.setVisible(true);
        playerTable.setVisible(false);

        // Add the enemy content tables to the stack
        enemyTypeStack.add(landEnemyTable);
        enemyTypeStack.add(waterEnemyTable);
        enemyTypeStack.add(airEnemyTable);

        // Initialize the visibility of the subtab content (only Land visible initially)
        landEnemyTable.setVisible(false);
        waterEnemyTable.setVisible(false);
        airEnemyTable.setVisible(true);

        // Populate the root table with tabs and content
        rootTable.add(tabs).fillX().row();
        rootTable.add(tabContentStack).expand().fill();
        rootTable.add(menuBtns).top().right().pad(10);

        // Add sub-tabs to the enemies table
        enemiesTable.add(enemyTabs).colspan(4).fillX(); // Subtab layout
        enemiesTable.row(); // Move to the next row for content
        // Add the enemy stack to the enemies table
        enemiesTable.add(enemyTypeStack).colspan(4).fillX();

        stage.addActor(rootTable);
    }

    void updateHoverEffect(Actor newButton) {
        // Hover effect actions
        Action hoverIn = Actions.moveBy(0, 5, 0.3f);  // Move up
        Action hoverOut = Actions.moveBy(0, -5, 0.3f); // Move down

        // Create a repeating action sequence
        Action repeatHover = Actions.forever(Actions.sequence(hoverIn, hoverOut));

        if (lastPressedButton[0] != null) {
            lastPressedButton[0].clearActions();
            lastPressedButton[0].setPosition(lastPressedButton[0].getX(), lastPressedButton[0].getY() - 5); // Reset Y position
            lastPressedButton[0].setScale(1f);  // Reset scale if applicable
        }

        newButton.addAction(repeatHover);  // Apply repeating hover effect to the new "button"
        lastPressedButton[0] = newButton;  // Update the last pressed "button"
    }

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
                updateHoverEffect(itemButton);  // Update hover effect
            }
        });

        enemyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(true);
                playersTable.setVisible(false);
                updateHoverEffect(enemyButton);
            }
        });

        playerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(false);
                playersTable.setVisible(true);
                updateHoverEffect(playerButton);
            }
        });
        // Set the initial hover effect
        this.originalY = itemButton.getY();
        updateHoverEffect(itemButton);
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
        Texture itemBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-land-tab.png", Texture.class);
        Texture enemyBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-water-tab.png", Texture.class);
        Texture playerBG =
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/stats/lb-stats-air-tab.png", Texture.class);

        // Labels for the tab titles
        TabButton landTypeButton = new TabButton("Land", skin, itemBG);
        TabButton waterTypeButton = new TabButton("Water", skin, enemyBG);
        TabButton airTypeButton = new TabButton("Air", skin, playerBG);

        // Add the button tables to the main table
        addButtonElevationEffect(landTypeButton);
        addButtonElevationEffect(waterTypeButton);
        addButtonElevationEffect(airTypeButton);
        tabButtonTable.left().top();
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
     * Creates the Table for the visual representation of end game statistics.
     * @return The Table showing stats.
     */
    private Table makeLogbookTable(Stat.StatType type) {
        Table table = new Table().left().top().padLeft(50);

        // Create and add the title label at the top of the table
        String modifiedType = type.name().charAt(0) + type.name().substring(1).toLowerCase();
        Label titleLabel = new Label(modifiedType + " Statistics", skin);
        titleLabel.setFontScale(2.0f); // Set the font size
        table.add(titleLabel).center().padTop(35).padBottom(20);  // Spanning across both columns and adding some padding below
        table.row();
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
    private Table makeMenuBtns() {
        TextButton exitBtn = new TextButton("Exit", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        exitMenu();
                    }
                });


        Table table = new Table();
        table.add(exitBtn);

        // Align the table to the top right of its parent container
        table.top().right();
        table.padRight(10);
        table.padTop(10);

        return table;
    }
    
    /**
     * Sets the current game screen back to the main menu.
     */
    void exitMenu() {
//        saveStats(stats);
        SaveHandler.save(GameState.class, "saves", FileLoader.Location.LOCAL);
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
        SaveHandler.save(GameState.class, "saves", FileLoader.Location.LOCAL);
        rootTable.clear();
        ServiceLocator.getResourceService().unloadAssets(StatTextures);
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

    public Object getRootTable() {
        return rootTable;
    }

    public Object[] getLastPressedButton() {
        return lastPressedButton;
    }
}
