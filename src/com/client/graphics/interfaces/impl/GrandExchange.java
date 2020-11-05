package com.client.graphics.interfaces.impl;

import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;

public class GrandExchange extends RSInterface {
	
	/**
	 * Interfaces:
	 * 
	 * Main GE Interface	:	25000
	 * Buy Interface		:	25600
	 * Sell Interface		:	25650
	 * Collect Interface	:	26000
	 * 
	 * Offer status interfaces
	 * Box 0	:	25800
	 * Box 1	:	25820
	 * Box 2	:	25840
	 * Box 3	:	25860
	 * Box 4	:	25880
	 * Box 5	:	25900
	 * Box 6	:	25920
	 * Box 7	:	25940
	 * 
	 */
	
	private static int GREEN_COLOR = 0x005F00;
	
	private static int RED_COLOR = 0x8F0000;
	
	public static void initializeInterfaces(TextDrawingArea[] tda) {
		mainInterface(tda);
		addBuyAndSellButtons(tda);
		addItemBoxes(tda);
		
		buyAndSellBackground(tda);
		buyScreen(tda);
		sellScreen(tda);
		
		offerStatusBackground(tda);
		addOfferStatusInterfaces(tda);
		
		collectInterface(tda);
	}
	
	public static int[] grandExchangeBuyAndSellBoxIds = new int[] {
		25100, 25120, 25140, 25160,
		25180, 25200, 25220, 25240
	};
	
	public static int[] grandExchangeItemBoxIds = new int[] {
		25300, 25320, 25340, 25360,
		25380, 25400, 25420, 25440
	};
	
	public static int[] grandExchangeOfferStatusInterfaceIds = new int[] {
		25800, 25820, 25840, 25860,
		25880, 25900, 25920, 25940
	};
	
	private static void mainInterface(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(25000);
		
		addSprite(25001, 1, "Interfaces/GE/IMAGE");
		
		addHoverButton(25002, "Interfaces/GE/CLOSE", 1, 21, 21, "Close", -1, 25003, 3);
		addHoveredButton(25003, "Interfaces/GE/CLOSE", 2, 21, 21, 25004);
		
		addText(25005, "Grand Exchange", tda, 2, 0xFF981F, true, true);
		
		addGEBox(25010, 0);
		addGEBox(25011, 1);
		addGEBox(25012, 2);
		addGEBox(25013, 3);
		addGEBox(25014, 4);
		addGEBox(25015, 5);
		addGEBox(25016, 6);
		addGEBox(25017, 7);
		
		setChildren(12, widget);
		setBounds(25001, 20, 13, 0, widget);
		setBounds(25002, 476, 20, 1, widget);
		setBounds(25003, 476, 20, 2, widget);
		setBounds(25005, 260, 23, 3, widget);
		
		setBounds(25010, 29, 75, 4, widget);
		setBounds(25011, 146, 75, 5, widget);
		setBounds(25012, 263, 75, 6, widget);
		setBounds(25013, 380, 75, 7, widget);
		
		setBounds(25014, 29, 196, 8, widget);
		setBounds(25015, 146, 196, 9, widget);
		setBounds(25016, 263, 196, 10, widget);
		setBounds(25017, 380, 196, 11, widget);
	}
	
	private static void addGEBox(int identity, int slotType) {
		RSInterface component = addInterface(identity);
		component.id = identity;
		component.type = 22;
		component.width = 115;
		component.height = 110;
		component.grandExchangeSlot = slotType;
	}
	
