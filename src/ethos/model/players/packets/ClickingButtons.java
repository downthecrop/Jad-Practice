package ethos.model.players.packets;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import ethos.model.players.*;
import org.apache.commons.lang3.text.WordUtils;

import ethos.Config;
import ethos.Server;
import ethos.model.content.PlayerEmotes;
import ethos.model.content.help.HelpDatabase;
import ethos.model.content.staff.StaffControl;
import ethos.model.content.teleportation.Teleports;
import ethos.model.content.tradingpost.Listing;
import ethos.model.content.wogw.Wogw;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.model.items.bank.BankItem;
import ethos.model.items.bank.BankTab;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.players.combat.Special;
import ethos.model.players.combat.Specials;
import ethos.model.players.combat.magic.LunarSpells;
import ethos.model.players.combat.magic.MagicData;
import ethos.model.players.combat.magic.NonCombatSpells;
import ethos.model.players.combat.melee.QuickPrayers;
import ethos.model.players.packets.dialogueoptions.FiveOptions;
import ethos.model.players.packets.dialogueoptions.FourOptions;
import ethos.model.players.packets.dialogueoptions.ThreeOptions;
import ethos.model.players.packets.dialogueoptions.TwoOptions;
import ethos.model.players.skills.Cooking;
import ethos.model.players.skills.Smelting;
import ethos.model.players.skills.crafting.BattlestaveMaking;
import ethos.model.players.skills.crafting.BraceletMaking;
import ethos.model.players.skills.crafting.GlassBlowing;
import ethos.model.players.skills.crafting.LeatherMaking;
import ethos.model.players.skills.crafting.Tanning;
import ethos.model.players.skills.crafting.CraftingData.tanningData;
import ethos.model.shops.ShopAssistant;
import ethos.util.Misc;

/**
 * Clicking most buttons
 *
 */
