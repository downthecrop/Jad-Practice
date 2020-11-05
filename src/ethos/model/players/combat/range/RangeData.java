package ethos.model.players.combat.range;

import java.util.Optional;

import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.skills.herblore.PoisonedWeapon;

public class RangeData {

	public static void fireProjectileNpc(Player player, NPC npc, int angle, int speed, int projectile, int startHeight, int endHeight, int time, int slope) {
		int playerX = player.getX();
		int playerY = player.getY();
		int npcX = npc.getX();
		int npcY = npc.getY();
		int xOffset = (playerY - npcY) * -1;
		int yOffset = (playerX - npcX) * -1;
		player.getPA().createPlayersProjectile2(playerX, playerY, xOffset, yOffset, angle, speed, projectile, startHeight, endHeight, npc.getIndex() + 1, time, slope);
	}

	public static void fireProjectilePlayer(Player player, Player target, int angle, int speed, int projectile, int startHeight, int endHeight, int time, int slope) {
		int playerX = player.getX();
		int playerY = player.getY();
		int npcX = target.getX();
		int npcY = target.getY();
		int xOffset = (playerY - npcY) * -1;
		int yOffset = (playerX - npcX) * -1;
		player.getPA().createPlayersProjectile2(playerX, playerY, xOffset, yOffset, angle, speed, projectile, startHeight, endHeight, target.getIndex() + 1, time, slope);
	}

