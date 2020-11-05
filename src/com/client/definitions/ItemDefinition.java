package com.client.definitions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.client.DrawingArea;
import com.client.MRUNodes;
import com.client.Model;
import com.client.Rasterizer;
import com.client.Sprite;
import com.client.Stream;
import com.client.StreamLoader;
import com.client.sign.Signlink;
import com.client.utilities.FileOperations;

public final class ItemDefinition {

	public static void unpackConfig(final StreamLoader streamLoader) {
		// stream = new Stream(streamLoader.getDataForName("obj.dat"));
		// Stream stream = new Stream(streamLoader.getDataForName("obj.idx"));
		stream = new Stream(FileOperations.readFile(Signlink.getCacheDirectory() + "/data/obj.dat"));
		final Stream stream = new Stream(FileOperations.readFile(Signlink.getCacheDirectory() + "/data/obj.idx"));

		totalItems = stream.readUnsignedWord();
		streamIndices = new int[totalItems + 1000];
		int i = 2;
		for (int j = 0; j < totalItems; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}

		cache = new ItemDefinition[10];
		for (int index = 0; index < 10; index++) {
			cache[index] = new ItemDefinition();
		}
		//itemDump();
	}

	public static ItemDefinition forID(int itemId) {
		for (int j = 0; j < 10; j++) {
			if (cache[j].id == itemId) {
				return cache[j];
			}
		}

		if (itemId == -1)
			itemId = 0;
		if (itemId > streamIndices.length)
			itemId = 0;

		cacheIndex = (cacheIndex + 1) % 10;
		final ItemDefinition itemDef = cache[cacheIndex];
		stream.currentOffset = streamIndices[itemId];
		itemDef.id = itemId;
		itemDef.setDefaults();
		itemDef.readValues(stream);

		if (itemDef.certTemplateID != -1) {
			itemDef.toNote();
		}

		if (itemDef.opcode140 != -1) {
			itemDef.method2789(forID(itemDef.opcode140), forID(itemDef.opcode139));
		}

		if (itemDef.opcode149 != -1) {
			itemDef.method2790(forID(itemDef.opcode149), forID(itemDef.opcode148));
		}

		customItems(itemDef.id);

		switch (itemId) {

		}
		return itemDef;
	}

	private static void customItems(int itemId) {
		ItemDefinition itemDef = forID(itemId);

		switch (itemId) {

		case 11864:
		case 11865:
		case 19639:
		case 19641:
		case 19643:
		case 19645:
		case 19647:
		case 19649:
			itemDef.equipActions[2] = "Log";
			itemDef.equipActions[1] = "Check";
			break;

		case 13136:
			itemDef.equipActions[2] = "Elidinis";
			itemDef.equipActions[1] = "Kalphite Hive";
			break;
		case 2550:
			itemDef.equipActions[2] = "Check";
			break;

		case 1712:
		case 1710:
		case 1708:
		case 1706:
			itemDef.equipActions[1] = "Edgeville";
			itemDef.equipActions[2] = "Karamja";
			itemDef.equipActions[3] = "Draynor";
			itemDef.equipActions[4] = "Al-Kharid";
			break;

		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566: // Ring of duelling
			itemDef.equipActions[2] = "Shantay Pass";
			itemDef.equipActions[1] = "Clan wars";
			break;

			case 21307:
				itemDef.name = "Pursuit crate";
				break;
			case 2996:
				itemDef.name = "PKP Ticket";
				itemDef.inventoryOptions = new String[5];
				itemDef.description = "Exchange this for a PK Point.";
				break;
			case 13346:
				itemDef.name = "Ultra Mystery Box";
				itemDef.inventoryOptions = new String[] { "Open", null, null, null, "Drop" };
				itemDef.description = "Mystery box that contains goodies.";
				break;
			case 15098:
				itemDef.name = "Dice (up to 100)";
				itemDef.description = "A 100-sided dice.";
				itemDef.modelId = 31223;
				itemDef.modelZoom = 1104;
				itemDef.modelRotation2 = 215;
				itemDef.modelRotation1 = 94;
				itemDef.modelOffset2 = -5;
				itemDef.modelOffset1 = -18;
				itemDef.inventoryOptions = new String[5];
				itemDef.inventoryOptions[1] = "Public-roll";
				itemDef.inventoryOptions[2] = null;
				itemDef.name = "Dice (up to 100)";
				itemDef.anInt196 = 15;
				itemDef.anInt184 = 25;
				break;
		case 11773:
		case 11771:
		case 11770:
		case 11772:
			itemDef.anInt196 += 45;
			break;
		case 2697:
			itemDef.name = "$10 Scroll";
			itemDef.description = "Read this scroll to be rewarded with the Donator status.";
			break;
		case 2698:
			itemDef.name = "$50 Scroll";
			itemDef.description = "Read this scroll to be rewarded with the Super Donator status.";
			break;
		case 2699:
			itemDef.name = "$150 Donator";
			itemDef.description = "Read this scroll to be rewarded with the Extreme Donator status.";
			break;
		case 2700:
			itemDef.name = "$300 Scroll";
			itemDef.description = "Read this scroll to be rewarded with the Legendary Donator status.";
			break;
		case 1464:
			itemDef.name = "Vote ticket";
			itemDef.description = "This ticket can be exchanged for a voting point.";
			break;
		}
	}

