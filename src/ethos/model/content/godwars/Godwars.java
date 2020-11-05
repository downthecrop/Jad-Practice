package ethos.model.content.godwars;

import java.util.HashMap;
import java.util.Map;

import ethos.Server;
import ethos.model.content.instances.InstancedAreaManager;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerAssistant;
import ethos.util.Misc;

public class Godwars {

	private static final int KC_REQUIRED = 10;
	public static final int KEY_ID = 11942;

	public static final Boundary GODWARS_AREA = new Boundary(2819, 5255, 2942, 5375);

	private Player player;
	private Map<God, Integer> killcount;

	public Godwars(Player player) {
		this.player = player;
		initialize();
	}

	/**
	 * Sets all killcount values to 0.
	 */
	public void initialize() {
		killcount = new HashMap<>();
		for (God god : God.values()) {
			killcount.put(god, 0);
		}
	}

	/**
	 * Handles entering a boss room.
	 * 
	 * @param god The god to which the room belongs to.
	 */
	public void enterBossRoom(God god) {
		if (killcount.get(god) >= KC_REQUIRED) {
			killcount.put(god, killcount.get(god) - KC_REQUIRED);
		} else if (player.getItems().playerHasItem(KEY_ID)) {
			player.getItems().deleteItem(KEY_ID, 1);
		} else {
			player.sendMessage("You need to kill " + (KC_REQUIRED - killcount.get(god)) + " more " + Misc.capitalizeJustFirst(god.name()) + " creatures before you can enter.");
			return;
		}
		int previousHeight = player.heightLevel;

		switch (god) {
		case SARADOMIN:
			player.getPA().movePlayer(2907, 5265, getInstanceHeight());
			break;
		case ZAMORAK:
			player.getPA().movePlayer(2925, 5331, getInstanceHeight() + 2);
			break;
		case BANDOS:
			player.getPA().movePlayer(2864, 5354, getInstanceHeight() + 2);
			break;
		case ARMADYL:
			player.getPA().movePlayer(2839, 5296, getInstanceHeight() + 2);
			break;
		}
		if (player.heightLevel != previousHeight) {
			Server.itemHandler.reloadItems(player);
		}
	}

	/**
	 * Returns the height level of the instance which the player should be teleported to.
	 * 
	 * @return The height level of the instance.
	 */
	private int getInstanceHeight() {
		if (player.getMode().isIronman() || player.getMode().isUltimateIronman()) {
			return InstancedAreaManager.GODWARS_IRONMAN_HEIGHT;
		} else {
			return 0;
		}
	}

	/**
	 * Increases the amount of minions slain of a certain god.
	 * 
	 * @param god The god of which the killcount should be increased.
	 */
	public void increaseKillcount(God god) {
		killcount.put(god, killcount.get(god) + 1);
	}

	public void increaseKillcountByTeleportationDevice(God god, int amount) {
		killcount.put(god, killcount.get(god) + amount);
	}

	/**
	 * Updates the killcount values on the interface.
	 */
	public void drawInterface() {
		PlayerAssistant assistant = player.getPA();
		assistant.sendFrame126(Integer.toString(killcount.get(God.ARMADYL)), 16216);
		assistant.sendFrame126(Integer.toString(killcount.get(God.BANDOS)), 16217);
		assistant.sendFrame126(Integer.toString(killcount.get(God.SARADOMIN)), 16218);
		assistant.sendFrame126(Integer.toString(killcount.get(God.ZAMORAK)), 16219);
	}
}
