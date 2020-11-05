package ethos.model.minigames.inferno;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.items.ItemAssistant;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

public class Inferno extends Tzkalzuk {

	public Inferno(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	
	}

	private int killsRemaining;


	public void spawn() {
		final int[][] type = InfernoWave.LEVEL;
		if(player.infernoWaveId >= type.length && player.infernoWaveType > 0 && Boundary.isIn(player, Boundary.SKOTIZO_BOSSROOM)) {
			if (player.zukDead)
			stop();
			return;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer event) {
				if(player == null) {
					event.stop();
					return;
				}
				if(!Boundary.isIn(player, Boundary.INFERNO)) {
					player.infernoWaveId = 0;
					player.infernoWaveType = 0;
					event.stop();
					return;
				}
				if(player.infernoWaveId >= type.length && player.infernoWaveType > 0) {
					stop();
					event.stop();
					return;
				}
				if(player.infernoWaveId != 0 && player.infernoWaveId < type.length)
					player.sendMessage("@red@You are now on wave "+(player.infernoWaveId + 1)+" of "+type.length+1+".", 255);
					if(player.infernoWaveId == 68) {
						player.sendMessage("Relog if the boss instance does not start within the next 10 seconds.");
						initiateTzkalzuk();
					}
				setKillsRemaining(type[player.infernoWaveId].length);
				for(int i = 0; i < getKillsRemaining(); i++) {
					int npcType = type[player.infernoWaveId][i];
					//int index = Misc.random(InfernoWave.SPAWN_DATA.length - 1);
					
					//int random = Misc.random(10);
					
					int startX = 2271 + Misc.random(-7, 7); //InfernoWave.SPAWN_DATA[index][0]
					int startY = 5342 + Misc.random(-7, 7); // InfernoWave.SPAWN_DATA[index][1]
					
					int hp = InfernoWave.getHp(npcType);
					int maxhit = InfernoWave.getMax(npcType);
					int atk = InfernoWave.getAtk(npcType);
					int def = InfernoWave.getDef(npcType);
					
					//System.out.println("Spawned "+npcType+": "+startX+", "+startY+" "+height+"");
					
					//NPCHandler.spawnNpc(npcType, x, y, height, 1, hp, maxhit, atk, def);
					Server.npcHandler.spawnNpc(player, npcType, startX, startY, height, 1, hp, maxhit, atk, def, true, false);
				}
				event.stop();
			}
			
			@Override
			public void stop() {

				
			}
		}, 16);
	}


	public void leaveGame() {
		if (System.currentTimeMillis() - player.infernoLeaveTimer < 15000) {
			player.sendMessage("You cannot leave yet, wait a couple of seconds and try again.");
			return;
		}
		killAllSpawns();
		player.sendMessage("You have left the Inferno minigame.");
		player.getPA().movePlayer(2495, 5110, 0);
		player.infernoWaveType = 0;
		player.infernoWaveId = 0;
	}

	public void create(int type) {
		player.getPA().removeAllWindows();
		player.getPA().movePlayer(2271, 5329, height);
		player.infernoWaveType = type;
		player.sendMessage("Welcome to the Inferno. Your first wave will start soon.", 255);
		player.infernoWaveId = 0;
		player.infernoLeaveTimer = System.currentTimeMillis();
		spawn();
	}

	public void stop() {
		reward();
		player.getPA().movePlayer(2495, 5110, 0);
		player.getDH().sendStatement("Congratulations for finishing the Inferno!");
		player.waveInfo[player.infernoWaveType - 1] += 1;
		player.infernoWaveType = 0;
		player.infernoWaveId = 0;
		player.nextChat = 0;
		player.setRunEnergy(100);
		killAllSpawns();
		player.zukDead = false;
	}

	public void handleDeath() {
		player.getPA().movePlayer(2495, 5110, 0);
		player.getDH().sendStatement("Unfortunately you died on wave " + player.infernoWaveId + ". Better luck next time.");
		player.nextChat = 0;
		player.infernoWaveType = 0;
		player.infernoWaveId = 0;
		killAllSpawns();
	}

	public void killAllSpawns() {
		for (int i = 0; i < NPCHandler.npcs.length; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.isInfernoNpc(i)) {
					if (NPCHandler.isSpawnedBy(player, NPCHandler.npcs[i])) {
						NPCHandler.npcs[i] = null;
					}
				}
			}
		}
	}
	
	public void gamble() {
		if (!player.getItems().playerHasItem(INFERNAL_CAPE)) {
			player.sendMessage("You do not have a firecape.");
			return;
		}
		player.getItems().deleteItem(INFERNAL_CAPE, 1);
		
		if (Misc.random(200) == 0) {
			 if (player.getItems().getItemCount(13225, true) == 0 && player.summonId != 13225) {
				 PlayerHandler.executeGlobalMessage("[@red@PET@bla@] @cr20@<col=255> " + player.playerName + "</col> received a pet from <col=255>TzTok-Jad</col>.");
				 player.getItems().addItemUnderAnyCircumstance(13225, 1);
				 player.getDH().sendDialogues(74, 2180);
			 }
		} else {
			player.getDH().sendDialogues(73, 2180);
			return;
		}
	}

	private static final int[] REWARD_ITEMS = { 6571, 6528, 11128, 6523, 6524, 6525, 6526, 6527, 6568, 6523, 6524, 6525, 6526, 6527, 6568 };

	public static final int INFERNAL_CAPE = 21295;

	public static final int TOKKUL = 6529;

	public void reward() {

		player.getItems().addItemUnderAnyCircumstance(INFERNAL_CAPE, 1);
	}

	public int getKillsRemaining() {
		return killsRemaining;
	}

	public void setKillsRemaining(int remaining) {
		this.killsRemaining = remaining;
	}

}
