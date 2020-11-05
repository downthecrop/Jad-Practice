package com.client.graphics.interfaces.impl;

import com.client.Client;
import com.client.Configuration;
import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;
public class SettingsWidget extends Interfaces {

	public static void widget(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(42500);

		addSettingsSprite(42501, 28); // Frame
		/* Top buttons */
		configHoverButton(42502, "Display", 0, 1, 2, 3, true, new int[] { 42503, 42504, 42505 });
		configHoverButton(42503, "Audio", 4, 5, 6, 7, false, new int[] { 42502, 42504, 42505 });
		configHoverButton(42504, "Chat", 8, 9, 10, 11, false, new int[] { 42502, 42503, 42505 });
		configHoverButton(42505, "Controls", 12, 13, 14, 15, false, new int[] { 42502, 42503, 42504 });
		/* Bottom buttons */
		configButton(ACCEPT_AID, "Toggle Accept Aid", 55,22);
		configButton(RUN, "Toggle Run", 23, 24);
		hoverButton(42508, "View House Options", 49, 49);
		hoverButton(42509, "View Webstore", 25, 25);
		/* Middle */
		RSInterface display = addTabInterface(42520);
		RSInterface audio = addTabInterface(42530);
		RSInterface chat = addTabInterface(42540);
		RSInterface controls = addTabInterface(42550);

		tab.totalChildren(10);
		int childNum = 0;

		setBounds(42501, 3, 42, childNum++, tab);
		int x = 0;
		for (int i=0; i<4; i++, x += 46) {
			setBounds(42502+i, 6 + x, 0, childNum++, tab);
			setBounds(42506+i, 6 + x, 219, childNum++, tab);
		}
		setBounds(42520, 0, 0, childNum++, tab); // Adjustable middle widget

		displaySettings(display, tda);
		audioSettings(audio, tda);
		chatSettings(chat, tda);
		controlsSettings(controls, tda);
		// Run: 19158, 19159, 19177
	}
	public static void displaySettings(RSInterface display, TextDrawingArea[] tda) {
		/* Mouse zoom */
		hoverButton(42521, "Restore Default Zoom", 20, 21);
		configButtonFromData(ZOOMTOGGLE, "Select", 0, 1);
		/* Screen sizes */
		configHoverButton(FIXED_MODE, "Fixed mode",  16, 16, 40, 17,true, 42523);
		configHoverButton(RESIZABLE_MODE, "Resizable mode", 18, 19, 39, 39, false, 42522);
		/* Advanced options */
		hoverButton(42524, "Configure @lre@Advanced options", 26, 26, "Advanced options", RSInterface.newFonts[1], 0xff981f, 0xffffff, true);
		
		/* Sliders */
		slider(ZOOM_SLIDER, 0, 1200, 27, 36, 1);
		slider(BRIGHTNESS_SLIDER, 0.6, 1.0, 37, 36, 2);
		/* Brightness */
		addSprite3(42527, 38);

		display.totalChildren(9);
		int childNum = 0;

		addText(22539, "100%", tda, 1, 0xfe971e, true, true);
		setBounds(22539, 71, 241, childNum++, display); // Adjustable middle widget
		setBounds(42521, 11, 50, childNum++, display);
		setBounds(ZOOMTOGGLE, 11, 50, childNum++, display);
		setBounds(42522, 25, 118, childNum++, display);
		setBounds(42523, 102, 118, childNum++, display);
		setBounds(42524, 25, 176, childNum++, display);
		setBounds(42525, 47, 59, childNum++, display);
		setBounds(42526, 47, 92, childNum++, display);
		setBounds(42527, 11, 83, childNum++, display);

	}

