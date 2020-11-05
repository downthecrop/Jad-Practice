package ethos.model.players.packets.itemoptions;

import java.util.Objects;

import ethos.Config;
import ethos.Server;
import ethos.model.content.LootingBag.LootingBag;
import ethos.model.content.barrows.Barrows;
import ethos.model.content.teleportation.TeleportTablets;
import ethos.model.items.ItemDefinition;
import ethos.model.items.item_combinations.Godswords;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.Boundary;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.model.players.combat.Degrade;
import ethos.model.players.skills.hunter.impling.Impling;
import ethos.model.players.skills.runecrafting.Pouches;
import ethos.model.players.skills.runecrafting.Pouches.Pouch;
import ethos.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemOptionTwo implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int itemId = player.getInStream().readSignedWordA();

		if (!player.getItems().playerHasItem(itemId, 1))
			return;
		if (player.getInterfaceEvent().isActive()) {
			player.sendMessage("Please finish what you're doing.");
			return;
		}
		if (player.getBankPin().requiresUnlock()) {
			player.getBankPin().open(2);
			return;
		}
		if (player.getTutorial().isActive()) {
			player.getTutorial().refresh();
			return;
		}
		if (LootingBag.isLootingBag(player, itemId)) {
			player.getLootingBag().openDepositMode();
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			player.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(player).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		TeleportTablets.operate(player, itemId);
		ItemDefinition def = ItemDefinition.forId(itemId);
		switch (itemId) {

		case 21347:
			player.boltTips = false;
			player.arrowTips = true;
			player.javelinHeads = false;
			player.sendMessage("Your Amethyst method is now Arrowtips!");
			break;

			case 13660:
				if(player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
					player.sendMessage("You can't teleport above " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
					return;
				}
				player.getPA().showInterface(51000);
				player.getTeleport().selection(player, 0);
				return;
		case 11238:
		case 11240:
		case 11242:
		case 11244:
		case 11246:
		case 11248:
		case 11250:
		case 11252:
		case 11254:
		case 11256:
		case 19732:
			Impling.getReward(player, itemId);
			break;
			
		case 20164: //Spade
			int x = player.absX;
			int y = player.absY;
			if (Boundary.isIn(player, Barrows.GRAVEYARD)) {
				player.getBarrows().digDown();
			}
			if (x == 3005 && y == 3376 || x == 2999 && y == 3375 || x == 2996 && y == 3377) {
				if (!player.getRechargeItems().hasItem(13120)) {
					player.sendMessage("You must have the elite falador shield to do this.");
					return;
				}
				player.getPA().movePlayer(1760, 5163, 0);
			}
			break;
		
		case 20243:
				if (System.currentTimeMillis() - player.lastPerformedEmote < 2500)
					return;			
				player.startAnimation(7268);
				player.lastPerformedEmote = System.currentTimeMillis();
			break;
		
		case 13136:
			if (player.inClanWars() || player.inClanWarsSafe()) {
				player.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
				return;
			}
			if (Server.getMultiplayerSessionListener().inAnySession(player)) {
				player.sendMessage("You cannot do that right now.");
				return;
			}
			if (player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
				player.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
				return;
			}
			player.getPA().spellTeleport(3426, 2927, 0, false);
			break;
		
		case 13117:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(4);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13118:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(2);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
		case 13119:
		case 13120:
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				if (player.getRechargeItems().useItem(itemId)) {
					player.getRechargeItems().replenishPrayer(1);
				}
			} else {
				player.sendMessage("You already have full prayer points.");
				return;
			}
			break;
			
		case 13111:
			if (player.getRechargeItems().useItem(itemId)) {
				player.getPA().spellTeleport(3236, 3946, 0, false);
			}
			break;
		
		case 13241:
		case 13243:
			Degrade.checkRemaining(player, itemId);
			break;
		
		case 13280:
			player.getDH().sendDialogues(76, 1);
			break;

			case 11802:
			case 11804:
			case 11806:
			case 11808:
				Godswords.dismantle(player,itemId);
				break;

		case 13226:
			player.getHerbSack().withdrawAll();
			break;
			
		case 12020:
			player.getGemBag().check();
			break;
			
		case 5509:
			Pouches.check(player, Pouch.forId(itemId), itemId, 0);
			break;
		case 5510:
			Pouches.check(player, Pouch.forId(itemId), itemId, 1);
			break;
		case 5512:
			Pouches.check(player, Pouch.forId(itemId), itemId, 2);
			break;
		case 12904:
			player.sendMessage("The toxic staff of the dead has " + player.getToxicStaffOfTheDeadCharge() + " charges remaining.");
			break;
		case 13199:
		case 13197:
			player.sendMessage("The " + def.getName() + " has " + player.getSerpentineHelmCharge() + " charges remaining.");
			break;
		case 11907:
		case 12899:
			int charge = itemId == 11907 ? player.getTridentCharge() : player.getToxicTridentCharge();
			player.sendMessage("The " + def.getName() + " has " + charge + " charges remaining.");
			break;
		case 12926:
			def = ItemDefinition.forId(player.getToxicBlowpipeAmmo());
			player.sendMessage("The blowpipe has " + player.getToxicBlowpipeAmmoAmount() + " " + def.getName() + " and " + player.getToxicBlowpipeCharge() + " charge remaining.");
			break;

				case 12929:
					if(player.getItems().playerHasItem(12929)) {
						player.getItems().deleteItem(12929, 1);
						player.getItems().addItemUnderAnyCircumstance(12934, 20000);
						player.sendMessage("You successfully dismantle the Serpentine Helmet for 20,000 zulrah scales.");
					}
				break;

		case 12931:
			def = ItemDefinition.forId(itemId);
			if (def == null) {
				return;
			}
			player.sendMessage("The " + def.getName() + " has " + player.getSerpentineHelmCharge() + " charge remaining.");
			break;
		case 8901:
			player.getPA().assembleSlayerHelmet();
			break;
		case 19675:
			//if (player.getArcLightCharge() >= 1000) {
			//	player.getItems().addItem(19677, 3);
			//	player.getDH().sendStatement("You lost some of the ancient shards in the process!");
			//} else {
			//	player.getDH().sendStatement("Your Arclight's charge was too low to refund shards.");
			//}
		//	player.getItems().deleteItem(19675, 1);
		//	player.getItems().addItem(6746, 1);
		//	player.setArcLightCharge(0);
			player.getDH().sendItemStatement("Your arc light has "+player.getArcLightCharge()+ " charges.",19675);
			break;
		case 11283:
		case 11285:
		case 11284:
			player.sendMessage("Your dragonfire shield currently has " + player.getDragonfireShieldCharge() + " charges.");
			break;
		case 4155:
			player.sendMessage("I currently have@blu@ " + player.getSlayer().getPoints() + "@bla@ slayer points.");
			break;
		default:
			if (player.getRights().isOrInherits(Right.OWNER)) {
				Misc.println("[DEBUG] Item Option #2-> Item id: " + itemId);
			}
			break;
		}

	}

}
