package ethos.model.content;

import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * Opening resource packs
 * @author Matt
 */
public enum Packs {
		/**
		 * Packs data
		 * id, itemId, amount
		 */
		EYE_OF_NEWT_PACK(12859,222,100),
		UNFINISHED_BROAD_ARROW_PACK(11885,11874,100),
		COMPOST_PACK(19704,6033,100),
		AIR_PACK(12728, 556, 100), 
		WATER_PACK(12730, 555, 100), 
		EARTH_PACK(12732, 557, 100), 
		FIRE_PACK(12734, 554, 100), 
		CHAOS_PACK(12738, 562, 100), 
		FEATHER_PACK(11881, 314, 100), 
		VIAL_OF_WATER_PACK(11879, 228, 100), 
		EMPTY_VIAL_PACK(11877, 230, 100), 
		BAIT_PACK(11883, 313, 100), 
		SOFT_CLAY_PACK(12009, 1762, 50), 
		BIRD_SNARE_PACK(12740, 10007, 50), 
		BOX_TRAP_PACK(12742, 10009, 50), 
		MAGIC_IMP_PACK(12744, -1, -1),
		AMYLASE_PACK(12641, 12640, 100),
		EASY_CLUE_BOTTLE(13648, 2677, 1),
		MEDIUM_CLUE_BOTTLE(13649, 2801, 1),
		HARD_CLUE_BOTTLE(13650, 2722, 1),
		EASY_CLUE_GEODE(20358, 2677, 1),
		MEDIUM_CLUE_GEODE(20360, 2801, 1),
		HARD_CLUE_GEODE(20362, 2722, 1),
		EASY_CLUE_NEST(19712, 2677, 1),
		MEDIUM_CLUE_NEST(19714, 2801, 1),
		HARD_CLUE_NEST(19716, 2722, 1),
		UNFINISIHED_BROAD_BOLT_PACK(11887, 11876, 100);

		private int packId;
		private int itemId;
		private int itemAmount;

		public int getPackId() {
			return packId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getItemAmount() {
			return itemAmount;
		}

		Packs(int packId, int itemId, int itemAmount) {
			this.packId = packId;
			this.itemId = itemId;
			this.itemAmount = itemAmount;
		}
	
	public static void openPack(final Player player, int item) {
		for (Packs pack : Packs.values()) {
			String name = pack.name().toLowerCase().replaceAll("_", " ");
			if (pack.getPackId() == item) {
				if (player.getItems().playerHasItem(item)) {
					player.getItems().deleteItem(pack.getPackId(), 1);
					player.getItems().addItem(pack.getItemId(), pack.getItemAmount());
					player.sendMessage("You opened the " + name + ".");
				}
			}
		}
	}

	public static void openSuperSet(Player c, int itemId){
		if (itemId == 13066) {
				if (c.getItems().freeSlots() < 3) {
					c.sendMessage("You need at least three slots to do this.");
					return;
				}
				int random = Misc.random(2);
				int amount = c.getItems().getItemAmount(itemId);
				for (int i = 0; i < amount; amount--) {
					c.getItems().deleteItem2(itemId, 1);
					if (random == 0) {
						c.getItems().addItemUnderAnyCircumstance(12695, 1);
						c.sendMessage("You break the combat potion set into one super combat potion.");
					} else {
						c.getItems().addItemUnderAnyCircumstance(2441, 1);
						c.getItems().addItemUnderAnyCircumstance(2437, 1);
						c.getItems().addItemUnderAnyCircumstance(2443, 1);
						c.sendMessage("You break the combat potion set into three individual potions.");
					}
				}
		}
	}


}