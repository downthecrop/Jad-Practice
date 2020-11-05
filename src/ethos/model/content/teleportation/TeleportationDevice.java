package ethos.model.content.teleportation;

import ethos.model.content.godwars.God;
import ethos.model.players.Player;
import ethos.util.Misc;

public class TeleportationDevice {

	/**
	 * Data used for each teleport
	 */
	private static final int[][] COORDINATE_DATA = { 
			{ 3184, 3944, 0, 60_000 }, // 0 Resource area
			{ 2271, 4681, 0, 25_000 }, // 1 King Black Dragon lair
			{ 2880, 5311, 2, 80_000 }, // 2 GWD
			{ 2945, 4384, 2, 100_000 }, //Corporeal Beast
	};

	/**
	 * Executing the teleportation device
	 * @param player	The player who is executing the device
	 * @param i			The teleportation id the player is executing}
	 */
	public static void startTeleport(final Player player, final int i) {
		player.getPA().removeAllWindows();
		int random = Misc.random(3);
		if (!player.getItems().playerHasItem(995, getPrice(i))) {
			player.sendMessage("You need " + getPrice(i) + " coins to use this teleport.");
			return;
		}

		/**
		 * Godwars teleport killcount
		 */
		if (i == 2) {
			player.getGodwars().increaseKillcountByTeleportationDevice(God.ARMADYL, 10);
			player.getGodwars().increaseKillcountByTeleportationDevice(God.BANDOS, 10);
			player.getGodwars().increaseKillcountByTeleportationDevice(God.SARADOMIN, 10);
			player.getGodwars().increaseKillcountByTeleportationDevice(God.ZAMORAK, 10);
		}

		/**
		 * Chance of VIP+ to operate the teleportation device for free
		 */
		if (player.heightLevel == 1 && random == 1) {
			player.sendMessage("You operate the device free of charge.");
		} else {
			player.getItems().deleteItem(995, getPrice(i));
		}
		player.getPA().startTeleport(getX(i), getY(i), getZ(i), "obelisk", false);
	}

	/**
	 * The x coordinate
	 * @param i		The teleportation id the coordinate is grabbed from
	 * @return		The coordinate being set
	 */
	public static int getX(int i) {
		return COORDINATE_DATA[i][0];
	}


	/**
	 * The y coordinate
	 * @param i		The teleportation id the coordinate is grabbed from
	 * @return		The coordinate being set
	 */
	public static int getY(int i) {
		return COORDINATE_DATA[i][1];
	}


	/**
	 * The height
	 * @param i		The teleportation id the height is grabbed from
	 * @return		The height being set
	 */
	public static int getZ(int i) {
		return COORDINATE_DATA[i][2];
	}


	/**
	 * The price
	 * @param i		The teleportation id the price is grabbed from
	 * @return		The price being set
	 */
	public static int getPrice(int i) {
		return COORDINATE_DATA[i][3];
	}

}
