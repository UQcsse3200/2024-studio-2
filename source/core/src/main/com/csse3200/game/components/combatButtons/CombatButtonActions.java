package com.csse3200.game.components.combatButtons;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.animal.AnimalSelectionActions;
import com.csse3200.game.components.animal.AnimalSelectionDisplay;
import com.csse3200.game.screens.LoadingScreen;
import com.csse3200.game.ui.PopUpDialogBox.PopUpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CombatButtonActions {
    private static final Logger logger = LoggerFactory.getLogger(AnimalSelectionActions.class);
    final CombatButtonDisplay display;
    private final PopUpHelper dialogHelper;
    private final GdxGame game;

    /**
     * Constructs an instance of the class.
     * @param display the display containing animal images and buttons
     * @param dialogHelper helper for displaying dialogs
     * @param game the game instance to change screens
     */
    public CombatButtonActions(CombatButtonDisplay display, PopUpHelper dialogHelper, GdxGame game) {
        this.display = display;
        this.dialogHelper = dialogHelper;
        this.game = game;
        addListeners(); // Add listeners to handle user interactions
    }

    private void addListeners() {
        //add image[] if needed
        TextButton[] CombatButtons = display.getCombatButtons();
        TextButton AttackButton = display.getAttackButton();
        TextButton BoostButton = display.getBoostButton();

        // Add listener to the "Attack" button to proceed with combat
        display.getAttackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int healthCheck=100;
                if (healthCheck!= 0) {
                    logger.info("CombatButtonActions::getAttackButton, Attack- health check!=0");
                    game.setScreen(new LoadingScreen(game));
                } else {
                    logger.info("CombatButtonActions::getAttackButton, Attack- health check = 0 big L");
                    AttackButton.setVisible(false);// cannot attack no more
                }
            }
        });

        // Add listener to the "Boost" button go to loading page temporarily later add combat logic
        display.getBoostButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int healthCheck=100;
                if (healthCheck!=100) {
                    logger.info("CombatButtonActions::getBoostButton, Boost- health check!=100");
                    game.setScreen(new LoadingScreen(game));
                } else {
                    logger.info("CombatButtonActions::getBoostButton, Boost- health check = sigma");
                    BoostButton.setVisible(false);// Maximum boost my guy
                }
            }
        });
    }
}