public class ClickingButtons implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0, packetSize);
		if (c.debugMessage) {
			c.sendMessage("actionbutton: " + actionButtonId + ", DialogueID: " + c.dialogueAction);
		}
		if (c.isDead || c.getHealth().getAmount() <= 0) {
			return;
		}
		if (c.isIdle) {
			if (c.debugMessage)
				c.sendMessage("You are no longer in idle mode.");
			c.isIdle = false;
		}
		if (c.getLootingBag().handleButton(actionButtonId)) {
			return;
		}
		if (c.getPrestige().prestigeClicking(c, actionButtonId)) {
			return;
		}
		if (c.getExpLock().ExpLockClicking(c, actionButtonId)) {
			return;
		}
		if (c.getRunePouch().handleButton(actionButtonId)) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.getInterfaceEvent().clickButton(actionButtonId);
			return;
		}
		if (!c.getTutorial().isActive() && c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (c.getSlayer().onActionButton(actionButtonId)) {
			return;
		}
		if (c.getTutorial().isActive()) {
			if (actionButtonId == 165179) {
				c.getTutorial().getIronmanInterface().confirm();
			}
			c.getTutorial().getIronmanInterface().click(actionButtonId);
			return;
		}

		// (TeleportationInterface.actions(c, actionButtonId)) {
		// return;
		// }
		Listing.postButtons(c, actionButtonId);
		Teleports.teleportButtons(c, actionButtonId);
		c.getTeleport().handleTeleports(c, actionButtonId);
		if (StaffControl.isUsingControl) {
			StaffControl.clickActions(c, actionButtonId);
		}

		/** Achievement Buttons **/
		if (actionButtonId >= 140197 && actionButtonId <= 140255) {
			c.getAchievements().viewAchievement(actionButtonId, c.getAchievements().currentInterface);
			return;
		}

		/** Drop Manager Buttons **/
		if (actionButtonId >= 128240 && actionButtonId <= 129113) {
			Server.getDropManager().select(c, actionButtonId);
			return;
		}

		/*
		 * if (actionButtonId >= 175205 && actionButtonId <= 176149) { int id = 175204;
		 * if (!StaffControl.isUsingControl) { StaffControl.loadOnPlayerOptions(c);
		 * StaffControl.username = p.playerName;
		 * c.getPA().sendFrame126("<col=0xFF981F>Player: " + p.playerName, 45254); }
		 * c.setSidebarInterface(2, 45000); }
		 */
		if (actionButtonId >= 232182 && actionButtonId <= 233022) {
			HelpDatabase.getDatabase().view(c, actionButtonId);
			HelpDatabase.getDatabase().delete(c, actionButtonId);
			return;
		}
		// if (BattlestaveMaking.handleActions(c, actionButtonId)) {
		// return;
		// }
		/*
		 * if (actionButtonId >= 166035 && actionButtonId < 166035 +
		 * DropManager.AMOUNT_OF_TABLES) { Server.getDropManager().select(c,
		 * actionButtonId); return; }
		 */
		if (actionButtonId == 15040) {
			c.getDH().sendDialogues(68, -1);
			return;
		}
		if (actionButtonId == 15041) {
			Server.getDropManager().open2(c);
			return;
		}
		if (actionButtonId == 166023) {
			c.getPA().removeAllWindows();
			return;
		}
		c.getPestControlRewards().click(actionButtonId);
		if (c.getTitles().click(actionButtonId)) {
			return;
		}
		if (c.battlestaffDialogue) {
			BattlestaveMaking.craftBattlestave(c, actionButtonId);
		}
		if (c.craftDialogue) {
			LeatherMaking.craftLeather(c, actionButtonId);
		}
		if (c.braceletDialogue) {
			BraceletMaking.craftBracelet(c, actionButtonId);
		}
		for (tanningData t : tanningData.values()) {
			if (actionButtonId == t.getButtonId(actionButtonId)) {
				Tanning.tanHide(c, actionButtonId);
			}
		}
		if (c.getPresets().clickButton(actionButtonId)) {
			return;
		}
		int[] spellIds = { 4128, 4130, 4132, 4134, 4136, 4139, 4142, 4145, 4148, 4151, 4153, 4157, 4159, 4161, 4164,
				4165, 4129, 4133, 4137, 6006, 6007, 6026, 6036, 6046, 6056, 4147, 6003, 47005, 4166, 4167, 4168, 48157,
				50193, 50187, 50101, 50061, 50163, 50211, 50119, 50081, 50151, 50199, 50111, 50071, 50175, 50223, 50129,
				50091 };
		for (int i = 0; i < spellIds.length; i++) {
			if (actionButtonId == spellIds[i]) {
				c.autocasting = true;
				c.autocastId = i;
			}
		}
		if (Server.getHolidayController().clickButton(c, actionButtonId)) {
			return;
		}
		if (c.getPunishmentPanel().clickButton(actionButtonId)) {
			return;
		}
		DuelSession duelSession = null;
		c.getFletching().select(actionButtonId);
		GlassBlowing.glassBlowing(c, actionButtonId);
		PlayerEmotes.performEmote(c, actionButtonId);
		// int[] teleportButtons = { 4140, 4143, 4146, 4150, 6004, 6005, 29031,
		// 50235, 50245, 50253, 51005, 51013, 51023, 51031, 51039,
		// 117112, 117131, 117154, 117186, 117210, 118018, 118042, 118058 };
		// if (IntStream.of(teleportButtons).anyMatch(id -> actionButtonId == id)) {
		// CityTeleports.teleport(c, actionButtonId);
		// }
		QuickPrayers.clickButton(c, actionButtonId);
		LunarSpells.lunarButton(c, actionButtonId);
		switch (actionButtonId) {

		case 19236:
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(54000);
			c.getOutStream().writeWord(5065);
			c.flushOutStream();
			break;

		case 4140:
			if (c.teleTimer > 0) {
				return;
			}
			if (c.playerLevel[6] > 24) {
				if (c.getItems().playerHasItem(554, 1) && c.getItems().playerHasItem(556, 3)
						&& c.getItems().playerHasItem(563, 1)) {
					c.getPA().startTeleport(Config.VARROCK_X, Config.VARROCK_Y, 0, "modern", false);
					c.getItems().deleteItem(554, c.getItems().getItemSlot(554), 1);
					c.getItems().deleteItem(556, c.getItems().getItemSlot(556), 3);
					c.getItems().deleteItem(563, c.getItems().getItemSlot(563), 1);
				} else {
					c.sendMessage("You do not have the required runes to cast this spell.");
				}
			} else {
				c.sendMessage("You do not have the required magic level to cast this spell.");
			}
			break;

		case 4143:
			if (c.teleTimer > 0) {
				return;
			}
			if (c.playerLevel[6] > 30) {
				if (c.getItems().playerHasItem(557, 1) && c.getItems().playerHasItem(556, 3)
						&& c.getItems().playerHasItem(563, 1)) {
					c.getPA().startTeleport(Config.LUMBY_X, Config.LUMBY_Y, 0, "modern", false);
					c.getItems().deleteItem(557, c.getItems().getItemSlot(557), 1);
					c.getItems().deleteItem(556, c.getItems().getItemSlot(556), 3);
					c.getItems().deleteItem(563, c.getItems().getItemSlot(563), 1);
				} else {
					c.sendMessage("You do not have the required runes to cast this spell.");
				}
			} else {
				c.sendMessage("You do not have the required magic level to cast this spell.");
			}
			break;
			case 183156:
				switch(c.boxCurrentlyUsing) {
					case 13346: //ultra rare
						c.getUltraMysteryBox().spin();
						break;
				}
				break;
		case 4146:
			if (c.teleTimer > 0) {
				return;
			}
			if (c.playerLevel[6] > 36) {
				if (c.getItems().playerHasItem(555, 1) && c.getItems().playerHasItem(556, 3)
						&& c.getItems().playerHasItem(563, 1)) {
					c.getPA().startTeleport(Config.FALADOR_X, Config.FALADOR_Y, 0, "modern", false);
					c.getItems().deleteItem(555, c.getItems().getItemSlot(555), 1);
					c.getItems().deleteItem(556, c.getItems().getItemSlot(556), 3);
					c.getItems().deleteItem(563, c.getItems().getItemSlot(563), 1);
				} else {
					c.sendMessage("You do not have the required runes to cast this spell.");
				}
			} else {
				c.sendMessage("You do not have the required magic level to cast this spell.");
			}
			break;

		case 4150:
			if (c.teleTimer > 0) {
				return;
			}
			if (c.playerLevel[6] > 44) {
				if (c.getItems().playerHasItem(556, 5) && c.getItems().playerHasItem(563, 1)) {
					c.getPA().startTeleport(Config.CAMELOT_X, Config.CAMELOT_Y, 0, "modern", false);
					c.getItems().deleteItem(556, c.getItems().getItemSlot(556), 5);
					c.getItems().deleteItem(563, c.getItems().getItemSlot(563), 1);
				} else {
					c.sendMessage("You do not have the required runes to cast this spell.");
				}
			} else {
				c.sendMessage("You do not have the required magic level to cast this spell.");
			}
			break;

		case 6004:
			if (c.teleTimer > 0) {
				return;
			}
			if (c.playerLevel[6] > 50) {
				if (c.getItems().playerHasItem(555, 2) && c.getItems().playerHasItem(563, 2)) {
					c.getPA().startTeleport(Config.ARDOUGNE_X, Config.ARDOUGNE_Y, 0, "modern", false);
					c.getItems().deleteItem(555, c.getItems().getItemSlot(555), 2);
					c.getItems().deleteItem(563, c.getItems().getItemSlot(563), 2);
				} else {
					c.sendMessage("You do not have the required runes to cast this spell.");
				}
			} else {
				c.sendMessage("You do not have the required magic level to cast this spell.");
			}
			break;

		case 6005:
			if (c.teleTimer > 0) {
				return;
			}
			if (c.playerLevel[6] > 57) {
				if (c.getItems().playerHasItem(557, 2) && c.getItems().playerHasItem(563, 2)) {
					c.getPA().startTeleport(Config.WATCHTOWER_X, Config.WATCHTOWER_Y, 0, "modern", false);
					c.getItems().deleteItem(557, c.getItems().getItemSlot(557), 2);
					c.getItems().deleteItem(563, c.getItems().getItemSlot(563), 2);
				} else {
					c.sendMessage("You do not have the required runes to cast this spell.");
				}
			} else {
				c.sendMessage("You do not have the required magic level to cast this spell.");
			}
			break;

		case 113238:
			if (Config.BONUS_XP_WOGW == true) {
				c.sendMessage("@cr10@ <col=6666FF>Wogw is granting double experience for another "
						+ TimeUnit.MILLISECONDS.toMinutes(Wogw.EXPERIENCE_TIMER * 600) + " minutes.");
			} else if (Config.BONUS_PC_WOGW == true) {
				c.sendMessage("@cr10@ <col=6666FF>Wogw is granting +5 bonus pc points for another "
						+ TimeUnit.MILLISECONDS.toMinutes(Wogw.PC_POINTS_TIMER * 600) + " minutes.");
			} else if (Config.DOUBLE_DROPS == true) {
				c.sendMessage("@cr10@ <col=6666FF>Wogw is granting double drop rate for another "
						+ TimeUnit.MILLISECONDS.toMinutes(Wogw.DOUBLE_DROPS_TIMER * 600) + " minutes.");
			} else {
				c.sendMessage("@cr10@ <col=6666FF>Wogw is currently inactive.");
			}
			break;

		/*
		 * case 166056: c.getPA().showInterface(53000); break;
		 */

		case 90077:
			c.getPA().showInterface(37700);
			break;

		case 113234:// Players online
			c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
			break;

		case 226158:
			c.placeHolders = !c.placeHolders;
			c.getPA().sendChangeSprite(58014, c.placeHolders ? (byte) 1 : (byte) 0);
			break;

		// Close interface for drop checker
		case 152109:
		case 128234:
			c.getPA().removeAllWindows();
			break;

		case 113249:
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			// c.getDH().sendDialogues(12000, -1);
			for (int i = 8144; i < 8195; i++) {
				c.getPA().sendFrame126("", i);
			}
			int[] frames = { 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163,
					8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175, 8176, 8177, 8178, 8179,
					8180, 8181, 8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194 };
			c.getPA().sendFrame126("@dre@Kill Tracker for @blu@" + c.playerName + "", 8144);
			c.getPA().sendFrame126("", 8145);
			c.getPA().sendFrame126("@blu@Total kills@bla@ - " + c.getNpcDeathTracker().getTotal() + "", 8147);
			c.getPA().sendFrame126("", 8148);
			int frameIndex = 0;
			for (Entry<String, Integer> entry : c.getNpcDeathTracker().getTracker().entrySet()) {
				if (entry == null) {
					continue;
				}
				if (frameIndex > frames.length - 1) {
					break;
				}
				if (entry.getValue() > 0) {
					c.getPA().sendFrame126(
							"@blu@" + WordUtils.capitalize(entry.getKey().toLowerCase()) + ": @red@" + entry.getValue(),
							frames[frameIndex]);
					frameIndex++;
				}
			}
			c.getPA().showInterface(8134);
			// c.checkWellOfGoodwillTimers();
			break;

		case 148118: // Exp
			// if (Wogw.EXPERIENCE_TIMER > 0) {
			// c.sendMessage("@cr10@ <col=6666FF>Wogw is already granting +20% bonus
			// experience for another " +
			// TimeUnit.MILLISECONDS.toMinutes(Wogw.EXPERIENCE_TIMER * 600) + " minutes.");
			// return;
			// }
			c.getPA().sendChangeSprite(38006, (byte) 2);
			c.getPA().sendChangeSprite(38007, (byte) 1);
			c.getPA().sendChangeSprite(38008, (byte) 1);

			c.wogwOption = "experience";
			break;

		case 148119: // Pc points
			// if (Wogw.PC_POINTS_TIMER > 0) {
			// c.sendMessage("@cr10@ <col=6666FF>Wogw is already granting +5 bonus pc points
			// for another " + TimeUnit.MILLISECONDS.toMinutes(Wogw.PC_POINTS_TIMER * 600) +
			// " minutes.");
			// return;
			// }
			c.getPA().sendChangeSprite(38006, (byte) 1);
			c.getPA().sendChangeSprite(38007, (byte) 2);
			c.getPA().sendChangeSprite(38008, (byte) 1);

			c.wogwOption = "pc";
			break;

		case 242150:
			case 154078:
			case 183155:
			c.getPA().closeAllWindows();
			break;



		case 148120: // Drops
			// if (Wogw.DOUBLE_DROPS_TIMER > 0) {
			// c.sendMessage("@cr10@ <col=6666FF>Wogw is already granting double drops for
			// another " + TimeUnit.MILLISECONDS.toMinutes(Wogw.DOUBLE_DROPS_TIMER * 600) +
			// " minutes.");
			// return;
			// }
			c.getPA().sendChangeSprite(38006, (byte) 1);
			c.getPA().sendChangeSprite(38007, (byte) 1);
			c.getPA().sendChangeSprite(38008, (byte) 2);

			c.wogwOption = "drops";
			break;
			case 185151:
			case 166048:
				c.getTitles().display();
				break;

		case 148135: // Confirm donation
			Wogw.donate(c, (int) c.wogwDonationAmount);
			break;

		case 148137:
			c.getPA().sendFrame171(1, 38020);
			c.getPA().sendChangeSprite(38006, (byte) 1);
			c.getPA().sendChangeSprite(38007, (byte) 1);
			c.getPA().sendChangeSprite(38008, (byte) 1);
			c.sendMessage("You decided to end your donation to the well of goodwill.");
			break;

		case 136192:
			if (c.inSafeBox) {
				c.getPA().sendFrame171(0, 35010);
			}
			break;
		case 136199:
			// No
			if (c.inSafeBox) {
				c.getPA().sendFrame171(1, 35010);
			}
			break;
		case 136197:
			// Yes
			if (c.inSafeBox) {
				c.getPA().sendFrame171(1, 35010);
				if (c.safeBoxSlots == 50) {
					c.sendMessage("@cr10@You already have the max amount of slots.");
					return;
				}
				if (c.getItems().playerHasItem(13307, 15)) {
					c.safeBoxSlots++;
					c.getItems().deleteItem(13307, 15);
					c.sendMessage("@cr10@You successfully purchased 1 extra slot in the safe deposit box.");
					c.getPA().sendFrame126("" + (c.getSafeBox().items.size()) + "/" + c.safeBoxSlots, 35005);
				} else {
					c.sendMessage(
							"@cr10@You must have 15 blood money to purchase 1 extra slot in the safe deposit box.");
					return;
				}
			}
			break;

		case 136186:
			c.getSafeBox().closeLootbag();
			break;
			case 40164:
				c.setSidebarInterface(2, 29465); // 638
				break;
			case 115031:
				c.setSidebarInterface(2, 10220); // 638
				break;
		case 23132:
			c.setSidebarInterface(1, 3917); // Skilltab > 3917
			c.setSidebarInterface(2, 638); // 638
			c.setSidebarInterface(3, 3213);
			c.setSidebarInterface(4, 1644);
			c.setSidebarInterface(5, 5608);
			switch (c.playerMagicBook) {
			case 0:
				c.setSidebarInterface(6, 938); // modern
				break;

			case 1:
				c.setSidebarInterface(6, 838); // ancient
				break;

			case 2:
				c.setSidebarInterface(6, 29999); // ancient
				break;
			}
			c.setSidebarInterface(7, 18128);
			c.setSidebarInterface(8, 5065);
			c.setSidebarInterface(9, 5715);
			c.setSidebarInterface(10, 2449);
			c.setSidebarInterface(11, 23000); // wrench tab
			c.setSidebarInterface(12, 147); // run tab
			c.setSidebarInterface(0, 2423);
			// if (c.playerEquipment[c.playerRing] == 7927) {
			// c.getItems().deleteEquipment(c.playerEquipment[c.playerRing], c.playerRing);
			// c.getItems().addItem(7927,1);
			// }
			c.morphed = false;
			c.isNpc = false;
			c.updateRequired = true;
			c.appearanceUpdateRequired = true;
			break;

		case 19136:
			QuickPrayers.toggle(c);
			c.getPA().sendFrame36(197, 1);
			break;
		case 19137:
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				if (c.prayerActive[p]) {
					c.sendMessage("You need to deactivate your active prayers before doing this.");
					return;
				}
			}
			c.isSelectingQuickprayers = true;
			c.setSidebarInterface(5, 17200);
			break;

		case 114093:
			c.setSidebarInterface(2, 29265); // 29265
			break;
		case 175202:
			StaffControl.emptyList(c);
			StaffControl.isUsingControl = false;
			int line = 45005;
			c.getPA().sendFrame126("Online: " + PlayerHandler.getPlayerCount(), 45001);
			c.getPA().sendFrame126("<col=0xFF981F>Players", 45254);
			for (Player p : PlayerHandler.players) {
				if (p == null)
					continue;
				c.getPA().sendFrame126(
						"@or2@" + p.playerName + "  -  " + p.getHealth().getAmount() + "/" + p.getHealth().getMaximum(),
						line);
				line++;
			}
			c.setSidebarInterface(2, 45000);
			break;
		case 114083:
			c.setSidebarInterface(2, 638);
			break;

		/**
		 * Dialogue Handling
		 */

		case 9157:
			TwoOptions.handleOption1(c);
			break;

		case 9158:
			TwoOptions.handleOption2(c);
			break;

		case 9178:
			FourOptions.handleOption1(c);
			break;

		case 9179:
			FourOptions.handleOption2(c);
			break;

		case 9180:
			FourOptions.handleOption3(c);
			break;

		case 9181:
			FourOptions.handleOption4(c);
			break;

		case 9190:
			FiveOptions.handleOption1(c);
			break;

		case 9191:
			FiveOptions.handleOption2(c);
			break;

		case 9192:
			FiveOptions.handleOption3(c);
			break;

		case 9193:
			FiveOptions.handleOption4(c);
			break;

		case 9194:
			FiveOptions.handleOption5(c);
			break;

		case 255255:
			c.sendMessage("You reset your experience counter.");
			c.setExperienceCounter(0L);
			break;

		case 135114:
		case 92122:
		case 118026:
			if (c.inClanWars() || c.inClanWarsSafe()) {
				c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
				return;
			}
			c.getBH().teleportToTarget();
			break;

		case 4135:
		case 62005:
			NonCombatSpells.attemptDate(c, actionButtonId);
			break;

		case 55095:// This is the button id
			if (c.droppingItem) {
				// c.getPA().destroyItem(c.droppedItem);
				if (c.getItems().playerHasItem(c.droppedItem) && c.droppedItem != -1 && !c.isDead) {
					Server.itemHandler.createGroundItem(c, c.droppedItem, c.absX, c.absY, c.heightLevel,
							c.playerItemsN[c.getItems().getItemSlot(c.droppedItem)], c.getIndex());
					c.getItems().deleteItem(c.droppedItem, c.getItems().getItemSlot(c.droppedItem),
							c.playerItemsN[c.getItems().getItemSlot(c.droppedItem)]);
					// c.getPA().destroyItem(itemId);
					c.getPA().removeAllWindows();
				} else {
					return;
				}
				c.droppedItem = -1;
				c.droppingItem = false;
			} else {
				c.usingMagic = true;
				c.getPA().removeAllWindows();
				// if (!c.getItems().playerHasItem(554, 5) || !c.getItems().playerHasItem(561,
				// 1)) {
				// c.sendMessage("You do not have the required runes to do this.");
				// return;
				// }
				c.getPA().alchemy(c.droppedItem, "high");
			}
			break;
		case 55096:// This is the button id
			c.getPA().removeAllWindows();// Choosing No will remove all the
											// windows
			c.droppedItem = -1;
			c.droppingItem = false;
			break;

		/*
		 * case 191109: c.getAchievements().currentInterface = 0;
		 * c.getAchievements().drawInterface(0); break;
		 * 
		 * case 191110: c.getAchievements().currentInterface = 1;
		 * c.getAchievements().drawInterface(1); break;
		 * 
		 * case 191111: c.getAchievements().currentInterface = 2;
		 * c.getAchievements().drawInterface(2); break;
		 */

		case 250002:
		case 140244:
		case 141088:
		case 148122:
			c.getPA().closeAllWindows();
			break;
		case 24150:
			c.getPA().closeAllWindows();
			break;

		case 20174:
			c.getPA().closeAllWindows();
			ethos.model.items.bank.BankPin pin = c.getBankPin();
			if (pin.getPin().length() <= 0)
				c.getBankPin().open(1);
			else if (!pin.getPin().isEmpty() && !pin.isAppendingCancellation())
				c.getBankPin().open(3);
			else if (!pin.getPin().isEmpty() && pin.isAppendingCancellation())
				c.getBankPin().open(4);
			break;

		case 226162:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking)
				return;
			if (System.currentTimeMillis() - c.lastBankDeposit < 500)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			c.lastBankDeposit = System.currentTimeMillis();
			for (int slot = 0; slot < c.playerItems.length; slot++) {
				if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
					c.getItems().addToBank(c.playerItems[slot] - 1, c.playerItemsN[slot], false);
				}
			}
			c.getItems().updateInventory();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			break;

		case 226170:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking)
				return;
			if (System.currentTimeMillis() - c.lastBankDeposit < 250)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			c.lastBankDeposit = System.currentTimeMillis();
			for (int slot = 0; slot < c.playerEquipment.length; slot++) {
				if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
					if (c.getItems().addEquipmentToBank(c.playerEquipment[slot], slot, c.playerEquipmentN[slot],
							false)) {
						c.getItems().wearItem(-1, 0, slot);
					} else {
						c.sendMessage("Your bank is full.");
						break;
					}
				}
			}
			c.getItems().updateInventory();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			break;

		case 226186:
		case 226198:
		case 226209:
		case 226220:
		case 226231:
		case 226242:
		case 226253:
		case 227008:
		case 227019:
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			int tabId = actionButtonId == 226186 ? 0
					: actionButtonId == 226198 ? 1
							: actionButtonId == 226209 ? 2
									: actionButtonId == 226220 ? 3
											: actionButtonId == 226231 ? 4
													: actionButtonId == 226242 ? 5
															: actionButtonId == 226253 ? 6
																	: actionButtonId == 227008 ? 7
																			: actionButtonId == 227019 ? 8 : -1;
			if (tabId <= -1 || tabId > 8)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset(tabId);
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			BankTab tab = c.getBank().getBankTab(tabId);
			if (tab.getTabId() == c.getBank().getCurrentBankTab().getTabId())
				return;
			if (tab.size() <= 0 && tab.getTabId() != 0) {
				c.sendMessage("Drag an item into the new tab slot to create a tab.");
				return;
			}
			c.getBank().setCurrentBankTab(tab);
			c.getPA().openUpBank();
			break;

		case 226197:
		case 226208:
		case 226219:
		case 226230:
		case 226241:
		case 226252:
		case 227007:
		case 227018:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			tabId = actionButtonId == 226197 ? 1
					: actionButtonId == 226208 ? 2
							: actionButtonId == 226219 ? 3
									: actionButtonId == 226230 ? 4
											: actionButtonId == 226241 ? 5
													: actionButtonId == 226252 ? 6
															: actionButtonId == 227007 ? 7
																	: actionButtonId == 227018 ? 8 : -1;
			tab = c.getBank().getBankTab(tabId);
			if (tab == null || tab.getTabId() == 0 || tab.size() == 0) {
				c.sendMessage("You cannot collapse this tab.");
				return;
			}
			if (tab.size() + c.getBank().getBankTab()[0].size() >= Config.BANK_SIZE) {
				c.sendMessage("You cannot collapse this tab. The contents of this tab and your");
				c.sendMessage("main tab are greater than " + Config.BANK_SIZE + " unique items.");
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			for (BankItem item : tab.getItems()) {
				c.getBank().getBankTab()[0].add(item);
			}
			tab.getItems().clear();
			if (tab.size() == 0) {
				c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
			}
			c.getPA().openUpBank();
			break;

		case 226185:
		case 226196:
		case 226207:
		case 226218:
		case 226229:
		case 226240:
		case 226251:
		case 227006:
		case 227017:
			if (c.getPA().viewingOtherBank) {
				c.getPA().resetOtherBank();
				return;
			}
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBankPin().requiresUnlock()) {
				c.isBanking = false;
				c.getBankPin().open(2);
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			tabId = actionButtonId == 226185 ? 0
					: actionButtonId == 226196 ? 1
							: actionButtonId == 226207 ? 2
									: actionButtonId == 226218 ? 3
											: actionButtonId == 226229 ? 4
													: actionButtonId == 226240 ? 5
															: actionButtonId == 226251 ? 6
																	: actionButtonId == 227006 ? 7
																			: actionButtonId == 227017 ? 8 : -1;
			tab = c.getBank().getBankTab(tabId);
			long value = 0;
			if (tab == null || tab.size() == 0)
				return;
			for (BankItem item : tab.getItems()) {
				long tempValue = item.getId() - 1 == 995 ? 1 : ShopAssistant.getItemShopValue(item.getId() - 1);
				value += tempValue * item.getAmount();
			}
			c.sendMessage("<col=255>The total networth of tab " + tab.getTabId() + " is </col><col=600000>"
					+ Long.toString(value) + " gp</col>.");
			break;

		case 22024:
		case 86008:
			c.getPA().openUpBank();
			break;

		case 226154:
			c.takeAsNote = !c.takeAsNote;
			break;

		case 10252:
			c.antiqueSelect = 0;
			c.sendMessage("You select Attack");
			break;

		/** Start Achievement Interface - Grant **/
		// Opening Interface
		case 113230:
			if (c.inWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)
					|| c.underAttackBy > 0) {
				c.sendMessage("Please finish what you are doing before viewing your achievements.");
				return;
			}
			c.getAchievements().currentInterface = 0;
			c.getAchievements().drawInterface(0);
			break;

		case 179182:
			c.getAchievements().currentInterface = 0;
			c.getAchievements().drawInterface(0);
			break;

		case 179183:
			c.getAchievements().currentInterface = 1;
			c.getAchievements().drawInterface(1);
			break;

		case 179184:
			c.getAchievements().currentInterface = 2;
			c.getAchievements().drawInterface(2);
			break;

		// Closing
		case 140162:
			c.getPA().removeAllWindows();
			break;
		/** End Achievement Interface - Grant **/
		// case 113248: //Spawntab
		// c.getPA().sendFrame171(0, 36200);
		// c.setSidebarInterface(2, 36200); //638
		// break;
		// case 141112:
		// c.setSidebarInterface(2, 638); //638
		// c.getPA().sendFrame171(1, 36200);
		// c.getPA().sendFrame126("Name", 36202);
		// c.getPA().sendFrame126("Amount", 36205);
		// break;

		case 164034:
		case 164035:
		case 164036:
		case 164037:
			int index = actionButtonId - 164034;
			String[] removed = c.getSlayer().getRemoved();
			if (index < 0 || index > removed.length - 1) {
				return;
			}
			if (removed[index].isEmpty()) {
				c.sendMessage("There is no task in this slow that is being blocked.");
				return;
			}
			removed[index] = "";
			c.getSlayer().setRemoved(removed);
			c.getSlayer().updateCurrentlyRemoved();
			break;

		case 164028:
			c.getSlayer().cancelTask();
			break;
		case 164029:
			c.getSlayer().removeTask();
			break;

		case 160045:
		case 162033:
		case 164021:
			if (c.interfaceId != 41000)
				c.getSlayer().handleInterface("buy");
			break;

		case 160047:
		case 162035:
		case 164023:
			if (c.interfaceId != 41500)
				c.getSlayer().handleInterface("learn");
			break;

		case 160049:
		case 162037:
		case 164025:
			if (c.interfaceId != 42000)
				c.getSlayer().handleInterface("assignment");
			break;

		case 162030:
		case 164018:
		case 160042:
			c.getPA().removeAllWindows();
			break;
		case 251246:
			c.getPA().removeAllWindows();
			for (int i = 0; i < 12; i++) {
				c.getPA().itemOnInterface(-1, -1, 64503, i);
			}
			break;
		case 113236: // double drop
			break;
		case 113237: // double pkp
			// c.forcedChat("My Hunter killstreak is: " +
			// c.getKillstreak().getAmount(Killstreak.Type.HUNTER) + " and my Rogue
			// killstreak is: "
			// + c.getKillstreak().getAmount(Killstreak.Type.ROGUE) + " ");
			break;
		case 113235: // double xp
			break;
		// case 113238:
		/*
		 * if (Server.getMultiplayerSessionListener().inAnySession(c)) { return; } //
		 * c.getDH().sendDialogues(12000, -1); for (int i = 8144; i < 8195; i++) {
		 * c.getPA().sendFrame126("", i); } int[] frames = { 8149, 8150, 8151, 8152,
		 * 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163, 8164, 8165,
		 * 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175, 8176, 8177, 8178,
		 * 8179, 8180, 8181, 8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191,
		 * 8192, 8193, 8194 }; c.getPA().sendFrame126("@dre@Kill Tracker for @blu@" +
		 * c.playerName + "", 8144); c.getPA().sendFrame126("", 8145);
		 * c.getPA().sendFrame126("@blu@Total kills@bla@ - " +
		 * c.getNpcDeathTracker().getTotal() + "", 8147); c.getPA().sendFrame126("",
		 * 8148); int frameIndex = 0; for (Entry<String, Integer> entry :
		 * c.getNpcDeathTracker().getTracker().entrySet()) { if (entry == null) {
		 * continue; } if (frameIndex > frames.length - 1) { break; } if
		 * (entry.getValue() > 0) { c.getPA().sendFrame126("@blu@" +
		 * WordUtils.capitalize(entry.getKey().toLowerCase()) + ": @red@" +
		 * entry.getValue(), frames[frameIndex]); frameIndex++; } }
		 * c.getPA().showInterface(8134); break;
		 */
		// break;

		case 114121:
			c.getDiaryManager().getVarrockDiary().display();
			break;
		case 114122:
			c.getDiaryManager().getArdougneDiary().display();
			break;
		case 114123:
			c.getDiaryManager().getDesertDiary().display();
			break;
		case 114124:
			c.getDiaryManager().getFaladorDiary().display();
			break;
		case 114125:
			c.getDiaryManager().getFremennikDiary().display();
			break;
		case 114126:
			c.getDiaryManager().getKandarinDiary().display();
			break;
		case 114127:
			c.getDiaryManager().getKaramjaDiary().display();
			break;
		case 114128:
			c.getDiaryManager().getLumbridgeDraynorDiary().display();
			break;
		case 114129:
			c.getDiaryManager().getMorytaniaDiary().display();
			break;
		case 114130:
			c.getDiaryManager().getWesternDiary().display();
			break;
		case 114134:
			c.getDiaryManager().getWildernessDiary().display();
			break;

		case 39241:
			long milliseconds = (long) c.playTime * 600;
			long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
			long hours = TimeUnit.MILLISECONDS.toHours(milliseconds - TimeUnit.DAYS.toMillis(days));
			String time = days + " days, " + hours + " hours.";
			c.forcedChat("I've played Ascend for a total of : " + time);
			break;
		case 113240:
			c.forcedChat("I currently have: " + c.pkp + " PK Points.");
			break;
		case 113241:
			c.forcedChat("I currently have: " + c.donatorPoints + " Donator Points.");
			break;
		case 113242:
			c.forcedChat("I currently have: " + c.votePoints + " Vote Points.");
			break;
		case 113243:
			c.forcedChat("I currently have: " + c.pcPoints + " PC Points.");
			break;
		case 185142:
				List<Player> staff = PlayerHandler.nonNullStream().filter(Objects::nonNull).filter(p -> p.getRights().isOrInherits(Right.HELPER)).collect(Collectors.toList());
				c.sendMessage("@red@You can also type ::help to report something.");
				if (staff.size() > 0) {
					PlayerHandler.sendMessage("@blu@[Help] " + WordUtils.capitalize(c.playerName) + "" + " needs help, PM or TELEPORT and help them.", staff);
					c.getPA().logStuck();

				} else {
					c.sendMessage("@red@You've activated the help command but there are no staff-members online.");
					c.sendMessage("@red@Please try contacting a staff on the forums and discord and they will respond ASAP.");
					c.sendMessage("@red@You can also type ::help to report something.");
				}
				break;
		case 113244:
			c.forcedChat("I currently have: " + c.getArenaPoints() + " Mage Arena Points.");
			break;
		case 113246:
			c.sendMessage("@blu@I currently have: " + c.getSlayer().getConsecutiveTasks() + " consecutive slayer tasks.");
			break;
		case 39247:
			c.forcedChat("I currently have: " + c.getSlayer().getPoints() + " Slayer Points.");
			c.sendMessage("@blu@I currently have: " + c.getSlayer().getConsecutiveTasks() + " consecutive slayer tasks.");
			break;
		case 39244:
			DecimalFormat df = new DecimalFormat("#.##");
			double ratio = ((double) c.killcount) / ((double) c.deathcount);

			c.sendMessage("@blu@You have "+c.killcount+ " kills and "+c.deathcount+" deaths.");
			break;
		case 10253:
			c.antiqueSelect = 2;
			c.sendMessage("You select Strength");
			break;
		case 10254:
			c.antiqueSelect = 4;
			c.sendMessage("You select Ranged");
			break;
		case 10255:
			c.antiqueSelect = 6;
			c.sendMessage("You select Magic");
			break;
		case 11000:
			c.antiqueSelect = 1;
			c.sendMessage("You select Defence");
			break;
		case 11001:
			c.antiqueSelect = 3;
			c.sendMessage("You select Hitpoints");
			break;
		case 11002:
			c.antiqueSelect = 5;
			c.sendMessage("You select Prayer");
			break;
		case 11003:
			c.antiqueSelect = 16;
			c.sendMessage("You select Agility");
			break;
		case 11004:
			c.antiqueSelect = 15;
			c.sendMessage("You select Herblore");
			break;
		case 11005:
			c.antiqueSelect = 17;
			c.sendMessage("You select Thieving");
			break;
		case 11006:
			c.sendMessage("Sorry, but you can not select Slayer.");
			break;
		case 11007:
			c.antiqueSelect = 20;
			c.sendMessage("You select Runecrafting");
			break;
		case 47002:
			c.sendMessage("Sorry, but you can not select Slayer.");
			break;
		case 54090:
			c.antiqueSelect = 19;
			c.sendMessage("You select Farming");
			break;
		case 11008:
			c.antiqueSelect = 14;
			c.sendMessage("You select Mining");
			break;
		case 11009:
			c.antiqueSelect = 13;
			c.sendMessage("You select Smithing");
			break;
		case 11010:
			c.antiqueSelect = 10;
			c.sendMessage("You select Fishing");
			break;
		case 11011:
			c.antiqueSelect = 7;
			c.sendMessage("You select Cooking");
			break;
		case 11012:
			c.antiqueSelect = 11;
			c.sendMessage("You select Firemaking");
			break;
		case 11013:
			c.antiqueSelect = 8;
			c.sendMessage("You select Woodcutting");
			break;
		case 11014:
			c.antiqueSelect = 9;
			c.sendMessage("You select Fletching");
			break;
		case 11015:
			if (c.usingLamp) {
				if (c.antiqueLamp && !c.normalLamp) {
					c.usingLamp = false;
					c.getPA().addSkillXP(13100000, c.antiqueSelect, true);
					c.getItems().deleteItem2(4447, 1);
					c.sendMessage("The lamp mysteriously vanishes...");
					c.getPA().closeAllWindows();
				}
				if (c.normalLamp && !c.antiqueLamp) {
					int EXP_AWARDED = 150000;
					
					if (Config.BETA_MODE) {
						EXP_AWARDED += EXP_AWARDED/2;
						int currentExp = c.playerXP[c.antiqueSelect];
						if (currentExp > EXP_AWARDED) {
							EXP_AWARDED += currentExp / 2;
							c.sendMessage("During beta, lamps give increased exp!");
						}
						
					}
					
					c.usingLamp = false;
					c.getPA().addSkillXP(EXP_AWARDED, c.antiqueSelect, true);
					c.getItems().deleteItem2(2528, 1);
					c.sendMessage("The lamp mysteriously vanishes...");
					c.sendMessage("...and you gain some experience!");
					c.getPA().closeAllWindows();
				}
			} else {
				c.sendMessage("You must rub a lamp to gain the experience.");
				return;
			}
			break;

		/*
		 * case 28172: if (c.expLock == false) { c.expLock = true; c.sendMessage(
		 * "Your experience is now locked. You will not gain experience.");
		 * c.getPA().sendFrame126( "@whi@EXP: @gre@LOCKED", 7340); } else { c.expLock =
		 * false; c.sendMessage(
		 * "Your experience is now unlocked. You will gain experience.");
		 * c.getPA().sendFrame126( "@whi@EXP: @gre@UNLOCKED", 7340); } break;
		 */
		case 28215:
			if (c.getSlayer().getTask().isPresent()) {
				c.sendMessage("You do not have a task, please talk with a slayer master!");
			} else {
				c.forcedText = "I must slay another " + c.getSlayer().getTaskAmount() + " "
						+ c.getSlayer().getTask().get().getPrimaryName() + ".";
				c.forcedChatUpdateRequired = true;
				c.updateRequired = true;
			}
			break;


			case 185148:
			c.getPA().showInterface(36000);
			c.getAchievements().drawInterface(0);
				break;
			case 185149:
				c.getPA().showInterface(39500);
				break;





















		/* End Quest */
		case 15147:// Bronze, 1
			Smelting.startSmelting(c, "bronze", "ONE", "FURNACE");
			break;
		case 15146:// Bronze, 5
			Smelting.startSmelting(c, "bronze", "FIVE", "FURNACE");
			break;
		case 10247:// Bronze, 10
			Smelting.startSmelting(c, "bronze", "TEN", "FURNACE");
			break;
		case 9110:// Bronze, 28
			Smelting.startSmelting(c, "bronze", "ALL", "FURNACE");
			break;
		case 15151:// Iron, 1
			Smelting.startSmelting(c, "iron", "ONE", "FURNACE");
			break;
		case 15150:// Iron, 5
			Smelting.startSmelting(c, "iron", "FIVE", "FURNACE");
			break;
		case 15149:// Iron, 10
			Smelting.startSmelting(c, "iron", "TEN", "FURNACE");
			break;
		case 15148:// Iron, 28
			Smelting.startSmelting(c, "iron", "ALL", "FURNACE");
			break;
		case 15155:// silver, 1
			Smelting.startSmelting(c, "silver", "ONE", "FURNACE");
			break;
		case 15154:// silver, 5
			Smelting.startSmelting(c, "silver", "FIVE", "FURNACE");
			break;
		case 15153:// silver, 10
			Smelting.startSmelting(c, "silver", "TEN", "FURNACE");
			break;
		case 15152:// silver, 28
			Smelting.startSmelting(c, "silver", "ALL", "FURNACE");
			break;
		case 15159:// steel, 1
			Smelting.startSmelting(c, "steel", "ONE", "FURNACE");
			break;
		case 15158:// steel, 5
			Smelting.startSmelting(c, "steel", "FIVE", "FURNACE");
			break;
		case 15157:// steel, 10
			Smelting.startSmelting(c, "steel", "TEN", "FURNACE");
			break;
		case 15156:// steel, 28
			Smelting.startSmelting(c, "steel", "ALL", "FURNACE");
			break;
		case 15163:// gold, 1
			Smelting.startSmelting(c, "gold", "ONE", "FURNACE");
			break;
		case 15162:// gold, 5
			Smelting.startSmelting(c, "gold", "FIVE", "FURNACE");
			break;
		case 15161:// gold, 10
			Smelting.startSmelting(c, "gold", "TEN", "FURNACE");
			break;
		case 15160:// gold, 28
			Smelting.startSmelting(c, "gold", "ALL", "FURNACE");
			break;
		case 29017:// mithril, 1
			Smelting.startSmelting(c, "mithril", "ONE", "FURNACE");
			break;
		case 29016:// mithril, 5
			Smelting.startSmelting(c, "mithril", "FIVE", "FURNACE");
			break;
		case 24253:// mithril, 10
			Smelting.startSmelting(c, "mithril", "TEN", "FURNACE");
			break;
		case 16062:// mithril, 28
			Smelting.startSmelting(c, "mithril", "ALL", "FURNACE");
			break;
		case 29022:// addy, 1
			Smelting.startSmelting(c, "adamant", "ONE", "FURNACE");
			break;
		case 29021:// addy, 5
			Smelting.startSmelting(c, "adamant", "FIVE", "FURNACE");
			break;
		case 29019:// addy, 10
			Smelting.startSmelting(c, "adamant", "TEN", "FURNACE");
			break;
		case 29018:// addy, 28
			Smelting.startSmelting(c, "adamant", "ALL", "FURNACE");
			break;
		case 29026:// rune, 1
			Smelting.startSmelting(c, "rune", "ONE", "FURNACE");
			break;
		case 29025:// rune, 5
			Smelting.startSmelting(c, "rune", "FIVE", "FURNACE");
			break;
		case 29024:// rune, 10
			Smelting.startSmelting(c, "rune", "TEN", "FURNACE");
			break;
		case 29023:// rune, 28
			Smelting.startSmelting(c, "rune", "ALL", "FURNACE");
			break;

		/*
		 * case 58025: case 58026: case 58027: case 58028: case 58029: case 58030: case
		 * 58031: case 58032: case 58033: case 58034:
		 * c.getBankPin().pinEnter(actionButtonId); break;
		 */

		case 53152:
			Cooking.getAmount(c, 1);
			break;
		case 53151:
			Cooking.getAmount(c, 5);
			break;
		case 53150:
			Cooking.getAmount(c, 10);
			break;
		case 53149:
			Cooking.getAmount(c, 28);
			break;
		case 33206:
			if (c.inClanWarsSafe()) {
				c.outStream.createFrame(27);
				c.attackSkill = true;
			} else {
				c.getSI().attackComplex(1);
				c.getSI().selected = 0;
			}
			break;
		case 33209:
			if (c.inClanWarsSafe()) {
				c.outStream.createFrame(27);
				c.strengthSkill = true;
			} else {
				c.getSI().strengthComplex(1);
				c.getSI().selected = 1;
			}
			break;
		case 33212:
			if (c.inClanWarsSafe()) {
				c.outStream.createFrame(27);
				c.defenceSkill = true;
			} else {
				c.getSI().defenceComplex(1);
				c.getSI().selected = 2;
			}
			break;
		case 33215:
			if (c.inClanWarsSafe()) {
				c.outStream.createFrame(27);
				c.rangeSkill = true;
			} else {
				c.getSI().rangedComplex(1);
				c.getSI().selected = 3;
			}
			break;
		case 33218:
			if (c.inClanWarsSafe()) {
				c.outStream.createFrame(27);
				c.prayerSkill = true;
			} else {
				c.getSI().prayerComplex(1);
				c.getSI().selected = 4;
			}
			break;
		case 33221:
			if (c.inClanWarsSafe()) {
				c.outStream.createFrame(27);
				c.mageSkill = true;
			} else {
				c.getSI().magicComplex(1);
				c.getSI().selected = 5;
			}
			break;
		case 33207:
			if (c.inClanWarsSafe()) {
				c.outStream.createFrame(27);
				c.healthSkill = true;
			} else {
				c.getSI().hitpointsComplex(1);
				c.getSI().selected = 7;
			}
			break;
		case 33224: // runecrafting
			c.getSI().runecraftingComplex(1);
			c.getSI().selected = 6;
			break;
		case 33210: // agility
			c.getSI().agilityComplex(1);
			c.getSI().selected = 8;
			break;
		case 33213: // herblore
			c.getSI().herbloreComplex(1);
			c.getSI().selected = 9;
			break;
		case 33216: // theiving
			c.getSI().thievingComplex(1);
			c.getSI().selected = 10;
			break;
		case 33219: // crafting
			c.getSI().craftingComplex(1);
			c.getSI().selected = 11;
			break;
		case 33222: // fletching
			c.getSI().fletchingComplex(1);
			c.getSI().selected = 12;
			break;
		case 47130:// slayer
			c.getSI().slayerComplex(1);
			c.getSI().selected = 13;
			break;
		case 33208:// mining
			c.getSI().miningComplex(1);
			c.getSI().selected = 14;
			break;
		case 33211: // smithing
			c.getSI().smithingComplex(1);
			c.getSI().selected = 15;
			break;
		case 33214: // fishing
			c.getSI().fishingComplex(1);
			c.getSI().selected = 16;
			break;
		case 33217: // cooking
			c.getSI().cookingComplex(1);
			c.getSI().selected = 17;
			break;
		case 33220: // firemaking
			c.getSI().firemakingComplex(1);
			c.getSI().selected = 18;
			break;
		case 33223: // woodcut
			c.getSI().woodcuttingComplex(1);
			c.getSI().selected = 19;
			break;
		case 54104: // farming
			c.getSI().farmingComplex(1);
			c.getSI().selected = 20;
			break;
		case 73141: // farming
			c.getSI().hunterComplex(1);
			c.getSI().selected = 21;
			break;

		case 34142: // tab 1
			c.getSI().menuCompilation(1);
			break;

		case 34119: // tab 2
			c.getSI().menuCompilation(2);
			break;

		case 34120: // tab 3
			c.getSI().menuCompilation(3);
			break;

		case 34123: // tab 4
			c.getSI().menuCompilation(4);
			break;

		case 34133: // tab 5
			c.getSI().menuCompilation(5);
			break;

		case 34136: // tab 6
			c.getSI().menuCompilation(6);
			break;

		case 34139: // tab 7
			c.getSI().menuCompilation(7);
			break;

		case 34155: // tab 8
			c.getSI().menuCompilation(8);
			break;

		case 34158: // tab 9
			c.getSI().menuCompilation(9);
			break;

		case 34161: // tab 10
			c.getSI().menuCompilation(10);
			break;

		case 59199: // tab 11
			c.getSI().menuCompilation(11);
			break;

		case 59202: // tab 12
			c.getSI().menuCompilation(12);
			break;
		case 59203: // tab 13
			c.getSI().menuCompilation(13);
			break;
		// case 73113: // tab 13
		// c.getSI().menuCompilation(21);
		// break;

		case 89061:
		case 94051:
			if (c.autoRet == 0) {
				c.autoRet = 1;
			} else {
				c.autoRet = 0;
			}
			break;
			case 40163:
			c.updateQuestTab();
			break;
		// case 58253:
		case 108005:
			/*
			 * if (Server.getMultiplayerSessionListener().inAnySession(c)) { return; }
			 * c.getPA().showInterface(15106); // c.getItems().writeBonus();
			 */
			c.getPA().showInterface(15106);
			break;
		case 108006: // items kept on death
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			c.getPA().sendFrame126("Items Kept on Death", 17103);
			c.StartBestItemScan(c);
			c.EquipStatus = 0;
			for (int k = 0; k < 4; k++) {
				c.getPA().sendFrame34a(10494, -1, k, 1);
			}
			for (int k = 0; k < 39; k++) {
				c.getPA().sendFrame34a(10600, -1, k, 1);
			}
			if (c.WillKeepItem1 > 0) {
				c.getPA().sendFrame34a(10494, c.WillKeepItem1, 0, c.WillKeepAmt1);
			}
			if (c.WillKeepItem2 > 0) {
				c.getPA().sendFrame34a(10494, c.WillKeepItem2, 1, c.WillKeepAmt2);
			}
			if (c.WillKeepItem3 > 0) {
				c.getPA().sendFrame34a(10494, c.WillKeepItem3, 2, c.WillKeepAmt3);
			}
			if (c.WillKeepItem4 > 0 && c.prayerActive[10]) {
				c.getPA().sendFrame34a(10494, c.WillKeepItem4, 3, 1);
			}
			for (int ITEM = 0; ITEM < 28; ITEM++) {
				if (c.playerItems[ITEM] - 1 > 0
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
						&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM]);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt1) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt1);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt2) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt2);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
						&& c.playerItemsN[ITEM] > c.WillKeepAmt3) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
							c.playerItemsN[ITEM] - c.WillKeepAmt3);
					c.EquipStatus += 1;
				} else if (c.playerItems[ITEM] - 1 > 0
						&& (c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)
						&& c.playerItemsN[ITEM] > 1) {
					c.getPA().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - 1);
					c.EquipStatus += 1;
				}
			}
			for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
				if (c.playerEquipment[EQUIP] > 0
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot)
						&& !(c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt1 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt1);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt2 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt2);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt3 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - c.WillKeepAmt3);
					c.EquipStatus += 1;
				} else if (c.playerEquipment[EQUIP] > 0
						&& (c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)
						&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - 1 > 0) {
					c.getPA().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
							c.playerEquipmentN[EQUIP] - 1);
					c.EquipStatus += 1;
				}
			}
			c.ResetKeepItems();
			c.getPA().showInterface(17100);
			break;

		case 59004:
			c.getPA().removeAllWindows();
			break;

		case 26010:
			c.getPA().resetAutocast();
			break;
		case 1093:
		case 1094:
		case 1097:
			if (c.autocastId > 0) {
				c.getPA().resetAutocast();
			} else {
				if (c.playerMagicBook == 1) {
					if (c.playerEquipment[c.playerWeapon] == 4675 || c.playerEquipment[c.playerWeapon] == 6914
							|| c.playerEquipment[c.playerWeapon] == 21006
							|| c.playerEquipment[c.playerWeapon] == 12904|| c.playerEquipment[c.playerWeapon] == 22296) {
						c.setSidebarInterface(0, 1689);
					} else {
						c.sendMessage("You can't autocast ancients without a proper staff.");
					}
				} else if (c.playerMagicBook == 0) {
					if (c.playerEquipment[c.playerWeapon] == 4170 || c.playerEquipment[c.playerWeapon] == 21255) {
						c.setSidebarInterface(0, 12050);
					} else {
						c.setSidebarInterface(0, 1829);
					}
				}
			}
			break;
			case 115040:
				c.getDiaryManager().getVarrockDiary().display();
				break;
			case 115041:
				c.getDiaryManager().getArdougneDiary().display();
				break;
			case 115042:
				c.getDiaryManager().getDesertDiary().display();
				break;
			case 115043:
				c.getDiaryManager().getFaladorDiary().display();
				break;
			case 115044:
				c.getDiaryManager().getFremennikDiary().display();
				break;
			case 115045:
				c.getDiaryManager().getKandarinDiary().display();
				break;
			case 115046:
				c.getDiaryManager().getKaramjaDiary().display();
				break;
			case 115047:
				c.getDiaryManager().getLumbridgeDraynorDiary().display();
				break;
			case 115048:
				c.getDiaryManager().getMorytaniaDiary().display();
				break;
			case 115049:
				c.getDiaryManager().getWesternDiary().display();
				break;
			case 115050:
				c.getDiaryManager().getWildernessDiary().display();
				break;
		case 9167:
			ThreeOptions.handleOption1(c);
			break;
		case 9168:
			ThreeOptions.handleOption2(c);
			break;
		case 9169:
			ThreeOptions.handleOption3(c);
			break;
		/* VENG */
		case 118098:
			c.getPA().castVengeance();
			break;
		/**
		 * Specials *
		 */

		case 29063:
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (session != null) {
				if (session.getRules().contains(Rule.NO_SPECIAL_ATTACK)) {
					c.sendMessage("You are not permitted to activate special attacks during a duel.");
					return;
				}
			}
			Special special = Specials.DRAGON_BATTLEAXE.getSpecial();
			if (c.specAmount < special.getRequiredCost()) {
				c.sendMessage("You don't have the special amount to use this.");
				return;
			}
			if (!Arrays.stream(special.getWeapon()).anyMatch(axe -> c.getItems().isWearingItem(axe))) {
				return;
			}
			special.activate(c, null, null);
			c.specAmount -= special.getRequiredCost();
			c.usingSpecial = false;
			c.getItems().updateSpecialBar();
			break;

		case 29188:
			c.specBarId = 7636;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29038:
			if (c.playerEquipment[c.playerWeapon] == 4153 || c.playerEquipment[c.playerWeapon] == 12848) {
				c.specBarId = 7486;
				c.getCombat().handleGmaulPlayer();
				c.getItems().updateSpecialBar();
			} else {
				c.specBarId = 7486;
				c.usingSpecial = !c.usingSpecial;
				c.getItems().updateSpecialBar();
			}
			break;

		case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29113:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		/**
		 * Dueling *
		 */
		/*
		 * case 26065: // no forfeit case 26040: c.duelSlot = -1;
		 * c.getTradeAndDuel().selectRule(0); break;
		 */

		case 26065:
		case 26040:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.FORFEIT);
			break;

		case 26066: // no movement
		case 26048:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			if (!duelSession.getRules().contains(Rule.FORFEIT)) {
				duelSession.toggleRule(c, Rule.FORFEIT);
			}
			duelSession.toggleRule(c, Rule.NO_MOVEMENT);
			break;

		case 26069: // no range
		case 26042:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_RANGE);
			break;

		case 26070: // no melee
		case 26043:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_MELEE);
			break;

		case 26071: // no mage
		case 26041:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_MAGE);
			break;

		case 26072: // no drinks
		case 26045:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_DRINKS);
			break;

		case 26073: // no food
		case 26046:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_FOOD);
			break;

		case 26074: // no prayer
		case 26047:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_PRAYER);
			break;

		case 26076: // obsticals
		case 26075:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.OBSTACLES);
			break;

		case 2158: // fun weapons
		case 2157:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			if (duelSession.getRules().contains(Rule.WHIP_AND_DDS)) {
				duelSession.toggleRule(c, Rule.WHIP_AND_DDS);
				return;
			}
			if (!Rule.WHIP_AND_DDS.getReq().get().meets(c)
					&& !duelSession.getRules().contains(Rule.NO_SPECIAL_ATTACK)) {
				c.getPA().sendString("You must have a whip and dragon dagger to select this.", 6684);
				return;
			}
			if (!Rule.WHIP_AND_DDS.getReq().get().meets(duelSession.getOther(c))) {
				c.getPA().sendString("Your opponent does not have a whip and dragon dagger.", 6684);
				return;
			}
			if (duelSession.getStage().getStage() != MultiplayerSessionStage.OFFER_ITEMS) {
				c.sendMessage("You cannot change rules whilst on the second interface.");
				return;
			}
			duelSession.getRules().reset();
			for (Rule rule : Rule.values()) {
				index = rule.ordinal();
				if (index == 3 || index == 8 || index == 10 || index == 14) {
					continue;
				}
				duelSession.toggleRule(c, rule);
			}
			break;

		case 30136: // sp attack
		case 30137:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_SPECIAL_ATTACK);
			break;

		case 53245: // no helm
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_HELM);
			break;

		case 53246: // no cape
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_CAPE);
			break;

		case 53247: // no ammy
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_AMULET);
			break;

		case 53249: // no weapon
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_WEAPON);
			break;

		case 53250: // no body
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_BODY);
			break;

		case 53251: // no shield
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_SHIELD);
			break;

		case 53252: // no legs
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_LEGS);
			break;

		case 53255: // no gloves
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_GLOVES);
			break;

		case 53254: // no boots
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_BOOTS);
			break;

		case 53253: // no rings
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_RINGS);
			break;

		case 53248: // no arrows
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_ARROWS);
			break;

		case 26018:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (System.currentTimeMillis() - c.getDuel().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			if (Objects.nonNull(duelSession) && duelSession instanceof DuelSession) {
				duelSession.accept(c, MultiplayerSessionStage.OFFER_ITEMS);
			}
			break;

		case 25120:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (System.currentTimeMillis() - c.getDuel().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			if (Objects.nonNull(duelSession) && duelSession instanceof DuelSession) {
				duelSession.accept(c, MultiplayerSessionStage.CONFIRM_DECISION);
			}
			break;

		case 4169: // god spell charge
			c.usingMagic = true;
			if (c.getCombat().checkMagicReqs(48)) {
				if (System.currentTimeMillis() - c.godSpellDelay < 300000L) {
					c.sendMessage("You still feel the charge in your body!");
				} else {
					c.godSpellDelay = System.currentTimeMillis();
					c.sendMessage("You feel charged with a magical power!");
					c.gfx100(MagicData.MAGIC_SPELLS[48][3]);
					c.startAnimation(MagicData.MAGIC_SPELLS[48][2]);
					c.usingMagic = false;
				}
			}
			break;

		/*
		 * case 152: c.isRunning2 = !c.isRunning2; int frame = c.isRunning2 == true ? 1
		 * : 0; c.getPA().sendFrame36(173,frame); break;
		 */
		case 154:
			System.out.println("Setting cape: " + c.playerEquipment[c.playerCape]);
			if (c.playerEquipment[c.playerCape] == -1) {
				c.sendMessage("You must be wearing a skillcape in order to do this emote.");
				return;
			}
			PlayerEmotes.performSkillcapeAnimation(c, new GameItem(c.playerEquipment[c.playerCape]));
			break;
		case 166011:
		case 152:
			if (Boundary.isIn(c, Boundary.ICE_PATH)) {
				c.getPA().setConfig(173, 0);
				c.getPA().setConfig(173, 0);
				return;
			}
			if (c.getRunEnergy() < 1) {
				c.isRunning = false;
				c.isRunning2 = false;
				c.getPA().setConfig(173, 0);
				c.getPA().setConfig(173, 0);
				return;
			}
			c.isRunning2 = !c.isRunning2;
			c.isRunning = !c.isRunning;
			c.getPA().sendFrame36(173, c.isRunning2 ? 1 : 0);
			c.getPA().sendFrame36(173, c.isRunning2 ? 1 : 0);
			break;

		case 48176:
			c.acceptAid = !c.acceptAid;
			c.getPA().setConfig(427, c.acceptAid ? 1 : 0);
			break;

		case 9154:
			long buttonDelay = 0;
			if (System.currentTimeMillis() - buttonDelay > 2000) {
				c.logout();
				buttonDelay = System.currentTimeMillis();
			}
			break;

		case 21010:
			c.takeAsNote = true;
			break;

		case 21011:
			c.takeAsNote = false;
			break;

		case 156065:
			if (c.lastTeleportX == 0) {
				c.sendMessage("You haven't teleported anywhere recently.");
			} else {
				c.getPA().startTeleport(c.lastTeleportX, c.lastTeleportY, c.lastTeleportZ, "modern", false);
			}
			break;
		case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Scimitar */
		case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Mace */
		case 29199:
			c.specBarId = 7636;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Battleaxe & Hatchets */
		case 29074:
			c.specBarId = 7511;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Halberd $ Staff of Light */
		case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Spear */
		case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Godswords & 2h Swords */
		case 30007:
			c.specBarId = 7711;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Claws */
		case 30108:
			if (c.getItems().isWearingItem(11791, c.playerWeapon)
					|| c.getItems().isWearingItem(12904, c.playerWeapon)) {
				session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (session != null) {
					if (session.getRules().contains(Rule.NO_SPECIAL_ATTACK)) {
						c.sendMessage("You are not permitted to activate special attacks during a duel.");
						return;
					}
				}
				Special sotd = Specials.STAFF_OF_THE_DEAD.getSpecial();
				if (c.specAmount >= sotd.getRequiredCost()) {
					c.specAmount -= sotd.getRequiredCost();
					sotd.activate(c, c, null);
					c.specBarId = 7812;
					c.usingSpecial = false;
					c.getItems().updateSpecialBar();
					return;
				}
			}
			c.specBarId = 7812;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Whip */
		case 48034:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Warhammer & Mauls */
		case 29049:
			if (c.playerEquipment[c.playerWeapon] == 4153 || c.playerEquipment[c.playerWeapon] == 12848) {
				c.specBarId = 7486;
				c.getCombat().handleGmaulPlayer();
				c.getItems().updateSpecialBar();
			} else {
				c.specBarId = 7486;
				c.usingSpecial = !c.usingSpecial;
				c.getItems().updateSpecialBar();
			}
			break;
		/* Pickaxe */
		case 30043:
			c.specBarId = 7736;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Bows */
		case 29124:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		/* Throwing Axe & Javelins */
		case 29213:
			c.specBarId = 7661;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
		// home teleports
		case 4171:
		case 117048:
		case 84237:
		case 75010:
			if(c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
				c.sendMessage("You can't teleport above " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
				return;
			}
			c.getPA().spellTeleport(3094, 3476, 0, true);
			//c.getPA().showInterface(51000);
			//c.getTeleport().selection(c, 0);
			break;
		case 50056:
			if (c.homeTeleport >= 1 && c.homeTeleport <= 10) {
				return;
			}
			c.getPA().spellTeleport(Config.START_LOCATION_X, Config.START_LOCATION_Y, 0, true);
			break;

		// case 4171: case 50056: case 117048: if (c.homeTeleDelay <= 0) { c.homeTele =
		// 10; } else if (c.homeTeleDelay <= 0) { c.homeTele = 10; }
		//
		//
		// if (c.reset == false) { c.HomePort(); String type = c.playerMagicBook == 0 ?
		// "modern" : "ancient"; c.getPA().startTeleport(Config.EDGEVILLE_X,
		// Config.EDGEVILLE_Y, 0,
		// type); } else if (c.reset == true) { c.resetHomePort(); }

		/*
		 * case 29031: c.getDH().sendDialogues(121312, -1); break;
		 */

		/*
		 * case 51013: case 6004: case 118242: c.getDH().sendOption5("Lumbridge",
		 * "Varrock", "Camelot", "Falador", "Canifis"); c.teleAction = 20; break;
		 */
		/*
		 * case 4140: case 4143: case 4150: case 4146: case 6004: case 6005: case 29031:
		 * case 50235: case 50245: case 50253: case 51005: case 51013: case 51023: case
		 * 51031: case 51039: case 117112: case 117131: case 117154: case 117186: case
		 * 117210: case 118018: case 118042: case 118058:
		 * c.getPA().showInterface(62100); break;
		 */

		case 9125: // Accurate
		case 6221: // range accurate
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
		case 22228: // punch
			c.usingMagic = false;
			c.fightMode = 0;
			if (c.autocasting) {
				c.getPA().resetAutocast();
			}
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
			// case 22231: //unarmed
		case 22229: // unarmed
			c.usingMagic = false;
			c.fightMode = 1;
			if (c.autocasting) {
				c.getPA().resetAutocast();
			}
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
		case 33018: // jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
		case 18077: // lunge (spear)
		case 18080: // swipe (spear)
		case 18079: // pound (spear)
		case 17100: // longrange (darts)
			c.usingMagic = false;
			c.fightMode = 3;
			if (c.autocasting) {
				c.getPA().resetAutocast();
			}
			break;

		case 9128: // Aggressive
		case 6220: // range rapid
		case 21203: // impale (pickaxe)
		case 21202: // smash (pickaxe)
		case 1079: // pound (staff)
		case 6171: // hack (axe)
		case 6170: // smash (axe)
		case 33020: // swipe (hally)
		case 6235: // rapid (long bow)
		case 17101: // repid (darts)
		case 8237: // lunge (dagger)
		case 8236: // slash (dagger)
		case 22230: // kick
			c.usingMagic = false;
			c.fightMode = 2;
			if (c.autocasting) {
				c.getPA().resetAutocast();
			}
			break;
			case 166056:
				c.getPA().showInterface(53000);
				break;

			case 166028:
				c.getPA().showInterface(39000);
				break;
		/**
		 * Prayers *
		 */
		case 21233: // thick skin
			c.getCombat().activatePrayer(0);
			break;
		case 21234: // burst of str
			c.getCombat().activatePrayer(1);
			break;
		case 21235: // charity of thought
			c.getCombat().activatePrayer(2);
			break;
		case 77100: // range
			c.getCombat().activatePrayer(3);
			break;
		case 77102: // mage
			c.getCombat().activatePrayer(4);
			break;
		case 21236: // rockskin
			c.getCombat().activatePrayer(5);
			break;
		case 21237: // super human
			c.getCombat().activatePrayer(6);
			break;
		case 21238: // improved reflexes
			c.getCombat().activatePrayer(7);
			break;
		case 21239: // hawk eye
			c.getCombat().activatePrayer(8);
			break;
		case 21240:
			c.getCombat().activatePrayer(9);
			break;
		case 21241: // protect Item
			c.getCombat().activatePrayer(10);
			break;
		case 77104: // 26 range
			c.getCombat().activatePrayer(11);
			break;
		case 77106: // 27 mage
			c.getCombat().activatePrayer(12);
			break;
		case 21242: // steel skin
			c.getCombat().activatePrayer(13);
			break;
		case 21243: // ultimate str
			c.getCombat().activatePrayer(14);
			break;
		case 21244: // incredible reflex
			c.getCombat().activatePrayer(15);
			break;
		case 21245: // protect from magic
			c.getCombat().activatePrayer(16);
			break;
		case 21246: // protect from range
			c.getCombat().activatePrayer(17);
			break;
		case 21247: // protect from melee
			c.getCombat().activatePrayer(18);
			break;
		case 77109: // 44 range
			c.getCombat().activatePrayer(19);
			break;
		case 77111: // 45 mystic
			c.getCombat().activatePrayer(20);
			break;
		case 2171: // retrui
			c.getCombat().activatePrayer(21);
			break;
		case 2172: // redem
			c.getCombat().activatePrayer(22);
			break;
		case 2173: // smite
			c.getCombat().activatePrayer(23);
			break;
		case 153233: // preserve
			c.getCombat().activatePrayer(24);
			break;
		case 77113: // chiv
			c.getCombat().activatePrayer(25);
			break;
		case 77115: // piety
			c.getCombat().activatePrayer(26);
			break;
		case 153236: // rigour
			c.getCombat().activatePrayer(27);
			break;

		case 153239: // augury
			c.getCombat().activatePrayer(28);
			break;

		case 13092:
			if (!Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				c.sendMessage("You are not trading!");
				return;
			}
			if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.TRADE).accept(c,
					MultiplayerSessionStage.OFFER_ITEMS);
			break;

		case 13218:
			if (!Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				c.sendMessage("You are not trading!");
				return;
			}
			if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.TRADE).accept(c,
					MultiplayerSessionStage.CONFIRM_DECISION);
			break;

		case 125011: // Click agree
			if (!c.ruleAgreeButton) {
				c.ruleAgreeButton = true;
				c.getPA().sendFrame36(701, 1);
			} else {
				c.ruleAgreeButton = false;
				c.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (c.ruleAgreeButton) {
				c.getPA().showInterface(3559);
				c.newPlayer = false;
			} else if (!c.ruleAgreeButton) {
				c.sendMessage("You need to agree before you can carry on.");
			}
			break;
		case 125006:// Decline
			c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
		case 166055:
			if (!c.mouseButton) {
				c.mouseButton = true;
				c.getPA().sendFrame36(500, 1);
				c.getPA().sendFrame36(170, 1);
			} else if (c.mouseButton) {
				c.mouseButton = false;
				c.getPA().sendFrame36(500, 0);
				c.getPA().sendFrame36(170, 0);
			}
			break;
		case 74184:
		case 166046:
		case 3189:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getPA().sendFrame36(502, 1);
				c.getPA().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getPA().sendFrame36(502, 0);
				c.getPA().sendFrame36(287, 0);
			}
			break;
		case 74180:
		case 166045:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getPA().sendFrame36(501, 1);
				c.getPA().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getPA().sendFrame36(501, 0);
				c.getPA().sendFrame36(171, 1);
			}
			break;
		case 74188:
		case 166010:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getPA().sendFrame36(503, 1);
				c.getPA().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getPA().sendFrame36(503, 0);
				c.getPA().sendFrame36(427, 0);
			}
			break;
		case 74192:
			if (!c.isRunning2) {
				c.isRunning2 = true;
				c.getPA().sendFrame36(504, 1);
				c.getPA().sendFrame36(173, 1);
			} else {
				c.isRunning2 = false;
				c.getPA().sendFrame36(504, 0);
				c.getPA().sendFrame36(173, 0);
			}
			break;
		case 74201:// brightness1
			c.getPA().sendFrame36(505, 1);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 1);
			break;
		case 74203:// brightness2
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 1);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 2);
			break;

		case 74204:// brightness3
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 1);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 3);
			break;

		case 74205:// brightness4
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 1);
			c.getPA().sendFrame36(166, 4);
			break;
		case 74206:// area1
			c.getPA().sendFrame36(509, 1);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 1);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 1);
			break;
		case 43092:
			c.startAnimation(0x558);
			break;
		/*
		 * case 72254: //c.startAnimation(3866); break; /* END OF EMOTES
		 */

		case 24017:
			c.getPA().resetAutocast();
			// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon],
					ItemAssistant.getItemName(c.playerEquipment[c.playerWeapon]));
			// c.setSidebarInterface(0, 328);
			// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 938 :
			// c.playerMagicBook == 1 ? 838 : 938);
			break;
		}
		if (c.isAutoButton(actionButtonId)) {
			c.assignAutocast(actionButtonId);
		}
	}
}
