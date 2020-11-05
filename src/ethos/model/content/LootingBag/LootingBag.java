package ethos.model.content.LootingBag;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import ethos.Server;
import ethos.model.entity.Entity;
import ethos.model.items.GameItem;
import ethos.model.items.ItemDefinition;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.players.Player;
import ethos.model.players.PlayerSave;

/**
 * Looting bag functionality.
 *
 * @author Sky
 * @Author trees (Fixed)
 */
public class LootingBag {

    public Player player;
    public List<LootingBagItem> items;

    public int selectedItem = -1;
    public int selectedSlot = -1;

    public LootingBag(Player player) {
        this.player = player;
        items = new ArrayList<>();
    }

    /**
     * Checks wether or not a player is allowed to configure the looting bag
     *
     * @return
     */
    public boolean configurationPermitted() {
        if (/*player.underAttackBy > 0 || player.underAttackBy2 > 0 ||*/ player.inDuelArena() || player.inPcGame()
                || player.inPcBoat() || player.isInJail() || player.getInterfaceEvent().isActive()
                || player.getPA().viewingOtherBank || player.isDead || player.viewingRunePouch) {
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
        DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
                MultiplayerSessionType.DUEL);
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

    /**
     * Configuring the inventory of the looting bag on death
     *
     * @param player
     */
    public void onDeath(Player player, String entity) {
        Entity killer = player.getKiller();

        for (Iterator<LootingBagItem> iterator = items.iterator(); iterator.hasNext(); ) {
            LootingBagItem item = iterator.next();

            if (item == null) {
                continue;
            }
            if (item.getId() <= 0 || item.getAmount() <= 0) {
                continue;
            }
            if (Objects.equals(entity, "PVP")) {
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
        sendItems();
        PlayerSave.saveGame(player);
    }

	/*
     * Calculating the price of the loot bag
	 */

    public void updateTotalCost() {
        int total = 0;
        for (LootingBagItem item : items) {
            if (item == null) {
                continue;
            }
            if (item.getId() <= 0 || item.getAmount() <= 0) {
                continue;
            }
            if (ItemDefinition.forId(item.getId()) != null) {
                if (player.debugMessage)
                    player.sendMessage("Name: " + ItemDefinition.forId(item.getId()).getName() + "- Value:" + ItemDefinition.forId(item.getId()).getValue() + " - Amount:" + item.getAmount());
                total += ((ItemDefinition.forId(item.getId()).getValue() * item.getAmount()));
            }
        }
        DecimalFormatSymbols separator = new DecimalFormatSymbols();
        separator.setGroupingSeparator(',');
        DecimalFormat formatter = new DecimalFormat("##,###,###", separator);
        player.getPA().sendFrame126("Value: " + formatter.format(total), 39348);
    }

    /**
     * The looting bag id
     */
    public static final int LOOTING_BAG = 11941;

    public static boolean isLootingBag(Player player, int itemId) {
        return itemId == LOOTING_BAG;
    }

    public boolean handleButton(int buttonId) {
        if (buttonId == 153177) {
            closeLootbag();
            return true;
        } else if (buttonId == 153179) {
            depositAllLootBag();
            return true;
        }
        return false;
    }

    public void depositAllLootBag() {
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (!player.Area(1592, 1670, 3659, 3696) && !player.inBank() || player.inWild()) {
            player.sendMessage("You must be at home to do this.");
            return;
        }
        if (System.currentTimeMillis() - player.lastBankDeposit < 1000)
            return;
        player.lastBankDeposit = System.currentTimeMillis();
        if (items.isEmpty()) {
            player.sendMessage("You don't have any items in your lootbag.");
            return;
        }
        for (Iterator<LootingBagItem> iterator = items.iterator(); iterator.hasNext(); ) {
            LootingBagItem item = iterator.next();
            if (item == null) {
                continue;
            }
            if (item.getId() <= 0 || item.getAmount() <= 0) {
                continue;
            }
            player.getItems().addItemToBank(item.getId(), item.getAmount());
            //player.sendMessage("Completed.");
            iterator.remove();
        }
        updateTotalCost();
        sendItems();
    }

    /**
     * Opens the check looting bag interface
     */
    public void openLootbag() {
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (items.isEmpty()) {
            player.sendMessage("You don't have any items in your lootbag.");
            return;
        }
        onClose();
        sendItems();
        updateTotalCost();
        player.setSidebarInterface(3, 39342);
        player.viewingLootBag = true;
    }

    /**
     * Opens the deposit looting bag interface
     */
    public void openLootbagAdd() {
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (player.inClanWars() || player.inClanWarsSafe()) {
            return;
        }
        if (!player.inWild()) {
            player.sendMessage("You can only do this in the wilderness.");
            return;
        }
        onClose();
        //sendInventoryItems();
        sendItems();
        player.setSidebarInterface(3, 39443);
        player.addingItemsToLootBag = true;
    }

    /**
     * Handles deposit and withdrawal of items
     *
     * @param id     The item being configured
     * @param amount The amount of the item being configured
     * @return
     */
    public boolean handleClickItem(int id, int amount) {
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return false;
        }

        if (player.viewingLootBag) {
            removeMultipleItemsFromBag(id, amount);
            //withdraw(id, amount);
            return true;
        }
        if (player.addingItemsToLootBag) {
            deposit(id, amount);
            return true;
        }
        return false;
    }

    public void removeMultipleItemsFromBag(int id, int amount) {
        if (amount >= Integer.MAX_VALUE) {
            amount = countItems(id);
        }
        int count = 0;
        while (containsItem(id)) {
            if (!withdraw(id, amount)) {
                return;
            }
            if (player.getItems().isStackable(id)) {
                count += amount;
            } else {
                count++;
            }
            if (count >= amount) {
                return;
            }
        }
    }

    public boolean containsItem(int id) {
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public int findIndexInLootBag(int id) {
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                return items.indexOf(item);
            }
        }
        return -1;
    }

    /**
     * Handles withdrawal from the lootingbag
     *
     * @param id     The id of the item being withdrawn
     * @param amount The amount of the item being withdrawn
     */
    public boolean withdraw(int id, int amount) {
        int index = findIndexInLootBag(id);
        int amountToAdd = 0;
        if (items.size() <= 0) {
            return false;
        }
        if (index == -1) {
            return false;
        }
        LootingBagItem item = items.get(index);
        if (item == null) {
            return false;
        }
        if (item.getId() <= 0 || item.getAmount() <= 0 || player.getItems().freeSlots() <= 0) {
            return false;
        }
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return false;
        }
        if (!player.Area(1592, 1670, 3659, 3696) && !player.inBank() || player.inWild()) {
            player.sendMessage("You must be at home to do this.");
            return false;
        }
        if (player.getItems().getItemCount(id, false) + amount >= Integer.MAX_VALUE || player.getItems().getItemCount(id, false) + amount <= 0) {
            return false;
        }
        if ((items.get(items.indexOf(item)).getAmount()) > amount) {
            amountToAdd = amount;
            items.get(items.indexOf(item)).incrementAmount(-amount);
        } else {
            amountToAdd = item.getAmount();
            items.remove(index);
        }
        player.getItems().addItem(item.getId(), amountToAdd);
        sendItems();
        //sendInventoryItems();
        updateTotalCost();
        return true;
    }

