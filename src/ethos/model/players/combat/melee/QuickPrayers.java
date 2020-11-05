package ethos.model.players.combat.melee;

import ethos.model.players.Player;

/**
 * Handles quick prayer
 *
 * @author 2012 <http://www.rune-server.org/members/dexter+morgan/>
 *
 */
public class QuickPrayers {

	/**
	 * The normal
	 */
	private final boolean[] normal = new boolean[29];

	/**
	 * The config id
	 */
	public static final int CONFIG = 620;

	/**
	 * Displays the interface
	 *
	 * @param player
	 *            the player
	 */
	public static void sendInterface(Player player) {
		player.setSidebarInterface(5, 17200);
		player.getPA().sendFrame106(5);

		for (int i = 0; i < player.getQuick().getNormal().length; i++) {
			if (player.getQuick().getNormal()[i]) {
				player.getPA().setConfig(CONFIG + i, 1);
			}
		}

	}

	/**
	 * Checks if all prayers are deactivated
	 *
	 * @param player
	 *            the player
	 * @return deactivated prayers
	 */
	public static boolean noneActivate(Player player) {
		for (int i = 0; i < player.prayerActive.length; i++) {
			if (player.prayerActive[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Toggles the quick prayers
	 *
	 * @param player
	 *            the player
	 */
	public static void toggle(Player player) {
		/**
		 * No points
		 */
		if (player.isSelectingQuickprayers) {
			player.sendMessage(":prayerfalse:");
			player.sendMessage("Please finish setting your quick prayers before toggling them.");
			return;
		}
		if (player.playerLevel[5] <= 0) {
			player.sendMessage(":prayerfalse:");
			player.sendMessage("You don't have any prayer points!");
			return;
		}
		boolean found = false;
		for (int i = 0; i < player.getQuick().getNormal().length; i++) {
			if (player.getQuick().getNormal()[i]) {
				found = true;
				CombatPrayer.activatePrayer(player, i);
				player.sendMessage(":prayertrue:");
			}
		}
		if (noneActivate(player)) {
			player.sendMessage(":prayerfalse:");
		}
		if (!found) {
			player.sendMessage(":prayerfalse:");
			player.sendMessage("You need to have some quick prayers selected to use quick prayers.");
		}
	}

	/**
	 * Activating quick prayers
	 *
	 * @param player
	 *            the player
	 * @param button
	 *            the button
	 */
	public static boolean clickButton(Player player, int button) {
		int buttonIds[] = {42057, 42058, 42059, 97180, 97182, 42060, 42061, 42062, 42063, 42064, 42065, 97184, 97186, 42066, 42067, 42068, 42069, 42070, 42071, 97189, 97191, 22251, 22252, 22253, 174057, 97193, 97195, 174060, 174063 };
		for (int i = 0; i < buttonIds.length; i++) {
			if (button == buttonIds[i]) {
				activateNormal(player, i);
				return true;
			}
		}
		if (button == 67080) {
			player.isSelectingQuickprayers = false;
			player.setSidebarInterface(5, 15608);
			return true;
		}
		return false;
	}

	/**
	 * Activates quick regular
	 *
	 * @param player
	 *            the player
	 * @param prayer
	 *            the prayer
	 */
	private static void activateNormal(Player player, int prayer) {
		if (prayer == 24) {
			if (player.playerLevel[1] < 65) {
				player.sendMessage("You must have a defence level of 70 to use this prayer.");
				for (int i = 0; i < CombatPrayer.getTurnOff(prayer).length; i++) {
					player.getQuick().getNormal()[CombatPrayer.getTurnOff(prayer)[i]] = false;
					player.getPA().sendConfig(CONFIG + CombatPrayer.getTurnOff(prayer)[i], 0);
				}
				return;
			}
		}
		if (prayer == 25) {
			if (player.playerLevel[1] < 70) {
				player.sendMessage("You must have a defence level of 70 to use this prayer.");
				for (int i = 0; i < CombatPrayer.getTurnOff(prayer).length; i++) {
					player.getQuick().getNormal()[CombatPrayer.getTurnOff(prayer)[i]] = false;
					player.getPA().sendConfig(CONFIG + CombatPrayer.getTurnOff(prayer)[i], 0);
				}
				return;
			}
		}
		/**
		 * Checks for level
		 */
		if (player.getLevelForXP(player.playerXP[5]) <  player.PRAYER_LEVEL_REQUIRED[prayer]) {
			player.sendMessage("You don't have the required Prayer level to activate " +  player.PRAYER_NAME[prayer] + ".");
			return;
		}
		/**
		 * Switches
		 */
		if (!player.getQuick().getNormal()[prayer]) {
			for (int i = 0; i < CombatPrayer.getTurnOff(prayer).length; i++) {
				player.getQuick().getNormal()[CombatPrayer.getTurnOff(prayer)[i]] = false;
				player.getPA().sendConfig(CONFIG + CombatPrayer.getTurnOff(prayer)[i], 0);
			}
		}
		/**
		 * Activates
		 */
		player.getQuick().getNormal()[prayer] = !player.getQuick().getNormal()[prayer];
		player.getPA().setConfig(CONFIG + prayer, player.getQuick().getNormal()[prayer] ? 1 : 0);
	}

	/**
	 * Resets all quick prayers
	 *
	 * @param player
	 *            the player
	 */
	public static void resetAll(Player player) {
		for (int i = 0; i < player.getQuick().getNormal().length; i++) {
			player.getQuick().getNormal()[i] = false;
			player.getPA().sendConfig(CONFIG + i, 0);
		}
		player.sendMessage(":prayerfalse:");
	}

	/**
	 * Gets the normal
	 *
	 * @return the normal
	 */
	public boolean[] getNormal() {
		return normal;
	}
}
