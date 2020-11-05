package ethos.model.npcs.bosses.wildypursuit;

import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;

public class IceQueen {
	
	public static int specialAmount = 0;
	
	public static void queenSpecial(Player player) {
		NPC QUEEN = NPCHandler.getNpc(4922);
		
		if (QUEEN.isDead) {
			return;
		}
		
		if (QUEEN.getHealth().getAmount() < 1400 && specialAmount == 0 ||
			QUEEN.getHealth().getAmount() < 1100 && specialAmount == 1 ||
			QUEEN.getHealth().getAmount() < 900 && specialAmount == 2 ||
			QUEEN.getHealth().getAmount() < 700 && specialAmount == 3 ||
			QUEEN.getHealth().getAmount() < 400 && specialAmount == 4 ||
			QUEEN.getHealth().getAmount() < 100 && specialAmount == 5) {
				NPCHandler.npcs[QUEEN.getIndex()].forceChat("Prison of Ice!");
				QUEEN.startAnimation(1979);
				QUEEN.underAttackBy = -1;
				QUEEN.underAttack = false;
				NPCHandler.queenAttack = "SPECIAL";
				specialAmount++;
				PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.PURSUIT_AREAS))
				.forEach(p -> {
					p.gfx100(369);
					p.appendDamage(Misc.random(25) + 12, Hitmark.HIT);
					p.freezeTimer = 10;
					p.sendMessage("Ice floods your veins!");
				});
			}
		}
	
	public static void rewardPlayers(Player player) {
		PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.PURSUIT_AREAS))
		.forEach(p -> {
				//int reward = (p.getIceQueenDamageCounter() >= 50 ? Misc.random(5) + 3 : 0);
				if (p.getIceQueenDamageCounter() >= 1) {
					p.sendMessage("@blu@The boss in pursuit has been killed!");
					p.sendMessage("@blu@You receive a Pursuit Crate for damaging the boss!");
					p.getItems().addItemUnderAnyCircumstance(21307, 1);
				} else {
					p.sendMessage("@blu@You didn't do enough damage to the boss!");
				}
				p.setIceQueenDamageCounter(0);
		});
	}
}