	void method2789(ItemDefinition var1, ItemDefinition var2) {
		modelId = var1.modelId * 1;
		modelZoom = var1.modelZoom * 1;
		modelRotation1 = 1 * var1.modelRotation1;
		modelRotation2 = 1 * var1.modelRotation2;
		anInt204 = 1 * var1.anInt204;
		modelOffset1 = 1 * var1.modelOffset1;
		modelOffset2 = var1.modelOffset2 * 1;
		originalModelColors = var2.originalModelColors;
		modifiedModelColors = var2.modifiedModelColors;
		// originalTextureColors = var2.originalTextureColors;
		// modifiedTextureColors = var2.modifiedTextureColors;
		name = var2.name;
		membersObject = var2.membersObject;
		stackable = var2.stackable;
		maleModel = 1 * var2.maleModel;
		anInt188 = 1 * var2.anInt188;
		anInt185 = 1 * var2.anInt185;
		femaleModel = var2.femaleModel * 1;
		anInt164 = var2.anInt164 * 1;
		anInt162 = 1 * var2.anInt162;
		anInt175 = 1 * var2.anInt175;
		anInt166 = var2.anInt166 * 1;
		anInt197 = var2.anInt197 * 1;
		anInt173 = var2.anInt173 * 1;
		team = var2.team * 1;
		groundOptions = var2.groundOptions;
		inventoryOptions = new String[5];
		equipActions = new String[5];
		if (null != var2.inventoryOptions) {
			for (int var4 = 0; var4 < 4; ++var4) {
				inventoryOptions[var4] = var2.inventoryOptions[var4];
			}
		}

		inventoryOptions[4] = "Discard";
		value = 0;
	}

	void method2790(ItemDefinition var1, ItemDefinition var2) {
		modelId = var1.modelId * 1;
		modelZoom = 1 * var1.modelZoom;
		modelRotation1 = var1.modelRotation1 * 1;
		modelRotation2 = var1.modelRotation2 * 1;
		anInt204 = var1.anInt204 * 1;
		modelOffset1 = 1 * var1.modelOffset1;
		modelOffset2 = var1.modelOffset2 * 1;
		originalModelColors = var1.originalModelColors;
		modifiedModelColors = var1.modifiedModelColors;
		originalTextureColors = var1.originalTextureColors;
		modifiedTextureColors = var1.modifiedTextureColors;
		stackable = var1.stackable;
		name = var2.name;
		value = 0;
	}

