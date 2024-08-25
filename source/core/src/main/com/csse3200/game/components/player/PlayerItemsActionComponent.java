//package com.csse3200.game.components.player;
//
//import com.csse3200.game.components.Component;
//import com.csse3200.game.components.tasks.ItemProximityTask;
//import com.csse3200.game.entities.Entity;
//import com.csse3200.game.inventory.Inventory;
//import com.csse3200.game.inventory.items.AbstractItem;
//
//public class PlayerItemsActionComponent extends Component {
//    private boolean ItemPickUp = false;
//    @Override
//    public void create() {
//        super.create();
//        entity.getEvents().addListener("pickUpItem", this::pickUpItem);
//    }
//
//    private void pickUpItem() {ItemPickUp= !ItemPickUp;}
//
//    /**
//     * Makes player pick up item in range
//     */
//    public boolean pickUpItem(ItemProximityTask itemTask, Inventory inventory) {
//        if (itemTask.getPriority() != 0) {
//            itemTask.getItemEntity();
//            return true;
//        }
//        return false;
//    }
//
//
////  private ItemProximityTask findNearbyFood(Entity item, Entity player) {
////    return null;
////  }
////
////
////  private Entity findItemAtPosition(GridPoint2 position) {
////    // Assuming you have a way to retrieve all items in the world
////    // This will depend on your game architecture
////    for (Entity item : getAllItemsInWorld()) {
////      // Assuming items have a position component to determine their location
////      Vector2 itemPosition = item.getComponent(PositionComponent.class).getPosition();
////      if (itemPosition.equals(position.toVector2())) {
////        return item;
////      }
////    }
////    return null;
////  }
////
////  private List<Entity> getAllItemsInWorld() {
////
////    return new ArrayList<>();
////  }
////  /**
////   * Picks the item up from postion on map and add to the
////   * @param itemTask
////   * @param inventory
////   * @param item
////   */
////  private void pickUpItem(ItemProximityTask itemTask, Inventory inventory, AbstractItem item, Entity itemPos) {
////    if (!inventory.add(item)) {
////      entity.getEvents().trigger("pickUpItem", itemTask.getPriority());
////      itemTask.owner
////    }
////    spawnEntityAt
////  }
//}
