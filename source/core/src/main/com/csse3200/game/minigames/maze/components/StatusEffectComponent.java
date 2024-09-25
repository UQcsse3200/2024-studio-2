package com.csse3200.game.minigames.maze.components;

import com.csse3200.game.components.Component;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//TODO: James pls comment this class
public class StatusEffectComponent extends Component {
    private static final float FOREVER = 1e6f;
    private final GameTime timeSource;
    Map<String, Long> statusExpiry;
    Map<String, StatusEffect> statusEffect;

    public StatusEffectComponent() {
        timeSource = ServiceLocator.getTimeSource();
        statusExpiry = new HashMap<>();
        statusEffect = new HashMap<>();
    }

    @Override
    public void create() {
        for (StatusEffect effect : statusEffect.values()) {
            effect.create(entity);
        }
    }

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

    public StatusEffectComponent registerStatusEffect(String status, StatusEffect effect) {
        statusEffect.put(status, effect);
        return this;
    }

    public StatusEffect unregisterStatusEffect(String status) {
        return statusEffect.remove(status);
    }

    public void setStatusExpiry(String status, float duration) {
        if (!hasStatus(status)) {
            if (hasEffect(status)) {
                statusEffect.get(status).start();
            }
        }
        statusExpiry.put(status, timeSource.getTime() + (int) (duration * 1000));
    }

    public void addStatusExpiry(String status, float duration) {
        if (hasStatus(status)) {
            statusExpiry.put(status, statusExpiry.get(status) + (int) (duration * 1000));
        } else {
            setStatusExpiry(status, duration);
        }
    }
    public void setMinStatusExpiry(String status, float duration) {
        if (hasStatus(status)) {
            statusExpiry.put(status, Math.max(statusExpiry.get(status),
                    timeSource.getTime() + (int) (duration * 1000)));
        } else {
            setStatusExpiry(status, duration);
        }
    }

    public void addStatus(String status) {
        setStatusExpiry(status, FOREVER);
    }

    public Long removeStatus(String status) {
        if (hasStatus(status) && hasEffect(status)) {
            statusEffect.get(status).stop();
        }
        return statusExpiry.remove(status);
    }

    public void clearStatus(String status) {
        for (StatusEffect effect : statusEffect.values()) {
            effect.stop();
        }
        statusExpiry.clear();
    }

    public boolean hasStatus(String status) {
        return statusExpiry.containsKey(status);
    }

    public boolean hasEffect(String status) {
        return statusEffect.containsKey(status);
    }

    public Set<String> getActiveStatuses() {
        return statusExpiry.keySet();
    }
}
