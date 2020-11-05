package ethos.model.npcs.bosses;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.Range;

import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.util.Misc;

public class Scorpia {
	
	/**
	 * Checks the healer stage to avoid multiple spawns
	 */
	public static int stage = 0;
	
	/**
	 * Spawns halears when scorpia reaches below a certain health
	 */
	public static void spawnHealer() {
		NPC scorpia = NPCHandler.getNpc(6615);
		if (scorpia == null) {
			return;
		}

		List<NPC> healer = Arrays.asList(NPCHandler.npcs);
		if (healer.stream().filter(Objects::nonNull).anyMatch(n -> n.npcType == 6617 && !n.isDead && n.getHealth().getAmount() > 0)) {
			return;
		}

		int maximumHealth = scorpia.getHealth().getMaximum();
		int currentHealth = scorpia.getHealth().getAmount();
		int percentRemaining = (int) (((double) currentHealth / (double) maximumHealth) * 100D);

		if (percentRemaining > 50) {
			return;
		}

		if (!Misc.passedProbability(Range.between(0, percentRemaining), 10, true)) {
			return;
		}
		if (stage == 0) {
			NPCHandler.spawnNpc(6617, 3238, 10338, 0, 0, 70, 13, 100, 120);
			NPCHandler.spawnNpc(6617, 3238, 10346, 0, 0, 70, 13, 100, 120);
			NPCHandler.spawnNpc(6617, 3228, 10338, 0, 0, 70, 13, 100, 120);
			NPCHandler.spawnNpc(6617, 3228, 10346, 0, 0, 70, 13, 100, 120);
			stage = 1;
		}
	}

}
