package ethos.model.minigames.inferno;


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

public class Tzkalzuk extends SingleInstancedArea {

	/**
	 * Player variables, start coordinates.
	 */
	private static final int START_X = 2271, START_Y = 5358;
	
	/**
	 * Npc variables, start coordinates.
	 */
	public static final int SPAWN_X = 2268, SPAWN_Y = 5365;
	
	public static final int GLYPH_SPAWN_X = 2270, GLYPH_SPAWN_Y = 5363;
	
	public boolean behindGlyph,glyphCanMove, glyphMoveLeft, glyphMoveRight, singleJad, singleRanger, singleMager, cutsceneWalkBlock = false;

	public int glyphX,glyphY=0;
	public Tzkalzuk(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	public void tzkalzukSpecials(int tick) {
		NPC TZKALZUK = NPCHandler.getNpc(InfernoWave.TZKAL_ZUK, height);
		
		if (TZKALZUK.isDead) {
			return;
		}
		
		int random = Misc.random(11);
		
		if(random == 0 && !singleRanger && !singleMager) {
			player.sendMessage("Send Range & Mage");
			Server.npcHandler.spawnNpc(player, InfernoWave.JAL_AKREK_XIL, player.absX, player.absY - 1, height, 0, 60, 8, 350, 540, true, false);
			Server.npcHandler.spawnNpc(player, InfernoWave.JAL_AKREK_KET, player.absX, player.absY - 1, height, 0, 60, 8, 350, 540, true, false);
			singleRanger = true;
			singleMager = true;
		}
		
			if (TZKALZUK.getHealth().getAmount() < 480 && !singleJad) {
				player.sendMessage("Send Jad");
				//NPCHandler.npcs[TZKALZUK.getIndex()].forceChat("Gar mulno ful taglo!");
				//Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX + 1, player.absY, height, 0, 85, 8, 350, 540, true, false);
				//Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX - 1, player.absY, height, 0, 85, 8, 350, 540, true, false);
				//Server.npcHandler.spawnNpc(player, REANIMATED_DEMON, player.absX, player.absY + 1, height, 0, 85, 8, 350, 540, true, false);
				singleJad = true;
			} else if (TZKALZUK.getHealth().getAmount() < 240) {
				//Server.npcHandler.spawnNpc(player, DARK_ANKOU, player.absX, player.absY - 1, height, 0, 60, 8, 350, 540, true, false);
				player.sendMessage("Send Healers");
			}
	}

	/**
	 * Constructs the content by creating an event
	 */
	public void init() {
		//Start event by starting Inferno here

	}
	public void spawnNpcs() {
		Server.npcHandler.spawnNpc(player, InfernoWave.TZKAL_ZUK, SPAWN_X, SPAWN_Y, height, 0, 1200, 251, 1000, 1000, true, false);
	}


	public boolean isBehindGlyph(){
		if(player.getX() <= NPCHandler.getNpc(7707,player.getHeight()).getX() + 3 && player.getX() >= NPCHandler.getNpc(7707,player.getHeight()).getX() - 3){
			return true;
		}
		return false;
	}
	
	public void initiateTzkalzuk() {
		
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (container.getOwner() == null || player == null || player.isDead || player.getInferno() == null) {
					//player.sendMessage("tits");
					container.stop();
					return;
				}
				int cycle = container.getTotalTicks();
				//player.sendMessage("tick "+cycle);
				if (cycle == 1) {
					cutsceneWalkBlock = true;
					player.getPA().sendScreenFade("TzKal-Zuk instance loading...", -1, 5);
					player.getPA().movePlayer(START_X, START_Y, height + 1);
				} else if (cycle == 2) {
					player.turnPlayerTo(START_X, SPAWN_Y);
				} else if (cycle == 3) {
					Server.getGlobalObjects().add(new GlobalObject(-1, 2267, 5368, (height + 1), 0, 10, -1, -1));  // Delete ceiling
					player.getPA().movePlayer(START_X, START_Y, height);
					Server.npcHandler.spawnNpc(player, InfernoWave.ANCESTRAL_GLYPH, GLYPH_SPAWN_X, GLYPH_SPAWN_Y, height, 0, 450, 38, 500, 700, false, false);
				} else if (cycle == 4) {
					Server.getGlobalObjects().add(new GlobalObject(30342, 2267, 5366, height, 1, 10, -1, -1));  // West Wall
					Server.getGlobalObjects().add(new GlobalObject(30341, 2275, 5366, height, 3, 10, -1, -1));  // East Wall
					Server.getGlobalObjects().add(new GlobalObject(30340, 2267, 5364, height, 1, 10, -1, -1));  // West Corner
					Server.getGlobalObjects().add(new GlobalObject(30339, 2275, 5364, height, 3, 10, -1, -1));  // East Corner
					Server.getGlobalObjects().add(new GlobalObject(30344, 2268, 5364, height, 3, 10, -1, -1)); // Set falling rocks - west
					Server.getGlobalObjects().add(new GlobalObject(30343, 2273, 5364, height, 3, 10, -1, -1)); // Set falling rocks - east
					Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5363, height, 3, 10, -1, -1)); // Delete ancestral glyph
					player.getPA().stillCamera(2271, 5365, 2, 0, 10);
				} else if (cycle == 10) {
					player.getPA().sendPlayerObjectAnimation(player, 2268, 5364, 7560, 10, 3, height); // Set falling rocks animation - west
					player.getPA().sendPlayerObjectAnimation(player, 2273, 5364, 7559, 10, 3, height); // Set falling rocks animation - east
					//player.getPA().sendPlayerObjectAnimation(player, 2270, 5363, 7560, 10, 3, height); // Set falling rocks animation - middle
					spawnNpcs();
					player.sendMessage("error");
				} else if (cycle >= 14) {
					player.getPA().resetCamera();
					cutsceneWalkBlock = false;
					glyphCanMove = true;
					player.sendMessage("Stop");
					container.stop();
				}
			}
		}, 1);
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
			NPCHandler.kill(InfernoWave.TZKAL_ZUK, height);
			NPCHandler.kill(InfernoWave.ANCESTRAL_GLYPH, height);
			//Inferno.reward();
			player.getPA().movePlayer(1665, 10046, 0);
			player.zukDead = true;
		} else if (dispose == DisposeTypes.INCOMPLETE) {			
			NPCHandler.kill(InfernoWave.TZKAL_ZUK, height);
			NPCHandler.kill(InfernoWave.ANCESTRAL_GLYPH, height);
			player.getPA().movePlayer(1665, 10046, 0);
		}
	}

	@Override
	public void onDispose() {
		end(DisposeTypes.INCOMPLETE);
	}
	
	public int getHeight() {
		return height;
	}
}
