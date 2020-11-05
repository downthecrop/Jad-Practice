package ethos.model.players.skills;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

/**
 * Class BowStringing contains the data for stringing the bow and the bow string together.
 */

public class BowStringing extends StringingData {

	private static int BOW_STRING = 1777;
	private static int CROSSBOW_STRING = 9438;

	/**
	 * This void contains the cycle event to string the unstrung bow and the bow string together playingSkilling[9] = fletching,
	 */

	public static void stringCrossBow(final Player c, final int itemUsed, final int usedWith) {
		if (c.playerSkilling[9]) {
			return;
		}
		final int itemId = (itemUsed == CROSSBOW_STRING ? usedWith : itemUsed);
		for (final stringingData g : stringingData.values()) {
			if (itemId == g.unStrung()) {
				if (c.playerLevel[9] < g.getLevel()) {
					c.sendMessage("You need a fletching level of " + g.getLevel() + " to string this bow.");
					return;
				}
				if (!c.getItems().playerHasItem(itemId)) {
					return;
				}
				c.playerSkilling[9] = true;
				c.startAnimation(g.getAnimation());
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.playerSkilling[9] == true) {
							if (c.getItems().playerHasItem(itemId)) {
								c.startAnimation(g.getAnimation());
							} else {
								container.stop();
							}
						} else {
							container.stop();
						}
					}

					@Override
					public void stop() {
						c.playerSkilling[9] = false;
					}
				}, 3);
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.playerSkilling[9] == true) {
							if (c.getItems().playerHasItem(itemId)) {
								c.getItems().deleteItem(itemId, 1);
								c.getItems().deleteItem(CROSSBOW_STRING, 1);
								c.getItems().addItem(g.Strung(), 1);
								c.getPA().addSkillXP((int) g.getXP(), 9, true);
								c.sendMessage("You attach the crossbow string to the " + ItemAssistant.getItemName(itemId).toLowerCase() + ".");
								c.startAnimation(g.getAnimation());
							} else {
								container.stop();
							}
						} else {
							container.stop();
						}
					}

					@Override
					public void stop() {
						c.playerSkilling[9] = false;
					}
				}, 2);
			}
		}
	}

	public static void stringBow(final Player c, final int itemUsed, final int usedWith) {
		if (c.playerSkilling[9]) {
			return;
		}
		final int itemId = (itemUsed == BOW_STRING ? usedWith : itemUsed);
		for (final stringingData g : stringingData.values()) {
			if (itemId == g.unStrung()) {
				if (c.playerLevel[9] < g.getLevel()) {
					c.sendMessage("You need a fletching level of " + g.getLevel() + " to string this bow.");
					return;
				}
				if (!c.getItems().playerHasItem(itemId)) {
					return;
				}
				c.playerSkilling[9] = true;
				c.startAnimation(g.getAnimation());
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.playerSkilling[9] == true) {
							if (c.getItems().playerHasItem(itemId)) {
								c.startAnimation(g.getAnimation());
							} else {
								container.stop();
							}
						} else {
							container.stop();
						}
					}

					@Override
					public void stop() {
						c.playerSkilling[9] = false;
					}
				}, 3);
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.playerSkilling[9] == true) {
							if (c.getItems().playerHasItem(itemId)) {
								switch (g.Strung()) {
								case 853:
									if (Boundary.isIn(c, Boundary.SEERS_BOUNDARY)) {
										c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.STRING_MAPLE_SHORT);
									}
								}
								c.getItems().deleteItem(itemId, 1);
								c.getItems().deleteItem(BOW_STRING, 1);
								c.getItems().addItem(g.Strung(), 1);
								c.getPA().addSkillXP((int) g.getXP(), 9, true);
								c.sendMessage("You attach the bow string to the " + ItemAssistant.getItemName(itemId).toLowerCase() + ".");
								c.startAnimation(g.getAnimation());
							} else {
								container.stop();
							}
						} else {
							container.stop();
						}
					}

					@Override
					public void stop() {
						c.playerSkilling[9] = false;
					}
				}, 2);
			}
		}
	}
}