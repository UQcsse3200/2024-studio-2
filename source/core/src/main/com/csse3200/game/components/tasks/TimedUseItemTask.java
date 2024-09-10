package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;

import java.util.Objects;

public class TimedUseItemTask extends DefaultTask implements PriorityTask {
    private final Entity target;
    private final TimedUseItem potion;
    private final int priority;
    private final ItemUsageContext context;

    public TimedUseItemTask(Entity target, int priority, TimedUseItem potion, ItemUsageContext context) {
        this.target = target;
        this.priority = priority;
        this.potion = potion;
        this.context = context;
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
        if(activePotion() && this.potion.isExpired(this.context)) {
            if (this.potion.isExpired(this.context)) { //check if the item is in use
                this.potion.update(this.context);
            }
        }
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
