package ethos.model.players.skills.crafting;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.Item;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;

public class GlassBlowing extends GlassData {

	private static int amount;

	public static void glassBlowing(final Player c, final int buttonId) {
		if (c.playerIsCrafting) {
			return;
		}
		for (final glassData g : glassData.values()) {
			if (buttonId == g.getButtonId(buttonId)) {
				if (c.playerLevel[12] < g.getLevelReq()) {
					c.sendMessage("You need a crafting level of " + g.getLevelReq() + " to make this.");
					c.getPA().removeAllWindows();
					return;
				}
				if (!c.getItems().playerHasItem(1775, 1)) {
					c.sendMessage("You have run out of molten glass.");
					return;
				}
				c.startAnimation(884);
				c.getPA().removeAllWindows();
				c.playerIsCrafting = true;
				amount = g.getAmount(buttonId);
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c == null || c.disconnected || c.getSession() == null) {
							stop();
							return;
						}
						if (c.playerIsCrafting) {
							if (amount == 0) {
								container.stop();
								return;
							}
							if (!c.getItems().playerHasItem(1775, 1)) {
								c.sendMessage("You have run out of molten glass.");
								container.stop();
								return;
							}
							c.getItems().deleteItem(1775, 1);
							c.getItems().addItem(g.getNewId(), 1);
							c.sendMessage("You make a " + Item.getItemName(g.getNewId()) + ".");
							c.getPA().addSkillXP((int) g.getXP() * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.CRAFTING_EXPERIENCE), 12, true);
							c.startAnimation(884);
							amount--;
						} else {
							container.stop();
						}
					}
					@Override
					public void stop() {
						c.startAnimation(65535);
						c.playerIsCrafting = true;
					}
				}, 3);
			}
		}
	}

	public static void makeGlass(final Player c, final int itemUsed,
			final int usedWith) {
		final int blowPipeId = (itemUsed == 1785 ? usedWith : itemUsed);
		c.getPA().showInterface(11462);
		for (final glassData g : glassData.values()) {
			if (blowPipeId == g.getNewId()) {
				c.getItems().deleteItem(1759, 1);
				c.getItems().addItem(g.getNewId(), 1);
				c.getPA().addSkillXP(4, 12, true);
			}
		}
	}

}