	private static void addBuyAndSellButtons(TextDrawingArea[] tda) {
		RSInterface widget;
		int childId = 0;
		for(int index = 0; index < grandExchangeBuyAndSellBoxIds.length; index++) {
			
			childId = grandExchangeBuyAndSellBoxIds[index];
			widget = addInterface(childId);
			
			setChildren(6, widget);
			
			addSprite(childId + 1, 2, "Interfaces/GE/IMAGE");
			setBounds(childId + 1, 0, 0, 0, widget);
			
			addHoverButton(childId + 2, "Interfaces/GE/IMAGE", 3, 47, 46, "Create buy offer", 0, childId + 3, 1);
			addHoveredButton(childId + 3, "Interfaces/GE/IMAGE", 4, 47, 46, childId + 4);
			
			setBounds(childId + 2, 7, 44, 1, widget);
			setBounds(childId + 3, 7, 44, 2, widget);
			
			addHoverButton(childId + 5, "Interfaces/GE/IMAGE", 5, 47, 46, "Create sell offer", 0, childId + 6, 1);
			addHoveredButton(childId + 6, "Interfaces/GE/IMAGE", 6, 47, 46, childId + 7);
			
			setBounds(childId + 5, 61, 44, 3, widget);
			setBounds(childId + 6, 61, 44, 4, widget);
			
			addText(childId + 8, "Empty", tda, 2, 0xFF981F, true, true);
			setBounds(childId + 8, 58, 6, 5, widget);
		}
	}
	
	private static void addItemBoxes(TextDrawingArea[] tda) {
		RSInterface widget;
		int childId = 0;
		
		for(int index = 0; index < grandExchangeItemBoxIds.length; index++) {
			
			childId = grandExchangeItemBoxIds[index];
			widget = addInterface(childId);
			
			setChildren(8, widget);
			
			addHoverButton(childId + 1, "Interfaces/GE/IMAGE", 2, 115, 110, "View offer", 0, childId + 2, 11);
			addHoveredButton(childId + 2, "Interfaces/GE/IMAGE", 7, 115, 110, childId + 3);
			setBounds(childId + 1, 0, 0, 0, widget);
			setBounds(childId + 2, 0, 0, 1, widget);
			
			addSprite(childId + 3, 8, "Interfaces/GE/IMAGE");
			setBounds(childId + 3, 5, 33, 2, widget);
			
			addToItemGroup(childId + 4, 1, 1, 0, 0, false, "", "", "");
			setBounds(childId + 4, 7, 35, 3, widget);
			
			addProgressBar(childId + 5, 107, 15, new int[] { GREEN_COLOR, RED_COLOR });
			setBounds(childId + 5, 4, 75, 4, widget);
			
			addText(childId + 6, "" + (childId + 6), tda, 2, 0xFF981F, true, true);
			setBounds(childId + 6, 58, 6, 5, widget);
			
			addText(childId + 7, "" + (childId + 7), tda, 0, 0xFF981F, false, true);
			setBounds(childId + 7, 50, 35, 6, widget);
			
			addText(childId + 8, "" + (childId + 8), tda, 0, 0xFF981F, true, true);
			setBounds(childId + 8, 58, 92, 7, widget);
		}
	}

	private static void buyScreen(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(25600);
		final int startX = 20, startY = 13;
		
		setChildren(5, widget);
		
		setBounds(25500, 0, 0, 0, widget);
		
		addSprite(25601, 15, "Interfaces/GE/BuyAndSell/IMAGE");
		setBounds(25601, startX + 152, startY + 48, 1, widget);
		
		addText(25602, "Buy offer", tda, 2, 0xFF981F, true, true);
		setBounds(25602, startX + 95, startY + 47, 2, widget);
		
		addHoverButton(25603, "Interfaces/GE/BuyAndSell/IMAGE", 16, 40, 36, "Search", 0, 25604, 1);
		addHoveredButton(25604, "Interfaces/GE/BuyAndSell/IMAGE", 16, 40, 36, 25605);
		
		setBounds(25603, startX + 76, startY + 71, 3, widget);
		setBounds(25604, startX + 76, startY + 71, 4, widget);
	}
	
	private static void sellScreen(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(25650);
		final int startX = 20, startY = 13;
		
		setChildren(3, widget);
		
		setBounds(25500, 0, 0, 0, widget);
		
		addSprite(25651, 14, "Interfaces/GE/BuyAndSell/IMAGE");
		setBounds(25651, startX + 152, startY + 48, 1, widget);
		
		addText(25652, "Sell offer", tda, 2, 0xFF981F, true, true);
		setBounds(25652, startX + 95, startY + 47, 2, widget);
	}
	
