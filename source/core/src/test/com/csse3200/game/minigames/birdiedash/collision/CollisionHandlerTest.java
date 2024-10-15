package com.csse3200.game.minigames.birdiedash.collision;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.minigames.birdiedash.entities.Bird;
import com.csse3200.game.minigames.birdiedash.entities.Coin;
import com.csse3200.game.minigames.birdiedash.entities.Pipe;
import com.csse3200.game.minigames.birdiedash.entities.Spike;
import com.csse3200.game.services.AudioManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
public class CollisionHandlerTest {

    private CollisionHandler collisionHandler;
    private Bird bird;
    private List<Pipe> pipes;
    private List<Coin> coins;
    private Spike spike;

    @Before
    public void setup() {
        bird = new Bird(300, 300);
        pipes = new ArrayList<>();
        pipes.add(new Pipe(0, 1));
        coins = new ArrayList<>();
        coins.add(new Coin(0, 1));
        spike = new Spike(0);
        collisionHandler = new CollisionHandler(bird, pipes, coins, spike);

    }

    @Test
    public void testCheckPipes_noCollision() {
        bird.setPosition(300, 300);
        pipes.getFirst().setPosition(0);
        collisionHandler.checkCollisions();
        assertFalse(bird.isCollidingPipe());
    }

    @Test
    public void testCheckPipes_Collision() {
        bird.setPosition(0, 0);
        pipes.getFirst().setPosition(0);
        collisionHandler.checkCollisions();
        assertTrue(bird.isCollidingPipe());
    }

    @Test
    public void testCheckSpikes_Collision() {
        bird.setPosition(20,20);
        assertTrue(collisionHandler.checkSpikes());
    }

    @Test
    public void testCheckSpikes_noCollision() {
        bird.setPosition(300,300);
        assertFalse(collisionHandler.checkSpikes());
    }

    @Test
    public void testCheckCoin_Collision() {
        try (MockedStatic<AudioManager> mockedAudioManager = mockStatic(AudioManager.class)) {
            mockedAudioManager.when(() -> AudioManager.playSound(anyString())).thenAnswer(invocation -> null);
            bird.setPosition(0, 0);
            coins.getFirst().setPosition(0, 0);
            int initialScore = collisionHandler.getScore();
            collisionHandler.checkCollisions();
            assertEquals(initialScore + 1, collisionHandler.getScore());
        }
    }

    @Test
    public void testCheckCoin_noCollision() {
        bird.setPosition(300, 300);
        coins.getFirst().setPosition(0, 0);
        int initialScore = collisionHandler.getScore();
        collisionHandler.checkCollisions();
        assertEquals(initialScore, collisionHandler.getScore());
    }
}
