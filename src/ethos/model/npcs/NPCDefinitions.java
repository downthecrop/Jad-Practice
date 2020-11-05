package ethos.model.npcs;

public class NPCDefinitions {

	public static int NPCS = 30_000;

	private static NPCDefinitions[] definitions = new NPCDefinitions[NPCS];

	/**
	 * @return the definitions
	 */
	public static NPCDefinitions[] getDefinitions() {
		return definitions;
	}

	/**
	 * The NPCDefinitions object for the associated npc identification value
	 * 
	 * @param npcId the npc id
	 * @return the {@link NPCDefinitions} object
	 */
	public static NPCDefinitions get(int npcId) {
		if (definitions[npcId] == null) {
			definitions[npcId] = new NPCDefinitions(npcId);
		}
		return definitions[npcId];
	}

	/**
	 * Attempts to retrieve the {@link NPCDefinitions} object with the exact same name as specified.
	 * 
	 * <p>
	 * <b>Please note that this is a fairly resource intensive function as we are sifting through thousands of definitions in hopes to retrieve a definition with the same name.</b>
	 * </p>
	 * 
	 * @param name the name of the npc
	 * @return the definition with the same name if possible, otherwise null
	 */
	public static NPCDefinitions get(String name) {
		for (NPCDefinitions definition : definitions) {
			if (definition == null || definition.name == null) {
				continue;
			}
			if (definition.name.equalsIgnoreCase(name)) {
				return definition;
			}
		}
		return null;
	}

	/**
	 * Creates a new npc definitions object based on some pre-defined information
	 * 
	 * @param npcId the npc id
	 * @param name the name of the npc
	 * @param combat the combat level of the npc
	 * @param health the health of the npc
	 * @param size the size o the npc
	 * @return
	 */
	public static NPCDefinitions create(int npcId, String name, int combat, int health, int size) {
		NPCDefinitions def = new NPCDefinitions(npcId);
		def.name = name;
		def.combat = combat;
		def.health = health;
		def.size = size;
		definitions[npcId] = def;
		return def;
	}

	private int id;
	private String name;
	private int combat;
	private int health;
	private int size = 1;

	public NPCDefinitions(int id) {
		this.id = id;
	}

	public int getNpcId() {
		return id;
	}

	public void setNpcId(int id) {
		this.id = id;
	}

	public String getNpcName() {
		return name;
	}

	public void setNpcName(String name) {
		this.name = name;
	}

	public int getNpcCombat() {
		return combat;
	}

	public void setNpcCombat(int combat) {
		this.combat = combat;
	}

	public int getNpcHealth() {
		return health;
	}

	public void setNpcHealth(int health) {
		this.health = health;
	}

	/**
	 * Sets the tile size of the npc.
	 * 
	 * @param size the amount of tiles the npc occupies
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * The amount of tiles the npc occupies
	 * 
	 * @return the size of the npc
	 */
	public int getSize() {
		return size;
	}
}