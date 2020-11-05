package ethos.model.players.packets;

import org.apache.commons.lang3.text.WordUtils;

import ethos.Config;
import ethos.Server;
import ethos.event.impl.WheatPortalEvent;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemDefinition;
import ethos.model.npcs.NPC;
import ethos.model.npcs.NPCHandler;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.players.*;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.Degrade;
import ethos.model.players.combat.Degrade.DegradableItem;
import ethos.model.players.combat.effects.DragonfireShieldEffect;
import ethos.util.Misc;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class OperateItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int slot = c.getInStream().readSignedWordA(); // the row of the action
		int itemId = c.getInStream().readSignedWordA(); //the item's id

		if(c.getRights().isOrInherits(Right.OWNER)) {
			c.sendMessage("Operate Item - itemId: " + itemId + " slot: " + slot);
		}

			ItemDefinition def = ItemDefinition.forId(itemId);
			Optional<DegradableItem> d = DegradableItem.forId(itemId);
			if (d.isPresent()) {
				Degrade.checkPercentage(c, itemId);
				return;
			}
			switch (itemId) {
			case 9948:
			case 9949:
				if (WheatPortalEvent.xLocation > 0 && WheatPortalEvent.yLocation > 0) {
					c.getPA().spellTeleport(WheatPortalEvent.xLocation + 1, WheatPortalEvent.yLocation + 1, 0, false);
				} else {
					c.sendMessage("There is currently no portal available, wait 5 minutes.");
					return;
				}
				break;
			case 12904:
				c.sendMessage("The toxic staff of the dead has " + c.getToxicStaffOfTheDeadCharge() + " charges remaining.");
				break;

				case 13660:
					if(c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
						c.sendMessage("You can't teleport above " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
						return;
					}
					c.getPA().showInterface(51000);
					c.getTeleport().selection(c, 0);
					return;
			case 13199:
			case 13197:
				c.sendMessage("The " + def.getName() + " has " + c.getSerpentineHelmCharge() + " charges remaining.");
				break;
			case 19675:
				c.sendMessage("Your Arclight has "+ c.getArcLightCharge() +" charges remaining.");
			break;
			case 11907:
			case 12899:
				int charge = itemId == 11907 ? c.getTridentCharge() : c.getToxicTridentCharge();
				c.sendMessage("The " + def.getName() + " has " + charge + " charges remaining.");
				break;
			case 12926:
				def = ItemDefinition.forId(c.getToxicBlowpipeAmmo());
				c.sendMessage("The blowpipe has " + c.getToxicBlowpipeAmmoAmount() + " " + def.getName() + " and " + c.getToxicBlowpipeCharge() + " charge remaining.");
				break;
			case 12931:
				def = ItemDefinition.forId(itemId);
				if (def == null) {
					return;
				}
				c.sendMessage("The " + def.getName() + " has " + c.getSerpentineHelmCharge() + " charge remaining.");
				break;

			case 13125:
			case 13126:
			case 13127:
				if (c.getRunEnergy() < 100) {
					if (c.getRechargeItems().useItem(itemId)) {
						c.getRechargeItems().replenishRun(50);
					}
				} else {
					c.sendMessage("You already have full run energy.");
					return;
				}
				break;

			case 13128:
				if (c.getRunEnergy() < 100) {
					if (c.getRechargeItems().useItem(itemId)) {
						c.getRechargeItems().replenishRun(100);
					}
				} else {
					c.sendMessage("You already have full run energy.");
					return;
				}
				break;

			case 13117:
				if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
					if (c.getRechargeItems().useItem(itemId)) {
						c.getRechargeItems().replenishPrayer(4);
					}
				} else {
					c.sendMessage("You already have full prayer points.");
					return;
				}
				break;
			case 13118:
				if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
					if (c.getRechargeItems().useItem(itemId)) {
						c.getRechargeItems().replenishPrayer(2);
					}
				} else {
					c.sendMessage("You already have full prayer points.");
					return;
				}
				break;
			case 13119:
			case 13120:
				if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
					if (c.getRechargeItems().useItem(itemId)) {
						c.getRechargeItems().replenishPrayer(1);
					}
				} else {
					c.sendMessage("You already have full prayer points.");
					return;
				}
				break;
			case 13111:
				if (c.getRechargeItems().useItem(itemId)) {
					c.getPA().spellTeleport(3236, 3946, 0, false);
				}
				break;
			case 10507:
				if (c.getItems().isWearingItem(10507)) {
					if (System.currentTimeMillis() - c.lastPerformedEmote < 2500)
						return;
					c.startAnimation(6382);
					c.gfx0(263);
					c.lastPerformedEmote = System.currentTimeMillis();
				}
				break;

			case 20243:
					if (System.currentTimeMillis() - c.lastPerformedEmote < 2500)
						return;
					c.startAnimation(7268);
					c.lastPerformedEmote = System.currentTimeMillis();
				break;

			case 4212:
			case 4214:
			case 4215:
			case 4216:
			case 4217:
			case 4218:
			case 4219:
			case 4220:
			case 4221:
			case 4222:
			case 4223:
				c.sendMessage("You currently have " + (250 - c.crystalBowArrowCount) + " charges left before degradation to " + (c.playerEquipment[3] == 4223 ? "Crystal seed" : ItemAssistant.getItemName(c.playerEquipment[3] + 1)));
				break;

			case 4202:
			case 9786:
			case 9787:
				PlayerAssistant.ringOfCharosTeleport(c);
				break;

			case 11283:
			case 11284:
				if (Boundary.isIn(c, Zulrah.BOUNDARY) || Boundary.isIn(c, Boundary.CERBERUS_BOSSROOMS)) {
					return;
				}
				DragonfireShieldEffect dfsEffect = new DragonfireShieldEffect();
				if (c.npcIndex <= 0 && c.playerIndex <= 0) {
					return;
				}
				if (c.getHealth().getAmount() <= 0 || c.isDead) {
					return;
				}
				if (dfsEffect.isExecutable(c)) {
					Damage damage = new Damage(Misc.random(25));
					if (c.playerIndex > 0) {
						Player target = PlayerHandler.players[c.playerIndex];
						if (Objects.isNull(target)) {
							return;
						}
						c.attackTimer = 7;
						dfsEffect.execute(c, target, damage);
						c.setLastDragonfireShieldAttack(System.currentTimeMillis());
					} else if (c.npcIndex > 0) {
						NPC target = NPCHandler.npcs[c.npcIndex];
						if (Objects.isNull(target)) {
							return;
						}
						c.attackTimer = 7;
						dfsEffect.execute(c, target, damage);
						c.setLastDragonfireShieldAttack(System.currentTimeMillis());
					}
				}
				break;

				/**
				 * Max capes
				 */
			case 13280:
			case 13329:
			case 13337:
			case 13331:
			case 13333:
			case 13335:
			case 20760:
				c.getDH().sendDialogues(76, 1);
				break;

				/**
				 * Crafting cape
				 */
			case 9780:
			case 9781:
				c.getPA().startTeleport(2936, 3283, 0, "modern", false);
				break;

				/**
				 * Magic skillcape
				 */
			case 9762:
			case 9763:
				if (!Boundary.isIn(c, Boundary.EDGEVILLE_PERIMETER)) {
					c.sendMessage("This cape can only be operated within the edgeville perimeter.");
					return;
				}
				if (c.inWild()) {
					return;
				}
					if (c.playerMagicBook == 0) {
						c.playerMagicBook = 1;
						c.setSidebarInterface(6, 838);
						c.autocasting = false;
						c.sendMessage("An ancient wisdomin fills your mind.");
						c.getPA().resetAutocast();
					} else if (c.playerMagicBook == 1) {
						c.sendMessage("You switch to the lunar spellbook.");
						c.setSidebarInterface(6, 29999);
						c.playerMagicBook = 2;
						c.autocasting = false;
						c.autocastId = -1;
						c.getPA().resetAutocast();
					} else if (c.playerMagicBook == 2) {
						c.setSidebarInterface(6, 938);
						c.playerMagicBook = 0;
						c.autocasting = false;
						c.sendMessage("You feel a drain on your memory.");
						c.autocastId = -1;
						c.getPA().resetAutocast();
					}
					break;
				case 13136:
					switch (slot) {
						case 1:
							c.getPA().spellTeleport(3484, 9510, 2, false);
							break;
						case 2:
							c.getPA().spellTeleport(3426, 2927, 0, false);
							break;
					}
					break;
				case 1712:
				case 1710:
				case 1708:
				case 1706:
					switch (slot) {
					case 1:
						c.getPA().startTeleport(3087, 3493, 0, "glory",false);

						break;
					case 2:
						c.getPA().startTeleport(2925, 3173, 0, "glory",false);
						break;
					case 3:
						c.getPA().startTeleport(3079, 3250, 0, "glory",false);
						break;
					case 4:
						c.getPA().startTeleport(3293, 3176, 0,"glory", false);
						break;
					}
					break;

				case 11864:
				case 11865:
				case 19639:
				case 19641:
				case 19643:
				case 19645:
				case 19647:
				case 19649:
					switch (slot) {
						case 1:
							if (!c.getSlayer().getTask().isPresent()) {
								c.sendMessage("You do not have a task!");
								return;
							}
							c.sendMessage("I currently have @blu@" + c.getSlayer().getTaskAmount() + " " + c.getSlayer().getTask().get().getPrimaryName() + "@bla@ to kill.");
							c.getPA().closeAllWindows();
							break;
						case 2:
							if (Server.getMultiplayerSessionListener().inAnySession(c)) {
								return;
							}
//				c.getDH().sendDialogues(12000, -1);
							for (int i = 8144; i < 8195; i++) {
								c.getPA().sendFrame126("", i);
							}
							int[] frames = { 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173,
									8174, 8175, 8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194 };
							c.getPA().sendFrame126("@dre@Kill Tracker for @blu@" + c.playerName + "", 8144);
							c.getPA().sendFrame126("", 8145);
							c.getPA().sendFrame126("@blu@Total kills@bla@ - " + c.getNpcDeathTracker().getTotal() + "", 8147);
							c.getPA().sendFrame126("", 8148);
							int frameIndex = 0;
							for (Map.Entry<String, Integer> entry : c.getNpcDeathTracker().getTracker().entrySet()) {
								if (entry == null) {
									continue;
								}
								if (frameIndex > frames.length - 1) {
									break;
								}
								if (entry.getValue() > 0) {
									c.getPA().sendFrame126("@blu@" + WordUtils.capitalize(entry.getKey().toLowerCase()) + ": @red@" + entry.getValue(), frames[frameIndex]);
									frameIndex++;
								}
							}
							c.getPA().showInterface(8134);
							break;
					}
					break;

				case 2552:
				case 2554:
				case 2556:
				case 2558:
				case 2560:
				case 2562:
				case 2564:
				case 2566:
					switch (slot) {
						case 1:
							c.getPA().spellTeleport(3370, 3157, 0, false);
							break;
						case 2:
							c.getPA().spellTeleport(2441, 3088, 0, false);
							break;
						case 3:
							c.getPA().spellTeleport(3304, 3130, 0, false);
							break;
					}
					break;

				default:
					c.sendMessage("Nothing interesting happens..");
					break;
			}
		}
	}
