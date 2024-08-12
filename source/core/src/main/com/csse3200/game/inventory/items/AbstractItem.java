package com.csse3200.game.inventory.items;

/**
 * Abstract base class for items that can be used by a player.
 * <p>
 * This class provides a common implementation for the {@link ItemInterface} interface,
 * including default behavior for common properties like name and limit. Subclasses
 * must provide their own implementation for the {@code useItem} method, which
 * defines how the item can be used and must define what {@link ItemUsageContext} should be input
 * to {@code useItem}.
 * </p>
 */
public abstract class AbstractItem implements ItemInterface {
    protected String name;
    protected final int limit;
    protected final int itemCode; // Unique up to item name
    protected int quantity; // Number of items (non-zero and fixed if not Consumable)

    /**
     * Constructs an AbstractItem with the specified name and limit.
     */
    public AbstractItem(String name, int limit, int quantity) {
        this.name = name;
        this.limit = limit;
        this.quantity = quantity;
        this.itemCode = 0;
    }

    /**
     * Gets the name of the item.
     *
     * @return the name of the item
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the code of the item.
     *
     * @return the code of the item
     */
    @Override
    public int getItemCode() {
        return itemCode;
    }

    /**
     * Gets the limit associated with the item.
     *
     * @return the limit of the item
     */
    @Override
    public int getLimit() {
        return limit;
    }

    /**
     * @return the quantity of the item
     */
    @Override
    public int getQuantity() {return quantity;}

    // TODO: Test
    /**
     * @return whether the quantity of this item can increase
     */
    @Override
    public boolean canAdd(int n) {return quantity + n <= limit;}

    // TODO: Test
    /**
     * @param n - number of item to add
     */
    @Override
    public void add(int n) {quantity = quantity + n;}

    // TODO: Test
    /**
     * Uses the item with the provided {@link ItemUsageContext} and returns a result.
     * <p>
     * This method must be implemented by subclasses to define the specific behavior
     * of the item when used. The result type is defined by the
     * generic parameters of the subclass. The input type
     * </p>
     *
     * @param inputs the inputs for this item to be used
     */
    @Override
    public abstract void useItem(ItemUsageContext inputs);

    // TODO: Test
    @Override
    public boolean isConsumed() {
        return quantity == 0;
    }
}