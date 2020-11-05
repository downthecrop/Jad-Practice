package com.client.graphics.interfaces;

import java.awt.Dimension;
import java.util.Objects;

import com.client.Class36;
import com.client.Client;
import com.client.Configuration;
import com.client.DrawingArea;
import com.client.MRUNodes;
import com.client.Model;
import com.client.RSFont;
import com.client.RSMenuItem;
import com.client.Sprite;
import com.client.Stream;
import com.client.StreamLoader;
import com.client.TextClass;
import com.client.TextDrawingArea;
import com.client.definitions.ItemDefinition;
import com.client.definitions.NpcDefinition;
import com.client.features.gameframe.ScreenMode;
import com.client.graphics.interfaces.impl.Dropdown;
import com.client.graphics.interfaces.impl.DropdownMenu;
import com.client.graphics.interfaces.impl.Interfaces;
import com.client.graphics.interfaces.impl.Slider;

public class RSInterface {
	public static boolean showIds = false;
	public static RSFont[] newFonts;

	public static void unpack(StreamLoader streamLoader, TextDrawingArea textDrawingAreas[],
			StreamLoader streamLoader_1, RSFont[] newFontSystem) {
		aMRUNodes_238 = new MRUNodes(50000);
		Stream stream = new Stream(streamLoader.getDataForName("data"));
		newFonts = newFontSystem;
		int i = -1;
		int j = stream.readUnsignedWord();
		interfaceCache = new RSInterface[j + 80000];
		while (stream.currentOffset < stream.buffer.length) {
			int k = stream.readUnsignedWord();
			if (k == 65535) {
				i = stream.readUnsignedWord();
				k = stream.readUnsignedWord();
			}
			RSInterface rsInterface = interfaceCache[k] = new RSInterface();
			rsInterface.id = k;
			rsInterface.parentID = i;
			rsInterface.type = stream.readUnsignedByte();
			rsInterface.atActionType = stream.readUnsignedByte();
			rsInterface.contentType = stream.readUnsignedWord();
			rsInterface.width = stream.readUnsignedWord();
			rsInterface.height = stream.readUnsignedWord();
			rsInterface.aByte254 = (byte) stream.readUnsignedByte();
			rsInterface.mOverInterToTrigger = stream.readUnsignedByte();
			if (rsInterface.mOverInterToTrigger != 0)
				rsInterface.mOverInterToTrigger = (rsInterface.mOverInterToTrigger - 1 << 8)
						+ stream.readUnsignedByte();
			else
				rsInterface.mOverInterToTrigger = -1;
			int i1 = stream.readUnsignedByte();
			if (i1 > 0) {
				rsInterface.anIntArray245 = new int[i1];
				rsInterface.anIntArray212 = new int[i1];
				for (int j1 = 0; j1 < i1; j1++) {
					rsInterface.anIntArray245[j1] = stream.readUnsignedByte();
					rsInterface.anIntArray212[j1] = stream.readUnsignedWord();
				}

			}
			int k1 = stream.readUnsignedByte();
			if (k1 > 0) {
				rsInterface.valueIndexArray = new int[k1][];
				for (int l1 = 0; l1 < k1; l1++) {
					int i3 = stream.readUnsignedWord();
					rsInterface.valueIndexArray[l1] = new int[i3];
					for (int l4 = 0; l4 < i3; l4++)
						rsInterface.valueIndexArray[l1][l4] = stream.readUnsignedWord();

				}

			}
			if (rsInterface.type == 0) {
				rsInterface.drawsTransparent = false;
				rsInterface.scrollMax = stream.readUnsignedWord();
				rsInterface.isMouseoverTriggered = stream.readUnsignedByte() == 1;
				int i2 = stream.readUnsignedWord();
				rsInterface.children = new int[i2];
				rsInterface.childX = new int[i2];
				rsInterface.childY = new int[i2];
				for (int j3 = 0; j3 < i2; j3++) {
					rsInterface.children[j3] = stream.readUnsignedWord();
					rsInterface.childX[j3] = stream.readSignedWord();
					rsInterface.childY[j3] = stream.readSignedWord();
				}
			}
			if (rsInterface.type == 1) {
				stream.readUnsignedWord();
				stream.readUnsignedByte();
			}
			if (rsInterface.type == 2) {
				rsInterface.inv = new int[rsInterface.width * rsInterface.height];
				rsInterface.invStackSizes = new int[rsInterface.width * rsInterface.height];
				rsInterface.aBoolean259 = stream.readUnsignedByte() == 1;
				rsInterface.isInventoryInterface = stream.readUnsignedByte() == 1;
				rsInterface.usableItemInterface = stream.readUnsignedByte() == 1;
				rsInterface.aBoolean235 = stream.readUnsignedByte() == 1;
				rsInterface.invSpritePadX = stream.readUnsignedByte();
				rsInterface.invSpritePadY = stream.readUnsignedByte();
				rsInterface.spritesX = new int[20];
				rsInterface.spritesY = new int[20];
				rsInterface.sprites = new Sprite[20];
				for (int j2 = 0; j2 < 20; j2++) {
					int k3 = stream.readUnsignedByte();
					if (k3 == 1) {
						rsInterface.spritesX[j2] = stream.readSignedWord();
						rsInterface.spritesY[j2] = stream.readSignedWord();
						String s1 = stream.readString();
						if (streamLoader_1 != null && s1.length() > 0) {
							int i5 = s1.lastIndexOf(",");
							rsInterface.sprites[j2] = method207(Integer.parseInt(s1.substring(i5 + 1)), streamLoader_1,
									s1.substring(0, i5));
						}
					}
				}
				rsInterface.actions = new String[6];
				for (int l3 = 0; l3 < 5; l3++) {
					rsInterface.actions[l3] = stream.readString();
					if (rsInterface.actions[l3].length() == 0)
						rsInterface.actions[l3] = null;
					if (rsInterface.parentID == 3822)
						rsInterface.actions[4] = "Sell X";
					if (rsInterface.parentID == 3824)
						rsInterface.actions[4] = "Buy X";
					if (rsInterface.parentID == 1644)
						rsInterface.actions[2] = "Operate";
				}
			}
			if (rsInterface.type == 3)
				rsInterface.aBoolean227 = stream.readUnsignedByte() == 1;
			if (rsInterface.type == 4 || rsInterface.type == 1) {
				rsInterface.centerText = stream.readUnsignedByte() == 1;
				int k2 = stream.readUnsignedByte();
				if (textDrawingAreas != null)
					rsInterface.textDrawingAreas = textDrawingAreas[k2];
				rsInterface.textShadow = stream.readUnsignedByte() == 1;
			}
			if (rsInterface.type == 4) {
				rsInterface.message = stream.readString().replaceAll("RuneScape", Configuration.CLIENT_TITLE);

				if (showIds) {
					rsInterface.message = Integer.toString(rsInterface.id);
				}
				rsInterface.aString228 = stream.readString();
			}
			if (rsInterface.type == 1 || rsInterface.type == 3 || rsInterface.type == 4)
				rsInterface.textColor = stream.readDWord();
			if (rsInterface.type == 3 || rsInterface.type == 4) {
				rsInterface.secondaryColor = stream.readDWord();
				rsInterface.anInt216 = stream.readDWord();
				rsInterface.anInt239 = stream.readDWord();
			}
			if (rsInterface.type == 5) {
				rsInterface.drawsTransparent = false;
				String s = stream.readString();
				if (streamLoader_1 != null && s.length() > 0) {
					int i4 = s.lastIndexOf(",");
					rsInterface.sprite1 = method207(Integer.parseInt(s.substring(i4 + 1)), streamLoader_1,
							s.substring(0, i4));
				}
				s = stream.readString();
				if (streamLoader_1 != null && s.length() > 0) {
					int j4 = s.lastIndexOf(",");
					rsInterface.sprite2 = method207(Integer.parseInt(s.substring(j4 + 1)), streamLoader_1,
							s.substring(0, j4));
				}
			}
			if (rsInterface.type == 6) {
				int l = stream.readUnsignedByte();
				if (l != 0) {
					rsInterface.anInt233 = 1;
					rsInterface.mediaID = (l - 1 << 8) + stream.readUnsignedByte();
				}
				l = stream.readUnsignedByte();
				if (l != 0) {
					rsInterface.anInt255 = 1;
					rsInterface.anInt256 = (l - 1 << 8) + stream.readUnsignedByte();
				}
				l = stream.readUnsignedByte();
				if (l != 0)
					rsInterface.anInt257 = (l - 1 << 8) + stream.readUnsignedByte();
				else
					rsInterface.anInt257 = -1;
				l = stream.readUnsignedByte();
				if (l != 0)
					rsInterface.anInt258 = (l - 1 << 8) + stream.readUnsignedByte();
				else
					rsInterface.anInt258 = -1;
				rsInterface.modelZoom = stream.readUnsignedWord();
				rsInterface.modelRotation1 = stream.readUnsignedWord();
				rsInterface.modelRotation2 = stream.readUnsignedWord();
			}
			if (rsInterface.type == 7) {
				rsInterface.inv = new int[rsInterface.width * rsInterface.height];
				rsInterface.invStackSizes = new int[rsInterface.width * rsInterface.height];
				rsInterface.centerText = stream.readUnsignedByte() == 1;
				int l2 = stream.readUnsignedByte();
				if (textDrawingAreas != null)
					rsInterface.textDrawingAreas = textDrawingAreas[l2];
				rsInterface.textShadow = stream.readUnsignedByte() == 1;
				rsInterface.textColor = stream.readDWord();
				rsInterface.invSpritePadX = stream.readSignedWord();
				rsInterface.invSpritePadY = stream.readSignedWord();
				rsInterface.isInventoryInterface = stream.readUnsignedByte() == 1;
				rsInterface.actions = new String[6];
				for (int k4 = 0; k4 < 5; k4++) {
					rsInterface.actions[k4] = stream.readString();
					if (rsInterface.actions[k4].length() == 0)
						rsInterface.actions[k4] = null;
				}

			}
			if (rsInterface.atActionType == 2 || rsInterface.type == 2) {
				rsInterface.selectedActionName = stream.readString();
				rsInterface.spellName = stream.readString();
				rsInterface.spellUsableOn = stream.readUnsignedWord();
			}

			if (rsInterface.type == 8)
				rsInterface.message = stream.readString();

			if (rsInterface.atActionType == 1 || rsInterface.atActionType == 4 || rsInterface.atActionType == 5
					|| rsInterface.atActionType == 6) {
				rsInterface.tooltip = stream.readString();
				if (rsInterface.tooltip.length() == 0) {
					if (rsInterface.atActionType == 1)
						rsInterface.tooltip = "Ok";
					if (rsInterface.atActionType == 4)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 5)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 6)
						rsInterface.tooltip = "Continue";
				}
			}
			if (rsInterface.id == 8278) {
				rsInterface.message = "Players will be required to just use an Abyssal whip and Dragon dagger during combat.";
			}
			if (rsInterface.parentID == 6412) {
				if (rsInterface.scrollMax > 0) {
					rsInterface.scrollMax = 300;
				}
			}
		}
		aClass44 = streamLoader;
		defaultTextDrawingAreas = textDrawingAreas;
		Interfaces.loadInterfaces();
		teleport(textDrawingAreas);
		OsDropViewer(textDrawingAreas);
		achievementPopup2(textDrawingAreas);
		// aMRUNodes_238 = null;
	}

	public static final int OPTION_CLOSE = 3;

	public DropdownMenu dropdown;
	public int[] dropdownColours;
	public boolean hovered = false;
	public RSInterface dropdownOpen;
	public int dropdownHover = -1;
	public boolean inverted;
	public Slider slider;
	public boolean drawingDisabled;

	public int msgX, msgY;

	public boolean toggled = false;
	
	public static void hoverButton(int id, String tooltip) {
		hoverButton(id, tooltip, 255);
	}

	public static void hoverButton(int id, String tooltip, int sprite2, int sprite1) {
		hoverButton(id, tooltip, sprite2, sprite1, 255);
	}

	public static void configButton(int id, String tooltip, int enabledSprite, int disabledSprite) {
		RSInterface tab = addInterface(id);
		tab.tooltip = tooltip;
		tab.atActionType = OPTION_OK;
		tab.type = TYPE_CONFIG;
		tab.sprite2 = Client.cacheSprite3[enabledSprite];
		tab.sprite1 = Client.cacheSprite3[disabledSprite];
		tab.width = tab.sprite2.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.active = false;
	}

	public static void adjustableConfig(int id, String tooltip, int sprite, int opacity, int enabledSpriteBehind,
			int disabledSpriteBehind) {
		RSInterface tab = addInterface(id);
		tab.tooltip = tooltip;
		tab.atActionType = OPTION_OK;
		tab.type = TYPE_ADJUSTABLE_CONFIG;
		tab.sprite2 = Client.cacheSprite3[sprite];
		tab.enabledAltSprite = Client.cacheSprite3[enabledSpriteBehind];
		tab.disabledAltSprite = Client.cacheSprite3[disabledSpriteBehind];
		tab.width = tab.enabledAltSprite.myWidth;
		tab.height = tab.disabledAltSprite.myHeight;
		tab.spriteOpacity = opacity;
	}

	public static void hoverButton(int id, String tooltip, int opacity) {
		RSInterface tab = addInterface(id);
		tab.tooltip = tooltip;
		tab.atActionType = 1;
		tab.type = TYPE_HOVER;
		tab.sprite2 = Client.cacheSprite3[0];
		tab.sprite1 = Client.cacheSprite3[0];
		tab.width = tab.sprite2.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.active = false;
		tab.toggled = false;
	}
	
	public static void hoverButton(int id, String tooltip, int sprite2, int sprite1, int opacity) {
		RSInterface tab = addInterface(id);
		tab.tooltip = tooltip;
		tab.atActionType = 1;
		tab.type = TYPE_HOVER;
		tab.sprite2 = Client.cacheSprite3[sprite2];
		tab.sprite1 = Client.cacheSprite3[sprite1];
		tab.width = tab.sprite2.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.active = false;
		tab.toggled = false;
		tab.spriteOpacity = opacity;
	}
	
	public static void hoverButton(int id, String tooltip, int enabledSprite, int disabledSprite, String buttonText,
			TextDrawingArea tda[], int idx, int colour, int hoveredColour, boolean centerText) {
		RSInterface tab = addInterface(id);
		tab.tooltip = tooltip;
		tab.atActionType = 1;
		tab.type = TYPE_HOVER;
		tab.sprite2 = Client.cacheSprite3[enabledSprite];
		tab.sprite1 = Client.cacheSprite3[disabledSprite];
		tab.width = tab.sprite2.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.msgX = tab.width / 2;
		tab.msgY = (tab.height / 2) + 4;
		tab.message = buttonText;
		tab.active = false;
		tab.toggled = false;
		tab.textDrawingAreas = tda[idx];
		tab.textColor = colour;
		tab.anInt216 = hoveredColour;
		tab.centerText = centerText;
		tab.spriteOpacity = 255;
	}

	public static void hoverButton(int id, String tooltip, int enabledSprite, int disabledSprite, String buttonText,
			RSFont tda, int colour, int hoveredColour, boolean centerText) {
		RSInterface tab = addInterface(id);
		tab.tooltip = tooltip;
		tab.atActionType = 1;
		tab.type = TYPE_HOVER;
		tab.sprite2 = Client.cacheSprite3[enabledSprite];
		tab.sprite1 = Client.cacheSprite3[disabledSprite];
		tab.width = tab.sprite2.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.msgX = (tab.width / 2);
		tab.msgY = (tab.height / 2) + 4;
		tab.message = buttonText;
		tab.active = false;
		tab.toggled = false;
		tab.rsFont = tda;
		tab.textColor = colour;
		tab.anInt216 = hoveredColour;
		tab.centerText = centerText;
		tab.spriteOpacity = 255;
	}

	public void swapInventoryItems(int i, int j) {
		int k = inv[i];
		inv[i] = inv[j];
		inv[j] = k;
		k = invStackSizes[i];
		invStackSizes[i] = invStackSizes[j];
		invStackSizes[j] = k;
	}

	public static void slider(int id, double min, double max, int icon, int background, int contentType) {
		RSInterface widget = addInterface(id);
		widget.slider = new Slider(Client.cacheSprite3[icon], Client.cacheSprite3[background], min, max);
		widget.type = TYPE_SLIDER;
		widget.contentType = contentType;
	}

	public static void dropdownMenu(int id, int width, int defaultOption, String[] options, Dropdown d,
			TextDrawingArea tda[], int idx) {
		dropdownMenu(id, width, defaultOption, options, d,
				new int[] { 0x0d0d0b, 0x464644, 0x473d32, 0x51483c, 0x787169 }, false, tda, idx);
	}

	public static void dropdownMenu(int id, int width, int defaultOption, String[] options, Dropdown d,
			int[] dropdownColours, boolean centerText, TextDrawingArea tda[], int idx) {
		RSInterface menu = addInterface(id);
		menu.type = TYPE_DROPDOWN;
		menu.textDrawingAreas = tda[idx];
		menu.dropdown = new DropdownMenu(width, false, defaultOption, options, d);
		menu.atActionType = OPTION_DROPDOWN;
		menu.dropdownColours = dropdownColours;
		menu.centerText = centerText;
	}

	public static void handleConfigHover(RSInterface widget) {
		if (widget.active) {
			return;
		}
		widget.active = true;
		configHoverButtonSwitch(widget);
		disableOtherButtons(widget);
	}

	public static void configHoverButtonSwitch(RSInterface widget) {
		Sprite[] backup = new Sprite[] { widget.sprite2, widget.sprite1 };

		widget.sprite2 = widget.enabledAltSprite;
		widget.sprite1 = widget.disabledAltSprite;

		widget.enabledAltSprite = backup[0];
		widget.disabledAltSprite = backup[1];
	}

	public static void disableOtherButtons(RSInterface widget) {
		if (widget.buttonsToDisable == null) {
			return;
		}
		for (int btn : widget.buttonsToDisable) {
			RSInterface btnWidget = interfaceCache[btn];

			if (btnWidget.active) {
				btnWidget.active = false;
				configHoverButtonSwitch(btnWidget);
			}
		}
	}

	public static void achievementPopup2(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(36000);
		String dir = "Interfaces/Achievements2/SPRITE";
		addSprite(36001, 0, dir);
		addHoverButton(36002, dir, 1, 21, 21, "Close", -1, 36003, 1);
		addHoveredButton(36003, dir, 2, 21, 21, 36004);
		addText(36005, "Achievement Viewer", tda, 2, 0xff9040, true, true);
		addText(36550, "All rewards for completing this achievement:", tda, 0, 0xFFA500, false, true);
		addText(36026, "Easy", tda, 2, 0xff9040, true, true);
		int x = 5, y = 5;
		tab.totalChildren(19);
		tab.child(0, 36001, x, y);
		tab.child(1, 36002, 469 + x, 7 + y);
		tab.child(2, 36003, 469 + x, 7 + y);
		tab.child(3, 36005, 248 + x, 10 + y);
		String[] titles = { "Easy", "Medium", "Hard" };
		int xx = 7;
		for (int i = 0; i < 3; i++) {
			addHoverButton(46006 + i, dir, 3, 160, 20, "View", -1, 36010 + i, 1);
			addHoveredButton(46010 + i, dir, 4, 160, 20, 36014 + i);
			addText(46018 + i, titles[i], tda, 1, 0xff7000, true, true);
			tab.child(4 + i, 46006 + i, xx + x, 36 + y);
			tab.child(7 + i, 46010 + i, xx + x, 36 + y);
			tab.child(10 + i, 46018 + i, xx + x + 80, 40 + y);
			xx += 161;
		}
		tab.child(13, 36023, 8 + x, 82 + y);
		tab.child(14, 36500, 152 + x, 68 + y);
		tab.child(15, 36510, 317 + x, 232 + y);
		tab.child(16, 36520, 152 + x, 232 + y);
		tab.child(17, 36550, 171 + x, 218 + y);
		tab.child(18, 36026, 70 + x, 62 + y);

		RSInterface info = addInterface(36500);
		addText(36501, "Title Of Achievement", tda, 3, 0xff9040, true, true);
		addText(36502, "Progress: @gre@0% (0/0)", tda, 1, 0xffffff, true, true);
		info.totalChildren(8);
		info.child(0, 36501, 163, 6);
		info.child(1, 36502, 163, 26);
		for (int i = 0; i < 6; i++) {
			addText(36503 + i, "Description #" + i, tda, 0, 0xffffff, true, true);
			info.child(2 + i, 36503 + i, 163, 51 + (i * 13));
		}

		RSInterface exp = addInterface(36510);
		exp.totalChildren(5);
		for (int i = 0; i < 5; i++) {
			addText(36511 + i, "Text", tda, 0, 0xffffff, false, true);
			exp.child(i, 36511, 3, 3 + (i * 13));
		}
		exp.width = 146;
		exp.height = 72;
		exp.scrollMax = 200;

		RSInterface items = addInterface(36520);
		items.totalChildren(1);
		itemGroup(36521, 4, 3, 4, 5, true, true);
		// fill(36521);
		interfaceCache[36521].contentType = 206;
		items.child(0, 36521, 5, 5);
		items.width = 146;
		items.height = 72;
		items.scrollMax = 200;

		for (int i = 0; i < 3; i++) {
			RSInterface scroll = addInterface(36023 + i);
			scroll.totalChildren(100);
			for (int j = 0; j < 100; j++) {
				addClickableText(36037 + j + (i * 100), "Achievement: " + j, "Select", tda, 0, 0xff9040, false, true,
						112);
				scroll.child(j, 36037 + j + (i * 100), 2, 4 + (j * 13));
			}
			scroll.width = 109;
			scroll.height = 232;
			scroll.scrollMax = 1325;
		}
	}
    public static void infoTab(TextDrawingArea[] tda) {
        RSInterface tab = addTabInterface(47500);
        addText(47501, "@or1@Info Tab", tda, 2, 16750899, false, true);
        addHoverText(47502, "@or1@Call for help", "Call help",tda, 2, 16750899, true, true,100);
        addSprite(47503, 0, "Interfaces/infoTab/SPRITE");
        addSprite(47504, 1, "Interfaces/infoTab/SPRITE");
        addText(47505, "@cr10@", tda, 2, 16750899, false, true);
        tab.totalChildren(7);
        tab.child(0, 47501, 20, 7);
        tab.child(1, 47502, 38, 240);
        tab.child(2, 47503, 0, 35);
        tab.child(3, 47504, 0, 230);

        tab.child(4, 47503, 0, 32);
        tab.child(5, 47506, 2, 34);
        tab.child(6, 47505, 3, 10);
        RSInterface infoList = addTabInterface(47506);
        infoList.height = 196;
        infoList.width = 172;
        infoList.scrollMax = 250;
        infoList.newScroller = false;


        infoList.totalChildren(58);

        addText(47507, "@or1@Useful Stuff", tda, 2, 16750899, false, true);
        addHoverText(47508, "@or1@View Achievements", "View Achievements", tda, 0, 1022259, false, true, 150);
        addHoverText(47509, "@or1@View Npc Drops", "View  Npc Drops", tda, 0, 1022259, false, true, 150);
        addHoverText(47510, "@or1@View Npc KillCount", "View Npc KillCount", tda, 0, 1022259, false, true, 150);
        addHoverText(47511, "@or1@View Titles", "View Titles", tda, 0, 1022259, false, true, 150);
        addHoverText(47512, "", "View Details", tda, 0, 1022259, false, true, 150);
        addText(47513, "@or1@Quick Links", tda, 2, 16750899, false, true);

        infoList.totalChildren(58);
        infoList.child(0, 47507, 1, 3);
        infoList.child(1, 47508, 1, 25);
        infoList.child(2, 47509, 1, 41);
        infoList.child(3, 47510, 1, 57);
        infoList.child(4, 47511, 1, 73);
        infoList.child(5, 47512, 1, 89);
        infoList.child(6, 47513, 1, 105);

        int Ypos = 123;
        int frameID = 7;
        for (int iD = 47514; iD <= 47564; iD++) {
            addHoverText(iD, "", "View", tda,
                    0, 16711680, false, true, 150);
            infoList.child(frameID, iD, 1, Ypos);
            frameID++;
            Ypos += 13;
            Ypos++;
        }



    }
	public static void questTab(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(10220);
		addText(10221, "@or1@Control Panel", tda, 2, 16750899, false, true);
		addText(10222, "@or1@www.ascend-ps.com", tda, 2, 16750899, true, true);
		addSprite(10224, 0, "Interfaces/infoTab/SPRITE");
		addButton(10403, 2, "Interfaces/infoTab/TAB", "Refresh Tab");
		addButton(10404, 4, "Interfaces/infoTab/TAB", "View Diaries");
		addSprite(10223, 1, "Interfaces/infoTab/SPRITE");
		addText(10405, "@cr10@", tda, 2, 16750899, false, true);
		tab.totalChildren(9);
		tab.child(0, 10221, 20, 7);
		tab.child(1, 10222, 95, 240);
		tab.child(2, 10224, 0, 35);
		tab.child(3, 10223, 0, 230);
		tab.child(4, 10403, 172, 15);
		tab.child(5, 10223, 0, 32);
		tab.child(6, 10280, 2, 34);
		tab.child(7, 10404, 154, 15);
		tab.child(8, 10405, 3, 10);
		RSInterface infoList = addTabInterface(10280);
		infoList.height = 196;
		infoList.width = 172;
		infoList.scrollMax = 250;
		infoList.newScroller = false;

		addText(10406, "@or1@Server information", tda, 2, 16750899, false, true);
		addHoverText(10407, "@cr11@@or1@ Players online : @gre@0", "View Details", tda, 0, 1022259, false, true, 150);
		addHoverText(10408, "@cr22@@or1@ Wilderness count: @gre@0", "View Details", tda, 0, 1022259, false, true, 150);
		addHoverText(10409, "@cr19@@or1@Current event : @red@None", "View Details", tda, 0, 1022259, false, true, 150);
		addHoverText(10410, "@cr18@@or1@WOGW: @red@None ", "View Details", tda, 0, 1022259, false, true, 150);
		addHoverText(10411, "@cr9@@or1@ Current deal: @red@None", "View Details", tda, 0, 1022259, false, true, 150);
		addText(10412, "@or1@Player information", tda, 2, 16750899, false, true);

		infoList.totalChildren(58);
		infoList.child(0, 10406, 1, 3);
        infoList.child(1, 10407, 1, 25);
        infoList.child(2, 10408, 1, 41);
        infoList.child(3, 10409, 1, 57);
        infoList.child(4, 10410, 1, 73);
        infoList.child(5, 10411, 1, 89);
        infoList.child(6, 10412, 1, 105);

		int Ypos = 125;
		int frameID = 7;
		for (int iD = 10225; iD <= 10275; iD++) {
			addHoverText(iD, "", "View", tda,
					0, 16711680, false, true, 150);
			infoList.child(frameID, iD, 1, Ypos);
			frameID++;
			Ypos += 13;
			Ypos++;
		}



		RSInterface aDiary = addTabInterface(29465);
		try
		{
			addText(29466, "@cr17@@or1@ Diaries", tda, 2, 16750899, false, true);
			addSprite(29467, 0, "Interfaces/infoTab/SPRITE");
			//addButton(29468, 0, "Interfaces/infoTab/TAB", "View Main Information");
			//addButton(29469, 1, "Interfaces/infoTab/TAB", "Quick Load Preset");
			//addButton(29470, 2, "Interfaces/infoTab/TAB", "Refresh");
			addButton(29471, 4, "Interfaces/infoTab/TAB", "Main tab");
			addButton(29472, 2, "Interfaces/infoTab/TAB", "Refresh");
			addSprite(29473, 1, "Interfaces/infoTab/SPRITE");
			addText(29474, "@or1@Diaries Completed: @gre@0", tda, 2, 16750899, true, true);

			RSInterface diaryList = addTabInterface(29475);
			diaryList.height = 196;
			diaryList.width = 172;
			diaryList.scrollMax = 222;
			diaryList.newScroller = false;

			aDiary.totalChildren(8);
			aDiary.child(0, 29466, 10, 10);
			aDiary.child(1, 29467, 0, 35);
			aDiary.child(2, 29473, 0, 230);
			//aDiary.child(3, 29468, 95, 15);
			//aDiary.child(4, 29469, 114, 15);
			//aDiary.child(5, 29470, 133, 15);
			aDiary.child(3, 29471, 154, 15);
			aDiary.child(4, 29472, 172, 15);
			aDiary.child(5, 29473, 0, 32);
			aDiary.child(6, 29474, 95, 240);
			aDiary.child(7, 29475, 2, 34);

			diaryList.totalChildren(16);
			int Ypos2 = 8;
			int frameID2 = 0;
			for (int iD2 = 29480; iD2 <= 29495; iD2++) {
				addHoverText(iD2, "", "View", tda,
						0, 16711680, false, true, 150);
				diaryList.child(frameID2, iD2, 8, Ypos2);
				frameID2++;
				Ypos2 += 13;
				Ypos2++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static RSFont rsFont;

	public static void OsDropViewer(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(39500);
		String dir = "Interfaces/DropViewer/SPRITE";
		addSprite(39501, 0, dir);
		addHoverButton(39502, dir, 1, 21, 21, "Close", 0, 39503, 1);
		addHoveredButton(39503, dir, 2, 21, 21, 39504);
		addText(43005, "Monster Drop Viewer", tda, 2, 0xFFA500, true, true);
		addText(43110, "Health: @whi@0", tda, 1, 0xff9040, false, true);// overrides
		addText(43111, "Combat Level: @whi@0", tda, 1, 0xff9040, false, true);
		addText(43112, "Max Hit: @whi@0", tda, 1, 0xff9040, false, true);
		addText(43113, "Aggressive: @whi@false", tda, 1, 0xff9040, false, true);

		addInputField(39806, 30, 0xFF981F, "NPC/Item Name..", 130, 28, false, false, "[A-Za-z0-9 .,]");
		//addText(42522, "Find npc/item drops", drawingArea, 0, 0xFF981F, true, false);
		int x = 7, y = 7;
		tab.totalChildren(11);
		tab.child(0, 39501, 0 + x, 0 + y);
		tab.child(1, 39502, 472 + x, 7 + y);
		tab.child(2, 39503, 472 + x, 7 + y);
		tab.child(3, 43005, 250 + x, 11 + y);
		tab.child(4, 39806, 8+x, 37+y);
		tab.child(5, 39507, 6 + x, 66 + y);
		tab.child(6, 34000, 150 + x, 86 + y);
		tab.child(7, 43110, 250 + x, 40 + y);
		tab.child(8, 43111, 250 + x, 60 + y);
		tab.child(9, 43112, 360 + x, 40 + y);
		tab.child(10, 43113, 360 + x, 60 + y);

		RSInterface results = addInterface(39507);
		results.width = 122;
		results.height = 250;
		results.scrollMax = 1800;
		results.totalChildren(200);
		for (int j = 0; j < 200; j++) {
			addClickableText(33008 + j, "", "View Drops", tda, 0, 0xff0000, false, true, 110);
			results.child(j, 33008 + j, 6, 8 + (j * 14));
		}

		RSInterface main = addInterface(34000);
		main.totalChildren(720);
		main.width = 328;
		main.height = 230;
		main.scrollMax = 2560;
		addSprite(34001, 3, dir);
		addSprite(34002, 4, dir);
		for (int i = 0; i < 40; i++) {
			main.child(i, 34001, 0, (i * 64));
			main.child(i + 40, 34002, 0, 32 + (i * 64));
		}
		addText(34003, "Amount:", tda, 0, 0xff9040, true, true);
		addText(34004, "Rarity:", tda, 0, 0xff9040, true, true);
		addText(34005, "Chance:", tda, 0, 0xff9040, true, true);
		for (int i = 0; i < 80; i++) {
			itemGroup(34010 + i, 1, 1, 1, 1, false, false);
			interfaceCache[34010 + i].inv[0] = 14485;
			interfaceCache[34010 + i].invStackSizes[0] = 1;
			addText(34100 + i, "Item Name", tda, 1, 0xFFA500, false, true);
			addText(34200 + i, "1-50", tda, 0, 0xffffff, true, true);
			addText(34300 + i, "Common", tda, 0, 0xffffff, true, true);
			addText(34400 + i, "1/200", tda, 0, 0xffffff, true, true);
			int yy = (i * 32);
			main.child(80 + i, 34010 + i, 1, 0 + yy);
			main.child(160 + i, 34100 + i, 39, 6 + yy);
			main.child(240 + i, 34003, 175, 2 + yy);
			main.child(320 + i, 34004, 234, 2 + yy);
			main.child(400 + i, 34005, 293, 2 + yy);
			main.child(480 + i, 34200 + i, 175, 14 + yy);
			main.child(560 + i, 34300 + i, 234, 14 + yy);
			main.child(640 + i, 34400 + i, 293, 14 + yy);
		}

	}

	public static void itemGroup(int id, int w, int h, int x, int y, boolean drag, boolean examine) {
		RSInterface rsi = addInterface(id);
		rsi.width = w;
		rsi.height = h;
		rsi.inv = new int[w * h];
		rsi.invStackSizes = new int[w * h];
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.invSpritePadX = x;
		rsi.invSpritePadY = y;
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		rsi.sprites = new Sprite[20];
		rsi.type = 2;
	}

	public static TextDrawingArea[] defaultTextDrawingAreas;

	private static void addTransparentSprite2(int id, int spriteId, int transparency) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.transparency = transparency;
		tab.hoverType = 52;
		tab.sprite2 = Client.cacheSprite2[spriteId];
		tab.sprite1 = Client.cacheSprite2[spriteId];
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, int width, int height, String tT,
			int configID, int aT, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.opacity = 0;
		Tab.hoverType = -1;
		Tab.valueCompareType = new int[1];
		Tab.requiredValues = new int[1];
		Tab.valueCompareType[0] = 1;
		Tab.requiredValues[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = Client.cacheSprite1[bID];// imageLoader(bID, bName);
		Tab.sprite2 = Client.cacheSprite1[bID2];
		Tab.tooltip = tT;
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, int width, int height, int configID, int aT,
			int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.opacity = 0;
		Tab.hoverType = -1;
		Tab.valueCompareType = new int[1];
		Tab.requiredValues = new int[1];
		Tab.valueCompareType[0] = 1;
		Tab.requiredValues[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite2 = Client.cacheSprite1[bID];// imageLoader(bID, bName);
		Tab.sprite1 = Client.cacheSprite1[bID2];
		Tab.tooltip = "xd";
	}

	public static void addHoverButton_sprite_loader2(int i, int spriteId, int width, int height, String text,
			int contentType, int hoverOver, int aT) {// hoverable
		// button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.hoverType = hoverOver;
		tab.sprite2 = Client.cacheSprite2[spriteId];
		tab.sprite1 = Client.cacheSprite2[spriteId];
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton_sprite_loader2(int i, int spriteId, int w, int h, int IMAGEID) {// hoverable
		// button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.opacity = 0;
		tab.hoverType = -1;
		tab.scrollMax = 0;
		addHoverImage_sprite_loader2(IMAGEID, spriteId);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHoverImage_sprite_loader2(int i, int spriteId) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.hoverType = 52;
		tab.sprite2 = Client.cacheSprite2[spriteId];
		tab.sprite1 = Client.cacheSprite2[spriteId];
	}

	public static void quickPrayers(TextDrawingArea[] TDA) {
		int frame = 0;
		RSInterface tab = addTabInterface(17200);
		addTransparentSprite2(17235, 12, 330);
		int child = 17202;
		int config = 620;
		for (int i = 0; i < 29; i++) {
			addConfigButton2(child++, 904, 133, 134, 14, 14, "Select", 0, 0, config++);
		}
		addHoverButton_sprite_loader2(17232, 25, 83, 20, "Done", -1, 17233, 1);
		addHoveredButton_sprite_loader2(17233, 26, 83, 20, 17234);
		int removeOffset = 16;
		setChildren(33, tab);//
		setBounds(17235, 0, 25 - removeOffset, frame++, tab);// Faded backing
		setBounds(15608, 0, 16 - removeOffset, frame++, tab);// Faded backing
		setBounds(17202, 5 - 3, 8 + 17 - removeOffset, frame++, tab);
		setBounds(17203, 44 - 3, 8 + 17 - removeOffset, frame++, tab);
		setBounds(17204, 79 - 3, 8 + 17 - removeOffset, frame++, tab);
		setBounds(17205, 116 - 3, 8 + 17 - removeOffset, frame++, tab);
		setBounds(17206, 153 - 3, 8 + 17 - removeOffset, frame++, tab);
		setBounds(17207, 5 - 3, 48 + 17 - removeOffset, frame++, tab);
		setBounds(17208, 44 - 3, 48 + 17 - removeOffset, frame++, tab);
		setBounds(17209, 79 - 3, 48 + 17 - removeOffset, frame++, tab);
		setBounds(17210, 116 - 3, 48 + 17 - removeOffset, frame++, tab);
		setBounds(17211, 153 - 3, 48 + 17 - removeOffset, frame++, tab);
		setBounds(17212, 5 - 3, 85 + 17 - removeOffset, frame++, tab);
		setBounds(17213, 44 - 3, 85 + 17 - removeOffset, frame++, tab);
		setBounds(17214, 79 - 3, 85 + 17 - removeOffset, frame++, tab);
		setBounds(17215, 116 - 3, 85 + 17 - removeOffset, frame++, tab);
		setBounds(17216, 153 - 3, 85 + 17 - removeOffset, frame++, tab);
		setBounds(17217, 5 - 3, 124 + 17 - removeOffset, frame++, tab);
		setBounds(17218, 44 - 3, 124 + 17 - removeOffset, frame++, tab);
		setBounds(17219, 79 - 3, 124 + 17 - removeOffset, frame++, tab);
		setBounds(17220, 116 - 3, 124 + 17 - removeOffset, frame++, tab);
		setBounds(17221, 153 - 3, 124 + 17 - removeOffset, frame++, tab);
		setBounds(17222, 5 - 3, 160 + 17 - removeOffset, frame++, tab);
		setBounds(17223, 44 - 3, 160 + 17 - removeOffset, frame++, tab);
		setBounds(17224, 79 - 3, 160 + 17 - removeOffset, frame++, tab);
		setBounds(17225, 116 - 3, 160 + 17 - removeOffset, frame++, tab);
		setBounds(17226, 153 - 3, 160 + 17 - removeOffset, frame++, tab);
		setBounds(17227, 1, 207 + 4 - removeOffset, frame++, tab); // Chivalry toggle button
		setBounds(17228, 41, 207 + 4 - removeOffset, frame++, tab); // Piety toggle button
		setBounds(17229, 77, 207 + 4 - removeOffset, frame++, tab); // Rigour toggle button
		setBounds(17230, 116, 207 + 4 - removeOffset, frame++, tab); // Augury toggle button
		setBounds(17232, 52, 251 - removeOffset, frame++, tab);// confirm
		setBounds(17233, 52, 251 - removeOffset, frame++, tab);// Confirm hover
	}

	private static void addPrayerWithTooltip(final int i, final int j, final int k, final int l, final int j2,
			final int m, final String string) {
		addPrayer(i, j, k, l, j2, string, m, "normal/PRAYERON");
	}

	public static void addPrayer(final int i, final int configId, final int configFrame, final int requiredValues,
			final int prayerSpriteID, final String PrayerName, final int Hover, final String loca) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 22500;
		Interface.type = 5;
		Interface.atActionType = 4;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.hoverType = Hover;
		Interface.sprite1 = imageLoader(prayerSpriteID, "QuickPrayer/" + loca + "");
		Interface.sprite2 = imageLoader(prayerSpriteID, "QuickPrayer/" + loca + "");
		Interface.width = 34;
		Interface.height = 34;
		Interface.anIntArray212 = new int[1];
		Interface.anIntArray245 = new int[1];
		Interface.anIntArray212[0] = 1;
		Interface.anIntArray245[0] = configId;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 5;
		Interface.valueIndexArray[0][1] = configFrame;
		Interface.valueIndexArray[0][2] = 0;
		if (Client.tabInterfaceIDs[Client.tabID] != 17200) {
			Interface.tooltip = "Activate@or1@ " + PrayerName;
		}
		Interface = addTabInterface(i + 1);
		Interface.id = i + 1;
		Interface.parentID = 22500;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.sprite1 = imageLoader(prayerSpriteID, "QuickPrayer/" + loca + "");
		Interface.sprite2 = imageLoader(prayerSpriteID, "QuickPrayer/" + loca + "");
		Interface.width = 34;
		Interface.height = 34;
		Interface.anIntArray212 = new int[1];
		Interface.anIntArray245 = new int[1];
		Interface.anIntArray212[0] = 2;
		Interface.anIntArray245[0] = requiredValues + 1;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 2;
		Interface.valueIndexArray[0][1] = 5;
		Interface.valueIndexArray[0][2] = 0;
	}

	/**
	 * Rune pouch
	 * 
	 * @author Sky
	 */
	// public static RSInterface runePouch;
	public static void runePouch(TextDrawingArea[] tda) {
		final int START_ID = 29875;
		int id = START_ID;
		int child = 0;

		final int WINDOW_X = 80;
		final int WINDOW_Y = 25;

		RSInterface rsi = addTabInterface(id++);
		rsi.totalChildren(34);

		addSprite(id, 0, "/Interfaces/runepouch/SPRITE");
		rsi.child(child++, id++, WINDOW_X, WINDOW_Y);// BACKGROUND

		addHoverButton(id, "/Interfaces/runepouch/CLOSE", 0, 21, 21, "Close", 0, id + 1, 1);
		addHoveredButton(id + 1, "/Interfaces/runepouch/CLOSE", 1, 21, 21, id + 2);
		rsi.child(child++, id, WINDOW_X + 324, WINDOW_Y + 7); // CLOSE BUTTON
		rsi.child(child++, id + 1, WINDOW_X + 324, WINDOW_Y + 7); // CLOSE
																	// BUTTON
																	// HOVER
		id += 3;

		/**
		 * Inventory items
		 */
		final int START_X = WINDOW_X + 20;
		final int START_Y = WINDOW_Y + 135;
		int x = START_X;
		int y = START_Y;
		final int X_DIFF = 47;
		final int Y_DIFF = 32;
		// id = 27342;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 7; j++) {
				// System.out.println("Rune pouch inventory item interface: " + id);
				addItem(id, new String[] { "Add 1", "Add 5", "Add 10", "Add All", "Add X" });
				rsi.child(child++, id++, x, y);
				x += X_DIFF;
			}

			x = START_X;
			y += Y_DIFF;

		}

		/**
		 * Pouch items
		 */
		final int START_POUCH_X = WINDOW_X + 104;
		final int START_POUCH_Y = WINDOW_Y + 64;
		final int X_POUCH_DIFF = 56;
		x = START_POUCH_X;
		y = START_POUCH_Y;
		for (int i = 0; i < 3; i++) {
			addItem(id, new String[] { "Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X" }, 1336);
			rsi.child(child++, id++, x, y);
			x += X_POUCH_DIFF;
		}
	}

	public static void spawnTab(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(62000);
		addSpawnChooser(62007, tda);
		setChildren(1, widget);
		setBounds(62007, 0, 0, 0, widget);
	}

	public static void teleportationInterface(TextDrawingArea[] tda) {
		RSInterface rsinterface = addInterface(62100);
		addSprite(62101, 501, "Interfaces/Teleportation/IMAGE");
		addHoverButton(62102, "Interfaces/Teleportation/IMAGE", 1337, 23, 23, "Close", -1, 33332, 1);
		addHoveredButton(62103, "Interfaces/Teleportation/IMAGE", 500, 23, 23, 33333);
		addHoverText(62104, "", "Teleport", tda, 2, 0x000000, true, false, 260);
		// addText(62105, "Select Location", tda, 2, 0xF0B000);
		addText(62105, "Select Location", 0x000000, true, false, 52, tda, 3);
		setChildren(6, rsinterface);
		setBounds(62101, 1, 15, 0, rsinterface);
		setBounds(62102, 451, 37, 1, rsinterface);
		setBounds(62103, 451, 37, 2, rsinterface);
		setBounds(62104, 100, 64, 3, rsinterface);
		setBounds(62105, 260, 43, 4, rsinterface);
		setBounds(62106, 165, 80, 5, rsinterface);
		RSInterface scrollInterface = addTabInterface(62106);
		scrollInterface.scrollPosition = 0;
		scrollInterface.contentType = 0;
		scrollInterface.width = 280;
		scrollInterface.height = 220;
		scrollInterface.scrollMax = 1400;
		int x = 7, y = 11;
		int amountOfLines = 90;
		setChildren(amountOfLines, scrollInterface);
		for (int i = 0; i < amountOfLines; i++) {
			addHoverText(62107 + i, "coming soon", "Select teleport", tda, 1, 0x3F3F3F, true, false, 168);
			scrollInterface.child(i, 62107 + i, x, y);
			y += 22;
		}
	}

	public static void addAntibotWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(33300);
		addSprite(33301, 0, "Interfaces/AntiBot/IMAGE");
		addText(33302, "Click the 'Abyssal Whip'", tda, 2, 0xFF981F, true, true);
		addText(33303, "1:00", tda, 0, 0xFF981F, true, true);
		addText(33304,
				"If you click the wrong item or the time depletes to 0,\\nYou will be teleported to a new location.",
				tda, 0, 0xFF981F, true, true);
		setChildren(7, widget);
		setBounds(33301, 115, 96, 0, widget);
		setBounds(33302, 253, 105, 1, widget);
		setBounds(33303, 375, 105, 2, widget);
		setBounds(33304, 255, 190, 3, widget);
		setBounds(33310, 180, 140, 4, widget);
		setBounds(33313, 240, 140, 5, widget);
		setBounds(33316, 300, 140, 6, widget);

		for (int i = 0; i < 9; i += 3) {
			RSInterface option = addInterface(33310 + i);
			addToItemGroup(33311 + i, 1, 1, 0, 0, false, "", "", "");
			interfaceCache[33311 + i].inv = new int[] { 4152 };
			interfaceCache[33311 + i].invStackSizes = new int[] { 1 };
			addButton(33312 + i, 1, "Interfaces/AntiBot/IMAGE", "Select");
			setChildren(2, option);
			setBounds(33311 + i, 0, 0, 0, option);
			setBounds(33312 + i, 0, 0, 1, option);
		}
	}

	public static void addModerateWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(33200);
		addSprite(33201, 0, "Interfaces/Moderate/IMAGE");
		addHoverButton(33202, "Interfaces/Moderate/IMAGE", 1, 21, 21, "Close", -1, 33203, 3);
		addHoveredButton(33203, "Interfaces/Moderate/IMAGE", 2, 21, 21, 33204);
		addInputField(33205, 120, 0xFF981F, "Reason", 144, 20, false, true, "[A-Za-z0-9 ,']");
		addText(33206, "'Fourteen Chars'", tda, 2, 0xFF981F, true, true);
		addHoverButton(33207, "Interfaces/Moderate/IMAGE", 5, 90, 25, "Close", -1, 33208, 1);
		addHoveredButton(33208, "Interfaces/Moderate/IMAGE", 6, 90, 25, 33209);
		addText(33210, "Execute", tda, 2, 0xFF981F, true, true);
		addInputField(33211, 120, 0xFF981F, "Duration", 144, 20, false, true, "[0-9]");
		setChildren(10, widget);
		setBounds(33201, 180, 46, 0, widget);
		setBounds(33202, 313, 53, 1, widget);
		setBounds(33203, 313, 53, 2, widget);
		setBounds(33205, 185, 232, 3, widget);
		setBounds(33206, 250, 55, 4, widget);
		setBounds(33207, 215, 254, 5, widget);
		setBounds(33208, 215, 254, 6, widget);
		setBounds(33210, 258, 259, 7, widget);
		setBounds(33211, 185, 212, 8, widget);
		setBounds(33214, 185, 77, 9, widget);

		RSInterface scroll = addInterface(33214);
		scroll.width = 128;
		scroll.height = 135;
		scroll.scrollMax = 220;
		setChildren(20, scroll);
		for (int i = 0; i < 20; i += 2) {
			addClickableSprites(33215 + i, "Select", "Interfaces/Moderate/IMAGE", 3, 4);
			addText(33215 + i + 1, "Empty", tda, 0, 0xFF981F, false, true);
			setBounds(33215 + i, 0, i / 2 * 22, i, scroll);
			setBounds(33215 + i + 1, 4, 6 + (i / 2 * 22), i + 1, scroll);
		}
	}

	public static void addPestControlRewardWidget(TextDrawingArea[] tda) {
		RSInterface main = addInterface(37000);
		addSprite(37001, 0, "Interfaces/Pest Control/Reward/IMAGE");
		addButton(37002, 1, "Interfaces/Pest Control/Reward/IMAGE", "Confirm");
		addText(37003, "10 points", tda, 1, 0xFF981F, true, true);
		addHoverButton(37004, "Interfaces/Pest Control/Reward/IMAGE", 3, 21,
				21, "Close", -1, 37005, 3);
		addHoveredButton(37005, "Interfaces/Pest Control/Reward/IMAGE", 4, 21,
				21, 37006);
		addText(37007, "1,000 pts", tda, 2, 0xFF981F, false, true);
		setChildren(7, main);
		setBounds(37001, 0, 0, 0, main);
		setBounds(37002, 181, 273, 1, main);
		setBounds(37003, 253, 300, 2, main);
		setBounds(37004, 463, 14, 3, main);
		setBounds(37005, 463, 14, 4, main);
		setBounds(37007, 35, 19, 5, main);
		setBounds(37010, 27, 43, 6, main);

		RSInterface scroll = addInterface(37010);
		scroll.width = 442;
		scroll.height = 221;
		scroll.scrollMax = 520;
		setChildren(77, scroll);
		int x = 5;
		int y = 5;
		int imageId = 5;
		String[] names = new String[] { "Attack - 10,000 xp",
				"Defence - 10,000 xp", "Magic - 10,000 xp",
				"Prayer - 1,000 xp", "Strength - 10,000 xp",
				"Range - 10,000 xp", "Hitpoints - 3,300 xp" };
		for (int index = 0; index < 35; index += 5) {
			addSprite(37012 + index, imageId,
					"Interfaces/Pest Control/Reward/IMAGE");
			addText(37013 + index, names[index / 5], tda, 1, 0x339900, false,
					true);
			addClickableText(37014 + index, "(1 Pt)", "(1 Pt)", tda, 0,
					0xFF981F, false, true, 40);
			addClickableText(37015 + index, "(10 Pts)", "(10 Pts)", tda, 0,
					0xFF981F, false, true, 40);
			addClickableText(37016 + index, "(100 Pts)", "(100 Pts)", tda, 0,
					0xFF981F, false, true, 40);
			setBounds(37012 + index, x, y, index, scroll);
			setBounds(37013 + index, x + 32, y, index + 1, scroll);
			setBounds(37014 + index, x + 32, y + 16, index + 2, scroll);
			setBounds(37015 + index, x + 70, y + 16, index + 3, scroll);
			setBounds(37016 + index, x + 120, y + 16, index + 4, scroll);
			y += 40;
			if (imageId == 8) {
				x += 210;
				y = 5;
			}
			imageId++;
		}
		addSprite(37050, 2, "Interfaces/Pest Control/Reward/IMAGE");
		setBounds(37050, 53, 165, 35, scroll);
		addSprite(37051, 2, "Interfaces/Pest Control/Reward/IMAGE");
		setBounds(37051, 53, 265, 36, scroll);
		x = 5;
		y = 180;
		names = new String[] { "Herb Pack", "Seed Pack", "Mineral Pack",
				"Void Knight Mace", "Void Knight Robe", "Void Mage Helm",
				"Void Melee Helm", "Void Knight Top", "Void Knight Gloves",
				"Void Range Helm", "Fighter Torso", "Barrows Gloves",
				"Fighter Hat" };
		int[] items = new int[] { 257, 5295, 449, 8841, 8840, 11663, 11665,
				8839, 8842, 11664, 10551, 7462, 10548 };
		String[] costs = new String[] { "(30 Pts)", "(15 Pts)", "(15 Pts)",
				"(160 Pts)", "(175 Pts)", "(150 Pts)", "(150 Pts)",
				"(175 Pts)", "(110 Pts)", "(150 Pts)", "(300 Pts)",
				"(80 Pts)", "(60 Pts)" };
		for (int index = 0; index < 39; index += 3) {
			addText(37052 + index, names[index / 3], tda, 1, 0x339900, false,
					true);
			addClickableText(37053 + index, costs[index / 3], costs[index / 3],
					tda, 0, 0xFF981F, false, true, 40);
			addToItemGroup(37054 + index, 1, 1, 0, 0, false, "", "", "");
			interfaceCache[37054 + index].inv = new int[] { items[index / 3] + 1 };
			interfaceCache[37054 + index].invStackSizes = new int[] { 1 };
			setBounds(37052 + index, x + 32, y, 37 + index, scroll);
			setBounds(37053 + index, x + 32, y + 16, 37 + index + 1, scroll);
			setBounds(37054 + index, x, y, 37 + index + 2, scroll);
			y += 40;
			if (y == 220 && x == 215) {
				x = 5;
				y = 280;
			}
			if (x == 5 && y == 440) {
				x += 210;
				y = 280;
			}
			if (y == 260) {
				x += 210;
				y = 180;
			}
			if (x == 215 && y == 440) {
				x -= 210;
			}
			if (x == 5 && y == 480) {
				x = 215;
				y = 440;
			}
		}
		darken(37100, 200, 40, 0x000000, (byte) 100);
		setBounds(37100, 0, 0, 76, scroll);
	}

	public static void lootingBagAdd(TextDrawingArea[] tda) {
		RSInterface rsi = addTabInterface(39443);
		addSprite(39444, 0, "/Interfaces/Lootingbag/SPRITE");
		addText(39445, "Add items", tda, 2, 16750623, true, true);

		addHoverButton(39446, "/Interfaces/Lootingbag/CLOSE", 0, 128, 35, "Close", 0, 36484, 1);
		addHoveredButton(39447, "/Interfaces/Lootingbag/CLOSE", 1, 128, 35, 36485);
		rsi.totalChildren(32);

		int child = 0;

		rsi.child(child++, 39444, 7, 30);// bg
		rsi.child(child++, 39445, 90, 10);// title
		rsi.child(child++, 39446, 160, 9); // close button
		rsi.child(child++, 39447, 160, 9); // close hover

		/**
		 * Inventory
		 */
		final int START_X = 20;
		int x = START_X;
		int y = 35;
		final int X_DIFF = 38;
		final int Y_DIFF = 30;
		int id = 39448;

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 4; j++) {
				addItem(id, new String[] { "Deposit 1", "Deposit 5", "Deposit 10", "Deposit All", "Deposit X" });
				rsi.child(child++, id, x, y);
				id++;
				// System.out.println(child);
				x += X_DIFF;
			}

			x = START_X;
			y += Y_DIFF;

		}
		// System.out.println("Total children " + child);
	}

	public static void lootingBag(TextDrawingArea[] tda) {
		RSInterface rsi = addTabInterface(39342);
		addSprite(39343, 0, "/Interfaces/Lootingbag/SPRITE");
		addText(39344, "Looting bag", tda, 2, 16750623, true, true);

		addHoverButton(39345, "/Interfaces/Lootingbag/CLOSE", 0, 128, 35, "Close", 0, 36484, 1);
		addHoveredButton(39346, "/Interfaces/Lootingbag/CLOSE", 1, 128, 35, 36485);
		addHoverText(39347, "Bank All", "Bank all items", tda, 0, 0xff9040, false, true, 40);
		addText(39348, "Value: 0 coins", tda, 0, 0xFF9900, true, true);
		rsi.totalChildren(34);

		int child = 0;

		rsi.child(child++, 39343, 7, 19);// bg
		rsi.child(child++, 39344, 107, 1);// title
		rsi.child(child++, 39345, 160, 1); // close button
		rsi.child(child++, 39346, 160, 1); // close hover
		rsi.child(child++, 39347, 10, 4); // Bank All
		rsi.child(child++, 39348, 92, 250); // Total Value

		/**
		 * Inventory
		 */
		final int START_X = 15;
		int x = START_X;
		int y = 21;
		final int X_DIFF = 1;
		final int Y_DIFF = 2;
		int id = 39349;

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 4; j++) {
				addItem(id, new String[] { "Remove 1", "Remove 5", "Remove 10", "Remove All", "Remove X" });
				rsi.child(child++, id, x, y);
				id++;
				// System.out.println(child);
				x += X_DIFF;
			}

			x = START_X;
			y += Y_DIFF;

		}
	}

	public static void addItem(int childId, String[] options) {
		int interfaceId = 39342;

		RSInterface rsi = interfaceCache[childId] = new RSInterface();
		rsi.actions = new String[10];
		rsi.spritesX = new int[20];
		rsi.inv = new int[30];
		rsi.invStackSizes = new int[30];
		rsi.spritesY = new int[20];
		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];

		for (int i = 0; i < options.length; i++) {
			if (options[i] == null) {
				continue;
			}
			rsi.actions[i] = options[i];
		}

		rsi.centerText = true;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.invSpritePadX = 8;
		rsi.invSpritePadY = 1;
		rsi.height = 7;
		rsi.width = 4;
		rsi.parentID = interfaceId;
		rsi.id = childId;
		rsi.type = 2;
	}

	public static void addItem(int childId, String[] options, int interfaceId) {
		// int interfaceId = 37342;

		RSInterface rsi = interfaceCache[childId] = new RSInterface();
		rsi.actions = new String[10];
		rsi.spritesX = new int[20];
		rsi.inv = new int[30];
		rsi.invStackSizes = new int[25];
		rsi.spritesY = new int[20];
		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];

		for (int i = 0; i < options.length; i++) {
			if (options[i] == null) {
				continue;
			}
			rsi.actions[i] = options[i];
		}

		rsi.centerText = true;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.invSpritePadX = 23;
		rsi.invSpritePadY = 22;
		rsi.height = 5;
		rsi.width = 6;
		rsi.parentID = interfaceId;
		rsi.id = childId;
		rsi.type = 2;
	}

    public static void initializeTitleWidget(TextDrawingArea[] tda) {
        RSInterface widget = addInterface(53501);
        addSprite(53502, 0, "Interfaces/Titles/IMAGE");
        addSprite(53503, 6, "Interfaces/Titles/IMAGE");
        drawRoundedRectangle(53505, 200, 130, 0x000000, (byte) 30, true, true);
        addButton(53506, 1, "Interfaces/Titles/IMAGE", "Close", 3, 52);
        addButton(53508, 7, "Interfaces/Titles/IMAGE", "Ok");
        addText(53511, "Lorem ipsum dolor sit amet,\\n"
                        + "consectetur adipiscing elit,\\n"
                        + "sed do eiusmod tempor incididunt\\n"
                        + "ut labore et dolore magna aliqua.\\n"
                        + "fUt enim ad minim veniam, quis\\n"
                        + "nostrud exercitation ullamco \\n"
                        + "laboris nisi ut aliquip ex ea\\n" + "commodo consequat.",
                tda, 1, 0xFF981F, false, true);
        addText(53512, "Purchase", tda, 1, 0xFF981F, true, true);
        drawRoundedRectangle(53513, 140, 22, 0x000000, (byte) 30, true, true);
        addText(53514, "$", tda, 2, 0xFF981F, false, true);
        addText(53515, "45,000GP", tda, 1, 0xFF981F, false, true);
        addButton(53516, 9, "Interfaces/Titles/IMAGE", "Information");
        addSprites(53517, "Interfaces/Titles/IMAGE", 11, 12, 13);
        setChildren(15, widget);
        setBounds(53502, 56, 45, 0, widget);
        setBounds(53503, 250, 80, 1, widget);
        setBounds(53505, 223, 90, 2, widget);
        setBounds(53506, 435, 51, 3, widget);
        setBounds(53508, 277, 250, 4, widget);
        setBounds(53511, 230, 100, 5, widget);
        setBounds(53512, 320, 255, 6, widget);
        setBounds(53513, 250, 224, 7, widget);
        setBounds(53514, 255, 228, 8, widget);
        setBounds(53515, 268, 228, 9, widget);
        setBounds(53516, 418, 51, 10, widget);
        setBounds(53517, 370, 221, 11, widget);
        setBounds(53549, 61, 51, 12, widget);
        setBounds(53530, 0, 0, 13, widget);
        setBounds(53535, 0, 0, 14, widget);

        RSInterface scroll = addInterface(53549);
        scroll.width = 114;
        scroll.height = 230;
        scroll.scrollMax = 750;
        setChildren(70, scroll);

        for (int i = 0; i < 70; i += 2) {
            addClickableSprites(53550 + i, "View", "Interfaces/Titles/IMAGE",
                    3, 4, 5, 14);
            addText(53550 + i + 1, "Entry " + (1 + i / 2), tda, 1, 0xFF981F,
                    false, true);
            setBounds(53550 + i, 0, i / 2 * 22, i, scroll);
            setBounds(53550 + i + 1, 4, 3 + (i / 2 * 22), i + 1, scroll);
        }

        widget = addInterface(53530);
        setChildren(1, widget);
        addText(53531, "Selected Title", tda, 2, 0xFF981F, true, true);
        setBounds(53531, 320, 66, 0, widget);

        widget = addInterface(53535);
        setChildren(1, widget);
        addInputField(53536, 16, 0xFF981F, "Custom title", 120, 22, false,
                true, "[A-Za-z0-9 ]");
        setBounds(53536, 262, 58, 0, widget);
    }

	public static final void initializeCommandHelp() {
		for (int childId : interfaceCache[8143].children) {
			interfaceCache[childId].message = "";
		}
	}

	public static void teleportation(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(31000);
		addSprite(31001, 0, "Interfaces/PreloadingGear/IMAGE");
		addText(31002, "Teleportation Menu", tda, 2, 0xFF981F);
		addButton(31005, 16, "Interfaces/PreloadingGear/IMAGE", 290, 100, new String[] { "Teleport" });
		addText(31004, "Teleport", tda, 1, 0xFF981F);
		addHoverButton(31006, "Interfaces/PreloadingGear/IMAGE", 3, 16, 16, "Close", -1, 32007, 3);
		setChildren(10, widget);
		setBounds(31001, 12, 1, 0, widget);
		setBounds(31002, 190, 13, 1, widget);
		setBounds(31005, 270, 290, 2, widget);
		setBounds(31004, 301, 295, 3, widget);
		setBounds(31050, 17, 29 + 7, 4, widget);
		setBounds(31010, 165, 35 + 7, 5, widget);
		setBounds(31030, 350, 35 + 7, 6, widget);
		setBounds(31006, 472, 11, 7, widget);

		// Left teleport
		RSInterface teleport_buttons = addInterface(31010);
		setChildren(6, teleport_buttons);
		int height_one = 0;
		int childId_one = 0;
		for (int id_two = 0; id_two < 18; id_two += 3) {
			RSInterface teleport_button = addInterface(31011 + id_two);
			addButton(31011 + id_two + 1, 1, "Interfaces/PreloadingGear/IMAGE", "View");
			addClickableSprites(31011 + id_two + 1, "Select", "Interfaces/PreloadingGear/IMAGE", 1, 2);
			addText(31011 + id_two + 2, "", tda, 1, 0xFF981F);
			setChildren(2, teleport_button);
			setBounds(31011 + id_two + 1, 0, 0, 0, teleport_button);
			setBounds(31011 + id_two + 2, 5, 7, 1, teleport_button);
			setBounds(31011 + id_two, 0, height_one, childId_one, teleport_buttons);
			childId_one++;
			height_one += 29;
		}

		// Right teleport
		RSInterface teleport_buttons_two = addInterface(31030);
		setChildren(6, teleport_buttons_two);
		int height_two = 0;
		int childId_two = 0;
		for (int id_one = 0; id_one < 18; id_one += 3) {
			RSInterface teleport_button_two = addInterface(31031 + id_one);
			addClickableSprites(31031 + id_one + 1, "Select", "Interfaces/PreloadingGear/IMAGE", 1, 2);
			addText(31031 + id_one + 2, "", tda, 1, 0xFF981F);
			setChildren(2, teleport_button_two);
			setBounds(31031 + id_one + 1, 0, 0, 0, teleport_button_two);
			setBounds(31031 + id_one + 2, 5, 7, 1, teleport_button_two);
			setBounds(31031 + id_one, 0, height_two, childId_two, teleport_buttons_two);
			childId_two++;
			height_two += 29;
		}

		// Menu
		RSInterface buttons = addInterface(31050);
		setChildren(6, buttons);
		int height = 0;
		int childId = 0; // haha mb, sec
		for (int id = 0; id < 18; id += 3) {
			RSInterface button = addInterface(31051 + id);
			addClickableSprites(31051 + id + 1, "Select", "Interfaces/PreloadingGear/IMAGE", 1, 2);
			addText(31051 + id + 2, "Menu", tda, 1, 0xFF981F);
			setChildren(2, button);
			setBounds(31051 + id + 1, 0, 0, 0, button);
			setBounds(31051 + id + 2, 5, 7, 1, button);
			setBounds(31051 + id, 0, height, childId, buttons);
			childId++;
			height += 29;
		}

		addText(31003,
				"Teleportation Menu \\n used for quick access \\n to various areas \\n throughout the world \\n \\n Use with care \\n and with caution \\n \\n Travel safely.",
				tda, 1, 0xFF981F);
		setBounds(31003, 17, 212, 8, widget);
		addText(31007,
				"By clicking teleport below, you are aware that you might \\n have chosen a location which may be placed within \\n the wilderness. And is aware of the risks that follows.",
				tda, 1, 0xFF981F);
		setBounds(31007, 170, 238, 9, widget);

	}

	public static void preloadEquipmentWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(32000);
		addSprite(32001, 0, "Interfaces/PreloadingGear/IMAGE");
		addText(32003, "Preloaded Gear Management", tda, 2, 0xFF981F);
		addHoverButton(32004, "Interfaces/PreloadingGear/IMAGE", 3, 16, 16, "Dismiss", -1, 32005, 3);
		addHoveredButton(32005, "Interfaces/PreloadingGear/IMAGE", 4, 16, 16, 32006);
		addSprite(32006, 12, "Interfaces/PreloadingGear/IMAGE");
		addItemChooser(32007, tda);
		addInputField(32002, 14, 0xFF981F, "Name", 120, 22, false, false, "[A-Za-z0-9 ]");
		setChildren(10, widget);
		setBounds(32001, 12, 1, 0, widget);
		setBounds(32030, 17, 29 + 7, 1, widget);
		setBounds(32003, 170, 13, 2, widget);
		setBounds(32004, 472, 11, 3, widget);
		setBounds(32005, 472, 11, 4, widget);
		setBounds(32006, 313, 55, 5, widget);
		setBounds(32100, 165, 55, 6, widget);
		setBounds(32200, 0, -10, 7, widget);
		setBounds(32002, 340, 283, 8, widget);
		setBounds(32007, 125, 65, 9, widget);

		RSInterface buttons = addInterface(32030);
		setChildren(10, buttons);
		int height = 0;
		int childId = 0;
		for (int id = 0; id < 30; id += 3) {
			RSInterface button = addInterface(32031 + id);
			addButton(32031 + id + 1, 1, "Interfaces/PreloadingGear/IMAGE", "View");
			addButton(32031 + id + 1, 1, "Interfaces/PreloadingGear/IMAGE", 130, 29,
					new String[] { "Load inventory", "Load equipment", "View" });
			addText(32031 + id + 2, "New slot", tda, 1, 0xFF981F);
			setChildren(2, button);
			setBounds(32031 + id + 1, 0, 0, 0, button);
			setBounds(32031 + id + 2, 5, 7, 1, button);
			setBounds(32031 + id, 0, height, childId, buttons);
			childId++;
			height += 29;
		}

		RSInterface inventory = addInterface(32100);
		setChildren(28, inventory);
		int index = 0;
		for (int row = 0; row < 7; row++) {
			for (int collumn = 0; collumn < 4; collumn++) {
				// addButton(32101 + index, 13, "Interfaces/PreloadingGear/IMAGE", 36, 36, new
				// String[] { "Delete", "Edit" });
				addToItemGroup(32129 + index, 1, 1, 0, 0, false, "", "", "");
				interfaceCache[32129 + index].sprite2 = new Sprite("Interfaces/PreloadingGear/IMAGE 13");
				// setBounds(32101 + index, collumn * 36, row * 36, index,inventory);
				setBounds(32129 + index, collumn * 36 + 2, row * 36 + 2, index, inventory);
				index++;
			}
		}

		RSInterface equipment = addInterface(32200);
		setChildren(11, equipment);
		int xLocation[] = { 389, 348, 389, 333, 389, 445, 389, 333, 389, 445, 430 };
		int yLocation[] = { 78, 117, 117, 156, 156, 156, 196, 236, 236, 236, 117 };
		for (int i = 0; i < 11; i++) {
			// addButton(32201 + i, 14, "Interfaces/PreloadingGear/IMAGE", 36, 36,new
			// String[] { "Delete", "Edit" });
			addToItemGroup(32212 + i, 1, 1, 0, 0, false, "", "", "");
			interfaceCache[32212 + i].sprite2 = new Sprite("Interfaces/PreloadingGear/IMAGE 17");
			// setBounds(32201 + i, xLocation[i] - 3, yLocation[i] - 3, i,equipment);
			setBounds(32212 + i, xLocation[i] - 5, yLocation[i] - 8, i, equipment);
		}
	}

	// You can draw this interface in the chat area

	public static void addItemChooser(int interfaceId, TextDrawingArea[] tda) {
		RSInterface widget = addTabInterface(interfaceId);
		addSprite(interfaceId + 1, 19, "Interfaces/PreloadingGear/IMAGE");
		addInputField(interfaceId + 2, 50, 0xFF981F, "Name", 242, 21, false, true);
		addInputField(interfaceId + 5, 10, 0xFF981F, "Amount", 110, 21, false, true, "[\\d]");
		addButton(interfaceId + 6, 18, "Interfaces/PreloadingGear/IMAGE", "Submit");
		addText(interfaceId + 7, "Submit", tda, 0, 0xFF981F);
		addHoverButton(interfaceId + 8, "Interfaces/PreloadingGear/IMAGE", 3, 16, 16, "Cancel", -1, interfaceId + 9, 1);
		addHoveredButton(interfaceId + 9, "Interfaces/PreloadingGear/IMAGE", 4, 16, 16, interfaceId + 10);
		addConfigButton(interfaceId + 11, interfaceId, 20, 21, "Interfaces/PreloadingGear/IMAGE", 14, 15, "Toggle", 1,
				4, 810);
		addTooltip(interfaceId + 12, "When toggled on, item searching is limited to a player's bank.\n"
				+ "When off, any item name that contains your input will be visible.");
		interfaceCache[interfaceId + 11].mOverInterToTrigger = interfaceId + 12;
		darken(interfaceId + 14, 487, 332, 0x000000, (byte) 110);
		setChildren(11, widget);
		setBounds(interfaceId + 14, -113, -64, 0, widget);
		setBounds(interfaceId + 1, 0, 0, 1, widget);
		setBounds(interfaceId + 2, 5, 160, 2, widget);
		setBounds(interfaceId + 3, 5, 6, 3, widget);
		setBounds(interfaceId + 5, 5, 182, 4, widget);
		setBounds(interfaceId + 6, 117, 182, 5, widget);
		setBounds(interfaceId + 7, 150, 187, 6, widget);
		setBounds(interfaceId + 8, 216, 6, 7, widget);
		setBounds(interfaceId + 9, 216, 6, 8, widget);
		setBounds(interfaceId + 11, 230, 184, 9, widget);
		setBounds(interfaceId + 12, 0, 210, 10, widget);
		RSInterface scroll = addInterface(interfaceId + 3);
		scroll.scrollMax = 32 * 10 + 30;
		scroll.height = 154;
		scroll.width = 226;
		setChildren(1, scroll);
		int x = 0, y = 0;
		addToItemGroup(interfaceId + 4, 6, 10, 3, 3, false, "", "", "");
		setBounds(interfaceId + 4, x + 5, y + 5, 0, scroll);
		for (int i = 0; i < widget.children.length - 1; i++) {
			interfaceCache[interfaceId + i].isItemSearchComponent = true;
		}
	}

	public static void addSpawnChooser(int interfaceId, TextDrawingArea[] tda) {
		RSInterface widget = addInterface(interfaceId);
		addSprite(interfaceId + 1, 0, "Interfaces/PreloadingGear/IMAGE");
		addInputField(interfaceId + 2, 50, 0xFF981F, "Name", 120, 21, false, true);
		addInputField(interfaceId + 5, 10, 0xFF981F, "Amount", 93, 21, false, true, "[\\d]");
		addButton(interfaceId + 6, 18, "Interfaces/PreloadingGear/IMAGE", "Submit");
		addText(interfaceId + 7, "Spawn", tda, 0, 0xFF981F);
		addHoverButton(interfaceId + 8, "Interfaces/PreloadingGear/IMAGE", 3, 16, 16, "Cancel", -1, interfaceId + 9, 1);
		addHoveredButton(interfaceId + 9, "Interfaces/PreloadingGear/IMAGE", 4, 16, 16, interfaceId + 10);
		addConfigButton(interfaceId + 11, interfaceId, 20, 21, "Interfaces/PreloadingGear/IMAGE", 14, 15,
				"Toggle Noted Items", 1, 4, 810);
		addTooltip(interfaceId + 12, "When toggled on, item searching is limited to a player's bank.\n"
				+ "When off, any item name that contains your input will be visible.");
		addText(interfaceId + 13,
				"Below you are able \\n to input item name and \\n amount which you \\n then is able to spawn. \\n \\n By toggling the button \\n you change between \\n noted and unnoted \\n items.",
				tda, 0, 0xFF981F);
		addText(interfaceId + 14, "Item Spawner", tda, 2, 0xFF981F);
		interfaceCache[interfaceId + 11].mOverInterToTrigger = interfaceId + 12;
		// darken(interfaceId + 14, 487, 332, 0x000000, (byte) 110);
		setChildren(12, widget);
		// setBounds(interfaceId + 14, -113, -64, 0, widget);
		setBounds(interfaceId + 1, 15, 2, 0, widget); // Background
		setBounds(interfaceId + 2, 25, 300, 1, widget); // Name
		setBounds(interfaceId + 5, 25, 275, 3, widget); // Amount
		setBounds(interfaceId + 3, -40, 40, 2, widget); // Scroll
		setBounds(interfaceId + 6, 25, 245, 4, widget); // Spawn button 33-
		setBounds(interfaceId + 7, 58, 249, 5, widget); // Spawn text
		setBounds(interfaceId + 8, 479, 10, 6, widget); // Close button
		setBounds(interfaceId + 9, 216, 6, 7, widget);
		setBounds(interfaceId + 11, 130, 275, 8, widget); // Tick
		setBounds(interfaceId + 12, 130, 275, 9, widget); // Tooltip
		setBounds(interfaceId + 13, 30, 110, 10, widget); // Text
		setBounds(interfaceId + 14, 230, 10, 11, widget); // Text

		RSInterface scroll = addInterface(interfaceId + 3);
		scroll.scrollMax = 32 * 10 + 30;
		scroll.height = 282;
		scroll.width = 520;
		setChildren(1, scroll);
		int x = 205, y = 0;
		addToItemGroup(interfaceId + 4, 8, 8, 5, 5, false, "Spawn 1", "Spawn 5", "Spawn 10", "Spawn 100", null, null);
		setBounds(interfaceId + 4, x + 8, y + 8, 0, scroll);
		for (int i = 1; i < widget.children.length - 1; i++) {
			interfaceCache[interfaceId + i].isItemSearchComponent = true;
		}
	}

	public static void updateShopWidget(TextDrawingArea[] tda) {
		RSInterface widget = interfaceCache[3824];
		int[] childrenId = new int[widget.children.length + 1];
		int[] childrenX = new int[widget.children.length + 1];
		int[] childrenY = new int[widget.children.length + 1];
		for (int i = 0; i < widget.children.length; i++) {
			childrenId[i] = widget.children[i];
			childrenX[i] = widget.childX[i];
			childrenY[i] = widget.childY[i];
		}
		setChildren(93, widget);

		for (int i = 0; i < widget.children.length; i++) {
			setBounds(childrenId[i], childrenX[i], childrenY[i], i, widget);
		}
		setBounds(28050, 0, 0, 92, widget);

		RSInterface subWidget = addInterface(28050);
		setChildren(2, subWidget);
		addSprite(28051, 2, "Interfaces/BountyHunter/IMAGE");
		addText(28052, "Bounties:", tda, 1, 0xFFFF00, false, true);
		setBounds(28051, 20, 30, 0, subWidget);
		setBounds(28052, 48, 30, 1, subWidget);
	}

	public static void shopWidget(TextDrawingArea[] tda) {
		// Interface background
		RSInterface widget = addInterface(64000);
		setChildren(5, widget);
		addSprite(64001, 1, "Interfaces/Shop/IMAGE");
		addHoverButton(64002, "Interfaces/Shop/IMAGE", 2, 21, 21, "Close Window", 201, 64002, 5);
		addText(64003, "Tony's store", tda, 2, 0xFFA500, false, true);
		setBounds(64001, 10, 10, 0, widget);
		setBounds(64015, 20, 45, 1, widget);
		setBounds(64002, 485, 20, 2, widget);
		setBounds(64003, 200, 20, 3, widget);
		setBounds(64017, 0, 0, 4, widget);

		// Scroll
		RSInterface scroll = addInterface(64015);
		setChildren(1, scroll);
		scroll.height = 252;
		scroll.width = 465;
		scroll.scrollMax = 800;

		// Item container
		addToItemGroup(64016, 10, 15, 14, 14, true, "Value", "Buy 1", "Buy 5", "Buy 10", "Buy X", null);
		setBounds(64016, 8, 8, 0, scroll);

		// Bounty hunter widget
		RSInterface subWidget = addInterface(64017);
		setChildren(2, subWidget);
		addSprite(64018, 2, "Interfaces/BountyHunter/IMAGE");
		addText(64019, "Bounties:", tda, 1, 0xFFFF00, false, true);
		setBounds(64018, 20, 20, 0, subWidget);
		setBounds(64019, 48, 20, 1, subWidget);
	}
	private static void caelTournamentSupplies(TextDrawingArea[] tda) {
		RSInterface main = addInterface(55100);

		RSInterface widget = addInterface(55106);

		addSprite(55101, 0, "int/twbase/sprite");

		addHoverButton(55102, "int/twbase/sprite", 1, 16, 16,
				"Close Window", 0, 55104, 4);
		addHoveredButton(55104, "interfaces/twbase/sprite", 2, 16, 16, 55103);

		addText(55105, "Tournament Supplies", tda, 2, 0xE68A00, true, true);

		addInputField(55120, 50, 0xE68A00, "Search", 132, 23, false, true);

		addToItemGroup(55121, 8, 80, 25, 10, true, "Value", "Buy 1", "Buy 5", "Buy 10", "Buy X", null);

		setChildren(6, main);
		main.child(0, 55101, 13, 10);
		main.child(1, 55102, 475, 18);
		main.child(2, 55104, 475, 18);
		main.child(3, 55105, 250, 19);
		main.child(4, 55120, 180, 279);
		main.child(5, 55106, 12, 57);

		setChildren(1, widget);
		widget.child(0, 55121, 12, 12);

		widget.width = 450;

		widget.height = 223;

		widget.scrollMax = 450;
	}
	public static void bountyHunterWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(28000);
		addTransparentSprite(28001, 1, "Interfaces/BountyHunter/IMAGE", 20);
		addText(28003, "Target:", tda, 0, 0xFFFF00, false, true);
		addText(28004, "Abnant", tda, 1, 0xFFFFFF, true, true);
		addText(28005, "Lvl 1-4, Cmb 70", tda, 0, 0xCC0000, true, true);
		addText(28006, "Wealth: V. Low", tda, 0, 0xFFFF00, true, true);
		setChildren(14, widget);
		setBounds(28001, 320, 15, 0, widget);
		setBounds(28003, 440, 18, 1, widget);
		setBounds(28004, 458, 31, 2, widget);
		setBounds(28005, 458, 47, 3, widget);
		setBounds(28006, 359, 47, 4, widget);

		/** TODO WIldy Skull **/
		setBounds(196, Client.currentScreenMode != ScreenMode.FIXED ? 600 : 420,
				Client.currentScreenMode != ScreenMode.FIXED ? 186 : 286, 5, widget);

		setBounds(28030, 345, 25, 6, widget);
		setBounds(28032, 345, 25, 7, widget);
		setBounds(28034, 345, 25, 8, widget);
		setBounds(28036, 345, 25, 9, widget);
		setBounds(28038, 345, 25, 10, widget);
		setBounds(28040, 345, 25, 11, widget);
		setBounds(28020, 0, 5, 12, widget);
		setBounds(28070, 0, 5, 13, widget);

		RSInterface sprite;
		int imageId = 2;
		for (int i = 0; i < 12; i += 2) {
			sprite = addInterface(28030 + i);
			addSprite(28031 + i, imageId++, "Interfaces/BountyHunter/IMAGE");
			setChildren(1, sprite);
			setBounds(28031 + i, 0, 0, 0, sprite);
		}

		RSInterface statistics = addInterface(28020);
		setChildren(9, statistics);
		addTransparentSprite(28021, 0, "Interfaces/BountyHunter/IMAGE", 20);
		addText(28022, "Current  Record", tda, 0, 0xFFFF00, false, true);
		addText(28023, "Rogue:", tda, 0, 0xFFFF00, false, true);
		addText(28024, "Hunter:", tda, 0, 0xFFFF00, false, true);
		addText(28025, "1", tda, 0, 0xFFFF00, true, true);
		addText(28026, "2", tda, 0, 0xFFFF00, true, true);
		addText(28027, "3", tda, 0, 0xFFFF00, true, true);
		addText(28028, "4", tda, 0, 0xFFFF00, true, true);
		addSprite(28029, 8, "Interfaces/BountyHunter/IMAGE");

		setBounds(28021, 340, 58, 0, statistics);
		setBounds(28022, 420, 60, 1, statistics);
		setBounds(28023, 375, 73, 2, statistics);
		setBounds(28024, 375, 87, 3, statistics);
		setBounds(28025, 440, 73, 4, statistics);
		setBounds(28026, 440, 87, 5, statistics);
		setBounds(28027, 481, 73, 6, statistics);
		setBounds(28028, 481, 87, 7, statistics);
		setBounds(28029, 347, 74, 8, statistics);

		RSInterface timerWidget = addInterface(28070);
		addTransparentSprite(28071, 10, "Interfaces/BountyHunter/IMAGE", 20);
		addText(28072, "0:59", tda, 0, 0xff9040, true, true);
		setChildren(2, timerWidget);
		setBounds(28071, 293, 10, 0, timerWidget);
		setBounds(28072, 307, 27, 1, timerWidget);
	}

	public static void PVPInterface(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21200);
		addSprite(21201, 0, "PVP/NOTINWILD1");
		addText(21202, "", tda, 1, 0xff9040, true, true);
		int last = 2;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21201, 400, 285, 0, RSinterface);
		setBounds(21202, 444, 318, 1, RSinterface);
	}

	public static void PVPInterface2(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21300);
		addSprite(21301, 0, "PVP/INWILD1");
		addText(21302, "", tda, 1, 0xff9040, true, true);
		int last = 2;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21301, 400, 285, 0, RSinterface);
		setBounds(21302, 444, 318, 1, RSinterface);
	}

	public static void PVPInterface3(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21400);
		addSprite(21401, 0, "PVP/INCOUNT1");
		addText(21402, "", tda, 1, 0xff9040, true, true);
		addText(21403, "", tda, 1, 0xffffff, true, true);
		int last = 3;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21401, 400, 285, 0, RSinterface);
		setBounds(21402, 444, 318, 1, RSinterface);
		setBounds(21403, 412, 290, 2, RSinterface);
	}

	public static void helpComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(59525);
		addSprite(59526, 1, "Interfaces/HelpInterface/IMAGE");
		addInputField(59527, 200, 0xFF981F, "Describe the bug you've experienced. (200 characters max)", 430, 28, false,
				false, "[A-Za-z0-9 .,]");
		addText(59528, "Help Request", tda, 2, 0xFF981F, true, true);
		addText(59529,
				"You are only allowed to request help when you need it. Situations such as being stuck,\\n"
						+ "threatened by another player, problems with a donation, or experiencing a bug are all\\n"
						+ "viable uses of the help system. Improper use of this system may lead to punishment.",
				tda, 0, 0xFF981F, false, true);
		addHoverButton(59530, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 59531, 3);
		addHoveredButton(59531, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 59532);
		setChildren(6, widget);
		setBounds(59526, 33, 106, 0, widget);
		setBounds(59527, 40, 192, 1, widget);
		setBounds(59528, 256, 113, 2, widget);
		setBounds(59529, 40, 135, 3, widget);
		setBounds(59530, 456, 112, 4, widget);
		setBounds(59531, 456, 112, 5, widget);
	}

	public static void barrowsReward(TextDrawingArea[] wid) {
		RSInterface tab = addInterface(64500);
		addSprite(64501, 0, "Interfaces/Bankpin/IMAGE");
		addHoverButton(64502, "Interfaces/Shop/IMAGE", 2, 21, 21, "Close Window", 201, 64502, 5);
		setChildren(3, tab);
		setBounds(64501, 256 - 140, 120, 0, tab);
		setBounds(64502, 374, 127, 1, tab);

		// Item container
		addToItemGroup(64503, 6, 2, 5, 5, false, "", "", "");
		setBounds(64503, 148, 155, 2, tab);
	}

	public static void staffInterface(TextDrawingArea[] wid) {
		RSInterface tab = addInterface(36000);
		addSprite(36001, 0, "Interfaces/PreloadingGear/IMAGE");
		addText(36002, "Staff Panel; Checking user: Limp", wid, 2, 0xFF981F, true, true);
		addText(36003, "Select an option:", wid, 2, 0xFF981F, true, true);
		addHoverButton(36084, "Interfaces/Shop/IMAGE", 2, 21, 21, "Close Window", 201, 36083, 5);
		setChildren(15, tab);
		setBounds(36001, 15, 0, 0, tab);
		setBounds(36002, 256, 12, 1, tab);
		setBounds(36003, 85, 35, 2, tab);
		setBounds(36025, 160, 35, 3, tab);
		setBounds(36080, 235, 35, 4, tab);
		setBounds(36082, 370, 35, 5, tab);
		setBounds(36084, 480, 10, 6, tab);

		int h = 60;
		for (int i = 7; i < 15; i++) {
			// System.out.println("h = " + h);
			addClickableText(36001 + i, "ID " + i, "Select", wid, 1, 0xFF981F, false, true, 40);
			setBounds(36001 + i, 50, h, i, tab);
			h += 30;
		}

		/**
		 * Stats and various info
		 */
		RSInterface stats = addInterface(36025);
		setChildren(44, stats);
		stats.height = 290;
		stats.width = 50;
		stats.scrollMax = 680;

		int height = 15;
		for (int i = 0; i < 22; i++) {
			addSprite(36026 + i, i, "Interfaces/StaffInterface/IMG");
			setBounds(36026 + i, 0, height, i, stats);
			addText(36048 + i, "99", wid, 0, 0xFF981F, true, true);
			setBounds(36048 + i, 30, height, 22 + i, stats);
			height += 30;
		}

		/**
		 * Equipment
		 */
		RSInterface equipment = addInterface(36080);
		setChildren(1, equipment);
		equipment.height = 290;
		equipment.width = 115;
		equipment.scrollMax = 680;
		addToItemGroup(36081, 3, 10, 9, 9, true, null, null, null);
		setBounds(36081, 0, 5, 0, equipment);

		/**
		 * Inventory
		 */
		RSInterface inventory = addInterface(36082);
		setChildren(1, inventory);
		inventory.height = 290;
		inventory.width = 110;
		inventory.scrollMax = 680;
		addToItemGroup(36083, 3, 10, 9, 9, true, null, null, null);
		setBounds(36083, 0, 5, 0, inventory);
	}

	public static void staffInterfaceBank(TextDrawingArea[] wid) {
		RSInterface tab = addInterface(36100);
		addSprite(36101, 0, "Interfaces/PreloadingGear/IMAGE");
		addText(36102, "Staff Panel; Checking user: Limp", wid, 2, 0xFF981F, true, true);
		addText(36103, "Select an option:", wid, 2, 0xFF981F, true, true);
		addHoverButton(36184, "Interfaces/Shop/IMAGE", 2, 21, 21, "Close Window", 201, 36183, 5);
		setChildren(13, tab);
		setBounds(36101, 15, 0, 0, tab);
		setBounds(36102, 256, 12, 1, tab);
		setBounds(36103, 85, 35, 2, tab);
		setBounds(36182, 160, 35, 3, tab);
		setBounds(36184, 480, 10, 4, tab);

		int h = 60;
		for (int i = 5; i < 13; i++) {
			// System.out.println("h = " + h);
			addClickableText(36101 + i, "ID " + i, "Select", wid, 1, 0xFF981F, false, true, 40);
			setBounds(36101 + i, 50, h, i, tab);
			h += 30;
		}

		/**
		 * Item container
		 */
		RSInterface inventory = addInterface(36182);
		setChildren(1, inventory);
		inventory.height = 290;
		inventory.width = 320;
		inventory.scrollMax = 2500;
		addToItemGroup(36183, 8, 50, 9, 9, true, null, null, null);
		setBounds(36183, 0, 5, 0, inventory);
	}

	public static void wellOfGoodWill(TextDrawingArea[] tda) {
		RSInterface wogw = addInterface(38000);
		addSprite(38001, 0, "Interfaces/Wogw/IMAGE");
		addText(38002, "Well Of Goodwill", tda, 2, 0xFF981F, true, true);
		addText(38003, "Experience (x2)", tda, 2, 0xFF981F, true, true);
		addText(38004, "PC Points (+5)", tda, 2, 0xFF981F, true, true);
		addText(38005, "Drops (x2 rate)", tda, 2, 0xFF981F, true, true);
		addClickableSprites(38006, "Toggle", "Interfaces/Ironman/IMAGE", 2, 3, 4);
		addClickableSprites(38007, "Toggle", "Interfaces/Ironman/IMAGE", 2, 3, 4);
		addClickableSprites(38008, "Toggle", "Interfaces/Ironman/IMAGE", 2, 3, 4);
		addText(38009, "How much would you like to donate?", tda, 0, 0xFF981F, true, true);
		addHoverButton(38010, "Interfaces/Shop/IMAGE", 2, 21, 21, "Close Window", 201, 38010, 5);
		addInputField(38011, 120, 0xFF981F, "Amount of coins", 144, 20, false, false, "[0-9]");
		addText(38012, "10M/200M", tda, 0, 0xFF981F, true, true);
		addText(38013, "10M/200M", tda, 0, 0xFF981F, true, true);
		addText(38014, "10M/200M", tda, 0, 0xFF981F, true, true);
		setChildren(16, wogw);
		setBounds(38001, 256 - 120, 20, 0, wogw);
		setBounds(38002, 270, 30, 1, wogw);
		setBounds(38003, 290, 60, 2, wogw);
		setBounds(38004, 273, 90, 3, wogw);
		setBounds(38005, 260, 120, 4, wogw);
		setBounds(38006, 360, 60, 5, wogw);
		setBounds(38007, 360, 90, 6, wogw);
		setBounds(38008, 360, 120, 7, wogw);
		setBounds(38009, 260, 170, 8, wogw);
		setBounds(38010, 366, 26, 9, wogw);
		setBounds(38011, 190, 190, 10, wogw);
		setBounds(38012, 183, 63, 11, wogw);
		setBounds(38013, 183, 93, 12, wogw);
		setBounds(38014, 183, 123, 13, wogw);
		setBounds(38020, 0, 0, 14, wogw);
		setBounds(38030, 0, 0, 15, wogw);

		RSInterface confirmation = addInterface(38020);
		setChildren(6, confirmation);
		addSprite(38021, 0, "Interfaces/Wogw/IMAGE");
		addText(38022, "Are you sure you want to contribute\\n# gp?", tda, 2, 0xFF981F, false, true);
		addHoverButton(38023, "Interfaces/Ironman/IMAGE", 4, 21, 21, "Yes", 201, 38023, 5);
		addHoverButton(38025, "Interfaces/Ironman/IMAGE", 3, 21, 21, "No", 201, 38023, 5);
		addText(38024, "YES", tda, 0, 0xFF981F, false, true);
		addText(38026, "NO", tda, 0, 0xFF981F, false, true);
		setBounds(38021, 256 - 120, 20, 0, confirmation);
		setBounds(38022, 145, 82, 1, confirmation);
		setBounds(38023, 220, 152, 2, confirmation); // Yes button
		setBounds(38024, 220, 140, 3, confirmation); // Yes text
		setBounds(38025, 288, 152, 4, confirmation); // No button
		setBounds(38026, 290, 140, 5, confirmation); // No text

		RSInterface last = addInterface(38030);
		addSprite(38031, 1, "Interfaces/Wogw/IMAGE");
		addText(38032, "Recent Donators", tda, 2, 0xFF981F, false, true);
		addText(38033, "Limp donated 10.000.000 towards x2 experience", tda, 0, 0xFF981F, false, true);
		addText(38034, "Limp donated 10.000.000 towards +5 PC Points", tda, 0, 0xFF981F, false, true);
		addText(38035, "Limp donated 10.000.000 towards Double drops", tda, 0, 0xFF981F, false, true);
		addText(38036, "Limp donated 10.000.000 towards Double drops", tda, 0, 0xFF981F, false, true);
		addText(38037, "Limp donated 10.000.000 towards Double drops", tda, 0, 0xFF981F, false, true);
		setChildren(7, last);
		setBounds(38031, 3, 235, 0, last);
		setBounds(38032, 200, 240, 1, last);
		setBounds(38033, 140, 260, 2, last);
		setBounds(38034, 140, 272, 3, last);
		setBounds(38035, 140, 284, 4, last);
		setBounds(38036, 140, 296, 5, last);
		setBounds(38037, 140, 308, 6, last);
	}

	public static void safeBox(TextDrawingArea[] wid) {
		RSInterface tab = addInterface(35000);
		addSprite(35001, 19, "Interfaces/PreloadingGear/IMAGE");
		addHoverButton(35002, "Interfaces/Shop/IMAGE", 2, 21, 21, "Close Window", 201, 35002, 5);
		addHoverButton(35008, "Interfaces/Clan Chat/plus", 1, 21, 21, "Add more slots", 201, 35008, 5);
		addText(35003, "Safe Deposit Box", wid, 2, 0xFF981F, false, true);
		addText(35004, "These items will be saved when you leave \\n the PK-District.", wid, 0, 0xFF981F, false, true);
		addText(35005, "0/50", wid, 3, 0xFF981F, false, true);
		addText(35009, "Add more slots", wid, 0, 0xFFFFFF, false, true);
		setChildren(9, tab);
		setBounds(35001, 256 - 120, 60, 0, tab);
		setBounds(35002, 366, 66, 1, tab);
		setBounds(35003, 205, 66, 2, tab);
		setBounds(35004, 160, 82, 3, tab);
		setBounds(35005, 245, 110, 4, tab);
		setBounds(35006, 100, 128, 5, tab);
		setBounds(35008, 330, 113, 6, tab);
		setBounds(35009, 300, 100, 7, tab);
		setBounds(35010, 0, 0, 8, tab);

		RSInterface slotPurchase = addInterface(35010);
		setChildren(6, slotPurchase);
		darken(35011, 250, 210, 0x000000, (byte) 126);
		addText(35012, "Are you sure you want to purchase\\n1 extra slot for 15 blood money?", wid, 0, 0xFF981F, false,
				true);
		addHoverButton(35013, "Interfaces/Ironman/IMAGE", 4, 21, 21, "Yes", 201, 35013, 5);
		addHoverButton(35015, "Interfaces/Ironman/IMAGE", 3, 21, 21, "No", 201, 35013, 5);
		addText(35014, "YES", wid, 0, 0xFF981F, false, true);
		addText(35016, "NO", wid, 0, 0xFF981F, false, true);
		setBounds(35011, 135, 60, 0, slotPurchase);
		setBounds(35012, 170, 142, 1, slotPurchase);
		setBounds(35013, 210, 202, 2, slotPurchase); // Yes button
		setBounds(35014, 210, 190, 3, slotPurchase); // Yes text
		setBounds(35015, 278, 202, 4, slotPurchase); // No button
		setBounds(35016, 280, 190, 5, slotPurchase); // No text

		// Scroll
		RSInterface scroller = addInterface(35006);
		setChildren(1, scroller);
		scroller.height = 130;
		scroller.width = 266;
		scroller.scrollMax = 420;

		// Item container
		addToItemGroup(35007, 5, 10, 9, 9, true, "Withdraw", null, null);
		setBounds(35007, 50, 5, 0, scroller);
	}

	public static void bankPin(TextDrawingArea[] wid) {
		RSInterface tab = addInterface(59500);
		addSprite(59501, 0, "Interfaces/Bankpin/IMAGE");
		addText(59502, "Account Pin", wid, 2, 0xFF981F, true, true);
		addText(59503, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59504, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59505, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59506, "Press enter to submit", wid, 2, 0xFF981F, true, true);
		addInputField(59507, 8, 0xFF981F, "", 100, 24, true);
		addHoverButton(59508, "Interfaces/Bankpin/IMAGE", 1, 16, 16, "Close", 375, 59509, 3);
		addHoveredButton(59509, "Interfaces/Bankpin/IMAGE", 2, 16, 16, 59510);
		setChildren(9, tab);
		setBounds(59501, 256 - 140, 120, 0, tab);
		setBounds(59502, 256, 132, 1, tab);
		setBounds(59503, 256, 150, 2, tab);
		setBounds(59504, 256, 165, 3, tab);
		setBounds(59505, 256, 180, 4, tab);
		setBounds(59506, 256, 200, 5, tab);
		setBounds(59507, 256 - 50, 220, 6, tab);
		setBounds(59508, 374, 127, 7, tab);
		setBounds(59509, 374, 127, 8, tab);
	}

	public static void bank(TextDrawingArea[] tda) {
		RSInterface rs = addInterface(5292);
		rs.message = "";
		setChildren(29, rs);
		addSprite(58001, 0, "BankTab/07/BANK");
		addHoverButton(5384, "BankTab/BANK", 1, 24, 24, "Close Window", 250, 5380, 3);
		addHoveredButton(5380, "BankTab/BANK", 2, 24, 24, 5379);
		addHoverButton(5294, "BankTab/07/BANK", 7, 37, 29, "Set/Edit Your Bank-Pin", 250, 5295, 4);
		addHoveredButton(5295, "BankTab/BANK", 4, 100, 33, 5296);
		addBankHover(58002, 4, 58003, 10, 11, "BankTab/07/BANK", 37, 29, 304, 1, "Swap Item Movement Mode", 58004, 7, 6,
				"BankTab/BANK", 58005, "Switch to insert items \nmode", "Switch to swap items \nmode.", 12, 20);
		addBankHover(58010, 4, 58011, 8, 9, "BankTab/07/BANK", 37, 29, 116, 1, "Enable/Disable Noted Withdrawal", 58012,
				10, 12, "BankTab/BANK", 58013, "Switch to note withdrawal \nmode", "Switch to item withdrawal \nmode",
				12, 20);
		addClickableSprites(58014, "Enable/Disable Always Placeholders", "BankTab/07/BANK", 5, 6, 5);

		addBankHover1(58018, 5, 58019, 1, "BankTab/07/BANK", 37, 29, "Deposit carried items", 58020, 2,
				"BankTab/07/BANK", 58021, "Empty your backpack into\nyour bank", 0, 20);
		addBankHover1(58026, 5, 58027, 3, "BankTab/07/BANK", 35, 25, "Deposit worn items", 58028, 4, "BankTab/07/BANK",
				58029, "Empty the items your are\nwearing into your bank", 0, 20);

		for (int i = 0; i < 9; i++) {
			addInterface(58050 + i);
			if (i == 0)
				addConfigButton(58031, 5292, 1, 0, "BankTab/TAB", 48, 38, new String[] { "Price Check", "View" }, 1,
						700);
			else
				addConfigButton(58031 + i, 5292, 4, 2, "BankTab/TAB", 48, 38,
						new String[] { "Price Check", "Collapse", "View" }, 1, 700 + i);
			addToItemGroup(58040 + i, 1, 1, 0, 0, false, "", "", "");
		}

		addSprite(58060, 21, "BankTab/BANK");
		addText(58061, "0", tda, 0, 0xE68A00, true, true);
		addText(58062, "350", tda, 0, 0xE68A00, true, true);

		addInputField(58063, 50, 0xE68A00, "Search", 235, 23, false, true);
		addText(58064, "Bank Of " + Configuration.CLIENT_TITLE, tda, 1, 0xE68A00, true, true);
		RSInterface Interface = interfaceCache[5385];
		Interface.height = 202;
		Interface.width = 481;
		Interface = interfaceCache[5382];
		Interface.width = 10;
		Interface.invSpritePadX = 12;
		Interface.height = 35;
		Interface.actions = new String[] { "Withdraw 1", "Withdraw 5", "Withdraw 10", "Withdraw All", "Withdraw X",
				"Withdraw All but one" };
		setBounds(58001, 13, 1, 0, rs);
		setBounds(5384, 475, 10, 1, rs);
		setBounds(5380, 475, 10, 2, rs);
		setBounds(5294, 265, 292, 3, rs); // Bank pin
		setBounds(5295, 295, 297, 4, rs);
		setBounds(58002, 303, 292, 5, rs); // Rearrange mode
		setBounds(58003, 10, 237, 6, rs);
		setBounds(58010, 341, 292, 7, rs); // Noting
		setBounds(58011, 52, 237, 8, rs);

		setBounds(58018, 417, 292, 9, rs); // Items

		setBounds(58019, 94, 237, 10, rs);
		setBounds(58026, 455, 292, 11, rs); // Invo
		setBounds(58014, 379, 292, 28, rs); // Placeholder

		setBounds(58027, 136, 237, 12, rs);
		setBounds(5385, -3, 76, 13, rs);
		RSInterface.interfaceCache[5385].height = 216;
		int x = 68;
		for (int i = 0; i < 9; i++) {
			setBounds(58050 + i, 0, 0, 14 + i, rs);
			RSInterface rsi = interfaceCache[58050 + i];
			setChildren(2, rsi);
			setBounds(58031 + i, x, 36, 0, rsi);
			setBounds(58040 + i, x + 5, 39, 1, rsi);
			x += 41;
		}
		// 0-350
		setBounds(58060, 25, 41, 23, rs);
		setBounds(58061, 45, 44, 24, rs);
		setBounds(58062, 45, 57, 25, rs);

		setBounds(58063, 25, 298, 26, rs); // Search
		setBounds(58064, 250, 11, 27, rs);
	}

	public static void addInputField(int identity, int characterLimit, int color, String text, int width, int height,
			boolean asterisks, boolean updatesEveryInput, String regex) {
		RSInterface field = addFullScreenInterface(identity);
		field.id = identity;
		field.type = 16;
		field.atActionType = 8;
		field.message = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.textColor = color;
		field.displayAsterisks = asterisks;
		field.tooltips = new String[] { "Clear", "Edit" };
		field.defaultInputFieldText = text;
		field.updatesEveryInput = updatesEveryInput;
		field.inputRegex = regex;
	}

	public static void addInputField(int identity, int characterLimit, int color, String text, int width, int height,
			boolean asterisks, boolean updatesEveryInput) {
		RSInterface field = addFullScreenInterface(identity);
		field.id = identity;
		field.type = 16;
		field.atActionType = 8;
		field.message = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.textColor = color;
		field.displayAsterisks = asterisks;
		field.defaultInputFieldText = text;
		field.tooltips = new String[] { "Clear", "Edit" };
		field.updatesEveryInput = updatesEveryInput;
	}

	public static void addInputField(int identity, int characterLimit, int color, String text, int width, int height,
			boolean asterisks) {
		RSInterface field = addFullScreenInterface(identity);
		field.id = identity;
		field.type = 16;
		field.atActionType = 8;
		field.message = text;
		field.width = width;
		field.height = height;
		field.characterLimit = characterLimit;
		field.textColor = color;
		field.displayAsterisks = asterisks;
		field.defaultInputFieldText = text;
		field.tooltips = new String[] { "Clear", "Edit" };
	}

	public static RSInterface addFullScreenInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.width = 765;
		rsi.height = 503;
		return rsi;
	}

	public boolean updatesEveryInput;
	public String defaultInputFieldText = "";
	int[] inputFieldTriggers;
	public boolean displayAsterisks;
	public int characterLimit;
	public static int currentInputFieldId;
	public String inputRegex = "";
	public boolean isInFocus;

	public String[] tooltips;

	public static void addBankHover(int interfaceID, int actionType, int hoverid, int spriteId, int spriteId2,
			String NAME, int Width, int Height, int configFrame, int configId, String Tooltip, int hoverId2,
			int hoverSpriteId, int hoverSpriteId2, String hoverSpriteName, int hoverId3, String hoverDisabledText,
			String hoverEnabledText, int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.opacity = 0;
		hover.hoverType = hoverid;
		hover.sprite1 = imageLoader(spriteId, NAME);
		hover.sprite2 = imageLoader(spriteId2, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover.anIntArray245 = new int[1];
		hover.anIntArray212 = new int[1];
		hover.anIntArray245[0] = 1;
		hover.anIntArray212[0] = configId;
		hover.valueIndexArray = new int[1][3];
		hover.valueIndexArray[0][0] = 5;
		hover.valueIndexArray[0][1] = configFrame;
		hover.valueIndexArray[0][2] = 0;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		hover.hoverType = -1;
		addSprites(hoverId2, hoverSpriteId, hoverSpriteId2, hoverSpriteName, configId, configFrame);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverEnabledText, configId, configFrame);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	public static void addToItemGroup(int id, int w, int h, int x, int y, boolean actions, String action1,
			String action2, String action3, String action4, String action5, String action6) {
		RSInterface rsi = addInterface(id);
		rsi.width = w;
		rsi.height = h;
		rsi.inv = new int[w * h];
		rsi.invStackSizes = new int[w * h];
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.isMouseoverTriggered = false;
		rsi.invSpritePadX = x;
		rsi.invSpritePadY = y;
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		rsi.sprites = new Sprite[20];
		rsi.actions = new String[6];
		if (actions) {
			rsi.actions[0] = action1;
			rsi.actions[1] = action2;
			rsi.actions[2] = action3;
			rsi.actions[3] = action4;
			rsi.actions[4] = action5;
			rsi.actions[5] = action6;
		}
		rsi.type = 2;
	}

	public static void addToItemGroup(int id, int w, int h, int x, int y, boolean actions, String action1,
			String action2, String action3) {
		RSInterface rsi = addInterface(id);
		rsi.width = w;
		rsi.height = h;
		rsi.inv = new int[w * h];
		rsi.invStackSizes = new int[w * h];
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.isMouseoverTriggered = false;
		rsi.invSpritePadX = x;
		rsi.invSpritePadY = y;
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		rsi.sprites = new Sprite[20];
		rsi.actions = new String[5];
		if (actions) {
			rsi.actions[0] = action1;
			rsi.actions[1] = action2;
			rsi.actions[2] = action3;
		}
		rsi.type = 2;
	}

	public static void addSprites(int ID, int i, int i2, String name, int configId, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.id = ID;
		Tab.parentID = ID;
		Tab.type = 5;
		Tab.atActionType = 0;
		Tab.contentType = 0;
		Tab.width = 512;
		Tab.height = 334;
		Tab.opacity = (byte) 0;
		Tab.hoverType = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configId;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(i, name);
		Tab.sprite2 = imageLoader(i2, name);
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, String bName, int width, int height,
			String[] tooltips, int configID, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = 8;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.opacity = 0;
		Tab.hoverType = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(bID, bName);
		Tab.sprite2 = imageLoader(bID2, bName);
		Tab.tooltips = tooltips;
	}

	public static void drawTooltip(int id, String text) {
		RSInterface rsinterface = addTabInterface(id);
		rsinterface.parentID = id;
		rsinterface.type = 0;
		rsinterface.isMouseoverTriggered = true;
		rsinterface.mOverInterToTrigger = -1;
		addTooltipBox(id + 1, text);
		rsinterface.totalChildren(1);
		rsinterface.child(0, id + 1, 0, 0);
	}

	public static void addButton(int i, int j, String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.sprite2 = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void addButton(int interfaceId, int spriteId, String spriteLocation, int width, int height,
			String[] tooltips) {
		RSInterface RSInterface = addInterface(interfaceId);
		RSInterface.id = interfaceId;
		RSInterface.parentID = interfaceId;
		RSInterface.type = 5;
		RSInterface.atActionType = 8;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = imageLoader(spriteId, spriteLocation);
		RSInterface.sprite2 = imageLoader(spriteId, spriteLocation);
		RSInterface.width = width;
		RSInterface.height = height;
		RSInterface.tooltips = tooltips;
	}

	public boolean newScroller;

	// public static void staffTab(TextDrawingArea[] TDA) {
	// RSInterface Interface = addInterface(45000);
	// setChildren(2, Interface);
	// addText(45001, "OSPS", 0xFF981F, false, true, 52, TDA, 2);
	// addButton(45002, 2, "Interfaces/QuestTab/QUEST", "Update");
	// setBounds(45001, 10, 5, 0, Interface);
	// setBounds(45002, 105, 5, 1, Interface);
	//
	// Interface = addInterface(45003);
	// Interface.height = 214;
	// Interface.width = 165;
	// Interface.scrollMax = 220;
	// Interface.newScroller = false;
	//
	// setChildren(103, Interface);
	// int yCoord = 38;
	// int frame = 2;
	// for (int id = 45005; id <= 45105; id++) {
	// setBounds(id, 8, yCoord, frame, Interface);
	// addHoverText(id, "Name", "Visit Player", TDA, 0, 0xff0000, false, true, 150);
	// frame++;
	// yCoord += 15;
	// }
	//
	// }

	public static void teleport(TextDrawingArea[] tda) {
		RSInterface inter = addInterface(51000);
		int extraX = 5;
		addHoverButton(41249, "/Interfaces/Teleportation/SPRITE", -1, 157, 45, "Select", -1, 41250, 1);
		addHoveredButton(41250, "/Interfaces/Teleportation/SPRITE", 0, 157, 40, 41251);
		addHoverButton(41252, "/Interfaces/Teleportation/SPRITE", -1, 157, 45, "Select", -1, 41253, 1);
		addHoveredButton(41253, "/Interfaces/Teleportation/SPRITE", 0, 157, 40, 41254);
		addHoverButton(42255, "/Interfaces/Teleportation/SPRITE", -1, 157, 45, "Select", -1, 42256, 1);
		addHoveredButton(42256, "/Interfaces/Teleportation/SPRITE", 0, 157, 40, 42257);
		addHoverButton(42258, "/Interfaces/Teleportation/SPRITE", -1, 157, 45, "Select", -1, 42259, 1);
		addHoveredButton(42259, "/Interfaces/Teleportation/SPRITE", 0, 157, 40, 42260);
		addHoverButton(42261, "/Interfaces/Teleportation/SPRITE", -1, 157, 45, "Select", -1, 42262, 1);
		addHoveredButton(42262, "/Interfaces/Teleportation/SPRITE", 0, 157, 40, 42263);
		addHoverButton(42264, "/Interfaces/Teleportation/SPRITE", -1, 157, 45, "Select", -1, 42265, 1);
		addHoveredButton(42265, "/Interfaces/Teleportation/SPRITE", 0, 157, 40, 42266);
		addHoverButton(42267, "/Interfaces/Teleportation/SPRITE", -1, 157, 45, "Select", -1, 42268, 1);
		addHoveredButton(42268, "/Interfaces/Teleportation/SPRITE", 0, 157, 40, 42269);
		addText(44305, "Monsters", tda, 2, 0xFF981F, true, true);
		addText(44306, "Minigames", tda, 2, 0xFF981F, true, true);
		addText(44307, "Wilderness", tda, 2, 0xFF981F, true, true);
		addText(44308, "Bosses", tda, 2, 0xFF981F, true, true);
		addText(44309, "Skilling", tda, 2, 0xFF981F, true, true);
		addText(44340, "Cities", tda, 2, 0xFF981F, true, true);
		addText(44341, "Dungeons", tda, 2, 0xFF981F, true, true);

		RSInterface scroller = addTabInterface(28700);
		scroller.height = 222;
		scroller.width = 146;
		scroller.scrollMax = 310;
		scroller.totalChildren(21);
		setBounds(41249, 9 - 4, 50 - 49, 0, scroller);
		setBounds(41250, 9 - 4, 50 - 49, 1, scroller);
		setBounds(41252, 9 - 4, 96 - 50, 2, scroller);
		setBounds(41253, 9 - 4, 96 - 50, 3, scroller);
		setBounds(42255, 9 - 4, 96 + 45 - 50, 4, scroller);
		setBounds(42256, 9 - 4, 96 + 45 - 50, 5, scroller);
		setBounds(42258, 9 - 4, 96 + 90 - 50, 6, scroller);
		setBounds(42259, 9 - 4, 96 + 90 - 50, 7, scroller);
		setBounds(42261, 9 - 4, 96 + 90 + 45 - 50, 8, scroller);
		setBounds(42262, 9 - 4, 96 + 90 + 45 - 50, 9, scroller);
		setBounds(42264, 9 - 4, 96 + 90 + 90 - 50, 10, scroller);
		setBounds(42265, 9 - 4, 96 + 90 + 90 - 50, 11, scroller);
		setBounds(42267, 9 - 4, 96 + 90 + 90 + 45 - 50, 12, scroller);
		setBounds(42268, 9 - 4, 96 + 90 + 90 + 45 - 50, 13, scroller);
		setBounds(44305, 81 - 0, 62 - 50, 14, scroller);
		setBounds(44306, 81 - 0, 62 + 45 - 50, 15, scroller);
		setBounds(44307, 81 - 0, 62 + 45 + 45 - 50, 16, scroller);
		setBounds(44308, 81 - 0, 62 + 45 + 90 - 50, 17, scroller);
		setBounds(44309, 81 - 0, 62 + 90 + 90 - 50, 18, scroller);
		setBounds(44340, 81 - 0, 62 + 90 + 90 + 45 - 50, 19, scroller);
		setBounds(44341, 81 - 0, 62 + 90 + 90 + 90 - 50, 20, scroller);

		inter.totalChildren(8);
		addSprite(44001, 2, "/Interfaces/Teleportation/SPRITE");
		addHoverButton(44003, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 44004, 3);
		addHoveredButton(44004, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 44005);
		addText(44505, "Where would you like to be teleported?", tda, 2, 0xFF981F, true, true);
		addText(44310, "Previous Teleport", tda, 1, 0xFF981F, true, true);
		addHoverButton(40001, "/Interfaces/Teleportation/SPRITE", 1, 155, 40, "Teleport Previous", -1, 40002, 1);
		addHoveredButton(40002, "/Interfaces/Teleportation/SPRITE", 3, 155, 40, 40003);
		setBounds(44001, 4 + extraX, 16, 0, inter);
		setBounds(44003, 481 + extraX, 25, 1, inter);

		setBounds(44505, 254 + extraX, 25, 2, inter);
		setBounds(28700, 4 + extraX, 50, 3, inter);
		setBounds(40001, 10 + extraX, 273, 4, inter);
		setBounds(40002, 10 + extraX, 273, 5, inter);

		setBounds(44310, 84 + extraX, 58 + 90 + 90 + 48, 6, inter);

		RSInterface scroller2 = addTabInterface(29700);
		scroller2.height = 258;
		scroller2.width = 306;
		scroller2.scrollMax = 770;
		scroller2.totalChildren(51);

		addHoverButton(46261, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46262, 1);
		addHoveredButton(46262, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46263);
		addHoverButton(46264, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46265, 1);
		addHoveredButton(46265, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46266);
		addHoverButton(46268, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46269, 1);
		addHoveredButton(46269, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46270);
		addHoverButton(46271, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46272, 1);
		addHoveredButton(46272, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46273);
		addHoverButton(46274, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46275, 1);
		addHoveredButton(46275, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46276);

		addHoverButton(46277, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46278, 1);
		addHoveredButton(46278, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46279);

		addHoverButton(46280, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46281, 1);
		addHoveredButton(46281, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46282);

		addHoverButton(46283, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46284, 1);
		addHoveredButton(46284, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46285);

		addHoverButton(46286, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46287, 1);
		addHoveredButton(46287, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46288);

		addHoverButton(46289, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46290, 1);
		addHoveredButton(46290, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46291);

		addHoverButton(46292, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46293, 1);
		addHoveredButton(46293, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46294);

		addHoverButton(46295, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46296, 1);
		addHoveredButton(46296, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46297);

		addHoverButton(46298, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46299, 1);
		addHoveredButton(46299, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46300);

		addHoverButton(46301, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46302, 1);
		addHoveredButton(46302, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46303);

		addHoverButton(46304, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46305, 1);
		addHoveredButton(46305, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46306);

		addHoverButton(46307, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46308, 1);
		addHoveredButton(46308, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46309);

		addHoverButton(46310, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46311, 1);
		addHoveredButton(46311, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46312);

		addHoverButton(46313, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46314, 1);
		addHoveredButton(46314, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46315);

		addHoverButton(46316, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46317, 1);
		addHoveredButton(46317, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46318);

		addHoverButton(46319, "/Interfaces/Teleportation/SPRITE", 1, 357, 45, "Teleport", -1, 46320, 1);
		addHoveredButton(46320, "/Interfaces/Teleportation/SPRITE", 0, 357, 40, 46321);

		setBounds(46261, -4 + extraX, 0, 0, scroller2);
		setBounds(46262, -4 + extraX, 0, 1, scroller2);
		setBounds(46264, -4 + extraX, 0 + 45, 2, scroller2);
		setBounds(46265, -4 + extraX, 0 + 45, 3, scroller2);
		setBounds(46268, -4 + extraX, 0 + 90, 4, scroller2);
		setBounds(46269, -4 + extraX, 0 + 90, 5, scroller2);
		setBounds(46271, -4 + extraX, 0 + 90 + 45, 6, scroller2);
		setBounds(46272, -4 + extraX, 0 + 90 + 45, 7, scroller2);
		setBounds(46274, -4 + extraX, 0 + 90 + 90, 8, scroller2);
		setBounds(46275, -4 + extraX, 0 + 90 + 90, 9, scroller2);

		setBounds(46277, -4 + extraX, 0 + 90 + 90 + 45, 10, scroller2);
		setBounds(46278, -4 + extraX, 0 + 90 + 90 + 45, 11, scroller2);
		setBounds(46280, -4 + extraX, 0 + 90 + 90 + 90, 12, scroller2);
		setBounds(46281, -4 + extraX, 0 + 90 + 90 + 90, 13, scroller2);

		setBounds(46283, -4 + extraX, 0 + 90 + 90 + 90 + 45, 14, scroller2);
		setBounds(46284, -4 + extraX, 0 + 90 + 90 + 90 + 45, 15, scroller2);

		setBounds(46286, -4 + extraX, 0 + 90 + 90 + 90 + 90, 16, scroller2);
		setBounds(46287, -4 + extraX, 0 + 90 + 90 + 90 + 90, 17, scroller2);

		setBounds(46289, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 45, 18, scroller2);
		setBounds(46290, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 45, 19, scroller2);

		setBounds(46292, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90, 20, scroller2);
		setBounds(46293, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90, 21, scroller2);

		setBounds(46295, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 45, 22, scroller2);
		setBounds(46296, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 45, 23, scroller2);

		setBounds(46298, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90, 24, scroller2);
		setBounds(46299, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90, 25, scroller2);

		setBounds(46301, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 45, 26, scroller2);
		setBounds(46302, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 45, 27, scroller2);

		setBounds(46304, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 28, scroller2);
		setBounds(46305, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 29, scroller2);

		setBounds(46307, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 45, 30, scroller2);
		setBounds(46308, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 45, 31, scroller2);

		setBounds(46310, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 32, scroller2);
		setBounds(46311, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 33, scroller2);

		setBounds(46313, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 32, scroller2);
		setBounds(46314, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 33, scroller2);

		setBounds(46316, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 32, scroller2);
		setBounds(46317, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 33, scroller2);

		setBounds(46319, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 32, scroller2);
		setBounds(46320, -4 + extraX, 0 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90, 33, scroller2);

		setBounds(40505, 10 + extraX, 10 + 2, 34, scroller2);
		setBounds(40506, 10 + extraX, 10 + 45 + 2, 35, scroller2);
		setBounds(40507, 10 + extraX, 10 + 90 + 2, 36, scroller2);
		setBounds(40508, 10 + extraX, 10 + 45 + 90 + 2, 37, scroller2);
		setBounds(40509, 10 + extraX, 10 + 90 + 90 + 2, 38, scroller2);
		setBounds(40510, 10 + extraX, 10 + 90 + 90 + 45 + 2, 39, scroller2);
		setBounds(40511, 10 + extraX, 10 + 90 + 90 + 90 + 2, 40, scroller2);
		setBounds(40512, 10 + extraX, 10 + 90 + 90 + 90 + 45 + 2, 41, scroller2);
		setBounds(40513, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 2, 42, scroller2);
		setBounds(40514, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 45 + 2, 43, scroller2);
		setBounds(40515, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 90 + 2, 44, scroller2);
		setBounds(40516, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 90 + 45 + 2, 45, scroller2);
		setBounds(40517, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 90 + 90 + 2, 46, scroller2);
		setBounds(40518, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 90 + 90 + 45 + 2, 47, scroller2);
		setBounds(40519, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 2, 48, scroller2);
		setBounds(40520, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 45 + 2, 49, scroller2);
		setBounds(40521, 10 + extraX, 10 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 90 + 2, 50, scroller2);

		addText(40505, "TELEPORT #1", tda, 1, 0xFF981F, false, true);
		addText(40506, "TELEPORT #2", tda, 1, 0xFF981F, false, true);
		addText(40507, "TELEPORT #3", tda, 1, 0xFF981F, false, true);
		addText(40508, "TELEPORT #4", tda, 1, 0xFF981F, false, true);
		addText(40509, "TELEPORT #5", tda, 1, 0xFF981F, false, true);
		addText(40510, "TELEPORT #6", tda, 1, 0xFF981F, false, true);
		addText(40511, "TELEPORT #7", tda, 1, 0xFF981F, false, true);
		addText(40512, "TELEPORT #8", tda, 1, 0xFF981F, false, true);
		addText(40513, "TELEPORT #9", tda, 1, 0xFF981F, false, true);
		addText(40514, "TELEPORT #10", tda, 1, 0xFF981F, false, true);
		addText(40515, "TELEPORT #11", tda, 1, 0xFF981F, false, true);
		addText(40516, "TELEPORT #12", tda, 1, 0xFF981F, false, true);
		addText(40517, "TELEPORT #13", tda, 1, 0xFF981F, false, true);
		addText(40518, "TELEPORT #14", tda, 1, 0xFF981F, false, true);
		addText(40519, "TELEPORT #15", tda, 1, 0xFF981F, false, true);
		addText(40520, "TELEPORT #16", tda, 1, 0xFF981F, false, true);
		addText(40521, "TELEPORT #16", tda, 1, 0xFF981F, false, true);
		setBounds(29700, 175 + extraX, 51, 7, inter);
	}

	public static void addBankHover1(int interfaceID, int actionType, int hoverid, int spriteId, String NAME, int Width,
			int Height, String Tooltip, int hoverId2, int hoverSpriteId, String hoverSpriteName, int hoverId3,
			String hoverDisabledText, int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.aByte254 = 0;
		hover.sprite1 = imageLoader(spriteId, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		addSprite(hoverId2, hoverSpriteId, hoverSpriteId, hoverSpriteName, 0, 0);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverDisabledText, 0, 0);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	public static void itemsOnDeath(TextDrawingArea[] wid) {
		RSInterface rsinterface = addInterface(17100);
		addSprite(17101, 2, 2);
		// addHover(17102,"Items Kept On Death/SPRITE", 1, 17, 17, "Close", 0,
		// 10602, 1);
		// addHovered(10602,"Items Kept On Death/SPRITE", 3, 17, 17, 10603);
		addText(17103, "Items kept on death", wid, 2, 0xff981f);
		addText(17104, "Items I will keep...", wid, 1, 0xff981f);
		addText(17105, "Items I will lose...", wid, 1, 0xff981f);
		addText(17106, "Info", wid, 1, 0xff981f);
		addText(17107, "DethWish", wid, 1, 0xffcc33);
		addText(17108, "", wid, 1, 0xffcc33);
		// rsinterface.scrollMax = 50;
		rsinterface.isMouseoverTriggered = false;
		rsinterface.children = new int[12];
		rsinterface.childX = new int[12];
		rsinterface.childY = new int[12];

		rsinterface.children[0] = 17101;
		rsinterface.childX[0] = 7;
		rsinterface.childY[0] = 8;
		rsinterface.children[1] = 15210;
		rsinterface.childX[1] = 478;
		rsinterface.childY[1] = 17;
		rsinterface.children[2] = 17103;
		rsinterface.childX[2] = 185;
		rsinterface.childY[2] = 18;
		rsinterface.children[3] = 17104;
		rsinterface.childX[3] = 22;
		rsinterface.childY[3] = 49;
		rsinterface.children[4] = 17105;
		rsinterface.childX[4] = 22;
		rsinterface.childY[4] = 109;
		rsinterface.children[5] = 17106;
		rsinterface.childX[5] = 347;
		rsinterface.childY[5] = 49;
		rsinterface.children[6] = 17107;
		rsinterface.childX[6] = 348;
		rsinterface.childY[6] = 270;
		rsinterface.children[7] = 17108;
		rsinterface.childX[7] = 401;
		rsinterface.childY[7] = 293;
		rsinterface.children[8] = 17115;
		rsinterface.childX[8] = 348;
		rsinterface.childY[8] = 64;
		rsinterface.children[9] = 10494;
		rsinterface.childX[9] = 26;
		rsinterface.childY[9] = 71;
		rsinterface.children[10] = 10600;
		rsinterface.childX[10] = 26;
		rsinterface.childY[10] = 129;
		rsinterface.children[11] = 15211;
		rsinterface.childX[11] = 478;
		rsinterface.childY[11] = 17;
		rsinterface = interfaceCache[10494];
		rsinterface.invSpritePadX = 6;
		rsinterface.invSpritePadY = 5;
		rsinterface = interfaceCache[10600];
		rsinterface.invSpritePadX = 6;
		rsinterface.invSpritePadY = 5;
	}

	public static void itemsOnDeathDATA(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(17115);
		addText(17109, "", 0xff981f, false, false, 0, tda, 0);
		addText(17110, "The normal amount of", 0xff981f, false, false, 0, tda, 0);
		addText(17111, "items kept is three.", 0xff981f, false, false, 0, tda, 0);
		addText(17112, "", 0xff981f, false, false, 0, tda, 0);
		addText(17113, "If you are skulled,", 0xff981f, false, false, 0, tda, 0);
		addText(17114, "you will lose all your", 0xff981f, false, false, 0, tda, 0);
		addText(17117, "items, unless an item", 0xff981f, false, false, 0, tda, 0);
		addText(17118, "protecting prayer is", 0xff981f, false, false, 0, tda, 0);
		addText(17119, "used.", 0xff981f, false, false, 0, tda, 0);
		addText(17120, "", 0xff981f, false, false, 0, tda, 0);
		addText(17121, "Item protecting prayers", 0xff981f, false, false, 0, tda, 0);
		addText(17122, "will allow you to keep", 0xff981f, false, false, 0, tda, 0);
		addText(17123, "one extra item.", 0xff981f, false, false, 0, tda, 0);
		addText(17124, "", 0xff981f, false, false, 0, tda, 0);
		addText(17125, "The items kept are", 0xff981f, false, false, 0, tda, 0);
		addText(17126, "selected by the server", 0xff981f, false, false, 0, tda, 0);
		addText(17127, "and include the most", 0xff981f, false, false, 0, tda, 0);
		addText(17128, "expensive items you're", 0xff981f, false, false, 0, tda, 0);
		addText(17129, "carrying.", 0xff981f, false, false, 0, tda, 0);
		addText(17130, "", 0xff981f, false, false, 0, tda, 0);
		RSinterface.parentID = 17115;
		RSinterface.id = 17115;
		RSinterface.type = 0;
		RSinterface.atActionType = 0;
		RSinterface.contentType = 0;
		RSinterface.width = 130;
		RSinterface.height = 197;
		RSinterface.opacity = 0;
		RSinterface.hoverType = -1;
		RSinterface.scrollMax = 280;
		RSinterface.children = new int[20];
		RSinterface.childX = new int[20];
		RSinterface.childY = new int[20];
		RSinterface.children[0] = 17109;
		RSinterface.childX[0] = 0;
		RSinterface.childY[0] = 0;
		RSinterface.children[1] = 17110;
		RSinterface.childX[1] = 0;
		RSinterface.childY[1] = 12;
		RSinterface.children[2] = 17111;
		RSinterface.childX[2] = 0;
		RSinterface.childY[2] = 24;
		RSinterface.children[3] = 17112;
		RSinterface.childX[3] = 0;
		RSinterface.childY[3] = 36;
		RSinterface.children[4] = 17113;
		RSinterface.childX[4] = 0;
		RSinterface.childY[4] = 48;
		RSinterface.children[5] = 17114;
		RSinterface.childX[5] = 0;
		RSinterface.childY[5] = 60;
		RSinterface.children[6] = 17117;
		RSinterface.childX[6] = 0;
		RSinterface.childY[6] = 72;
		RSinterface.children[7] = 17118;
		RSinterface.childX[7] = 0;
		RSinterface.childY[7] = 84;
		RSinterface.children[8] = 17119;
		RSinterface.childX[8] = 0;
		RSinterface.childY[8] = 96;
		RSinterface.children[9] = 17120;
		RSinterface.childX[9] = 0;
		RSinterface.childY[9] = 108;
		RSinterface.children[10] = 17121;
		RSinterface.childX[10] = 0;
		RSinterface.childY[10] = 120;
		RSinterface.children[11] = 17122;
		RSinterface.childX[11] = 0;
		RSinterface.childY[11] = 132;
		RSinterface.children[12] = 17123;
		RSinterface.childX[12] = 0;
		RSinterface.childY[12] = 144;
		RSinterface.children[13] = 17124;
		RSinterface.childX[13] = 0;
		RSinterface.childY[13] = 156;
		RSinterface.children[14] = 17125;
		RSinterface.childX[14] = 0;
		RSinterface.childY[14] = 168;
		RSinterface.children[15] = 17126;
		RSinterface.childX[15] = 0;
		RSinterface.childY[15] = 180;
		RSinterface.children[16] = 17127;
		RSinterface.childX[16] = 0;
		RSinterface.childY[16] = 192;
		RSinterface.children[17] = 17128;
		RSinterface.childX[17] = 0;
		RSinterface.childY[17] = 204;
		RSinterface.children[18] = 17129;
		RSinterface.childX[18] = 0;
		RSinterface.childY[18] = 216;
		RSinterface.children[19] = 17130;
		RSinterface.childX[19] = 0;
		RSinterface.childY[19] = 228;
	}

	public static void itemsKeptOnDeath(TextDrawingArea[] tda) {
		RSInterface Interface = addInterface(22030);
		addSprite(22031, 1, "Death/SPRITE");
		addHoverButton(22032, "Death/SPRITE", 2, 17, 17, "Close", 250, 22033, 3);
		addHoveredButton(22033, "Death/SPRITE", 3, 17, 17, 22034);
		addText(22035, "22035", tda, 0, 0xff981f, false, true);
		addText(22036, "22036", tda, 0, 0xff981f, false, true);
		addText(22037, "22037", tda, 0, 0xff981f, false, true);
		addText(22038, "22038", tda, 0, 0xff981f, false, true);
		addText(22039, "22039", tda, 0, 0xff981f, false, true);
		addText(22040, "22040", tda, 1, 0xffcc33, false, true);
		setChildren(9, Interface);
		setBounds(22031, 7, 8, 0, Interface);
		setBounds(22032, 480, 18, 1, Interface);
		setBounds(22033, 480, 18, 2, Interface);
		setBounds(22035, 348, 98, 3, Interface);
		setBounds(22036, 348, 110, 4, Interface);
		setBounds(22037, 348, 122, 5, Interface);
		setBounds(22038, 348, 134, 6, Interface);
		setBounds(22039, 348, 146, 7, Interface);
		setBounds(22040, 398, 297, 8, Interface);
	}

	public static void addTextButton(int i, String s, String tooltip, int k, boolean l, boolean m,
			TextDrawingArea[] TDA, int j, int w) {
		RSInterface rsinterface = addInterface(i);
		rsinterface.parentID = i;
		rsinterface.id = i;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = w;
		rsinterface.height = 16;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = (byte) 0xFF981F;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = l;
		rsinterface.textShadow = m;
		rsinterface.textDrawingAreas = TDA[j];
		rsinterface.message = s;
		rsinterface.aString228 = "";
		rsinterface.secondaryColor = 0xFF981F;
		rsinterface.textColor = 0xFF981F;
		rsinterface.tooltip = tooltip;
	}

	public static void homeTeleport() {
		RSInterface RSInterface = addInterface(30000);
		RSInterface.tooltip = "Cast @gre@Lunar Home Teleport";
		RSInterface.id = 30000;
		RSInterface.parentID = 30000;
		RSInterface.type = 5;
		RSInterface.atActionType = 5;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = 30001;
		RSInterface.sprite1 = imageLoader(1, "Lunar/SPRITE");
		RSInterface.width = 20;
		RSInterface.height = 20;
		RSInterface Int = addInterface(30001);
		Int.isMouseoverTriggered = true;
		Int.mOverInterToTrigger = -1;
		setChildren(1, Int);
		addLunarSprite(30002, 0, "SPRITE");
		setBounds(30002, 0, 0, 0, Int);
	}

	public static void addLunarSprite(int i, int j, String name) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = 5;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = 52;
		RSInterface.sprite1 = LoadLunarSprite(j, name);
		RSInterface.width = 500;
		RSInterface.height = 500;
		RSInterface.tooltip = "";
	}

	public static void drawRune(int i, int id, String runeName) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = 52;
		RSInterface.sprite1 = LoadLunarSprite(id, "RUNE");
		RSInterface.width = 500;
		RSInterface.height = 500;
	}

	public static void drawRune(int i, int id) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = LoadLunarSprite(id, "RUNE");
		RSInterface.width = 500;
		RSInterface.height = 500;
	}

	public static void addButtons(int id, int sid, String spriteName, String tooltip, int mOver, int atAction) {
		RSInterface rsinterface = interfaceCache[id] = new RSInterface();
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 5;
		rsinterface.atActionType = atAction;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = (byte) 0;
		rsinterface.mOverInterToTrigger = mOver;
		rsinterface.sprite1 = imageLoader(sid, spriteName);
		rsinterface.sprite2 = imageLoader(sid, spriteName);
		rsinterface.width = rsinterface.sprite1.myWidth;
		rsinterface.height = rsinterface.sprite2.myHeight;
		rsinterface.tooltip = tooltip;
		rsinterface.inventoryhover = true;
	}

	public int hoverXOffset = 0;
	public int hoverYOffset = 0;
	public int spriteXOffset = 0;
	public int spriteYOffset = 0;
	public boolean regularHoverBox;

	public boolean invisible = false;

	public static void addRuneText(int ID, int runeAmount, int RuneID, TextDrawingArea[] font) {
		RSInterface rsInterface = addTabInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 4;
		rsInterface.atActionType = 0;
		rsInterface.contentType = 0;
		rsInterface.width = 0;
		rsInterface.height = 14;
		rsInterface.aByte254 = 0;
		rsInterface.mOverInterToTrigger = -1;
		rsInterface.anIntArray245 = new int[1];
		rsInterface.anIntArray212 = new int[1];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = runeAmount;
		rsInterface.valueIndexArray = new int[1][4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = RuneID;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.centerText = true;
		rsInterface.textDrawingAreas = font[0];
		rsInterface.textShadow = true;
		rsInterface.message = "%1/" + runeAmount + "";
		rsInterface.aString228 = "";
		rsInterface.textColor = 12582912;
		rsInterface.secondaryColor = 49152;
	}

	public static void addLunar3RunesSmallBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = Client.cacheSprite1[LUNAR_ON_SPRITES_START + sid];
		rsInterface.sprite1 = Client.cacheSprite1[LUNAR_OFF_SPRITES_START + sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 0, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 14, 35, 3, INT);
		setBounds(rune1, 74, 35, 4, INT);
		setBounds(rune2, 130, 35, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 66, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 66, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 66, 8, INT);
	}

	public static void addSpellSmall2(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int rune3, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite1 = new Sprite("magic/spell " + sid);
		rsInterface.sprite2 = new Sprite("magic/spell " + (sid + 1));
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 0, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(rune1, 14, 35, 3, INT);
		setBounds(rune2, 74, 35, 4, INT);
		setBounds(rune3, 130, 35, 5, INT);
		addRuneText(ID + 5, ra1, r1, TDA);
		setBounds(ID + 5, 26, 66, 6, INT);
		addRuneText(ID + 6, ra2, r2, TDA);
		setBounds(ID + 6, 87, 66, 7, INT);
		addRuneText(ID + 7, ra3, r3, TDA);
		setBounds(ID + 7, 142, 66, 8, INT);
	}
	
	public static void addSpellSmall(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int rune3, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite1 = new Sprite("magic/spell " + sid); //Client.cacheSprite2[sid];
		rsInterface.sprite2 = new Sprite("magic/spell " + (sid + 1)); //Client.cacheSprite2[sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 0, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(rune1, 14, 35, 3, INT);
		setBounds(rune2, 74, 35, 4, INT);
		setBounds(rune3, 130, 35, 5, INT);
		addRuneText(ID + 5, ra1, r1, TDA);
		setBounds(ID + 5, 26, 66, 6, INT);
		addRuneText(ID + 6, ra2, r2, TDA);
		setBounds(ID + 6, 87, 66, 7, INT);
		addRuneText(ID + 7, ra3, r3, TDA);
		setBounds(ID + 7, 145, 66, 8, INT);
	}
	
	public static void addSpellSmaller(int ID, int r1, int r2, int ra1, int ra2, int rune1,
			int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[3];
		rsInterface.anIntArray212 = new int[3];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = lvl;
		rsInterface.valueIndexArray = new int[3][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[3];
		rsInterface.valueIndexArray[2][0] = 1;
		rsInterface.valueIndexArray[2][1] = 6;
		rsInterface.valueIndexArray[2][2] = 0;
		rsInterface.sprite1 = new Sprite("magic/spell " + sid); //Client.cacheSprite2[sid];
		rsInterface.sprite2 = new Sprite("magic/spell " + (sid + 1)); //Client.cacheSprite2[sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(7, INT);
		addLunarSprite(ID + 2, 0, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(rune1, 40, 35, 3, INT);
		setBounds(rune2, 110, 35, 4, INT);
		addRuneText(ID + 5, ra1, r1, TDA);
		setBounds(ID + 5, 53, 66, 5, INT);
		addRuneText(ID + 6, ra2, r2, TDA);
		setBounds(ID + 6, 124, 66, 6, INT);
	}
	
	public static void addLunar2RunesSmallBox(int ID, int r1, int r2, int ra1, int ra2, int rune1, int lvl, String name,
			String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast On";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[3];
		rsInterface.anIntArray212 = new int[3];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = lvl;
		rsInterface.valueIndexArray = new int[3][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[3];
		rsInterface.valueIndexArray[2][0] = 1;
		rsInterface.valueIndexArray[2][1] = 6;
		rsInterface.valueIndexArray[2][2] = 0;
		rsInterface.sprite2 = Client.cacheSprite1[LUNAR_ON_SPRITES_START + sid];
		rsInterface.sprite1 = Client.cacheSprite1[LUNAR_OFF_SPRITES_START + sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(7, INT);
		addLunarSprite(ID + 2, 0, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 37, 35, 3, INT);// Rune
		setBounds(rune1, 112, 35, 4, INT);// Rune
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 50, 66, 5, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 123, 66, 6, INT);

	}

	public static void addLunar3RunesBigBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = Client.cacheSprite1[LUNAR_ON_SPRITES_START + sid];
		rsInterface.sprite1 = Client.cacheSprite1[LUNAR_OFF_SPRITES_START + sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 1, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 21, 2, INT);
		setBounds(30016, 14, 48, 3, INT);
		setBounds(rune1, 74, 48, 4, INT);
		setBounds(rune2, 130, 48, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 79, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 79, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 79, 8, INT);
	}

	public static void addSpellBig2(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int rune3, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite1 = Client.cacheSprite2[sid];
		rsInterface.sprite2 = Client.cacheSprite2[sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 1, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 21, 2, INT);
		setBounds(rune1, 14, 48, 3, INT);
		setBounds(rune2, 74, 48, 4, INT);
		setBounds(rune3, 130, 48, 5, INT);
		addRuneText(ID + 5, ra1, r1, TDA);
		setBounds(ID + 5, 26, 79, 6, INT);
		addRuneText(ID + 6, ra2, r2, TDA);
		setBounds(ID + 6, 87, 79, 7, INT);
		addRuneText(ID + 7, ra3, r3, TDA);
		setBounds(ID + 7, 142, 79, 8, INT);
	}

	public static void addLunar3RunesBigBox3(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite1 = Client.cacheSprite2[sid];
		rsInterface.sprite2 = Client.cacheSprite2[sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 1, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 21, 2, INT);
		setBounds(30016, 14, 48, 3, INT);
		setBounds(rune1, 74, 48, 4, INT);
		setBounds(rune2, 130, 48, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 79, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 79, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 79, 8, INT);
	}

	public Sprite enabledAltSprite;
	public Sprite disabledAltSprite;
	public int[] buttonsToDisable;

	public static void addSettingsSprite(int childId, int spriteId) {
		RSInterface rsi = interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentID = childId;
		rsi.type = 5;
		rsi.atActionType = 0;
		rsi.contentType = 0;
		rsi.sprite1 = Client.cacheSprite3[spriteId];
		rsi.sprite2 = Client.cacheSprite3[spriteId];
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite2.myHeight - 2;
	}

	public int spriteOpacity;

	public static void configHoverButton(int id, String tooltip, int sprite2, int sprite1, int enabledAltSprite,
			int disabledAltSprite, boolean active, int... buttonsToDisable) {
		RSInterface tab = addInterface(id);
		/*
		 * Tab.parentID = pID; Tab.id = ID; Tab.type = 5; Tab.atActionType = aT;
		 * Tab.contentType = 0; Tab.width = width; Tab.height = height; Tab.aByte254 =
		 * 0; Tab.mOverInterToTrigger = -1; Tab.anIntArray245 = new int[1];
		 * Tab.anIntArray212 = new int[1]; Tab.anIntArray245[0] = 1;
		 * Tab.anIntArray212[0] = configID; Tab.valueIndexArray = new int[1][3];
		 * Tab.valueIndexArray[0][0] = 5; Tab.valueIndexArray[0][1] = configFrame;
		 * Tab.valueIndexArray[0][2] = 0; Tab.sprite1 = imageLoader(bID, bName);
		 * Tab.sprite2 = imageLoader(bID2, bName); Tab.tooltip = tT;
		 */
		tab.tooltip = tooltip;
		tab.atActionType = OPTION_OK;
		tab.type = TYPE_CONFIG_HOVER;
		tab.sprite2 = Client.cacheSprite3[sprite2];
		tab.sprite1 = Client.cacheSprite3[sprite1];
		tab.width = tab.sprite2.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.enabledAltSprite = Client.cacheSprite3[enabledAltSprite];
		tab.disabledAltSprite = Client.cacheSprite3[disabledAltSprite];
		tab.buttonsToDisable = buttonsToDisable;
		tab.active = active;
		tab.toggled = active;
		tab.spriteOpacity = 255;
	}

	public static final int OPTION_OK = 1;
	public boolean active;

	private static final int LUNAR_RUNE_SPRITES_START = 232;
	private static final int LUNAR_OFF_SPRITES_START = 246;
	private static final int LUNAR_ON_SPRITES_START = 285;
	private static final int LUNAR_HOVER_BOX_SPRITES_START = 324;

	public static void addLunar3RunesLargeBox0(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = Client.cacheSprite1[LUNAR_ON_SPRITES_START + sid];
		rsInterface.sprite1 = Client.cacheSprite1[LUNAR_OFF_SPRITES_START + sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 2, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 34, 2, INT);
		setBounds(30016, 14, 61, 3, INT);
		setBounds(rune1, 74, 61, 4, INT);
		setBounds(rune2, 130, 61, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 92, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 92, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 92, 8, INT);
	}

	public static void addLunar3RunesLargeBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite2 = Client.cacheSprite1[LUNAR_ON_SPRITES_START + sid];
		rsInterface.sprite1 = Client.cacheSprite1[LUNAR_OFF_SPRITES_START + sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 2, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 34, 2, INT);
		setBounds(30016, 14, 61, 3, INT);
		setBounds(rune1, 74, 61, 4, INT);
		setBounds(rune2, 130, 61, 5, INT);
		addRuneText(ID + 5, ra1, r1, TDA);
		setBounds(ID + 5, 26, 92, 6, INT);
		addRuneText(ID + 6, ra2, r2, TDA);
		setBounds(ID + 6, 87, 92, 7, INT);
		addRuneText(ID + 7, ra3, r3, TDA);
		setBounds(ID + 7, 142, 92, 8, INT);
	}
	
	public static void addSpellSmall2_3(int ID, int r1, int r2, int r3, int r4, int ra1, int ra2, int ra3, int ra4, int rune1,
			int rune2, int rune3, int rune4, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[5];
		rsInterface.anIntArray212 = new int[5];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = ra4;
		rsInterface.anIntArray245[4] = 3;
		rsInterface.anIntArray212[4] = lvl;
		rsInterface.valueIndexArray = new int[5][];
		rsInterface.valueIndexArray[0] = new int[5];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[4];
		rsInterface.valueIndexArray[3][0] = 4;
		rsInterface.valueIndexArray[3][1] = 3214;
		rsInterface.valueIndexArray[3][2] = r4;
		rsInterface.valueIndexArray[3][3] = 0;
		rsInterface.valueIndexArray[4] = new int[4];
		rsInterface.valueIndexArray[4][0] = 1;
		rsInterface.valueIndexArray[4][1] = 0;
		rsInterface.valueIndexArray[4][2] = 0;
		rsInterface.valueIndexArray[4][3] = 0;
		rsInterface.sprite1 = new Sprite("magic/spell " + sid);
		rsInterface.sprite2 = new Sprite("magic/spell " + (sid + 1));
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(11, INT);
		addLunarSprite(ID + 2, 0, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(rune1, 15, 35, 3, INT);
		setBounds(rune2, 55, 35, 4, INT);
		setBounds(rune3, 95, 35, 5, INT);
		setBounds(rune4, 135, 35, 6, INT);
		addRuneText(ID + 5, ra1, r1, TDA);
		setBounds(ID + 5, 29, 66, 7, INT);
		addRuneText(ID + 6, ra2, r2, TDA);
		setBounds(ID + 6, 69, 66, 8, INT);
		addRuneText(ID + 7, ra3, r3, TDA);
		setBounds(ID + 7, 109, 66, 9, INT);
		addRuneText(ID + 8, ra4, r4, TDA);
		setBounds(ID + 8, 149, 66, 10, INT);
	}
	
	public static void addSpellLarge2(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int rune3, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = name.contains("Bounty") ? "Cast @gre@Teleport to Bounty Target" : "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 0;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite1 = new Sprite("magic/spell " + sid);
		rsInterface.sprite2 = new Sprite("magic/spell " + (sid + 1));
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 2, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 34, 2, INT);
		setBounds(rune1, 14, 61, 3, INT);
		setBounds(rune2, 74, 61, 4, INT);
		setBounds(rune3, 130, 61, 5, INT);
		addRuneText(ID + 5, ra1, r1, TDA);
		setBounds(ID + 5, 26, 92, 6, INT);
		addRuneText(ID + 6, ra2, r2, TDA);
		setBounds(ID + 6, 87, 92, 7, INT);
		addRuneText(ID + 7, ra3, r3, TDA);
		setBounds(ID + 7, 142, 92, 8, INT);
	}

	public static void addLunar3RunesLargeBox2(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, TextDrawingArea[] TDA, int sid, int suo, int type) {
		RSInterface rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mOverInterToTrigger = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.anIntArray245 = new int[4];
		rsInterface.anIntArray212 = new int[4];
		rsInterface.anIntArray245[0] = 3;
		rsInterface.anIntArray212[0] = ra1;
		rsInterface.anIntArray245[1] = 3;
		rsInterface.anIntArray212[1] = ra2;
		rsInterface.anIntArray245[2] = 3;
		rsInterface.anIntArray212[2] = ra3;
		rsInterface.anIntArray245[3] = 3;
		rsInterface.anIntArray212[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		rsInterface.sprite1 = Client.cacheSprite2[sid];
		rsInterface.sprite2 = Client.cacheSprite2[sid];
		RSInterface INT = addInterface(ID + 1);
		INT.isMouseoverTriggered = true;
		INT.mOverInterToTrigger = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 2, "BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 34, 2, INT);
		setBounds(30016, 14, 61, 3, INT);
		setBounds(rune1, 74, 61, 4, INT);
		setBounds(rune2, 130, 61, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 92, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 92, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 92, 8, INT);
	}

	private static Sprite LoadLunarSprite(int i, String s) {
		Sprite sprite = imageLoader(i, "/Lunar/" + s);
		return sprite;
	}

	public static void Levelup(TextDrawingArea[] tda) {
		// RSInterface text = interfaceCache[7202];
		RSInterface attack = interfaceCache[6247];
		RSInterface defence = interfaceCache[6253];
		RSInterface str = interfaceCache[6206];
		RSInterface hits = interfaceCache[6216];
		RSInterface rng = interfaceCache[4443];
		RSInterface pray = interfaceCache[6242];
		RSInterface mage = interfaceCache[6211];
		RSInterface cook = interfaceCache[6226];
		RSInterface wood = interfaceCache[4272];
		RSInterface flet = interfaceCache[6231];
		RSInterface fish = interfaceCache[6258];
		RSInterface fire = interfaceCache[4282];
		RSInterface craf = interfaceCache[6263];
		RSInterface smit = interfaceCache[6221];
		RSInterface mine = interfaceCache[4416];
		RSInterface herb = interfaceCache[6237];
		RSInterface agil = interfaceCache[4277];
		RSInterface thie = interfaceCache[4261];
		RSInterface slay = interfaceCache[12122];
		RSInterface farm = interfaceCache[5267];
		RSInterface rune = interfaceCache[4267];
		RSInterface cons = interfaceCache[7267];
		RSInterface hunt = interfaceCache[8267];
		RSInterface summ = addInterface(9267);
		RSInterface dung = addInterface(10267);
		addSprite(17878, 0, "Interfaces/skillchat/skill");
		addSprite(17879, 1, "Interfaces/skillchat/skill");
		addSprite(17880, 2, "Interfaces/skillchat/skill");
		addSprite(17881, 3, "Interfaces/skillchat/skill");
		addSprite(17882, 4, "Interfaces/skillchat/skill");
		addSprite(17883, 5, "Interfaces/skillchat/skill");
		addSprite(17884, 6, "Interfaces/skillchat/skill");
		addSprite(17885, 7, "Interfaces/skillchat/skill");
		addSprite(17886, 8, "Interfaces/skillchat/skill");
		addSprite(17887, 9, "Interfaces/skillchat/skill");
		addSprite(17888, 10, "Interfaces/skillchat/skill");
		addSprite(17889, 11, "Interfaces/skillchat/skill");
		addSprite(17890, 12, "Interfaces/skillchat/skill");
		addSprite(17891, 13, "Interfaces/skillchat/skill");
		addSprite(17892, 14, "Interfaces/skillchat/skill");
		addSprite(17893, 15, "Interfaces/skillchat/skill");
		addSprite(17894, 16, "Interfaces/skillchat/skill");
		addSprite(17895, 17, "Interfaces/skillchat/skill");
		addSprite(17896, 18, "Interfaces/skillchat/skill");
		addSprite(11897, 19, "Interfaces/skillchat/skill");
		addSprite(17898, 20, "Interfaces/skillchat/skill");
		addSprite(17899, 21, "Interfaces/skillchat/skill");
		addSprite(17900, 22, "Interfaces/skillchat/skill");
		addSprite(17901, 23, "Interfaces/skillchat/skill");
		addSprite(17902, 24, "Interfaces/skillchat/skill");
		setChildren(4, attack);
		setBounds(17878, 20, 30, 0, attack);
		setBounds(4268, 80, 15, 1, attack);
		setBounds(4269, 80, 45, 2, attack);
		setBounds(358, 95, 75, 3, attack);
		setChildren(4, defence);
		setBounds(17879, 20, 30, 0, defence);
		setBounds(4268, 80, 15, 1, defence);
		setBounds(4269, 80, 45, 2, defence);
		setBounds(358, 95, 75, 3, defence);
		setChildren(4, str);
		setBounds(17880, 20, 30, 0, str);
		setBounds(4268, 80, 15, 1, str);
		setBounds(4269, 80, 45, 2, str);
		setBounds(358, 95, 75, 3, str);
		setChildren(4, hits);
		setBounds(17881, 20, 30, 0, hits);
		setBounds(4268, 80, 15, 1, hits);
		setBounds(4269, 80, 45, 2, hits);
		setBounds(358, 95, 75, 3, hits);
		setChildren(4, rng);
		setBounds(17882, 20, 30, 0, rng);
		setBounds(4268, 80, 15, 1, rng);
		setBounds(4269, 80, 45, 2, rng);
		setBounds(358, 95, 75, 3, rng);
		setChildren(4, pray);
		setBounds(17883, 20, 30, 0, pray);
		setBounds(4268, 80, 15, 1, pray);
		setBounds(4269, 80, 45, 2, pray);
		setBounds(358, 95, 75, 3, pray);
		setChildren(4, mage);
		setBounds(17884, 20, 30, 0, mage);
		setBounds(4268, 80, 15, 1, mage);
		setBounds(4269, 80, 45, 2, mage);
		setBounds(358, 95, 75, 3, mage);
		setChildren(4, cook);
		setBounds(17885, 20, 30, 0, cook);
		setBounds(4268, 80, 15, 1, cook);
		setBounds(4269, 80, 45, 2, cook);
		setBounds(358, 95, 75, 3, cook);
		setChildren(4, wood);
		setBounds(17886, 20, 30, 0, wood);
		setBounds(4268, 80, 15, 1, wood);
		setBounds(4269, 80, 45, 2, wood);
		setBounds(358, 95, 75, 3, wood);
		setChildren(4, flet);
		setBounds(17887, 20, 30, 0, flet);
		setBounds(4268, 80, 15, 1, flet);
		setBounds(4269, 80, 45, 2, flet);
		setBounds(358, 95, 75, 3, flet);
		setChildren(4, fish);
		setBounds(17888, 20, 30, 0, fish);
		setBounds(4268, 80, 15, 1, fish);
		setBounds(4269, 80, 45, 2, fish);
		setBounds(358, 95, 75, 3, fish);
		setChildren(4, fire);
		setBounds(17889, 20, 30, 0, fire);
		setBounds(4268, 80, 15, 1, fire);
		setBounds(4269, 80, 45, 2, fire);
		setBounds(358, 95, 75, 3, fire);
		setChildren(4, craf);
		setBounds(17890, 20, 30, 0, craf);
		setBounds(4268, 80, 15, 1, craf);
		setBounds(4269, 80, 45, 2, craf);
		setBounds(358, 95, 75, 3, craf);
		setChildren(4, smit);
		setBounds(17891, 20, 30, 0, smit);
		setBounds(4268, 80, 15, 1, smit);
		setBounds(4269, 80, 45, 2, smit);
		setBounds(358, 95, 75, 3, smit);
		setChildren(4, mine);
		setBounds(17892, 20, 30, 0, mine);
		setBounds(4268, 80, 15, 1, mine);
		setBounds(4269, 80, 45, 2, mine);
		setBounds(358, 95, 75, 3, mine);
		setChildren(4, herb);
		setBounds(17893, 20, 30, 0, herb);
		setBounds(4268, 80, 15, 1, herb);
		setBounds(4269, 80, 45, 2, herb);
		setBounds(358, 95, 75, 3, herb);
		setChildren(4, agil);
		setBounds(17894, 20, 30, 0, agil);
		setBounds(4268, 80, 15, 1, agil);
		setBounds(4269, 80, 45, 2, agil);
		setBounds(358, 95, 75, 3, agil);
		setChildren(4, thie);
		setBounds(17895, 20, 30, 0, thie);
		setBounds(4268, 80, 15, 1, thie);
		setBounds(4269, 80, 45, 2, thie);
		setBounds(358, 95, 75, 3, thie);
		setChildren(4, slay);
		setBounds(17896, 20, 30, 0, slay);
		setBounds(4268, 80, 15, 1, slay);
		setBounds(4269, 80, 45, 2, slay);
		setBounds(358, 95, 75, 3, slay);
		setChildren(3, farm);
		setBounds(4268, 80, 15, 0, farm);
		setBounds(4269, 80, 45, 1, farm);
		setBounds(358, 95, 75, 2, farm);
		setChildren(4, rune);
		setBounds(17898, 20, 30, 0, rune);
		setBounds(4268, 80, 15, 1, rune);
		setBounds(4269, 80, 45, 2, rune);
		setBounds(358, 95, 75, 3, rune);
		setChildren(3, cons);
		setBounds(4268, 80, 15, 0, cons);
		setBounds(4269, 80, 45, 1, cons);
		setBounds(358, 95, 75, 2, cons);
		setChildren(3, hunt);
		setBounds(4268, 80, 15, 0, hunt);
		setBounds(4269, 80, 45, 1, hunt);
		setBounds(358, 95, 75, 2, hunt);
		setChildren(4, summ);
		setBounds(17901, 20, 30, 0, summ);
		setBounds(4268, 80, 15, 1, summ);
		setBounds(4269, 80, 45, 2, summ);
		setBounds(358, 95, 75, 3, summ);
		setChildren(4, dung);
		setBounds(17902, 20, 30, 0, dung);
		setBounds(4268, 80, 15, 1, dung);
		setBounds(4269, 80, 45, 2, dung);
		setBounds(358, 95, 75, 3, dung);
	}

	public static void addActionButton(int id, int sprite, int sprite2, int width, int height, String s) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		if (sprite2 == sprite)
			rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		else
			rsi.sprite2 = CustomSpriteLoader(sprite2, "");
		rsi.tooltip = s;
		rsi.contentType = 0;
		rsi.atActionType = 1;
		rsi.width = width;
		rsi.mOverInterToTrigger = 52;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
	}

	public static void addButton(int id, int sid, String spriteName, String tooltip, int mOver, int atAction, int width,
			int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = atAction;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = mOver;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		// tab.width = width;
		tab.height = tab.sprite1.myHeight;
		tab.width = tab.sprite1.myWidth;
		tab.tooltip = tooltip;
		tab.inventoryhover = true;
	}

	public static void addText(int id, String text, TextDrawingArea wid[], int idx, int color) {
		RSInterface rsinterface = addTab(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 0;
		rsinterface.width = 174;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = wid[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.secondaryColor = 0;
		rsinterface.anInt216 = 0;
		rsinterface.anInt239 = 0;
	}

	public static void sprite1(int id, int sprite) {
		RSInterface class9 = interfaceCache[id];
		class9.sprite1 = CustomSpriteLoader(sprite, "");
	}

	public static RSInterface addInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.width = 512;
		rsi.height = 334;
		return rsi;
	}

	public static void addHoverText(int id, String text, String tooltip, TextDrawingArea tda[], int idx, int color,
			boolean center, boolean textShadowed, int width) {
		RSInterface rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parentID = id;
		rsinterface.type = 4;
		rsinterface.atActionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = center;
		rsinterface.textShadow = textShadowed;
		rsinterface.textDrawingAreas = tda[idx];
		rsinterface.message = text;
		rsinterface.aString228 = "";
		rsinterface.textColor = color;
		rsinterface.secondaryColor = 0;
		rsinterface.anInt216 = 0xffffff;
		rsinterface.anInt239 = 0;
		rsinterface.tooltip = tooltip;
	}

	public static RSInterface addTab(int i) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 0;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 512;
		rsinterface.height = 334;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = 0;
		return rsinterface;
	}

	public static void addConfigButton2(int ID, int pID, int bID, int bID2, int width, int height, String tT,
			int configID, int aT, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.aByte254 = 0;
		Tab.mOverInterToTrigger = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = Client.cacheSprite1[bID];
		Tab.sprite2 = Client.cacheSprite1[bID2];
		Tab.tooltip = tT;
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, String bName, int width, int height,
			String tT, int configID, int aT, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.aByte254 = 0;
		Tab.mOverInterToTrigger = -1;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(bID, bName);
		Tab.sprite2 = imageLoader(bID2, bName);
		Tab.tooltip = tT;
	}

	public static void drawBlackBox(int xPos, int yPos) {
		// /Light Coloured Borders\\\
		DrawingArea.drawPixels(71, yPos - 1, xPos - 2, 0x726451, 1); // Left
																		// line
		DrawingArea.drawPixels(69, yPos, xPos + 174, 0x726451, 1); // Right line
		DrawingArea.drawPixels(1, yPos - 2, xPos - 2, 0x726451, 178); // Top
																		// Line
		DrawingArea.drawPixels(1, yPos + 68, xPos, 0x726451, 174); // Bottom
																	// Line

		// /Dark Coloured Borders\\\
		DrawingArea.drawPixels(71, yPos - 1, xPos - 1, 0x2E2B23, 1); // Left
																		// line
		DrawingArea.drawPixels(71, yPos - 1, xPos + 175, 0x2E2B23, 1); // Right
																		// line
		DrawingArea.drawPixels(1, yPos - 1, xPos, 0x2E2B23, 175); // Top line
		DrawingArea.drawPixels(1, yPos + 69, xPos, 0x2E2B23, 175); // Top line

		// /Black Box\\\
		DrawingArea.method335(0x000000, yPos, 174, 68, 220, xPos); // Yes
																	// method335
																	// is
																	// galkons
																	// opacity
																	// method
	}

	public Sprite disabledHover;
	public Sprite enabledHover;

	public static void addPrayer(int i, int configId, int configFrame, int requiredValues, int spriteID,
			String prayerName) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = 5608;
		tab.type = 5;
		tab.atActionType = 4;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = -1;
		tab.sprite1 = imageLoader(0, "PRAYERGLOW");
		tab.sprite2 = imageLoader(1, "PRAYERGLOW");
		tab.width = 34;
		tab.height = 34;
		tab.anIntArray245 = new int[1];
		tab.anIntArray212 = new int[1];
		tab.anIntArray245[0] = 1;
		tab.anIntArray212[0] = configId;
		tab.valueIndexArray = new int[1][3];
		tab.valueIndexArray[0][0] = 5;
		tab.valueIndexArray[0][1] = configFrame;
		tab.valueIndexArray[0][2] = 0;
		if (Client.tabInterfaceIDs[Client.tabID] != 17200) {
			tab.tooltip = "Activate@or1@ " + prayerName;
		}
		RSInterface tab2 = addTabInterface(i + 1);
		tab2.id = i + 1;
		tab2.parentID = 5608;
		tab2.type = 5;
		tab2.atActionType = 0;
		tab2.contentType = 0;
		tab2.aByte254 = 0;
		tab2.mOverInterToTrigger = -1;
		tab2.sprite1 = imageLoader(spriteID, "Prayer/PRAYON");
		tab2.sprite2 = imageLoader(spriteID, "Prayer/PRAYOFF");
		tab2.width = 34;
		tab2.height = 34;
		tab2.anIntArray245 = new int[1];
		tab2.anIntArray212 = new int[1];
		tab2.anIntArray245[0] = 2;
		tab2.anIntArray212[0] = requiredValues + 1;
		tab2.valueIndexArray = new int[1][3];
		tab2.valueIndexArray[0][0] = 2;
		tab2.valueIndexArray[0][1] = 5;
		tab2.valueIndexArray[0][2] = 0;
	}

	public static void addToggleButton(int id, int sprite, int setconfig, int width, int height, String s) {
		RSInterface rsi = addInterface(id);
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray212[0] = 1;
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}
	
	public static void addToggleButton2(int id, int sprite, int setconfig, int width, int height, String s, String spriteName) {
		RSInterface rsi = addInterface(id);
		rsi.sprite1 = imageLoader(sprite, spriteName);
		rsi.sprite2 = imageLoader(sprite, spriteName);
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray212[0] = 1;
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}

	public static void removeSomething(int id) {
		@SuppressWarnings("unused")
		RSInterface rsi = interfaceCache[id] = new RSInterface();
	}

	public static void setBounds(int ID, int X, int Y, int frame, RSInterface RSinterface) {
		RSinterface.children[frame] = ID;
		RSinterface.childX[frame] = X;
		RSinterface.childY[frame] = Y;
	}

	public static void textSize(int id, TextDrawingArea tda[], int idx) {
		RSInterface rsi = interfaceCache[id];
		rsi.textDrawingAreas = tda[idx];
	}

	/**
	 * Adds your current character to an interface.
	 **/

	protected static void addOldPrayer(int id, String prayerName) {
		RSInterface rsi = interfaceCache[id];
		if (Client.tabInterfaceIDs[Client.tabID] != 17200) {
			rsi.tooltip = "Activate@or1@ " + prayerName;
		}
	}

	public static void addPrayerHover(int i, int hoverID, int prayerSpriteID, String hoverText) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 5608;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.aByte254 = 0;
		Interface.mOverInterToTrigger = hoverID;
		Interface.sprite2 = imageLoader(0, "tabs/prayer/hover/PRAYERH");
		Interface.sprite1 = imageLoader(0, "tabs/prayer/hover/PRAYERH");
		Interface.width = 34;
		Interface.height = 34;

		Interface = addTabInterface(hoverID);
		Interface.id = hoverID;
		Interface.parentID = 5608;
		Interface.type = 0;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.aByte254 = 0;
		Interface.mOverInterToTrigger = -1;
		Interface.width = 512;
		Interface.height = 334;
		Interface.isMouseoverTriggered = true;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, Interface);
		setBounds(hoverID + 1, 0, 0, 0, Interface);
	}

	public static void addChar(int ID) {
		RSInterface t = interfaceCache[ID] = new RSInterface();
		t.id = ID;
		t.parentID = ID;
		t.type = 6;
		t.atActionType = 0;
		t.contentType = 328;
		t.width = 136;
		t.height = 168;
		t.opacity = 0;
		t.modelZoom = 580;
		t.modelRotation1 = 150;
		t.modelRotation2 = 0;
		t.anInt257 = -1;
		t.anInt258 = -1;
	}

	public static void addCacheSprite(int id, int sprite1, int sprite2, String sprites) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.sprite1 = method207(sprite1, aClass44, sprites);
		rsi.sprite2 = method207(sprite2, aClass44, sprites);
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
	}

	public void specialBar(int id, TextDrawingArea[] tda) // 7599
	{
		addActionButton(id - 12, 7587, -1, 150, 26, "Use @gre@Special Attack");
		for (int i = id - 11; i < id; i++)
			removeSomething(i);

		RSInterface rsi = interfaceCache[id - 12];
		rsi.width = 150;
		rsi.height = 26;
		rsi.mOverInterToTrigger = 40005;

		rsi = interfaceCache[id];
		rsi.width = 150;
		rsi.height = 26;

		rsi.child(0, id - 12, 0, 0);

		rsi.child(12, id + 1, 3, 7);

		rsi.child(23, id + 12, 16, 8);

		for (int i = 13; i < 23; i++) {
			rsi.childY[i] -= 1;
		}

		rsi = interfaceCache[id + 1];
		rsi.type = 5;
		rsi.sprite1 = CustomSpriteLoader(7600, "");

		for (int i = id + 2; i < id + 12; i++) {
			rsi = interfaceCache[i];
			rsi.type = 5;
		}

		sprite1(id + 2, 7601);
		sprite1(id + 3, 7602);
		sprite1(id + 4, 7603);
		sprite1(id + 5, 7604);
		sprite1(id + 6, 7605);
		sprite1(id + 7, 7606);
		sprite1(id + 8, 7607);
		sprite1(id + 9, 7608);
		sprite1(id + 10, 7609);
		sprite1(id + 11, 7610);

		rsi = addInterface(40005);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = 40005;
		rsi.id = 40005;
		addBox(40006, 0, false, 0x000000, "Select to perform a special\nattack.");
		setChildren(1, rsi);
		setBounds(40006, 0, 0, 0, rsi);
	}

	public static void addAttackHover(int id, int hoverID, String hoverText, TextDrawingArea[] TDA) {
		RSInterface rsi = interfaceCache[id];
		rsi.mOverInterToTrigger = hoverID;

		rsi = addInterface(hoverID);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = hoverID;
		rsi.id = hoverID;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, rsi);
		setBounds(hoverID + 1, 0, 0, 0, rsi);
	}

	public static void addAttackText(int id, String text, TextDrawingArea tda[], int idx, int color, boolean centered) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		if (centered)
			rsi.centerText = true;
		rsi.textShadow = true;
		rsi.textDrawingAreas = tda[idx];
		rsi.message = text;
		rsi.textColor = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void addAttackStyleButton2(int id, int sprite, int setconfig, int width, int height, String s,
			int hoverID, int hW, int hH, String hoverText, TextDrawingArea[] TDA) {
		RSInterface rsi = addInterface(id);
		rsi.sprite1 = CustomSpriteLoader(sprite, "");
		rsi.sprite2 = CustomSpriteLoader(sprite, "a");
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray212[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.mOverInterToTrigger = hoverID;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
		rsi = addInterface(hoverID);
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.mOverInterToTrigger = -1;
		rsi.parentID = hoverID;
		rsi.id = hoverID;
		addBox(hoverID + 1, 0, false, 0x000000, hoverText);
		setChildren(1, rsi);
		setBounds(hoverID + 1, 0, 0, 0, rsi);
	}

	public static void addBox(int id, int byte1, boolean filled, int color, String text) {
		RSInterface Interface = addInterface(id);
		Interface.id = id;
		Interface.parentID = id;
		Interface.type = 9;
		Interface.aByte254 = (byte) byte1;
		Interface.aBoolean227 = filled;
		Interface.mOverInterToTrigger = -1;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.textColor = color;
		Interface.message = text;
	}

	public static void setChildren(int total, RSInterface i) {
		i.children = new int[total];
		i.childX = new int[total];
		i.childY = new int[total];
	}

	protected static Sprite CustomSpriteLoader(int id, String s) {
		long l = (TextClass.method585(s) << 8) + id;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null) {
			return sprite;
		}
		try {
			sprite = new Sprite("/Attack/" + id + s);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public static void addTooltipBox(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 9;
		rsi.message = text;
	}

	public static void addTooltip(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.type = 0;
		rsi.isMouseoverTriggered = true;
		rsi.mOverInterToTrigger = -1;
		addTooltipBox(id + 1, text);
		rsi.totalChildren(1);
		rsi.child(0, id + 1, 0, 0);
	}

	public static void addText(int i, String s, int k, boolean l, boolean m, int a, TextDrawingArea[] TDA, int j) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.parentID = i;
		RSInterface.id = i;
		RSInterface.type = 4;
		RSInterface.atActionType = 0;
		RSInterface.width = 0;
		RSInterface.height = 0;
		RSInterface.contentType = 0;
		RSInterface.aByte254 = 0;
		RSInterface.mOverInterToTrigger = a;
		RSInterface.centerText = l;
		RSInterface.textShadow = m;
		RSInterface.textDrawingAreas = TDA[j];
		RSInterface.message = s;
		RSInterface.aString228 = "";
		RSInterface.textColor = k;
	}

	public static void addSprite(int i, int j, int k) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 5;
		rsinterface.atActionType = 1;
		rsinterface.contentType = 0;
		rsinterface.width = 20;
		rsinterface.height = 20;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = 52;
		rsinterface.sprite1 = imageLoader(j, "Equipment/SPRITE");
		rsinterface.sprite2 = imageLoader(k, "Equipment/SPRITE");
	}

	public static void addHover(int i, int aT, int cT, int hoverid, int sId, String NAME, int W, int H, String tip) {
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.id = i;
		rsinterfaceHover.parentID = i;
		rsinterfaceHover.type = 5;
		rsinterfaceHover.atActionType = aT;
		rsinterfaceHover.contentType = cT;
		rsinterfaceHover.mOverInterToTrigger = hoverid;
		rsinterfaceHover.sprite1 = imageLoader(sId, NAME);
		rsinterfaceHover.sprite2 = imageLoader(sId, NAME);
		rsinterfaceHover.width = W;
		rsinterfaceHover.height = H;
		rsinterfaceHover.tooltip = tip;
	}

	public static void addHovered(int i, int j, String imageName, int w, int h, int IMAGEID) {
		RSInterface rsinterfaceHover = addInterface(i);
		rsinterfaceHover.parentID = i;
		rsinterfaceHover.id = i;
		rsinterfaceHover.type = 0;
		rsinterfaceHover.atActionType = 0;
		rsinterfaceHover.width = w;
		rsinterfaceHover.height = h;
		rsinterfaceHover.isMouseoverTriggered = true;
		rsinterfaceHover.mOverInterToTrigger = -1;
		addSprite(IMAGEID, j, imageName);
		setChildren(1, rsinterfaceHover);
		setBounds(IMAGEID, 0, 0, 0, rsinterfaceHover);
	}

	public static void addText(int id, String text, TextDrawingArea tda[], int idx, int color, boolean centered) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		if (centered)
			rsi.centerText = true;
		rsi.textShadow = true;
		rsi.textDrawingAreas = tda[idx];
		rsi.message = text;
		rsi.textColor = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void addText(int id, String text, TextDrawingArea tda[], int idx, int color, boolean center,
			boolean shadow) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 0;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = 0;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = tda[idx];
		tab.message = text;
		tab.aString228 = "";
		tab.textColor = color;
		tab.secondaryColor = 0;
		tab.anInt216 = 0;
		tab.anInt239 = 0;
	}

	public String hoverText;
	public int opacity;
	public int hoverType;
	@SuppressWarnings("unused")
	private boolean inventoryHover;

	public static void addHoverBox(int id, String text) {
		RSInterface rsi = interfaceCache[id];// addTabInterface(id);
		rsi.id = id;
		rsi.parentID = id;
		rsi.isMouseoverTriggered = true;
		rsi.type = 8;
		rsi.hoverText = text;
	}

	public static void addButton(int id, int sid, String spriteName, String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite2.myHeight;
		tab.tooltip = tooltip;
	}

	public static void addButton(int id, int spriteId, String spriteName, String tooltip, int actionType,
			int mouseOverTrigger) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = actionType;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = mouseOverTrigger;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite2.myHeight;
		tab.tooltip = tooltip;
	}

	public static void addHoverBox(int id, int ParentID, String text, String text2, int configId, int configFrame) {
		RSInterface rsi = addTabInterface(id);
		rsi.id = id;
		rsi.parentID = ParentID;
		rsi.type = 8;
		rsi.aString228 = text;
		rsi.message = text2;
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.anIntArray212[0] = configId;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configFrame;
		rsi.valueIndexArray[0][2] = 0;
	}

	public static void addSprite(int ID, int i, int i2, String name, int configId, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.id = ID;
		Tab.parentID = ID;
		Tab.type = 5;
		Tab.atActionType = 0;
		Tab.contentType = 0;
		Tab.width = 512;
		Tab.height = 334;
		Tab.aByte254 = (byte) 0;
		Tab.anIntArray245 = new int[1];
		Tab.anIntArray212 = new int[1];
		Tab.anIntArray245[0] = 1;
		Tab.anIntArray212[0] = configId;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		Tab.sprite1 = imageLoader(i, name);
		Tab.sprite2 = imageLoader(i2, name);
	}

	public static void addSprite(int id, int spriteId, String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
	}

	public static void addHoverButton(int i, String imageName, int j, int width, int height, String text,
			int contentType, int hoverOver, int aT) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = hoverOver;
		tab.sprite1 = imageLoader(j, imageName);
		tab.sprite2 = imageLoader(j, imageName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton(int i, String imageName, int j, int w, int h, int IMAGEID) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = -1;
		tab.scrollMax = 0;
		addHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHoverImage(int i, int j, int k, String name) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.aByte254 = 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(j, name);
		tab.sprite2 = imageLoader(k, name);
	}

	public static void addTransparentSprite(int id, int spriteId, String spriteName) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	public static RSInterface addScreenInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 0;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 0;
		return tab;
	}

	public static RSInterface addTabInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;// 250
		tab.parentID = id;// 236
		tab.type = 0;// 262
		tab.atActionType = 0;// 217
		tab.contentType = 0;
		tab.width = 512;// 220
		tab.height = 700;// 267
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = -1;// Int 230
		return tab;
	}

	protected static Sprite imageLoader(int i, String s) {
		long l = (TextClass.method585(s) << 8) + i;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(s + " " + i);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public void child2(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x + 4;
		childY[id] = y + 2;
	}

	public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}

	public void totalChildren(int t) {
		children = new int[t];
		childX = new int[t];
		childY = new int[t];
	}

	private Model method206(int i, int j) {
		Model model = (Model) aMRUNodes_264.insertFromCache((i << 16) + j);
		if (model != null)
			return model;
		if (i == 1)
			model = Model.method462(j);
		if (i == 2)
			model = NpcDefinition.forID(j).method160();
		if (i == 3)
			model = Client.myPlayer.method453();
		if (i == 4)
			model = ItemDefinition.forID(j).method202(50);
		if (i == 5)
			model = null;
		if (model != null)
			aMRUNodes_264.removeFromCache(model, (i << 16) + j);
		return model;
	}

	private static Sprite method207(int i, StreamLoader streamLoader, String s) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(streamLoader, s, i);
			aMRUNodes_238.removeFromCache(sprite, l);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}

	public static void method208(boolean flag, Model model) {
		int i = 0;// was parameter
		int j = 5;// was parameter
		if (flag)
			return;
		aMRUNodes_264.unlinkAll();
		if (model != null && j != 4)
			aMRUNodes_264.removeFromCache(model, (j << 16) + i);
	}

	public Model method209(int j, int k, boolean flag) {
		Model model;
		if (flag)
			model = method206(anInt255, anInt256);
		else
			model = method206(anInt233, mediaID);
		if (model == null)
			return null;
		if (k == -1 && j == -1 && model.face_color == null)
			return model;
		Model model_1 = new Model(true, Class36.method532(k) & Class36.method532(j), false, model);
		if (k != -1 || j != -1)
			model_1.method469();
		if (k != -1)
			model_1.method470(k);
		if (j != -1)
			model_1.method470(j);
		model_1.method479(64, 768, -50, -10, -50, true);
		return model_1;
	}

	public static void AddInterfaceButton(int id, int sid, String spriteName, String tooltip, int mOver, int atAction,
			int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = atAction;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.hoverType = mOver;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
		tab.inventoryHover = true;
	}

	public RSInterface() {
	}

	public static StreamLoader aClass44;
	public boolean drawsTransparent;
	public Sprite sprite1;
	public int anInt208;
	public Sprite sprites[];
	public static RSInterface interfaceCache[];
	public int anIntArray212[];
	public int contentType;// anInt214
	public int spritesX[];
	public int anInt216;
	public int atActionType;
	public String spellName;
	public int secondaryColor;
	public int width;
	public String tooltip;
	public String selectedActionName;
	public boolean centerText;
	public int scrollPosition;
	public String actions[];
	public int valueCompareType[];
	public int requiredValues[];
	public int valueIndexArray[][];
	public boolean aBoolean227;
	public String aString228;
	public int mOverInterToTrigger;
	public int invSpritePadX;
	public int textColor;
	public int anInt233;
	public int mediaID;
	public boolean aBoolean235;
	public int parentID;
	public int spellUsableOn;
	private static MRUNodes aMRUNodes_238;
	public int anInt239;
	public int children[];
	public int childX[];
	public boolean usableItemInterface;
	public TextDrawingArea textDrawingAreas;
	public int invSpritePadY;
	public int anIntArray245[];
	public int anInt246;
	public int spritesY[];
	public String message;
	public boolean isInventoryInterface;
	public int id;
	public int invStackSizes[];
	public int inv[];
	public byte aByte254;
	private int anInt255;
	private int anInt256;
	public int anInt257;
	public int anInt258;
	public boolean aBoolean259;
	public Sprite sprite2;
	public int scrollMax;
	public int type;
	public int anInt263;
	private static final MRUNodes aMRUNodes_264 = new MRUNodes(30);
	public int transparency = 0;
	public int anInt265;
	public boolean isMouseoverTriggered;
	public int height;
	public boolean textShadow;
	public int modelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int childY[];
	public boolean inventoryhover;
	public boolean isItemSearchComponent;
	public int itemSearchSelectedId, itemSearchSelectedSlot = -1;
	public static int selectedItemInterfaceId = -1;
	public int priority;

	public int grandExchangeSlot;

	public int colorTypes[];
	public byte progressBarState, progressBarPercentage;

	public static final int TYPE_CONTAINER = 0;
	public static final int TYPE_MODEL_LIST = 1;
	public static final int TYPE_INVENTORY = 2;
	public static final int TYPE_RECTANGLE = 3;
	public static final int TYPE_TEXT = 4;
	public static final int TYPE_SPRITE = 5;
	public static final int TYPE_MODEL = 6;
	public static final int TYPE_ITEM_LIST = 7;
	public static final int TYPE_HOVER = 9;
	public static final int TYPE_CONFIG = 10;
	public static final int TYPE_CONFIG_HOVER = 11;
	public static final int TYPE_SLIDER = 12;
	public static final int TYPE_DROPDOWN = 13;
	public static final int TYPE_KEYBINDS_DROPDOWN = 15;
	public static final int TYPE_XP_POSITION_DROPDOWN = 22;
	public static final int TYPE_ADJUSTABLE_CONFIG = 17;
	public static final int TYPE_BOX = 18;
	public static final int TYPE_MAP = 19;
	public static final int OPTION_DROPDOWN = 7;

	public static void addProgressBar(int identity, int width, int height, int[] colorTypes) {
		RSInterface component = addInterface(identity);
		component.id = identity;
		component.type = 23;
		component.width = width;
		component.height = height;
		component.colorTypes = colorTypes;
	}

	public static void AddInterfaceButton(int id, int sid, String spriteName, String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite2.myHeight;
		tab.tooltip = tooltip;
	}

	public static void AddInterfaceButton(int i, int j, String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = 52;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.sprite2 = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void AddInterfaceButton(int id, int sid, String spriteName, String tooltip, int w, int h) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(sid, spriteName);
		tab.sprite2 = imageLoader(sid, spriteName);
		tab.width = w;
		tab.height = h;
		tab.tooltip = tooltip;
	}

	public static void AddInterfaceButton(int i, int j, int hoverId, String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.opacity = 0;
		RSInterface.hoverType = hoverId;
		RSInterface.sprite1 = imageLoader(j, name);
		RSInterface.sprite2 = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void addClickableText(int id, String text, String tooltip, TextDrawingArea tda[], int idx, int color,
			boolean center, boolean shadow, int width) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 1;
		tab.width = width;
		tab.height = 11;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mOverInterToTrigger = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = tda[idx];
		tab.message = text;
		tab.hoverText = text;
		tab.textColor = color;
		tab.secondaryColor = 0;
		tab.hoverTextColor = 0xffffff;
		tab.anInt239 = 0;
		tab.tooltip = tooltip;
	}

	public int hoverTextColor;

	public static void addHDSprite(int id, int spriteId, String spriteName) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 12;
		rsi.atActionType = 0;
		rsi.contentType = 0;
		rsi.opacity = (byte) 0;
		rsi.hoverType = 52;
		rsi.sprite1 = imageLoader(spriteId, spriteName);
		rsi.sprite2 = imageLoader(spriteId, spriteName);
	}

	public static void addTransparentSprite(int id, int spriteId, String spriteName, int opacity) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 10;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) opacity;
		tab.hoverType = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	public static void darken(int identity, int width, int height, int color, byte transparency) {
		RSInterface component = addInterface(identity);
		component.id = identity;
		component.type = 17;
		component.width = width;
		component.height = height;
		component.fillColor = color;
		component.opacity = transparency;
	}

	public static void drawRoundedRectangle(int identity, int width, int height, int color, byte transparency,
			boolean filled, boolean shadowed) {
		RSInterface component = addInterface(identity);
		component.id = identity;
		component.type = 18;
		component.width = width;
		component.height = height;
		component.fillColor = color;
		component.opacity = transparency;
		component.textShadow = shadowed;
		component.filled = filled;
	}

	public static void addSprites(int id, String path, int... spriteIds) {
		if (spriteIds.length < 2) {
			throw new IllegalStateException("Error adding sprites, not enough sprite id's provided.");
		}
		RSInterface component = addInterface(id);
		component.id = id;
		component.type = 19;
		component.backgroundSprites = new Sprite[spriteIds.length];
		for (int i = 0; i < spriteIds.length; i++) {
			component.backgroundSprites[i] = imageLoader(spriteIds[i], path);
			if (component.backgroundSprites[i] == null) {
				throw new IllegalStateException("Error adding sprites, unable to find one of the images.");
			}
		}
		component.sprite1 = component.backgroundSprites[0];
	}

	public static void addClickableSprites(int id, String tooltip, String path, int... spriteIds) {
		addSprites(id, path, spriteIds);
		RSInterface component = interfaceCache[id];
		component.atActionType = 4;
		component.tooltip = tooltip;
		component.width = component.backgroundSprites[0].myWidth;
		component.height = component.backgroundSprites[0].myHeight;
	}

	public static void addDropdown(int id, Dimension menuDimension, Dimension elementDimension,
			RSInterface... children) {
		Objects.requireNonNull(children);
		RSInterface menu = addInterface(id);
		menu.type = 20;
		menu.atActionType = 9;
		menu.width = (int) menuDimension.getWidth();
		menu.height = (int) menuDimension.getHeight();
		setChildren(1, menu);

		RSInterface scrollpane = addInterface(id + 1);
		setChildren(children.length, scrollpane);

		int yPosition = 0;
		for (int index = 0; index < children.length; index++) {
			children[index].width = (int) elementDimension.getWidth();
			children[index].height = (int) elementDimension.getHeight();
			interfaceCache[children[index].id] = children[index];
			setBounds(index, 0, yPosition, index, scrollpane);
			yPosition += (int) elementDimension.getHeight();
		}
	}

	public static void addDropMenu(int id, int width, int height, RSMenuItem... menuItems) {
		RSInterface component = addInterface(id);
		setChildren(1 + menuItems.length, component);
		setBounds(id + 1, 0, 0, 0, component);
		for (int i = 0; i < menuItems.length; i++) {
			setBounds(id + 2 + i, 0, height + (i * height), 1 + i, component);
		}

		RSInterface menu = addInterface(id + 1);
		menu.type = 20;
		menu.opacity = 255;
		menu.atActionType = 9;
		menu.width = width;
		menu.height = height;
		menu.tooltip = "View";
		menu.textDrawingAreas = defaultTextDrawingAreas[1];

		for (int i = 0; i < menuItems.length; i++) {
			RSInterface menuItem = addInterface(id + 2 + i);
			menuItem.type = 21;
			menuItem.width = width;
			menuItem.height = height;
			menuItem.opacity = 255;
			menuItem.atActionType = 10;
			menuItem.mOverInterToTrigger = id + 1;
			Sprite sprite = menuItems[i].getSprite();
			if (sprite != null) {
				if (menuItems[i].getSprite().myHeight > menuItem.height - 2) {
					sprite = menuItems[i].getSprite().scale(height, height);
				}
			}
			menuItem.menuItem = menuItems[i];
			menuItem.textDrawingAreas = defaultTextDrawingAreas[1];
		}
	}

	/**
	 * The menu item for this component
	 */
	private RSMenuItem menuItem;

	/**
	 * Retrieves the {@link RSMenuItem} object that is currently in focus by this
	 * component
	 * 
	 * @return the item in focus
	 */
	public RSMenuItem getMenuItem() {
		return menuItem;
	}

	/**
	 * Modifies the current {@link RSMenuItem} for this component
	 * 
	 * @param menuItem
	 *            the new item that will be replacing the previous item
	 */
	public void setMenuItem(RSMenuItem menuItem) {
		this.menuItem = menuItem;
	}

	/**
	 * The visibility of the menu of items.
	 */
	private boolean menuVisible;

	/**
	 * Determines if the menu of items is visible.
	 * 
	 * @return will return true if the player has triggered the drop down button.
	 */
	public boolean isMenuVisible() {
		return menuVisible;
	}

	/**
	 * Sets the menu to either a visible or invisible state.
	 * 
	 * @param menuVisible
	 *            true if the menu is to be visible, otherwise invisible.
	 */
	public void setMenuVisible(boolean menuVisible) {
		this.menuVisible = menuVisible;
	}

	/**
	 * Determines if a component is filled with pixels, or is empty.
	 */
	public boolean filled;

	/**
	 * The color a component is filled with
	 */
	public int fillColor;

	/**
	 * An array of background sprites
	 */
	public Sprite[] backgroundSprites;
	public boolean interfaceShown;
}