	/*
	 * private void readValues(Stream stream) { while(true) { int opcode =
	 * stream.readUnsignedByte(); if(opcode == 0) return; if(opcode == 1) modelId =
	 * stream.readUnsignedWord(); else if(opcode == 2) name = stream.readString();
	 * else if(opcode == 3) description = stream.readString(); else if(opcode == 4)
	 * modelZoom = stream.readUnsignedWord(); else if(opcode == 5) modelRotation1 =
	 * stream.readUnsignedWord(); else if(opcode == 6) modelRotation2 =
	 * stream.readUnsignedWord(); else if(opcode == 7) { modelOffset1 =
	 * stream.readUnsignedWord(); if(modelOffset1 > 32767) modelOffset1 -= 0x10000;
	 * } else if(opcode == 8) { modelOffset2 = stream.readUnsignedWord();
	 * if(modelOffset2 > 32767) modelOffset2 -= 0x10000; } else if(opcode == 11)
	 * stackable = true; else if(opcode == 12) value = stream.readDWord(); else
	 * if(opcode == 16) membersObject = true; else if(opcode == 23) { maleModel =
	 * stream.readUnsignedWord(); aByte205 = stream.readSignedByte(); } else if
	 * (opcode == 24) anInt188 = stream.readUnsignedWord(); else if (opcode == 25) {
	 * femaleModel = stream.readUnsignedWord(); aByte154 = stream.readSignedByte();
	 * } else if (opcode == 26) anInt164 = stream.readUnsignedWord(); else if(opcode
	 * >= 30 && opcode < 35) { if(groundOptions == null) groundOptions = new
	 * String[5]; groundOptions[opcode - 30] = stream.readString();
	 * if(groundOptions[opcode - 30].equalsIgnoreCase("hidden"))
	 * groundOptions[opcode - 30] = null; } else if(opcode >= 35 && opcode < 40) {
	 * if(inventoryOptions == null) inventoryOptions = new String[5];
	 * inventoryOptions[opcode - 35] = stream.readString(); } else if(opcode == 40)
	 * { int size = stream.readUnsignedByte(); originalModelColors = new int[size];
	 * modifiedModelColors = new int[size]; for(int index = 0; index < size;
	 * index++) { originalModelColors[index] = stream.readUnsignedWord();
	 * modifiedModelColors[index] = stream.readUnsignedWord(); } } else if(opcode ==
	 * 41) { int size = stream.readUnsignedByte(); originalTextureColors = new
	 * short[size]; modifiedTextureColors = new short[size]; for(int index = 0;
	 * index < size; index++) { originalTextureColors[index] = (short)
	 * stream.readUnsignedWord(); modifiedTextureColors[index] = (short)
	 * stream.readUnsignedWord(); } } else if(opcode == 65) { searchableItem = true;
	 * } else if(opcode == 78) anInt185 = stream.readUnsignedWord(); else if(opcode
	 * == 79) anInt162 = stream.readUnsignedWord(); else if(opcode == 90) anInt175 =
	 * stream.readUnsignedWord(); else if(opcode == 91) anInt197 =
	 * stream.readUnsignedWord(); else if(opcode == 92) anInt166 =
	 * stream.readUnsignedWord(); else if(opcode == 93) anInt173 =
	 * stream.readUnsignedWord(); else if(opcode == 95) anInt204 =
	 * stream.readUnsignedWord(); else if(opcode == 97) certID =
	 * stream.readUnsignedWord(); else if(opcode == 98) certTemplateID =
	 * stream.readUnsignedWord(); else if (opcode >= 100 && opcode < 110) { if
	 * (stackIDs == null) { stackIDs = new int[10]; stackAmounts = new int[10]; }
	 * stackIDs[opcode - 100] = stream.readUnsignedWord(); stackAmounts[opcode -
	 * 100] = stream.readUnsignedWord(); } else if(opcode == 110) anInt167 =
	 * stream.readUnsignedWord(); else if(opcode == 111) anInt192 =
	 * stream.readUnsignedWord(); else if(opcode == 112) anInt191 =
	 * stream.readUnsignedWord(); else if(opcode == 113) anInt196 =
	 * stream.readSignedByte(); else if(opcode == 114) anInt184 =
	 * stream.readSignedByte() * 5; else if(opcode == 115) team =
	 * stream.readUnsignedByte(); else if (opcode == 139) opcode139 =
	 * stream.readUnsignedWord(); else if (opcode == 140) opcode140 =
	 * stream.readUnsignedWord(); else if (opcode == 148) opcode148 =
	 * stream.readUnsignedWord(); else if (opcode == 149) opcode149 =
	 * stream.readUnsignedWord(); else { System.out.println("Error loading item " +
	 * id + ", opcode " + opcode); } } }
	 */

	private void readValues(Stream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				return;
			if (opcode == 1)
				modelId = stream.readUnsignedWord();
			else if (opcode == 2)
				name = stream.readString();
			else if (opcode == 3)
				description = stream.readString();
			else if (opcode == 4)
				modelZoom = stream.readUnsignedWord();
			else if (opcode == 5)
				modelRotation1 = stream.readUnsignedWord();
			else if (opcode == 6)
				modelRotation2 = stream.readUnsignedWord();
			else if (opcode == 7) {
				modelOffset1 = stream.readUnsignedWord();
				if (modelOffset1 > 32767)
					modelOffset1 -= 0x10000;
			} else if (opcode == 8) {
				modelOffset2 = stream.readUnsignedWord();
				if (modelOffset2 > 32767)
					modelOffset2 -= 0x10000;
			} else if (opcode == 11)
				stackable = true;
			else if (opcode == 12)
				value = stream.readDWord();
			else if (opcode == 16)
				membersObject = true;
			else if (opcode == 23) {
				maleModel = stream.readUnsignedWord();
				aByte205 = stream.readSignedByte();
			} else if (opcode == 24)
				anInt188 = stream.readUnsignedWord();
			else if (opcode == 25) {
				femaleModel = stream.readUnsignedWord();
				aByte154 = stream.readSignedByte();
			} else if (opcode == 26)
				anInt164 = stream.readUnsignedWord();
			else if (opcode >= 30 && opcode < 35) {
				if (groundOptions == null)
					groundOptions = new String[5];
				groundOptions[opcode - 30] = stream.readString();
				if (groundOptions[opcode - 30].equalsIgnoreCase("hidden"))
					groundOptions[opcode - 30] = null;
			} else if (opcode >= 35 && opcode < 40) {
				if (inventoryOptions == null)
					inventoryOptions = new String[5];
				inventoryOptions[opcode - 35] = stream.readString();
			} else if (opcode == 40) {
				int size = stream.readUnsignedByte();
				originalModelColors = new int[size];
				modifiedModelColors = new int[size];
				for (int index = 0; index < size; index++) {
					originalModelColors[index] = stream.readUnsignedWord();
					modifiedModelColors[index] = stream.readUnsignedWord();
				}
			} else if (opcode == 41) {
				int size = stream.readUnsignedByte();
				originalTextureColors = new short[size];
				modifiedTextureColors = new short[size];
				for (int index = 0; index < size; index++) {
					originalTextureColors[index] = (short) stream.readUnsignedWord();
					modifiedTextureColors[index] = (short) stream.readUnsignedWord();
				}
			} else if (opcode == 65) {
				searchableItem = true;
			} else if (opcode == 78)
				anInt185 = stream.readUnsignedWord();
			else if (opcode == 79)
				anInt162 = stream.readUnsignedWord();
			else if (opcode == 90)
				anInt175 = stream.readUnsignedWord();
			else if (opcode == 91)
				anInt197 = stream.readUnsignedWord();
			else if (opcode == 92)
				anInt166 = stream.readUnsignedWord();
			else if (opcode == 93)
				anInt173 = stream.readUnsignedWord();
			else if (opcode == 95)
				anInt204 = stream.readUnsignedWord();
			else if (opcode == 97)
				certID = stream.readUnsignedWord();
			else if (opcode == 98)
				certTemplateID = stream.readUnsignedWord();
			else if (opcode >= 100 && opcode < 110) {
				if (stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[opcode - 100] = stream.readUnsignedWord();
				stackAmounts[opcode - 100] = stream.readUnsignedWord();
			} else if (opcode == 110)
				anInt167 = stream.readUnsignedWord();
			else if (opcode == 111)
				anInt192 = stream.readUnsignedWord();
			else if (opcode == 112)
				anInt191 = stream.readUnsignedWord();
			else if (opcode == 113)
				anInt196 = stream.readSignedByte();
			else if (opcode == 114)
				anInt184 = stream.readSignedByte() * 5;
			else if (opcode == 115)
				team = stream.readUnsignedByte();
			else if (opcode == 139)
				opcode139 = stream.readUnsignedWord();
			else if (opcode == 140)
				opcode140 = stream.readUnsignedWord();
			else if (opcode == 148)
				opcode148 = stream.readUnsignedWord();
			else if (opcode == 149)
				opcode149 = stream.readUnsignedWord();
			else {
				// System.out.println("Error loading item " + id + ", opcode " + opcode);
			}
		}
	}

