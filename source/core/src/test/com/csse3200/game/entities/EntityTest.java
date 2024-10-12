package com.csse3200.game.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GameExtension.class)
class EntityTest {
  @Test
  void shouldSetAndGetPosition() {
    Entity entity = new Entity();
    Vector2 pos = new Vector2(5f, -5f);
    entity.setPosition(pos);
    assertEquals(pos, entity.getPosition());

    entity.setPosition(0f, 0f);
    assertEquals(Vector2.Zero, entity.getPosition());
  }

  @Test
  void shouldSetAndGetScale() {
    Entity entity = new Entity();
    Vector2 scale = new Vector2(2f, 3f);
    
    //scale that sets both x and y
    entity.setScale(scale);
    assertEquals(scale, entity.getScale());

    entity.setScale(0.1f, 0.2f);
    assertEquals(new Vector2(0.1f, 0.2f), entity.getScale());
    
    //scale that sets x, y scales with
    entity.scaleWidth(0.2f);
    assertEquals(new Vector2(0.2f, 0.4f), entity.getScale());
    
    //scale that sets y, x scales with
    entity.scaleHeight(0.2f);
    assertEquals(new Vector2(0.1f, 0.2f), entity.getScale());
  }

  @Test
  void shouldHaveCorrectCenter() {
    Entity entity = new Entity();
    entity.setPosition(0f, 0f);
    entity.setScale(2f, 2f);
    assertEquals(new Vector2(1f, 1f), entity.getCenterPosition());

    entity.setPosition(-5f, -10f);
    assertEquals(new Vector2(-4f, -9f), entity.getCenterPosition());
  }

  @Test
  void shouldUpdateComponents() {
    Entity entity = new Entity();
    Component component1 = spy(TestComponent1.class);
    Component component2 = spy(TestComponent2.class);
    entity.addComponent(component1);
    entity.addComponent(component2);
    entity.create();
    entity.update();

    verify(component1).update();
    verify(component2).update();

    entity.earlyUpdate();
    verify(component1).earlyUpdate();
    verify(component2).earlyUpdate();
  }

  @Test
  void shouldCreateComponents() {
    Entity entity = new Entity();
    Component component1 = spy(TestComponent1.class);
    Component component2 = spy(TestComponent2.class);
    entity.addComponent(component1);
    entity.addComponent(component2);
    entity.create();

    verify(component1).create();
    verify(component2).create();
    assertEquals(entity, component1.getEntity());
    assertEquals(entity, component2.getEntity());
  }

  @Test
  void shouldRejectSameClassComponents() {
    Entity entity = new Entity();
    Component component1 = new TestComponent1();
    Component component2 = new TestComponent1();
    entity.addComponent(component1);
    entity.addComponent(component2);
    assertEquals(component1, entity.getComponent(TestComponent1.class));
  }

  @Test
  void shouldRejectAfterCreate() {
    Entity entity = new Entity();
    entity.create();
    entity.addComponent(new TestComponent1());
    assertNull(entity.getComponent(TestComponent1.class));
  }

  @Test
  void shouldGetComponent() {
    Entity entity = new Entity();
    Component component1 = new TestComponent1();
    Component component2 = new TestComponent2();
    entity.addComponent(component1);
    entity.addComponent(component2);

    TestComponent1 gotComponent1 = entity.getComponent(TestComponent1.class);
    TestComponent2 gotComponent2 = entity.getComponent(TestComponent2.class);

    assertEquals(component1, gotComponent1);
    assertEquals(component2, gotComponent2);
  }

  @Test
  void shouldNotGetMissingComponent() {
    Entity entity = new Entity();
    TestComponent1 component = entity.getComponent(TestComponent1.class);
    assertNull(component);
  }

  @Test
  void shouldDisposeComponents() {
    Entity entity = new Entity();
    TestComponent1 component = spy(TestComponent1.class);
    entity.addComponent(component);
    entity.create();

    EntityService entityService = mock(EntityService.class);
    ServiceLocator.registerEntityService(entityService);

    entity.dispose();
    verify(component).dispose();
    verify(entityService).unregister(entity);
  }
  
  @Test
  void shouldNotDisposeAnimationComponent() {
    RenderService render = new RenderService();
    ServiceLocator.registerRenderService(render);
    
    ResourceService resourceService = new ResourceService();
    ServiceLocator.registerResourceService(resourceService);
    resourceService.loadTextureAtlases(new String[] {"images/banana.atlas"});
    resourceService.loadAll();
    
    Entity entity = new Entity();
    TestComponent1 component = spy(TestComponent1.class);
    AnimationRenderComponent renderComponent = spy(AnimationRenderComponent.class);
    
    entity.addComponent(component);
    entity.addComponent(renderComponent);
    entity.create();
    
    EntityService entityService = mock(EntityService.class);
    ServiceLocator.registerEntityService(entityService);
    
    clearInvocations(renderComponent);
    
    entity.specialDispose();
    verify(component).dispose();
    
    verifyNoInteractions(renderComponent);
    verify(entityService).unregister(entity);
  }

  @Test
  void shouldHaveUniqueId() {
    Entity entity1 = new Entity();
    Entity entity2 = new Entity();

    assertNotEquals(entity1.getId(), entity2.getId());
  }

  @Test
  void shouldEqualWithId() {
    Entity entity1 = new Entity();
    Entity entity2 = mock(Entity.class);
    int id = entity1.getId();
    when(entity2.getId()).thenReturn(id);

    assertEquals(entity1, entity2);
  }
  
  @Test
  void shouldNotEqualDifferentClass() {
    //sorta redundant test, but might cover all branches of the equals method to make sonarcloud happy
    Entity entity1 = new Entity();
    Component component1 = new TestComponent1();
    
    assertNotEquals(entity1, component1);
  }

  @Test
  void shouldNotUpdateIfDisabled() {
    Entity entity = new Entity();
    TestComponent1 component = spy(TestComponent1.class);
    entity.addComponent(component);
    entity.create();

    entity.setEnabled(false);
    entity.earlyUpdate();
    entity.update();

    verify(component, times(0)).earlyUpdate();
    verify(component, times(0)).update();
  }

  static class TestComponent1 extends Component {}

  static class TestComponent2 extends Component {}
}
