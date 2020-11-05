package ethos.model.items.bank;

import java.util.ArrayList;
import java.util.Collection;

import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;

public class BankSearch {

	String text;
	Player player;
	BankTab tab = new BankTab();
	boolean searching;

	public BankSearch(Player player) {
		this.player = player;
	}

	public void updateItems() {
		tab.bankItems.clear();
		Collection<BankItem> items = new ArrayList<>();
		for (BankTab bankTab : player.getBank().getBankTab()) {
			for (BankItem item : bankTab.getItems()) {
				if (item.getAmount() >= 0) {
					if (ItemAssistant.getItemName(item.getId() - 1).toLowerCase().contains(text.toLowerCase())) {
						items.add(item);
					}
				}
			}
		}

		tab.bankItems.addAll(items);
	}

	public void removeItem(int itemId, int amount) {
		if (!tab.contains(new BankItem(itemId + 1, amount))) {
			return;
		}
		for (BankTab tab : player.getBank().getBankTab()) {
			for (BankItem bankItem : tab.getItems()) {
				if (itemId == bankItem.getId() - 1) {
					player.getBank().setCurrentBankTab(tab);
					player.getItems().removeFromBank(itemId, amount, false);
					break;
				}
			}
		}
		player.getBank().setCurrentBankTab(tab);
		updateItems();
		player.getItems().resetBank();
	}

	public void reset() {
		if (player.getBank().getBankSearch().isSearching()) {
			player.getBank().setCurrentBankTab(player.getBank().getBankTab(0));
			player.getItems().resetBank();
			player.sendMessage("Search results reset.");
			player.getPA().sendFrame126("", 58063);
			searching = false;
		}
	}

	public void reset(int tabId) {
		if (player.getBank().getBankSearch().isSearching()) {
			player.getBank().setCurrentBankTab(player.getBank().getBankTab(tabId));
			player.getItems().resetBank();
			player.sendMessage("Search results reset.");
			player.getPA().sendFrame126("", 58063);
			searching = false;
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public BankTab getTab() {
		return this.tab;
	}

	public boolean isSearching() {
		return this.searching;
	}

	public boolean setSearching(boolean searching) {
		return this.searching = searching;
	}

}
