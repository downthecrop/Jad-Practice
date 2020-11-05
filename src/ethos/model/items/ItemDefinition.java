package ethos.model.items;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Holds information regarding items
 *
 * @author Stuart
 * @created 03/08/2012
 */
public class ItemDefinition {


    /**
     * The array that contains all of the item definitions.
     */
    public static final ItemDefinition[] DEFINITIONS = new ItemDefinition[25000];

    /**
     * Adds a new {@link ItemDefinition} to the memory.
     *
     * @param index
     *         the index to add the definition on.
     * @param def
     *         the definition to add.
     */
    public static void add(int index, ItemDefinition def) {
        DEFINITIONS[index] = def;
    }

	/**
	 * The definitions.
	 */
	private static Map<Integer, ItemDefinition> definitions = new HashMap<>();

	/**
	 * Loads item definitions from item_defs.json
	 */
	public static void load() throws IOException {
		System.out.println("Loading item definitions...");

		List<ItemDefinition> list = new Gson().fromJson(FileUtils.readFileToString(new File("./Data/json/item_definitions.json")), new TypeToken<List<ItemDefinition>>() {
		}.getType());

		list.stream().filter(Objects::nonNull).forEach(item -> definitions.put((int) item.id, item));

		System.out.println("Loaded " + definitions.size() + " item definitions.");
	}

	/**
	 * Get an items definition by id.
	 *
	 * @param id The id.
	 * @return The item definition.
	 */
	public static ItemDefinition forId(int id) {
		return definitions.get(id);
	}

	/**
	 * A map of all definitions
	 * 
	 * @return the map
	 */
	public static Map<Integer, ItemDefinition> getDefinitions() {
		return definitions;
	}

	/**
	 * The id.
	 */
	private short id;

	/**
	 * The name.
	 */
	private String name;

	/**
	 * The description.
	 */
	private String desc;

	/**
	 * The value.
	 */
	private int value;

	/**
	 * The value of the drop
	 */
	private int dropValue;

	/**
	 * The bonuses.
	 */
	private short[] bonus;

	/**
	 * The slot the item goes in.
	 */
	private byte slot;

	/**
	 * Full mask flag.
	 */
	private boolean fullmask;

	/**
	 * Stackable flag
	 */
	private boolean stackable;

	/**
	 * Notable flag
	 */
	private boolean noteable;

	/**
	 * Stackable flag
	 */
	private boolean tradable;

	/**
	 * Wearable flag
	 */
	private boolean wearable;

	/**
	 * Show beard flag
	 */
	private boolean showBeard;

	/**
	 * Members flag
	 */
	private boolean members;

	/**
	 * Two handed flag
	 */
	private boolean twoHanded;

	/**
	 * Level requirements
	 */
	private final byte[] requirements = new byte[25];

/*    public ItemDefinition(short id, String name, String description, Equipment.Slot equipmentSlot, boolean stackable, int shopValue, int lowAlchValue, int highAlchValue, int[] bonus, boolean twoHanded, boolean fullHelm, boolean fullMask, boolean platebody) {
        this.id = id;
        this.name = name;
        this.desc = description;
        this.slot = equipmentSlot;
        this.stackable = stackable;
        this.shopValue = shopValue;
        this.lowAlchValue = lowAlchValue;
        this.highAlchValue = highAlchValue;
        this.bonus = bonus;
        this.twoHanded = twoHanded;
        this.fullHelm = fullHelm;
        this.fullMask = fullMask;
        this.platebody = platebody;
    }*/
	
	/**
	 * Get the id.
	 *
	 * @return The id.
	 */
	public short getId() {
		return id;
	}

	/**
	 * Get the name.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the description.
	 *
	 * @return The description.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Get the value.
	 *
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Get the bonus.
	 *
	 * @return The bonus.
	 */
	public short[] getBonus() {
		return bonus;
	}

	/**
	 * Gets the slot
	 *
	 * @return The slot.
	 */
	public byte getSlot() {
		return slot;
	}

	/**
	 * Gets the fullmask flag
	 *
	 * @return The fullmask flag
	 */
	public boolean isFullmask() {
		return fullmask;
	}

	/**
	 * Is this item stackable?
	 *
	 * @return
	 */
	public boolean isStackable() {
		return stackable;
	}

	/**
	 * Can this item be noted?
	 *
	 * @return
	 */
	public boolean isNoteable() {
		return noteable;
	}

	/**
	 * Is this item tradable?
	 *
	 * @return
	 */
	public boolean isTradable() {
		return tradable;
	}

	/**
	 * Get the level requirements
	 *
	 * @return
	 */
	public byte[] getRequirements() {
		return requirements;
	}

	/**
	 * Can this item be equipped
	 * 
	 * @return
	 */
	public boolean isWearable() {
		return wearable;
	}

	/**
	 * Does this item show the players beard
	 * 
	 * @return
	 */
	public boolean showBeard() {
		return showBeard;
	}

	/**
	 * Is this item two handed
	 * 
	 * @return
	 */
	public boolean isTwoHanded() {
		if (this.getId() == 20997)
			return true;
		return twoHanded;
	}

	/**
	 * Gets the drop value
	 * 
	 * @return
	 */
	public int getDropValue() {
		return dropValue;
	}

	/**
	 * Is this a members item
	 * 
	 * @return
	 */
	public boolean isMembers() {
		return members;
	}

}
