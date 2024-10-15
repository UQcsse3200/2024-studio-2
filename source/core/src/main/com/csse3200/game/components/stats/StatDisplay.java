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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Display detailed stats after defeating the final boss of the game.
 */
public class StatDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(StatDisplay.class);
    private final GdxGame game;
    private Table rootTable;
    /** Array to store stats. */
    private final Array<Stat> stats;
    final Actor[] lastPressedButton = {null};

    /**
     * Array of texture paths used in the Achievements game screen.
     */
    private static final String[] StatTextures = {"images/logbook/lb-bg.png","images/logbook/lb-yellow-tab.png",
            "images/logbook/lb-blue-tab.png", "images/logbook/lb-red-tab.png", "images/logbook/lb-blue-btn.png",
            "images/logbook/lb-red-btn.png", "images/logbook/lb-yellow-btn.png", "images/logbook/lb-blue-btn-pressed.png", "images/logbook/lb-red-btn-pressed.png", "images/logbook/lb-yellow-btn-pressed.png"};


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

        // Populate tables with stat data
        Table itemsTable = makeLogbookTable(Stat.StatType.ITEM);

        Table enemiesTable = makeLogbookTable(Stat.StatType.ENEMY);
        //enemiesTable.add(new Label("Content for Tab 2", skin));

        Table playerTable = makeLogbookTable(Stat.StatType.PLAYER);
        //playerTable.add(new Label("Content for Tab 3", skin));

        Table tabs = makeTabs(itemsTable, enemiesTable, playerTable);

        // Add content tables to the stack
        tabContentStack.add(itemsTable);
        tabContentStack.add(enemiesTable);
        tabContentStack.add(playerTable);

        itemsTable.setVisible(false);
        enemiesTable.setVisible(true);
        playerTable.setVisible(false);

        rootTable.add(tabs).fillX().row();
        rootTable.add(tabContentStack).expand().fill();
        rootTable.add(menuBtns).top().right().pad(10);
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

    Table makeTabs(Table itemsTable, Table enemiesTable, Table achievementsTable) {
        Table tabButtonTable = new Table().padLeft(50);

        // Background images for the tabs
        Image itemBG = new Image(ServiceLocator.getResourceService()
                .getAsset("images/logbook/lb-yellow-tab.png", Texture.class));
        Image enemyBG = new Image(ServiceLocator.getResourceService()
                .getAsset("images/logbook/lb-red-tab.png", Texture.class));
        Image playerBG = new Image(ServiceLocator.getResourceService()
                .getAsset("images/logbook/lb-blue-tab.png", Texture.class));

        // Labels for the tab titles
        Label itemLabel = new Label("Items", skin);
        Label enemyLabel = new Label("Enemy", skin);
        Label playerLabel = new Label("Player", skin);

        // Set the alignment and font size for the labels
        itemLabel.setFontScale(1.5f);
        enemyLabel.setFontScale(1.5f);
        playerLabel.setFontScale(1.5f);
        itemLabel.setAlignment(Align.center);
        enemyLabel.setAlignment(Align.center);
        playerLabel.setAlignment(Align.center);

        // Create tables for each button to simulate text over image
        Table itemTable = new Table();
        itemTable.add(itemBG).padBottom(-itemLabel.getHeight()); // Add image and adjust padding to overlay label
        itemTable.row();
        itemTable.add(itemLabel).center().padTop(-itemBG.getHeight() / 2f); // Overlay label on image

        Table enemyTable = new Table();
        enemyTable.add(enemyBG).padBottom(-enemyLabel.getHeight());
        enemyTable.row();
        enemyTable.add(enemyLabel).center().padTop(-enemyBG.getHeight() / 2f);

        Table playerTable = new Table();
        playerTable.add(playerBG).padBottom(-playerLabel.getHeight());
        playerTable.row();
        playerTable.add(playerLabel).center().padTop(-playerBG.getHeight() / 2f);

        // Add the button tables to the main table
        tabButtonTable.left().top();
        tabButtonTable.add(itemTable).left().padRight(10);
        tabButtonTable.add(enemyTable).left().padRight(10);
        tabButtonTable.add(playerTable).left();

        // Add event listeners to simulate button clicks using ClickListener
        itemTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemsTable.setVisible(true);
                enemiesTable.setVisible(false);
                achievementsTable.setVisible(false);
                updateHoverEffect(itemTable);  // Update hover effect
            }
        });

        enemyTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(true);
                achievementsTable.setVisible(false);
                updateHoverEffect(enemyTable);
            }
        });

        playerTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(false);
                achievementsTable.setVisible(true);
                updateHoverEffect(playerTable);
            }
        });

        // Add elevation effects to the tables
        addTableElevationEffect(itemTable);
        addTableElevationEffect(enemyTable);
        addTableElevationEffect(playerTable);

        // Set the initial hover effect
        updateHoverEffect(itemTable);

        return tabButtonTable;
    }


    /**
     * Creates the Table for the visual representation of end game statistics.
     * @return The Table showing stats.
     */
    private Table makeLogbookTable(Stat.StatType type) {
        Table table = new Table();
        table.top().pad(10);  // Adds padding around the top of the table
        table.padLeft(200);  // Adds padding around the left of the table

        // Create and add the title label at the top of the table
        String modifiedType = type.name().charAt(0) + type.name().substring(1).toLowerCase();
        Label titleLabel = new Label(modifiedType + " Statistics", skin);
        titleLabel.setFontScale(2.0f); // Set the font size
        table.add(titleLabel).colspan(4).padLeft(-100).padBottom(35).padTop(35);  // Spanning across both columns and adding some padding below
        table.row();
        int count = 0;

        // Log two stats per row with a line separator between them
        for (Stat stat : stats) {
            // Log 2 stats per row
            logger.info("stat is: {}", stat);
            if (stat.getCurrent() != 0 && stat.getType() == type) {
                // Create labels to display stat information
                Label statNameLabel = new Label(String.valueOf(stat.getStatDescription()), skin);
                Label statCurrentLabel = new Label(String.valueOf(stat.getCurrent()), skin);

                // Style the labels
                statNameLabel.setFontScale(1.5f);
                statCurrentLabel.setFontScale(1.5f);

                // Add stat labels to the table
                table.add(statNameLabel).uniform().pad(10).padBottom(10);
                table.add(statCurrentLabel).uniform().pad(10).padBottom(20);
                count++;

                if (count % 2 == 0) {
                    logger.info("count is even");
                    table.row();
                    // Add a physical line as a separator
                    String statSeparator = "--------------------------------------------------------------------------------------------------";
                    Label separator = new Label(statSeparator, skin);
                    separator.setFontScale(1.2f); // Adjust line thickness/size
                    separator.setAlignment(Align.center);
                    table.add(separator).colspan(4).padLeft(-125);  // Spanning across both columns
                    table.row();
                }
            }
        }
        return table;
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
    private void addTableElevationEffect(Table table) {
        table.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                table.addAction(Actions.parallel(
                        Actions.moveBy(0, 5, 0.1f),
                        Actions.scaleTo(1.05f, 1.05f, 0.1f)
                ));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                table.addAction(Actions.parallel(
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
