package com.csse3200.game.inventory.items;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.inventory.items.food.AbstractFood;
import com.csse3200.game.inventory.items.food.Foods;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.inventory.items.food.Foods.Apple;
import com.csse3200.game.inventory.items.food.Foods.Candy;
import com.csse3200.game.inventory.items.food.Foods.Meat;
import com.csse3200.game.inventory.items.food.Foods.Carrot;
import com.csse3200.game.inventory.items.food.Foods.ChickenLeg;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class FoodItemTest {
    private CombatStatsComponent stat;
    private FoodItemTest.TestablePlayer player1;

    private static class TestablePlayer extends ItemUsageContext {
        public TestablePlayer(Entity entity) {super(entity);
        }
    }

    @BeforeEach
    void setUp() {
        int maxHealth = 100;
        int maxHunger = 100;
        stat = new CombatStatsComponent(maxHealth, maxHunger,0,0,0,0, true);
        stat.setHunger(50);
        player1 = new TestablePlayer(new Entity().addComponent(stat));
    }

    void helperTestFood(AbstractFood food) {
        int originalHunger = player1.player.getComponent(CombatStatsComponent.class).getHunger();
        assertEquals(originalHunger, 50);
        food.useItem(player1);
        int newHunger = player1.player.getComponent(CombatStatsComponent.class).getHunger();
        assertTrue(newHunger > originalHunger, "Hunger did not increase when food was used!");
    }

    @Test
    void testApple() {
        Apple apple = new Apple(1);
        helperTestFood(apple);
    }

    @Test
    void testCandy() {
        Candy candy = new Candy(1);
        helperTestFood(candy);
    }

    @Test
    void testMeat() {
        Meat meat = new Meat(1);
        helperTestFood(meat);
    }

    @Test
    void testCarrot() {
        Carrot carrot = new Carrot(1);
        helperTestFood(carrot);
    }

    @Test
    void testChickenLeg() {
        ChickenLeg chickenLeg = new ChickenLeg(1);
        helperTestFood(chickenLeg);
    }
}
