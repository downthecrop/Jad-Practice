package ethos.model.npcs.bosses;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.util.Misc;

public class GreatOlm {
	
	/**
	 * Manages the Great olm's stage to avoid multiple spawns.
	 */
	public static int stage = 0;
	
	/**
	 * Checks wether or not a claw is alive, else spawns when the Olm goes below a certain HP.
	 */
	public static void checkClaws() {
		NPC corp = NPCHandler.getNpc(319);
		if (corp == null) {
			return;
		}

		List<NPC> core = Arrays.asList(NPCHandler.npcs);
		if (core.stream().filter(Objects::nonNull).anyMatch(n -> n.npcType == 320 && !n.isDead && n.getHealth().getAmount() > 0)) {
			return;
		}

		int currentHealth = corp.getHealth().getAmount();

		if (currentHealth < 1600 && stage == 0) {
			stage = 1;
		}
		if (currentHealth < 1000 && stage == 1) {
			stage = 2;
		}
		if (currentHealth < 450 && stage == 2) {
			stage = 3;
		}
	}

	/**
	 * Checks the player attacks towards the beast
	 * 
	 * @param attacker
	 * @param damage
	 */
	public static void attack(Player attacker, Damage damage) {
		NPC olm = NPCHandler.getNpc(319);
		int change = Misc.random((int) (damage.getAmount() / 2));
		
		if (olm.getHealth().getAmount() > 50)
		if (!attacker.getItems().isWearingItem(11824)) {
			damage.setAmount(change);
		}
	}
}
