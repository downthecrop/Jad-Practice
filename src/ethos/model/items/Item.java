package ethos.model.items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import ethos.Config;
import ethos.model.players.Player;

/**
 * Handles The Item Wielding Of The Server Used When Hardcoding Additional Items
 * Fixing Bugged Items
 *
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class Item {

	/**
	 * Server cape IDs.
	 */
	public static int capes[] = { 22109, 2731, 2677, 2683, 2680, 2701, 2686, 2689, 2692, 2737, 2734, 2716, 2728, 2695, 2713,
			2725, 2722, 2707, 2704, 2710, 2719, 2737, 2698, 14590, 2701, 8102, 8075, 8044, 8045, 8042, 8043, 8037, 8038,
			8025, 8026, 8018, 7858, 7994, 7983, 7984, 7985, 7986, 7987, 7982, 7978, 3781, 3783, 3785, 3787, 3789, 3777,
			3779, 3759, 3761, 3763, 3765, 6111, 6570, 6568, 1007, 1019, 1021, 1023, 1027, 1029, 1031, 1052, 2412, 2413,
			2414, 4304, 4315, 4317, 4319, 4321, 4323, 4325, 4327, 4329, 4331, 4333, 4335, 4337, 4339, 4341, 4343, 4345,
			4347, 4349, 4351, 4353, 4355, 4357, 4359, 4361, 4363, 4365, 4367, 4369, 4371, 4373, 4375, 4377, 4379, 4381,
			4383, 4385, 4387, 4389, 4391, 4393, 4395, 4397, 4399, 4401, 4403, 4405, 4407, 4409, 4411, 4413, 4514, 4516,
			6070, 6568, 6570, 9074 };

	/**
	 * Server boot IDs.
	 */
	public static int boots[] = { 11840, 7596, 8029, 6619, 8017, 7159, 7991, 6666, 6061, 6106, 88, 89, 626, 628, 630,
			632, 634, 1061, 1837, 1846, 2577, 2579, 2894, 2904, 2914, 2924, 2934, 3061, 3105, 3107, 3791, 4097, 4107,
			4117, 4119, 4121, 4123, 4125, 4127, 4129, 4131, 4310, 5064, 5345, 5557, 6069, 6106, 6143, 6145, 6147, 6328,
			6920, 6349, 6357, 3393, 9073, 10933, 7114 };

	/**
	 * Server glove IDs.
	 */
	public static int gloves[] = { 7595, 6629, 8021, 8016, 7964, 2491, 1065, 2487, 2489, 3060, 1495, 775, 777, 778,
			6708, 1059, 1063, 1065, 1580, 2487, 2489, 2491, 2902, 2912, 2922, 2932, 2942, 3060, 3799, 4095, 4105, 4115,
			4308, 5556, 6068, 6110, 6149, 6151, 6153, 6922, 7454, 7455, 7456, 7457, 7458, 7459, 7460, 7461, 7462, 6330,
			3391, 9072 };

	/**
	 * Server shield IDs.
	 */
	public static int shields[] = { 11283, 20846, 7676, 7342, 7348, 7354, 7360, 7334, 7340, 7347, 7352, 7358, 7356, 7350, 7344,
			8087, 8058, 8059, 8060, 8061, 8062, 8063, 6633, 7977, 7976, 7972, 7959, 6591, 7332, 7338, 7336, 7360, 1171,
			1173, 1175, 1177, 1179, 1181, 1183, 1185, 1187, 1189, 1191, 1193, 1195, 1197, 1199, 1201, 1540, 2589, 2597,
			2603, 2611, 2621, 2629, 2659, 2667, 2675, 2890, 3122, 3488, 3758, 3839, 3840, 3841, 3842, 3843, 3844, 4072,
			4156, 4224, 4225, 4226, 4227, 4228, 4229, 4230, 4231, 4232, 4233, 4234, 4302, 4507, 4512, 6215, 6217, 6219,
			6221, 6223, 6225, 6227, 6229, 6231, 6233, 6235, 6237, 6239, 6241, 6243, 6245, 6247, 6249, 6251, 6253, 6255,
			6257, 6259, 6261, 6263, 6265, 6267, 6269, 6271, 6273, 6275, 6277, 6279, 6524, 6889 };

	/**
	 * Server hat IDs.
	 */
	public static int hats[] = { 3057, 20838, 2679, 2685, 2682, 2703, 2688, 2691, 2691, 2733, 2736, 2718, 2730, 2697, 2715,
			2727, 2724, 2709, 2706, 2712, 2721, 2739, 2700, 2518, 2524, 2526, 7319, 7321, 7323, 7325, 7327, 1167, 8077,
			8076, 8074, 4168, 1169, 8034, 8035, 8036, 8030, 6623, 8024, 8023, 8022, 8013, 1169, 7594, 7995, 7996, 7997,
			7998, 7999, 8000, 8001, 7992, 7990, 7975, 7973, 7971, 7967, 7963, 6665, 6665, 7321, 6886, 6547, 6548, 2645,
			2647, 2649, 4856, 4857, 4858, 4859, 4880, 4881, 4882, 4883, 4904, 4905, 4906, 4907, 4928, 4929, 4930, 4931,
			4952, 4953, 4954, 4955, 4976, 4977, 4978, 4979, 4732, 4753, 4611, 6188, 6182, 4511, 4056, 4071, 4724, 2639,
			2641, 2643, 2665, 6109, 5525, 5527, 5529, 5531, 5533, 5535, 5537, 5539, 5541, 5543, 5545, 5547, 5549, 5551,
			74, 579, 656, 658, 660, 662, 664, 740, 1017, 1037, 1040, 1042, 1044, 1046, 1038, 1048, 1050, 1053, 1055,
			1057, 1137, 1139, 1141, 1143, 1145, 1147, 1149, 1151, 1153, 1155, 1157, 1159, 1161, 1163, 1165, 1506, 1949,
			2422, 2581, 2587, 2595, 2605, 2613, 2619, 2627, 2631, 2633, 2635, 2637, 2651, 2657, 2673, 2900, 2910, 2920,
			2930, 2940, 2978, 2979, 2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988, 2989, 2990, 2991, 2992, 2993,
			2994, 2995, 3057, 3385, 3486, 3748, 3749, 3751, 3753, 3797, 4041, 4042, 4071, 4089, 3755, 4099, 4109, 4164,
			4302, 4506, 4511, 4513, 4515, 4551, 4567, 4708, 4716, 4724, 4745, 4753, 4857, 4858, 4859, 4880, 4881, 4882,
			4883, 4904, 4905, 4906, 4907, 4952, 4953, 4954, 4955, 4976, 4977, 4978, 4979, 5013, 5014, 5554, 5574, 6109,
			6128, 6131, 6137, 6182, 6188, 6335, 6337, 6339, 6345, 6355, 6365, 6375, 6382, 6392, 6400, 6918, 6656, 2581,
			7539, 7394, 7396, 7534, 5574, 6885, 6858, 6860, 6862, 6856, 6326, 6128, 6137, 7400, 7323, 7325, 7327, 7003,
			10547, 10548, 10608, 10802, 10804, 10806, 10941, 11280, 12419, 12457, 13876, 7112, 7124 };

	/**
	 * Server amulet IDs.
	 */
	public static int amulets[] = { 1654, 1656, 1658, 1660, 1662, 1664, 8081, 8033, 7968, 6585, 86, 87, 295, 421, 552,
			589, 1478, 1692, 1694, 1696, 1698, 1700, 1702, 1704, 1706, 1708, 1710, 1712, 1725, 1727, 1729, 1731, 4021,
			4081, 4250, 4677, 6040, 6041, 6208, 1718, 1722, 6859, 6863, 6857, 10354, 10355, 10356, 10357, 10358, 10359,
			10360, 10361, 10362, 10363, 10364, 10365, 10366, 10367, 12851, 12852, 12853 };

	/**
	 * Server arrow IDs.
	 */
	public static int arrows[] = { 11212, 8052, 9211, 9010, 9209, 9208, 9207, 9206, 9205, 9203, 9301, 8065, 7919, 7906,
			7988, 7989, 78, 598, 877, 878, 879, 880, 881, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893,
			942, 2532, 2533, 2534, 2535, 2536, 2537, 2538, 2539, 2540, 2541, 2866, 4160, 4172, 4173, 4174, 4175, 4740,
			5616, 5617, 5618, 5619, 5620, 5621, 5622, 5623, 5624, 5625, 5626, 5627, 6061, 6062 };

	/**
	 * Server ring IDs.
	 */
	public static int rings[] = { 8082, 773, 1635, 1637, 1639, 1641, 1643, 1645, 2550, 2552, 2554, 2556, 2558, 2560,
			2562, 2564, 2566, 2568, 2570, 2572, 4202, 4657, 6465, 6737, 6731, 6735, 6735, 6583, 6733 };

	/**
	 * Body armour that covers your body.
	 */
	public static int body[] = { 7390, 20840, 7392, 577, 10400, 10404, 10408, 10412, 10416, 10420, 10424, 10428, 10432, 10436,
			10748, 10750, 10752, 10754, 10756, 7608, 2520, 430, 7362, 7364, 636, 638, 640, 642, 644, 8064, 426, 1005,
			1757, 7592, 8031, 8027, 6617, 8019, 8014, 8002, 7376, 544, 7372, 7370, 577, 7974, 7970, 7965, 7961, 7960,
			3793, 3775, 3773, 3771, 3769, 3767, 6139, 1135, 2499, 2501, 1035, 540, 5553, 4757, 1833, 6388, 6384, 4111,
			4101, 4091, 6186, 6184, 6180, 3058, 4509, 4504, 4069, 4728, 4736, 4712, 6107, 2661, 3140, 1101, 1103, 1105,
			1107, 1109, 1111, 1113, 1115, 1117, 1119, 1121, 1123, 1125, 1127, 1129, 1131, 1133, 2583, 2591, 2599, 2607,
			2615, 2623, 2653, 2669, 3481, 4712, 4720, 4728, 4749, 4892, 4893, 4894, 4895, 4916, 4917, 4918, 4919, 4964,
			4965, 4966, 4967, 6107, 6133, 6322, 6322, 6129, 75, 6916, 6916, 4111, 6654, 6654, 75, 7399, 7390, 7374,
			5575, 2503, 6341, 6351, 3387, 5030, 5032, 5034, 5030, 5032, 5034, 7392, 546, 10945, 13870, 12458, 12420,
			1035 };

	/**
	 * Leg armour that covers your legs.
	 */
	public static int legs[] = { 10438, 10434, 20842, 10430, 10426, 10422, 10418, 10414, 10410, 10406, 10402, 7609, 2522, 7378,
			7380, 7382, 7368, 7366, 7388, 646, 648, 650, 652, 654, 428, 1097, 1095, 7593, 8032, 8028, 6625, 8020, 8015,
			7384, 7969, 7966, 7962, 6141, 1835, 538, 1033, 5555, 4759, 6386, 6390, 2497, 2495, 2493, 1099, 4113, 4103,
			4093, 6924, 12459, 12421, 6187, 6185, 6181, 3059, 4510, 4505, 4070, 6108, 538, 542, 548, 1011, 1013, 1015,
			1067, 1069, 1071, 1073, 1075, 1077, 1079, 1081, 1083, 1085, 1087, 1089, 1091, 1093, 2585, 2593, 2601, 2609,
			2617, 2625, 2655, 2663, 2671, 3059, 3389, 3472, 3473, 3474, 3475, 3476, 3477, 3478, 3479, 3480, 3483, 3485,
			3795, 4087, 4585, 4712, 4714, 4722, 4730, 4738, 4751, 4759, 4874, 4875, 4876, 4877, 4898, 4899, 4900, 4901,
			4922, 4923, 4924, 4925, 4946, 4947, 4948, 4949, 4970, 4971, 4972, 4973, 4994, 4995, 4996, 4997, 5048, 5050,
			5052, 5576, 6107, 6130, 6187, 6390, 6386, 6390, 6396, 6404, 6135, 6809, 6916, 4091, 4111, 6655, 6654, 7398,
			7398, 7386, 6324, 6343, 6353, 7116, 7126, 7132, 7134, 3387, 5036, 5038, 5040, 13873,22328 };

	/**
	 * Body armour that are platebodies.
	 */
	public static int platebody[] = { 12277, 12235, 12205, 11020, 13887, 13884, 13858, 10551, 10338, 7608, 2520, 430,
			636, 638, 640, 642, 644, 426, 8031, 8027, 6617, 8019, 8014, 8002, 544, 577, 7974, 7970, 7965, 7961, 7960,
			3793, 3773, 3775, 3771, 3769, 3767, 6139, 1035, 540, 5553, 4757, 1833, 1835, 6388, 6384, 4111, 4101, 4868,
			4869, 4870, 4871, 4892, 4893, 4894, 4895, 4916, 4917, 4918, 4919, 4940, 4941, 4942, 4943, 4964, 4965, 4966,
			4967, 4988, 4989, 4990, 0x2f9a0eb, 6186, 6184, 6180, 3058, 4509, 4504, 4069, 4728, 4736, 4712, 6107, 2661,
			3140, 1115, 1117, 1119, 1121, 1123, 1125, 1127, 2583, 2591, 2599, 2607, 2615, 6322, 2623, 2653, 2669, 3481,
			4720, 4728, 4749, 2661, 6129, 6916, 4091, 6654, 6133, 75, 7399, 7390, 5575, 6341, 6351, 3387, 5030, 5032,
			5034, 7392, 1035,577,22327 };

	/**
	 * Body armour that covers your body and arms.
	 */
	private static String[] fullbody = { "platebody", "plate", "top", "wings", "blouse", "monk's robe", "shirt",
			"robetop", "robe top", "tabard", "jacket", "tunic", "chestplate", "leathertop", "brassard", "torso",
			"robe (g)", "robe (t)", "zamorak robe", "hauberk", "coat", "Mummy", "varrock", "decorative", "garb", "zamorak robe", "Justiciar chestguard"
			// "Ironman platebody", "Shayzien platebody", "Cow top", "wings",
			// "blouse",
			// "Spined body", "plate", "Monk's robe", "torso", "top", "shirt",
			// "platebody", "Ahrims robetop",
			// "Karils leathertop", "brassard", "Robe top", "robetop",
			// "platebody (t)", "robe (g)", "robe (t)",
			// "platebody (g)", "chestplate", "torso", "hauberk", "Dragon
			// chainbody", "Rangers tunic", "Santa jacket",
			// "Antisanta jacket", "Graceful top", "Zamorak Robe", "Musketeer
			// tabard"
	};


	/**
	 * Headgear that covers your head but not your face.
	 */
	private static String[] fullhat = { "splitbark helm", "mystic", "black mask", "highwayman mask", "mime mask",
			"facemask", "ironman helm", "grim reaper", "imp mask", "mage hat", "med helm", "coif",
			"Dharok's helm", "hood", "Initiate helm", "Coif", "Helm of neitiznot", "Armadyl helmet", "Berserker helm",
			"Archer helm", "Farseer helm", "Warrior helm", "Void", "reindeer", "cowl", "Void ranger helm",
			"Blacksmith's_helm", "black mask", "kandarin headgear", "Robin hood hat",
			"pith helmet", "spiny helmet", "gold helmet", "mining helmet", "camo helmet", "crab helmet",
			"3rd age full helmet", "dwarven helmet", "kandarin headgear", "camo"
	};

	/**
	 * Headgear that covers your head and your face.
	 */
	private static String[] fullmask = { "helm", "helmet_imbued", "helm_(g)", "Bronze_full_helm", "Iron_full_helm",
			"Mithril_full_helm", "helm_(t)", "full_helm(g)", "full", "heraldic", "heraldic_helm", "full_helm",
			"Verac's_helm", "Guthan's_helm", "Karil's_coif", "ween_mask", "Torag's_helm", "Void", "sallet",
			"slayer helmet", "Void_ranger_helm", "Shayzien", "Graceful", "gas_mask", "serpentine_helm", "magma_helm",
			"ankou", "demon", "mummy", "head", "Banshee", "Goblin mask", "Jack_lantern_mask", "h'ween",
			"slayer helmet (i)", "rogue mask", "santa mask", "antisanta mask", "Splitbark helm", "Justiciar faceguard"
	};

	public static String getNameForItemInSlot(Player player, int slot) {
		if (slot < 0 || slot > player.playerEquipment.length) {
			throw new IllegalStateException();
		}
		int id = player.playerEquipment[slot];
		return id == -1 ? "null" : ItemAssistant.getItemName(id);
	}

	/**
	 * Calls if an item is a full body item.
	 */
	public static boolean isFullBody(int itemId) {
		String armour = getItemName(itemId);
		if (armour == null)
			return false;

		switch (itemId) {
			case 11899:
			case 11896:
				return true;
		}
		for (int i = 0; i < fullbody.length; i++) {
			if (armour.contains(fullbody[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calls if an item is a full helm item.
	 */
	public static boolean isFullHat(int itemId) {
		String weapon = getItemName(itemId);

		if (weapon == null)
			return false;

		switch (itemId) {
			case 2581:
				return false;
		}
		for (int i = 0; i < fullhat.length; i++) {
			if (weapon.contains(fullhat[i]) || weapon.equals(fullhat[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calls if an item is a full mask item.
	 */
	public static boolean isFullMask(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;

		if (weapon.contains("slayer helm"))
			return true;

		if (weapon.contains("headband"))
			return false;

		switch (itemId) {
			case 10507:
			case 12887:
			case 20095:
				return true;

			case 12516:
			case 12013:
				return false;
		}
		for (int i = 0; i < fullmask.length; i++) {
			if (!isFullHat(itemId)) {
				if (weapon.contains(fullmask[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets an item name from the itemlist.
	 */
	public static String getItemName(int id) {
		if (id < 0) {
			return null;
		}
//		
//		ItemAssistant.getItemName(id);
//
//		ItemList itemList = Server.itemHandler.ItemList[id];
//
//		if (itemList == null) {
//			return null;
//		}
		return ItemAssistant.getItemName(id);
	}

	/**
	 * Checks if the item is stackable.
	 */
	public static boolean[] itemStackable = new boolean[Config.ITEM_LIMIT];

	/**
	 * Checks if the item can be made into a note.
	 */
	public static boolean[] itemIsNote = new boolean[Config.ITEM_LIMIT];

	/**
	 * Checks if the item can wield into a slot.
	 */
	public static int[] targetSlots = new int[Config.ITEM_LIMIT];

	static {
		try {
			List<String> stackableData = Files.readAllLines(Paths.get("./Data/", "data", "stackables.dat"));
			for (String data : stackableData) {
				int id = Integer.parseInt(data.split("\t")[0]);
				boolean stackable = Boolean.parseBoolean(data.split("\t")[1]);
				itemStackable[id] = stackable;
				itemStackable[21880] = true;
				itemStackable[6646] = true;
				itemStackable[6651] = true;
				itemStackable[21930] = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		int counter = 0;
		int c = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemIsNote[counter] = true;
				} else {
					itemIsNote[counter] = false;
				}
				counter++;
			}
			dataIn.close();
			System.out.println("Loaded " + counter + " noted configurations");
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}

		counter = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/equipment.dat"));
			while ((c = dataIn.read()) != -1) {
				targetSlots[counter++] = c;
			}
			dataIn.close();
			System.out.println("Loaded " + counter + " equipment configurations");
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
	}
}
