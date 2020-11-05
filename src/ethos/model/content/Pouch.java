package ethos.model.content;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import ethos.Server;
import ethos.model.content.LootingBag.LootingBagItem;
import ethos.model.entity.Entity;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.Player;



public abstract class Pouch {
	public Player player;
	void handleDeath(Player player,String entity,List<LootingBagItem>items){
		Entity killer = player.getKiller();
		for (Iterator<LootingBagItem> iterator=items.iterator(); iterator.hasNext();) {
			LootingBagItem item = iterator.next();

			if (item == null) {
				continue;
			}
			if (item.getId() <= 0 || item.getAmount() <= 0) {
				continue;
			}
			if (entity.equals("PVP")) {
				if (killer != null && killer instanceof Player) {
					Player playerKiller = (Player) killer;
					if (playerKiller.getMode().isItemScavengingPermitted()) {
						Server.itemHandler.createGroundItem(playerKiller, item.getId(), player.getX(), player.getY(), player.heightLevel, item.getAmount(), player.killerId);
					} else {
						Server.itemHandler.createUnownedGroundItem(item.getId(), player.getX(), player.getY(), player.heightLevel, item.getAmount());
					}
				}
			} else {
				Server.itemHandler.createGroundItem(player, item.getId(), player.getX(), player.getY(),
				                                    player.heightLevel, item.getAmount(), player.getIndex());
			}
			iterator.remove();
		}
	}

	int countItems(int id,List<LootingBagItem> items) {
		int count = 0;
		for (LootingBagItem item : items) {
			if (item.getId() == id + 1) {
				count += item.getAmount();
			}
		}
		return count;
	}

	void withdrawItems(List<LootingBagItem>items){
		if (!configurationPermitted()) {
			player.sendMessage("You cannot do this right now.");
			return;
		}
		for (Iterator<LootingBagItem> iterator = items.iterator(); iterator.hasNext();) {
			LootingBagItem item = iterator.next();
			if (!player.getItems().addItem(item.getId(), item.getAmount())) {
				break;
			}
			iterator.remove();
		}
	}

	boolean sackContainsItem(int id,List<LootingBagItem> items) {
		for (LootingBagItem item : items) {
			if (item.getId() == id) {
				return true;
			}
		}
		return false;
	}

	boolean addItemToList(int id, int amount,List<LootingBagItem> items) {
		for (LootingBagItem item : items) {
			if (item.getId() == id) {
				if (item.getAmount() + amount >= 61) {
					return false;
				}
				if (player.getItems().isStackable(id)) {
					item.incrementAmount(amount);
					return false;
				}
			}
		}
		items.add(new LootingBagItem(id, amount));
		return true;
	}

	boolean configurationPermitted() {
		if (player.inDuelArena() || player.inPcGame() || player.inPcBoat() || player.isInJail() || player.getInterfaceEvent().isActive() || player.getPA().viewingOtherBank
				|| player.isDead || player.viewingLootBag || player.addingItemsToLootBag) {
			return false;
		}
		if (player.getBankPin().requiresUnlock()) {
			player.getBankPin().open(2);
			return false;
		}
		if (player.getTutorial().isActive()) {
			player.getTutorial().refresh();
			return false;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			player.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(player).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return false;
		}
		if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
			player.sendMessage("You must decline the trade to start walking.");
			return false;
		}
		if (player.isStuck) {
			player.isStuck = false;
			player.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
			return false;
		}
		return true;
	}

}
