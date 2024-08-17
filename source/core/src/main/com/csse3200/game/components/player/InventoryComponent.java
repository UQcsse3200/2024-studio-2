package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.inventory.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component intended to be used by the player to track their inventory.
 */
public class InventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);
  private final Inventory inventory;

  public InventoryComponent(int capacity) {
    this.inventory = new Inventory(capacity);
  }
}
