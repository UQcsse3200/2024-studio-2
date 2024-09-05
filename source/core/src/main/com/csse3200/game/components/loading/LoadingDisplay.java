package com.csse3200.game.components.loading;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.settingsmenu.UserSettings;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * A UI component for displaying the Main menu.
 */
public class LoadingDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(LoadingDisplay.class);
    private static final float Z_INDEX = 2f;
    private static final float LOADING_DURATION = 15f;
    private static final float MESSAGE_INTERVAL = 3f;
    private Table table;
    public ProgressBar progressBar;
    private Label loadingLabel;
    private String[] loadingMessages = {
            "Fetching the best bones... please wait!",
            "The jungle is waking up... loading soon!",
            "Watering the game plants... almost done!",
            "Wagging tails, flapping wings, hang tight!",
            "Gathering the animal council... loading peace!",
            "Tracking animal footprints... loading your path!",
            "Lions and tigers and bears, oh my! Loading...",
            "Unleashing wild adventures… just a moment!",
            "Just a few more feathers to ruffle... loading!",
            "Brushing the fur... we will be ready soon!",
            "Training the mini-bosses… they will be fierce!",
            "Sharpening claws and minds... almost there!",
            "Preparing animal battle strategies... stay wild!",
            "Hopping into the next frame... almost loaded!",
            "Flying with the flock... almost there!",
            "Flipping through the animal rulebook... loading!",
            "Spinning webs of code... just a moment!",
            "Ready to unleash the wild... almost there!",
            "Stampeding towards adventure... loading complete soon!",
            "Snoozing under a tree... game will wake up soon!"
    };
    private float elapsedTime = 0;
    private float messageElapsedTime = 0;
    private String currentMessage;
    private int currentMessageIndex;

    private float progress;
    public LoadingDisplay() {
//        progress = 0;
        progressBar = new ProgressBar(0, 1, 0.01f, false, skin);
        progressBar.setValue(0);
        elapsedTime = 0;
        currentMessageIndex = (int) (Math.random() * loadingMessages.length);
        currentMessage = loadingMessages[currentMessageIndex];
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        loadingLabel = new Label(currentMessage, skin);

        table.add(loadingLabel).expandX().padTop(50);
        table.row();
        table.add(progressBar).width(300).padTop(20);

        stage.addActor(table);
    }

    public boolean isLoadingFinished() {
        return progressBar.getValue() >= 1;
    }

    @Override
    public void update() {
        super.update();
        float deltaTime = ServiceLocator.getTimeSource().getDeltaTime();
        elapsedTime += deltaTime;
        messageElapsedTime += deltaTime;

        if (messageElapsedTime >= MESSAGE_INTERVAL) {
            int newMessageIndex;
            do {
                newMessageIndex = (int) (Math.random() * loadingMessages.length);
            } while (newMessageIndex == currentMessageIndex);

            currentMessageIndex = newMessageIndex;
            currentMessage = loadingMessages[currentMessageIndex];
            loadingLabel.setText(currentMessage);
            messageElapsedTime = 0;
        }

        float progress = Math.min(elapsedTime / LOADING_DURATION, ServiceLocator.getResourceService().getProgress() / 100f);
        progressBar.setValue(progress);


    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
