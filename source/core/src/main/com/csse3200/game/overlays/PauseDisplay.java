package com.csse3200.game.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.GameState;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.screens.PausableScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.CustomButton;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class PauseDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PauseDisplay.class);
    private Table rootTable;
    private PausableScreen screen;
    private GdxGame game;


    public PauseDisplay(PausableScreen screen, GdxGame game) {
     super();
     this.screen = screen;
     this.game = game;
    }
  
    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        // Title label
        Label title = new Label(" Game Paused", skin, "title", Color.WHITE);
        Image titleBackGround = new Image(ServiceLocator.getResourceService().getAsset("images/PauseOverlay/TitleBG.png", Texture.class));
        // Create tables
        Table menuBtns = makeMenuBtns();
        // Root table that holds everything
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.add(titleBackGround).width(Gdx.graphics.getWidth() * 0.3f).height(Gdx.graphics.getHeight() * 0.1f).center();
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
        CustomButton resumeBtn = new CustomButton("Resume", skin);
        resumeBtn.setButtonStyle(CustomButton.Style.BROWN_WIDE, skin);
        CustomButton questsBtn = new CustomButton("Quest Tracker", skin);
        questsBtn.setButtonStyle(CustomButton.Style.BROWN_WIDE, skin);
        CustomButton settingsBtn = new CustomButton("Settings", skin);
        settingsBtn.setButtonStyle(CustomButton.Style.BROWN_WIDE, skin);
        CustomButton saveBtn = new CustomButton("Save Game", skin);
        saveBtn.setButtonStyle(CustomButton.Style.BROWN_WIDE, skin);
        CustomButton mainMenuBtn = new CustomButton("Return to Main Menu", skin);
        mainMenuBtn.setButtonStyle(CustomButton.Style.BROWN_WIDE, skin);

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
        settingsBtn.addListener(new ChangeListener() {  // Settings button listener
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Settings button clicked");
                openSettings();  // Call method to open settings overlay
            }
        });

        saveBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        SaveHandler.save(GameState.class, "saves", FileLoader.Location.LOCAL);
                    }
                });

        // Triggers an event when the button is pressed.
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        SaveHandler.save(GameState.class, "saves", FileLoader.Location.LOCAL);
                        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
                    }
                });


        // Layout buttons in a table
        Table table = new Table();
        Actor[] actors = {questsBtn, resumeBtn,settingsBtn, saveBtn, mainMenuBtn};
        for (Actor button : actors){
            table.row();
            table.add(button).center().width(500f).height(70f);
            table.row().padTop(10f);
        }

        return table;
    }

    private void exitOverlay() {
        screen.removeOverlay();
    }

    private void openQuests() {
        screen.addOverlay(Overlay.OverlayType.QUEST_OVERLAY);
    }

    private void openSettings() {  // New method to open settings overlay
        screen.addOverlay(Overlay.OverlayType.SETTINGS_OVERLAY);
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
