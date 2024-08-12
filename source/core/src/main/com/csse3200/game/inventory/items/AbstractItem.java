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
    protected final int itemCode; // Unique up to item name
    protected final int limit; // Must be non-negative
    protected int quantity; // Must be non-negative

    /**
     * Constructs an AbstractItem with the specified name and item code. Defaults to single
     * non-stackable item.
     *
     * @param name name of the item
     * @param itemCode unique item code (up to name)
     *
     * <p><strong>Note - all concrete subclasses must provide a unique itemCode</strong></p>
     */
    public AbstractItem(String name, int itemCode) {
        this.name = name;
        this.itemCode = itemCode;
        this.limit = 1; // Default to non-stackable item
        this.quantity = 1;
    }

    /**
     * Constructs an AbstractItem with the specified name, item code, limit and initial quantity.
     *
     * @param name name of the item
     * @param itemCode unique item code (up to name)
     * @param limit the maximum size of the item stack
     * @param quantity the initial starting quantity of the item
     *
     * @throws IllegalArgumentException if limit or quantity is negative, or quantity > limit
     *
     * <p><strong>Note - all concrete subclasses must provide a unique itemCode</strong></p>
     */
    public AbstractItem(String name, int itemCode, int limit, int quantity) {
        if (quantity < 0 || limit < 0 || quantity > limit) {
            String msg = "Quantity/Limit must be non-negative and quantity must be less than limit";
            throw new IllegalArgumentException(msg);
        }

        this.name = name;
        this.itemCode = itemCode;
        this.limit = limit; // Default to non-stackable item
        this.quantity = quantity;
    }

    /**
     * Gets the name of the item.
     *
     * @return the name of the item
     */
    @Override
    public String getName() {return name;}

    /**
     * Gets the item code of the item.
     *
     * @return the code of the item
     */
    @Override
    public int getItemCode() {return itemCode;}

    /**
     * Gets the limit associated with the item.
     *
     * @return the limit of the item
     */
    @Override
    public int getLimit() {return limit;}

    /**
     * Gets the quantity of the item currently stacked.
     *
     * @return the quantity of the item
     */
    @Override
    public int getQuantity() {return quantity;}

    /**
     * Checks the number of this item that can be further stacked.
     *
     * @return how much the quantity of this item can increase by
     */
    @Override
    public int canAdd() {return limit - quantity;}

    /**
     * Increases the current quantity of the item by n.
     *
     * @param n - number of item to add
     *
     * @throws IllegalArgumentException if the number of items cannot be increased by n
     */
    @Override
    public void add(int n) {
        if (canAdd() < n) {
            throw new IllegalArgumentException("Cannot add this many items!");
        }
        quantity += n;
    }

    /**
     * Uses the item with the provided {@link ItemUsageContext} and returns a result.
     * <p>
     * This method must be implemented by subclasses to define the specific behavior
     * of the item when used. The result type is defined by the
     * generic parameters of the subclass. The input type
     * </p>
     *
     * @param context the context for this item usage
     */
    @Override
    public abstract void useItem(ItemUsageContext context);

    /**
     * Returns whether the quantity of this item has reached 0.
     *
     * @return whether there are none of this item remaining
     */
    @Override
    public boolean isEmpty() {return quantity == 0;}
}