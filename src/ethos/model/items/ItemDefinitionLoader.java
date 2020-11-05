package ethos.model.items;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ethos.util.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all item definitions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemDefinitionLoader extends JsonLoader {

    /**
     * Creates a new {@link ItemDefinitionLoader}.
     */
    public ItemDefinitionLoader() {
        super("./Data/json/item_definitions.json");
    }

    public void load(JsonObject reader, Gson builder) {
/*        this.id = id;
        this.name = name;
        this.description = description;
        this.equipmentSlot = equipmentSlot;
        this.stackable = stackable;
        this.shopValue = shopValue;
        this.lowAlchValue = lowAlchValue;
        this.highAlchValue = highAlchValue;
        this.bonus = bonus;
        this.twoHanded = twoHanded;
        this.fullHelm = fullHelm;
        this.fullMask = fullMask;
        this.platebody = platebody;
        int index = reader.get("id").getAsInt();
        String name = Objects.requireNonNull(reader.get("name").getAsString());
        String examine = Objects.requireNonNull(reader.get("examine").getAsString());
        Equipment equipmentSlot = Equipment.update(reader.get("equipmentSlot").getAsString());
        boolean stackable = reader.get("stackable").getAsBoolean();
        int generalPrice = reader.get("shopValue").getAsInt();
        int highAlchValue = reader.get("lowAlchValue").getAsInt();
        int lowAlchValue = reader.get("highAlchValue").getAsInt();
        int[] bonus = builder.fromJson(reader.get("bonus").getAsJsonArray(), int[].class);
        boolean twoHanded = reader.get("twoHanded").getAsBoolean();
        boolean fullHelm = reader.get("fullHelm").getAsBoolean();
        boolean fullMask = reader.has("fullMask") ? reader.get("full-mask").getAsBoolean() : false;
        boolean platebody = reader.get("platebody").getAsBoolean();
        ItemDefinition.add(index, new ItemDefinition(index, name, examine, equipmentSlot, stackable, generalPrice, lowAlchValue, highAlchValue, bonus, twoHanded, fullHelm, fullMask, platebody));*/
    }
}