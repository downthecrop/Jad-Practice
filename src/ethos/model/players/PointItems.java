package ethos.model.players;

import java.util.Map;

import ethos.Server;

public class PointItems {

	private Player player;

	public PointItems(Player player) {
		this.player = player;
	}

	public static final int[][] ITEM_POINT_LIST = { { 14484, 200 }, { 1050, 100 }, { 1040, 95 }, { 1042, 95 }, { 1044, 95 }, { 1046, 95 }, { 1048, 95 }, { 1038, 95 },
			{ 13899, 90 }, { 13902, 85 }, { 13905, 85 }, { 11924, 85 }, { 11926, 85 }, { 12006, 85 }, { 11907, 85 }, { 13893, 80 }, { 13887, 80 }, { 13890, 75 }, { 13884, 75 },
			{ 13896, 75 }, { 11802, 80 }, { 11061, 70 }, { 3204, 70 }, { 11785, 70 }, { 11791, 70 }, { 11804, 60 }, { 11806, 60 }, { 11808, 60 }, { 11832, 60 }, { 11834, 60 },
			{ 12436, 50 }, { 4084, 50 }, { 1037, 40 }, { 1419, 35 }, { 11144, 40 }, { 11283, 40 }, { 6199, 50 }, { 11838, 35 }, { 12851, 40 }, { 10352, 40 }, { 10350, 40 },
			{ 10348, 40 }, { 10346, 40 }, { 10344, 40 }, { 10342, 40 }, { 10340, 40 }, { 10338, 40 }, { 10336, 40 }, { 10334, 40 }, { 10332, 40 }, { 10330, 40 }, { 10887, 30 },
			{ 12000, 40 }, { 11235, 35 }, { 6585, 25 }, { 11836, 10 }, { 4708, 15 }, { 4710, 15 }, { 4712, 15 }, { 4714, 15 }, { 4716, 20 }, { 4718, 20 }, { 4720, 20 },
			{ 4722, 20 }, { 4724, 15 }, { 4726, 15 }, { 4728, 15 }, { 4730, 15 }, { 4732, 15 }, { 4734, 15 }, { 4736, 15 }, { 4738, 15 }, { 4745, 15 }, { 4747, 15 }, { 4749, 15 },
			{ 4751, 15 }, { 4753, 15 }, { 4755, 15 }, { 4757, 15 }, { 4759, 15 }, { 12603, 10 }, { 12601, 10 }, { 12605, 10 }, { 11773, 8 }, { 11778, 8 }, { 11772, 8 },
			{ 11771, 8 }, { 11770, 8 }, { 6737, 5 }, { 6735, 5 }, { 6733, 5 }, { 6731, 5 },

	};

	public boolean useItem(int itemId) {
		for (int i = 0; i < ITEM_POINT_LIST.length; i++) {
			if (itemId == ITEM_POINT_LIST[i][0]) {
				return true;
			}
		}
		player.sendMessage("The gods are not interested in this offering.");
		return false;
	}

	public void sendConfirmation(int itemId) {
		for (int i = 0; i < ITEM_POINT_LIST.length; i++) {
			if (itemId == ITEM_POINT_LIST[i][0]) {
				player.itemId = itemId;
				player.getDH().sendStatement("Are you sure you wish to give up the item " + ethos.model.items.Item.getItemName(itemId) + "?",
						"In return, " + ITEM_POINT_LIST[i][1] + " points will be added to your amount contributed.", "@red@You will not be able to undo this action.");
				player.nextChat = 100;
			}
		}
	}

	public void giveReward() {
		for (int i = 0; i < ITEM_POINT_LIST.length; i++) {
			if (player.itemId == ITEM_POINT_LIST[i][0]) {
				if (!player.getItems().playerHasItem(player.itemId, 1)) {
					player.sendMessage("You must have the item offered.");
					return;
				}
				int currentAmount = 0;
				Map<String, Integer> map = Server.getServerData().getWellOfGoodWill().getEntries();
				if (map.containsKey(player.playerName.toLowerCase())) {
					currentAmount = map.get(player.playerName.toLowerCase());
				}
				long offset = (long) currentAmount + ITEM_POINT_LIST[i][1];
				if (offset > Integer.MAX_VALUE) {
					player.sendMessage("You have already contributed the maximum amount to the gods.");
					player.getPA().closeAllWindows();
					return;
				}

				if (player.getItems().playerHasItem(player.itemId, 1)) {
					player.getItems().deleteItem(player.itemId, 1);
					player.getDH().sendStatement("The gods are very pleased with your offering, " + player.playerName + ".",
							ITEM_POINT_LIST[i][1] + " points have been added to your total amount contributed.");
					Server.getServerData().getWellOfGoodWill().update(player.playerName.toLowerCase(), ITEM_POINT_LIST[i][1]);
					player.nextChat = 0;
					player.itemId = 0;
				}
				break;
			}
		}
	}
}