	private static void buyAndSellBackground(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(25500);
		
		final int startX = 20, startY = 13;
		
		setChildren(52, widget);
		
		addSprite(25501, 0, "Interfaces/GE/BuyAndSell/IMAGE");
		setBounds(25501, startX, startY, 0, widget);
		
		addHoverButton(25502, "Interfaces/GE/CLOSE", 1, 21, 21, "Close", -1, 25503, 3);
		addHoveredButton(25503, "Interfaces/GE/CLOSE", 2, 21, 21, 25504);
		setBounds(25502, 476, 20, 1, widget);
		setBounds(25503, 476, 20, 2, widget);
		
		addHoverButton(25505, "Interfaces/GE/BuyAndSell/IMAGE", 5, 13, 13, "Decrease amount", 0, 25506, 1);
		addHoveredButton(25506, "Interfaces/GE/BuyAndSell/IMAGE", 6, 13, 13, 25507);
		setBounds(25505, startX + 30, startY + 162, 3, widget);
		setBounds(25506, startX + 30, startY + 162, 4, widget);
		
		addHoverButton(25508, "Interfaces/GE/BuyAndSell/IMAGE", 3, 13, 13, "Increase amount", 0, 25509, 1);
		addHoveredButton(25509, "Interfaces/GE/BuyAndSell/IMAGE", 4, 13, 13, 25510);
		setBounds(25508, startX + 221, startY + 162, 5, widget);
		setBounds(25509, startX + 221, startY + 162, 6, widget);
		
		addHoverButton(25511, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Increase amount", 0, 25512, 1);
		addHoveredButton(25512, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25513);
		addText(25514, "+1", tda, 0, 0xFF981F, true, true);
		setBounds(25511, startX + 31, startY + 182, 7, widget);
		setBounds(25512, startX + 31, startY + 182, 8, widget);
		setBounds(25514, startX + 31 + 17, startY + 182 + 7, 9, widget);
		
		addHoverButton(25515, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Increase amount", 0, 25516, 1);
		addHoveredButton(25516, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25517);
		addText(25518, "+10", tda, 0, 0xFF981F, true, true);
		setBounds(25515, startX + 73, startY + 182, 10, widget);
		setBounds(25516, startX + 73, startY + 182, 11, widget);
		setBounds(25518, startX + 73 + 17, startY + 182 + 7, 12, widget);
		
		addHoverButton(25519, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Increase amount", 0, 25520, 1);
		addHoveredButton(25520, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25521);
		addText(25522, "+100", tda, 0, 0xFF981F, true, true);
		setBounds(25519, startX + 115, startY + 182, 13, widget);
		setBounds(25520, startX + 115, startY + 182, 14, widget);
		setBounds(25522, startX + 115 + 17, startY + 182 + 7, 15, widget);
		
		addHoverButton(25523, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Increase amount", 0, 25524, 1);
		addHoveredButton(25524, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25525);
		addText(25526, "All", tda, 0, 0xFF981F, true, true);
		setBounds(25523, startX + 157, startY + 182, 16, widget);
		setBounds(25524, startX + 157, startY + 182, 17, widget);
		setBounds(25526, startX + 157 + 17, startY + 182 + 7, 18, widget);
		
		addHoverButton(25527, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Increase amount", 0, 25528, 1);
		addHoveredButton(25528, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25529);
		addText(25530, "...", tda, 0, 0xFF981F, true, true);
		setBounds(25527, startX + 199, startY + 182, 19, widget);
		setBounds(25528, startX + 199, startY + 182, 20, widget);
		setBounds(25530, startX + 199 + 17, startY + 182 + 7, 21, widget);
		
		addHoverButton(25531, "Interfaces/GE/BuyAndSell/IMAGE", 5, 13, 13, "Decrease price", 0, 25532, 1);
		addHoveredButton(25532, "Interfaces/GE/BuyAndSell/IMAGE", 6, 13, 13, 25533);
		setBounds(25531, startX + 30 + 222, startY + 162, 22, widget);
		setBounds(25532, startX + 30 + 222, startY + 162, 23, widget);
		
		addHoverButton(25534, "Interfaces/GE/BuyAndSell/IMAGE", 3, 13, 13, "Increase price", 0, 25535, 1);
		addHoveredButton(25535, "Interfaces/GE/BuyAndSell/IMAGE", 4, 13, 13, 25536);
		setBounds(25534, startX + 221 + 222, startY + 162, 24, widget);
		setBounds(25535, startX + 221 + 222, startY + 162, 25, widget);
		
		addHoverButton(25537, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Decrease amount", 0, 25538, 1);
		addHoveredButton(25538, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25539);
		addSprite(25540, 11, "Interfaces/GE/BuyAndSell/IMAGE");
		setBounds(25537, startX + 30 + 222 + 7, startY + 162 + 21, 26, widget);
		setBounds(25538, startX + 30 + 222 + 7, startY + 162 + 21, 27, widget);
		setBounds(25540, startX + 30 + 222 + 7 + 11, startY + 162 + 21 + 10, 28, widget);
		
		addHoverButton(25541, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Decrease amount", 0, 25542, 1);
		addHoveredButton(25542, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25543);
		addSprite(25544, 13, "Interfaces/GE/BuyAndSell/IMAGE");
		setBounds(25541, startX + 30 + 222 + 7 + 58, startY + 162 + 21, 29, widget);
		setBounds(25542, startX + 30 + 222 + 7 + 58, startY + 162 + 21, 30, widget);
		setBounds(25544, startX + 30 + 222 + 7 + + 58 + 9, startY + 162 + 21 + 4, 31, widget);
		
		addHoverButton(25545, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Decrease amount", 0, 25546, 1);
		addHoveredButton(25546, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25547);
		addText(25548, "...", tda, 0, 0xFF981F, true, true);
		setBounds(25545, startX + 30 + 222 + 7 + 58 + 43, startY + 162 + 21, 32, widget);
		setBounds(25546, startX + 30 + 222 + 7 + 58 + 43, startY + 162 + 21, 33, widget);
		setBounds(25548, startX + 30 + 222 + 7 + 58 + 43 + 17, startY + 162 + 21 + 7, 34, widget);
		
		addHoverButton(25549, "Interfaces/GE/BuyAndSell/IMAGE", 1, 35, 25, "Decrease amount", 0, 25550, 1);
		addHoveredButton(25550, "Interfaces/GE/BuyAndSell/IMAGE", 2, 35, 25, 25551);
		addSprite(25552, 12, "Interfaces/GE/BuyAndSell/IMAGE");
		setBounds(25549, startX + 30 + 222 + 7 + 58 + 43 + 58, startY + 162 + 21, 35, widget);
		setBounds(25550, startX + 30 + 222 + 7 + 58 + 43 + 58, startY + 162 + 21, 36, widget);
		setBounds(25552, startX + 30 + 222 + 7 + 58 + 43 + 58 + 11, startY + 162 + 21 + 10, 37, widget);
		
		addHoverButton(25553, "Interfaces/GE/BuyAndSell/IMAGE", 7, 152, 40, "Confirm", 0, 25554, 1);
		addHoveredButton(25554, "Interfaces/GE/BuyAndSell/IMAGE", 8, 152, 40, 25555);
		addText(25556, "Confirm", tda, 2, 0xFFFFFF, true, true);
		setBounds(25553, startX + 157 + 10, startY + 182 + 69, 38, widget);
		setBounds(25554, startX + 157 + 10, startY + 182 + 69, 39, widget);
		setBounds(25556, startX + 157 + 10 + 76, startY + 182 + 69 + 12, 40, widget);
		
		addHoverButton(25557, "Interfaces/GE/BuyAndSell/IMAGE", 9, 30, 23, "Back", 0, 25558, 11);
		addHoveredButton(25558, "Interfaces/GE/BuyAndSell/IMAGE", 10, 30, 23, 25559);
		setBounds(25557, startX + 157 + 10 - 150, startY + 182 + 69 + 9, 41, widget);
		setBounds(25558, startX + 157 + 10 - 150, startY + 182 + 69 + 9, 42, widget);
		
		addText(25560, "Grand Exchange: Set up offer", tda, 2, 0xFF981F, true, true);
		setBounds(25560, startX + 245, startY + 10, 43, widget);
		
		addText(25561, "Choose an item...", tda, 2, 0xFF981F, false, true);
		setBounds(25561, startX + 180, startY + 50, 44, widget);
		
		addText(25562, Integer.toString(25562), tda, 0, 0xFFB83F, false, true);
		setBounds(25562, startX + 180, startY + 70, 45, widget);
		
		addText(25563, "Quantity:", tda, 2, 0xFF981F, true, true);
		setBounds(25563, startX + 130, startY + 140, 46, widget);
		
		addText(25564, "Price per item:", tda, 2, 0xFF981F, true, true);
		setBounds(25564, startX + 355, startY + 140, 47, widget);
		
		addText(25565, Integer.toString(25565), tda, 0, 0xFFB83F, true, true);
		setBounds(25565, startX + 52 + 81, startY + 159 + 4, 48, widget);
		
		addText(25566, Integer.toString(25566), tda, 0, 0xFFB83F, true, true);
		setBounds(25566, startX + 52 + 222 + 81, startY + 159 + 4, 49, widget);
		
		addText(25567, Integer.toString(25567), tda, 0, 0xFFFFFF, true, true);
		setBounds(25567, startX + 52 + 86 + 106, startY + 159 + 55 + 4, 50, widget);
		
		addToItemGroup(25568, 1, 1, 0, 0, false, "", "", "");
		setBounds(25568, startX + 79, startY + 74, 51, widget);
	}
	
	private static void offerStatusBackground(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(25700);
		
		final int startX = 20, startY = 13;
		
		setChildren(15, widget);
		
		addSprite(25701, 17, "Interfaces/GE/BuyAndSell/IMAGE");
		setBounds(25701, startX, startY, 0, widget);
		
		addHoverButton(25702, "Interfaces/GE/CLOSE", 1, 21, 21, "Close", -1, 25703, 3);
		addHoveredButton(25703, "Interfaces/GE/CLOSE", 2, 21, 21, 25704);
		setBounds(25702, 476, 20, 1, widget);
		setBounds(25703, 476, 20, 2, widget);
		
		addHoverButton(25705, "Interfaces/GE/BuyAndSell/IMAGE", 9, 30, 23, "Back", 0, 25706, 11);
		addHoveredButton(25706, "Interfaces/GE/BuyAndSell/IMAGE", 10, 30, 23, 25707);
		setBounds(25705, startX + 157 + 10 - 150, startY + 182 + 69 + 9, 3, widget);
		setBounds(25706, startX + 157 + 10 - 150, startY + 182 + 69 + 9, 4, widget);
		
		addText(25708, "Grand Exchange: Offer status", tda, 2, 0xFF981F, true, true);
		setBounds(25708, startX + 245, startY + 10, 5, widget);
		
		addText(25709, "Choose an item...", tda, 2, 0xFF981F, false, true);
		setBounds(25709, startX + 180, startY + 50, 6, widget);
		
		addText(25710, Integer.toString(25562), tda, 0, 0xFFB83F, false, true);
		setBounds(25710, startX + 180, startY + 70, 7, widget);
		
		addText(25711, "Quantity:", tda, 2, 0xFF981F, true, true);
		setBounds(25711, startX + 130, startY + 140, 8, widget);
		
		addText(25712, "Price per item:", tda, 2, 0xFF981F, true, true);
		setBounds(25712, startX + 355, startY + 140, 9, widget);
		
		addText(25713, Integer.toString(25713), tda, 2, 0xFFB83F, true, true);
		setBounds(25713, startX + 52 + 81, startY + 159 + 1, 10, widget);
		
		addText(25714, Integer.toString(25714), tda, 2, 0xFFB83F, true, true);
		setBounds(25714, startX + 52 + 222 + 81, startY + 159 + 1, 11, widget);
		
		addText(25715, Integer.toString(25715), tda, 2, 0xFFFFFF, true, true);
		setBounds(25715, startX + 52 + 86 + 106, startY + 159 + 55 + 1, 12, widget);
		
		addText(25716, Integer.toString(25716), tda, 0, 0xFFB83F, true, true);
		setBounds(25716, startX + 210, startY + 250, 13, widget);
		
		addText(25717, Integer.toString(25717), tda, 0, 0xFFB83F, true, true);
		setBounds(25717, startX + 210, startY + 265, 14, widget);
		
	}
	
	private static void addOfferStatusInterfaces(TextDrawingArea[] tda) {
		RSInterface widget;
		int childId = 0;
		int itemBoxChildId = 0;
		final int startX = 20, startY = 13;
		
		for(int index = 0; index < grandExchangeOfferStatusInterfaceIds.length; index++) {
			
			childId = grandExchangeOfferStatusInterfaceIds[index];
			itemBoxChildId = grandExchangeItemBoxIds[index];
			widget = addInterface(childId);
			
			setChildren(6, widget);
			
			//Adds the main offer status interface
			setBounds(25700, 0, 0, 0, widget);
			
			//Adds the current GE item 
			setBounds(itemBoxChildId + 4, startX + 79, startY + 74, 1, widget);
			
			addProgressBar(childId + 1, 291, 15, new int[] { GREEN_COLOR, RED_COLOR });
			setBounds(childId + 1, startX + 60, startY + 280, 2, widget);
			
			addText(childId + 2, Integer.toString(childId + 2), tda, 2, 0xFF981F, true, true);
			setBounds(childId + 2, startX + 95, startY + 47, 3, widget);
			
			addToItemGroup(childId + 3, 1, 1, 0, 0, true, "Collect to inventory", "Collect to bank", "");
			setBounds(childId + 3, startX + 380, startY + 254, 4, widget);
			
			addToItemGroup(childId + 4, 1, 1, 0, 0, true, "Collect to inventory", "Collect to bank", "");
			setBounds(childId + 4, startX + 380 + 52, startY + 254, 5, widget);
		}
	}
	
	private static void collectInterface(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(26000);
		
		final int startX = 30, startY = 45;
		
		setChildren(42, widget);
		
		addSprite(26001, 0, "Interfaces/GE/Collect/IMAGE");
		setBounds(26001, startX, startY, 0, widget);
		
		addHoverButton(26002, "Interfaces/GE/CLOSE", 1, 21, 21, "Close", -1, 26003, 3);
		addHoveredButton(26003, "Interfaces/GE/CLOSE", 2, 21, 21, 26004);
		setBounds(26002, startX + 432, startY + 7, 1, widget);
		setBounds(26003, startX + 432, startY + 7, 2, widget);

		int grandExchangeBox = 0;
		addProgressBar(26005, 40, 18, new int[] { GREEN_COLOR, RED_COLOR });
		setBounds(26005, startX + 23, startY + 52, 3, widget);
		setBounds(grandExchangeItemBoxIds[grandExchangeBox] + 4, startX + 88, startY + 53, 4, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 3, startX + 27, startY + 78, 5, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 4, startX + 72, startY + 78, 6, widget);
		grandExchangeBox++;
		
		addProgressBar(26006, 40, 18, new int[] { GREEN_COLOR, RED_COLOR });
		setBounds(26006, startX + 133, startY + 52, 7, widget);
		setBounds(grandExchangeItemBoxIds[grandExchangeBox] + 4, startX + 198, startY + 53, 8, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 3, startX + 137, startY + 78, 9, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 4, startX + 182, startY + 78, 10, widget);
		grandExchangeBox++;
		
		addProgressBar(26007, 40, 18, new int[] { GREEN_COLOR, RED_COLOR });
		setBounds(26007, startX + 243, startY + 52, 11, widget);
		setBounds(grandExchangeItemBoxIds[grandExchangeBox] + 4, startX + 308, startY + 53, 12, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 3, startX + 248, startY + 78, 13, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 4, startX + 304, startY + 78, 14, widget);
		grandExchangeBox++;
		
		addProgressBar(26008, 40, 18, new int[] { GREEN_COLOR, RED_COLOR });
		setBounds(26008, startX + 353, startY + 52, 15, widget);
		setBounds(grandExchangeItemBoxIds[grandExchangeBox] + 4, startX + 418, startY + 53, 16, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 3, startX + 359, startY + 78, 17, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 4, startX + 415, startY + 78, 18, widget);
		grandExchangeBox++;
		
		
		addProgressBar(26009, 40, 18, new int[] { GREEN_COLOR, RED_COLOR });
		setBounds(26009, startX + 23, startY + 136, 19, widget);
		setBounds(grandExchangeItemBoxIds[grandExchangeBox] + 4, startX + 88, startY + 137, 20, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 3, startX + 27, startY + 162, 21, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 4, startX + 72, startY + 162, 22, widget);
		grandExchangeBox++;
		
		addProgressBar(26010, 40, 18, new int[] { GREEN_COLOR, RED_COLOR });
		setBounds(26010, startX + 133, startY + 136, 23, widget);
		setBounds(grandExchangeItemBoxIds[grandExchangeBox] + 4, startX + 198, startY + 137, 24, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 3, startX + 137, startY + 162, 25, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 4, startX + 182, startY + 162, 26, widget);
		grandExchangeBox++;
		
		addProgressBar(26011, 40, 18, new int[] { GREEN_COLOR, RED_COLOR });
		setBounds(26011, startX + 243, startY + 136, 27, widget);
		setBounds(grandExchangeItemBoxIds[grandExchangeBox] + 4, startX + 308, startY + 137, 28, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 3, startX + 248, startY + 162, 29, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 4, startX + 304, startY + 162, 30, widget);
		grandExchangeBox++;
		
		addProgressBar(26012, 40, 18, new int[] { GREEN_COLOR, RED_COLOR });
		setBounds(26012, startX + 353, startY + 136, 31, widget);
		setBounds(grandExchangeItemBoxIds[grandExchangeBox] + 4, startX + 418, startY + 137, 32, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 3, startX + 359, startY + 162, 33, widget);
		setBounds(grandExchangeOfferStatusInterfaceIds[grandExchangeBox] + 4, startX + 415, startY + 162, 34, widget);
		
		
		
		addHoverButton(26013, "Interfaces/GE/Collect/IMAGE", 1, 83, 19, "Collect to inventory", 0, 26014, 1);
		addHoveredButton(26014, "Interfaces/GE/Collect/IMAGE", 2, 83, 19, 26015);
		addText(26016, "Inventory", tda, 0, 0xFFB83F, true, true);
		setBounds(26013, startX + 142, startY + 214, 35, widget);
		setBounds(26014, startX + 142, startY + 214, 36, widget);
		setBounds(26016, startX + 142 + 41, startY + 214 + 4, 37, widget);
		
		addHoverButton(26017, "Interfaces/GE/Collect/IMAGE", 1, 83, 19, "Collect to bank", 0, 26018, 1);
		addHoveredButton(26018, "Interfaces/GE/Collect/IMAGE", 2, 83, 19, 26019);
		addText(26020, "Bank", tda, 0, 0xFFB83F, true, true);
		setBounds(26017, startX + 142 + 96, startY + 214, 38, widget);
		setBounds(26018, startX + 142 + 96, startY + 214, 39, widget);
		setBounds(26020, startX + 142 + 96 + 41, startY + 214 + 4, 40, widget);
		
		addText(26021, "Collection Box", tda, 2, 0xFF981F, true, true);
		setBounds(26021, startX + 225, startY + 11, 41, widget);
	}
	
	public static boolean isSmallItemSprite(int childId) {
		for(int index = 0; index < grandExchangeItemBoxIds.length; index++)
			if(childId == grandExchangeItemBoxIds[index] + 4)
				return true;
		return false;
	}
	
}