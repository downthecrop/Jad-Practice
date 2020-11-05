package com.client.graphics.interfaces.impl;

import com.client.Client;
import com.client.Configuration;
import com.client.graphics.interfaces.RSInterface;
import sun.security.krb5.Config;

public enum Dropdown {

	XP_POSITION() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			Configuration.xpPosition = selected;
		}
	},
	
	XP_SIZE() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			Configuration.xpSize = selected;
		}
	},
	
	XP_SPEED() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			Configuration.xpSpeed = selected;
		}
	},
	
	XP_DURATION() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			Configuration.xpDuration = selected;
		}
	},
	
	XP_COLOUR() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			Configuration.xpColour = selected;
		}
	},
	
	XP_GROUP() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			Configuration.xpGroup = selected;
		}
	},

	KEYBIND_SELECTION() {
		@Override
		public void selectOption(int selected, RSInterface dropdown) {
			Keybinding.bind((dropdown.id - Keybinding.MIN_FRAME) / 3, selected);
		}
	},

	PLAYER_ATTACK_OPTION_PRIORITY() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			Configuration.playerAttackOptionPriority = selected;
		}
	},

	OLD_GAMEFRAME() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0:
					Client.oldGameframe = true;
					Client.instance.loadTabArea();
					Client.instance.drawTabArea();
					return;
				case 1:
					Client.oldGameframe = false;
					Client.instance.loadTabArea();
					Client.instance.drawTabArea();
					return;

			}
		}
	},

	GAME_TIMERS() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0:
					Client.gameTimers = true;
					return;
				case 1:
					Client.gameTimers = false;
					return;

			}
		}
	},

	ANTI_ALIASING() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0:
					Configuration.enableAntiAliasing = true;
					return;
				case 1:
					Configuration.enableAntiAliasing = false;
					return;

			}
		}
	},

	GROUND_ITEM_NAMES() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0:
					Client.groundItemsOn = true;
					return;
				case 1:
					Client.groundItemsOn = false;
					return;

			}
		}
	},

	FOG() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0://false
                    Configuration.enableFogRendering = false;
                    Configuration.enableRainbowFog= false;
					return;
				case 1://grey
                    Configuration.fogColor=0xDCDBDF;
                    Configuration.enableFogRendering = true;
                    Configuration.enableRainbowFog= false;
					return;

                case 2: //Sisle
                    Configuration.fogColor=0xC8C0A8;
                    Configuration.enableFogRendering = true;
                    Configuration.enableRainbowFog= false;
                    return;

                case 3: //dark
                    Configuration.fogColor=0x0e0d0b;
                    Configuration.enableFogRendering = true;
                    Configuration.enableRainbowFog= false;

                    return;
                case 4://marroon
                    Configuration.fogColor= 0x800000;
                    Configuration.enableFogRendering = true;
                    Configuration.enableRainbowFog= false;
                    return;
                case 5://rainbow
                    Configuration.enableFogRendering = true;
                    Configuration.enableRainbowFog= true;
                    Client.instance.pushMessage("Please do ::fogdelay to add a timer to the fog!", 0,"");
					Client.instance.pushMessage("@red@ Warning this could give you seizures! Use at an extreme caution! Ascend not responsible! LoL", 0,"");
                return;

			}
		}
	},

	SMOOTH_SHADING() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0:
					Configuration.enableSmoothShading = true;
					return;
				case 1:
					Configuration.enableSmoothShading = true;
					return;

			}
		}
	},

	TILE_BLENDING() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0:
					Configuration.enableTileBlending = true;
					return;
				case 1:
					Configuration.enableTileBlending = false;
					return;

			}
		}
	},

	INVENTORY_CONTEXT_MENU() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0: //off
					Configuration.inventoryContextMenu=false;
					Configuration.statMenuColor=0xFFFFFF;
					return;
				case 1: //magenta
					Configuration.inventoryContextMenu=true;
					Configuration.statMenuColor=0xFF00FF;
					return;
				case 2://lime
					Configuration.inventoryContextMenu=true;
					Configuration.statMenuColor=0x00FF00;
					return;
				case 3://cyan
					Configuration.inventoryContextMenu=true;
					Configuration.statMenuColor=0x00FFFF;
					return;
				case 4://red
					Configuration.inventoryContextMenu=true;
					Configuration.statMenuColor=0xFF0000;
					return;

			}
		}
	},
	CHAT_EFFECT() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0: //default
					Configuration.chatColor=0;
					return;
				case 1: //red:
					Configuration.chatColor=1;
					return;
				case 2: //green:
					Configuration.chatColor=2;
					return;
				case 3: //cyan:
					Configuration.chatColor=3;
					return;
				case 4: //purple:
					Configuration.chatColor=4;
					return;
				case 5: //white:
					Configuration.chatColor=5;
					return;
				case 6: //flash1:
					Configuration.chatColor=6;
					return;
				case 7: //flash2:
					Configuration.chatColor=7;
					return;
				case 8: //flash3:
					Configuration.chatColor=8;
					return;
				case 9: //glow1:
					Configuration.chatColor=9;
					return;
				case 10: //glow2:
					Configuration.chatColor=10;
					return;
				case 11: //glow3:
					Configuration.chatColor=11;
					return;

			}
		}
	},
	BOUNTY_HUNTER() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0:
					Configuration.bountyHunter = true;
					return;
				case 1:
					Configuration.bountyHunter = false;
					return;

			}
		}
	},
	TARGET_INTERFACE() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			switch(selected){
				case 0:
					Client.instance.showEntityTarget = true;
					return;
				case 1:
					Client.instance.showEntityTarget = false;
					return;

			}
		}
	},

	NPC_ATTACK_OPTION_PRIORITY() {
		@Override
		public void selectOption(int selected, RSInterface r) {
			Configuration.npcAttackOptionPriority = selected;
		}
	}
	;

	private Dropdown() { }

	public abstract void selectOption(int selected, RSInterface r);
}

