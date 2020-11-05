package ethos.model.players.skills.herblore;

import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.players.Player;
import ethos.util.Misc;

public class UnfCreator {
	
	public enum UnfinishedPotions {
		GUAM_POTION(new GameItem(91), new GameItem(250), 1), //Attack potion
		MARRENTILL_POTION(new GameItem(93), new GameItem(252), 5), //Antipoison
		TARROMIN_POTION(new GameItem(95), new GameItem(254), 12), //Strength potion
		HARRALANDER_POTION(new GameItem(97), new GameItem(256), 22), //Restore potion, Energy potion & Combat potion
		RANARR_POTION(new GameItem(99), new GameItem(258), 30), //Prayer potion
		TOADFLAX_POTION(new GameItem(3002), new GameItem(2999), 34), //Agility potion & Saradomin brew
		IRIT_POTION(new GameItem(101), new GameItem(260), 45), //Super attack & Fishing potion
		AVANTOE_POTION(new GameItem(103), new GameItem(262), 50), //Super energy
		KWUARM_POTION(new GameItem(105), new GameItem(264), 55), //Super strength & Weapon poison
		SNAPDRAGON_POTION(new GameItem(3004), new GameItem(3001), 63), //Super restore
		CADANTINE_POTION(new GameItem(107), new GameItem(266), 66), //Super defence
		LANTADYME(new GameItem(2483), new GameItem(2482), 69), //Antifire, Magic potion
		DWARF_WEED_POTION(new GameItem(109), new GameItem(268), 72), //Ranging potion
		TORSTOL_POTION(new GameItem(111), new GameItem(270), 78); //Zamorak brew
		
		
		private final GameItem potion, ingredient;
		private int levelReq;
		
		private UnfinishedPotions(GameItem potion, GameItem ingredient, int levelReq) {
			this.potion = potion;
			this.ingredient = ingredient;
			this.levelReq = levelReq;
		}
		
		public GameItem getPotion() {
			return potion;
		}
		
		public GameItem getHerb() {
			return ingredient;
		}
		
		public int getLevelReq() {
			return levelReq;
		}
		
		public static UnfinishedPotions forId(int i) {
			for(UnfinishedPotions unf : UnfinishedPotions.values()) {
				if (unf.getHerb().getId() == i) {
					return unf;
				}
			}
			return null;
		}
	}
	
	public static boolean setPotionToCreate(final Player player, final GameItem itemUsed) {
		final UnfinishedPotions unf = UnfinishedPotions.forId(itemUsed.getId());
		if (unf == null) {
			player.getPA().closeAllWindows();
			return false;
		}
		if (player.getLevelForXP(player.playerXP[player.playerHerblore]) < unf.getLevelReq()) {
			player.sendMessage("You need a Herblore level of " + unf.getLevelReq() + " to make this potion.");
			player.getPA().closeAllWindows();
			return false;
		}
		player.unfPotHerb = itemUsed.getId();
		player.unfPotAmount = player.getItems().getItemAmount(itemUsed.getId());
		player.sendMessage("You have " + player.unfPotAmount + " x " + Item.getItemName(itemUsed.getId() - 1) + ". " + player.unfPotAmount + " x 250gp = " + Misc.format(player.getItems().getItemAmount(itemUsed.getId()) * 250) + "gp.");
		player.getDH().sendDialogues(810, 5449);
		return true;
	}
	
	public static boolean makeUnfinishedPotion(final Player player, final GameItem itemUsed) {
		final UnfinishedPotions unf = UnfinishedPotions.forId(itemUsed.getId());
		if (unf == null) {
			return false;
		}
		if (player.getItems().playerHasItem(player.unfPotHerb, player.unfPotAmount)) {
			if (!player.getItems().playerHasItem(228, player.unfPotAmount)) {
				player.sendMessage("You much have the equiv amount of noted vials of water to do this.");
				player.getPA().closeAllWindows();
				return false;
			}
			if (!player.getItems().playerHasItem(995, player.unfPotAmount * 250)) {
				player.sendMessage(
						"You do not have enough money to do this. (" + Misc.format(player.unfPotAmount * 250) + "gp)");
				player.getPA().closeAllWindows();
				return false;
			}
			player.getItems().deleteItem(player.unfPotHerb, player.unfPotAmount);
			player.getItems().deleteItem(228, player.unfPotAmount);
			player.getItems().deleteItem(995, player.unfPotAmount * 250);
			player.getItems().addItem(unf.getPotion().getId() + 1, player.unfPotAmount);
			player.sendMessage("Successfully created " + player.unfPotAmount + " x "
					+ Item.getItemName(unf.getPotion().getId()) + ".");
			player.getPA().closeAllWindows();
		}
		return false;
	}

}
