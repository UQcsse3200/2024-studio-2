package com.csse3200.game.overlays;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.stats.Stat;
import com.csse3200.game.components.stats.StatManager;
import com.csse3200.game.components.quests.QuestManager;
import com.csse3200.game.components.quests.QuestPopup;
import com.csse3200.game.components.lootboxview.LootBoxOverlayComponent;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerStatsDisplay extends UIComponent {
    private Table backgroundTable;
    private Table rootTable;
    private PausableScreen screen;
    private String spritePath;
    private String playerDescription;

    public PlayerStatsDisplay(PausableScreen screen, String spritePath, String playerDescription) {
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
            addStatRow(table, "Experience", stats.getExperience());
        }

        return table;
    }

    private void addStatRow(Table table, String name, int value, int maxValue) {
        Label nameLabel = new Label(name, skin, "default");
        Label valueLabel = new Label(value + "/" + maxValue, skin, "default");
        table.add(nameLabel).expandX().left();
        table.add(valueLabel).expandX().right();
        table.row().padTop(5f);
    }

    private void addStatRow(Table table, String name, int value) {
        Label nameLabel = new Label(name, skin, "default");
        Label valueLabel = new Label(String.valueOf(value), skin, "default");
        table.add(nameLabel).expandX().left();
        table.add(valueLabel).expandX().right();
        table.row().padTop(5f);
    }

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

    private void exitMenu() {
        screen.removeOverlay();
    }

    private void addActors() {
        // Load the background image
        Image statsBackground = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/QuestsOverlay/Quest_SBG.png", Texture.class));

        float backgroundWidth = 3;
        float backgroundHeight = 6;
        statsBackground.setSize(backgroundWidth, backgroundHeight);

        // Create the title and description labels
        Label title = new Label("PLAYER STATS", skin, "title");
        title.setColor(Color.RED);
        title.setFontScale(1.2f);

        Image playerSprite = new Image(ServiceLocator.getResourceService().getAsset(spritePath, Texture.class));
        Label description = new Label(playerDescription, skin, "default");

        // Create the background table and set the background image
        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(statsBackground).expand().fill();
        stage.addActor(backgroundTable);

        // Create the stats and menu tables
        Table statsTable = makeStatsTable();
        Table menuBtns = makeMenuBtns();

        rootTable = new Table();
        rootTable.setSize(backgroundTable.getWidth(), backgroundTable.getHeight());
        rootTable.setFillParent(true);

        // Add components to the root table
        rootTable.add(playerSprite).left().padTop(20f).padLeft(20f);
        rootTable.add(description).left().padTop(20f).padLeft(10f);
        rootTable.row().padTop(10f);
        rootTable.add(statsTable).padBottom(10f).padTop(10f).colspan(2);
        rootTable.row();
        rootTable.add(menuBtns).center().padTop(10f);

        // Add the root table to the stage
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