	public static void audioSettings(RSInterface audio, TextDrawingArea[] tda) {
		addSprite3(42531, 56);
		slider(MUSIC_SLIDER, 0, 10, 37, 54, 3);
		addSprite3(42533, 52);
		slider(SOUND_SLIDER, 0, 10, 37, 54, 4);
		addSprite3(42535, 53);
		slider(AREA_SOUND_SLIDER, 0, 10, 37, 54, 4);
		

		audio.totalChildren(7);
		int childNum = 0;

		int yOffset = 10;
		addText(22539, "100%", tda, 1, 0xfe971e, true, true);
		setBounds(22539, 71, 241, childNum++, audio); // Adjustable middle widget
		setBounds(42531, 12, 56 + yOffset, childNum++, audio);
		setBounds(42532, 50, 65 + yOffset, childNum++, audio);
		setBounds(42533, 11, 101 + yOffset, childNum++, audio);
		setBounds(42535, 11, 146 + yOffset, childNum++, audio);
		setBounds(42534, 50, 111 + yOffset, childNum++, audio);
		setBounds(AREA_SOUND_SLIDER, 50, 157 + yOffset, childNum++, audio);


	}

	public static void chatSettings(RSInterface chat, TextDrawingArea[] tda) {
		configButton(CHAT_EFFECTS, "Toggle Chat Effects", 42,41);
		configButton(SPLIT_PRIVATE_CHAT, "Toggle Split Private Chat", 44,43);
		configButton(42543, "Toggle Hide Private Chat", 88, 87);
		
		configButton(41541, "Toggle Profanity Filter", 90,89);
		hoverButton(41542, "Notifications", 45,45);
		configButton(41543, "Toggle Login/Logout notification timeout", 92, 91);

		hoverButton(42544, "Configure @lre@Titles", 26, 26, "View title", tda, 1, 0xff981f, 0xffffff, true);

		chat.totalChildren(8);
		int childNum = 0;

		int[] buttons = new int[] { 42541, 42542, 42543 };
		int x = 19;
		for (int btn : buttons) {
			setBounds(btn, x, 61, childNum++, chat);
			x += 56;
		}
		int[] buttons2 = new int[] { 41541, 41542, 41543 };
		int x2 = 19;
		for (int btn : buttons2) {
			setBounds(btn, x2, 61 + 53, childNum++, chat);
			x2 += 56;
		}
		addText(22539, "100%", tda, 1, 0xfe971e, true, true);
		setBounds(22539, 71, 241, childNum++, chat); // Adjustable middle widget
		setBounds(42544, 25, 168, childNum++, chat);

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
	
	public static void configButtonFromData(int id, String tooltip, int enabledSprite, int disabledSprite) {
		RSInterface tab = addInterface(id);
		tab.tooltip = tooltip;
		tab.atActionType = OPTION_OK;
		tab.type = TYPE_CONFIG;
		tab.sprite2 = Client.cacheSprite4[enabledSprite];
		tab.sprite1 = Client.cacheSprite4[disabledSprite];
		tab.width = tab.sprite2.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.active = false;
	}

	public static void controlsSettings(RSInterface controls, TextDrawingArea[] tda) {
		configButton(MOUSE_BUTTONS, "Toggle number of Mouse Buttons", 47,46);
		hoverButton(42552, "Keybinding", 48,48);
		configButton(SHIFT_CLICK_DROP, "Toggle follower options priority", 96, 95);
		
		configButton(41551, "Toggle Mouse Camera", 94,93);
		configButton(41552, "Toggle Shift Click Drop", 51, 50);


		String[] options = {"Depends on combat levels", "Always right-click", "Left-click where available", "Hidden"};

		dropdownMenu(PLAYER_ATTACK_DROPDOWN, 166,0, options, Dropdown.PLAYER_ATTACK_OPTION_PRIORITY, tda, 1);
		addText(42555, "Player 'Attack' options:", tda, 1, 0xfe971e, false, true);

		dropdownMenu(NPC_ATTACK_DROPDOWN, 166,2, options, Dropdown.NPC_ATTACK_OPTION_PRIORITY, tda, 1);
		addText(42557, "NPC 'Attack' options:", tda, 1, 0xfe971e, false, true);

		controls.totalChildren(10);
		int childNum = 0;

		int[] buttons = new int[] { 42551, 41551, 42553 };
		int x = 25;
		for (int btn : buttons) {
			setBounds(btn, x, 49, childNum++, controls);
			x += 50;
		}
		
		int[] buttons2 = new int[] { 42552, 41552 };
		int x2 = 45;
		for (int btn : buttons2) {
			setBounds(btn, x2, 91, childNum++, controls);
			x2 += 60;
		}
		
		addText(22539, "100%", tda, 1, 0xfe971e, true, true);
		setBounds(22539, 71, 241, childNum++, controls); // Adjustable middle widget
		setBounds(42555, 13, 114 + 20, childNum++, controls);
		setBounds(42556, 13, 181 + 9, childNum++, controls);
		setBounds(42557, 13, 161 + 11, childNum++, controls);
		setBounds(42554, 13, 134 + 17, childNum++, controls);
	}

	public static void settings(int button) {
		switch (button) {
			case 42502:
			case 42503:
			case 42504:
			case 42505:
				switchSettings(button);
				break;
			case FIXED_MODE:
				break;
			case RESIZABLE_MODE:
				break;
			case 42521:
				Client.cameraZoom = 600;
				Slider slider = RSInterface.interfaceCache[ZOOM_SLIDER].slider;
				slider.setValue(600);
				break;
			case 42552:
				Keybinding.updateInterface();
				break;
		}
	}

	public static void switchSettings(int button) {
		int tab = button - 42502;
		int[] tabs = new int[] { 42520, 42530, 42540, 42550 };
		RSInterface.interfaceCache[42500].children[9] = tabs[tab];
	}

	public static void updateSettings() {
		/* Settings */
		RSInterface.interfaceCache[ACCEPT_AID].active = true;
		//RSInterface.interfaceCache[RUN].active = Client.instance.settings[152] == 1;
		RSInterface.interfaceCache[CHAT_EFFECTS].active = true;
		RSInterface.interfaceCache[SPLIT_PRIVATE_CHAT].active = false;
		RSInterface.interfaceCache[MOUSE_BUTTONS].active = true;
		//RSInterface.interfaceCache[SHIFT_CLICK_DROP].active = Configuration.enableShiftClickDrop;

		RSInterface.interfaceCache[PLAYER_ATTACK_DROPDOWN].dropdown.setSelected(RSInterface.interfaceCache[42554].dropdown.getOptions()[Configuration.playerAttackOptionPriority]);
		RSInterface.interfaceCache[NPC_ATTACK_DROPDOWN].dropdown.setSelected(RSInterface.interfaceCache[42556].dropdown.getOptions()[Configuration.npcAttackOptionPriority]);

		RSInterface.interfaceCache[ZOOM_SLIDER].slider.setValue(Client.cameraZoom);
		//RSInterface.interfaceCache[BRIGHTNESS_SLIDER].slider.setValue(Client.brightnessState);
		RSInterface.interfaceCache[MUSIC_SLIDER].slider.setValue(Client.cameraZoom);
		RSInterface.interfaceCache[SOUND_SLIDER].slider.setValue(Client.cameraZoom);
	}

	public static final int PLAYER_ATTACK_DROPDOWN = 42554;
	public static final int NPC_ATTACK_DROPDOWN = 42556;

	public static final int ZOOM_SLIDER = 42525;
	public static final int BRIGHTNESS_SLIDER = 42526;
	public static final int MUSIC_SLIDER = 42532;
	public static final int SOUND_SLIDER = 42534;
	public static final int AREA_SOUND_SLIDER = 42536;

	/* Settings */
	public static final int ZOOMTOGGLE = 44151;
	public static final int FIXED_MODE = 42522;
	public static final int RESIZABLE_MODE = 42523;
	public static final int ACCEPT_AID = 42506;
	public static final int RUN = 42507;
	public static final int CHAT_EFFECTS = 42541;
	public static final int SPLIT_PRIVATE_CHAT = 42542;
	public static final int MOUSE_BUTTONS = 42551;
	public static final int SHIFT_CLICK_DROP = 42553;
	
}

