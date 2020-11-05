package ethos.model.players.combat.magic;

import ethos.Config;
import ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.shops.ShopAssistant;

public class NonCombatSpells extends MagicRequirements {

	public static void attemptDate(Player c, int action) {
		switch (action) {
		case 4135:
			bonesToBase(c, 15, new int[] { 557, 555, 561 }, new int[] { 2, 2, 1 }, new int[] { 526, 1963 });
			break;
		case 62005:
			bonesToBase(c, 60, new int[] { 557, 555, 561 }, new int[] { 4, 4, 2 }, new int[] { !c.getItems().playerHasItem(532) ? 526 : 532, 6883 });
			break;
		case 8014:
			bonesToBase(c, 15, new int[] { 8014, -1, -1 }, new int[] { 1, -1, -1 }, new int[] { !c.getItems().playerHasItem(532) ? 526 : 532, 1963 });
			break;
		case 8015:
			bonesToBase(c, 60, new int[] { 8015, -1, -1 }, new int[] { 1, -1, -1 }, new int[] { !c.getItems().playerHasItem(532) ? 526 : 532, 6883 });
			break;
		}
	}

	/**
	 * @param c player.
	 * @param levelReq requirement for the spell.
	 * @param runes required to cast the spell.
	 * @param amount of @param runes required to cast the spell.
	 * @param item Item required/given upon casted spell.
	 */
	public static void bonesToBase(Player c, int levelReq, int[] runes, int[] amount, int[] item) {
		if (!hasRequiredLevel(c, levelReq)) {
			c.sendMessage("You need to have a magic level of " + levelReq + " to cast this spell.");
			return;
		}
		if (!c.getItems().playerHasItem(runes[0], amount[0]) || !c.getItems().playerHasItem(runes[1], amount[1]) || !c.getItems().playerHasItem(runes[2], amount[2])) {
			c.sendMessage("You do not have the required runes for this spell.");
			return;
		}
		if ((!c.getItems().playerHasItem(item[0], 1))) {
			c.sendMessage("You need some bones to cast this spell!");
			return;
		}
		deleteRunes(c, new int[] { runes[0], runes[1], runes[2] }, new int[] { amount[0], amount[1], amount[2] });
		c.getItems().replaceItem(c, item[0], item[1]);
		c.gfx100(141);
		c.startAnimation(722);
		c.getPA().addSkillXP(c.getMode().isOsrs() ? 35 : 214, 6, true);
		c.sendMessage("You use your magic power to convert bones into " + ItemAssistant.getItemName(item[1]).toLowerCase() + (item[1] != 526 ? ("e") : ("")) + "s!");
		c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.BONES_TO_PEACHES);
		c.getCombat().resetPlayerAttack();
	}

	public static void superHeatItem(Player c, int itemID) {
		if (System.currentTimeMillis() - c.alchDelay > 700) {
			if (c.playerLevel[6] < 43) {
				c.sendMessage("You need to have a magic level of 43 to cast this spell.");
				return;
			}
			if (!c.getCombat().checkMagicReqs(54)) {
				return;
			}

			int[][] data = { { 436, 1, 438, 1, 2349, 6, 1 }, // TIN
					{ 438, 1, 436, 1, 2349, 6, 15 }, // COPPER
					{ 440, 1, -1, -1, 2351, 13, 20 }, // IRON ORE
					{ 442, 1, -1, -1, 2355, 18, 30 }, // SILVER ORE
					{ 444, 1, -1, -1, 2357, 23, 40 }, // GOLD BAR
					{ 447, 1, 453, 1, 2359, 30, 50 }, // MITHRIL ORE
					{ 449, 1, 453, 1, 2361, 38, 70 }, // ADDY ORE
					{ 451, 1, 453, 1, 2363, 50, 85 }, // RUNE ORE
			};
			for (int i = 0; i < data.length; i++) {
				if (itemID == data[i][0]) {
					if (!c.getItems().playerHasItem(data[i][2], data[i][3])) {
						c.sendMessage("You haven't got enough " + ItemAssistant.getItemName(data[i][2]).toLowerCase() + " to cast this spell!");
						return;
					}
					if (c.playerLevel[Player.playerSmithing] < data[i][6]) {
						c.sendMessage("You need a smithing level of " + data[i][6] + " to heat this ore.");
						return;
					}
					c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
					for (int lol = 0; lol < data[i][3]; lol++) {
						c.getItems().deleteItem(data[i][2], c.getItems().getItemSlot(data[i][2]), 1);
					}
					c.getItems().addItem(data[i][4], 1);
					c.alchDelay = System.currentTimeMillis();
					c.getPA().addSkillXP(data[i][5] * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.SMITHING_EXPERIENCE), Player.playerSmithing, true);
					c.getPA().addSkillXP(53 * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.MAGIC_EXP_RATE), 6, true);
					c.startAnimation(MagicData.MAGIC_SPELLS[54][2]);
					c.gfx100(MagicData.MAGIC_SPELLS[54][3]);
					c.getPA().sendFrame106(6);
					return;
				}
			}
			c.sendMessage("You can only cast superheat item on ores!");
			return;
		}
	}

	public static void playerAlching(Player c, int spell, int itemId, int slot) {
		switch (spell) {
		case 1162: // low alch
			if (System.currentTimeMillis() - c.alchDelay > 1000) {
				if (c.getItems().playerHasItem(itemId, slot, 1)) {
					if (!c.getCombat().checkMagicReqs(49)) {
						break;
					}
					if (itemId == 995) {
						c.sendMessage("You can't alch coins.");
						break;
					}
					int reward = ShopAssistant.getItemShopValue(itemId) / 3;
					int playerAmount = c.getItems().getItemAmount(995);
					if (reward + playerAmount > 2147483647) {
						c.sendMessage("You have reached max cash you can't alch anymore.");
						break;
					}
					if (c.getItems().playerHasItem(itemId, slot, 1)) {
						c.getItems().deleteItem(itemId, slot, 1);
						c.getItems().addItem(995, ShopAssistant.getItemShopValue(itemId) / 3);
						c.startAnimation(MagicData.MAGIC_SPELLS[49][2]);
						c.gfx100(MagicData.MAGIC_SPELLS[49][3]);
						c.alchDelay = System.currentTimeMillis();
						c.getPA().sendFrame106(6);
						c.getPA().addSkillXP(MagicData.MAGIC_SPELLS[49][7] * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.MAGIC_EXP_RATE), 6, true);
						c.getPA().refreshSkill(6);
					}
				}
			}
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				if (c.getItems().playerHasItem(itemId, slot, 1)) {
					if (!c.getCombat().checkMagicReqs(50)) {
						break;
					}
					if (itemId == 995) {
						c.sendMessage("You can't alch coins.");
						break;
					}
					int reward = (int) (ShopAssistant.getItemShopValue(itemId) * .75);
					int playerAmount = c.getItems().getItemAmount(995);
					if (reward + playerAmount > 2147483647) {
						c.sendMessage("You can't alch anymore!");
						break;
					}
					if (c.getItems().playerHasItem(itemId, slot, 1)) {
						c.getItems().deleteItem(itemId, slot, 1);
						c.getItems().addItem(995, (int) (ShopAssistant.getItemShopValue(itemId) * .75));
						c.startAnimation(MagicData.MAGIC_SPELLS[50][2]);
						c.gfx100(MagicData.MAGIC_SPELLS[50][3]);
						c.alchDelay = System.currentTimeMillis();
						c.getPA().sendFrame106(6);
						c.getPA().addSkillXP(MagicData.MAGIC_SPELLS[50][7] * (c.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.MAGIC_EXP_RATE), 6, true);
						c.getPA().refreshSkill(6);
					}
				}
			}
			break;
		}
	}
}