package com.csse3200.game.components.combat.quicktimeevents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickTimeEventDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(QuickTimeEventDisplay.class);
    private Table table;
    private Label counter;
    private Texture backgroundTexture = new Texture("images/BackgroundSplashBasic.png");

    @Override
    public void create() {
        super.create();
        logger.info("Creating QuickTimeEventDisplay");
        addActors();
    }

    private void addActors() {
        table = new Table(skin);
        Texture tableTexture = new Texture(
                Gdx.files.internal("images/quicktimeevents/white_background.png")
        );
        table.setBackground(new TextureRegionDrawable(tableTexture));
        table.setFillParent(true);

        TextButton startButton = new TextButton("Start", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add start button listener
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("start button clicked");
                entity.getEvents().trigger("start", counter);
            }
        });

        // Add exit button listener
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("exit button clicked");
                entity.getEvents().trigger("exit");
            }
        });

        // first row
        table.add().width(160f);
        table.add().width(160f);
        counter = new Label(new StringBuffer(), skin);
        counter.setFontScale(3.0f);
        counter.setAlignment(Align.center);
        table.add(counter).width(500f).expand();
        table.add().width(160f);
        table.add().width(160f);

        // second row
        table.row();
        addRowFill(3);
        addButtonToTable(startButton);
        addButtonToTable(exitButton).padLeft(5f).padRight(75f);

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch = new SpriteBatch();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    /**
     * Adds a button to the table and resizes it
     *
     * @param button the button to add
     * @return a reference to the cell occupied by the button
     *         in the table
     */
    private Cell addButtonToTable(TextButton button) {
        return table.add(button).height(45f).width(90f).padBottom(60f);
    }

    /**
     * Fills the row with empty space
     *
     * @param numCols the number of cols to fill
     */
    private void addRowFill(int numCols) {
        for (int i = 0; i < numCols; i++) {
            table.add();
        }
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

}
