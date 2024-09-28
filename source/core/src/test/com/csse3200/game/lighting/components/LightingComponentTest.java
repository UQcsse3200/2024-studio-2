package com.csse3200.game.lighting.components;

import box2dLight.PositionalLight;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LightingComponentTest {
    private Entity entity;

    private LightingComponent component;

    private final static Vector2 offset = new Vector2(1.3f, 0.2f);

    @Mock
    PositionalLight light;

    @BeforeEach
    public void setup() {
        entity = new Entity();
        entity.setPosition(0,0);
        component = new LightingComponent().attach(light, offset);
        entity.addComponent(component);
        entity.create();
    }

    @Test
    public void testGetLights() {
        assertEquals(Collections.singletonList(light), component.getLights());
    }

    @Test
    public void testGetOffsets() {
        assertEquals(Collections.singletonList(offset), component.getOffsets());
    }

    @Test
    public void testUpdateLightPosition() {
        entity.setPosition(1,1);
        entity.update();
        verify(light).setPosition(entity.getCenterPosition().add(offset));
    }


    @Test
    public void testDisposeLight() {
        component.dispose();
        verify(light).remove(true);
    }
}
