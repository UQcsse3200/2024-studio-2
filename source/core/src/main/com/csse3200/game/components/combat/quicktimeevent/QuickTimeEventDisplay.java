package com.csse3200.game.components.combat.quicktimeevent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;
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
    private static final float QTE_ORIGIN_X = IMG_SIZE/2;
    private static final float QTE_ORIGIN_Y = IMG_SIZE/3;
    private static final float QTE_START_SIZE = 1.8f;
    private static final float QTE_START_ROT = -80f;
    private static final float LABEL_WIDTH = 500f;
    private static final float BTN_WIDTH = 90f;
    private static final float BTN_HEIGHT = 45f;

    // actors
    private Table table;
    private Label label;
    private ImageButton target;
    private Image qte;

    // flags
    private boolean quickTimeEventActive = false;

    // assets
    private final Texture backgroundTexture = new Texture("images/BackgroundSplashBasic.png");
    private final Texture tableTexture = new Texture("images/quicktimeevent/white_background.png");
    private final TextureAtlas pawsAtlas = new TextureAtlas("images/quicktimeevent/paws.atlas");

    @Override
    public void create() {
        super.create();
        logger.info("Creating QuickTimeEventDisplay");
        entity.getEvents().addListener("editLabel", this::onEditLabel);
        entity.getEvents().addListener("startQuickTime", this::onStartQuickTime);
        entity.getEvents().addListener("quickTimeBtnPress", this::onQuickTimeBtnPress);
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
                resetDisplay();
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
        Group group = new Group();
        // Set up the target (for the quick-time event)
        target = new ImageButton(skin);
        target.setDisabled(true);
        target.setSize(IMG_SIZE, IMG_SIZE);
        ImageButtonStyle buttonStyle = new ImageButtonStyle();
        target.setStyle(buttonStyle);
        setTargetImage("target_default");
        group.addActor(target);
        // Set up the quick-time event
        qte = new Image(pawsAtlas.findRegion("qte"));
        qte.setSize(IMG_SIZE, IMG_SIZE);
        qte.setOrigin(QTE_ORIGIN_X, QTE_ORIGIN_Y);
        qte.setVisible(false); // Start as not visible
        group.addActor(qte);
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
     * Sets image on target
     *
     * @param name the name of the image (in the atlas)
     */
    private void setTargetImage(String name) {
        target.getStyle().imageUp = new TextureRegionDrawable(pawsAtlas.findRegion(name));
    }

    /**
     * Edits the text in the label actor
     *
     * @param text the text to display in the label
     */
    private void onEditLabel(String text) {
        this.label.setText(new StringBuffer(text));
    }

    /**
     * Animates the quick-time events
     *
     * @param quickTimeEvents the quick-time event data
     */
    private void onStartQuickTime(QuickTimeEvent[] quickTimeEvents) {
        // process and set up the quick-time events
        SequenceAction quickTimeActions = new SequenceAction();
        int counter = quickTimeEvents.length - 1;
        for (QuickTimeEvent quickTimeEvent : quickTimeEvents) {
            Action quickTimeAction;
            if (counter != 0) {
                quickTimeAction = createQuickTimeAction(quickTimeEvent, false);
            } else {
                // reached last quick-time event
                quickTimeAction = createQuickTimeAction(quickTimeEvent, true);
            }
            quickTimeActions.addAction(quickTimeAction);
            counter -= 1;
        }
        qte.addAction(quickTimeActions);
    }

    /**
     * Create quick-time event action
     *
     * @param quickTimeEvent quick-time event data
     * @param last flag for last quick-time event in the sequence
     */
    private Action createQuickTimeAction(QuickTimeEvent quickTimeEvent, boolean last) {
        // Get quick-time event data
        float duration = quickTimeEvent.duration();
        float delay = quickTimeEvent.delay();
        // create action to initialise quick-time event
        RunnableAction initAction = new RunnableAction();
        initAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                qte.setScale(QTE_START_SIZE);
                qte.setRotation(QTE_START_ROT);
                qte.setVisible(true);
                setTargetImage("target_default");
                quickTimeEventActive = true;
            }
        });
        // create action to animate quick-time event
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setDuration(duration);
        scaleToAction.setScale(1.0f);
        RotateByAction rotateByAction = new RotateByAction();
        rotateByAction.setDuration(duration);
        rotateByAction.setAmount(-QTE_START_ROT);
        Action coreAction = new ParallelAction(scaleToAction, rotateByAction);
        // create action to toggle quick-time event flag off
        RunnableAction toggleFlagOffAction = new RunnableAction();
        toggleFlagOffAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                quickTimeEventActive = false;
            }
        });
        // create action to add some delay
        DelayAction delayAction = new DelayAction();
        delayAction.setDuration(delay);
        // sequence actions together
        SequenceAction actions = new SequenceAction(
                initAction, coreAction, toggleFlagOffAction, delayAction
        );
        if (last) {
            // last quick-time event - do some additional clean up
            RunnableAction resetAction = new RunnableAction();
            resetAction.setRunnable(new Runnable() {
                @Override
                public void run() {
                    qte.setVisible(false);
                    setTargetImage("target_default");
                }
            });
            actions.addAction(resetAction);
        }
        return actions;
    }

    /**
     * Handle quick-time button press
     */
    private void onQuickTimeBtnPress() {}

    /**
     * Forcefully reset the quick-time event display
     */
    private void resetDisplay() {
        quickTimeEventActive = false;
        qte.setVisible(false);
        qte.clearActions();
        setTargetImage("target_default");
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
