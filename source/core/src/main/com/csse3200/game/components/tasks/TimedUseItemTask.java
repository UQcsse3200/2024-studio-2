package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.player.PlayerItemInUseDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class TimedUseItemTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(TimedUseItemTask.class);
    private final Entity target;
    private final TimedUseItem potion;
    private final int priority;
    private final ItemUsageContext context;
    private final PlayerItemInUseDisplay itemDisplay;

    public TimedUseItemTask(Entity target, int priority, TimedUseItem potion, ItemUsageContext context, PlayerItemInUseDisplay itemDisplay) {
        this.target = target;
        this.priority = priority;
        this.potion = potion;
        this.context = context;
        this.itemDisplay = itemDisplay;
    }

    /**
     * Starts the task by adding an event listener for the "useItem" event.
     * The event is triggered when the Player presses P.
     */
    @Override
    public void start() {
        super.start();
        this.target.getEvents().addListener("itemUsed", this::activePotion);
    }

    /**
     * Updates the task each frame. It checks the players proximity to the item and displays
     * an overlay if the player is near. If the play moves away, the overlay is removed.
     */
    @Override
    public void update() {
        super.update();
        if (activePotion() && this.potion.isExpired(this.context)) {
            logger.debug("Potion expired: " + potion.getName());
            this.potion.update(this.context);
            if (this.potion instanceof DefensePotion) {
                itemDisplay.setDefenseExpired(false);
                itemDisplay.createIndicationBox();
            }
        } else if (activePotion() && this.potion instanceof DefensePotion) {
            itemDisplay.setDefenseExpired(true);
        }
        logger.debug("Not Expired yet: " + potion.getGameTime());
    }

    /**
     * Returns the priority of this task, which is based on whether the player is near the item or has just
     * picked it up
     * @return The priority level of this task
     */
    @Override
    public int getPriority() {
        if (activePotion()) {
            return priority;
        }
        return 0;
    }

    private boolean activePotion() {
        EventHandler eventHandler = new EventHandler();
        return Objects.equals(eventHandler.getLastTriggeredEvent(), "itemUsed");
    }
}
