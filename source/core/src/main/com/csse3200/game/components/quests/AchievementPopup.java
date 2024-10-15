package com.csse3200.game.components.quests;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.gamestate.Achievements;
import com.csse3200.game.gamestate.SaveHandler;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;



/**
 * AchievementPopup is an UIComponent that displays whenever the player completes a new achievement and then disappears.
 */
public class AchievementPopup extends UIComponent {
    // Flag to see if popup is displaying.
    private boolean showing;
    // Image for achievement completion. */
    private Table popup;




    public AchievementPopup() {
        this.showing = false;

    }

    /**
     * Adds the listener for triggering the popup.
     */
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("achievementCompleted", this::showPopup);
    }

    /**
     * Displays the popup on the current screen and save the completed achievement.
     */
    private void showPopup(Achievement achievement) {
        if (!showing) {
            generate(achievement);
        }
        SaveHandler.save(Achievements.class, "saves/achievement", FileLoader.Location.LOCAL);
    }

    /**
     * Handles drawing of the component. The actual rendering is managed by the stage.
     * @param batch The SpriteBatch used for drawing.
     */
    @Override
    public void draw(SpriteBatch batch) {
       // handled by the stage
    }

    /**
     * Creates the popup and adds it to the stage.
     */
    public void generate(Achievement achievement) {
        this.popup = new Table();
        showing = true;
        // Create the popup image
        Image book = new Image(new Texture(Gdx.files.internal("images/logbook-popup.png")));
        Image popupImage = new Image(new Texture(Gdx.files.internal(achievement.getPath())));
        book.setSize(100, 100);
        popupImage.setSize(100,100);

        popup.addActor(book);
        popup.addActor(popupImage);

        // Calculate and position the popup
        float screenHeight = Gdx.graphics.getHeight();
        float screenWidth = Gdx.graphics.getWidth();
        float displayX = screenWidth * 0.65f;
        float displayY = screenHeight * 0.85f;
        popup.setPosition(displayX, displayY);
        popup.setSize(200, 100);


        // Add actions to the popup
        SequenceAction sequence = new SequenceAction();
        sequence.addAction(Actions.delay(2f));
        sequence.addAction(Actions.fadeOut(1f));
        sequence.addAction(Actions.run(this::dispose));

        SequenceAction sequence2 = new SequenceAction();
        sequence2.addAction(Actions.moveBy(-60, 0));
        sequence2.addAction(Actions.moveBy(60, 0, 0.5f));
        sequence2.addAction(Actions.fadeOut(1f));
        popupImage.addAction(sequence2);
        popup.addAction(sequence);
        stage.addActor(popup);
    }


    /**
     * Disposes of popup message.
     */
    @Override
    public void dispose() {
        if (popup != null) {
            popup.clear();
            popup.remove();
            showing = false;
            popup = null;
        }
        super.dispose();
    }
}
