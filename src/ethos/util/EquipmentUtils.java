package ethos.util;

import ethos.model.players.Player;

public class EquipmentUtils {

	public static boolean isInFullVeracs(Player player) {
		return player.playerEquipment[player.playerHat] == 4753 && player.playerEquipment[player.playerWeapon] == 4755 && player.playerEquipment[player.playerChest] == 4757
				&& player.playerEquipment[player.playerLegs] == 4759;
	}

	public static boolean fullGuthans(Player player) {
		return player.playerEquipment[player.playerHat] == 4724 && player.playerEquipment[player.playerWeapon] == 4726 && player.playerEquipment[player.playerChest] == 4728
				&& player.playerEquipment[player.playerLegs] == 4730;
	}

}
