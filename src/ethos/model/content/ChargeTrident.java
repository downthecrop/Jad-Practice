package ethos.model.content;

import ethos.model.players.Player;

public class ChargeTrident {

	private Player player;

	public ChargeTrident(Player player) {
		this.player = player;
	}
	public void seas(String amount) {
		
		int price = player.getRechargeItems().hasItem(13129) ? 8000
				  : player.getRechargeItems().hasItem(13130) ? 6000
				  : player.getRechargeItems().hasItem(13131) ? 4000
				  : player.getRechargeItems().hasItem(13132) ? -1 : 10000;
		
		if (!player.getItems().playerHasItem(11907, 1)) {
			return;
		}
		if (player.getTridentCharge() >= 2500) {
			player.sendMessage("Your trident already has 2,500 charges.");
			return;
		}

		switch (amount) {
		case "TEN": // Plus 10
			if (!player.getItems().playerHasItem(995, price)) {
				player.sendMessage("You need at least "+price+" coins to add 10 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(554, 50)) {
				player.sendMessage("You need at least 50 fire runes to add 10 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(560, 10)) {
				player.sendMessage("You need at least 10 death rune to add 10 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(562, 10)) {
				player.sendMessage("You need at least 10 chaos rune to add 10 charges.");
				return;
			}
			player.getItems().deleteItem2(554, 50);
			player.getItems().deleteItem2(560, 10);
			player.getItems().deleteItem2(562, 10);
			player.getItems().deleteItem2(995, price);
			player.setTridentCharge(player.getTridentCharge() > 2489 ? 2500 : player.getTridentCharge() + 10);
			player.sendMessage("You successfully added 10 charges to your trident.");
			break;

		case "HUNDRED": // Plus 100
			if (!player.getItems().playerHasItem(995, price == -1 ? -1 : price * 10)) {
				player.sendMessage("You need at least " + price * 10 +" coins to add 100 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(554, 500)) {
				player.sendMessage("You need at least 500 fire runes to add 100 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(560, 100)) {
				player.sendMessage("You need at least 100 death rune to add 100 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(562, 100)) {
				player.sendMessage("You need at least 100 chaos rune to add 100 charges.");
				return;
			}
			player.getItems().deleteItem2(554, 500);
			player.getItems().deleteItem2(560, 100);
			player.getItems().deleteItem2(562, 100);
			player.getItems().deleteItem2(995, price == -1 ? -1 : price * 10);
			player.setTridentCharge(player.getTridentCharge() > 2399 ? 2500 : player.getTridentCharge() + 100);
			player.sendMessage("You successfully added 100 charges to your trident.");
			break;

		case "THOUSAND": // Plus 1000
			if (player.getTridentCharge() > 1500) {
				player.sendMessage("Doing this would equal your charges above the maximum, try a lower choice.");
				return;
			}
			if (!player.getItems().playerHasItem(995, price == -1 ? -1 : price * 100)) {
				player.sendMessage("You need at least " + price * 100 + " coins to add 1000 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(554, 5000)) {
				player.sendMessage("You need at least 5000 fire runes to fill it fully.");
				return;
			}
			if (!player.getItems().playerHasItem(560, 1000)) {
				player.sendMessage("You need at least 1000 death rune to add fill it fully.");
				return;
			}
			if (!player.getItems().playerHasItem(562, 1000)) {
				player.sendMessage("You need at least 1000 chaos rune to add fill it fully.");
				return;
			}
			player.getItems().deleteItem2(554, 5000);
			player.getItems().deleteItem2(560, 1000);
			player.getItems().deleteItem2(562, 1000);
			player.getItems().deleteItem2(995, price == -1 ? -1 : price * 100);
			player.setTridentCharge(player.getTridentCharge() > 1499 ? 2500 : player.getTridentCharge() + 1000);
			player.sendMessage("You successfully added 1.000 charges to your trident.");
			break;
		}

	}

	public void swamp(String amount) {

		if (!player.getItems().playerHasItem(12899, 1)) {
			return;
		}
		if (player.getToxicTridentCharge() >= 2500) {
			player.sendMessage("Your trident already has 2,500 charge.");
			return;
		}

		switch (amount) {
		case "TEN": // Plus 10
			if (!player.getItems().playerHasItem(12934, 100)) {
				player.sendMessage("You need at least 100 zulrah scales to add 10 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(554, 50)) {
				player.sendMessage("You need at least 50 fire runes to add 10 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(560, 10)) {
				player.sendMessage("You need at least 10 death rune to add 10 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(562, 10)) {
				player.sendMessage("You need at least 10 chaos rune to add 10 charges.");
				return;
			}
			player.getItems().deleteItem2(554, 50);
			player.getItems().deleteItem2(560, 10);
			player.getItems().deleteItem2(562, 10);
			player.getItems().deleteItem2(12934, 100);
			player.setToxicTridentCharge(player.getToxicTridentCharge() >= 2490 ? 2500 : player.getToxicTridentCharge() + 10);
			player.sendMessage("You successfully added 10 toxic charges to your trident.");
			break;

		case "HUNDRED": // Plus 100
			if (!player.getItems().playerHasItem(12934, 1000)) {
				player.sendMessage("You need at least 1.000 zulrah scales to add 100 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(554, 500)) {
				player.sendMessage("You need at least 500 fire runes to add 100 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(560, 100)) {
				player.sendMessage("You need at least 100 death rune to add 100 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(562, 100)) {
				player.sendMessage("You need at least 100 chaos rune to add 100 charges.");
				return;
			}
			player.getItems().deleteItem2(554, 500);
			player.getItems().deleteItem2(560, 100);
			player.getItems().deleteItem2(562, 100);
			player.getItems().deleteItem2(12934, 1000);
			player.setToxicTridentCharge(player.getToxicTridentCharge() >= 2400 ? 2500 : player.getToxicTridentCharge() + 100);
			player.sendMessage("You successfully added 100 charges to your trident.");
			break;

		case "THOUSAND": // Plus 1000
			if (!player.getItems().playerHasItem(12934, 10000)) {
				player.sendMessage("You need at least 10.000 zulrah scales to add 1000 charges.");
				return;
			}
			if (!player.getItems().playerHasItem(554, 5000)) {
				player.sendMessage("You need at least 5.000 fire runes to fill it fully.");
				return;
			}
			if (!player.getItems().playerHasItem(560, 1000)) {
				player.sendMessage("You need at least 1.000 death rune to add fill it fully.");
				return;
			}
			if (!player.getItems().playerHasItem(562, 1000)) {
				player.sendMessage("You need at least 1.000 chaos rune to add fill it fully.");
				return;
			}
			player.getItems().deleteItem2(554, 5000);
			player.getItems().deleteItem2(560, 1000);
			player.getItems().deleteItem2(562, 1000);
			player.getItems().deleteItem2(12934, 10000);
			player.setToxicTridentCharge(player.getToxicTridentCharge() >= 1500 ? 2500 : player.getToxicTridentCharge() + 1000);
			player.sendMessage("You successfully added 1.000 charges to your trident.");
			break;
		}
	}

}
