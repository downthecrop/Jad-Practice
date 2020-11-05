package ethos.model.content.LootingBag;

public class LootingBagItem {
	private int id;
	private int amount;
	
	public LootingBagItem(int id, int amount) {
		this.setId(id);
		this.setAmount(amount);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void incrementAmount(int amount) {
		this.amount += amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