	public int opcode139, opcode140, opcode148, opcode149;

	public static void nullLoader() {
		mruNodes2 = null;
		mruNodes1 = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public boolean method192(int j) {
		int k = anInt175;
		int l = anInt166;
		if (j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.method463(k))
			flag = false;
		if (l != -1 && !Model.method463(l))
			flag = false;
		return flag;
	}

	public Model method194(int j) {
		int k = anInt175;
		int l = anInt166;
		if (j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if (k == -1)
			return null;
		Model model = Model.method462(k);
		if (l != -1) {
			Model model_1 = Model.method462(l);
			Model aclass30_sub2_sub4_sub6s[] = { model, model_1 };
			model = new Model(2, aclass30_sub2_sub4_sub6s);
		}
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < modifiedModelColors.length; i1++)
				model.method476(modifiedModelColors[i1], originalModelColors[i1]);

		}
		return model;
	}

	public boolean method195(int j) {
		int k = maleModel;
		int l = anInt188;
		int i1 = anInt185;
		if (j == 1) {
			k = femaleModel;
			l = anInt164;
			i1 = anInt162;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.method463(k))
			flag = false;
		if (l != -1 && !Model.method463(l))
			flag = false;
		if (i1 != -1 && !Model.method463(i1))
			flag = false;
		return flag;
	}

