package com.csse3200.game.components.inventory;

import com.csse3200.game.inventory.Inventory;
import com.csse3200.game.inventory.items.AbstractItem;
import com.csse3200.game.inventory.items.ItemUsageContext;
import com.csse3200.game.inventory.items.TimedUseItem;
import com.csse3200.game.services.DialogueBoxService;
import com.csse3200.game.inventory.items.potions.AttackPotion;
import com.csse3200.game.inventory.items.potions.DefensePotion;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * PlayerInventoryDisplay extends InventoryDisplay and adds features like hotbar and drag-and-drop functionality.
 */
public class PlayerInventoryDisplay extends InventoryDisplay {
    private final ArrayList<TimedUseItem> potions = new ArrayList<>();

    public PlayerInventoryDisplay(Inventory inventory, int numCols, int hotBarCapacity) {
        super(inventory, numCols,hotBarCapacity, true);
    }

    /**
     * updates the potions effects if in or out of combat
     */
    public void updatePotions(ItemUsageContext context) {
        //unsure if this is intended behaviour but previous behaviour could only be false.
        if (potions.isEmpty()) { return;}

        Deque<Integer> removals = new ArrayDeque<>();

        // Find all items to remove
        for (int i = 0; i < potions.size(); i++) {
            TimedUseItem potion = potions.get(i);
            if (potion.onlyCombatItem()) {
                potions.get(i).update(context);
                removals.addFirst(i);  // Equivalent to stack.push(i)
            }
        }

        // Remove those items
        while (!removals.isEmpty()) {
            int i = removals.removeFirst();  // Equivalent to stack.pop()
            potions.remove(i);
        }
    }

    @Override
    public String toggleMsg() {return "toggleInventory";}

    @Override
    protected void useItem(AbstractItem item, int index) {
        ItemUsageContext context = new ItemUsageContext(entity);
        tryUseItem(item, context, index);
        if (item instanceof TimedUseItem timedUseItem) {
            potions.add(timedUseItem);
        }
        entity.getEvents().trigger("itemUsed", item);
    }

    @Override
    protected void enterSlot(AbstractItem item) {
        switch (item) {
            case DefensePotion potion -> {
                String[][] itemText = {{potion.getWarning()}};
                ServiceLocator.getDialogueBoxService().updateText(itemText, DialogueBoxService.DialoguePriority.ITEMINVENTORY);
            }
            case AttackPotion potion -> {
                String[][] itemText = {{potion.getWarning()}};
                ServiceLocator.getDialogueBoxService().updateText(itemText, DialogueBoxService.DialoguePriority.ITEMINVENTORY);
            }
            default -> {
                String[][] itemText = {{item.getDescription() + ". Quantity: "
                        + item.getQuantity() + "/" + item.getLimit()}};
                ServiceLocator.getDialogueBoxService().updateText(itemText, DialogueBoxService.DialoguePriority.ITEMINVENTORY);
            }
        }
    }

    @Override
    protected void exitSlot(AbstractItem item) {
        ServiceLocator.getDialogueBoxService().hideCurrentOverlay();
    }

    @Override
    public void regenerateDisplay() {
        super.regenerateDisplay();
        ItemUsageContext context = new ItemUsageContext(entity);
        updatePotions(context);
    }

    /**
     * Checks if the player is in combat or not to restrict certain actions i.e. using defense and attack potion when
     * not in combat or ensuring hotbar does not appear during combat
     * @param item the item in inventory
     * @param context context of the item usage
     * @param index index of the item in inventory
     */
    private void tryUseItem(AbstractItem item, ItemUsageContext context, int index) {
        if (item.onlyCombatItem()) {
            logger.warn("Cannot use combat items outside of combat.");
            return;
        }
        super.consumeItem(item, context, index);
    }
}
