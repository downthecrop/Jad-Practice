package ethos.model.npcs.instance;

import ethos.Server;
import ethos.model.content.instances.SingleInstancedArea;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * 6/16/17
 * @author Grant_ @ rune-server.ee/grant_
 *
 */
public class InstanceSoloFight extends SingleInstancedArea{

	/**Location the player is moved to**/
	private static final int LOCATION_X = 1694;
	private static final int LOCATION_Y = 4703;
	
	/**Location the NPC spawns**/
	private static final int SPAWN_X = 1680;
	private static final int SPAWN_Y = 4703;
	
	/**Current NPC being instanced**/
	private static int currentNPC;
	
	/**Super call from the SingleInstance class**/
	public InstanceSoloFight(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}

	/**
	 * Method runs when the player chooses a boss
	 * @param npcId
	 * @param player
	 */
	public void init(int npcId, Player player){
		player.sendMessage("Prepare to fight the <col=ff0000>" + Misc.optimizeText(Server.npcHandler.getNpcName(npcId)) + "</col>!");
		player.getPA().movePlayer(LOCATION_X, LOCATION_Y, height);
		InstanceBosses isb = InstanceBosses.getBossForID(npcId);
		if(isb == null){
			System.out.println("ERROR, BOSS HAS NO DEFINITION. CLASS: InstanceBosses");
			return;
		}
		int hp = isb.getHP();
		int maxHit = isb.getMaxHit();
		int attack = isb.getAttack();
		int defence = isb.getDefence();
		Server.npcHandler.spawnNpc(player, npcId, SPAWN_X, SPAWN_Y, height, 1, hp, maxHit, attack, defence, true, false);
		currentNPC = npcId;
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
			
		} else if (dispose == DisposeTypes.INCOMPLETE) {				
			NPCHandler.kill(currentNPC, height);						
		}
	}

	@Override
	public void onDispose() {
		end(DisposeTypes.INCOMPLETE);
	}
	
}
