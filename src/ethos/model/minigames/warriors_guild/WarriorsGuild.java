package ethos.model.minigames.warriors_guild;

import java.util.Arrays;
import java.util.Optional;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.SkillcapePerks;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 20, 2013
 */
public class WarriorsGuild {

	public static final Boundary CYCLOPS_BOUNDARY = new Boundary(2833, 3530, 2880, 3560, 2);

	public static final Boundary[] WAITING_ROOM_BOUNDARY = new Boundary[] { new Boundary(2838, 3536, 2846, 3542, 2), new Boundary(2847, 3537, 2847, 3537, 2) };

	private Player player;

	private boolean active;

	public static final int[][] DEFENDER_DATA = { { 8844, 10 }, { 8845, 15 }, { 8846, 20 }, { 8847, 22 }, { 8848, 25 }, { 8849, 30 }, { 8850, 35 }, { 12954, 50 } };

	public WarriorsGuild(Player player) {
		this.player = player;
	}

	public void cycle() {
		CycleEventHandler.getSingleton().stopEvents(this);
		setActive(true);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer event) {
				if (player == null || player.disconnected) {
					event.stop();
					return;
				}
				if (!Boundary.isIn(player, CYCLOPS_BOUNDARY) || Boundary.isIn(player, WAITING_ROOM_BOUNDARY)) {
					setActive(false);
					event.stop();
					return;
				}
				if (SkillcapePerks.ATTACK.isWearing(player) || SkillcapePerks.isWearingMaxCape(player)) {
					if (player.debugMessage) {
						player.sendMessage("Has cape. Yeet");
					}
				} else {
					if (!player.getItems().playerHasItem(8851, 10)) {
						removeFromRoom();
						setActive(false);
						event.stop();
						return;
					}
				}
				if (SkillcapePerks.ATTACK.isWearing(player) || SkillcapePerks.isWearingMaxCape(player)) {
					player.sendMessage("Your cape negates the need for warriors guild tokens.", 255);
				} else {
					player.getItems().deleteItem2(8851, 20);
					player.sendMessage("You notice some of your warrior guild tokens disappear..", 255);
				}
			}

			@Override
			public void stop() {

			}

		}, 200);
	}

	public void handleDoor() {
		//Walking out of the cyclops room
		if (player.absX == 2847 && player.absY == 3540 || player.absX == 2847 && player.absY == 3541) {
			CycleEventHandler.getSingleton().stopEvents(this);
			//player.getPA().movePlayer(player.absX - 1, player.absY, 2);
			player.getAgilityHandler().move(player, -1, 0, 0x333, -1);
			
		//Walking into the cyclops room
		} else if (player.absX == 2846 && player.absY == 3540 || player.absX == 2846 && player.absY == 3541 || Boundary.isIn(player, WAITING_ROOM_BOUNDARY)) {
			if (player.getItems().playerHasItem(8851, 200) || SkillcapePerks.ATTACK.isWearing(player) || SkillcapePerks.isWearingMaxCape(player)) {
				int current = currentDefender();
				if (current == -1) {
					player.getDH().sendNpcChat4("You are not in the possession of a defender.", "You must kill cyclops to obtain a defender.",
							"The fee for entering the area is 200 tokens.", "Do you want to enter?", 2461, "Kamfreena");
					player.nextChat = 627;
				} else {
					player.getDH().sendNpcChat3("You are currently in possession of a " + ItemAssistant.getItemName(current) + ".",
							"It will cost 200 tokens to re-enter the cyclops area.", "Do you want to enter? It will cost you.", 2461, "Kamfreena");
					player.nextChat = 627;
				}
			} else {
				player.getDH().sendNpcChat2("You need at least 200 warrior guild tokens.", "You can get some by operating the armour animator.", 2461, "Kamfreena");
				player.nextChat = 0;
			}
		}
	}

	/**
	 * Attempts to return the value of the defender the player is wearing or is in posession of in their inventory.
	 * 
	 * @return -1 will be returned in the case that the player does not have a defender
	 */
	private int currentDefender() {
		for (int index = DEFENDER_DATA.length - 1; index > -1; index--) {
			int[] defender = DEFENDER_DATA[index];
			if (player.getItems().playerHasItem(defender[0]) || player.getItems().isWearingItem(defender[0])) {
				return defender[0];
			}
		}
		return -1;
	}

	/**
	 * Attempts to return the next best defender.
	 * 
	 * @return The first defender, bronze, if the player doesnt have a defender. If the player has the best it will return the best. If either of the afforementioned conditions are
	 *         not met, the next best defender is returned.
	 */
	private int nextDefender() {
		int defender = currentDefender();
		if (defender == -1) {
			return DEFENDER_DATA[0][0];
		}
		int best = DEFENDER_DATA[DEFENDER_DATA.length - 1][0];
		if (best == defender) {
			return best;
		}
		int index = indexOf(defender);
		if (index != -1) {
			defender = DEFENDER_DATA[index + 1][0];
		}
		return defender;
	}

	/**
	 * Attempts to retrieve the index in the array of the defender
	 * 
	 * @param defender the defender
	 * @return -1 will be returned if the defender cannot be found
	 */
	private int indexOf(int defender) {
		for (int index = 0; index < DEFENDER_DATA.length; index++) {
			if (defender == DEFENDER_DATA[index][0]) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Retrieves the drop chance of the next best defender the player can receive.
	 * 
	 * @return the chance of the dropped dagger.
	 */
	private int chance() {
		Optional<int[]> defender = Arrays.stream(DEFENDER_DATA).filter(data -> data[0] == nextDefender()).findFirst();
		return defender.isPresent() ? defender.get()[1] : 0;
	}

	public void dropDefender(int x, int y) {
		int amount = player.getItems().getItemAmount(8851);
		if (isActive() && Boundary.isIn(player, CYCLOPS_BOUNDARY) && !Boundary.isIn(player, WAITING_ROOM_BOUNDARY) && amount > 1) {
			int chance = chance();
			int current = currentDefender();
			int item = current == -1 ? DEFENDER_DATA[0][0] : nextDefender();
			if (Misc.random(chance) == 0) {
				Server.itemHandler.createGroundItem(player, item, x, y, 2, 1, player.getIndex());
				player.sendMessage("@blu@The cyclops dropped a " + ItemAssistant.getItemName(item) + " on the ground.", 600000);
			}
		}
	}

	private void removeFromRoom() {
		player.getPA().movePlayer(2846, 3540, 2);
		player.getDH().sendStatement("You do not have enough tokens to continue.");
		player.nextChat = 0;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
