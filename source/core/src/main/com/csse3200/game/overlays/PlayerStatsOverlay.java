
package com.csse3200.game.overlays;

import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.overlays.PlayerStatsDisplay2;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.screens.PausableScreen;

public class PlayerStatsOverlay extends Overlay {
    private static final Logger logger = LoggerFactory.getLogger(PlayerStatsOverlay.class);
    private PausableScreen screen;

    public PlayerStatsOverlay(PausableScreen screen) {
        super(OverlayType.PLAYER_STATS_OVERLAY);
        this.screen = screen;
        logger.debug("Initialising PlayerStatsOverlay");
        createUI();
    }

    @Override
    public void rest() {
        logger.info("PlayerStatsOverlay rested");
        super.rest();
    }

    @Override
    public void wake() {
        logger.info("PlayerStatsOverlay awoken");
        super.wake();
    }

    private void createUI() {
        logger.debug("Creating UI");
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        String spritePath = PlayerFactory.getSelectedAnimalImagePath();
        String description = "Player Description"; // Replace with actual description if available
        super.add(ui);
        ui.addComponent(new PlayerStatsDisplay2(screen, spritePath, description)).addComponent(new InputDecorator(stage, 10));
        ServiceLocator.getEntityService().register(ui);
    }
}
