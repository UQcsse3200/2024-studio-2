package com.csse3200.game.components.combat.quicktimeevent;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.AudioManager;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class QuickTimeEventDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(QuickTimeEventDisplay.class);
    private GameTime gameTime;
    private static final Map<Integer, Float> IMAGE_ROTATE = Map.of(
            Keys.W, 0f, Keys.A, 90f, Keys.S, 180f, Keys.D, 270f
    );
    
    // sizes and dimensions
    private static final float IMG_SIZE = 250f;
    private static final float QTE_ORIGIN_X = IMG_SIZE/2;
    private static final float QTE_ORIGIN_Y = IMG_SIZE/3;
    private static final float QTE_START_SCALE = 1.8f;
    private static final float QTE_START_ROT = -80f;
    private static final float LABEL_WIDTH = 500f;

    // actors
    private Table table;
    private Label label;
    private ImageButton target;
    private Image qte;

    // quick-time event constants and variables
    private static final long QUICK_TIME_WINDOW = 40; // milli-secs
    private int keyRequired;
    private boolean qteActive = false;
    private boolean isDefault = true;
    private long startTime;
    private long windowStartTime;

    // score
    private int maxScore;
    private int score;

    // assets
    private final TextureAtlas pawsAtlas =
            new TextureAtlas("images/quicktimeevent/paws.atlas");
    private final String victorySound = "sounds/victory.mp3";

    @Override
    public void create() {
        super.create();
        logger.info("Creating QuickTimeEventDisplay");
        gameTime = ServiceLocator.getTimeSource();
        entity.getEvents().addListener("editLabel", this::onEditLabel);
        entity.getEvents().addListener("startQuickTime", this::onStartQuickTime);
        addActors();
    }

    private void addActors() {
        table = new Table(skin);
        table.setFillParent(true);

        // first row (quick-time event images)
        Group group = new Group();
        // Set up the target (for the quick-time event)
        target = new ImageButton(skin);
        target.setDisabled(true);
        target.setSize(IMG_SIZE, IMG_SIZE);
        ImageButtonStyle buttonStyle = new ImageButtonStyle();
        target.setStyle(buttonStyle);
        target.getImage().setOrigin(QTE_ORIGIN_X, QTE_ORIGIN_Y);
        setTargetImage("target_default");
        group.addActor(target);
        // Set up the quick-time event
        qte = new Image(pawsAtlas.findRegion("qte"));
        qte.setSize(IMG_SIZE, IMG_SIZE);
        qte.setOrigin(QTE_ORIGIN_X, QTE_ORIGIN_Y);
        qte.setVisible(false); // Start as not visible
        group.addActor(qte);
        table.add(group).size(IMG_SIZE,IMG_SIZE).expand().padTop(60f);

        // second row (text region)
        table.row();
        label = new Label(new StringBuffer(), skin);
        label.setFontScale(3.0f);
        label.setAlignment(Align.center);
        table.add(label).width(LABEL_WIDTH);

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    /**
     * Sets image on target
     *
     * @param name the name of the image (in the atlas)
     */
    public void setTargetImage(String name) {
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
                keyRequired = quickTimeEvent.keycode();
                qte.setScale(QTE_START_SCALE);
                qte.setRotation(QTE_START_ROT + IMAGE_ROTATE.get(keyRequired));
                target.getImage().setRotation(IMAGE_ROTATE.get(keyRequired));
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
                    target.getImage().setRotation(0f);
                    setTargetImage("target_default");
                }
            });
            actions.addAction(resetAction);
        }
        return actions;
    }

    /**
     * Handle quick-time event button press
     *
     * @param keycode the key pressed by the user
     */
    private void onQuickTimeBtnPress(int keycode) {
        if (qteActive) {
            // quick-time event in process
            if (keycode == keyRequired
                    && gameTime.getTime() >= windowStartTime) {
                // correct button pressed + timed correctly
                setTargetImage("target_perfect");
                incScore();
            } else {
                // incorrect button pressed or too fast
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

    /**
     * Returns a reference to the label actor
     *
     * @return the label
     */
    public Label getLabel() {
        return this.label;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

    /**
     * Creates a demo list of four quick-time events
     * with different durations
     *
     * @returns a list of demo quick-time events
     */
    private static QuickTimeEvent[] quickTimeEventsDemo() {
        float delay = 0.2f;
        float[] durations = {0.7f, 0.65f, 0.55f, 0.45f};
        int[] directions = {Keys.W, Keys.S, Keys.S, Keys.A};
        QuickTimeEvent[] quickTimeEvents = new QuickTimeEvent[durations.length];
        for (int i = 0; i < durations.length; i++) {
            quickTimeEvents[i] = new QuickTimeEvent(durations[i], delay, directions[i]);
        }
        return quickTimeEvents;
    }
}
