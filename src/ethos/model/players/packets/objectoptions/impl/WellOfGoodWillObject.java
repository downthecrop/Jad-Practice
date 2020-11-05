package ethos.model.players.packets.objectoptions.impl;

import ethos.model.content.wogw.Wogw;
import ethos.model.content.wogw.Wogwitems;
import ethos.model.players.Player;
import ethos.util.Misc;

public class WellOfGoodWillObject {

	private static void sendInterfaces(Player c) {

		c.getPA().showInterface(38000);
		c.getPA().sendFrame171(1, 38020);
		c.getPA().sendChangeSprite(38006, (byte) 1);
		c.getPA().sendChangeSprite(38007, (byte) 1);
		c.getPA().sendChangeSprite(38008, (byte) 1);
		c.getPA().sendFrame126(""+Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_EXPERIENCE)+"/50M", 38012);
		c.getPA().sendFrame126(""+Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_PC_POINTS)+"/50M", 38013);
		c.getPA().sendFrame126(""+Misc.getValueWithoutRepresentation(Wogw.MONEY_TOWARDS_DOUBLE_DROPS)+"/100M", 38014);

	}

	private static boolean canInteract(Player c, int itemId) {

		if (itemId==12926) {
			c.sendMessage("Please empty your blowpipe before donating it!");
			return false;
		}
		return true;
	}

	public static void handleInteraction(Player c, int itemId) {

		if (canInteract(c, itemId)) {
			if (itemId==995) {
				sendInterfaces(c);
				for (int i=0; i<5; i++) {
					if (Wogw.lastDonators[i]==null) {
						c.getPA().sendFrame126("None", 38033+i);
						continue;
					}
					c.getPA().sendFrame126(Wogw.lastDonators[i], 38033+i);
				}
			}
			for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
				if (itemId==t.getItemId()) {
					c.wellItem=itemId;
					c.wellItemPrice=t.getItemWorth();
					c.getDH().sendDialogues(784, -1);
				}
			}
		}
	}
}
