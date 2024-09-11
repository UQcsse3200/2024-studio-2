package com.csse3200.game.overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A public class that represents the display and logic for managing and showing player stats onto the screen.
 * This handles the user interface components.
 */
public class PlayerStatsDisplay extends UIComponent {
    private Table background;
    private Table rootTable;
    private PausableScreen screen;

    public PlayerStatsDisplay(PausableScreen screen) {
        super();
        this.screen = screen;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Creates and returns a table with player stats.
     * @return A table containing player stats information.
     */
    private Table makeStatsTable() {
        Table table = new Table();
        CombatStatsComponent stats = (CombatStatsComponent) ServiceLocator.getEntityService().getSpecificComponent(CombatStatsComponent.class);

        if (stats != null) {
            addStatRow(table, "Health", stats.getHealth(), stats.getMaxHealth());
            addStatRow(table, "Hunger", stats.getHunger(), stats.getMaxHunger());
            addStatRow(table, "Strength", stats.getStrength());
            addStatRow(table, "Defense", stats.getDefense());
            addStatRow(table, "Speed", stats.getSpeed());
            addStatRow(table, "Experience", stats.getExperience());
        }

        return table;
    }

    /**
     * Adds a row to the stats table with the given stat name and value.
     * @param table The table to which the row will be added.
     * @param name The name of the stat.
     * @param value The value of the stat.
     * @param maxValue Optional maximum value for stats with a range (e.g., Health).
     */
    private void addStatRow(Table table, String name, int value, int maxValue) {
        Label nameLabel = new Label(name, skin, "default");
        Label valueLabel = new Label(value + "/" + maxValue, skin, "default");
        table.add(nameLabel).expandX().left();
        table.add(valueLabel).expandX().right();
        table.row().padTop(5f);
    }

    /**
     * Adds a row to the stats table with the given stat name and value.
     * @param table The table to which the row will be added.
     * @param name The name of the stat.
     * @param value The value of the stat.
     */
    private void addStatRow(Table table, String name, int value) {
        Label nameLabel = new Label(name, skin, "default");
        Label valueLabel = new Label(String.valueOf(value), skin, "default");
        table.add(nameLabel).expandX().left();
        table.add(valueLabel).expandX().right();
        table.row().padTop(5f);
    }

    /**
     * Creates and returns a table containing menu buttons for navigating the stats menu.
     * @return A table containing the menu buttons.
     */
    private Table makeMenuBtns() {
        TextButton exitBtn = new TextButton("Leave Menu", skin);
        exitBtn.getLabel().setFontScale(0.8f);

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitMenu();
            }
        });

        Table table = new Table();
        table.add(exitBtn).expandX().center().padTop(10f);
        return table;
    }

    /**
     * Handles exiting the player stats menu.
     */
    private void exitMenu() {
        screen.removeOverlay();
    }

    /**
     * Adds actors to the stage for displaying the player stats UI components.
     */
    private void addActors() {
        Label title = new Label("PLAYER STATS", skin, "title");
        title.setColor(Color.RED);
        title.setFontScale(1.2f);

        //Image statsBackground = new Image(
                //ServiceLocator.getResourceService()
                        //.getAsset("images/PlayerStatsOverlay/Stats_SBG.png", Texture.class));
        background = new Table();
        background.setFillParent(true);
        //background.add(statsBackground).center();
        stage.addActor(background);

        Table menuBtns = makeMenuBtns();
        Table statsTable = makeStatsTable();

        rootTable = new Table();
        rootTable.setSize(background.getWidth(), background.getHeight());
        rootTable.setFillParent(true);

        float paddingTop = 28f;

        rootTable.add(title).center().padTop(paddingTop);
        rootTable.row();
        rootTable.add(statsTable).padBottom(10f).padTop(paddingTop);
        rootTable.row();
        rootTable.add(menuBtns).center().padTop(10f);

        stage.addActor(rootTable);
    }

    /**
     * Draws the player stats UI onto the screen.
     * @param batch The sprite batch used for drawing.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * Updates the player stats UI based on time.
     */
    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    /**
     * Disposes of assets used by the player stats display.
     */
    @Override
    public void dispose() {
        background.clear();
        rootTable.clear();
        super.dispose();
    }
}
