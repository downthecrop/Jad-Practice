package ethos.model.players.packets;

import java.util.Objects;

import ethos.Server;
import ethos.model.content.tradingpost.Listing;
import ethos.model.content.tradingpost.Sale;
import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.bank.BankItem;
import ethos.model.multiplayer_session.MultiplayerSession;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.trade.TradeSession;
import ethos.model.players.PacketType;
import ethos.model.players.Player;

/**
 * Bank All Items
 **/
public class BankAll implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int removeSlot = c.getInStream().readUnsignedWordA();
		int interfaceId = c.getInStream().readUnsignedWord();
		int removeId = c.getInStream().readUnsignedWordA();
		if (c.debugMessage)
			c.sendMessage("Bank All: interfaceid: "+interfaceId+", removeSlot: "+removeSlot+", removeID: " + removeId);
		
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (c.viewingLootBag || c.addingItemsToLootBag) {
			if (c.getLootingBag().handleClickItem(removeId, c.getItems().getItemAmount(removeId))) {
				return;
			}
		}
		if (c.viewingRunePouch) {
			if (c.getRunePouch().handleClickItem(removeId, c.getItems().getItemAmount(removeId), interfaceId)) {
				return;
			}
		}
		switch (interfaceId) {
		
		case 48021:
			Sale sales = Listing.getSale(c.saleResults.get(removeSlot));
			
			Listing.buyListing(c, removeSlot, sales.getQuantity() - sales.getTotalSold());
		break;
	
		case 48500: //Listing interface
			if(c.isListing) {
				Listing.openSelectedItem(c, removeId, c.getItems().getItemAmount(removeId), 0);
			}
		break;
		
		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;
			
		case 64016:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;

		case 5064:
			if (c.inTrade) {
				return;
			}
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
					&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
				c.sendMessage("You have declined the duel.");
				duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId, c.getItems().getItemAmount(removeId), true);
			}
			if (c.inSafeBox) {
				if (!c.pkDistrict && removeId != 13307) {
					c.sendMessage("You cannot do this right now.");
					return;
				}
				c.getSafeBox().deposit(removeId, c.getItems().getItemAmount(removeId));
			}
			break;

		case 5382:
			if (!c.isBanking) {
				return;
			}
			if (c.getItems().freeSlots() == 0 && !c.getItems().playerHasItem(removeId)) {
				c.sendMessage("There is not enough space in your inventory.");
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(removeId, c.getBank().getCurrentBankTab().getItemAmount(new BankItem(removeId + 1)));
				return;
			}
			c.getItems().removeFromBank(removeId, c.getBank().getCurrentBankTab().getItemAmount(new BankItem(removeId + 1)), true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession || session instanceof DuelSession) {
				session.addItem(c, new GameItem(removeId, c.getItems().getItemAmount(removeId)));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, Integer.MAX_VALUE));
			}
			break;

		case 6669:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof DuelSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, Integer.MAX_VALUE));
			}
			break;

		case 7295:
			if (Item.itemStackable[removeId]) {
				c.getItems().addToBank(c.playerItems[removeSlot], c.playerItemsN[removeSlot], false);
				c.getItems().resetItems(7423);
			} else {
				c.getItems().addToBank(c.playerItems[removeSlot], c.getItems().itemAmount(c.playerItems[removeSlot]), false);
				c.getItems().resetItems(7423);
			}
			break;

		}
	}

}
