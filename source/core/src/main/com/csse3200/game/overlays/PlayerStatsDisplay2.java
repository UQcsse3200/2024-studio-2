package com.csse3200.game.overlays;

import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerStatsDisplay2 extends UIComponent {
    private Table backgroundTable;
    private Table rootTable;
    private PausableScreen screen;
    private String spritePath;
    private String playerDescription;
    private static final String DEFAULT_TEXT = "default"; // Makes SonarCloud happy

    public PlayerStatsDisplay2(PausableScreen screen, String spritePath, String playerDescription) {
        super();
        this.screen = screen;
        this.spritePath = spritePath;
        this.playerDescription = playerDescription;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private Table makeStatsTable() {
        Table table = new Table();
        CombatStatsComponent stats = (CombatStatsComponent) ServiceLocator.getEntityService().getSpecificComponent(CombatStatsComponent.class);

        if (stats != null) {
            addStatRow(table, "Health", stats.getHealth(), stats.getMaxHealth());
            addStatRow(table, "Hunger", stats.getHunger(), stats.getMaxHunger());
            addStatRow(table, "Strength", stats.getStrength());
            addStatRow(table, "Defense", stats.getDefense());
            addStatRow(table, "Speed", stats.getSpeed());
            addStatRow(table, "Experience", stats.getExperience(), stats.getMaxExperience()); // Show experience as current/max
            addStatRow(table, "Level", stats.getLevel(), 10); // Show level as current/10
        }

        return table;
    }


    private void addStatRow(Table table, String name, int value, int maxValue) {
        Label nameLabel = new Label(name, skin, DEFAULT_TEXT);
        Label valueLabel = new Label(value + "/" + maxValue, skin, DEFAULT_TEXT);
        table.add(nameLabel).expandX().left();
        table.add(valueLabel).expandX().right().padLeft(25f);
        table.row().padTop(5f);
    }

    private void addStatRow(Table table, String name, int value) {
        Label nameLabel = new Label(name, skin, DEFAULT_TEXT);
        Label valueLabel = new Label(String.valueOf(value), skin, DEFAULT_TEXT);
        table.add(nameLabel).expandX().left();
        table.add(valueLabel).expandX().right().padLeft(25f);
        table.row().padTop(5f);
    }

    private void addStatRow(Table table, String name, float value) {
        Label nameLabel = new Label(name, skin, DEFAULT_TEXT);
        Label valueLabel = new Label(String.valueOf(value), skin, DEFAULT_TEXT);
        table.add(nameLabel).expandX().left();
        table.add(valueLabel).expandX().right().padLeft(25f);
        table.row().padTop(5f);
    }

    private Table makeMenuBtns() {
        CustomButton exitBtn = new CustomButton("Leave Menu", skin);
        //exitBtn.getLabel().setFontScale(0.8f);

        exitBtn.addClickListener(() -> {
                exitMenu();
        });

        Table table = new Table();
        table.add(exitBtn).size(250, 50).expandX().center().padTop(10f);
        return table;
    }

    private void exitMenu() {
        screen.removeOverlay();
    }

    private void addActors() {
        // Load the background image
        Image statsBackground = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/QuestsOverlay/Quest_SBG.png", Texture.class));

        // Create the title and description labels
        Label title = new Label("PLAYER STATS", skin, "title");
        title.setColor(Color.RED);
        title.setFontScale(1.2f);

        Image playerSprite = new Image(ServiceLocator.getResourceService().getAsset(spritePath, Texture.class));
        Label description = new Label(playerDescription, skin, DEFAULT_TEXT);

        // Create the background table and set the background image
        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(statsBackground).expand().fill();
        backgroundTable.center();

        stage.addActor(backgroundTable);

        // Create the stats and menu tables
        Table statsTable = makeStatsTable();
        Table menuBtns = makeMenuBtns();

        // Create a content table to hold all elements
        Table contentTable = new Table();
        contentTable.setSize(300, 100); // Set the size of the content table
        contentTable.center(); // Center the content table

        // Add components to the content table
        contentTable.add(title).padTop(10f).colspan(2).center(); // Title at the top
        contentTable.row().padTop(10f);
        contentTable.add(playerSprite).left().padTop(20f).padLeft(20f);
        contentTable.add(description).left().padTop(20f).padLeft(10f);
        contentTable.row().padTop(10f);
        contentTable.add(statsTable).padBottom(10f).padTop(10f).colspan(2);
        contentTable.row();
        contentTable.add(menuBtns).center().padTop(10f).colspan(2); // Center menu buttons

        // Create the root table
        rootTable = new Table();
        rootTable.setFillParent(true); // Make the rootTable fill the stage
        rootTable.add(contentTable).expand().center(); // Center contentTable in rootTable

        // Finally, add the root table to the stage
        stage.addActor(rootTable);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        backgroundTable.clear();
        rootTable.clear();
        super.dispose();
    }
}
