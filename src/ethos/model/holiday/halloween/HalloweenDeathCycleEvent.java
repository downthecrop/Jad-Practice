package ethos.model.holiday.halloween;

import java.util.Objects;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Player;
import ethos.util.Misc;

public class HalloweenDeathCycleEvent extends CycleEvent {
	/**
	 * The killer and the victim of the death event
	 */
	private Player victim, killer;

	/**
	 * The death npc, grim reaper
	 */
	private NPC death;

	/**
	 * Constructs a new death event based on the victim, and killer
	 * 
	 * @param victim the victim
	 * @param killer the killer
	 */
	public HalloweenDeathCycleEvent(Player victim, Player killer) {
		this.victim = victim;
		this.killer = killer;
		Server.npcHandler.spawnNpc(victim, 5567, victim.getX() - 1, victim.getY(), victim.heightLevel, 0, 0, 0, 0, 0, false, false);
		death = NPCHandler.getNpc(5567, victim.getX() - 1, victim.getY());
		if (death != null && death.spawnedBy == victim.getIndex()) {
			death.animNumber = 405;
			death.animUpdateRequired = true;
			death.facePlayer(victim.getIndex());
			death.updateRequired = true;
			String deathMessage = new DeathMessage(victim.playerName).create();
			death.forceChat(deathMessage);
		}
	}

	/**
	 * Executes the cycle event
	 */
	@Override
	public void execute(CycleEventContainer container) {
		if (Objects.isNull(victim) || Objects.isNull(killer)) {
			container.stop();
			return;
		}
		if (death != null && death.spawnedBy == victim.getIndex()) {
			death.absX = 0;
			death.absY = 0;
			death.makeX = 0;
			death.makeY = 0;
			death.actionTimer = 0;
			death.isDead = true;
			container.stop();
		}
	}

	class DeathMessage {

		String victim;

		DeathMessage(String victim) {
			this.victim = Misc.capitalize(victim);
		}

		String create() {
			switch (Misc.random(12)) {
			case 0:
			default:
				return "There is no escape " + victim + ".";

			case 1:
				return "This has been a long time coming " + Misc.capitalize(victim) + "!";

			case 2:
				return "Your time is up " + victim + ".";

			case 3:
				return "Edgeville requests your presence.";

			case 4:
				return "It's time to die.";

			case 5:
				return "Muahahahahaha";

			case 6:
				return "Whatcha gunna do when they come for you.";

			case 7:
				return "Trick or treat " + victim + ".";

			case 8:
				return "BOO!";

			case 9:
				return "Now it's time you die.";

			case 10:
				return victim + " is mine!";

			case 11:
				return "The darkness has requested your soul " + victim + ", come with me.";
			}
		}

	}

}
