package com.csse3200.game.services;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.eventservice.EventService;

/**
 * A container class that stores all of the services for a screen. This can be used to switch between screens without
 * loosing any of the services that are loaded.
 */
public class ServiceContainer {
    private EntityService entityService;
    private RenderService renderService;
    private PhysicsService physicsService;
    private GameTime timeSource;
    private InputService inputService;
    private ResourceService resourceService;
    private EventService eventService;

    public ServiceContainer(EntityService entityService, RenderService renderService, PhysicsService physicsService,
                            GameTime timeSource, InputService inputService, ResourceService resourceService,
                            EventService eventService) {
        this.entityService = entityService;
        this.renderService = renderService;
        this.physicsService = physicsService;
        this.timeSource = timeSource;
        this.inputService = inputService;
        this.resourceService = resourceService;
        this.eventService = eventService;
    }

    /**
     * Gets the EntityService stored in this container
     * @return the EntityService stored in this container
     */
    public EntityService getEntityService() {return entityService;}
    public RenderService getRenderService() {return renderService;}
    public PhysicsService getPhysicsService() {return physicsService;}
    public GameTime getTimeSource() {return timeSource;}
    public InputService getInputService() {return inputService;}
    public ResourceService getResourceService() {return resourceService;}
    public EventService getEventService() {return eventService;}

    /**
     * stores an EntityService in this container
     * @param entityService
     */
    public void storeEntityService(EntityService entityService) {this.entityService = entityService;}
    public void storeRenderService(RenderService renderService) {this.renderService = renderService;}
    public void storePhysicsService(PhysicsService physicsService) {this.physicsService = physicsService;}
    public void storeTimeSource(GameTime timeSource) {this.timeSource = timeSource;}
    public void storeInputService(InputService inputService) {this.inputService = inputService;}
    public void storeResourceService(ResourceService resourceService) {this.resourceService = resourceService;}
    public void storeEventService(EventService eventService) {this.eventService = eventService;}
}
