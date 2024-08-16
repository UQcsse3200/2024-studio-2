package com.csse3200.game.components.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.eventservice.EventService;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class QuestPopup extends UIComponent {
    private boolean showing = false;
    private Label questCompleted;
    private final EventService eventService = ServiceLocator.getEventService();
    private static final float  fontScale = 2f;

    private void addActors() {
        eventService.globalEventHandler.addListener("questCompleted", this::showQuestCompletedPopup);
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void showQuestCompletedPopup() {
        showing = true;
        draw(null); // Call draw with null since SpriteBatch is unused
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(showing) {
            questCompleted = new Label("Quest Completed!", skin);
            questCompleted.setFontScale(fontScale);
            questCompleted.getStyle().fontColor = Color.GOLD;
            stage.addActor(questCompleted);
            questCompleted.getWidth();

            // Position label
            float screenHeight = Gdx.graphics.getHeight();
            float screenWidth = Gdx.graphics.getWidth();
            float displayX = (screenWidth / 2) - (questCompleted.getWidth() * fontScale / 2);
            float displayY = (screenHeight / 2) - (questCompleted.getHeight() * fontScale / 2);
            questCompleted.setPosition( displayX, displayY);

            questCompleted.addAction(Actions.sequence(
                    Actions.fadeOut(1f),
                    Actions.run(this::dispose)
            ));
        }
    }

    @Override
    public void dispose() {
        if (questCompleted != null) {
            questCompleted.remove();
            questCompleted = null;
        }
        super.dispose();
    }
}
