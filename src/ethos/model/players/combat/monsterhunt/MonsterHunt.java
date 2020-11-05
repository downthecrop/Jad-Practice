package ethos.model.players.combat.monsterhunt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPCHandler;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 * MonsterHunt.java
 * 
 * @author Jashy
 *
 * Re-written to make it cleaner - @Emre
 * 
 * A class to spawn NPC's at different locations in the wilderness.
 *
 */

public class MonsterHunt {

	public enum Npcs {
               //NAME(NPC ID, "WHAT YOU WANT NPC TO BE CALLED" ,HP ,MAXHIT, ATTACK, DEFENCE);
		GLOD(5129, "Glod", 1500, 75, 350, 400),
		ICE_QUEEN(4922, "Ice Queen", 1500, 55, 350, 400);

		private final int npcId;

		private final String monsterName;

		private final int hp;

		private final int maxHit;

		private final int attack;

		private final int defence;

		private Npcs(final int npcId, final String monsterName, final int hp, final int maxHit, final int attack, final int defence) {
			this.npcId = npcId;
			this.monsterName = monsterName;
			this.hp = hp;
			this.maxHit = maxHit;
			this.attack = attack;
			this.defence = defence;
		}

		public int getNpcId() {
			return npcId;
		}

		public String getMonsterName() {
			return monsterName;
		}

		public int getHp() {
			return hp;
		}

		public int getMaxHit() {
			return maxHit;
		}

		public int getAttack() {
			return attack;
		}

		public int getDefence() {
			return defence;
		}
	}

	/**
	 * The spawnNPC method which handles the spawning of the NPC and the global
	 * message sent.
	 * 
	 * @param c
	 */

	private static boolean spawned;
	
	private static int npcType;
	
	private static MonsterHuntLocation[] locations = new MonsterHuntLocation[]{new MonsterHuntLocation(3005, 3633, "The Black Nights Fortress @red@(15)"), new MonsterHuntLocation(3238, 3621, "The Chaos Altar @red@(13)"), new MonsterHuntLocation(3306, 3934, "Rogue's Castle @red@(52)"), new MonsterHuntLocation(3255, 3875, "Demonic Ruins @red@(45)"), new MonsterHuntLocation(3305, 3667, "East Dragons @red@(19)")};

	private static MonsterHuntLocation currentLocation;
			
	private static String name;
	
	public static void spawnNPC() {
		CycleEventHandler.getSingleton().addEvent(spawned, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if(spawned) {
					NPCHandler.kill(npcType, 0);
					spawned = false;
					currentLocation = null;
					return;
				}
				List<MonsterHuntLocation> locationsList = Arrays.asList(locations);
				MonsterHuntLocation randomLocation = Misc.randomTypeOfList(locationsList);
				currentLocation = randomLocation;
				List<Npcs> npcs = new ArrayList<>(EnumSet.allOf(Npcs.class));
				Npcs randomNpc = Misc.randomTypeOfList(npcs);
				name = randomNpc.getMonsterName();
				npcType = randomNpc.getNpcId();
				NPCHandler.spawnNpc(randomNpc.getNpcId(), randomLocation.getX(), randomLocation.getY(), 0, 1, randomNpc.getHp(), randomNpc.getMaxHit(), randomNpc.getAttack(), randomNpc.getDefence()/*, false*/);
				PlayerHandler.executeGlobalMessage("@red@The " + randomNpc.getMonsterName() + " has been spotted near " + randomLocation.getLocationName() + ".");
				spawned = true;
			}
		}, 5000); //3000 THIS NUMBER HANDLES HOW OFTEN THE NEW NPC IS SPAWNED AND THE OLD ONE IS KILLED (IN TICKS)
	}                 // IT ALSO DETERMINES HOW MANY TICKS FROM SERVER START UNTIL THE FIRST NPC IS SPAWNED.

	public static MonsterHuntLocation getCurrentLocation() {
		return currentLocation;
	}

	public static void setCurrentLocation(MonsterHuntLocation currentLocation) {
		MonsterHunt.currentLocation = currentLocation;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		MonsterHunt.name = name;
	}
	
	
}