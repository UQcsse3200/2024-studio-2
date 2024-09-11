package com.csse3200.game.components.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.components.stats.StatSaveManager.saveStats;

/**
 * Display detailed stats after defeating the final boss of the game.
 */
public class StatDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(StatDisplay.class);
    private final GdxGame game;
    private Table rootTable;
    /** Array to store stats. */
    private final Array<Stat> stats;
    final ImageButton[] lastPressedButton = {null};

    /**
     * Array of texture paths used in the Achievements game screen.
     */
    private static final String[] StatTextures = {"images/logbook/lb-bg.png","images/logbook/lb-yellow-tab.png",
            "images/logbook/lb-blue-tab.png", "images/logbook/lb-red-tab.png", "images/logbook/lb-blue-btn.png",
            "images/logbook/lb-red-btn.png", "images/logbook/lb-yellow-btn.png", "images/logbook/lb-blue-btn-pressed.png", "images/logbook/lb-red-btn-pressed.png", "images/logbook/lb-yellow-btn-pressed.png"};


    public StatDisplay(GdxGame game) {
        super();
        this.game = game;
        StatSaveManager statSaveManager = new StatSaveManager();
        this.stats =  statSaveManager.getStats();
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
        enemiesTable.add(new Label("Content for Tab 2", skin));

        Table achievementsTable = makeLogbookTable(Stat.StatType.ADVANCEMENT);
        achievementsTable.add(new Label("Content for Tab 3", skin));

        Table tabs = makeTabs(itemsTable, enemiesTable, achievementsTable);

        // Add content tables to the stack
        tabContentStack.add(itemsTable);
        tabContentStack.add(enemiesTable);
        tabContentStack.add(achievementsTable);

        itemsTable.setVisible(true);
        enemiesTable.setVisible(false);
        achievementsTable.setVisible(false);

        rootTable.add(tabs).fillX().row();
        rootTable.add(tabContentStack).expand().fill();
        rootTable.add(menuBtns).fillX().row();
        stage.addActor(rootTable);
    }

    void updateHoverEffect(ImageButton newButton) {
        // Hover effect actions
        Action hoverIn = Actions.moveBy(0, 5, 0.3f);  // Move up
        Action hoverOut = Actions.moveBy(0, -5, 0.3f); // Move down

        // Create a repeating action sequence
        Action repeatHover = Actions.forever(Actions.sequence(hoverIn, hoverOut));

        if (lastPressedButton[0] != null) {
            lastPressedButton[0].clearActions();
            lastPressedButton[0].setPosition(lastPressedButton[0].getX(), lastPressedButton[0].getY() - 5); // Reset Y position
            lastPressedButton[0].setScale(1f);
        }

        newButton.addAction(repeatHover); // Apply repeating hover effect to the new button
        lastPressedButton[0] = newButton; // Update the last pressed button
    }


    private Table makeTabs(Table itemsTable, Table enemiesTable, Table achievementsTable) {
        Table tabButtonTable = new Table().padLeft(50);

        Image itemBG = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-yellow-tab.png", Texture.class));
        Image enemyBG = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-red-tab.png", Texture.class));
        Image achievementBG = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/logbook/lb-blue-tab.png", Texture.class));

        ImageButton itemButton = new ImageButton(itemBG.getDrawable());
        ImageButton enemyButton = new ImageButton(enemyBG.getDrawable());
        ImageButton achievementButton = new ImageButton(achievementBG.getDrawable());

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
                updateHoverEffect(itemButton);
            }
        });

        enemyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(true);
                achievementsTable.setVisible(false);
                updateHoverEffect(enemyButton);
            }
        });

        achievementButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                itemsTable.setVisible(false);
                enemiesTable.setVisible(false);
                achievementsTable.setVisible(true);
                updateHoverEffect(achievementButton);
            }
        });
        updateHoverEffect(itemButton);
        return tabButtonTable;
    }

    /**
     * Creates the Table for the visual representation of completed stats.
     * @return The Table showing stats.
     */
    private Table makeLogbookTable(Stat.StatType type) {
        Table table = new Table().left().top().padLeft(50);
        Integer advancementCounter = 0;

        for (Stat stat : stats) {
            logger.info("stats are: {}", stats);
            logger.info("AppleCollected current value is: {}", stat.getCurrent());
            if (stat.getCurrent() != 0 && stat.getType() == type) {
                // Create a label to display stat.getCurrent() instead of an ImageButton
                Label statLabel = new Label(String.valueOf(stat.getCurrent()), skin);

                // Style or add padding to the label
                statLabel.setFontScale(1.5f);
                statLabel.setAlignment(Align.center);

                // Add stat to table
                table.add(statLabel);

                advancementCounter++;
                if (advancementCounter == 6) {
                    table.row();  // Move to a new row after 6 entries
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
        return table;
    }

    /**
     * Sets the current game screen back to the main menu.
     */
    private void exitMenu() {
        saveStats(stats);
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(new Texture("images/BackgroundSplash.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    /**
     * Clear the UI elements on the display
     */
    @Override
    public void dispose() {
        saveStats(stats);
        rootTable.clear();
        ServiceLocator.getResourceService().unloadAssets(StatTextures);
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
