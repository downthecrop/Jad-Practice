package ethos.model.players.skills.crafting;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;

public class BraceletMaking extends CraftingData {
	
	
	public static void craftBraceletDialogue(final Player c, final int itemUsed) {
		final int gem = itemUsed;
		String[] name = { "Combat Bracelet", "Regen Bracelet", "Zenyte Bracelet" };
		c.getPA().sendFrame164(8880);
		c.getPA().itemOnInterface(8883, 180, 11126);
		c.getPA().itemOnInterface(8884, 180, 11133);
		c.getPA().itemOnInterface(8885, 180, 19492);
		for (int i = 0; i < name.length; i++) {
			c.getPA().sendFrame126(name[i], 8889 + (i * 4));
		}
		c.leatherType = gem;
		c.braceletDialogue = true;
		return;
	}

	private static int amount;

	public static void craftBracelet(final Player c, final int buttonId) {
		c.getPA().removeAllWindows();
		
		if (c.playerIsCrafting == true) {
			return;
		}
		for (final braceletData l : braceletData.values()) {
			if (buttonId == l.getButtonId(buttonId)) {
				final int gold_bar = 2357;
				
				if (c.leatherType == l.getGem()) {
					if (c.playerLevel[12] < l.getLevel()) {
						c.sendMessage("You need a crafting level of " + l.getLevel() + " to make this.");
						return;
					}
					if (!c.getItems().playerHasItem(gold_bar)) {
						c.sendMessage("You do not have any gold bars to do this.");
						return;
					}
					if (!c.getItems().playerHasItem(c.leatherType, l.getAmount())) {
						c.getItems();
						c.getItems();
						c.sendMessage("You need " + l.getAmount() + " " + ItemAssistant.getItemName(c.leatherType).toLowerCase() + " to make "
								+ ItemAssistant.getItemName(l.getProduct()).toLowerCase() + ".");
						return;
					}
					c.startAnimation(1249);
					c.getPA().removeAllWindows();
					c.playerIsCrafting = true;
					amount = l.getAmount(buttonId);
					CycleEventHandler.getSingleton().addEvent(3, c, new CycleEvent() {
						@SuppressWarnings("unused")
						@Override
						public void execute(CycleEventContainer container) {
							if (c == null) {
								container.stop();
								return;
							}
							if (c.playerIsCrafting == true) {
								if (!c.getItems().playerHasItem(gold_bar)) {
									c.sendMessage("You have run out of gold bars.");
									container.stop();
									return;
								}
								if (!c.getItems().playerHasItem(c.leatherType, l.getAmount())) {
									c.sendMessage("You have run out of gems.");
									container.stop();
									return;
								}
								if (amount == 0) {
									container.stop();
									return;
								}
								c.getItems().deleteItem(gold_bar, c.getItems().getItemSlot(gold_bar), 1);
								c.getItems().deleteItem2(c.leatherType, l.getAmount());
								c.getItems().addItem(l.getProduct(), 1);
								c.getItems();
								c.getItems();
								c.sendMessage("You make a "
										+ ItemAssistant.getItemName(l.getProduct()) + ".");
								c.getPA().addSkillXP((int) l.getXP() * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.CRAFTING_EXPERIENCE), 12, true);
								c.startAnimation(1249);
								amount--;
								if (!c.getItems().playerHasItem(gold_bar)) {
									c.sendMessage("You have run out of gold bars.");
									container.stop();
									return;
								}
								if (!c.getItems().playerHasItem(c.leatherType, l.getAmount())) {
									c.sendMessage("You have run out of gems.");
									container.stop();
									return;
								}
							} else {
								container.stop();
							}
						}

						@Override
						public void stop() {
							c.playerIsCrafting = false;
							c.braceletDialogue = false;
						}
					}, 5);
				}
			}
		}
	}
}
