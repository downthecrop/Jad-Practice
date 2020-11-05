package ethos.model.content;

import ethos.model.players.Player;

public enum SkillcapePerks {
		ATTACK(new int[][] { { 9747, 9748 } }),
		STRENGTH(new int[][] { { 9750, 9751 } }),
		DEFENCE(new int[][] { { 9753, 9754} }),
		RANGING(new int[][] { { 9756, 9757 } }),
		PRAYER(new int[][] { { 9759, 9760 } }),
		MAGIC(new int[][] { { 9762, 9763 } }),
		RUNECRAFTING(new int[][] { { 9765, 9766 } }),
		HITPOINTS(new int[][] { { 9768, 9769 } }),
		AGILITY(new int[][] { { 9771, 9772 } }),
		HERBLORE(new int[][] { { 9774, 9775 } }),
		THIEVING(new int[][] { { 9777, 9778 } }),
		CRAFTING(new int[][] { { 9780, 9781 } }),
		FLETCHING(new int[][] { { 9783, 9784 } }),
		SLAYER(new int[][] { { 9786, 9787 } }),
		MINING(new int[][] { { 9792, 9793 } }),
		SMITHING(new int[][] { { 9795, 9796 } }),
		FISHING(new int[][] { { 9798, 9799 } }),
		COOKING(new int[][] { { 9801, 9802 } }),
		FIREMAKING(new int[][] { { 9804, 9805 } }),
		WOODCUTTING(new int[][] { { 9807, 9808 } }),
		FARMING(new int[][] { { 9810, 9811 } }),
		HUNTER(new int[][] { { 9948, 9949} }),
		MAX_CAPE(new int[][] { { 13280 } }),
		ARDOUGNE_MAX_CAPE(new int[][] { { 20760 } }),
		FIRE_MAX_CAPE(new int[][] { { 13329 } }),
		AVAS_MAX_CAPE(new int[][] { { 13337 } }),
		SARADOMIN_MAX_CAPE(new int[][] { { 13331 } }),
		ZAMORAK_MAX_CAPE(new int[][] { { 13333 } }),
		GUTHIX_MAX_CAPE(new int[][] { { 13335 } 
	});
	
	public static final int[] MAX_CAPE_IDS = { 13280, 13329, 13337, 13331, 13333, 13335, 20760 };
		
	public static final SkillcapePerks[] MAX_CAPES = { MAX_CAPE, ARDOUGNE_MAX_CAPE, FIRE_MAX_CAPE, AVAS_MAX_CAPE, SARADOMIN_MAX_CAPE, ZAMORAK_MAX_CAPE, GUTHIX_MAX_CAPE };
	
	private int[][] skillcapes;

	SkillcapePerks(int[][] skillcapes) {
		this.skillcapes = skillcapes;
	}
	
	public int[][] getSkillcape() {
		return skillcapes;
	}
	
	static int MAX_CAPE_ID = 13280, MAX_CAPE_HOOD = 13281;
	
