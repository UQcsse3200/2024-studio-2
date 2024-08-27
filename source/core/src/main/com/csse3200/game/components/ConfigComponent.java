package com.csse3200.game.components;

/**
 * A component that holds and manages configuration data for an entity.
 * This component can store a configuration object of a specific type directly.
 *
 * @param <T> The type of the configuration object.
 */
public class ConfigComponent<T> extends Component {
    private T config;

    /**
     * Constructs a new ConfigComponent with the specified configuration object.
     *
     * @param config The configuration object associated with this component.
     */
    public ConfigComponent(T config) {
        this.config = config;
    }

    /**
     * Retrieves the configuration object associated with this component.
     *
     * @return The configuration object.
     */
    public T getConfig() {
        return config;
    }

}
