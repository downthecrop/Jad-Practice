package ethos.model.shops;

import java.util.stream.IntStream;

import ethos.Config;
import ethos.Server;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.content.wogw.Wogwitems;
import ethos.model.holiday.HolidayController;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.items.ItemList;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;
import ethos.util.Misc;
import ethos.world.ShopHandler;

public class ShopAssistant {

	private Player c;

	public ShopAssistant(Player client) {
		this.c = client;
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (!c.getMode().isShopAccessible(ShopID)) {
			c.sendMessage("Your game mode does not permit you to access this shop.");
			c.getPA().closeAllWindows();
			return;
		}
		if (c.viewingLootBag || c.addingItemsToLootBag || c.viewingRunePouch) {
			c.sendMessage("You should stop what you are doing before opening a shop.");
			return;
		}
		c.nextChat = 0;
		c.dialogueOptions = 0;
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getPA().sendFrame248(64000, 3822);
		
		switch (ShopID) {
		
		/*case 82:
		c.getPA().sendFrame126(ShopHandler.ShopName[ShopID] + " - " + c.getShayPoints() + " Assault Points", 64003);
			break;*/
		
		default: c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 64003);
		}
		//c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 64003); //3901 (Possibly item container)
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true && PlayerHandler.players[i].myShopId == c.myShopId && i != c.getIndex()) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public void resetShop(int ShopID) {
		// synchronized (c) {
		int TotalItems = 0;
		for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0) {
				TotalItems++;
			}
		}
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		if (ShopID == 80) {
			c.getPA().sendFrame171(0, 64017);
			c.getPA().sendFrame126("Bounties: " + Misc.insertCommas(Integer.toString(c.getBH().getBounties())), 64019);
		} else {
			c.getPA().sendFrame171(1, 64017);
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(64016);
		c.getOutStream().writeWord(TotalItems);
		int TotalCount = 0;
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0 || i <= ShopHandler.ShopItemsStandard[ShopID]) {
				if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(ShopHandler.ShopItemsN[ShopID][i]);
				} else {
					c.getOutStream().writeByte(ShopHandler.ShopItemsN[ShopID][i]);
				}
				if (ShopHandler.ShopItems[ShopID][i] > Config.ITEM_LIMIT || ShopHandler.ShopItems[ShopID][i] < 0) {
					ShopHandler.ShopItems[ShopID][i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(ShopHandler.ShopItems[ShopID][i]);
				TotalCount++;
			}
			if (TotalCount > TotalItems) {
				break;
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public double getItemShopValue(int ItemID, int Type, int fromSlot) {
		double ShopValue = 1;
		double TotPrice = 0;

		ItemList itemList = Server.itemHandler.ItemList[ItemID];

		if (itemList != null) {
			ShopValue = itemList.ShopValue;
		}

		TotPrice = ShopValue;

		if (ShopHandler.ShopBModifier[c.myShopId] == 1) {
			TotPrice *= 1;
			TotPrice *= 1;
			if (Type == 1) {
				TotPrice *= 1;
			}
		} else if (Type == 1) {
			TotPrice *= 1;
		}
		return TotPrice;
	}

	public static int getItemShopValue(int itemId) {
		if (itemId < 0) {
			return 0;
		}

		ItemList itemList = Server.itemHandler.ItemList[itemId];

		if (itemList != null) {
			return (int) itemList.ShopValue;
		}

		return 0;
	}

	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0, removeSlot));
		ShopValue *= 1.00;
		ShopValue = c.getMode().getModifiedShopPrice(c.myShopId, removeId, ShopValue);
		String ShopAdd = "";
		
		if (c.myShopId == 40) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " mage arena points.");
			return;
		}
		if (c.myShopId == 83) {
			c.sendMessage("You cannot buy items from this shop.");
			return;
		}
		if (c.myShopId == 44) {
			if (Item.getItemName(removeId).contains("head")) {
				c.sendMessage("This product cannot be purchased.");
				return;
			}
		}
		if (c.myShopId == 82) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " Assault points.");
			return;
		}
		if (c.myShopId == 118) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " Raid points.");
			return;
		}
		if (c.myShopId == 12) {
			switch (removeId) {
			case 3144:
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " pk points. (x100 karambwans)");
				break;
				
			case 2289:
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " pk points. (x50 plain pizza)");
				break;
			
			default:
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @red@" + getSpecialItemValue(removeId) + " @bla@] PK Points.");
				break;
			}
			return;
		}
		if (c.myShopId == 44) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " slayer points.");
			return;
		}
		if (c.myShopId == 13) {
			c.sendMessage("Jossik will switch " + ItemAssistant.getItemName(removeId) + " for " + getSpecialItemValue(removeId) + " rusty casket.");
			return;
		}
		if (c.myShopId == 80) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + " currently costs " + Misc.insertCommas(Integer.toString(getBountyHunterItemCost(removeId))) + " bounties.");
			return;
		}
		if (c.myShopId == 10) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @lre@" + getSpecialItemValue(removeId) + " @bla@] Slayer Points.");
			return;
		}
		if (c.myShopId == 120) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " prestige points.");
			return;
		}
		if (c.myShopId == 77) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Vote points.");
			return;
		}
		if (c.myShopId == 119) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @pur@" + getSpecialItemValue(removeId) + " @bla@] Blood Money points.");
			return;
		}
		if (c.myShopId == 78) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [@lre@" + getSpecialItemValue(removeId) + " @bla@] Achievement Points.");
			return;
		}
		if (c.myShopId == 75) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [@gre@ " + getSpecialItemValue(removeId) + " @bla@] PC points.");
			return;
		}
		if (c.myShopId == 9) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs [ @blu@" + getSpecialItemValue(removeId) + " @bla@ ] Trading Sticks.");
			return;
		}
		if (c.myShopId == 18) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " marks of grace.");
			return;
		}
		if (c.myShopId == 115) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": is completeley free.");
			return;
		}
		if (c.myShopId == 116) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " blood money.");
			return;
		}
		if (c.myShopId == 117) {
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId) + " blood money.");
			return;
		}
		if (c.myShopId == 29) {
			if (c.getRechargeItems().hasItem(11136)) {
				ShopValue = (int) (ShopValue * 0.95);
			}
			if (c.getRechargeItems().hasItem(11138)) {
				ShopValue = (int) (ShopValue * 0.9);
			}
			if (c.getRechargeItems().hasItem(11140)) {
				ShopValue = (int) (ShopValue * 0.85);
			}
			if (c.getRechargeItems().hasItem(13103)) {
				ShopValue = (int) (ShopValue * 0.75);
			}
			c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + ShopValue + " tokkul" + ShopAdd);
			return;
		}
		if (c.myShopId == 15) {
			c.sendMessage("This item current costs " + c.getItems().getUntradePrice(removeId) + " coins.");
			return;
		}
		if (c.myShopId == 79) {
			c.sendMessage("This item current costs 500,000 coins.");
			return;
		}
		if (ShopValue >= 1000 && ShopValue < 1000000) {
			ShopAdd = " (" + (ShopValue / 1000) + "K)";
		} else if (ShopValue >= 1000000) {
			ShopAdd = " (" + (ShopValue / 1000000) + " million)";
		}
		c.sendMessage(ItemAssistant.getItemName(removeId) + ": currently costs " + ShopValue + " coins" + ShopAdd);
	}

	public int getBountyHunterItemCost(int itemId) {
		switch (itemId) {
		case 11941:
			return 150_000;
		case 12783:
			return 10_000_000;
		case 12791:
			return 1_200_000;
		case 12804:
			return 25_000_000;
		case 12851:
			return 10_000_000;
		case 12855:
		case 12856:
			return 2_500_000;
		case 12833:
			return 50_000_000;
		case 12831:
			return 35_000_000;
		case 12829:
			return 25_000_000;
		case 14484:
			return 125_000_000;

		case 12800:
		case 12802:
			return 350_000;
		case 12786:
			return 100_000;
		case 10926:
			return 2_500;
		case 12846:
			return 8_000_000;
		case 12420:
		case 12421:
		case 12419:
		case 12457:
		case 12458:
		case 12459:
			return 10_000_000;
		case 12757:
		case 12759:
		case 12761:
		case 12763:
		case 12769:
		case 12771:
			return 500_000;
		case 12526:
			return 1_500_000;
		case 12849:
		case 12798:
			return 250_000;
		case 12608:
		case 12610:
		case 12612:
			return 350_000;
		case 12775:
		case 12776:
		case 12777:
		case 12778:
		case 12779:
		case 12780:
		case 12781:
		case 12782:
			return 5_000;

		default:
			return Integer.MAX_VALUE;
		}
	}

	public int getSpecialItemValue(int id) {
		
		switch (c.myShopId) {
			case 120:
				switch(id) {
					case 13317:
					case 13318:
					case 13319:
						return 100;
					case 13188:
						return 10;
					case 4079:
						return 25;
					case 1037:
						return 500;

				}
				break;
		case 118: //raids
			switch (id) {
			case 21009: //Dragon sword
				return 30000;
			case 21012: //Dragon hunter crossbow
				return 95000;
			case 21000: //Twisted buckler
				return 75000;
			case 11730: //Overload
				return 500;
			case 20849: //Dragon thrownaxe
				return 100;
			case 21003://Maul
			case 21015://Bulwark
				return 300000;
			case 21018://Ancestral
			case 21021:
			case 21024:
				return 150000;
			case 12785://Row
				return 30000;
			case 21043://Kodai
				return 65000;
			}
			break;
		case 82:
			switch (id) {
			case 10548:
				return 30;
			case 10551:
				return 100;
			case 11898:
			case 11897:
			case 11896:
				return 25;
			case 11899:
			case 11900:
				return 25;
			case 11937:
			case 11936:
				return 10;
			}
			break;
			
		case 77: //Vote Shop
			switch (id) {
			case 11936:
				return 3;
			case 11968:
				return 8;
			case 11920:
				return 140;
			case 12797:
				return 215;
			case 6739:
				return 50;
			case 4151:
				return 125;
			case 11235:
				return 100;
			case 6585:
				return 35;
			case 12526:
				return 50;
			case 20211:
			case 20214:
			case 20217:
				return 35;
			case 20050:
				return 40;
			case 13221:
				return 65;
			case 20756:
				return 85;
			case 21028:
				return 135;
			case 13241:
			case 13242:
			case 13243:
				return 200;
			case 15098:
				return 500;
			case 6666:
				return 15;
				
								
			}
			break;
			
		case 119: //Blood Money Shop
			switch(id) {
			case 13307:
				return 1;
			case 1526:
			case 222:
			case 236:
			case 224:
			case 9737:
			case 232:
			case 226:
			case 240:
			case 244:
			case 6050:
			case 6052:
			case 3139:
			case 6694:
			case 246:
			case 1976:
			case 2971:
			case 248:
			case 9437:
				return 1;
			case 7409:
				return 750;
			case 20708:
			case 20704:
			case 20706:
			case 20710:
			case 12013:
			case 12014:
			case 12015:
			case 12016:
			case 10941:
			case 10939:
			case 10940:
			case 10933:
			case 13258:
			case 13259:
			case 13260:
			case 13261:
			case 13646:
			case 13642:
			case 13640:
			case 13644:
				return 500;
			
			}
			break;
			
		case 12: //PK POINTS SHOP
			switch (id) {
			case 2996:
				return 1;
			case 13066:
				return 1;
			case 12006:
			case 12004:
				return 400;
			case 21003:
				return 1000;
			case 11802:
				return 600;
			case 11804:
			case 11806:
			case 11808:
				return 350;
			case 20784:
				return 650;
			case 13576:
				return 750;
			case 13267:
				return 450;
			case 13263:
				return 550;
			case 12765:
			case 12766:
			case 12768:
			case 12767:
				return 250;
			case 21012:
				return 750;
			case 11785:
				return 500;
			case 19481:
				return 500;
			case 12926:
				return 500;
			case 12817:
				return 800;
			case 12806:
			case 12807:
				return 445;
			case 21015:
				return 995;
			case 12924:
				return 650;
			}
			break;
			
			case 9: //Donator Point Shop
				switch (id) {
				case 13173:
					return 425;
				case 12399:
					return 45;
				case 11862:
					return 200;
				case 1038:
				case 1040:
				case 1042:
				case 1044:
				case 1046:
				case 1048:
					return 100;
				case 11863:
					return 500;
				case 1053:
				case 1055:
				case 1057:
					return 35;
				case 11847:
					return 200;
				case 13175:
					return 150;
				case 2697:
					return 10;
				case 2698:
					return 50;
				case 2699:
					return 150;
				case 2700:
					return 300;
				case 12785:
					return 30;
				case 1050:
					return 50;
				case 13343:
				case 13344:
					return 75;
				case 12006:
					return 15;
				case 13265:
					return 15;
				case 13263:
					return 17;
				case 11802:
					return 60;
				case 12809:
					return 12;
				case 12817:
					return 60;
				case 11286:
					return 10;
				case 12806:
				case 12807:
					return 10;
				case 11785:
					return 20;
				case 19481:
					return 10;
				case 12924:
					return 30;
				case 11235:
					return 8;
				case 11773:
					return 5;
				case 11770:
				case 11772:
				case 11771:
					return 3;
				case 12691:
				case 12692:
					return 6;
				case 6199:
					return 5;
				case 19710:
				case 19550:
				case 12853:
					return 5;
				case 6585:
					return 2;
				case 19553:
				case 19547:
					return 8;
				case 19544:
					return 4;
				case 13239:
				case 13235:
				case 13237:
					return 10;
				case 13233:
					return 9;
				case 11832:
				case 11834:
					return 15;
				case 11826:
				case 11828:
				case 11830:
					return 15;
				case 13346:
				return 15;
				}
				break;
		
		case 117:
			switch (id) {
			case 4716:
			case 4720:
			case 4722:
			case 4718: //Dharok
				return 120;
			case 4724:
			case 4726:
			case 4728:
			case 4730: //Guthan
			case 4745:
			case 4747:
			case 4749:
			case 4751: //Torag
			case 4753:
			case 4755:
			case 4757:
			case 4759: //Verac
				return 100;
			case 4708:
			case 4710:
			case 4712:
			case 4714: //Ahrim
			case 4732:
			case 4734:
			case 4736:
			case 4738: //Karil
				return 200;
			case 12006: //Abyssal tentacle
				return 400;
			case 13263: //Bludgeon
				return 3500;
			case 13271: //Abyssal dagger
				return 800;
			case 19481: //Ballista
				return 1500;
			case 12902: //Toxic staff
				return 1000;
			case 12924: //Blowpipe
				return 3000;
			case 11286: //Visage
				return 100;
			case 11785: //Armadyl crossbow
				return 2500;
			case 13227: //Crystals
			case 13229:
			case 13231:
			case 13233:
				return 1500;
			case 12695: //Super combat
				return 1;
			case 12929: //Serp helm
				return 1200;
			case 12831: //Blessed shield
				return 800;
			case 19529: //Zenyte shard
				return 1500;
			case 11832: //Bandos chest
			case 11834: //Bandos tassets
			case 11826: //Armadyl helm
			case 11828: //Armadyl chest
			case 11830: //Armadyl skirt
				return 700;
			case 6737: //Berseker ring
				return 500;
			case 6735: //Warrior ring
				return 50;
			case 6733: //Archers ring
				return 150;
			case 6731: //Seers ring
				return 150;
			case 12603: //Tyrannical ring
			case 12605: //Treasonous ring
				return 200;
			case 12853: //Amulet of the damned
				return 700;
			case 6585: //Fury
				return 150;
			case 11802: //Ags
				return 2000;
			case 11804: //Bgs
				return 200;
			case 11806: //Sgs
				return 1000;
			case 11808: //Zgs
				return 300;
			case 13576: //Dwh
				return 3000;
			case 11235: //Dbow
				return 150;
			case 11926: //Odium ward
			case 11924: //Malediction ward
				return 700;
			case 10551: //Torso
				return 150;
			case 10548: //Fighter hat
				return 100;
			case 11663: //Void mage helm
			case 11665: //Void melee helm
			case 11664: //Void ranger helm
			case 8842: //Void gloves
				return 50;
			case 8839: //Void top
			case 8840: //Void bottom
				return 75;
			}
			break;
			
		case 116:
			if (Item.getItemName(id).contains("dharok")) {
				return 20;
			}
			if (Item.getItemName(id).contains("guthan") || 
				Item.getItemName(id).contains("torag") || 
				Item.getItemName(id).contains("verac") || 
				Item.getItemName(id).contains("karil")) {
				return 12;
			}
			if (Item.getItemName(id).contains("ahrim")) {
				return 14;
			}
			
			switch (id) {
			case 12695: //Super combat
				return 5;
				
			case 12831: //Blessed spirit shield
				return 25;
				
			case 11772: //Warriors ring
			case 12692: //Treasonous ring
			case 12691: //Tyrannical ring
				return 50;
				
			case 12924: //Blowpipe
			case 11770: //Rings
			case 11771:
			case 11773:
			case 12851: //Amulet of damned
			case 12853: //Amulet of damned
				return 75;
				
			case 11235: //Dbow
				return 150;
				
			case 12929: //Serp helmets
			case 13196:
			case 13198:
			case 13235: //Cerb boots
			case 13237:
			case 13239:
			case 19553: //Amulet of torture
			case 19547: //Anguish
				return 250;
				
			case 12807: //Wards
			case 12806:
				return 300;
				
			case 11804: //Godswords
			case 11806:
			case 11808:
				return 500;
				
			case 12902: //Tsotd
			case 13271: //Abyssal dagger
				return 800;
				
			case 11802: //Armadyl godsword
			case 13576: //D warhammer
			case 13263: //Abyssal bludgeon
				return 1000;
				
			case 19481: //Heavy ballista
				return 1500;
				
			case 12821: //Spectral
				return 2000;
				
			case 12825: //Arcane
				return 2500;
				
			case 12817: //Elysian
				return 3500;
			}
			break;
		}
		
		switch (id) {
		/*
		 * Graceful store
		 */
		case 11850:
			return 35;
		case 11852:
			return 40;
		case 11854:
			return 55;
		case 11856:
			return 60;
		case 11858:
			return 30;
		case 11860:
			return 40;
		case 12792:
			return 15;
		case 12641:
			return 10;
		/*
		 * PK STORE
		 */
		case 11900:
			return 60;
		case 11899:
			return 70;
		case 11898:
			return 50;
		case 11896:
			return 80;
		case 11897:
			return 70;
		case 12829:
			return 120;
		case 2379:
		case 13066:
		case 2289:
			return 1;
		case 12746:
			return 20;
		case 7582:
		case 3144:
		case 4153:
			return 50;
		case 1052:
			return 40;
		case 4224:
			return 25;
		case 1249:
			return 300;
		case 2839:
			return 350;
		case 4202:
			return 200;
		case 6720:
			return 300;
		case 4081:
			return 250;
		case 3481:
		case 3483:
		case 3485:
		case 3486:
		case 3488:
		case 6856:
		case 6857:
		case 6858:
		case 6859:
		case 6860:
		case 6861:
		case 6862:
		case 6863:
		case 989:
			return 10;
		case 4333:
		case 4353:
		case 4373:
		case 4393:
		case 4413:
		case 11212:
			return 2;
		case 2996:
		case 1464:
			return 1;
		case 4170:
			return 50;
			
		case 12020:
			return 200;
			
		case 13226:
			return 350;
			
		case 10887:
			if (c.myShopId == 9)
				return 20;
			else
				return 200;
		case 8849:
			return 20;
		case 8848:
			return 20;
		case 8850:
		case 7398:
		case 7399:
		case 7400:
				return 40;
		case 8845:
			return 10;
		case 12751:
			return 500;
		case 7462:
			if (c.myShopId == 12)
				return 100;
			else
				return 12;
		case 2437:
		case 2441:
		case 2443:
			return 100;
		case 3025:
		case 6686:
			return 150;
		case 11937:
			return 250;
		case 7461:
			if (c.myShopId == 12)
				return 80;
			else
				return 8;
		case 7460:
			if (c.myShopId == 12)
				return 60;
			else
				return 6;
		case 7459:
			if (c.myShopId == 12)
				return 50;
			else
				return 5;
		case 12006:
			return 30;
		case 12000:
			return 20;
		case 12002:
			return 200;
		case 2677:
			return 15;
		case 2572:
				return 40;
		case 2722:
			return 10;
		case 12399:
			return 160;
		case 13887:
		case 13893:
			if (c.myShopId == 9)
				return 50;
			else
				return 280;
		case 13899:
			return 120;
		case 13902:
			return 110;
		case 13905:
			return 100;
		case 14484:
			return 400;
		case 13896:
		case 13884:
		case 13890:
			if (c.myShopId == 9)
				return 40;
			else
				return 250;
		case 13858:
		case 13861:
		case 13864:
			if (c.myShopId == 9)
				return 20;
			else
				return 130;
		case 13870:
		case 13873:
		case 13876:
			if (c.myShopId == 9)
				return 30;
			else
				return 130;
		case 10551:
		case 10548:
			if (c.myShopId == 12)
				return 150;
			else
				return 20;
		case 10550:
			return 80;
		case 7509:
				return 10;
		case 4168:
		case 4166:
		case 4551:
		case 4164:
			return 10;
		case 13116:
			return 180;
		case 536:
		case 537:
			return 1;
		case 6731:
		case 6735:
		case 6733:
		case 6737:
				return 20;
		case 9437:
			if (c.myShopId == 41)
				return 1000;
			else
				return 120;
			/*
			 * case 6916: case 6918: case 6920: case 6922: case 6924: return 75;
			 */
		case 3204:
			return 1000;
		case 6585:
		case 11840:
			if (c.myShopId == 12)
				return 20;
			else
				return 50;
		case 12905:
			return 1;
		case 2417:
		case 2415:
		case 2416:
			return 100;
		case 1409:
			return 150;
		case 3839:
		case 3841:
		case 3843:
			if (c.myShopId == 13)
				return 1;
			else
				return 50;
		case 6916:
		case 6918:
		case 6920:
		case 6922:
		case 6924:
			return 75;
		case 6570:
				return 60;
		case 11235:
			return 100;
		case 11785:
			if (c.myShopId == 9)
				return 30;
			else
				return 750;
		case 11791:
			if (c.myShopId == 9)
				return 30;
			else
				return 400;
		case 11061:
			if (c.myShopId == 9)
				return 50;
			else
				return 1000;
		case 11907:
			if (c.myShopId == 9)
				return 50;
			else
				return 500;
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			return 80;
		case 11739:
			return 1;
		case 6889:
			return 80;
		case 6914:
			return 200;
		case 4732:
		case 4734:
		case 4736:
		case 4738:
				return 12;
		case 4716:
		case 4718:
		case 4720:
		case 4722:
				return 20;
		case 4712:
		case 4710:
		case 4714:
		case 4708:
				return 14;
		case 4724:
		case 4726:
		case 4728:
		case 4730:
		case 4745:
		case 4747:
		case 4749:
		case 4751:
		case 4753:
		case 4755:
		case 4757:
		case 4759:
			return 12;
		case 10338:
		case 10342:
		case 10340:
		case 1989:
			return 500;
		case 11838:
		case 4151:
		case 1961:
			return 100;
		case 10352:
		case 10350:
		case 10348:
		case 10346:
			return 1000;
		case 11826:
			return 80;
		case 11828:
			return 100;
		case 11830:
			return 100;
		case 11283:
		case 1959:
		case 9703:
			if (c.myShopId == 9)
				return 50;
			else
				return 400;
		case 11802:
			if (c.myShopId == 9)
				return 50;
			else
				return 900;
		case 2581:
			return 40;
		case 2577:
			return 40;
		case 11832:
		case 11834:
			if (c.myShopId == 9)
				return 30;
			else
				return 500;
		case 11804:
			return 700;
		case 11808:
		case 11806:
			return 600;
		// DONATOR
		case 1042:
			return 140;
		case 1048:
			return 120;
		case 1038:
			return 100;
		case 1046:
			return 100;
		case 1044:
			return 90;
		case 1040:
			return 80;
		case 1053:
		case 1055:
		case 1057:
			return 60;
		case 1419:
			return 40;
		case 4566:
			return 40;
		case 4084:
			return 60;
		case 1037:
			return 50;
		case 11919:
			return 10;
		case 12956:
		case 12957:
		case 12958:
		case 12959:
		case 10933:
		case 10939:
		case 10940:
		case 10941:
		case 13258:
		case 13259:
		case 13260:
		case 13261:
			return 5;
		case 12596:
			return 12;
		case 6199:
			if (c.myShopId == 78)
				return 30;
			else
				return 20;
			// VOTE
		case 12748:
			return 2;
		case 7409:
			return 15;
		case 9920:
			return 10;
		case 12637:
		case 12638:
		case 12639:
			return 20;
		case 775:
				return 10;
		case 3057:
		case 3058:
		case 3059:
		case 6654:
		case 6655:
		case 6656:
		case 6180:
		case 6181:
		case 6182:
		case 13640:
		case 13642:
		case 13644:
		case 13646:
		case 5553:
		case 5554:
		case 5555:
		case 5556:
		case 5557:
		case 20704:
		case 20706:
		case 20708:
		case 20710:
		case 12013:
		case 12014:
		case 12015:
		case 12016:
			return 5;
		case 776:
		case 20008:
		case 10071:
			return 15;
		case 6666:
			return 20;
		case 1050:
			return 80;
		case 11887:
			return 10;

		}
		return Integer.MAX_VALUE;
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {
		boolean CANNOT_SELL = IntStream.of(Config.ITEM_SELLABLE).anyMatch(sellable -> sellable == removeId);
		if (c.myShopId != 116 && c.myShopId != 115) {
			if (CANNOT_SELL) {
				c.sendMessage("You can't sell " + ItemAssistant.getItemName(removeId).toLowerCase() + ".");
				return;
			}
		}
		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (IsIn == false) {
			c.sendMessage("You can't sell " + ItemAssistant.getItemName(removeId).toLowerCase() + " to this store.");
		} else {
			int ShopValue = 0;
			String ShopAdd = "";
			if (c.myShopId != 26) {
				ShopValue *= 0.667;
			}
			if (c.myShopId == 83) {
				int i = 0;
				for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
					if (t.getItemId() == removeId) {
						ShopValue = (int) Math.floor(t.getItemWorth());
						i++;
					}
				}
				if (i == 0) {
					c.sendMessage("You can't sell " + ItemAssistant.getItemName(removeId).toLowerCase() + " to this store.");
					return;
				}
			} else {
				ShopValue = (int) Math.floor(getItemShopValue(removeId, 1, removeSlot));
			}
			if (ShopValue >= 1000 && ShopValue < 1000000) {
				ShopAdd = " (" + (ShopValue / 1000) + "K)";
			} else if (ShopValue >= 1000000) {
				ShopAdd = " (" + (ShopValue / 1000000) + " million)";
			}
			if (c.myShopId == 12) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ((int) Math.ceil((getSpecialItemValue(removeId) * 0.60)) + " PKP Tickets."));
			} else if (c.myShopId == 44) {
				if (!Item.getItemName(removeId).contains("head")) {
					c.sendMessage("You cannot sell this to the slayer shop.");
					return;
				} else {
					c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ShopValue + " slayer points" + ShopAdd);
				}
			} else if (c.myShopId == 18) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ShopValue + " marks of grace" + ShopAdd);
			} else if (c.myShopId == 116) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ((int) Math.ceil((getSpecialItemValue(removeId) * 0.60)) + " blood money"));
			}  else if (c.myShopId == 29) {
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ShopValue + " tokkul" + ShopAdd);
			} else {
				ShopValue *= 0.667;
				c.sendMessage(ItemAssistant.getItemName(removeId) + ": shop will buy for " + ShopValue + " coins" + ShopAdd);
			}
		}
	}

	/**
	 * Selling items back to a store
	 * @param itemID	
	 * 					itemID that is being sold
	 * @param fromSlot
	 * 					fromSlot the item currently is located in
	 * @param amount
	 * 					amount that is being sold
	 * @return
	 * 					true is player is allowed to sell back to the store,
	 * 					else false
	 */
	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return false;
		}
		if (!c.getMode().isItemSellable(c.myShopId, itemID)) {
			c.sendMessage("Your game mode does not permit you to sell this item to the shop.");
			return false;
		}
		if (c.myShopId != 115) {
			if (itemID == 863 || itemID == 11230 || itemID == 869 || itemID == 868 || itemID == 867 || itemID == 866 || itemID == 4740 || itemID == 9244 || itemID == 11212
					|| itemID == 892 || itemID == 9194 || itemID == 9243 || itemID == 9242 || itemID == 9241 || itemID == 9240 || itemID == 9239 || itemID == 882 || itemID == 884
					|| itemID == 886 || itemID == 888 || itemID == 890 | itemID == 995) {
				c.sendMessage("You can't sell this item.");
				return false;
			}
		}
		
		switch (c.myShopId) {
		case 9:
		case 12:
		case 13:
		case 14:
		case 21:
		case 22:
		case 23:
		case 75:
		case 77:
		case 120:
		case 78:
		case 117:
			c.sendMessage("You cannot sell items to this shop.");
			return false;
		}
		boolean CANNOT_SELL = IntStream.of(Config.ITEM_SELLABLE).anyMatch(sellable -> sellable == itemID);
		if (c.myShopId != 116 && c.myShopId != 115) {
			if (CANNOT_SELL) {
				c.sendMessage("You can't sell " + ItemAssistant.getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.sendMessage("You can't sell " + ItemAssistant.getItemName(itemID).toLowerCase() + " to this store.");
					return false;
				}
			}

			if (amount > c.playerItemsN[fromSlot] && (Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == true || Item.itemStackable[(c.playerItems[fromSlot] - 1)] == true)) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID) && Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == false
					&& Item.itemStackable[(c.playerItems[fromSlot] - 1)] == false) {
				amount = c.getItems().getItemAmount(itemID);
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			int TotPrice3 = 0;
			int TotPrice4 = 0;
			// int Overstock;
			//for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1, fromSlot));
				TotPrice3 = (int) Math.floor(getSpecialItemValue(itemID));
				if (c.myShopId == 83) {
					int i = 0;
					for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
						if (t.getItemId() == itemID) {
							TotPrice4 = (int) Math.floor(t.getItemWorth());
							i++;
						}
					}
					if (i == 0) {
						c.sendMessage("You can't sell " + ItemAssistant.getItemName(itemID).toLowerCase() + " to this store.");
						return false;
					}
				}
				if (c.myShopId != 26) {
					TotPrice2 *= 0.667;
				}
				TotPrice2 = TotPrice2 * amount;
				TotPrice4 = TotPrice4 * amount;
				if (c.getItems().freeSlots() > 0 || c.getItems().playerHasItem(995)) {
						if ((Item.itemStackable[itemID] || Item.itemIsNote[itemID]) && c.getItems().playerHasItem(itemID, amount)) {
							c.getItems().deleteItemNoSave(itemID,c.getItems().getItemSlot(itemID),amount);
							if (c.myShopId == 12) {
								c.getItems().addItem(2996, (int) Math.ceil(TotPrice3 *= 0.75));
							} else if (c.myShopId != 12 && c.myShopId != 29 && c.myShopId != 44 && c.myShopId != 18  && c.myShopId != 83 && c.myShopId != 116 && c.myShopId != 115) {
								c.getItems().addItem(995, TotPrice2);
							} else if (c.myShopId == 29) {
								c.getItems().addItem(6529, TotPrice2);
							} else if (c.myShopId == 18) {
								c.getItems().addItem(11849, TotPrice2);
							} else if (c.myShopId == 83) {
								c.getItems().addItem(995, TotPrice4);
								addShopItem(itemID, amount);
							}  else if (c.myShopId == 116) {
								c.getItems().addItem(13307, (int) Math.ceil(TotPrice3 *= 0.60));
							} else if (c.myShopId == 44) {
								if (!Item.getItemName(itemID).contains("head")) {
									return false;
								} else {
									c.getSlayer().setPoints(c.getSlayer().getPoints() + TotPrice2);
									c.refreshQuestTab(5);
								}
							}
							//addShopItem(itemID, amount);
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							c.getItems().resetItems(3823);
							resetShop(c.myShopId);
							updatePlayerShop();
							return false;
						} else {
							if (c.myShopId == 12) {
								c.getItems().addItem(2996, (int) Math.ceil(TotPrice3 *= 0.75));
							} else if (c.myShopId != 12 && c.myShopId != 29 && c.myShopId != 44 && c.myShopId != 18 && c.myShopId != 83 && c.myShopId != 116 && c.myShopId != 115) {
								c.getItems().addItem(995, TotPrice2);
							} else if (c.myShopId == 29) {
								c.getItems().addItem(6529, TotPrice2);
							} else if (c.myShopId == 18) {
								c.getItems().addItem(11849, TotPrice2);
							} else if (c.myShopId == 83) {
								c.getItems().addItem(995, TotPrice4);
								addShopItem(itemID, amount);
							} else if (c.myShopId == 116) {
								c.getItems().addItem(13307, (int) Math.ceil(TotPrice3 *= 0.60));
							} else if (c.myShopId == 44) {
								if (!Item.getItemName(itemID).contains("head")) {
									return false;
								} else {
									c.getSlayer().setPoints(c.getSlayer().getPoints() + TotPrice2);
									c.refreshQuestTab(5);
								}
							}
						
						if (Item.itemIsNote[itemID] == false) {
							c.getItems().deleteItem2(itemID, amount);
						}
						// addShopItem(itemID, 1);
					}
				} else {
					c.sendMessage("You don't have enough space in your inventory.");
					c.getItems().resetItems(3823);
					return false;
				}
			//}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			PlayerSave.saveGame(c);
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}

		if (Item.itemIsNote[itemID] == true) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	/**
	 * Buying item(s) from a store
	 * @param itemID
	 * 					itemID that the player is buying
	 * @param fromSlot
	 * 					fromSlot the items is currently located in
	 * @param amount
	 * 					amount of items the player is buying
	 * @return
	 * 					true if the player is allowed to buy the item(s),
	 * 					else false
	 */
	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			if (c.debugMessage)
				c.sendMessage("rekt1");
			return false;
		}
		if (!c.getMode().isItemPurchasable(c.myShopId, itemID)) {
			c.sendMessage("Your game mode does not allow you to buy this item.");
			return false;
		}
		
		if (c.myShopId == 83) {
			c.sendMessage("You cannot buy items from this shop.");
			return false;
		}
		
		if (c.myShopId == 81) {
			if (!c.getDiaryManager().getWildernessDiary().hasDoneHard()) {
				c.sendMessage("You must have completed wilderness hard diaries to purchase this.");
				return false;
			}
		}
		
		if (c.myShopId == 6) {
			if (c.getMode().isIronman() || c.getMode().isUltimateIronman()) {
				if (!c.getDiaryManager().getVarrockDiary().hasDoneMedium()) {
					c.sendMessage("You must have completed the varrock diary up to medium to purchase this.");
					return false;
				}
			}
		}
		
		if (c.myShopId == 115) {
			if (!c.inClanWarsSafe()) {
				System.out.println("[District] " + c.playerName + " Attempting to purchase from free shop outside disitrict.");
				return false;
			}
			if (Item.itemStackable[itemID]) {
				amount = 1000;
			}
			if (itemID == 12934) {
				amount = 10000;
			}
		}
		if (c.myShopId == 116) {
			if (!c.inClanWarsSafe()) {
				System.out.println("[District] " + c.playerName + " Attempting to purchase from bm shop outside disitrict.");
				return false;
			}
		}
		/**
		 * Bounty hunter
		 */
		if (c.myShopId == 80) {
			if (itemID == 12791) {
				if (c.getItems().getItemCount(12791, true) > 0) {
					c.sendMessage("It seems like you already have one of these.");
					return false;
				}
			}
			if (itemID == 11941) {
				if (c.getItems().getItemCount(11941, true) > 0) {
					c.sendMessage("It seems like you already have one of these.");
					return false;
				}
			}
		}
		/**
		 * Slayer shop
		 */
		if (c.myShopId == 44) {
			if (Item.getItemName(itemID).contains("head")) {
				c.sendMessage("This product cannot be purchased.");
				return false;
			}
		}
		/**
		 * Avaas
		 */
		if (c.myShopId == 19) {
			if (itemID == 10498) {
				if (!c.getItems().playerHasItem(886, 75)) {
					c.sendMessage("You must have 75 steel arrows to exchange for this attractor");
					return false;
				}
				c.getItems().deleteItem(886, 775);
				c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.ATTRACTOR);
			}
		}
		/**
		 * RFD Shop
		 */
		if (c.myShopId == 14) {
			if (itemID == 7458 && c.rfdGloves < 1) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7459 && c.rfdGloves < 2) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7460 && c.rfdGloves < 3) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7461 && c.rfdGloves < 5) {
				c.sendMessage("You are not eligible to buy these.");
				return false;
			}
			if (itemID == 7462) {
				if (c.rfdGloves < 6) {
					c.sendMessage("You are not eligible to buy these.");
					return false;
				}
				c.getDiaryManager().getLumbridgeDraynorDiary()
						.progress(LumbridgeDraynorDiaryEntry.PURCHASE_BARROW_GLOVES);
			}
		}
		if (c.myShopId == 17) {
			skillBuy(itemID);
			return false;
		}
		if (c.myShopId == 15) {
			buyVoid(itemID);
			return false;
		}
		if (!shopSellsItem(itemID))
			return false;
		if (amount > 0) {
			if (c.myShopId != 115) {
				if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
					amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
				}
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			int Slot = 0;
			int Slot1 = 0;

			switch (c.myShopId) {
			case 9:
			case 10:
			case 12:
			case 13:
			case 18:
			case 40:
			case 44:
			case 75:
			case 77:
			case 78:
			case 79:
			case 80:
			case 82:
			case 116:
			case 117:
			case 118:
			case 119:
				case 120:
				handleOtherShop(itemID, amount);
				return false;
			}
			TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0, fromSlot));
			TotPrice2 = c.getMode().getModifiedShopPrice(c.myShopId, itemID, TotPrice2);
			Slot = c.getItems().getItemSlot(995);
			Slot1 = c.getItems().getItemSlot(6529);
			if (TotPrice2 <= 1) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0, fromSlot));
				TotPrice2 *= 1.66;
			}
			if (c.myShopId == 115) {
				TotPrice2 = -1;
			}
			if(c.myShopId==124 && c.amDonated>=150 && itemID == 299){
				TotPrice2=0;
			}
			if (Item.itemStackable[itemID]) {
				if (c.myShopId != 29) {
					if (c.getItems().playerHasItem(995, TotPrice2 * amount)) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(995, TotPrice2 * amount);
							c.getItems().addItem(itemID, amount);
							if (c.myShopId != 115) {
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							c.getItems().resetItems(3823);
							return false;
						}
					} else {
						c.sendMessage("You don't have enough coins.");
						c.getItems().resetItems(3823);
						return false;
					}
				}
			} else if (c.myShopId == 29) {
				if (c.getRechargeItems().hasItem(11136)) {
					TotPrice2 = (int) (TotPrice2 * 0.95);
				}
				if (c.getRechargeItems().hasItem(11138)) {
					TotPrice2 = (int) (TotPrice2 * 0.9);
				}
				if (c.getRechargeItems().hasItem(11140)) {
					TotPrice2 = (int) (TotPrice2 * 0.85);
				}
				if (c.getRechargeItems().hasItem(13103)) {
					TotPrice2 = (int) (TotPrice2 * 0.75);
				}
				if (c.playerItemsN[Slot1] >= TotPrice2 * amount) {
					if (c.getItems().freeSlots() > 0) {
						c.getItems().deleteItem(6529, TotPrice2 * amount);
						c.getItems().addItem(itemID, amount);
						ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= amount;
						ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
						if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
							ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
						}
					} else {
						c.sendMessage("You don't have enough space in your inventory.");
						c.getItems().resetItems(3823);
						return false;
					}
				} else {
					c.sendMessage("You don't have enough tokkul.");
					c.getItems().resetItems(3823);
					return false;
				}
			} else {
				for (int i = amount; i > 0; i--) {
					if (c.myShopId == 115) {
						TotPrice2 = -1;
					}
					if (Slot == -1 && c.myShopId != 29 && c.myShopId != 115) {
						c.sendMessage("You don't have enough coins.");
						return false;
					}
					if (Slot1 == -1 && (c.myShopId == 29)) {
						c.sendMessage("You don't have enough tokkul.");
						return false;
					}
					if (c.myShopId != 29) {
						if (c.getItems().playerHasItem(995, TotPrice2)) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(995, c.getItems().getItemSlot(995), TotPrice2);
								c.getItems().addItem(itemID, 1);
								if (c.myShopId == 6) {
									if (c.getMode().isIronman() || c.getMode().isUltimateIronman()) {
										if (!c.getDiaryManager().getVarrockDiary().hasDoneMedium()) {
											c.sendMessage("You must have completed the varrock diary up to medium to purchase this.");
											return false;
										}
											if (c.getRechargeItems().useItem(2, 1)) {
												
											} else {
												c.sendMessage("You have already purchased 150 battlestaves today.");
												return false;
											}
									}
								}
								if (c.myShopId != 115)
									ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
									ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
									if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
										ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
									}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								c.getItems().resetItems(3823);
								return false;
							}
						} else {
							c.sendMessage("You don't have enough coins.");
							c.getItems().resetItems(3823);
							return false;
						}
					}
					if (c.myShopId == 29) {
						if (c.getRechargeItems().hasItem(11136)) {
							TotPrice2 = (int) (TotPrice2 * 0.95);
						}
						if (c.getRechargeItems().hasItem(11138)) {
							TotPrice2 = (int) (TotPrice2 * 0.9);
						}
						if (c.getRechargeItems().hasItem(11140)) {
							TotPrice2 = (int) (TotPrice2 * 0.85);
						}
						if (c.getRechargeItems().hasItem(13103)) {
							TotPrice2 = (int) (TotPrice2 * 0.75);
						}
						if (c.playerItemsN[Slot1] >= TotPrice2) {
							if (c.getItems().freeSlots() > 0) {
								c.getItems().deleteItem(6529, c.getItems().getItemSlot(6529), TotPrice2);
								c.getItems().addItem(itemID, 1);
								ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
								ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
								if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
									ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
								}
							} else {
								c.sendMessage("You don't have enough space in your inventory.");
								c.getItems().resetItems(3823);
								return false;
							}
						} else {
							c.sendMessage("You don't have enough tokkul.");
							c.getItems().resetItems(3823);
							return false;
						}
					}
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}

	/**
	 * Special currency stores
	 * @param itemID	
	 * 					itemID that is being bought
	 * @param amount
	 * 					amount that is being bought
	 */
	public void handleOtherShop(int itemID, int amount) {
		if (amount <= 0) {
			c.sendMessage("You need to buy atleast one or more of this item.");
			return;
		}
		if (!c.getItems().isStackable(itemID)) {
			if (amount > c.getItems().freeSlots()) {
				amount = c.getItems().freeSlots();
			}
		}
		if (c.myShopId == 40) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getArenaPoints() < itemValue) {
				c.sendMessage("You do not have enough arena points to buy this from the shop.");
				return;
			}
			c.setArenaPoints(c.getArenaPoints() - itemValue);
			c.refreshQuestTab(4);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 82) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getShayPoints() < itemValue) {
				c.sendMessage("You do not have enough assault points to buy this from the shop.");
				return;
			}
			c.setShayPoints(c.getShayPoints() - itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 118) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getRaidPoints() < itemValue) {
				c.sendMessage("You do not have enough raid points to buy this from the shop.");
				return;
			}
			c.setRaidPoints(c.getRaidPoints() - itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 120) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.getPrestigePoints() < itemValue) {
				c.sendMessage("You do not have enough prestige points to buy this from the shop.");
				return;
			}
			c.prestigePoints =(c.getPrestigePoints() - itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 80) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			int itemValue = getBountyHunterItemCost(itemID) * amount;
			if (c.getBH().getBounties() < itemValue) {
				c.sendMessage("You do not have enough bounties to buy this from the shop.");
				return;
			}
			c.getBH().setBounties(c.getBH().getBounties() - itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			c.getPA().sendFrame126("Bounties: " + Misc.insertCommas(Integer.toString(c.getBH().getBounties())), 28052);
			return;
		}
		if (c.myShopId == 12 && itemID == 3144) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.pkp < itemValue) {
				c.sendMessage("You do not have enough pk points to buy this item.");
				return;
			}
			c.pkp -= itemValue;
			c.refreshQuestTab(0);
			c.getItems().addItem(itemID + 1, amount * 100);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 12 && itemID == 2289) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.pkp < itemValue) {
				c.sendMessage("You do not have enough pk points to buy this item.");
				return;
			}
			c.pkp -= itemValue;
			c.refreshQuestTab(0);
			c.getItems().addItem(itemID + 1, amount * 50);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 13) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(3849, itemValue)) {
				c.sendMessage("You do not have enough rusty caskets to switch with Jossik.");
				return;
			}
			c.getItems().deleteItem(3849, itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			return;
		}

		if (c.myShopId == 117 && itemID == 12695) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(13307, itemValue)) {
				c.sendMessage("You do not have enough blood money to purchase this.");
				return;
			}
			c.getItems().deleteItem(13307, itemValue);
			c.getItems().addItem(itemID + 1, amount * 2);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 116/* || c.myShopId == 117*/) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(13307, itemValue)) {
				c.sendMessage("You do not have enough blood money to purchase this.");
				return;
			}
			c.getItems().deleteItem(13307, itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 18) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(11849, itemValue)) {
				c.sendMessage("You do not have enough marks of grace to purchase this.");
				return;
			}
			c.getItems().deleteItem(11849, itemValue);
			c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 12 && itemID == 3025 || itemID == 6686) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (c.pkp < itemValue) {
				c.sendMessage("You do not have enough pk points to buy this item.");
				return;
			}
			c.pkp -= itemValue;
			c.refreshQuestTab(0);
			c.getItems().addItem(itemID, amount * 10);
			c.getItems().resetItems(3823);
			return;
		}
		if (c.myShopId == 79) {
			if (itemID >= 9920 && itemID <= 9925) {
				if (c.getHolidayStages().getStage("Halloween") < 5) {
					c.sendMessage("You needed to complete the Halloween event to obtain this.");
					return;
				}
			}
			if (itemID == 12845) {
				if (c.getHolidayStages().getStage("Halloween") < 6) {
					c.sendMessage("You needed to find this item in a chest in the halloween event.");
					return;
				}
			}
			if (itemID == 10507) {
				if (c.getHolidayStages().getStage("Christmas") < HolidayController.CHRISTMAS.getMaximumStage()) {
					c.sendMessage("You did not complete the christmas event, you cannot buy this.");
					return;
				}
			}
			if (c.getItems().playerHasItem(itemID) || c.getItems().isWearingItem(itemID) || c.getItems().bankContains(itemID)) {
				c.sendMessage("You still have this item, you have not lost it.");
				return;
			}
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			if (!c.getItems().playerHasItem(995, 500_000)) {
				c.sendMessage("You need atleast 500,000GP to purchase this item.");
				return;
			}
			c.getItems().deleteItem2(995, 500_000);
			c.getItems().addItem(itemID, 1);
			c.getItems().resetItems(3823);
			c.sendMessage("You have redeemed the " + ItemAssistant.getItemName(itemID) + ".");
			return;
		}
		if (c.myShopId == 12) {
			if (c.pkp >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.pkp -= getSpecialItemValue(itemID) * amount;
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					c.refreshQuestTab(0);
				}
			} else {
				c.sendMessage("You do not have enough pk points to buy this item.");
			}
		} else if (c.myShopId == 44) {
			if (itemID == 13226) {
				if (c.getItems().getItemCount(13226, true) == 1) {
					c.sendMessage("You already have a herb sack, theres no need for another.");
					return;
				}
			}
			if (itemID == 12020) {
				if (c.getItems().getItemCount(12020, true) == 1) {
					c.sendMessage("You already have a gem bag. theres no need for another.");
					return;
				}
			}
			if (c.getSlayer().getPoints() >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.getSlayer().setPoints(c.getSlayer().getPoints() - (getSpecialItemValue(itemID) * amount));
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					c.refreshQuestTab(5);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}
		} else if (c.myShopId == 9) {
			int itemValue = getSpecialItemValue(itemID) * amount;
			if (!c.getItems().playerHasItem(6306, itemValue)) {
				c.sendMessage("You do not have enough trading sticks to buy this item.");
				return;
			} else {
				c.getItems().deleteItem(6306, itemValue);
				c.getItems().addItem(itemID, amount);
				c.amDonated += itemValue;
				c.updateRank();
				c.getItems().resetItems(3823);
				return;
			}
		} else if (c.myShopId == 10) {
			if (c.getSlayer().getPoints() >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.getSlayer().setPoints(c.getSlayer().getPoints() - (getSpecialItemValue(itemID) * amount));
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					c.refreshQuestTab(5);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}
		} else if (c.myShopId == 78) {
			if (c.getAchievements().points >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.getAchievements().points -= getSpecialItemValue(itemID) * amount;
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough achievement points to buy this item.");
			}
		} else if (c.myShopId == 75) {
			if (c.pcPoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.pcPoints -= getSpecialItemValue(itemID) * amount;
					c.refreshQuestTab(3);
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough PC Points to buy this item.");
			}
		} else if (c.myShopId == 77) {
			if (c.votePoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.votePoints -= getSpecialItemValue(itemID) * amount;
					c.refreshQuestTab(1);
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough vote points to buy this item.");
			}
		} else if (c.myShopId == 119) {
			if (c.bloodPoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.bloodPoints -= getSpecialItemValue(itemID) * amount;
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough blood money points to buy this item.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 17;
		setupSkillCapes(capes, get99Count());
	}

	/*
	 * public int[][] skillCapes = {{0,9747,4319,2679},{1,2683,4329,2685},{2,2680 ,4359,2682},{3,2701,4341,2703 },{4,2686,4351,2688},{5,2689,4347,2691},{6,2692,4343,2691},
	 * {7,2737,4325,2733 },{8,2734,4353,2736},{9,2716,4337,2718},{10,2728,4335,2730 },{11,2695,4321,2697},{12,2713,4327,2715},{13,2725,4357,2727}, {14,2722,4345
	 * ,2724},{15,2707,4339,2709},{16,2704,4317,2706},{17,2710,4361, 2712},{18,2719,4355,2721},{19,2737,4331,2739},{20,2698,4333,2700}};
	 */
	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 9948 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < 22; j++) {
			if (c.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}

	public void setupSkillCapes(int capes, int capes2) {
		c.getPA().sendFrame171(1, 28050);
		c.getPA().sendFrame171(1, 28053);
		c.getItems().resetItems(3823);
		c.isShopping = true;
		c.myShopId = 17;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126("Skillcape Shop", 3901);

		int TotalItems = 0;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		for (int i = 0; i < 22; i++) {
			if (c.getLevelForXP(c.playerXP[i]) < 99)
				continue;
			c.getOutStream().writeByte(1);
			c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (c.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else {
						c.sendMessage("You need 99k to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().resetItems(3823);
	}

	public void openVoid() {
	}

	public void buyVoid(int item) {
	}

}
