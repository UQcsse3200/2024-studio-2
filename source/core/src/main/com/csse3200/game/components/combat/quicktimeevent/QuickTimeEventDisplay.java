package com.csse3200.game.components.combat.quicktimeevent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickTimeEventDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(QuickTimeEventDisplay.class);

    // sizes and dimensions
    private static final float COL_WIDTH = 160f;
    private static final float IMG_SIZE = 300f;
    private static final float LABEL_WIDTH = 500f;
    private static final float BTN_WIDTH = 90f;
    private static final float BTN_HEIGHT = 45f;

    // actors
    private Table table;
    private Label label;
    private Image target;
    private Image qteImage;

    // assets
    private Texture backgroundTexture = new Texture("images/BackgroundSplashBasic.png");
    private Texture tableTexture = new Texture("images/quicktimeevent/white_background.png");
    private TextureAtlas pawsAtlas = new TextureAtlas("images/quicktimeevent/paws.atlas");

    @Override
    public void create() {
        super.create();
        logger.info("Creating QuickTimeEventDisplay");
        entity.getEvents().addListener("editLabel", this::onEditLabel);
        addActors();
    }

    private void addActors() {
        table = new Table(skin);
        table.setBackground(new TextureRegionDrawable(tableTexture));
        table.setFillParent(true);

        TextButton startButton = new TextButton("Start", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add start button listener
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.debug("start button clicked");
                entity.getEvents().trigger("start");
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

        // first row (quick-time event images)
        table.add().width(COL_WIDTH);
        table.add().width(COL_WIDTH);
        // Use a group to manage images
        Group group = new Group();
        qteImage = new Image(pawsAtlas.findRegion("default"));
        qteImage.setVisible(false); // Start as not visible
        group.addActor(qteImage);
        target = new Image(pawsAtlas.findRegion("target"));
        target.setFillParent(true);
        group.addActor(target);
        table.add(group).size(IMG_SIZE,IMG_SIZE).expand().padTop(60f);
        table.add().width(COL_WIDTH);
        table.add().width(COL_WIDTH);

        // second row (text region)
        table.row();
        padCols(2);
        label = new Label(new StringBuffer(), skin);
        label.setFontScale(3.0f);
        label.setAlignment(Align.center);
        table.add(label).width(LABEL_WIDTH);
        padCols(2);

        // third row (buttons)
        table.row();
        padCols(3);
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
        return table.add(button).width(BTN_WIDTH).height(BTN_HEIGHT).padBottom(60f);
    }

    /**
     * Pads the columns with empty space
     *
     * @param numCols the number of cols to fill
     */
    private void padCols(int numCols) {
        for (int i = 0; i < numCols; i++) {
            table.add();
        }
    }

    /**
     * Edits the text in the label actor
     *
     * @param text the text to display in the label
     */
    private void onEditLabel(String text) {
        this.label.setText(new StringBuffer(text));
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

}
