package ethos.model.players.packets;

import java.util.Objects;
import java.util.Optional;

import ethos.Config;
import ethos.Server;
import ethos.model.items.GameItem;
import ethos.model.items.ItemCombination;
import ethos.model.items.ItemCombinations;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.npcs.pets.PetHandler;
import ethos.model.npcs.pets.PetHandler.Pets;
import ethos.model.players.Boundary;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;
import ethos.model.shops.ShopAssistant;

/**
 * Drop Item Class
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {

		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		int slot = c.getInStream().readUnsignedWordA();
		c.alchDelay = System.currentTimeMillis();
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		if (!c.getItems().playerHasItem(itemId)) {
			return;
		}
		if (c.viewingLootBag || c.addingItemsToLootBag || c.viewingRunePouch) {
			return;
		}
		if (c.isStuck) {
			c.isStuck = false;
			c.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
			return;
		}
		for (int item : Config.UNDROPPABLE_ITEMS) {
			if (item == itemId) {
				c.sendMessage("You can not drop this item!");
				return;
			}
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		Pets pet = PetHandler.forItem(itemId);

		if (pet != null) {
			c.startAnimation(827);
			PetHandler.spawn(c, pet, false, false);
			return;
		}

		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}

		if (c.playerIndex > 0) {
			c.sendMessage("You can't drop items during player combat.");
			return;
		}
		if (c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}

		boolean droppable = true;
		for (int i : Config.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}

		if (slot >= c.playerItems.length || slot < 0 || slot >= c.playerItems.length) {
			return;
		}

		switch (itemId) {
		case 19722:
			c.getItems().deleteItem(19722, 1);
			c.getItems().addItem(12954, 1);
			c.getItems().addItem(20143, 1);
			c.sendMessage("Your trimmed dragon defender has turned into regular again.");
			break;
		}

		if (itemId == 12904) {
			if (c.getToxicStaffOfTheDeadCharge() <= 0) {
				c.getItems().deleteItem2(12904, 1);
				c.getItems().addItem(12902, 1);
				c.sendMessage("The staff had no charge, but has been reverted to uncharged.");
				return;
			}
			if (c.getItems().freeSlots() <= 0) {
				c.sendMessage("You need one free slot to do this.");
				return;
			}
			c.getItems().deleteItem2(12904, 1);
			c.getItems().addItem(12902, 1);
			c.getItems().addItem(12934, c.getToxicStaffOfTheDeadCharge());
			c.setToxicStaffOfTheDeadCharge(0);
			c.sendMessage("You uncharged the toxic staff of the dead and retain.");
		}

		if (itemId == 12926) {
			int ammo = c.getToxicBlowpipeAmmo();
			int amount = c.getToxicBlowpipeAmmoAmount();
			int charge = c.getToxicBlowpipeCharge();
			if (ammo > 0 && amount > 0) {
				c.sendMessage("You must unload before you can uncharge.");
				return;
			}
			if (charge <= 0) {
				c.sendMessage("The toxic blowpipe had no charge, it is emptied.");
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(12924, 1);
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free slots to do this.");
				return;
			}
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(12924, 1);
			c.getItems().addItem(12934, charge);
			c.setToxicBlowpipeAmmo(0);
			c.setToxicBlowpipeAmmoAmount(0);
			c.setToxicBlowpipeCharge(0);
			return;
		}

		if (itemId == 12931 || itemId == 13199 || itemId == 13197) {
			int uncharged = itemId == 12931 ? 12929 : itemId == 13199 ? 13198 : 13196;
			int charge = c.getSerpentineHelmCharge();
			if (charge <= 0) {
				c.sendMessage("The serpentine helm had no charge, it is emptied.");
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(uncharged, 1);
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free slots to do this.");
				return;
			}
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(uncharged, 1);
			c.getItems().addItem(12934, charge);
			c.setSerpentineHelmCharge(0);
			return;
		}

		Optional<ItemCombination> revertableItem = ItemCombinations.isRevertable(new GameItem(itemId));

		if (revertableItem.isPresent()) {
			// revertableItem.get().sendRevertConfirmation(c);
			revertableItem.get().revert(c);
			c.dialogueAction = 555;
			c.nextChat = -1;
			return;
		}
		if (c.playerItemsN[slot] != 0 && itemId != -1 && c.playerItems[slot] == itemId + 1) {
			if (droppable) {
				if (c.underAttackBy > 0) {
					if (ShopAssistant.getItemShopValue(itemId) > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				c.droppedItem = itemId;
				c.droppingItem = true;
				if (c.showDropWarning()) {
					c.getPA().destroyInterface("drop");
				} else {
					// Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel,
					// c.playerItemsN[slot], c.getIndex());
					if (c.getItems().playerHasItem(itemId) && !c.isDead) {
						Server.itemHandler.createGroundItem(c, itemId, c.absX, c.absY, c.heightLevel,
								c.playerItemsN[slot], c.getIndex());
						c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
						// c.getPA().destroyItem(itemId);
						c.getPA().removeAllWindows();
					} else {
						return;
					}
				}
				PlayerSave.saveGame(c);
			}
		}
	}
}