    /**
     * Item allowed check
     * @param item
     * @return
     */
    public boolean allowedInBag(int item) {
        switch (item) {
            case LOOTING_BAG:
            case 11942:
                player.sendMessage("You may be surprised to learn that bagception is not permitted.");
                return false;
            default:
                if (!ItemDefinition.forId(item).isTradable()) {
                    player.sendMessage("This item is deemed untradable and cannot be put into the bag.");
                    return false;
                }
                return true;
        }
    }

    /**
     * Handles depositing of items into the looting bag
     *
     * @param id     The id of the item being deposited
     * @param amount The amount of the item being deposited
     */
    public void deposit(int id, int amount) {
        if (!allowedInBag(id)) {
            return;
        }
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (amount >= Integer.MAX_VALUE) {
            amount = player.getItems().getItemCount(id, false);
        }
        if (player.inClanWars() || player.inClanWarsSafe()) {
            return;
        }
        int bagSpotsLeft = 28 - items.size();
        boolean stackable = player.getItems().isStackable(id);
        boolean bagContainsItem = containsItem(id);
        if (amount > bagSpotsLeft) {
            if (!(stackable && bagContainsItem) && !stackable) {
                amount = bagSpotsLeft;
            }
        }
        if (!player.getItems().playerHasItem(id)) {
            return;
        }
        if (!player.inWild()) {
            player.sendMessage("You can only do this in the wilderness.");
            return;
        }
        if (items.size() >= 28 && !(stackable && bagContainsItem)) {
            player.sendMessage("The bag cannot hold anymore items.");
            return;
        }
        if (countItems(id) + amount >= Integer.MAX_VALUE || countItems(id) + amount <= 0) {
            return;
        }
        List<Integer> amounts = player.getItems().deleteItemAndReturnAmount(id, amount);

        int count = 0;
        for (int amt : amounts) {
            if (!addItemToList(id, amt)) {
                break;
            }
            count++;
            if (count >= amount) {
                break;
            }
        }

        resetItems();
        sendItems();
        //sendInventoryItems();
    }

