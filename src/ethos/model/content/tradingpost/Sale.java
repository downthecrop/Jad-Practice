package ethos.model.content.tradingpost;

public class Sale {
	
	private String name;
	private int saleId;
	private int itemId, quantity, price, totalSold, amountSoldSinceLastCollect;
	private boolean sold;
	
	public Sale(int _saleId, String _name, int _itemId, int _quantity, int _totalSold, int _price, int _amountSoldSinceLastCollect, boolean _sold) {
		this.saleId = _saleId;
		this.name = _name;
		this.itemId = _itemId;
		this.quantity = _quantity;
		this.totalSold = _totalSold;
		this.price = _price;
		this.amountSoldSinceLastCollect = _amountSoldSinceLastCollect;
		this.sold = _sold;
	}
	
	public int getSaleId() {
		return saleId;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return itemId;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setTotalSold(int i) {
		totalSold = i;
	}
	
	public int getTotalSold() {
		return totalSold;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setLastCollectedSold(int i) {
		amountSoldSinceLastCollect = i;
	}
	
	public int getLastCollectedAmount() {
		return amountSoldSinceLastCollect;
	}
	
	public void setHasSold(boolean i) {
		sold = i;
	}
	
	public Boolean hasSold() {
		return sold;
	}

}
