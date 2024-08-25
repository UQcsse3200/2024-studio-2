package com.csse3200.game.inventory.items;

import com.badlogic.gdx.graphics.Texture;

/**
 * Abstract base class for items that can be used by a player.
 * <p>
 * This class provides a common implementation for an item including default behavior for common
 * properties like name and limit. Subclasses must provide their own implementation for the
 * {@code useItem} method, which defines how the item can be used and must define what
 * {@link ItemUsageContext} should be input to {@code useItem}.
 * </p>
 */
public abstract class AbstractItem {
    protected String name; // TODO: May be unnecessary - can use getSimpleName instead!
    protected final int itemCode; // Unique up to item name
    protected final int limit; // Must be non-negative
    protected int quantity; // Must be non-negative
    protected String description; // Description of the item
    private String texturePath = null; // Path to the texture for this item

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
        this.limit = limit;
        this.quantity = quantity;
    }

    /**
     * Gets the name of the item.
     *
     * @return the name of the item
     */
    public String getName() {return name;}

    /**
     * Gets the item code of the item.
     *
     * @return the code of the item
     */
    public int getItemCode() {return itemCode;}

    /**
     * Gets the maximum number of this item that can be held in a `stack`.
     *
     * @return the stack limit of the item
     */
    public int getLimit() {return limit;}

    /**
     * Gets the quantity of the item currently stacked.
     *
     * @return the quantity of the item
     */
    public int getQuantity() {return quantity;}

    /**
     * Returns a description of how the current item can be used and what it does
     *
     * @return description of item
     */
    public String getDescription() {
        if (this.description == null) {
            throw new IllegalAccessError("Cannot access description without setting first!");
        }
        return description;
    }

    /**
     * If what item can do, update the description of current item
     *
     * @param description description of item
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checks the number of this item that can be further stacked.
     *
     * @return how much the quantity of this item can increase by
     */
    public int numAddable() {return limit - quantity;}

    /**
     * Increases the current quantity of the item by n.
     *
     * @param n - number of item to add
     *
     * @throws IllegalArgumentException if the number of items cannot be increased by n
     */
    public void add(int n) {
        if (numAddable() < n) {
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
    public abstract void useItem(ItemUsageContext context);

    /**
     * Returns whether the quantity of this item has reached 0 (ie is consumed).
     *
     * @return whether there are none of this item remaining
     */
    public boolean isEmpty() {return quantity == 0;}

    /**
     * Sets the texture of the object using the provided file path.
     *
     * @param texturePath The file path of the texture to set.
     */
    protected void setTexture(String texturePath) {
        this.texturePath = texturePath;
    }

    /**
     * Retrieves the texture of the object.
     *
     * @return The texture associated with this object.
     * @throws IllegalAccessError if the texture has not been set prior to calling this method.
     */
    public Texture getTexture() throws IllegalAccessError {
        if (this.texturePath == null) {
            throw new IllegalAccessError("Cannot access texture without setting first!");
        }
        return new Texture(texturePath);
    }

}
