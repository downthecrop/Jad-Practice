package ethos.model.content;

import ethos.Config;
import ethos.ServerState;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class DiceHandler {

	public static final int ROLL_TIMER = 1000, DICE_BAG = 15084;
	public static final Boundary DICING_AREA = new Boundary(3154, 3479, 3175, 3500);

	interface Data {
		public int diceId();

		public int diceSize();

		public int diceGfx();
	}

	enum Dice implements Data {
		DIE_6_SIDES(15086, 6, 2072), DICE_6_SIDES(15088, 12, 2074), DIE_8_SIDES(15090, 8, 2071), DIE_10_SIDES(15092, 10,
				2070), DIE_12_SIDES(15094, 12, 2073), DIE_20_SIDES(15096, 20, 2068), DICE_UP_TO_100(15098, 100,
						2075), DIE_4_SIDES(15100, 4, 2069);

		private int id, sides, gfx;

		Dice(int id, int sides, int gfx) {
			this.id = id;
			this.sides = sides;
			this.gfx = gfx;
		}

		@Override
		public int diceId() {
			return id;
		}

		@Override
		public int diceSize() {
			return sides;
		}

		@Override
		public int diceGfx() {
			return gfx;
		}
	}

	/**
	 * Handles the rolling of the dice to a player.
	 * 
	 * @param c
	 *            The player.
	 * @param roll
	 *            What the player rolled on the dice.
	 * @param item
	 *            The id of the dice.
	 */
	public static void selfRoll(Player c, int roll, int item) {
		c.sendMessage("You rolled @red@" + roll + "@bla@ on the " + ItemAssistant.getItemName(item) + ".");
	}

	/**
	 * Handles selecting the dice
	 * 
	 * @param c
	 *            The player.
	 * @param item
	 *            The dice id.
	 * @return Whether or not a dice were selected.
	 */
	public static boolean selectDice(Player c, int item) {
		for (Dice d : Dice.values()) {
			if (item == d.diceId() || item == DICE_BAG) {
				c.getDH().sendOption5(ItemAssistant.getItemName(Dice.DIE_6_SIDES.diceId()),
						ItemAssistant.getItemName(Dice.DICE_6_SIDES.diceId()),
						ItemAssistant.getItemName(Dice.DIE_8_SIDES.diceId()),
						ItemAssistant.getItemName(Dice.DIE_10_SIDES.diceId()), "Next Page");
				c.diceItem = item;
				return true;
			}
		}
		return false;
	}

	/**
	 * Handles all the clicking for the dice.
	 * 
	 * @param c
	 *            The player.
	 * @param actionButtonId
	 *            Action button id of what is clicked.
	 * @return Whether or not a click was handled.
	 */
	public static boolean handleClick(Player c, int actionButtonId) {
		int[][] dice = { { Dice.DIE_6_SIDES.diceId() }, { Dice.DICE_6_SIDES.diceId() }, { Dice.DIE_8_SIDES.diceId() },
				{ Dice.DIE_10_SIDES.diceId() }, { Dice.DIE_12_SIDES.diceId() }, { Dice.DIE_20_SIDES.diceId() },
				{ Dice.DICE_UP_TO_100.diceId() }, { Dice.DIE_4_SIDES.diceId() } };
		int DICE = 0;
		if (actionButtonId - 9190 >= 0 && actionButtonId - 9190 <= 5) {
			if (c.page == 0) {
				c.getPA().removeAllWindows();
				if (actionButtonId - 9190 <= 3) {
					if (c.getItems().playerHasItem(c.diceItem, 1)) {
						c.getItems().deleteItem(c.diceItem, 1);
						c.getItems().addItem(dice[actionButtonId - 9190][DICE], 1);
					}
				} else {
					c.getDH().sendOption5(ItemAssistant.getItemName(Dice.DIE_12_SIDES.diceId()),
							ItemAssistant.getItemName(Dice.DIE_20_SIDES.diceId()),
							ItemAssistant.getItemName(Dice.DICE_UP_TO_100.diceId()),
							ItemAssistant.getItemName(Dice.DIE_4_SIDES.diceId()), "Return");
					c.page = 1;
				}
			} else if (c.page == 1) {
				c.getPA().removeAllWindows();
				if (actionButtonId - 9190 <= 3) {
					if (c.getItems().playerHasItem(c.diceItem, 1)) {
						c.getItems().deleteItem(c.diceItem, 1);
						c.getItems().addItem(dice[actionButtonId - 9186][DICE], 1);
					}
				} else {
					c.getPA().closeAllWindows();
				}
				c.page = 0;
			}
			return true;
		}
		return false;
	}

	public static boolean inDicingArea(Player c) {
		return Boundary.isIn(c, DICING_AREA);
	}

	public static void rollDice(Player c) {
		if (!inDicingArea(c)) {
			c.sendMessage("You need to be in the ::dice area to roll dices.");
			return;
		}
		if (System.currentTimeMillis() - c.diceDelay >= 5000) {
			int roll = Misc.random(99) + 1;
			if (Config.SERVER_STATE == ServerState.PUBLIC_PRIMARY) {
				// TODO: Log handling
			}
			PlayerHandler.nonNullStream()
					.filter(player -> player.distanceToPoint(c.absX, c.absY) < 20 && player.heightLevel == 0)
					.forEach(player -> {
						player.sendMessage("[@red@DICE@bla@] @cr21@ @blu@" + Misc.capitalizeJustFirst(c.playerName)
								+ " @bla@rolled @red@" + roll + "@bla@ on the percentile dice.");
						if (roll < 55) {
							player.sendMessage("[@red@DICE@bla@] @cr21@ The gambler @blu@"
									+ Misc.capitalizeJustFirst(c.playerName) + " @bla@wins.");
						} else {
							player.sendMessage(
									"[@red@DICE@bla@] @cr21@ The gambler @blu@" + Misc.capitalizeJustFirst(c.playerName)
											+ " @bla@loses. Please pay out the winnings!");
						}
					});
			c.diceDelay = System.currentTimeMillis();
		} else {
			c.sendMessage("You must wait 5 seconds to roll dice again.");
		}
	}

}