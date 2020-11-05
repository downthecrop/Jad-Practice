package ethos.model.players.skills.runecrafting;

import ethos.model.players.Player;

public class Pouches {

	private static final int RUNE_ESS = 1436, PURE_ESS = 7936;

	public enum Pouch {
		SMALL_POUCH(5509, 7, 7), 
		MEDIUM_POUCH(5510, 16, 16), 
		LARGE_POUCH(5512, 25, 25);

		Pouch(int id, int capacityOfRune, int capacityOfPure) {
			this.pouchId = id;
			this.capacityOfRune = capacityOfRune;
			this.capacityOfPure = capacityOfPure;
		}

		private int pouchId;
		private int capacityOfRune, capacityOfPure;

		public static Pouch forId(int id) {
			for (Pouch pouch : Pouch.values()) {
				if (pouch.pouchId == id)
					return pouch;
			}
			return null;
		}
	}
	
	/**
	 * Attempting to fill runecrafting pouch
	 * @param player	The player who is attempting to fill
	 * @param pouch		The pouch that the player is attempting to fill
	 * @param pouchId	^
	 * @param size		The pouch size that the player is attempting to fill
	 */
	public static void fill(Player player, Pouch pouch, int pouchId, int size) {
		int rune_essence = player.getItems().getItemAmount(RUNE_ESS);
		int pure_essence = player.getItems().getItemAmount(PURE_ESS);	
		int amount = 0;
		rune_essence = rune_essence > pouch.capacityOfRune ? pouch.capacityOfRune : rune_essence;
		pure_essence = pure_essence > pouch.capacityOfPure ? pouch.capacityOfPure : pure_essence;	
		
		if (player.getInterfaceEvent().isActive()) {
			player.sendMessage("Please finish what you're doing.");
			return;
		}
		if(rune_essence == 0 && pure_essence == 0) {
			player.sendMessage("You do not have any essence in your inventory.");
			return;
		}
		if(player.getRuneEssencePouch(size) >= pouch.capacityOfRune)
			player.sendMessage("Your pouch can not hold any more Rune essence.");
		if(player.getPureEssencePouch(size) >= pouch.capacityOfPure)
			player.sendMessage("Your pouch can not hold any more Pure essence.");
		while(rune_essence > 0 && player.getRuneEssencePouch(size) < pouch.capacityOfRune && player.getItems().playerHasItem(RUNE_ESS)) {
			player.getItems().deleteItem(RUNE_ESS, 1);
			player.setRuneEssencePouch(size, player.getRuneEssencePouch(size) + 1);
			amount++;
		}
		while(pure_essence > 0 && player.getPureEssencePouch(size) < pouch.capacityOfPure && player.getItems().playerHasItem(PURE_ESS)) {
			player.getItems().deleteItem(PURE_ESS, 1);
			player.setPureEssencePouch(size, player.getPureEssencePouch(size) + 1);
			amount++;
		}
		if(amount > 0)
			player.sendMessage("You fill your pouch with "+amount+" essence.");
	}
	
	/**
	 * Attempting to empty runecrafting pouch
	 * @param player	The player who is attempting to empty
	 * @param pouch		The pouch that the player is attempting to empty
	 * @param pouchId	^
	 * @param size		The pouch size that the player is attempting to empty
	 */
	public static void empty(Player player, Pouch pouch, int pouchId, int size) {
		if (player.getInterfaceEvent().isActive()) {
			player.sendMessage("Please finish what you're doing.");
			return;
		}
		if (player.getRuneEssencePouch(size) == 0 && player.getPureEssencePouch(size) == 0) {
			player.sendMessage("This pouch seems to be empty.");
			return;
		}
		if (player.getItems().freeSlots() == 0) {
			player.sendMessage("You do not have enough space to do this.");
			return;
		}
		while(player.getRuneEssencePouch(size) > 0 && player.getItems().freeSlots() > 0) {
			player.getItems().addItem(RUNE_ESS, 1);
			player.setRuneEssencePouch(size, player.getRuneEssencePouch(size) - 1);
		}
		while(player.getPureEssencePouch(size) > 0 && player.getItems().freeSlots() > 0) {
			player.getItems().addItem(PURE_ESS, 1);
			player.setPureEssencePouch(size, player.getPureEssencePouch(size) - 1);
		}
		return;
	}

	public static void check(Player player, Pouch pouch, int pouchId, int size) {
		player.sendMessage("Your pouch currently contains "+player.getRuneEssencePouch(size)+" Rune essence and "+player.getPureEssencePouch(size)+" Pure essence.");
	}
}
