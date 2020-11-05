package ethos.model.npcs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class NPCSpawns {

	/**
	 * NPCSpawns, each represents a single npc spawn in the server which is held
	 * within the array {@link #spawns}.
	 *
	 * This was based off of Galkon's ItemDefinitions, therefore it will be heavily
	 * similar in a lot of ways.
	 *
	 * @Author Jesse (Sk8rdude461)
	 * @Author Galkon
	 */

	/**
	 * All the npc spawns.
	 */
	private static ArrayList<NPCSpawns> spawns;

	/**
	 * The Id of the npc. Commonly referred to as npcType.
	 */
	private int npcId;

	/**
	 * The home x-pos to the npc
	 */
	private int xPos;

	/**
	 * The home y-pos to the npc
	 */
	private int yPos;

	/**
	 * The height level where the npc is spawned
	 */
	private int height;

	/**
	 * The Walking type for the npc.
	 */
	private int walkType;

	/**
	 * The health(Constitution) of the npc.
	 */
	private int health;

	/**
	 * The highest possible hit for the npc.
	 */
	private int maxHit;

	/**
	 * The attack level of the npc.
	 */
	private int attack;

	/**
	 * The defence level of the npc.
	 */
	private int defence;

	/**
	 * Extra info I added so you can tell where the npc is spawned.
	 */
	private String name;

	public NPCSpawns(int npcid, int xPos, int yPos, int height, int walkType, int health, int maxHit, int attack,
			int defence, String name) {
		this.npcId = npcid;
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = height;
		this.walkType = walkType;
		this.health = health;
		this.maxHit = maxHit;
		this.attack = attack;
		this.defence = defence;
		this.name = name;
	}

	public static NPCSpawns defaultNPC() {
		try {
			NPCSpawns npc = new NPCSpawns(0, 0, 0, 0, 0, 0, 0, 0, 0, "New NPC");
			npc.setName("New NPC");
			System.out.println("NPC Made!");
			return npc;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Gets the npc spawns
	 *
	 * @return spawns
	 */
	public static ArrayList<NPCSpawns> getNPCSpawns() {
		return spawns;
	}

	/**
	 * Sets the npc spawns
	 *
	 * @param npcSpawns
	 *            the NPCSpawns to be set.
	 */
	public static void setNPCSpawns(ArrayList<NPCSpawns> npcSpawns) {
		NPCSpawns.spawns = npcSpawns;
	}

	/**
	 * Loading the NPCSpawns.
	 *
	 * @param file
	 *            The file to be read
	 * @throws java.io.FileNotFoundException
	 */
	public static void loadNPCSpawns() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		Gson gson = new Gson();
		long start = System.currentTimeMillis();
		System.out.println("Loading NPC spawns...");

		try (FileReader reader = new FileReader("./Data/json/npc_nodes.json")) {
			setNPCSpawns(gson.fromJson(reader, new TypeToken<ArrayList<NPCSpawns>>() {
			}.getType()));
		} catch (IOException e) {
			System.out.println("Failed to load NPC spawns!");
		} finally {
			System.out.println("Loaded " + NPCSpawns.getNPCSpawns().size() + " npc spawns in "
					+ (System.currentTimeMillis() - start) + "ms.");
		}

	}

	/**
	 * @return npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * @return xPos
	 */
	public int getXPos() {
		return xPos;
	}

	/**
	 * @return yPos
	 */
	public int getYPos() {
		return yPos;
	}

	/**
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return walkType
	 */
	public int getWalkType() {
		return walkType;
	}

	/**
	 * @return health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @return maxHit
	 */
	public int getMaxHit() {
		return maxHit;
	}

	/**
	 * @return attack
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * @return defence
	 */
	public int getDefence() {
		return defence;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	public void setYPos(int yPos) {
		this.yPos = yPos;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWalkType(int walkType) {
		this.walkType = walkType;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setMaxHit(int maxHit) {
		this.maxHit = maxHit;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public void setName(String name) {
		this.name = name;
	}
}