package com.csse3200.game.minigames.birdieDash.entities.collision;

import com.badlogic.gdx.math.Rectangle;
import com.csse3200.game.minigames.birdieDash.collision.CollisionHandler;
import com.csse3200.game.minigames.birdieDash.entities.Bird;
import com.csse3200.game.minigames.birdieDash.entities.Coin;
import com.csse3200.game.minigames.birdieDash.entities.Pipe;
import com.csse3200.game.minigames.birdieDash.entities.Spike;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CollisionHandlerTest {

    private CollisionHandler collisionHandler;
    private Bird bird;
    private List<Pipe> pipes;
    private List<Coin> coins;
    private Spike spike;

    @Before
    public void setup() {
        bird = mock(Bird.class);
        Rectangle birdBoundingBox = mock(Rectangle.class);
        when(bird.getBoundingBox()).thenReturn(birdBoundingBox);

        Pipe pipe1 = mock(Pipe.class);
        Pipe pipe2 = mock(Pipe.class);
        pipes = Arrays.asList(pipe1, pipe2);

        Coin coin1 = mock(Coin.class);
        Coin coin2 = mock(Coin.class);
        coins = Arrays.asList(coin1, coin2);

        spike = mock(Spike.class);
        Rectangle spikeBoundingBox = mock(Rectangle.class);
        when(spike.getSpikeBoundary()).thenReturn(spikeBoundingBox);

        // Mock pipe bounding boxes
        when(pipe1.getBottomPipe()).thenReturn(mock(Rectangle.class));
        when(pipe1.getTopPipe()).thenReturn(mock(Rectangle.class));
        when(pipe2.getBottomPipe()).thenReturn(mock(Rectangle.class));
        when(pipe2.getTopPipe()).thenReturn(mock(Rectangle.class));

        // Mock coin bounding boxes
        when(coin1.getBoundary()).thenReturn(mock(Rectangle.class));
        when(coin2.getBoundary()).thenReturn(mock(Rectangle.class));

        collisionHandler = new CollisionHandler(bird, pipes, coins, spike);
    }

    @Test
    public void testCheckPipes_noCollision() {
        when(bird.getBoundingBox().overlaps(any())).thenReturn(false);  // No overlap with pipes
        collisionHandler.checkCollisions();

        verify(bird, never()).setCollidingPipe();
    }




    @Test
    public void testCheckSpikes_withCollision() {
        when(bird.getBoundingBox().overlaps(spike.getSpikeBoundary())).thenReturn(true);  // Bird touches the spike
        boolean result = collisionHandler.checkSpikes();

        assertTrue(result);
    }

    @Test
    public void testCheckSpikes_noCollision() {
        when(bird.getBoundingBox().overlaps(spike.getSpikeBoundary())).thenReturn(false);  // No collision with spike
        boolean result = collisionHandler.checkSpikes();

        assertFalse(result);
    }
}
