package com.csse3200.game.services;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.eventservice.EventService;

/**
 * A container class that stores all the services for a screen. This can be used to switch between screens without
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

    public ServiceContainer() {
        this.entityService = ServiceLocator.getEntityService();
        this.renderService = ServiceLocator.getRenderService();
        this.physicsService = ServiceLocator.getPhysicsService();
        this.timeSource = ServiceLocator.getTimeSource();
        this.inputService = ServiceLocator.getInputService();
        this.resourceService = ServiceLocator.getResourceService();
        this.eventService = ServiceLocator.getEventService();
    }

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
     * @return the EntityService stored in this container
     */
    public EntityService getEntityService() {return entityService;}

    /**
     * @return the RenderService stored in this container
     */
    public RenderService getRenderService() {return renderService;}

    /**
     * @return the PhysicsService stored in this container
     */
    public PhysicsService getPhysicsService() {return physicsService;}
    /**
     * @return the GameTime stored in this container
     */
    public GameTime getTimeSource() {return timeSource;}
    /**
     * @return the InputService stored in this container
     */
    public InputService getInputService() {return inputService;}
    /**
     * @return the ResourceService stored in this container
     */
    public ResourceService getResourceService() {return resourceService;}
    /**
     * @return the EventService stored in this container
     */
    public EventService getEventService() {return eventService;}

    /**
     * stores an EntityService in this container
     * @param entityService the service to be stored
     */
    public void storeEntityService(EntityService entityService) {this.entityService = entityService;}
    /**
     * stores an RenderService in this container
     * @param renderService the service to be stored
     */
    public void storeRenderService(RenderService renderService) {this.renderService = renderService;}
    /**
     * stores an PhysicsService in this container
     * @param physicsService the service to be stored
     */
    public void storePhysicsService(PhysicsService physicsService) {this.physicsService = physicsService;}
    /**
     * stores a GameTime in this container
     * @param timeSource the time source to be stored
     */
    public void storeTimeSource(GameTime timeSource) {this.timeSource = timeSource;}
    /**
     * stores an InputService in this container
     * @param inputService the service to be stored
     */
    public void storeInputService(InputService inputService) {this.inputService = inputService;}
    /**
     * stores a ResourceService in this container
     * @param resourceService the service to be stored
     */
    public void storeResourceService(ResourceService resourceService) {this.resourceService = resourceService;}
    /**
     * stores an EventService in this container
     * @param eventService the service to be stored
     */
    public void storeEventService(EventService eventService) {this.eventService = eventService;}
}
