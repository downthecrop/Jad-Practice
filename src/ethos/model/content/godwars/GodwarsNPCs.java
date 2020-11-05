package ethos.model.content.godwars;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GodwarsNPCs {

	/**
	 * A map of all godwars minions and the god they follow.
	 */
	public static Map<Integer, God> NPCS = new HashMap<>();

	public static void load() throws IOException {
		List<GodwarsNPCs> list = new Gson().fromJson(FileUtils.readFileToString(new File("./Data/json/god_npcs.json")), new TypeToken<List<GodwarsNPCs>>() {
		}.getType());

		list.stream().filter(Objects::nonNull).forEach(element -> NPCS.put(element.id, element.god));
	}

	private int id;
	private God god;

}
