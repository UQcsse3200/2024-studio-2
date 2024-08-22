package com.csse3200.game.Overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
public class PauseDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PauseDisplay.class);
    EventService eventService = ServiceLocator.getEventService();
    private Table rootTable;

    public PauseDisplay() {
        super();
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        // Title label
        Label title = new Label("Attack On Animals", skin, "title");
        Image titleBackGround =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/PauseOverlay/TitleBG.png", Texture.class));
        // Create tables
        Table menuBtns = makeMenuBtns();
        //titleBackGround.setScale(0.75f);
        // Root table that holds everything
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(titleBackGround).size(titleBackGround.getWidth() * 0.75f, titleBackGround.getHeight() * 0.75f).center();
        rootTable.row();
        rootTable.add(title).center().padTop(-titleBackGround.getHeight() * 0.75f);
        // Buttons Here
        rootTable.row().padTop(10f);
        rootTable.add(menuBtns).fillX();

        // Add root table to the stage
        stage.addActor(rootTable);
    }

    private Table makeMenuBtns() {
        // Create buttons
        TextButton exitBtn = new TextButton("Resume", skin);
        TextButton questsBtn = new TextButton("Quest Tracker", skin);
        Image button = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/PauseOverlay/Button.png", Texture.class));
        Image button2 = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/PauseOverlay/Button.png", Texture.class));

        // Add listeners for buttons
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Exit button clicked");
                exitOverlay();
            }
        });

        questsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Quests button clicked");
                openQuests();
            }
        });

        //button.setScale(0.75f);
        //button2.setScale(0.75f);
        // Layout buttons in a table
        Table table = new Table();
        table.add(button).size(button.getWidth() * 0.75f, button.getHeight() * 0.75f).center();
        table.row();
        table.add(questsBtn).center().padTop(-button.getHeight()*0.75f);
        table.row().padTop(10f);
        table.add(button2).size(button2.getWidth() * 0.75f, button2.getHeight() * 0.75f).center();
        table.row();
        table.add(exitBtn).center().padTop(-button.getHeight()*0.75f);

        return table;
    }

    private void exitOverlay() {
        eventService.globalEventHandler.trigger("removeOverlay");
    }

    private void openQuests() {
        eventService.globalEventHandler.trigger("addOverlay",Overlay.OverlayType.QUEST_OVERLAY);
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
