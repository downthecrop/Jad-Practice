package ethos.model.players.skills;

import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.combat.magic.MagicRequirements;

public class TabletCreation {

	private static final int[][] TABLET_DATA = { 
			{ 15, 8014, 2, 2, 1 }, // Bones to bananas
			{ 60, 8015, 4, 4, 2 }, // Bones to peaches
	};

	private static int level(int i) {
		return TABLET_DATA[i][0];
	}

	private static int outcome(int i) {
		return TABLET_DATA[i][1];
	}

	public static void createTablet(Player player, int i, int amount) {
		long tabDelay = 0;
		if (System.currentTimeMillis() - tabDelay > 5000) {
			if (!MagicRequirements.hasRequiredLevel(player, level(i))) {
				player.sendMessage("You need to have a magic level of " + level(i) + " to create this tab.");
				player.getPA().closeAllWindows();
				return;
			}
			if (!player.getItems().playerHasItem(557, TABLET_DATA[i][2] * amount) || !player.getItems().playerHasItem(555, TABLET_DATA[i][3] * amount)
					|| !player.getItems().playerHasItem(561, TABLET_DATA[i][4] * amount)) {
				player.sendMessage("You do not have the required runes to create this tab.");
				player.sendMessage("You need: " + "x" + TABLET_DATA[i][2] * amount + " " + ItemAssistant.getItemName(557).toLowerCase() + ", " + "" + "x" + TABLET_DATA[i][3]
						* amount + " " + ItemAssistant.getItemName(555).toLowerCase() + ", " + "" + "x" + TABLET_DATA[i][4] * amount + " "
						+ ItemAssistant.getItemName(561).toLowerCase() + " to do this.");
				player.getPA().closeAllWindows();
				return;
			}
			MagicRequirements.deleteRunes(player, new int[] { 557, 555, 561 }, new int[] { TABLET_DATA[i][2] * amount, TABLET_DATA[i][3] * amount, TABLET_DATA[i][4] * amount });
			player.getItems().addItem(outcome(i), amount);
			player.gfx100(264);
			player.startAnimation(721);
			tabDelay = System.currentTimeMillis();
			player.getPA().closeAllWindows();
			player.getPA().addSkillXP((player.getMode().isOsrs() ? 1 : 5) * level(i), player.playerRunecrafting, true);
			player.sendMessage("You use your magic powers to convert runes into tablets!");
		}
	}
}
