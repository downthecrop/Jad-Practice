package ethos.model.npcs.bosses.raids;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class Tekton {
	
	public static int specialAmount = 0;
	
	public static void tektonSpecial(Player player) {
		NPC TEKTON = NPCHandler.getNpc(7544);
		
		if (TEKTON.isDead) {
			return;
		}
		
		if (TEKTON.getHealth().getAmount() < 1400 && specialAmount == 0 ||
			TEKTON.getHealth().getAmount() < 1100 && specialAmount == 1 ||
			TEKTON.getHealth().getAmount() < 900 && specialAmount == 2 ||
			TEKTON.getHealth().getAmount() < 700 && specialAmount == 3 ||
			TEKTON.getHealth().getAmount() < 400 && specialAmount == 4 ||
			TEKTON.getHealth().getAmount() < 100 && specialAmount == 5) {
			List<NPC> attacker = Arrays.asList(NPCHandler.npcs);
				if (attacker.stream().filter(Objects::nonNull)
						.anyMatch(n -> n.npcType == 7617 && !n.isDead)) {
					return;
				}
				NPCHandler.npcs[TEKTON.getIndex()].forceChat("RAAAAAAAA!");
				TEKTON.underAttackBy = -1;
				TEKTON.underAttack = false;
				NPCHandler.tektonAttack = "SPECIAL";
				specialAmount++;
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					int ticks = 0;
					@Override
					public void execute(CycleEventContainer container) {
						if (player.disconnected) {
							stop();
							return;
						}
						switch (ticks++) {
						case 1:
							Server.npcHandler.spawnNpc(player, 7617, 3308, 5285, 1, 0, 600, 30, 2000, 2000, true, false);
							NPCHandler.tektonAttack = "MELEE";
							break;
							
						case 2:
							Server.npcHandler.spawnNpc(player, 7617, 3323, 5300, 1, 0, 600, 30, 2000, 2000, true, false);
							break;
							
						case 3:
							Server.npcHandler.spawnNpc(player, 7617, 3307, 5303, 1, 0, 600, 30, 2000, 2000, true, false);
							break;
							
						case 4:
							Server.npcHandler.spawnNpc(player, 7617, 3322, 5292, 1, 0, 600, 30, 2000, 2000, true, false);
							break;
							
						case 5:
							Server.npcHandler.spawnNpc(player, 7617, 3317, 5285, 1, 0, 600, 30, 2000, 2000, true, false);
							break;
							
						case 7:
							NPCHandler.kill(7617, 1);
							container.stop();
							break;
						}
					}

					@Override
					public void stop() {

					}
				}, 2);
			}
		}
	
	public static void rewardPlayers(Player player) {
		PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.TEKTON))
		.forEach(p -> {
				int reward = p.getTektonDamageCounter();
				p.sendMessage("@dre@Tekton has been killed! Rewards have been dealt out!");
				p.sendMessage("@dre@You dealt " + p.getTektonDamageCounter() + " damage towards tekton; granting " + reward + " raid points.");
				//p.getItems().addItemUnderAnyCircumstance(995, reward);
				p.setRaidPoints(p.getRaidPoints() + p.getTektonDamageCounter());
				DailyTasks.increase(p, PossibleTasks.TEKTON_RAID);
				if (Misc.random(20) == 0) {
					p.getItems().addItemUnderAnyCircumstance(405, 1);
					p.sendMessage("@pur@You noticed a PvM Casket has dropped and picked it up right away.");
				}
				p.setTektonDamageCounter(0);
		});
	}
}