	/**
	 * Allows us to check wether or not a player is wearing one of the capes
	 * @param player
	 * @return
	 */
	public boolean isWearing(Player player) {
		for (int[] set : skillcapes) {
			for (int setItem : set) {
				if (player.getItems().isWearingItem(setItem, player.playerCape)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isWearingMaxCape(Player player) {
		if (MAX_CAPE.isWearing(player) || 
			FIRE_MAX_CAPE.isWearing(player) || 
			AVAS_MAX_CAPE.isWearing(player) || 
			SARADOMIN_MAX_CAPE.isWearing(player) || 
			ZAMORAK_MAX_CAPE.isWearing(player) || 
			ARDOUGNE_MAX_CAPE.isWearing(player) || 
			GUTHIX_MAX_CAPE.isWearing(player)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Purchasing max cape
	 * @param player		The player who is purchasing the max cape
	 */
	public static void purchaseMaxCape(Player player) {
		if (!player.getItems().playerHasItem(995, 10_000_000)) {
			player.sendMessage("You must have 10,000,000 coins to do this");
			player.getPA().closeAllWindows();
			return;
		} else {
			player.getItems().addItem(MAX_CAPE_ID, 1);
			player.getItems().addItem(MAX_CAPE_HOOD, 1);
			player.getItems().deleteItem(995, 10_000_000);
			player.getDH().sendItemStatement("Mac grunts and hands over his cape, pocketing your \\n money swiftly.", MAX_CAPE_ID);
		}
	}
	
	/**
	 * Mixing items with max cape
	 */
	public static void mixCape(Player player, String cape) {

		if (!player.getItems().playerHasItem(MAX_CAPE_ID) || !player.getItems().playerHasItem(MAX_CAPE_HOOD)) {
			player.sendMessage("You must have a max cape and hood in order to do this.");
			return;
		}

		player.getItems().deleteItem(MAX_CAPE_ID, 1);
		player.getItems().deleteItem(MAX_CAPE_HOOD, 1);
		switch (cape) {
		
		case "FIRE":
			if (!player.getItems().playerHasItem(6570)) {
				player.sendMessage("You must have a firecape in order to do this.");
				return;
			}
			player.getItems().deleteItem(6570, 1);
			player.getItems().addItem(13329, 1);
			player.getItems().addItem(13330, 1);
			player.getDH().sendItemStatement("You've combined the fire cape and max cape.", 13329);
			break;

			case "SARADOMINi":
				if (!player.getItems().playerHasItem(21791)) {
					player.sendMessage("You must have a Imbued saradomin cape in order to do this.");
					return;
				}
				player.getItems().deleteItem(21791, 1);
				player.getItems().addItem(21778, 1);
				player.getItems().addItem(21776, 1);
				player.getDH().sendItemStatement("You've combined the Imbued saradomin cape and max cape.", 13331);
				break;

			case "ZAMORAKi":
				if (!player.getItems().playerHasItem(21795)) {
					player.sendMessage("You must have a Imbued zamorak cape in order to do this.");
					return;
				}
				player.getItems().deleteItem(21795, 1);
				player.getItems().addItem(21782, 1);
				player.getItems().addItem(21780, 1);
				player.getDH().sendItemStatement("You've combined the Imbued zamorak cape and max cape.", 13333);
				break;

			case "GUTHIXi":
				if (!player.getItems().playerHasItem(21793)) {
					player.sendMessage("You must have a Imbued guthix cape in order to do this.");
					return;
				}
				player.getItems().deleteItem(21793, 1);
				player.getItems().addItem(21786, 1);
				player.getItems().addItem(21784, 1);
				player.getDH().sendItemStatement("You've combined the Imbued guthix cape and max cape.", 13335);
				break;
			
		case "SARADOMIN":
			if (!player.getItems().playerHasItem(2412)) {
				player.sendMessage("You must have a saradomin cape in order to do this.");
				return;
			}
			player.getItems().deleteItem(2412, 1);
			player.getItems().addItem(13331, 1);
			player.getItems().addItem(13332, 1);
			player.getDH().sendItemStatement("You've combined the saradomin cape and max cape.", 13331);
			break;
			
		case "ZAMORAK":
			if (!player.getItems().playerHasItem(2414)) {
				player.sendMessage("You must have a zamorak cape in order to do this.");
				return;
			}
			player.getItems().deleteItem(2414, 1);
			player.getItems().addItem(13333, 1);
			player.getItems().addItem(13334, 1);
			player.getDH().sendItemStatement("You've combined the zamorak cape and max cape.", 13333);
			break;
			
		case "GUTHIX":
			if (!player.getItems().playerHasItem(2413)) {
				player.sendMessage("You must have a guthix in order to do this.");
				return;
			}
			player.getItems().deleteItem(2413, 1);
			player.getItems().addItem(13335, 1);
			player.getItems().addItem(13336, 1);
			player.getDH().sendItemStatement("You've combined the guthix cape and max cape.", 13335);
			break;
			
		case "AVAS":
			if (!player.getItems().playerHasItem(10499)) {
				player.sendMessage("You must have a accumulator in order to do this.");
				return;
			}
			player.getItems().deleteItem(10499, 1);
			player.getItems().addItem(13337, 1);
			player.getItems().addItem(13338, 1);
			player.getDH().sendItemStatement("You've combined the accumulator and max cape.", 13337);
			break;
			
		case "ARDOUGNE":
			if (!player.getItems().playerHasItem(13124)) {
				player.sendMessage("You must have an ardougne cloak(4) in order to do this.");
				return;
			}
			player.getItems().deleteItem(13124, 1);
			player.getItems().addItem(20760, 1);
			player.getItems().addItem(20764, 1);
			player.getDH().sendItemStatement("You've combined the cloak and max cape.", 20760);
			break;
		}
	}

}
