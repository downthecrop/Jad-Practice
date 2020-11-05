package ethos.model.players.skills.crafting;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.crafting.CraftingData.chargeOrbData;

public class OrbCharging {
	
	private static int amount;

	public static void chargeOrbs(final Player c, final int spellId, final int objectId) {
		if (c.playerIsCrafting == true) {
			return;
		}
		for (final chargeOrbData l : chargeOrbData.values()) {
			if (objectId == l.getObjectId(objectId)) {
				if (l.getSpell() == spellId) {
//					if (c.playerLevel[12] < l.getLevel()) {
//						c.sendMessage("You need a crafting level of " + l.getLevel() + " to make this.");
//						c.getPA().removeAllWindows();
//						return;
//					}
					if (!c.getItems().playerHasItem(567, l.getOrbAmount())) {
						c.sendMessage("You need " + l.getOrbAmount() + " " + ItemAssistant.getItemName(567).toLowerCase() + " to make "
								+ ItemAssistant.getItemName(l.getProduct()).toLowerCase() + ".");
						c.getPA().removeAllWindows();
						return;
					}
					c.getPA().removeAllWindows();
					c.playerIsCrafting = true;
					amount = l.getAmount(objectId);
					CycleEventHandler.getSingleton().addEvent(3, c, new CycleEvent() {
						@SuppressWarnings("unused")
						@Override
						public void execute(CycleEventContainer container) {
							if (!c.getCombat().checkMagicReqs(l.getSpellConfig())) {
								container.stop();
								return;
							}
							if (c == null) {
								container.stop();
								return;
							}
							if (c.playerIsCrafting == true) {
								if (!c.getItems().playerHasItem(567, l.getOrbAmount())) {
									c.sendMessage("You have run out of unpowered orbs.");
									container.stop();
									return;
								}
								if (amount == 0) {
									container.stop();
									return;
								}
								c.startAnimation(726);
								c.gfx100(l.getOrbGfx());
								c.getItems().deleteItem2(567, l.getOrbAmount());
								c.getItems().addItem(l.getProduct(), 1);
								c.sendMessage("You make an " + ItemAssistant.getItemName(l.getProduct()) + ".");
								c.getPA().addSkillXP((int) l.getXP() * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : 40), 6, true);
								amount--;
								if (!c.getItems().playerHasItem(567, l.getOrbAmount())) {
									c.sendMessage("You have run out of unpowered orbs.");
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
							c.battlestaffDialogue = false;
						}
					}, 3);
				}
			}
		}
	}

}