	public Model method196(int i) {
		int j = maleModel;
		int k = anInt188;
		int l = anInt185;
		if (i == 1) {
			j = femaleModel;
			k = anInt164;
			l = anInt162;
		}
		if (j == -1)
			return null;
		Model model = Model.method462(j);
		if (k != -1)
			if (l != -1) {
				Model model_1 = Model.method462(k);
				Model model_3 = Model.method462(l);
				Model aclass30_sub2_sub4_sub6_1s[] = { model, model_1, model_3 };
				model = new Model(3, aclass30_sub2_sub4_sub6_1s);
			} else {
				Model model_2 = Model.method462(k);
				Model aclass30_sub2_sub4_sub6s[] = { model, model_2 };
				model = new Model(2, aclass30_sub2_sub4_sub6s);
			}
		if (i == 0 && aByte205 != 0)
			model.method475(0, aByte205, 0);
		if (i == 1 && aByte154 != 0)
			model.method475(0, aByte154, 0);
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < modifiedModelColors.length; i1++)
				model.method476(modifiedModelColors[i1], originalModelColors[i1]);

		}
		return model;
	}

	private void setDefaults() {
		// equipActions = new String[6];
		equipActions = new String[] { "Remove", null, "Operate", null, null };
		modelId = 0;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		modifiedTextureColors = null;
		originalTextureColors = null;
		modelZoom = 2000;
		modelRotation1 = 0;
		modelRotation2 = 0;
		anInt204 = 0;
		modelOffset1 = 0;
		modelOffset2 = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundOptions = null;
		inventoryOptions = null;
		maleModel = -1;
		anInt188 = -1;
		aByte205 = 0;
		femaleModel = -1;
		anInt164 = -1;
		aByte154 = 0;
		anInt185 = -1;
		anInt162 = -1;
		anInt175 = -1;
		anInt166 = -1;
		anInt197 = -1;
		anInt173 = -1;
		stackIDs = null;
		stackAmounts = null;
		certID = -1;
		certTemplateID = -1;
		anInt167 = 128;
		anInt192 = 128;
		anInt191 = 128;
		anInt196 = 0;
		anInt184 = 0;
		team = 0;

		opcode140 = -1;
		opcode139 = -1;
		opcode148 = -1;
		opcode149 = -1;

		searchableItem = false;
	}

	public static void dumpBonuses() {
		int[] bonuses = new int[14];
		int bonus = 0;
		int amount = 0;
		for (int i = 21304; i < totalItems; i++) {
			ItemDefinition item = ItemDefinition.forID(i);
			URL url;
			try {
				try {
					try {
						url = new URL("http://2007.runescape.wikia.com/wiki/" + item.name.replaceAll(" ", "_"));
						URLConnection con = url.openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line;
						BufferedWriter writer = new BufferedWriter(new FileWriter("item.cfg", true));
						while ((line = in.readLine()) != null) {
							try {
								if (line.contains("<td style=\"text-align: center; width: 35px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "")
											.replace("\"\"", "")
											.replace("<td style=\"text-align: center; width: 35px;\">", "");
									bonuses[bonus] = Integer.parseInt(line);
									bonus++;
								} else if (line.contains("<td style=\"text-align: center; width: 30px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<td style=\"text-align: center; width: 30px;\">", "");
									bonuses[bonus] = Integer.parseInt(line);
									bonus++;
								}
							} catch (NumberFormatException e) {

							}
							if (bonus >= 13)
								bonus = 0;
							// in.close();
						}
						in.close();
						writer.write("item	=	" + i + "	" + item.name.replace(" ", "_") + "	"
								+ item.description.replace(" ", "_") + "	" + item.value + "	" + item.value + "	"
								+ item.value + "	" + bonuses[0] + "	" + bonuses[1] + "	" + bonuses[2] + "	"
								+ bonuses[3] + "	" + bonuses[4] + "	" + bonuses[5] + "	" + bonuses[6] + "	"
								+ bonuses[7] + "	" + bonuses[8] + "	" + bonuses[9] + "	" + bonuses[10] + "	"
								+ bonuses[13]);
						bonuses[0] = bonuses[1] = bonuses[2] = bonuses[3] = bonuses[4] = bonuses[5] = bonuses[6] = bonuses[7] = bonuses[8] = bonuses[9] = bonuses[10] = bonuses[13] = 0;
						writer.newLine();
						amount++;
						writer.close();
					} catch (NullPointerException e) {

					}
				} catch (FileNotFoundException e) {

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done dumping " + amount + " item bonuses!");
	}

	public static void dumpBonus() {
		final int[] wikiBonuses = new int[18];
		int bonus = 0;
		int amount = 0;
		System.out.println("Starting to dump item bonuses...");
		for (int i = 20000; i < totalItems; i++) {
			ItemDefinition item = ItemDefinition.forID(i);
			try {
				try {
					try {
						final URL url = new URL(
								"http://2007.runescape.wikia.com/wiki/" + item.name.replaceAll(" ", "_"));
						URLConnection con = url.openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line;
						writer = new BufferedWriter(new FileWriter("item.cfg", true));
						while ((line = in.readLine()) != null) {
							try {
								if (line.contains("<td style=\"text-align: center; width: 35px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "")
											.replace("\"\"", "")
											.replace("<td style=\"text-align: center; width: 35px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								} else if (line.contains("<td style=\"text-align: center; width: 30px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<td style=\"text-align: center; width: 30px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							in.close();
							writer.write("item = " + i + "	" + item.name.replace(" ", "_") + "	"
									+ item.description.replace(" ", "_") + "	" + item.value + "	" + item.value
									+ "	" + item.value + "	" + wikiBonuses[0] + "	" + wikiBonuses[1] + "	"
									+ wikiBonuses[2] + "	" + wikiBonuses[3] + "	" + wikiBonuses[4] + "	"
									+ wikiBonuses[5] + "	" + wikiBonuses[6] + "	" + wikiBonuses[7] + "	"
									+ wikiBonuses[8] + "	" + wikiBonuses[9] + "	" + wikiBonuses[10] + "	"
									+ wikiBonuses[13]);
							amount++;
							wikiBonuses[0] = wikiBonuses[1] = wikiBonuses[2] = wikiBonuses[3] = wikiBonuses[4] = wikiBonuses[5] = wikiBonuses[6] = wikiBonuses[7] = wikiBonuses[8] = wikiBonuses[9] = wikiBonuses[10] = wikiBonuses[11] = wikiBonuses[13] = 0;
							writer.newLine();
							writer.close();
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Done dumping " + amount + " item bonuses!");
		}
	}

	public static void dumpItemDefs() {
		final int[] wikiBonuses = new int[18];
		int bonus = 0;
		int amount = 0;
		int value = 0;
		int slot = -1;
		// Testing Variables just so i know format is correct
		String fullmask = "false";
		// boolean stackable1 = false;
		String stackable = "false";
		// boolean noteable1 = false;
		String noteable = "true";
		// boolean tradeable1 = false;
		String tradeable = "true";
		// boolean wearable1 = false;
		String wearable = "true";
		String showBeard = "true";
		String members = "true";
		boolean twoHanded = false;
		System.out.println("Starting to dump item definitions...");
		for (int i = 21298; i < totalItems; i++) {
			ItemDefinition item = ItemDefinition.forID(i);
			try {
				try {
					try {
						final URL url = new URL(
								"http://2007.runescape.wikia.com/wiki/" + item.name.replaceAll(" ", "_"));
						URLConnection con = url.openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line;
						writer = new BufferedWriter(new FileWriter("itemDefs.json", true));
						while ((line = in.readLine()) != null) {
							try {
								if (line.contains("<td style=\"text-align: center; width: 35px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "")
											.replace("\"\"", "")
											.replace("<td style=\"text-align: center; width: 35px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								} else if (line.contains("<td style=\"text-align: center; width: 30px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<td style=\"text-align: center; width: 30px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								}
								if (line.contains("<div id=\"GEPCalcResult\" style=\"display:inline;\">")) {
									line = line.replace("</div>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<div id=\"GEPCalcResult\" style=\"display:inline;\">", "");
									value = Integer.parseInt(line);
								}

							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							in.close();
							// fw.write("ItemID: "+itemDefinition.id+" - "+itemDefinition.name);
							// fw.write(System.getProperty("line.separator"));
							// writer.write("[\n");
							writer.write("  {\n\t\"id\": " + item.id + ",\n\t\"name\": \"" + item.name
									+ "\",\n\t\"desc\": \"" + item.description.replace("_", " ") + "\",\n\t\"value\": "
									+ value + ",\n\t\"dropValue\": " + value + ",\n\t\"bonus\": [\n\t  "
									+ wikiBonuses[0] + ",\n\t  " + wikiBonuses[1] + ",\n\t  " + wikiBonuses[2]
									+ ",\n\t  " + wikiBonuses[3] + ",\n\t  " + wikiBonuses[4] + ",\n\t  "
									+ wikiBonuses[5] + ",\n\t  " + wikiBonuses[6] + ",\n\t  " + wikiBonuses[7]
									+ ",\n\t  " + wikiBonuses[8] + ",\n\t  " + wikiBonuses[9] + ",\n\t  "
									+ wikiBonuses[10] + ",\n\t  " + wikiBonuses[13] + ",\n\t],\n\t\"slot\": " + slot
									+ ",\n\t\"fullmask\": " + fullmask + ",\n\t\"stackable\": " + stackable
									+ ",\n\t\"noteable\": " + noteable + ",\n\t\"tradeable\": " + tradeable
									+ ",\n\t\"wearable\": " + wearable + ",\n\t\"showBeard\": " + showBeard
									+ ",\n\t\"members\": " + members + ",\n\t\"slot\": " + twoHanded
									+ ",\n\t\"requirements\": [\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t]\n  },\n");
							/*
							 * writer.write("item = " + i + "	" + item.name.replace(" ", "_") + "	" +
							 * item.description.replace(" ", "_") + "	" + item.value + "	" + item.value +
							 * "	" + item.value + "	" + wikiBonuses[0] + "	" + wikiBonuses[1] + "	" +
							 * wikiBonuses[2] + "	" + wikiBonuses[3] + "	" + wikiBonuses[4] + "	" +
							 * wikiBonuses[5] + "	" + wikiBonuses[6] + "	" + wikiBonuses[7] + "	" +
							 * wikiBonuses[8] + "	" + wikiBonuses[9] + "	" + wikiBonuses[10] + "	" +
							 * wikiBonuses[13]);
							 */
							amount++;
							wikiBonuses[0] = wikiBonuses[1] = wikiBonuses[2] = wikiBonuses[3] = wikiBonuses[4] = wikiBonuses[5] = wikiBonuses[6] = wikiBonuses[7] = wikiBonuses[8] = wikiBonuses[9] = wikiBonuses[10] = wikiBonuses[11] = wikiBonuses[13] = 0;
							writer.newLine();
							writer.close();
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Done dumping " + amount + " item definitions!");
		}
	}

	public static void dumpStackableList() {
		try {
			File file = new File("stackables.dat");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < totalItems; i++) {
					ItemDefinition definition = forID(i);
					if (definition != null) {
						writer.write(definition.id + "\t" + definition.stackable);
						writer.newLine();
					} else {
						writer.write(i + "\tfalse");
						writer.newLine();
					}
				}
			}

			System.out.println("Finished dumping noted items definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int[] unNoteable = {};

	public static void dumpNotes() {
		try {
			FileOutputStream out = new FileOutputStream(new File("notes.dat"));
			for (int j = 0; j < totalItems; j++) {
				ItemDefinition item = ItemDefinition.forID(j);
				for (int i = 0; i < totalItems; i++)
					if (j == unNoteable[i] + 1)
						out.write(0);
					else
						out.write(item.certTemplateID != -1 ? 0 : 1);
			}
			out.write(-1);
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void dumpStackable() {
		try {
			FileOutputStream out = new FileOutputStream(new File("stackable.dat"));
			for (int j = 0; j < totalItems; j++) {
				ItemDefinition item = ItemDefinition.forID(j);
				out.write(item.stackable ? 1 : 0);
			}
			out.write(-1);
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void dumpNotableList() {
		try {
			File file = new File("note_id.dat");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < totalItems; i++) {
					ItemDefinition definition = ItemDefinition.forID(i);
					if (definition != null) {
						if (definition.certTemplateID == -1 && definition.certID != -1) {
							writer.write(definition.id + "\t" + definition.certID);
							writer.newLine();
						}
					} else {
						writer.write(i + "\t-1");
						writer.newLine();
					}
				}
			}

			System.out.println("Finished dumping noted items definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void toNote() {
		ItemDefinition itemDef = forID(certTemplateID);
		modelId = itemDef.modelId;
		modelZoom = itemDef.modelZoom;
		modelRotation1 = itemDef.modelRotation1;
		modelRotation2 = itemDef.modelRotation2;

		anInt204 = itemDef.anInt204;
		modelOffset1 = itemDef.modelOffset1;
		modelOffset2 = itemDef.modelOffset2;
		modifiedModelColors = itemDef.modifiedModelColors;
		originalModelColors = itemDef.originalModelColors;
		ItemDefinition itemDef_1 = forID(certID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String s = "a";
		char c = itemDef_1.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
			s = "an";
		description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".");
		stackable = true;
	}

	public static Sprite getSmallSprite(int itemId) {
		ItemDefinition itemDef = forID(itemId);
		Model model = itemDef.method201(1);
		if (model == null) {
			return null;
		}
		Sprite sprite1 = null;
		if (itemDef.certTemplateID != -1) {
			sprite1 = getSprite(itemDef.certID, 10, -1);
			if (sprite1 == null) {
				return null;
			}
		}
		Sprite enabledSprite = new Sprite(18, 18);
		int k1 = Rasterizer.textureInt1;
		int l1 = Rasterizer.textureInt2;
		int ai[] = Rasterizer.anIntArray1472;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Rasterizer.aBoolean1464 = false;
		DrawingArea.initDrawingArea(18, 18, enabledSprite.myPixels, new float[1024]);
		DrawingArea.method336(18, 0, 0, 0, 18);
		Rasterizer.method364();
		int k3 = (int) (itemDef.modelZoom * 1.6D);
		int l3 = Rasterizer.anIntArray1470[itemDef.modelRotation1] * k3 >> 16;
		int i4 = Rasterizer.anIntArray1471[itemDef.modelRotation1] * k3 >> 16;
		model.method482(itemDef.modelRotation2, itemDef.anInt204, itemDef.modelRotation1, itemDef.modelOffset1,
				l3 + model.modelHeight / 2 + itemDef.modelOffset2, i4 + itemDef.modelOffset2);
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite1.maxWidth;
			int j6 = sprite1.maxHeight;
			sprite1.maxWidth = 18;
			sprite1.maxHeight = 18;
			sprite1.drawSprite(0, 0);
			sprite1.maxWidth = l5;
			sprite1.maxHeight = j6;
		}
		DrawingArea.initDrawingArea(j2, i2, ai1, new float[1024]);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Rasterizer.textureInt1 = k1;
		Rasterizer.textureInt2 = l1;
		Rasterizer.anIntArray1472 = ai;
		Rasterizer.aBoolean1464 = true;

		enabledSprite.maxWidth = 18;
		enabledSprite.maxHeight = 18;

		return enabledSprite;
	}

	public static Sprite getSprite(int itemId, int itemAmount, int highlightColor) {
		if (highlightColor == 0) {
			Sprite sprite = (Sprite) mruNodes1.insertFromCache(itemId);
			if (sprite != null && sprite.maxHeight != itemAmount && sprite.maxHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDefinition itemDef = forID(itemId);
		if (itemDef.stackIDs == null)
			itemAmount = -1;
		if (itemAmount > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (itemAmount >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];

			if (i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.method201(1);
		if (model == null)
			return null;
		Sprite sprite = null;
		if (itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if (sprite == null)
				return null;
		} else if (itemDef.opcode140 != -1) {
			sprite = getSprite(itemDef.opcode139, itemAmount, -1);
			if (sprite == null)
				return null;
		} else if (itemDef.opcode149 != -1) {
			sprite = getSprite(itemDef.opcode148, itemAmount, -1);
			if (sprite == null)
				return null;
		}
		Sprite sprite2 = new Sprite(32, 32);
		int k1 = Rasterizer.textureInt1;
		int l1 = Rasterizer.textureInt2;
		int ai[] = Rasterizer.anIntArray1472;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Rasterizer.aBoolean1464 = false;
		DrawingArea.initDrawingArea(32, 32, sprite2.myPixels, new float[1024]);
		DrawingArea.method336(32, 0, 0, 0, 32);
		Rasterizer.method364();
		if (itemDef.opcode149 != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		int k3 = itemDef.modelZoom;
		if (highlightColor == -1)
			k3 = (int) ((double) k3 * 1.5D);
		if (highlightColor > 0)
			k3 = (int) ((double) k3 * 1.04D);
		int l3 = Rasterizer.anIntArray1470[itemDef.modelRotation1] * k3 >> 16;
		int i4 = Rasterizer.anIntArray1471[itemDef.modelRotation1] * k3 >> 16;
		model.method482(itemDef.modelRotation2, itemDef.anInt204, itemDef.modelRotation1, itemDef.modelOffset1,
				l3 + model.modelHeight / 2 + itemDef.modelOffset2, i4 + itemDef.modelOffset2);
		if (itemDef.opcode140 != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		for (int i5 = 31; i5 >= 0; i5--) {
			for (int j4 = 31; j4 >= 0; j4--)
				if (sprite2.myPixels[i5 + j4 * 32] == 0)
					if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;

		}

		if (highlightColor > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--)
					if (sprite2.myPixels[j5 + k4 * 32] == 0)
						if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = highlightColor;
						else if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = highlightColor;
						else if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = highlightColor;
						else if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = highlightColor;

			}

		} else if (highlightColor == 0) {
			for (int k5 = 31; k5 >= 0; k5--) {
				for (int l4 = 31; l4 >= 0; l4--)
					if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0
							&& sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
						sprite2.myPixels[k5 + l4 * 32] = 0x302020;

			}

		}
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (highlightColor == 0)
			mruNodes1.removeFromCache(sprite2, itemId);
		DrawingArea.initDrawingArea(j2, i2, ai1, new float[1024]);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Rasterizer.textureInt1 = k1;
		Rasterizer.textureInt2 = l1;
		Rasterizer.anIntArray1472 = ai;
		Rasterizer.aBoolean1464 = true;
		if (itemDef.stackable)
			sprite2.maxWidth = 33;
		else
			sprite2.maxWidth = 32;
		sprite2.maxHeight = itemAmount;
		return sprite2;
	}

	public Model method201(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];

			if (j != -1)
				return forID(j).method201(1);
		}
		Model model = (Model) mruNodes2.insertFromCache(id);
		if (model != null)
			return model;
		model = Model.method462(modelId);
		if (model == null)
			return null;
		if (anInt167 != 128 || anInt192 != 128 || anInt191 != 128)
			model.method478(anInt167, anInt191, anInt192);
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.method476(modifiedModelColors[l], originalModelColors[l]);

		}
		model.method479(64 + anInt196, 768 + anInt184, -50, -10, -50, true);
		model.aBoolean1659 = true;
		mruNodes2.removeFromCache(model, id);
		return model;
	}

	public Model method202(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];

			if (j != -1)
				return forID(j).method202(1);
		}
		Model model = Model.method462(modelId);
		if (model == null)
			return null;
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.method476(modifiedModelColors[l], originalModelColors[l]);

		}
		return model;
	}

	private ItemDefinition() {
		id = -1;
	}

	private byte aByte154;
	public int value;
	public int[] originalModelColors;
	public int[] modifiedModelColors;

	private short[] originalTextureColors;
	private short[] modifiedTextureColors;

	public int id;
	public static MRUNodes mruNodes1 = new MRUNodes(100);
	public static MRUNodes mruNodes2 = new MRUNodes(50);

	public boolean membersObject;
	private int anInt162;
	public int certTemplateID;
	private int anInt164;
	public int maleModel;
	private int anInt166;
	private int anInt167;
	public String groundOptions[];
	public int modelOffset1;
	public String name;
	private static ItemDefinition[] cache;
	private int anInt173;
	public int modelId;
	private int anInt175;
	public boolean stackable;
	public String description;
	public int certID;
	private static int cacheIndex;
	public int modelZoom;
	private static Stream stream;
	private int anInt184;
	private int anInt185;
	private int anInt188;
	public String inventoryOptions[];
	public String equipActions[];
	public int modelRotation1;
	private int anInt191;
	private int anInt192;
	private int[] stackIDs;
	public int modelOffset2;
	private static int[] streamIndices;
	private int anInt196;
	private int anInt197;
	public int modelRotation2;
	public int femaleModel;
	private int[] stackAmounts;
	public int team;
	public static int totalItems;
	private int anInt204;
	private byte aByte205;
	public boolean searchableItem;
	private static BufferedWriter writer;

}
