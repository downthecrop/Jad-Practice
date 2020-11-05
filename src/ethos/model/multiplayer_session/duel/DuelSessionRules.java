package ethos.model.multiplayer_session.duel;

import java.util.ArrayList;
import java.util.Optional;

import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;

public class DuelSessionRules {

	ArrayList<Rule> rules = new ArrayList<>();

	private int totalValue;

	public boolean contains(Rule rule) {
		Optional<Rule> ruleAvailable = rules.stream().filter(r -> r.ordinal() == rule.ordinal()).findFirst();
		return ruleAvailable.isPresent();
	}

	public void add(Rule rule) {
		rules.add(rule);
	}

	public void remove(Rule rule) {
		rules.remove(rule);
	}

	public void reset() {
		rules.clear();
		totalValue = 0;
	}

	public int getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(int totalValue) {
		this.totalValue = totalValue;
	}

	public static enum Rule {
		FORFEIT(1, "Players cannot escape the arena."), 
		NO_MOVEMENT(2, "Players cannot walk or run."), 
		NO_RANGE(16, "Players cannot use ranged combat."), 
		NO_MELEE(32, "Players cannot use hand-to-hand combat."), 
		NO_MAGE(64, "Players cannot cast spells in combat."), 
		NO_DRINKS(128, "Players cannot drink potions."), 
		NO_FOOD(256, "Eating food to heal is not allowed."), 
		NO_PRAYER(512, "Using the Prayer skill is not allowed."), 
		OBSTACLES(1024, "Obstacles will exist in the arena."), 
		WHIP_AND_DDS(4096, "Only dds and whip are permitted.", new RuleRequirement() {
									@Override
									public boolean meets(Player player) {
										ItemAssistant items = player.getItems();
										int weapons[][] = new int[][] { { 4151, 12773, 12774 }, { 1215, 1231, 5680, 5698 } };
										int weaponCount = 0;
										for (int[] weaponGroup : weapons) {
											for (int weapon : weaponGroup) {
												if (items.playerHasItem(weapon) || items.isWearingItem(weapon)) {
													weaponCount++;
													break;
												}
											}
										}
										return weaponCount >= 2;
									}

								}), 
								NO_SPECIAL_ATTACK(8192, "Special attacks are disabled."), 
								NO_HELM(16384, ""), 
								NO_CAPE(32768, ""), 
								NO_AMULET(65536, ""), 
								NO_WEAPON(131072, ""),
								NO_BODY(262144, ""), 
								NO_SHIELD(524288, "Shields and 2-handed weapons are disabled."), 
								NO_LEGS(2097152, ""), 
								NO_GLOVES(8388608, ""), 
								NO_BOOTS(16777216, ""), 
								NO_RINGS(67108864, ""), 
								NO_ARROWS(134217728, "");

		private int value;

		private String details;

		private Optional<RuleRequirement> requirement = Optional.empty();

		Rule(int value, String details) {
			this.value = value;
			this.details = details;
		}

		Rule(int value, String details, RuleRequirement requirement) {
			this.value = value;
			this.details = details;
			this.requirement = Optional.of(requirement);
		}

		public int getValue() {
			return value;
		}

		public String getDetails() {
			return details;
		}

		public Optional<RuleRequirement> getReq() {
			return requirement;
		}

	}

	public interface RuleRequirement {

		boolean meets(Player player);

	}

}
