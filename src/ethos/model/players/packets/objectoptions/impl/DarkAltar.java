package ethos.model.players.packets.objectoptions.impl;

import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class DarkAltar {

	public static void handleDarkTeleportInteraction(Player c) {

		if (!c.getItems().playerHasItem(19685)) {
			c.sendMessage("You must have a dark totem to do this!");
		} else {
			c.getDH().sendDialogues(7286, 7283);
		}
	}

	public static void handlePortalInteraction(Player c) {

		if (!Boundary.isIn(c, Boundary.SKOTIZO_BOSSROOM)) {
			return;
		}
		Skotizo skotizo=c.getSkotizo();
		if (skotizo!=null) {
			skotizo.end(DisposeTypes.INCOMPLETE);
		} else {
			c.getPA().movePlayer(1665, 10046, 0);
		}
	}

	public static void handleRechargeInteraction(Player c) {

		int amount=c.getItems().getItemAmount(6646);
		if (amount==0) {
			c.sendMessage("You do not have any crystals to recharge.");
			return;
		}
		if (c.getItems().playerHasItem(6646, amount)) {
			c.startAnimation(716);
			c.gfx0(107);
			c.getItems().deleteItem(6646, amount);
			c.getItems().addItem(6651, amount);
			c.sendMessage("You recharged "+amount+" crystal"+(amount>1 ? "s" : "")+"!");
		}
	}

	public static void handleRechargeArcLight(Player c) {

		if (c.getItems().playerHasItem(19675, 1)&&c.getItems().playerHasItem(19677, 3)) {
			if (c.getArcLightCharge()<10000) {
				c.getDH().sendStatement("You add 1000 charges to your arclight!");
				c.setArcLightCharge(c.getArcLightCharge()+1000);
				c.nextChat=-1;
				c.getItems().deleteItem2(19677, 3);
			} else {
				c.setArcLightCharge(10000);
				c.getDH().sendStatement("You can only have a maximum of 10,000 charges.");
			}
		} else {
			c.sendMessage("You need 3 ancient shards to add 1000 charges.");
		}
	}

	public static void handleDarklightTransaction(Player c) {

		if (c.getItems().playerHasItem(6746, 1)&&c.getItems().playerHasItem(19677, 3)) {
			c.getDH().sendStatement("You turn your Darklight into an Arclight with 1000 charges.");
			c.sendMessage("Use your Arclight on the altar to add more charges!");
			c.setArcLightCharge(c.getArcLightCharge()+1000);
			c.nextChat=-1;
			c.getItems().deleteItem(6746, 1);
			c.getItems().deleteItem(19677, 3);
			c.getItems().addItemUnderAnyCircumstance(19675, 1);
		} else {
			c.sendMessage("You need 3 ancient shards to make an Arclight.");
		}
	}

}

