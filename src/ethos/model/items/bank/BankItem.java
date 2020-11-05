package ethos.model.items.bank;

/**
 * @author Jason http://www.rune-server.org/members/jason
 * @date Apr 11, 2014
 */
public class BankItem {

    private int itemId, itemAmount;

    /**
     * @param itemId     The item id of the bank item
     * @param itemAmount The amount of items for the bank item
     */
    public BankItem(int itemId, int itemAmount) {
        this.setId(itemId);
        this.setAmount(itemAmount);
    }

    /**
     * @param itemId The item id of the bank item
     */
    public BankItem(int itemId) {
        this(itemId, 0);
    }

    /**
     * @return the amount of the item
     */
    public int getAmount() {
        return itemAmount;
    }

    /**
     * @param itemAmount sets the item amount for this item
     */
    public void setAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    /**
     * @return gets the item id
     */
    public int getId() {
        return itemId;
    }

    /**
     * @param itemId sets the item id
     */
    public BankItem setId(int itemId) {
        this.itemId = itemId;
        return this;
    }

}
