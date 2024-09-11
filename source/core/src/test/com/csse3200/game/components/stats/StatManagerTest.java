package com.csse3200.game.components.stats;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class StatManagerTest {
    private StatManager statManager;
    @Mock
    private Entity player;
    @Mock
    private EventHandler eventHandler;
    @Mock
    private AbstractItem item;
    @Mock
    private Stat stat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(player.getEvents()).thenReturn(eventHandler);
        when(item.getName()).thenReturn("TestItem");

        // Assuming StatSaveManager returns a mocked Array<Stat>
        StatSaveManager statSaveManager = mock(StatSaveManager.class);
        Array<Stat> stats = new Array<>();
        stats.add(stat);
        when(statSaveManager.getStats()).thenReturn(stats);

        statManager = new StatManager(player);
    }

    @Test
    void testIncrementStatNotFound() {
        when(stat.getStatName()).thenReturn("DifferentStat");

        statManager.incrementStat("TestStat", "add", 1);
    }

    @Test
    void testDispose() {
        statManager.dispose();
    }

    @After
    public void tearDown() {
        reset(Gdx.class);
    }
}
