package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Component that allows status effects to be applied to an entity and trigger effects for a
 * limited or unlimited amount of time.
 */
public class StatusEffectComponent extends Component {
    // "Unlimited time"
    private static final float FOREVER = 1e6f;
    private final GameTime timeSource;
    Map<String, Long> statusExpiry;
    Map<String, StatusEffect> statusEffect;

    /**
     * Initialise status effect component.
     */
    public StatusEffectComponent() {
        timeSource = ServiceLocator.getTimeSource();
        statusExpiry = new HashMap<>();
        statusEffect = new HashMap<>();
    }

    /**
     * Called when entity is spawned. Creates all registered status effects.
     */
    @Override
    public void create() {
        for (StatusEffect effect : statusEffect.values()) {
            effect.create(entity);
        }
    }

    /**
     * Called every frame of game. Checks which status effects have timed out and should
     * no longer be applied. Calls the update function of all effects registered to a status.
     */
    @Override
    public void update() {
        for (String status : statusExpiry.keySet()) {
            if (statusExpiry.get(status) <= timeSource.getTime()) {
                removeStatus(status);
            } else {
                if (hasEffect(status)) {
                    statusEffect.get(status).update();
                }
            }
        }
    }

    /**
     * Register an effect to a given status. When this status is applied to the entity, the effect's
     * start method is executed. Every frame of the game while this entity has this status, the
     * effect's update method is executed. When this times out or is removed from the entity, the
     * effect's stop method is executed.
     *
     * @param status the status
     * @param effect the effect to attach to the status
     * @return this
     */
    public StatusEffectComponent registerStatusEffect(String status, StatusEffect effect) {
        statusEffect.put(status, effect);
        return this;
    }

    /**
     * Unregisters the effect attached to a given status.
     * @param status the status
     * @return the removes effect (or null if does not exist)
     */
    public StatusEffect unregisterStatusEffect(String status) {
        return statusEffect.remove(status);
    }

    /**
     * Sets the time before a status expires. If the entity does not currently have this status,
     * the start method of the effect attached to this status will be called.
     * @param status the status
     * @param duration the duration (in seconds)
     */
    public void setStatusExpiry(String status, float duration) {
        if (!hasStatus(status)) {
            if (hasEffect(status)) {
                statusEffect.get(status).start();
            }
        }
        statusExpiry.put(status, timeSource.getTime() + (int) (duration * 1000));
    }

    /**
     * Increases the duration before a status expires by a specified amount. If the entity does not
     * currently have this status, equivalent to setStatusExpiry.
     * @param status the status
     * @param duration the duration to add (in seconds)
     */
    public void addStatusExpiry(String status, float duration) {
        if (hasStatus(status)) {
            statusExpiry.put(status, statusExpiry.get(status) + (int) (duration * 1000));
        } else {
            setStatusExpiry(status, duration);
        }
    }

    /**
     * Sets the duration before a status expires to at least a specified amount. If the status
     * is active and has a longer duration, does nothing. If the status does not exist,
     * equivalent to setStatusExpiry.
     * @param status the status
     * @param duration the minimum expiry duration (in seconds)
     */
    public void setMinStatusExpiry(String status, float duration) {
        if (hasStatus(status)) {
            statusExpiry.put(status, Math.max(statusExpiry.get(status),
                    timeSource.getTime() + (int) (duration * 1000)));
        } else {
            setStatusExpiry(status, duration);
        }
    }

    /**
     * Permanently applies a status to the entity (until removed).
     * @param status the status
     */
    public void addStatus(String status) {
        setStatusExpiry(status, FOREVER);
    }

    public Long removeStatus(String status) {
        if (hasStatus(status) && hasEffect(status)) {
            statusEffect.get(status).stop();
        }
        return statusExpiry.remove(status);
    }

    /**
     * Removes all status effects applied to the entity.
     */
    public void clearStatus() {
        for (StatusEffect effect : statusEffect.values()) {
            effect.stop();
        }
        statusExpiry.clear();
    }

    /**
     * Checks whether the entity has a given status.
     * @param status the status
     * @return whether the entity has this status
     */
    public boolean hasStatus(String status) {
        return statusExpiry.containsKey(status);
    }

    /**
     * Checks whether the entity has an effect attached to a given status.
     * @param status the status
     * @return whether the entity has an effect attached to this status.
     */
    public boolean hasEffect(String status) {
        return statusEffect.containsKey(status);
    }

    /**
     * @return A set of all active statuses applied to the entity.
     */
    public Set<String> getActiveStatuses() {
        return statusExpiry.keySet();
    }
}
