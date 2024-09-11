package com.csse3200.game.components.stats;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events related to updating end game stats and does
 * something when one of the events is triggered.
 */
public class StatActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(StatActions.class);
    private final GdxGame game;
    private final Entity player;

    public StatActions(GdxGame game, Entity player) {
        this.game = game;
        this.player = player;
    }

    /**
     * Create listeners for updating stat values
     */
    @Override
    public void create() {
        entity.getEvents().addListener("incrementStat", this::onStatIncrement);

        //logger.info("Enemies defeated: {}", player.getComponent(StatManager.class).getStat("EnemyDefeated").getStatName());
    }

    /**
     * Increment a stat by a certain amount.
     */
    public void onStatIncrement(int value) {
        String operation = "add";
        player.getComponent(StatManager.class).incrementStat("EnemyDefeated", operation, value);

        //logger.info("Incrementing stat: {} ", player.getComponent(StatManager.class).getStat("EnemyDefeated"));
        //logger.info("New stat value: {}", player.getComponent(StatManager.class).getStat("EnemyDefeated").getCurrent());
    }
}

