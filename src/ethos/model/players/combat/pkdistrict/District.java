package ethos.model.players.combat.pkdistrict;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.util.Misc;

public class District {
	
	private static int time = 3;
	
	public static void stage(Player player, String stage) {
		NPC district = NPCHandler.getNpc(6773);
		NPC edge = NPCHandler.getNpc(6774);
		switch (stage) {
		case "start":
			if (player.hasFollower) {
				player.sendMessage("@cr10@@red@You cannot bring pets to the district.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getToxicBlowpipeCharge() > 0) {
				player.sendMessage("@cr10@@red@You must empty your blowpipe before entering the district.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getToxicTridentCharge() > 0) {
				player.sendMessage("@cr10@@red@You must empty your toxic trident before entering the district.");
				return;
			}
			if (player.getTridentCharge() > 0) {
				player.sendMessage("@cr10@@red@You must empty your trident before entering the district.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getToxicStaffOfTheDeadCharge() > 0) {
				player.sendMessage("@cr10@@red@You must empty your toxic sotd before entering the district.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getItems().isWearingItems() || player.getItems().freeSlots() < 28) {
				player.getDH().sendDialogues(812, 6773);
				return;
			}
			player.teleportingToDistrict = true;
			for (int i = 0; i < 7; i++) {
				player.playerStats[i] = player.playerXP[i];
			}
			player.stopMovement();
			edge.facePlayer(player.getIndex());
			edge.startAnimation(1818);
			edge.gfx0(343);
			edge.forceChat("Off to pk district you go " + Misc.formatPlayerName(player.playerName) + "!");
			player.getPA().removeAllWindows();
			player.nextChat = -1;
			player.freezeTimer = 3;
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (player.disconnected) {
						container.stop();
						return;
					}
					for (int i = 0; i < 7; i++) {
						player.playerLevel[i] = 99;
						player.playerXP[i] = player.getPA().getXPForLevel(99) + 1;
						player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
						player.getPA().refreshSkill(i);
					}
					player.freezeTimer = 3;
					player.stopMovement();
					player.getHealth().reset();
					player.setToxicBlowpipeAmmo(0);
					player.setToxicBlowpipeCharge(0);
					player.setToxicBlowpipeAmmoAmount(0);
					player.setTridentCharge(0);
					player.setToxicTridentCharge(0);
					player.setToxicStaffOfTheDeadCharge(0);
					player.getPlayerAssistant().movePlayer(3328, 4751, 0);
					player.pkDistrict = true;
					for (int i = 0; i < 3; i++) {
						player.sendMessage("");
					}
					player.getPA().sendFrame126("Combat Level: " + player.combatLevel + "", 3983);
					player.getPA().sendFrame126(player.totalLevel + "", 3984);
					player.sendMessage("@cr10@While here your stats have been set to 99.");
					player.sendMessage("@cr10@You can obtain gear and weapons just east by the district supply table.");
					player.sendMessage("@cr10@You can set your combat stats manually by clicking them in your skilltab.");
					player.sendMessage("@cr10@What you do and get here is limited to the time you are here.");
					player.sendMessage("@cr10@Enjoy!");
					player.teleportingToDistrict = false;
					container.stop();
				}
	
				@Override
				public void stop() {
	
				}
			}, time);
			break;
		
		case "end":
			if (!player.inClanWars() && !player.inClanWarsSafe()) {
				player.sendMessage("You are not within the PK district.");
				return;
			}
			player.getPA().removeAllWindows();
			player.nextChat = -1;
			player.freezeTimer = 3;
			player.stopMovement();
			district.facePlayer(player.getIndex());
			district.startAnimation(1818);
			district.gfx0(343);
			player.pkDistrict = false;
			district.forceChat("Off to reality you go " + Misc.formatPlayerName(player.playerName) + "!");
			player.getItems().deleteAllItems();
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (player.disconnected) {
						container.stop();
						return;
					}
					player.freezeTimer = 3;
					player.stopMovement();
					player.setToxicBlowpipeAmmo(0);
					player.setToxicBlowpipeCharge(0);
					player.setToxicBlowpipeAmmoAmount(0);
					player.setTridentCharge(0);
					player.setToxicTridentCharge(0);
					player.setToxicStaffOfTheDeadCharge(0);
					player.getPlayerAssistant().movePlayer(1803, 3763, 0);
					player.isSkulled = false;
					player.attackedPlayers.clear();
					player.headIconPk = -1;
					player.skullTimer = -1;
					player.sendMessage("Welcome back to reality.");
					player.getPA().requestUpdates();
					player.getHealth().reset();
					player.getPA().sendFrame126("Combat Level: " + player.combatLevel + "", 3983);
					player.getPA().sendFrame126(player.totalLevel + "", 3984);
					for (int i = 0; i < 7; i++) {
						player.playerLevel[i] = player.getPA().getLevelForXP(player.playerStats[i]);
						player.playerXP[i] = player.playerStats[i];
						player.getPA().setSkillLevel(i, player.playerLevel[i], player.playerXP[i]);
						player.getPA().refreshSkill(i);
					}
					player.getItems().deleteAllItems();
					container.stop();
				}

				@Override
				public void stop() {

				}
			}, time);
			break;
			
		case "bring_back":
			//TODO: Let players bring back what they've earned to reality
			
			player.getItems().deleteAllItems();
			break;
		}
	}

}
