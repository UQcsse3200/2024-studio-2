package com.csse3200.game.components.combat.quicktimeevent;

/**
 * Record data structure representing a quick-time event.
 * <p>
 * Duration should be >= 0.3 s otherwise the event is
 * too fast for the user to respond
 *
 * @param duration the duration of the event (in secs)
 * @param delay the time delay between events (in secs)
 */
public record QuickTimeEvent(float duration, float delay) {}
