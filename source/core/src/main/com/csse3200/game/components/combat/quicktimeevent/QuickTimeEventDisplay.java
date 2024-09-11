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
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickTimeEventDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(QuickTimeEventDisplay.class);
    private GameTime gameTime;
    
    // sizes and dimensions
    private static final float COL_WIDTH = 160f;
    private static final float IMG_SIZE = 300f;
    private static final float QTE_ORIGIN_X = IMG_SIZE/2;
    private static final float QTE_ORIGIN_Y = IMG_SIZE/3;
    private static final float QTE_START_SCALE = 1.8f;
    private static final float QTE_START_ROT = -80f;
    private static final float LABEL_WIDTH = 500f;
    private static final float BTN_WIDTH = 90f;
    private static final float BTN_HEIGHT = 45f;

    // actors
    private Table table;
    private Label label;
    private ImageButton target;
    private Image qte;

    // quick-time event constants and variables
    private static final long QUICK_TIME_WINDOW = 40; // milli-secs
    private boolean qteActive = false;
    private boolean isDefault = true;
    private long startTime;
    private long windowStartTime;

    // score
    private int maxScore;
    private int score;

    // assets
    private final Texture backgroundTexture = new Texture("images/BackgroundSplashBasic.png");
    private final Texture tableTexture = new Texture("images/quicktimeevent/white_background.png");
    private final TextureAtlas pawsAtlas = new TextureAtlas("images/quicktimeevent/paws.atlas");

    // sounds
    private final String victorySound = "sounds/victory.mp3";

    @Override
    public void create() {
        super.create();
        logger.info("Creating QuickTimeEventDisplay");
        gameTime = ServiceLocator.getTimeSource();
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
        isDefault = name.equals("target_default"); // set flag
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
     * Increments the score and updates the display
     */
    private void incScore() {
        if (score < maxScore) {
            score++; // increment score
        }
        updateDisplayScore();
    }

    /**
     * Updates the score display
     */
    private void updateDisplayScore() {
        onEditLabel(String.format("%d/%d", score, maxScore));
    }

    /**
     * Animates the quick-time events
     *
     * @param quickTimeEvents the quick-time event data
     */
    private void onStartQuickTime(QuickTimeEvent[] quickTimeEvents) {
        // set up the displayed score
        int numEvents = quickTimeEvents.length;
        score = 0;
        maxScore = numEvents;
        updateDisplayScore();
        // process and set up the quick-time events
        SequenceAction quickTimeActions = new SequenceAction();
        int counter = numEvents - 1;
        for (QuickTimeEvent quickTimeEvent : quickTimeEvents) {
            Action quickTimeAction = createAction(quickTimeEvent, counter==0);
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
     *             
     * @returns action representing the quick-time event
     */
    private Action createAction(QuickTimeEvent quickTimeEvent, boolean last) {
        // Get quick-time event data
        float duration = quickTimeEvent.duration();
        float delay = quickTimeEvent.delay();
        // convert duration to ms
        long durationMS = (long) (1000*duration);
        // initialise quick-time event
        RunnableAction initAction = new RunnableAction();
        initAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                logger.debug("Setting up quick-time event");
                qte.setScale(QTE_START_SCALE);
                qte.setRotation(QTE_START_ROT);
                qte.setVisible(true);
                setTargetImage("target_default");
                startTime = gameTime.getTime();
                windowStartTime = startTime + durationMS - QUICK_TIME_WINDOW;
                qteActive = true;
            }
        });
        // animate quick-time event
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setDuration(duration);
        scaleToAction.setScale(1.0f);
        RotateByAction rotateByAction = new RotateByAction();
        rotateByAction.setDuration(duration);
        rotateByAction.setAmount(-QTE_START_ROT);
        Action coreAction = new ParallelAction(scaleToAction, rotateByAction);
        // add small delay for input
        DelayAction smallDelayAction = new DelayAction();
        smallDelayAction.setDuration(QUICK_TIME_WINDOW / 2000f);
        // handle stuff after quick-time event
        RunnableAction toggleFlagOffAction = new RunnableAction();
        toggleFlagOffAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                logger.debug("Wrapping up quick-time event");
                qteActive = false;
                if (isDefault) {
                    setTargetImage("target_slow"); // player was too slow
                }
            }
        });
        // add some delay between quick-time events
        DelayAction delayAction = new DelayAction();
        delayAction.setDuration(delay);
        // sequence actions together
        SequenceAction actions = new SequenceAction(initAction, coreAction,
                smallDelayAction, toggleFlagOffAction, delayAction);
        if (last) {
            // last event - do some additional cleaning
            RunnableAction resetAction = new RunnableAction();
            resetAction.setRunnable(new Runnable() {
                @Override
                public void run() {
                    logger.debug("Final quick-time event clean up");
                    if (score == maxScore) {
                        // perfect score - play a jingle
                        AudioManager.playSound(victorySound);
                    }
                    qte.setVisible(false);
                    setTargetImage("target_default");
                }
            });
            actions.addAction(resetAction);
        }
        return actions;
    }

    /**
     * Handle quick-time event button press
     */
    private void onQuickTimeBtnPress() {
        if (qteActive) {
            // quick-time event in process
            if (gameTime.getTime() >= windowStartTime) {
                // button press timed correctly
                setTargetImage("target_perfect");
                incScore();
            } else {
                // button press too fast
                setTargetImage("target_fast");
            }
            // de-render quick-time event animation
            qteActive = false;
            qte.setVisible(false);
        }
    }

    /**
     * Reset the quick-time event display
     */
    private void resetDisplay() {
        qteActive = false;
        qte.setVisible(false);
        qte.clearActions();
        setTargetImage("target_default");
        onEditLabel(""); // clear display
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
