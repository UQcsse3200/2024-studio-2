package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemProximityTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final int priority;
    private final float proximityThreshold;
    private static final Logger logger = LoggerFactory.getLogger(ItemProximityTask.class);



    public ItemProximityTask(Entity target, int priority, float proximityThreshold) {
        this.target = target;
        this.priority = priority;
        this.proximityThreshold = proximityThreshold;
    }

    @Override
    public void start() {
        super.start();

        this.owner.getEntity().getEvents().trigger("pickUp");
    }

    public void update() {
        if (isPlayerNearItem()) {
            logger.info("Player is within pickup range of the item.");
        }
    }

    @Override
    public int getPriority() {
        if (isPlayerNearItem()) {
            return priority;
        }
        return 0;
    }

    private boolean isPlayerNearItem() {
        Entity item = owner.getEntity();
        Vector2 targetPosition = target.getPosition();
        Vector2 itemPosition = item.getPosition();
        float distance = targetPosition.dst(itemPosition);
        return distance <= proximityThreshold;



    }




}
