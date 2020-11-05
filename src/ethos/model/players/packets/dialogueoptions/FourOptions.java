package ethos.model.players.packets.dialogueoptions;

import org.apache.commons.lang3.RandomUtils;

import ethos.Config;
import ethos.Server;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.players.Player;
import ethos.server.data.SerializablePair;

/*
 * @author Matt
 * Four Option Dialogue actions
 */

public class FourOptions {

	/*
	 * Handles all the first options on 'Four option' dialogues.
	 */
	public static void handleOption1(Player c) {

		switch (c.dialogueAction) {
		
		case 1577:
			c.getPA().createSoloInstance(239);
			break;
			
		case 87:
			c.getDH().sendDialogues(88, c.npcType);
			break;

		}
		if (c.dialogueAction == 136) {
			if (c.getPA().canTeleport("modern")) {
				int offset = RandomUtils.nextInt(0, 3);
				c.getPA().startTeleport(3103 + offset, 3933 + offset, 0, "modern", false);
			}
			return;
		}
		if (c.dialogueAction == 129) {
			if (c.getZulrahLostItems().size() > 0) {
				c.getDH().sendDialogues(642, 2040);
				c.nextChat = -1;
			} else {
				if (c.getZulrahEvent().isActive()) {
					c.getDH().sendStatement("It seems that a zulrah instance for you is already created.", "If you think this is wrong then please re-log.");
					c.nextChat = -1;
					return;
				}
				c.getZulrahEvent().initialize();
			}
			return;
		}
		switch (c.teleAction) {
		case 1:
			c.getPA().spellTeleport(3011, 3632, 0, false);
			break;
		}
		if (c.dialogueAction == 3301) {
			c.getDH().sendDialogues(3302, c.npcType);
		}
		if (c.dialogueAction == 122) {
			c.getDH().sendDialogues(621, 954);
			return;
		}
		if (c.teleAction == 13) {
			c.getPA().spellTeleport(2839, 5296, 2, false);
			// //c.sendMessage("You must know it's not easy, get a team to
			// own that boss!");
		}
		if (c.teleAction == 3) {
			c.getPA().spellTeleport(2273, 4681, 0, false);
		}
		if (c.teleAction == 200) {
			// pest
			c.getPA().spellTeleport(2662, 2652, 0, false);
		}
		
		if (c.usingGlory)  //c.getPA().useCharge();
		{
			/*if (!c.getItems().playerHasItem(1712)) {
				c.getPA().removeAllWindows();
				return;
			}*/
			if (!c.getItems().playerHasItem(1712) && !c.getItems().isWearingItem(1712)) {
				c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "glory", false);
				return;
			} else {
				c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "glory", false);
			}
		}
		if (c.usingSkills)  //c.getPA().useCharge();
		{
			c.getPA().startTeleport(1504, 3419, 0, "glory", false);
			c.getPA().useChargeSkills();
		}
		if (c.dialogueAction == 14400) {
		c.getPA().startTeleport(2474, 3438, 0, "modern", false);
		c.sendMessage("You will gain XP after each lap");
		c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 2) {
			c.getPA().startTeleport(3428, 3538, 0, "modern", false);
		}
		if (c.dialogueAction == 3) {
			c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "modern", false);
		}
		if (c.dialogueAction == 4) {
			c.getPA().startTeleport(3565, 3314, 0, "modern", false);
		}
		if (c.dialogueAction == 20) {
			c.getPA().startTeleport(2897, 3618, 4, "modern", false);
			c.killCount = 0;
		} else if (c.teleAction == 200) {
			// barrows
			c.getPA().spellTeleport(3565, 3314, 0, false);
		}

		if (c.caOption4a) {
			c.getDH().sendDialogues(102, c.npcType);
			c.caOption4a = false;
		}
		if (c.caOption4c) {
			c.getDH().sendDialogues(118, c.npcType);
			c.caOption4c = false;
		}
	}

	/*
	 * Handles all the 2nd options on 'Four option' dialogues.
	 */
	public static void handleOption2(Player c) {
		switch (c.dialogueAction) {
		
		case 87:
			c.getDH().sendDialogues(91, c.npcType);
			break;

		}
		if (c.dialogueAction == 136) {
			c.getShops().openShop(40);
			return;
		}
		if (c.dialogueAction == 129) {
			if (c.getZulrahLostItems().size() == 0) {
				c.getDH().sendDialogues(639, 2040);
			} else {
				c.getDH().sendDialogues(640, 2040);
			}
			return;
		}
		switch (c.teleAction) {
		case 1:
			c.getPA().spellTeleport(3170, 3886, 0, false);
			break;
		}
		if (c.dialogueAction == 122) {
			c.getDH().sendDialogues(623, 954);
			return;
		}
		if (c.dialogueAction == 3301) {
			c.getDH().sendDialogues(3304, c.npcType);
		}
		if (c.teleAction == 13) {
			c.getPA().spellTeleport(2864, 5354, 2, false);
			// c.sendMessage("You must know it's not easy, get a team to own
			// that boss!");
		}
		if (c.teleAction == 200) {
			c.sendMessage("@red@Stake only what you can afford to lose!");

			c.getPA().spellTeleport(3365, 3266, 0, false);

		}
		if (c.teleAction == 3) {
			c.getPA().spellTeleport(1189, 3501, 0, false);
		}
		if (c.dialogueAction == 2299) {
			c.playerXP[0] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
			c.getPA().refreshSkill(0);
			c.playerXP[1] = c.getPA().getXPForLevel(45) + 5;
			c.playerLevel[1] = c.getPA().getLevelForXP(c.playerXP[1]);
			c.getPA().refreshSkill(1);
			c.playerXP[2] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
			c.getPA().refreshSkill(2);
			c.playerXP[3] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
			c.getPA().refreshSkill(3);
			c.playerXP[4] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
			c.getPA().refreshSkill(4);
			c.playerXP[5] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
			c.getPA().refreshSkill(5);
			c.playerXP[6] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
			c.getPA().refreshSkill(6);
			c.getItems().addItem(7461, 1);
			c.getItems().addItem(3751, 1);
			c.getItems().addItem(1712, 1);
			c.getItems().addItem(1201, 1);
			c.getItems().addItem(1127, 1);
			c.getItems().addItem(4151, 1);
			c.getItems().addItem(5698, 1);
			c.getItems().addItem(2414, 1);
			c.getItems().addItem(4131, 1);
			c.getItems().addItem(2550, 1);
			c.getItems().addItem(1079, 1);
			c.sendMessage("<img=10>An appropriate starter package has been given to you.");
			c.getPA().removeAllWindows();
			c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
			c.dialogueAction = 0;
		}
		if (c.dialogueAction == 14400) {
		c.getPA().startTeleport(2551, 3555, 0, "modern", false);
		c.sendMessage("You will gain XP after each lap");
		c.getPA().closeAllWindows();
	}
		if (c.dialogueAction == 1658) {
			if (!c.getItems().playerHasItem(995, 912000)) {
				c.sendMessage("You must have 912,000 coins to buy this package.");
				c.getPA().removeAllWindows();
				c.dialogueAction = 0;
				return;
			}
			c.dialogueAction = 0;
			c.getItems().addItemToBank(560, 2000);
			c.getItems().addItemToBank(9075, 4000);
			c.getItems().addItemToBank(557, 10000);
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 912000);
			c.sendMessage("@red@The runes has been added to your bank.");
			c.getPA().removeAllWindows();
			return;
		}
		if (c.usingGlory) // c.getPA().useCharge();
		{
			c.getPA().startTeleport(Config.KARAMJA_X, Config.KARAMJA_Y, 0, "glory", false);
			c.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.TELEPORT_TO_KARAMJA);
		}
		if (c.usingSkills)  //c.getPA().useCharge();
		{
			c.getPA().startTeleport(1769, 3711, 0, "glory", false);
			c.getPA().useChargeSkills();
		}
		if (c.dialogueAction == 2) {
			c.getPA().startTeleport(2884, 3395, 0, "modern", false);
		}
		if (c.dialogueAction == 3) {
			c.getPA().startTeleport(3243, 3513, 0, "modern", false);
		}
		if (c.dialogueAction == 4) {
			c.getPA().startTeleport(2444, 5170, 0, "modern", false);
		}
		if (c.dialogueAction == 20) {
			c.getPA().startTeleport(2897, 3618, 12, "modern", false);
			c.killCount = 0;
		} else if (c.teleAction == 200) {
			// assault
			c.getPA().spellTeleport(2605, 3153, 0, false);
		}
		if (c.caOption4c) {
			c.getDH().sendDialogues(120, c.npcType);
			c.caOption4c = false;
		}
		if (c.caPlayerTalk1) {
			c.getDH().sendDialogues(125, c.npcType);
			c.caPlayerTalk1 = false;
		}
	}

	/*
	 * Handles all the 3rd options on 'Four option' dialogues.
	 */
	public static void handleOption3(Player c) {
		switch (c.dialogueAction) {
		
		case 87:
			c.getDH().sendDialogues(111, c.npcType);
			break;

		}
		if (c.dialogueAction == 136) {
			c.getDH().sendDialogues(666, 1603);
			return;
		}
		if (c.dialogueAction == 129) {
			SerializablePair<String, Long> pair = Server.getServerData().getZulrahTime();
			if (pair == null || pair.getFirst() == null || pair.getSecond() == null) {
				c.getDH().sendDialogues(643, 2040);
			} else {
				c.getDH().sendDialogues(644, 2040);
			}
			return;
		}
		switch (c.teleAction) {
		case 1:
			c.getPA().spellTeleport(3289, 3639, 0, false);
			break;
		case 2:
			c.getPA().spellTeleport(1262, 3501, 0, false);
			break;
		}
		if (c.dialogueAction == 122) {
			c.getDH().sendDialogues(624, 954);
			return;
		}
		if (c.dialogueAction == 3301) {
			c.getDH().sendDialogues(3310, c.npcType);
		}
		if (c.teleAction == 13) {
			c.getPA().spellTeleport(2920, 5325, 2, false);
		}
		
		if (c.teleAction == 200) {
			c.getPA().spellTeleport(2439, 5169, 0, false);
			c.sendMessage("Use the cave entrance to start.");
		}
		if (c.teleAction == 3) {
			c.getPA().spellTeleport(1440, 3648, 0, false);
		}
		if (c.dialogueAction == 2299) {
			c.playerXP[0] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[0] = c.getPA().getLevelForXP(c.playerXP[0]);
			c.getPA().refreshSkill(0);
			c.playerXP[1] = c.getPA().getXPForLevel(1) + 5;
			c.playerLevel[1] = c.getPA().getLevelForXP(c.playerXP[1]);
			c.getPA().refreshSkill(1);
			c.playerXP[2] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[2] = c.getPA().getLevelForXP(c.playerXP[2]);
			c.getPA().refreshSkill(2);
			c.playerXP[3] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[3] = c.getPA().getLevelForXP(c.playerXP[3]);
			c.getPA().refreshSkill(3);
			c.playerXP[4] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[4] = c.getPA().getLevelForXP(c.playerXP[4]);
			c.getPA().refreshSkill(4);
			c.playerXP[5] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
			c.getPA().refreshSkill(5);
			c.playerXP[6] = c.getPA().getXPForLevel(99) + 5;
			c.playerLevel[6] = c.getPA().getLevelForXP(c.playerXP[6]);
			c.getPA().refreshSkill(6);
			c.getItems().addItem(3105, 1);
			c.getItems().addItem(1712, 1);
			c.getItems().addItem(4151, 1);
			c.getItems().addItem(5698, 1);
			c.getItems().addItem(6107, 1);
			c.getItems().addItem(6108, 1);
			c.getItems().addItem(4502, 1);
			c.getItems().addItem(6568, 1);
			c.getItems().addItem(3842, 1);
			c.getItems().addItem(2497, 1);
			c.getItems().addItem(7458, 1);
			c.getItems().addItem(2550, 1);
			c.sendMessage("<img=10>An appropriate starter package has been given to you.");
			c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 3983);
			c.getPA().removeAllWindows();
			c.dialogueAction = 0;
		}
		if (c.dialogueAction == 14400) {
		c.getPA().startTeleport(3004, 3935, 0, "modern", false);
		c.sendMessage("You will gain XP after each lap");
		c.getPA().closeAllWindows();
		}
		if (c.dialogueAction == 1658) {
			if (!c.getItems().playerHasItem(995, 1788000)) {
				c.sendMessage("You must have 1,788,000 coins to buy this package.");
				c.getPA().removeAllWindows();
				c.dialogueAction = 0;
				return;
			}
			c.dialogueAction = 0;
			c.getItems().addItemToBank(556, 1000);
			c.getItems().addItemToBank(554, 1000);
			c.getItems().addItemToBank(558, 1000);
			c.getItems().addItemToBank(557, 1000);
			c.getItems().addItemToBank(555, 1000);
			c.getItems().addItemToBank(560, 1000);
			c.getItems().addItemToBank(565, 1000);
			c.getItems().addItemToBank(566, 1000);
			c.getItems().addItemToBank(9075, 1000);
			c.getItems().addItemToBank(562, 1000);
			c.getItems().addItemToBank(561, 1000);
			c.getItems().addItemToBank(563, 1000);
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 1788000);
			c.sendMessage("@red@The runes has been added to your bank.");
			c.getPA().removeAllWindows();
		}
		if (c.usingGlory) // c.getPA().useCharge();
		{
			c.getPA().startTeleport(Config.DRAYNOR_X, Config.DRAYNOR_Y, 0, "glory", false);
		}
		if (c.usingSkills)  //c.getPA().useCharge();
		{
			c.getPA().startTeleport(1502, 3832, 0, "glory", false);
			c.getPA().useChargeSkills();
		}
		if (c.dialogueAction == 2) {
			c.getPA().startTeleport(2471, 10137, 0, "modern", false);
		}
		if (c.dialogueAction == 3) {
			c.getPA().startTeleport(3363, 3676, 0, "modern", false);
		}
		if (c.dialogueAction == 4) {
			c.getPA().startTeleport(2659, 2676, 0, "modern", false);
		}
		if (c.dialogueAction == 20) {
			c.getPA().startTeleport(2897, 3618, 8, "modern", false);
			c.killCount = 0;
		} else if (c.teleAction == 200) {
			// duel arena
			c.getPA().spellTeleport(3366, 3266, 0, false);
		}
		if (c.caOption4c) {
			c.getDH().sendDialogues(122, c.npcType);
			c.caOption4c = false;
		}
		if (c.caPlayerTalk1) {
			c.getDH().sendDialogues(127, c.npcType);
			c.caPlayerTalk1 = false;
		}
	}

	/*
	 * Handles all the 4th options on 'Four option' dialogues.
	 */
	public static void handleOption4(Player c) {
		switch (c.dialogueAction) {
		case 1401:
			c.getPA().removeAllWindows();
			break;
			
		case 87:
			c.getPA().removeAllWindows();
			break;
		}
		switch (c.teleAction) {
		case 1:
			c.getPA().spellTeleport(3153, 3923, 0, false);
			break;
		case 2:
			c.getPA().spellTeleport(2630, 5071, 0, false);
			return;
		}
		if (c.dialogueAction == 3301 || c.dialogueAction == 122) {
			c.getPA().closeAllWindows();
		}
		if (c.teleAction == 201 || c.dialogueAction == 129 || c.dialogueAction == 136) {
			// pest
			c.getPA().removeAllWindows();
		}
		if (c.teleAction == 13) {
			c.getPA().spellTeleport(2907, 5265, 0, false);
			// c.sendMessage("You must know it's not easy, get a team to own
			// that boss!");
		}
		if (c.teleAction == 3) {
			c.getPA().spellTeleport(2200, 3055, 0, false);
		}
		if (c.teleAction == 200) {
			c.getDH().sendDialogues(2002, -1);
			return;
		}
		if (c.dialogueAction == 2299) {
			c.sendMessage("<img=10>You can set your stats by clicking them in the stats tab.");
			c.getPA().removeAllWindows();
			c.dialogueAction = 0;
		}
		if (c.dialogueAction == 1658) {
			c.getShops().openShop(5);
			c.dialogueAction = 0;
		}
		if (c.dialogueAction == 14400) {
		c.getDH().sendDialogues(14402, -1);
		}
		if (c.usingGlory) // c.getPA().useCharge();
		{
			c.getPA().startTeleport(Config.AL_KHARID_X, Config.AL_KHARID_Y, 0, "glory", false);
		}
		if (c.usingSkills)  //c.getPA().useCharge();
		{
			c.getPA().startTeleport(1721, 3464, 0, "glory", false);
			c.getPA().useChargeSkills();
		}
		if (c.dialogueAction == 2) {
			c.getPA().startTeleport(2669, 3714, 0, "modern", false);
		}
		if (c.dialogueAction == 3) {
			c.getPA().startTeleport(2540, 4716, 0, "modern", false);
		}
		if (c.dialogueAction == 4) {
			c.getPA().startTeleport(3366, 3266, 0, "modern", false);

		} else if (c.teleAction == 200) {
			// tzhaar
			c.getPA().spellTeleport(2444, 5170, 0, false);
		}
		if (c.dialogueAction == 20) {
			// c.getPA().startTeleport(3366, 3266, 0, "modern");
			// c.killCount = 0;
			c.sendMessage("This will be added shortly");
		}
		if (c.caOption4c) {
			c.getDH().sendDialogues(124, c.npcType);
			c.caOption4c = false;
		}
		if (c.caPlayerTalk1) {
			c.getDH().sendDialogues(130, c.npcType);
			c.caPlayerTalk1 = false;
		}
	}

}
