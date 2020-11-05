package ethos.model.content.achievement_diary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;

/**
 * Items with value that recharges over time.
 * 
 * @author Sky
 */
public class RechargeItems {
	private Player player;

	public RechargeItems(Player player) {
		this.player = player;
	}
	
	public boolean hasItem(int itemId) {
		if (player.getItems().playerHasItem(itemId) || player.getItems().isWearingItem(itemId)) {
			return true;
		}
		return false;
	}
	
	public boolean hasAnyItem(int... items) {
		for (int i = 0; i < items.length; i++) {
			if (player.getItems().playerHasItem(items[i]))
				return true;
		}
		for (int equipmentId : player.playerEquipment) {
			for (int item : items) {
				if (equipmentId == item) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void replenishPrayer(int divide) {
		if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
			player.startAnimation(645);
			player.playerLevel[5] += player.getPA().getLevelForXP(player.playerXP[5]) / divide;
			if (player.playerLevel[5] > 99) {
				player.playerLevel[5] = 99;
			}
			player.sendMessage("You recharge some prayer points.");
			player.getPA().refreshSkill(5);
		} else {
			player.sendMessage("You already have full prayer points.");
		}
	}
	
	public void replenishRun(int amount) {
		int total = player.getRunEnergy() + amount;
		if (total > 100) {
			player.setRunEnergy(100);
			player.getPA().sendFrame126(Integer.toString(100), 149);
		} else {
			player.getPA().sendFrame126(Integer.toString(player.getRunEnergy()), 149);
			player.setRunEnergy(player.getRunEnergy() + amount);
		}
	}

	public enum ItemsWithRecharge {
		// ID - CHARGES - MS
		
		/*
		 * Others
		 */
		ALTAR(0, 3, TimeUnit.HOURS.toMillis(12)),
		ALTAR_TWO(1, 3, TimeUnit.HOURS.toMillis(12)),
		AUBURY_STAFFS(2, 150, TimeUnit.HOURS.toMillis(24)),
		
		/*
		 * Items
		 */
		FALADOR_SHIELD_ONE(13117, 1, TimeUnit.HOURS.toMillis(24)), 
		FALADOR_SHIELD_TWO(13118, 1, TimeUnit.HOURS.toMillis(24)), 
		FALADOR_SHIELD_THREE(13119, 1, TimeUnit.HOURS.toMillis(24)), 
		FALADOR_SHIELD_FOUR(13120, 2, TimeUnit.HOURS.toMillis(24)), 
		
		EXPLORERS_RING_ONE(13125, 2, TimeUnit.HOURS.toMillis(24)), 
		EXPLORERS_RING_TWO(13126, 3, TimeUnit.HOURS.toMillis(24)), 
		EXPLORERS_RING_THREE(13127, 4, TimeUnit.HOURS.toMillis(24)), 
		EXPLORERS_RING_FOUR(13128, 3, TimeUnit.HOURS.toMillis(24)), 
		
		WESTERN_BANNER_THREE(13143, 1, TimeUnit.HOURS.toMillis(24)), 
		WESTERN_BANNER_FOUR(13144, 1, TimeUnit.HOURS.toMillis(24)), 
		
		WESTERN_SWORD_FOUR(13111, 5, TimeUnit.HOURS.toMillis(24));

		private int itemId;
		private int charges;
		private long time;

		private ItemsWithRecharge(int itemId, int charges, long time) {
			this.itemId = itemId;
			this.charges = charges;
			this.time = time;
		}
	}

	/**
	 * Call this when you want to use a charge for an action.
	 * 
	 * @param itemId
	 * @return
	 */
	public boolean useItem(int itemId) {
		checkReset(itemId);
		int value = itemValues.get(itemId);
		if (value <= 0) {
			player.sendMessage("@red@Your " + ItemAssistant.getItemName(itemId) + " has no uses left.");
			return false;
		}
		value--;
		itemValues.put(itemId, value);
		if (itemId < 500) {
			player.sendMessage("@red@You now have " + value + " uses left.");
		} else {
			player.sendMessage("@red@Your " + ItemAssistant.getItemName(itemId) + " now has " + value + " uses.");
		}
		return true;
	}
	
	public boolean useItem(int itemId, int amount) {
		checkReset(itemId);
		int value = itemValues.get(itemId);
		if (value <= 0) {
			player.sendMessage("@red@Your " + ItemAssistant.getItemName(itemId) + " has no uses left.");
			return false;
		}
		value -= amount;
		itemValues.put(itemId, value);
		if (itemId < 1000) {
			player.sendMessage("@red@You now have " + value + " uses left.");
		} else {
			player.sendMessage("@red@Your " + ItemAssistant.getItemName(itemId) + " now has " + value + " uses.");
		}
		return true;
	}

	/**
	 * Call this to check the charges of an item.
	 * 
	 * @param itemId
	 */
	public void checkCharges(int itemId) {
		checkReset(itemId);
		int value = itemValues.get(itemId);
		if (value <= 0) {
			player.sendMessage("@blu@Your " + ItemAssistant.getItemName(itemId) + " has no charges.");
			return;
		}
		player.sendMessage("@blu@Your " + ItemAssistant.getItemName(itemId) + " has " + value + " charges left.");
	}

	/**
	 * Call on load.
	 * 
	 * @return
	 */
	public void loadItem(int itemId, int value, String date) {
		itemValues.put(itemId, value);
		itemLastUsed.put(itemId, date);
	}

	/**
	 * Call this on player login.
	 */
	public void onLogin() {
		Date today = new Date();
		for (ItemsWithRecharge item : ItemsWithRecharge.values()) {
			int id = item.itemId;
			if (!itemValues.containsKey(id)) {
				itemValues.put(id, item.charges);
			}
			if (!itemLastUsed.containsKey(id)) {
				itemLastUsed.put(id, convertDateToString(today));// hier
			}
		}
	}

	/////////////////////////////////

	/**
	 * Items with values are stored in here.
	 */
	private Map<Integer, Integer> itemValues = new HashMap<>();
	private Map<Integer, String> itemLastUsed = new HashMap<>();

	public Map<Integer, Integer> getItemValues() {
		return itemValues;
	}

	public String getItemLastUsed(int itemId) {
		return itemLastUsed.get(itemId).toString();
	}

	private String convertDateToString(Date date) {
		String dateText = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return dateText;
	}

	/**
	 * Check if time has passed, if so, reset value.
	 * 
	 * @param itemId
	 * @return
	 */
	private boolean checkReset(int itemId) {
		Date today = new Date();
		Date lastUsed = null;

		try {
			lastUsed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(itemLastUsed.get(itemId));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long millisecondsAgoLastUsed = (today.getTime() - lastUsed.getTime());

		ItemsWithRecharge item = getItemById(itemId);
		long timeRange = item.time;

		if (millisecondsAgoLastUsed >= timeRange) {
			itemValues.put(itemId, item.charges);
			itemLastUsed.put(itemId, convertDateToString(today));// hier
			return true;
		}
		return false;
	}

	public int getChargesLeft(int itemId) {
		if (!itemValues.containsKey(itemId)) {
			return 0;
		}
		return itemValues.get(itemId);
	}

	private ItemsWithRecharge getItemById(int itemId) {
		for (ItemsWithRecharge item : ItemsWithRecharge.values()) {
			int id = item.itemId;
			if (id == itemId) {
				return item;
			}
		}
		return null;
	}
}
