package ethos.model.content.godwars;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GodwarsEquipment {

	/**
	 * A map of all equipment associated to certain gods. Wearing these will pacify minions following the respective god.
	 */
	public static Map<God, List<Integer>> EQUIPMENT = new HashMap<>();

	public static void load() throws IOException {
		List<GodwarsEquipment> list = new Gson().fromJson(FileUtils.readFileToString(new File("./Data/json/god_equipment.json")), new TypeToken<List<GodwarsEquipment>>() {
		}.getType());

		for (GodwarsEquipment element : list) {
			if (element == null) {
				continue;
			}
			if (EQUIPMENT.containsKey(element.god)) {
				EQUIPMENT.get(element.god).add(element.id);
			} else {
				List<Integer> itemList = new ArrayList<>();
				itemList.add(element.id);
				EQUIPMENT.put(element.god, itemList);
			}
		}
	}

	private int id;
	private God god;
}