	public static void fireProjectileNpc(Player c, int delay) {
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				if (c.lastWeaponUsed == 12926) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 25, 10, c.oldNpcIndex + 1,
							45 + delay);
				} else {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 43, 31, c.oldNpcIndex + 1,
							c.getCombat().getProjectileShowDelay() + delay);
				}
				if (c.getCombat().usingDbow())
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 60, 31, c.oldNpcIndex + 1,
							c.getCombat().getStartDelay(), 35);
			}
		}
	}

	public static void fireProjectilePlayer(Player c, int delay) {
		if (c.oldPlayerIndex > 0) {
			if (PlayerHandler.players[c.oldPlayerIndex] != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int oX = PlayerHandler.players[c.oldPlayerIndex].getX();
				int oY = PlayerHandler.players[c.oldPlayerIndex].getY();
				int offX = (pY - oY) * -1;
				int offY = (pX - oX) * -1;
				if (!c.msbSpec && c.lastWeaponUsed != 12926) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 43, 31,
							-c.oldPlayerIndex - 1, c.getCombat().getStartDelay() + delay);
				} else if (c.lastWeaponUsed == 12926) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), c.getCombat().getRangeProjectileGFX(), 25, 10, c.oldNpcIndex + 1,
							45 + delay);
				} else if (c.msbSpec) {
					c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, c.getCombat().getProjectileSpeed(), 249, 43, 31, -c.oldPlayerIndex - 1,
							c.getCombat().getStartDelay(), 10);
					c.msbSpec = false;
				}
				if (c.getCombat().usingDbow())
					c.getPA().createProjectile3(pY, pX, offY, offX, c.getCombat().getRangeProjectileGFX(), 53, 31, 100, -c.oldPlayerIndex - 1);

			}
		}
	}

	public static int getRangeStr(int i) {
		int str = 0;
		Optional<Integer> original = PoisonedWeapon.getOriginal(i);
		int item = original.isPresent() ? original.get() : i;
		int[][] data = { 
				//Javelins
				{ 825, 75 }, { 826, 80 }, { 827, 90 }, { 828, 100 }, { 829, 120 }, { 830, 140 }, { 19484, 150 }, { 21318, 135 },
				
				{ 800, 5 }, { 801, 7 }, { 802, 11 }, { 803, 16 }, { 804, 23 }, { 805, 36 }, { 20849, 55 }, { 806, 35 }, { 807, 35 }, { 808, 38 }, { 809, 45 }, { 810, 52 }, { 811, 58 },
				{ 863, 4 }, { 864, 3 }, { 865, 7 }, { 866, 10 }, { 867, 14 }, { 868, 24 }, { 877, 10 },
				{ 882, 7 }, { 884, 10 }, { 886, 16 }, { 888, 22 }, { 890, 31 }, { 892, 49 }, { 4212, 70 }, { 4214, 70 }, { 4215, 64 }, { 4216, 60 }, { 4217, 55 },
				{ 4218, 52 }, { 4219, 50 }, { 4220, 48 }, { 4221, 45 }, { 4222, 43 }, { 4223, 40 }, { 4740, 44 }, { 6522, 49 }, { 9140, 46 }, { 9141, 64 }, { 9142, 82 },
				{ 9143, 100 }, { 11875, 100 }, { 21326, 55 }, { 21316, 115 }, { 9144, 115 }, { 9145, 36 }, { 9236, 14 }, { 9237, 30 }, { 9238, 48 }, { 9239, 66 }, { 9240, 83 }, { 9241, 85 }, { 9242, 103 }, { 9243, 105 }, { 9341, 110 },
				{ 9244, 117 }, { 9245, 120 }, { 9976, 0 }, { 9977, 15 }, { 10033, 60 }, { 10034, 85 }, { 11959, 95 }, { 11212, 60 }, { 11230, 65 }, { 12926, 53 } };
		for (int l = 0; l < data.length; l++) {
			if (item == data[l][0]) {
				str = data[l][1];
			}
		}
		return str;
	}

	public static int getRangeStartGFX(Player c) {
		Optional<Integer> originalItem = PoisonedWeapon.getOriginal(c.rangeItemUsed);
		int item = originalItem.isPresent() ? originalItem.get() : c.rangeItemUsed;
		int str = -1;
		
		int[][] data = {
				// KNIFES
				{ 863, 220 }, { 864, 219 }, { 865, 221 }, { 866, 223 }, { 867, 224 }, { 868, 225 }, { 869, 222 },

				// DARTS
				{ 806, 232 }, { 807, 233 }, { 808, 234 }, { 809, 235 }, { 810, 236 }, { 811, 237 },

//				// JAVELINS
//				{ 825, 206 }, { 826, 207 }, { 827, 208 }, { 828, 209 }, { 829, 210 }, { 830, 211 },

				// AXES
				{ 800, 36 }, { 801, 35 }, { 802, 37 }, { 803, 38 }, { 804, 39 }, { 805, 48 }, { 20849, 1319 },

				// ARROWS
				{ 882, 19 }, { 884, 18 }, { 886, 20 }, { 888, 21 }, { 890, 22 }, { 892, 24 }, { 11212, 1120 },

				// CRYSTAL_BOW
				{ 4212, 250 }, { 4214, 250 }, { 4215, 250 }, { 4216, 250 }, { 4217, 250 }, { 4218, 250 }, { 4219, 250 }, { 4220, 250 }, { 4221, 250 }, { 4222, 250 },
				{ 4223, 250 }, };
		for (int l = 0; l < data.length; l++) {
			if (item == data[l][0]) {
				str = data[l][1];
			}
		}
		if (c.playerEquipment[3] == 11235 || c.playerEquipment[3] == 12765 || c.playerEquipment[3] == 12766 || c.playerEquipment[3] == 12767 || c.playerEquipment[3] == 12768) {
			Optional<Integer> originalAmmo = PoisonedWeapon.getOriginal(c.playerEquipment[c.playerArrows]);
			int ammo = originalAmmo.isPresent() ? originalAmmo.get() : c.playerEquipment[c.playerArrows];
			int[][] moreD = { { 882, 1104 }, { 884, 1105 }, { 886, 1106 }, { 888, 1107 }, { 890, 1108 }, { 892, 1109 }, { 11212, 1111 }, };
			for (int l = 0; l < moreD.length; l++) {
				if (ammo == moreD[l][0]) {
					str = moreD[l][1];
				}
			}
		}
		return str;
	}

	public static int getRangeProjectileGFX(Player c) {
		switch (c.lastWeaponUsed) {
		case 861:
		case 12788:
			if (c.usingSpecial)
				return 249;
		}
		if (c.getItems().isWearingItem(12926)) {
			Optional<Integer> original = PoisonedWeapon.getOriginal(c.getToxicBlowpipeAmmo());
			int ammo = original.isPresent() ? original.get() : c.getToxicBlowpipeAmmo();
			final int[][] DARTS = { { 806, 226 }, { 807, 227 }, { 808, 228 }, { 809, 229 }, { 810, 230 }, { 811, 231 }, { 11230, 1123 } };
			for (int index = 0; index < DARTS.length; index++) {
				if (DARTS[index][0] == ammo) {
					return DARTS[index][1];
				}
			}
		}
		if (c.dbowSpec) {
			return Arrow.matchesMaterial(c.lastArrowUsed, Arrow.DRAGON) ? 1099 : 1101;
		}
		boolean castingMagic = (c.usingMagic || c.mageFollow || c.autocasting || c.spellId > 0);
		if (castingMagic) {
			return -1;
		}
		if (c.playerEquipment[c.playerWeapon] == 9185 || c.playerEquipment[c.playerWeapon] == 11785 || c.playerEquipment[c.playerWeapon] == 21012)
			return 27;
		int str = -1;
		Optional<Integer> original = PoisonedWeapon.getOriginal(c.rangeItemUsed);
		int ammo = original.isPresent() ? original.get() : c.rangeItemUsed;
		int[][] data = {
				// KNIFES
				{ 863, 213 }, { 864, 212 }, { 865, 214 }, { 866, 216 }, { 867, 217 }, { 868, 218 }, { 869, 215 },

				// DARTS
				{ 806, 226 }, { 807, 227 }, { 808, 228 }, { 809, 229 }, { 810, 230 }, { 811, 231 },

				// JAVELINS
				{ 825, 200 }, { 826, 201 }, { 827, 202 }, { 828, 203 }, { 829, 204 }, { 830, 205 }, { 19484, 1301 },

				// AXES
				{ 800, 36 }, { 801, 35 }, { 802, 37 }, { 803, 38 }, { 804, 39 }, { 805, 41 }, { 20849, 1320 },

				// ARROWS
				{ 882, 19 }, { 884, 18 }, { 886, 20 }, { 888, 21 }, { 890, 22 }, { 892, 24 }, { 11212, 1120 },

				// CHINCHOMPA
				{ 10033, 908 }, { 10034, 909 }, { 11959, 909 },

				// OTHERS
				{ 6522, 442 }, { 4740, 27 }, { 4212, 249 }, { 4214, 249 }, { 4215, 249 }, { 4216, 249 }, { 4217, 249 }, { 4218, 249 }, { 4219, 249 }, { 4220, 249 }, { 4221, 249 },
				{ 4222, 249 }, { 4223, 249 }, };
		for (int l = 0; l < data.length; l++) {
			if (ammo == data[l][0]) {
				str = data[l][1];
			}
		}
		return str;
	}

	public static int getRangeEndGFX(Player c) {
		int str = -1;
		int gfx = 0;
		int[][] data = { { 10033, 157, 100 }, { 10034, 157, 100 }, { 11959, 157, 100 } };
		for (int l = 0; l < data.length; l++) {
			if (c.playerEquipment[c.playerWeapon] == data[l][0]) {
				str = data[l][1];
				gfx = data[l][2];
			}
		}
		if (gfx == 100) {
			c.rangeEndGFXHeight = true;
		}
		return str;
	}

	public static int getProjectileSpeed(Player c) {
		if (c.dbowSpec)
			return 100;
		switch (c.playerEquipment[3]) {
		case 10033:
		case 10034:
		case 11959:
			return 60;
			
		case 19478:
		case 19481:
			return 100;
		}
		return 70;
	}

	public static int getProjectileShowDelay(Player c) {
		Optional<Integer> original = PoisonedWeapon.getOriginal(c.playerEquipment[c.playerWeapon]);
		int ammo = original.isPresent() ? original.get() : c.playerEquipment[c.playerWeapon];
		int[] data = { 806, 806, 808, 809, 810, 811, 10033, 10034, 11959, 11230, };
		int str = 53;
		for (int i = 0; i < data.length; i++) {
			if (ammo == data[i]) {
				str = 32;
			}
		}
		return str;
	}
}