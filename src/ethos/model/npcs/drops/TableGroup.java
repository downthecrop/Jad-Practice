package ethos.model.npcs.drops;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Range;

import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

@SuppressWarnings("serial")
public class TableGroup extends ArrayList<Table> {

	/**
	 * The non-playable character that has access to this group of tables
	 */
	private final List<Integer> npcIds;

	/**
	 * Creates a new group of tables
	 * 
	 * @param npcId the npc identification value
	 */
	public TableGroup(List<Integer> npcsIds) {
		this.npcIds = npcsIds;
	}

	/**
	 * Accesses each {@link Table} in this {@link TableGroup} with hopes of retrieving a {@link List} of {@link GameItem} objects.
	 * 
	 * @return
	 */
	public List<GameItem> access(Player player, double modifier, int repeats) {
		int rights = player.getRights().getPrimary().getValue() - 1;
		List<GameItem> items = new ArrayList<>();
		for (Table table : this) {
			TablePolicy policy = table.getPolicy();
			if (policy.equals(TablePolicy.CONSTANT)) {
				for (Drop drop : table) {
					int minimumAmount = drop.getMinimumAmount();

					items.add(new GameItem(drop.getItemId(), minimumAmount + Misc.random(drop.getMaximumAmount() - minimumAmount)));
				}
			} else {
				for (int i = 0; i < repeats; i++) {
					double chance = (1.0 / (double) (table.getAccessibility() * modifier)) * 100D;

					double roll = Misc.preciseRandom(Range.between(0.0, 100.0));

					if (chance > 100.0) {
						chance = 100.0;
					}
					if (roll <= chance) {
						Drop drop = table.fetchRandom();
						int minimumAmount = drop.getMinimumAmount();
						GameItem item = new GameItem(drop.getItemId(),
								minimumAmount + Misc.random(drop.getMaximumAmount() - minimumAmount));

						items.add(item);
						if (chance <= 1.5) {
							if (policy.equals(TablePolicy.VERY_RARE) || policy.equals(TablePolicy.RARE)) {
								if (Item.getItemName(item.getId()).toLowerCase().contains("cowhide")
										|| Item.getItemName(item.getId()).toLowerCase().contains("feather")
										|| Item.getItemName(item.getId()).toLowerCase().contains("arrow")
										|| Item.getItemName(item.getId()).toLowerCase().contains("sq shield")
										|| Item.getItemName(item.getId()).toLowerCase().contains("rune warhammer")
										|| Item.getItemName(item.getId()).toLowerCase().contains("rune battleaxe")
										|| Item.getItemName(item.getId()).toLowerCase().contains("casket")
										|| Item.getItemName(item.getId()).toLowerCase().contains("silver ore")
										|| Item.getItemName(item.getId()).toLowerCase().contains("rune spear")
										|| item.getId() >= 554 && item.getId() <= 566) {
									
								} else {
									PlayerHandler.executeGlobalMessage(
											"@red@" + Misc.capitalize(player.playerName) + " received a drop: "
													+ (item.getAmount() > 1 ? (item.getAmount() + "x ") : ItemAssistant.getItemName(item.getId()) + "@bla@."));
								}
							}
						}
					}
				}
			}
		}
		return items;
	}

	/**
	 * The non-playable character identification values that have access to this group of tables.
	 * 
	 * @return the non-playable character id values
	 */
	public List<Integer> getNpcIds() {
		return npcIds;
	}
}
