package ethos.model.content.teleportation;

import ethos.Config;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.achievement_diary.wilderness.WildernessDiaryEntry;
import ethos.model.players.Player;

public class CityTeleports {
	
	public enum TeleportData {
		
		//Regular
		VARROCK(4140, 25, new int[] { 554, 1, 556, 3, 563, 1 }, Config.VARROCK_X, Config.VARROCK_Y, 62, 35),
		LUMBRIDGE(4143, 31, new int[] { 557, 1, 556, 3, 563, 1 }, Config.LUMBY_X, Config.LUMBY_Y, 63, 41),
		FALADOR(4146, 37, new int[] { 555, 1, 556, 3, 563, 1 }, Config.FALADOR_X, Config.FALADOR_Y, 64, 48),
		CAMELOT(4150, 45, new int[] { 556, 5, 563, 1, -1, -1 }, Config.CAMELOT_X, Config.CAMELOT_Y, 65, 55.5),
		ARDOUGNE(6004, 51, new int[] { 555, 2, 563, 2, -1, -1 }, Config.ARDOUGNE_X, Config.ARDOUGNE_Y, 66, 61),
		WATCHTOWER(6005, 58, new int[] { 557, 2, 563, 2, -1, -1 }, Config.WATCHTOWER_X, Config.WATCHTOWER_Y, 67, 68),
		TROLLHEIM(29031, 61, new int[] { 554, 2, 563, 2, -1, -1 }, Config.TROLLHEIM_X, Config.TROLLHEIM_Y, 68, 68),
		
		//Ancients
		PADDEWWA(50235, 54, new int[] { 563, 2, 554, 1, 556, 1 }, Config.PADDEWWA_X, Config.PADDEWWA_Y, 69, 64),
		SENNTISTEN(50245, 60, new int[] { 563, 2, 566, 1, -1, -1 }, Config.SENNTISTEN_X, Config.SENNTISTEN_Y, 70, 70),
		KHARYRLL(50253, 66, new int[] { 563, 2, 565, 1, -1, -1 }, Config.KHARYRLL_X, Config.KHARYRLL_Y, 71, 76),
		LASSAR(51005, 72, new int[] { 563, 2, 555, 4, -1, -1 }, Config.LASSAR_X, Config.LASSAR_Y, 72, 82),
		DAREEYAK(51013, 78, new int[] { 563, 2, 554, 3, 556, 2 }, Config.DAREEYAK_X, Config.DAREEYAK_Y, 73, 88),
		CARRALLANGAR(51023, 84, new int[] { 563, 2, 566, 2, -1, -1 }, Config.CARRALLANGAR_X, Config.CARRALLANGAR_Y, 74, 94),
		ANNAKARL(51031, 90, new int[] { 563, 2, 565, 2, -1, -1 }, Config.ANNAKARL_X, Config.ANNAKARL_Y, 75, 100),
		GHORROCK(51039, 96, new int[] { 563, 2, 555, 8, -1, -1 }, Config.GHORROCK_X, Config.GHORROCK_Y, 76, 1016),
		
		//Lunar
		MOONCLAN(117112, 69, new int[] { 9075, 2, 563, 1, 557, 2 }, Config.MOONCLAN_X, Config.MOONCLAN_Y, 77, 66),
		OURANIA(117131, 71, new int[] { 9075, 2, 563, 1, 557, 6 }, Config.OURANIA_X, Config.OURANIA_Y, 78, 69),
		WATERBIRTH(117154, 72, new int[] { 9075, 2, 563, 1, 555, 1 }, Config.WATERBIRTH_X, Config.WATERBIRTH_Y, 79, 71),
		BARBARIAN(117186, 75, new int[] { 9075, 2, 563, 2, 554, 3 }, Config.BARBARIAN_X, Config.BARBARIAN_Y, 80, 76),
		KHAZARD(117210, 78, new int[] { 9075, 2, 563, 2, 555, 4 }, Config.KHAZARD_X, Config.KHAZARD_Y, 81, 80),
		FISHING_GUILD(118018, 85, new int[] { 9075, 3, 563, 3, 555, 10 }, Config.FISHING_GUILD_X, Config.FISHING_GUILD_Y, 82, 89),
		CATHERBY(118042, 87, new int[] { 9075, 3, 563, 3, 555, 10 }, Config.CATHERBY_X, Config.CATHERBY_Y, 83, 92),
		ICE_PLATEU(118058, 89, new int[] { 9075, 3, 563, 3, 555, 8 }, Config.ICE_PLATEU_X, Config.ICE_PLATEU_Y, 84, 96);
		
		private final int buttonId;
		private final int level;
		private final int[] runes;
		private final int x;
		private final int y;
		private final int spellId;
		private final double xp;
		
		TeleportData(int buttonId, int level, int[] runes, int x, int y, int spellId, double xp) {
			this.buttonId = buttonId;
			this.level = level;
			this.runes = runes;
			this.x = x;
			this.y = y;
			this.spellId = spellId;
			this.xp = xp;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getLevel() {
			return level;
		}

		public int[] getRunes() {
			return runes;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
		
		public int getSpellId() {
			return spellId;
		}
		
		public double getXP() {
			return xp;
		}
		
	}
	
	public static void teleport(Player player, int buttonId) {
		if (player.inClanWars() || player.inClanWarsSafe()) {
			player.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return;
		}
		player.usingMagic = true;
		for (TeleportData data : TeleportData.values()) {
			if (data.getButtonId() == buttonId) {
				if (System.currentTimeMillis() - player.lastTeleport < 6000) {
					return;	
				}
				if (!player.getCombat().checkMagicReqs(data.getSpellId())) {
					return;
				}
				player.getPA().spellTeleport(data.getX(), data.getY(), 0, false);
				player.getPA().addSkillXP((int) data.getXP(), 6, true);
				player.lastTeleport = System.currentTimeMillis();
				switch (buttonId) {
				case 4143:
					player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.LUMBRIDGE_TELEPORT);
					break;
				
				case 4150:
					player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CAMELOT_TELEPORT);
					break;
				
				case 6004:
					player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ARDOUGNE);
					break;
					
				case 4146:
					player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.TELEPORT_TO_FALADOR);
					break;
					
				case 29031:
					player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.TROLLHEIM_TELEPORT);
					break;
					
				case 117154:
					player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.WATERBIRTH_TELEPORT);
					break;
					
				case 118042:
					player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CATHERY_TELEPORT);
					break;
					
				case 51039:
					player.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.GHORROCK);
					break;
				}
			}
		}
	}
}
