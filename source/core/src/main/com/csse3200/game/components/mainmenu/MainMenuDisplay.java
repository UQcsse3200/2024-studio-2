package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.AnimalSelectionScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A UI component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private final GdxGame game;

    public MainMenuDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        // Load the title image
        Image title = new Image(
                ServiceLocator.getResourceService()
                        .getAsset("images/box_boy_title.png", Texture.class));

        // Create buttons
        TextButton startBtn = new TextButton("Start", skin);
        TextButton loadBtn = new TextButton("Load", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton animalSelectionBtn = new TextButton("Select Animal", skin);

        // Button listeners
        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Start button clicked");
                entity.getEvents().trigger("start");
            }
        });

        loadBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Load button clicked");
                entity.getEvents().trigger("load");
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Settings button clicked");
                entity.getEvents().trigger("settings");
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Exit button clicked");
                entity.getEvents().trigger("exit");
            }
        });

        animalSelectionBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Animal Selection button clicked");
                game.setScreen(new AnimalSelectionScreen(game));
            }
        });

        // Arrange buttons in the table
        table.add(title).padBottom(20f);
        table.row();
        table.add(startBtn).padTop(30f);
        table.row();
        table.add(loadBtn).padTop(15f);
        table.row();
        table.add(settingsBtn).padTop(15f);
        table.row();
        table.add(exitBtn).padTop(15f);
        table.row();
        table.add(animalSelectionBtn).padTop(15f);

        // Add the table to the stage
        stage.addActor(table);
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }
}
