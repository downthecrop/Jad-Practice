package ethos.model.players.packets;

import java.util.Objects;

import ethos.Server;
import ethos.model.content.LootValue;
import ethos.model.content.tradingpost.Listing;
import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.UseItem;
import ethos.model.items.bank.BankItem;
import ethos.model.multiplayer_session.MultiplayerSession;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.trade.TradeSession;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.skills.TabletCreation;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int Xamount = c.getInStream().readInteger();
		
		if (c.debugMessage)
			c.sendMessage("Bank X: amount: "+Xamount+", removeId: "+c.xRemoveId+", item: " + c.item);
		
		if (Xamount < 0) {
			Xamount = c.getItems().getItemAmount(c.xRemoveId);
		}
		if (Xamount == 0) {
			Xamount = 1;
		}
		if (Xamount > Integer.MAX_VALUE) {
			Xamount = 1;
		}
		
		if (c.buyingX) {
			int amount = Xamount;
			if(c.inWild() || c.inClanWars()) { //Fix wildy resource zone here inwild() && !inwildyzone
				return;
			}
			if (amount > 10000) {
				c.sendMessage("You can only buy 10,000 items at a time.");
				amount = 10000;
			}
			c.getShops().buyItem(c.xRemoveId, c.xRemoveSlot, amount);// buy X
            c.xRemoveSlot = 0;
            c.xInterfaceId = 0;
            c.xRemoveId = 0;
			c.buyingX = false;
            //return;
		}
		if (c.sellingX) {
			int amount = Xamount;
	    	if(c.inWild() || c.inClanWars()) {
				return;
			}
	    	c.getShops().sellItem(c.xRemoveId, c.xRemoveSlot, amount);// sell X
	        c.xRemoveSlot = 0;
	        c.xInterfaceId = 0;
	        c.xRemoveId = 0;
			c.sellingX = false;
	        //return;
		}
		final int amount2 = Xamount;
		c.getFletching().getSelectedFletchable().ifPresent(fletchable -> {
			c.getFletching().fletchLog(fletchable, amount2);
			return;
		});
		if (c.viewingLootBag || c.addingItemsToLootBag) {
			if (c.getLootingBag().handleClickItem(c.getLootingBag().selectedItem, Xamount)) {
				return;
			}
		}
		if (c.viewingRunePouch) {
			if (c.getRunePouch().handleClickItem(c.getRunePouch().selectedItem, Xamount,
					c.getRunePouch().interfaceId)) {
				return;
			}
		}

		switch (c.xInterfaceId) {
		
		case 48021:
			Listing.buyListing(c, c.xRemoveSlot, Xamount);
		break;
	
		case 191072:
			if(c.isListing) {
				if (c.debugMessage)
					c.sendMessage("Ting");
				Listing.openSelectedItem(c, c.item, c.quantity, Xamount);
			}
		break;
		
		case 191075: // This was removed
			if(c.isListing) {
				if (c.debugMessage)
					c.sendMessage("Tong");
				if(!Item.itemIsNote[c.xRemoveId]) {
				if(Xamount > c.getItems().getItemCount(c.item))
					Xamount = c.getItems().getItemCount(c.item);
				} else {
					if(Xamount > c.getItems().getItemAmount(c.xRemoveId))
						Xamount = c.getItems().getItemAmount(c.xRemoveId);
				}
				Listing.openSelectedItem(c, c.item, Xamount, c.price);
			}
		break;
	
		case 48500:
			if(c.isListing) {
				if (c.debugMessage)
					c.sendMessage("Bong");
				if(Xamount > c.getItems().getItemAmount(c.xRemoveId))
					Xamount = c.getItems().getItemAmount(c.xRemoveId);
				Listing.openSelectedItem(c, c.xRemoveId, Xamount, 0);
			}
		break;

		case 5064:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				c.sendMessage("You cannot bank items whilst trading.");
				return;
			}
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount))
				return;
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
					&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
				c.sendMessage("You have declined the duel.");
				duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(c.playerItems[c.xRemoveSlot] - 1, Xamount, true);
			}
			if (c.inSafeBox) {
				if (!c.pkDistrict) {
					c.sendMessage("You cannot do this right now.");
					return;
				}
				c.getSafeBox().deposit(c.playerItems[c.xRemoveSlot] -1, Xamount);
			}
			break;
		case 5382:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				c.sendMessage("You cannot remove items from the bank whilst trading.");
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				BankItem item = c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot);
				if (item != null) {
					c.getBank().getBankSearch().removeItem(item.getId() - 1, Xamount);
				}
				return;
			}
			if (c.XremoveSlot > c.getBank().getCurrentBankTab().size() - 1) {
				return;
			}
			BankItem item = c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot);
			if (item != null) {
				c.getItems().removeFromBank(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot).getId() - 1, Xamount, true);
			}
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession || session instanceof DuelSession) {
				session.addItem(c, new GameItem(c.xRemoveId, Xamount));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, c.xRemoveSlot, new GameItem(c.xRemoveId, Xamount));
			}
			break;

		case 6669:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof DuelSession) {
				session.removeItem(c, c.xRemoveSlot, new GameItem(c.xRemoveId, Xamount));
			}
			break;
		}
		if (c.settingMin) {
			if (Xamount < 0 || Xamount > Integer.MAX_VALUE)
				return;
			c.diceMin = Xamount;
			c.settingMin = false;
			c.settingMax = true;
			c.getDH().sendDialogues(9998, -1);
			return;
		} else if (c.settingMax) {
			if (Xamount < 0 || Xamount > Integer.MAX_VALUE)
				return;
			c.diceMax = Xamount;
			c.settingMax = false;
			c.settingMin = false;
			c.getDH().sendDialogues(9999, -1);
			return;
		} else if (c.settingBet) {
			Player o = c.diceHost;
			if (Xamount < 0 || Xamount > Integer.MAX_VALUE)
				return;
			if (!c.getItems().playerHasItem(2996, Xamount)) {
				c.sendMessage("You need more tickets!");
				o.sendMessage("The other player needs more tickets.");
				c.getPA().removeAllWindows();
				o.getPA().removeAllWindows();
				return;
			}
			if (!o.getItems().playerHasItem(2996, Xamount)) {
				c.sendMessage("The other player needs more tickets.");
				o.sendMessage("You need more tickets!");
				c.getPA().removeAllWindows();
				o.getPA().removeAllWindows();
				return;
			}
			if (Xamount < 0 || Xamount > Integer.MAX_VALUE)
				return;
			if (Xamount > o.diceMax || Xamount < o.diceMin) {
				c.sendMessage("That bet is too big or too small.");
				c.sendMessage("Please bet between " + o.diceMin + " and " + o.diceMax);
				c.getPA().removeAllWindows();
				o.getPA().removeAllWindows();
				return;
			}
			c.betAmount = Xamount;
			c.getItems().deleteItem(2996, Xamount);
			o.getItems().deleteItem(2996, Xamount);
			PlayerHandler.players[c.otherDiceId].betAmount = Xamount;
			c.settingBet = false;
			c.settingMax = false;
			c.settingMin = false;
			c.getDH().sendDialogues(11002, -1);
			return;
		}
		if (c.attackSkill) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot change levels here.");
				return;
			}
			if (!c.inClanWarsSafe()) {
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 0;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
				c.getPA().requestUpdates();
				c.attackSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.defenceSkill) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot change levels here.");
				return;
			}
			if (!c.inClanWarsSafe()) {
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 1;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getCombat().resetPrayers();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
				c.getPA().requestUpdates();
				c.defenceSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.strengthSkill) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot change levels here.");
				return;
			}
			if (!c.inClanWarsSafe()) {
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 2;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
				c.getPA().requestUpdates();
				c.strengthSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.healthSkill) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot change levels here.");
				return;
			}
			if (!c.inClanWarsSafe()) {
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 3;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
				c.getPA().requestUpdates();
				c.healthSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.rangeSkill) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot change levels here.");
				return;
			}
			if (!c.inClanWarsSafe()) {
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 4;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
				c.getPA().requestUpdates();
				c.rangeSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.prayerSkill) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot change levels here.");
				return;
			}
			if (!c.inClanWarsSafe()) {
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				c.getCombat().resetPrayers();
				int skill = 5;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
				c.getPA().requestUpdates();
				c.prayerSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.mageSkill) {
			if (c.inWild() || c.inDuelArena()) {
				c.sendMessage("You cannot change levels here.");
				return;
			}
			if (!c.inClanWarsSafe()) {
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 6;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
				c.getPA().requestUpdates();
				c.mageSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.boneOnAltar) {
			if (c.getPrayer().getAltarBone().isPresent()) {
				c.getPrayer().alter(Xamount);
				return;
			}
		}
		if (c.settingLootValue) {
			LootValue.configureValue(c, "setvalue", Xamount);
		}
		if (c.settingUnnoteAmount) {
			if (Xamount < 1) {
				UseItem.unNoteItems(c, c.unNoteItemId, 1);
			} else {
				UseItem.unNoteItems(c, c.unNoteItemId, Xamount);
			}
		}
		switch (c.tablet) {
		case 1:
			c.getPA().closeAllWindows();
			c.tablet = 0;
			if (Xamount > 100) {
				c.sendMessage("You may only create 100 at a time.");
				return;
			}

			try {
				TabletCreation.createTablet(c, 0, Xamount);
			} catch (Exception e) {
			}
			break;
		case 2:
			c.getPA().closeAllWindows();
			c.tablet = 0;
			if (Xamount > 100) {
				c.sendMessage("You may only create 100 at a time.");
				return;
			}
			try {
				TabletCreation.createTablet(c, 1, Xamount);
			} catch (Exception e) {
			}
			break;
			
		case 3:
			c.sendMessage("This");
			break;
		}
	}
}