package com.csse3200.game.overlays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.screens.MainGameScreen;
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
    private static final String BUTTONTEXTURE = "images/PauseOverlay/Button.png";
    private MainGameScreen mainGameScreen;

    public PauseDisplay(MainGameScreen mainGameScreen) {
     super();
     this.mainGameScreen = mainGameScreen;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        // Title label
        Label title = new Label("Attack On Animals", skin, "title");
        Image titleBackGround = new Image(ServiceLocator.getResourceService().getAsset("images/PauseOverlay/TitleBG.png", Texture.class));
        // Create tables
        Table menuBtns = makeMenuBtns();
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
        TextButton resumeBtn = new TextButton("Resume", skin);
        TextButton questsBtn = new TextButton("Quest Tracker", skin);
        TextButton mainMenuBtn = new TextButton("Return to Main Menu", skin);

        // Add listeners for buttons
        resumeBtn.addListener(new ChangeListener() {
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

        // Triggers an event when the button is pressed.
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        eventService.getGlobalEventHandler().trigger("exit");
                    }
                });

        // Layout buttons in a table
        Table table = new Table();
        Actor[] actors = {questsBtn, resumeBtn, mainMenuBtn};
        for ( Actor button : actors){
            Image buttonBackground = new Image(
                    ServiceLocator.getResourceService()
                            .getAsset(BUTTONTEXTURE, Texture.class));
            table.add(buttonBackground).size(buttonBackground.getWidth() * 0.75f, buttonBackground.getHeight() * 0.75f).center();
            table.row();
            table.add(button).center().padTop(-buttonBackground.getHeight()*0.75f);
            table.row().padTop(10f);
        }

        return table;
    }

    private void exitOverlay() {
        mainGameScreen.removeOverlay();
    }

    private void openQuests() {
        mainGameScreen.addOverlay(Overlay.OverlayType.QUEST_OVERLAY);
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