    public int countItems(int id) {
        int count = 0;
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                count += item.getAmount();
            }
        }
        return count;
    }

    public boolean addItemToList(int id, int amount) {
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                if (item.getAmount() + amount >= Integer.MAX_VALUE) {
                    return false;
                }
                if (player.getItems().isStackable(id)) {
                    item.incrementAmount(amount);
                    return true;
                }
            }
        }
        items.add(new LootingBagItem(id, amount));
        return true;
    }

    /**
     * Closing the looting bag and resetting
     */
    public void closeLootbag() {
        player.setSidebarInterface(3, 3213);
        player.viewingLootBag = false;
        player.addingItemsToLootBag = false;
        onClose();
    }

    /**
     * Opens withdrawal mode
     */
    public void openWithdrawalMode() {
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (!player.getItems().playerHasItem(LOOTING_BAG)) {
            return;
        }
        openLootbag();
    }

    public void onClose() {
        player.viewingLootBag = false;
        player.addingItemsToLootBag = false;
    }

    /**
     * Opens deposit mode
     */
    public void openDepositMode() {
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (!player.getItems().playerHasItem(LOOTING_BAG)) {
            return;
        }
        openLootbagAdd();
    }

    List<GameItem> loot = new ArrayList<GameItem>();

    public void sendItems() {
        if (!player.getItems().playerHasItem(LOOTING_BAG)) {
            return;
        }
        loot.clear();
        for (int i = 0; i < 28; i++) {
            if (i < items.size()) {
                LootingBagItem item = items.get(i);
                if (item != null) {
                    loot.add(new GameItem(item.getId(), item.getAmount()));
                }
            }
            player.getPA().sendItems(player, 39349, loot, 28);
        }
    }

    public void sendInventoryItems() {
        if (!player.getItems().playerHasItem(LOOTING_BAG)) {
            return;
        }
        final int START_ITEM_INTERFACE = 27342;
        for (int i = 0; i < 28; i++) {
            int id = 0;
            int amt = 0;

            if (i < player.playerItems.length) {
                id = player.playerItems[i];
                amt = player.playerItemsN[i];
            }

            player.getPA().sendFrame34a(START_ITEM_INTERFACE + i, id - 1, 0, amt);
        }
    }

    @SuppressWarnings("unused")
    private String getShortAmount(int amount) {
        if (amount <= 1) {
            return "";
        }
        String amountToString = "" + amount;
        if (amount > 1000000000) {
            amountToString = "@gre@" + (amount / 1000000000) + "B";
        } else if (amount > 1000000) {
            amountToString = "@gre@" + (amount / 1000000) + "M";
        } else if (amount > 1000) {
            amountToString = "@whi@" + (amount / 1000) + "K";
        }
        return amountToString;
    }

    private void resetItems() {
        player.getItems().resetItems(3214);
        player.getPA().requestUpdates();
    }
}
