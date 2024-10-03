package com.csse3200.game.components.loading;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A UI component for displaying the loading screen with a moon representing the loading progress.
 */
public class LoadingDisplay extends UIComponent {
    private static final float Z_INDEX = 2f;
    private static final float LOADING_DURATION = 6f;
    private static final float MESSAGE_INTERVAL = 2f;
    private Table table;
    private Label loadingLabel;

    private final List<String> loadingMessages = new LinkedList<>(List.of(
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
            "Training the mini-bosses...  they will be fierce!",
            "Sharpening claws and minds... almost there!",
            "Preparing animal battle strategies... stay wild!",
            "Hopping into the next frame... almost loaded!",
            "Flying with the flock... almost there!",
            "Flipping through the animal rulebook... loading!",
            "Spinning webs of code... just a moment!",
            "Ready to unleash the wild... almost there!",
            "Stampeding towards adventure... loading complete soon!",
            "Snoozing under a tree... game will wake up soon!"
    ));

    private List<String> shuffledMessages;
    private float elapsedTime;
    private float messageElapsedTime = 0;
    private String currentMessage;
    private int currentMessageIndex;

    private MoonActor moonActor;  // Moon actor to represent loading progress

    public LoadingDisplay() {
        elapsedTime = 0;
        shuffleMessages();  // Shuffle the messages at the beginning
        currentMessageIndex = 0;
        currentMessage = shuffledMessages.get(currentMessageIndex);  // Start with the first message in the shuffled list
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        // Create and add the moon actor
        moonActor = new MoonActor();
        moonActor.setOpacity(0.7f);  // Set the opacity to blend into the background
        stage.addActor(moonActor);
        table = new Table();
        table.setFillParent(true);

        loadingLabel = new Label(currentMessage, skin);
        table.add(loadingLabel).expandX().padTop(50);
        table.row();

        stage.addActor(table);  // Add the table to the stage
    }

    /**
     * Shuffles the list of messages to ensure random order.
     */
    private void shuffleMessages() {
        shuffledMessages = new LinkedList<>(loadingMessages);
        Collections.shuffle(shuffledMessages);  // Randomly shuffle the messages
    }

    @Override
    public void update() {
        super.update();
        float deltaTime = ServiceLocator.getTimeSource().getDeltaTime();
        elapsedTime += deltaTime;
        messageElapsedTime += deltaTime;

        // Update loading messages at regular intervals
        if (messageElapsedTime >= MESSAGE_INTERVAL) {
            currentMessageIndex++;

            // If we've reached the end of the shuffled list, reshuffle
            if (currentMessageIndex >= shuffledMessages.size()) {
                shuffleMessages();
                currentMessageIndex = 0;
            }

            currentMessage = shuffledMessages.get(currentMessageIndex);
            loadingLabel.setText(currentMessage);
            messageElapsedTime = 0;
        }

        // Update the progress
        float progress = Math.min(elapsedTime / LOADING_DURATION, ServiceLocator.getResourceService().getProgress() / 100f);
        moonActor.setProgress(progress);  // Update moon actor progress
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    public boolean isLoadingFinished() {
        return elapsedTime >= LOADING_DURATION;
    }

    public MoonActor getMoonActor() {
        return moonActor;
    }

    public List<String> getAllMessages() {
        return loadingMessages;
    }

    public String getCurrentMessage() {
        return currentMessage;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // No custom draw logic needed
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
