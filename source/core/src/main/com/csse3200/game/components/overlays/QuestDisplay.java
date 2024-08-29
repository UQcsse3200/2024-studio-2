package com.csse3200.game.components.overlays;

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
        // Title label
        Label title = new Label("Quests", skin, "title");

        // Create tables
        Table menuBtns = makeMenuBtns();

        // Root table that holds everything
        rootTable = new Table();
        rootTable.setFillParent(true);

        // Add title at the top, centered horizontally
        rootTable.add(title).expandX().top().padTop(20f);

        // Create another row for the menu buttons
        rootTable.row();
        rootTable.add(menuBtns).fillX();

        // Add root table to the stage
        stage.addActor(rootTable);
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
        eventService.getGlobalEventHandler().trigger("removeOverlay");
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
