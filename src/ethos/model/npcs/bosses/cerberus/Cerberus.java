package ethos.model.npcs.bosses.cerberus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

public class Cerberus extends SingleInstancedArea {

	/**
	 * Player variables, start coordinates.
	 */
	private static final int START_X = 1240, START_Y = 1241;
	
	/**
	 * Npc variables, start coordinates.
	 */
	private static final int SPAWN_X = 1238, SPAWN_Y = 1251, CERBERUS_ID = 5862, SUMMONED_SOUL_RANGE = 5867, SUMMONED_SOUL_MAGIC = 5868, SUMMONED_SOUL_MELEE = 5869;


	public Cerberus(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	public void cerberusSpecials() {
		NPC CERBERUS = NPCHandler.getNpc(5862, height);
		
		if (CERBERUS.isDead) {
			return;
		}
		
		int random = Misc.random(7);
		
		if (CERBERUS.getHealth().getAmount() < 400) {
			if (random == 1) {
				List<NPC> ghost = Arrays.asList(NPCHandler.npcs);
				if (ghost.stream().filter(Objects::nonNull)
						.anyMatch(n -> n.npcType == SUMMONED_SOUL_RANGE || n.npcType == SUMMONED_SOUL_MAGIC
								|| n.npcType == SUMMONED_SOUL_MELEE && height == n.heightLevel && !n.isDead)) {
					return;
				}
				NPCHandler.npcs[CERBERUS.getIndex()].forceChat("Aaarrrooooooo");
				player.CERBERUS_ATTACK_TYPE = "GHOST_ATTACK";
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
							Server.npcHandler.spawnNpc(player, SUMMONED_SOUL_RANGE, 1241, 1256, height, 0, 600, 30, 170, 240, true, false);
							player.CERBERUS_ATTACK_TYPE = "MELEE";
							break;
							
						case 2:
							Server.npcHandler.spawnNpc(player, SUMMONED_SOUL_MAGIC, 1240, 1256, height, 0, 600, 30, 170, 240, true, false);
							break;
							
						case 3:
							Server.npcHandler.spawnNpc(player, SUMMONED_SOUL_MELEE, 1239, 1256, height, 0, 600, 30, 170, 240, true, false);
							break;
							
						case 5:
							NPCHandler.kill(SUMMONED_SOUL_RANGE, height);
							NPCHandler.kill(SUMMONED_SOUL_MAGIC, height);
							NPCHandler.kill(SUMMONED_SOUL_MELEE, height);
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
		
		if (CERBERUS.getHealth().getAmount() < 201) {
			if (random == 5) {
				player.CERBERUS_ATTACK_TYPE = "GROUND_ATTACK";
			}
		}
	}

	/**
	 * Constructs the content by creating an event
	 */
	public void init() {
		Server.npcHandler.spawnNpc(player, CERBERUS_ID, SPAWN_X, SPAWN_Y, height, 0, 600, 23, 350, 540, false, false);
		player.getPA().movePlayer(START_X, START_Y, height);
		player.sendMessage("Walk forward and prepare to fight...");
		
		player.CERBERUS_ATTACK_TYPE = "FIRST_ATTACK";

		Server.getGlobalObjects().add(new GlobalObject(23105, 1241, 1242, height, 0, 10, -1, -1));
		Server.getGlobalObjects().add(new GlobalObject(23105, 1240, 1242, height, 0, 10, -1, -1));
		Server.getGlobalObjects().add(new GlobalObject(23105, 1239, 1242, height, 0, 10, -1, -1));
		Server.getGlobalObjects().add(new GlobalObject(23105, 1240, 1236, height, 0, 10, -1, -1));
	}

	/**
	 * Disposes of the content by moving the player and finalizing and or removing any left over content.
	 * 
	 * @param dispose the type of dispose
	 */
	public final void end(DisposeTypes dispose) {
		if (player == null) {
			return;
		}
		
		if (dispose == DisposeTypes.COMPLETE) {
			//player.sendMessage("You killed cerberus.");
		} else if (dispose == DisposeTypes.INCOMPLETE) {
			Server.getGlobalObjects().remove(23105, height);				
			NPCHandler.kill(CERBERUS_ID, height);						
			NPCHandler.kill(SUMMONED_SOUL_RANGE, height);
			NPCHandler.kill(SUMMONED_SOUL_MAGIC, height);
			NPCHandler.kill(SUMMONED_SOUL_MELEE, height);
		}
	}

	@Override
	public void onDispose() {
		end(DisposeTypes.INCOMPLETE);
	}
	
}
