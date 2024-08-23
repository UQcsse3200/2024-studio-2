package com.csse3200.game.Overlays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class QuestDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(QuestDisplay.class);
    EventService eventService = ServiceLocator.getEventService();

    private Table rootTable;

    public QuestDisplay() {
        super();
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        Label title = new Label("Quests", skin, "title");
        Image questsBackGround = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/QuestsOverlay/QuestsBG.png", Texture.class));

// Create tables for buttons and quests
        Table menuBtns = makeMenuBtns();
        Table quests = makeSliders();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(questsBackGround).center();
        rootTable.row();
        rootTable.add(title).center().padTop(-questsBackGround.getHeight()*1.1f);
        rootTable.row().padTop(10f);
        rootTable.add(quests).center();
        rootTable.row().padTop(10f);
        rootTable.add(menuBtns).center();

        // Add the root table to the stage
        stage.addActor(rootTable);
    }

    private Table makeSliders() {
        Table table = new Table();
        ProgressBar progressBarTest = new ProgressBar(0,2,1,false,skin);
        Label titleTest = new Label("Test Quest", skin,"title", Color.BLACK);
        table.add(titleTest);
        table.add(progressBarTest).padRight(titleTest.getWidth());
        return table;
    }

    private Table makeMenuBtns() {
        // Create buttons
        TextButton exitBtn = new TextButton("Exit", skin);

        // Add listeners for button
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Exit button clicked");
                exitMenu();
            }
        });

        // Layout buttons in a table
        Table table = new Table();
        table.add(exitBtn).expandX().left().pad(15f, 0f, 0f, 15f);

        return table;
    }

    private void exitMenu() {
        eventService.globalEventHandler.trigger("removeOverlay");
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
        rootTable.clear();
        super.dispose();
    }
}
