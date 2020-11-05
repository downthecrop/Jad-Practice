package ethos.model.players.skills.fletching;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import ethos.Config;
import ethos.Server;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Skill;
import ethos.util.Misc;

public class Fletching {

	private static final Set<FletchableGem> GEMS = Collections.unmodifiableSet(EnumSet.allOf(FletchableGem.class));

	private static final Set<FletchableUnfinishedBolt> UNFINISHED_BOLTS = Collections.unmodifiableSet(EnumSet.allOf(FletchableUnfinishedBolt.class));

	private static final Set<FletchableBow> BOWS = Collections.unmodifiableSet(EnumSet.allOf(FletchableBow.class));

	private static final Set<FletchableCrossbow> CROSSBOWS = Collections.unmodifiableSet(EnumSet.allOf(FletchableCrossbow.class));

	private static final Set<FletchableArrow> ARROWS = Collections.unmodifiableSet(EnumSet.allOf(FletchableArrow.class));

	private static final Set<FletchableJavelin> JAVELINS = Collections.unmodifiableSet(EnumSet.allOf(FletchableJavelin.class));

	private static final Set<FletchableDart> DARTS = Collections.unmodifiableSet(EnumSet.allOf(FletchableDart.class));

	private static final Set<FletchableBolt> BOLTS = Collections.unmodifiableSet(EnumSet.allOf(FletchableBolt.class));

	/**
	 * An unmodifiable {@link Set} of {@link FletchableLogGroup} elements
	 */
	private static final Set<FletchableLogGroup> FLETCHABLE_LOG_GROUP = Collections.unmodifiableSet(EnumSet.allOf(FletchableLogGroup.class));
	/**
	 * An array of values that represent the amount of some selection
	 */
	private static final int[] FLETCHABLE_AMOUNTS = { 1, 5, 10, -1 };

	/**
	 * The {@link Player} that will be fletching
	 */
	private final Player player;

	/**
	 * An Optional of type {@link FletchableLogGroup} that will keep track of what group the player has selected for fletching.
	 */
	private Optional<FletchableLogGroup> selectedGroup = Optional.empty();

	/**
	 * The {@link FletchableLog} that the player has selected to fletch
	 */
	private Optional<FletchableLog> selectedFletchable = Optional.empty();

	/**
	 * Creates a new single class to manage fletching operations related to the {@code fletching} skill.
	 * 
	 * @param player the player that will be fletching
	 */
	public Fletching(final Player player) {
		this.player = player;
	}

	public boolean combine(int use, int used) {
		selectedGroup = FLETCHABLE_LOG_GROUP.stream().filter(g -> Arrays.stream(g.getFletchables()).anyMatch(f -> f.getItemId() == use || f.getItemId() == used)).findFirst();
		selectedGroup.ifPresent(group -> {
			FletchableLog[] fletchables = group.getFletchables();
			player.getSkilling().stop();
			player.getSkilling().setSkill(Skill.FLETCHING);
			player.getPA().sendFrame164(8880);
			player.getPA().sendFrame126("What would you like to make?", 8879);
			player.getPA().sendFrame246(8884, 190, fletchables[1].getProduct());
			player.getPA().sendFrame246(8883, 190, fletchables[0].getProduct());
			player.getPA().sendFrame246(8885, 190, fletchables[2].getProduct());
			player.getPA().sendFrame126(ItemAssistant.getItemName(fletchables[0].getProduct()), 8889);
			player.getPA().sendFrame126(ItemAssistant.getItemName(fletchables[1].getProduct()), 8893);
			player.getPA().sendFrame126(ItemAssistant.getItemName(fletchables[2].getProduct()), 8897);
		});
		return selectedGroup.isPresent();
	}

	public void select(int buttonId) {
		selectedGroup.ifPresent(group -> {
			for (FletchableLog fletchable : group.getFletchables()) {
				int index = Misc.linearSearch(fletchable.getButtonIds(), buttonId);
				if (index != -1) {
					int amount = FLETCHABLE_AMOUNTS[index];
					selectedFletchable = Optional.of(fletchable);
					if (amount == -1) {
						player.getOutStream().createFrame(27);
						player.flushOutStream();
						break;
					}
					fletchLog(fletchable, amount);
					break;
				}
			}
		});
	}

	/**
	 * Attempts to cut a log with a knife in hopes to make some secondary item
	 * 
	 * @param logId the log being cut
	 */
	public void fletchLog(FletchableLog fletchable, int amount) {
		selectedGroup = Optional.empty();
		selectedFletchable = Optional.empty();
		if (!player.getItems().playerHasItem(fletchable.getItemId())) {
			player.sendMessage("You do not have the items required for this.");
			player.getPA().removeAllWindows();
			return;
		}
		if (player.playerLevel[Skill.FLETCHING.getId()] < fletchable.getLevel()) {
			player.sendMessage("You need a fletching level of " + fletchable.getLevel() + " to do this.");
			player.getPA().removeAllWindows();
			return;
		}
		player.startAnimation(1248);
		player.getPA().removeAllWindows();
		Server.getEventHandler().stop(player, "skilling");
		Server.getEventHandler().submit(new FletchLogEvent(player, 3, fletchable, amount));
	}

	public void fletchGem(int use, int used) {
		selectedGroup = Optional.empty();
		selectedFletchable = Optional.empty();
		Optional<FletchableGem> gem = GEMS.stream().filter(g -> g.getGem() == use || g.getGem() == used).findFirst();
		gem.ifPresent(g -> {
			if (!player.getItems().playerHasItem(g.getGem())) {
				player.sendMessage("You do not have the items required for this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.playerLevel[Skill.FLETCHING.getId()] < g.getLevel()) {
				player.sendMessage("You need a fletching level of " + g.getLevel() + " to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getItems().deleteItem2(g.getGem(), 1);
			player.getItems().addItem(g.getTips(), g.getAmount());
			player.getPA().addSkillXP(g.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId(), true);
		});
	}

	public void fletchUnfinishedBolt(int boltId) {
		Optional<FletchableUnfinishedBolt> bolt = UNFINISHED_BOLTS.stream().filter(b -> b.getUnfinished() == boltId).findFirst();
		bolt.ifPresent(b -> {
			if (player.getItems().freeSlots() < 1) {
				player.sendMessage("You need at least 1 free slot to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (!player.getItems().playerHasItem(314, 10)) {
				player.sendMessage("You need at least 10 feathers to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (!player.getItems().playerHasItem(b.getUnfinished(), 10)) {
				player.sendMessage("You need at least 10 of this bolt type to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.playerLevel[Skill.FLETCHING.getId()] < b.getLevel()) {
				player.sendMessage("You need a fletching level of " + b.getLevel() + " to fletch this bolt.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getSkilling().isSkilling()) {
				player.getSkilling().stop();
			}
			player.getItems().deleteItem2(314, 10);
			player.getItems().deleteItem2(b.getUnfinished(), 10);
			player.getItems().addItem(b.getBolt(), 10);
			player.getPA().addSkillXP(b.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId(), true);
		});
	}

	public void fletchHeadlessArrows() {
		if (player.getItems().freeSlots() < 1) {
			player.sendMessage("You need at least 1 free slot.");
			player.getPA().removeAllWindows();
			return;
		}
		if (!player.getItems().playerHasItem(52, 15)) {
			player.sendMessage("You need at least 15 arrow shafts to do this.");
			player.getPA().removeAllWindows();
			return;
		}
		if (!player.getItems().playerHasItem(314, 15)) {
			player.sendMessage("You need at least 15 feathers to do this.");
			player.getPA().removeAllWindows();
			return;
		}
		player.getItems().deleteItem2(314, 15);
		player.getItems().deleteItem2(52, 15);
		player.getItems().addItem(53, 15);
		player.getPA().addSkillXP(15 * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId(), true);
	}

	public void fletchUnstrung(int bowId) {
		Optional<FletchableBow> bow = BOWS.stream().filter(b -> b.getItem() == bowId).findFirst();
		bow.ifPresent(b -> {
			player.getSkilling().stop();
			if (player.playerLevel[Skill.FLETCHING.getId()] < b.getLevelRequired()) {
				player.sendMessage("You need a fletching level of " + b.getLevelRequired() + " to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getSkilling().setSkill(Skill.FLETCHING);
			Server.getEventHandler().submit(new StringBowEvent(b, player, 3));
		});
	}

	public void fletchUnstrungCross(int crossbowId) {
		Optional<FletchableCrossbow> crossbow = CROSSBOWS.stream().filter(b -> b.getItem() == crossbowId).findFirst();
		crossbow.ifPresent(b -> {
			player.getSkilling().stop();
			if (player.playerLevel[Skill.FLETCHING.getId()] < b.getLevelRequired()) {
				player.sendMessage("You need a fletching level of " + b.getLevelRequired() + " to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getSkilling().setSkill(Skill.FLETCHING);
			Server.getEventHandler().submit(new StringCrossbowEvent(b, player, 3));
		});
	}

	public void fletchArrow(int arrowId) {
		Optional<FletchableArrow> arrow = ARROWS.stream().filter(a -> a.getId() == arrowId).findFirst();
		arrow.ifPresent(a -> {
			player.getSkilling().stop();
			if (player.playerLevel[Skill.FLETCHING.getId()] < a.getLevelRequired()) {
				player.sendMessage("You need a fletching level of " + a.getLevelRequired() + " to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getSkilling().setSkill(Skill.FLETCHING);
			if (!player.getItems().playerHasItem(53, 15)) {
				player.sendMessage("You need at least 15 headless arrows to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (!player.getItems().playerHasItem(a.getId(), 15)) {
				player.sendMessage("You need at least 15 arrowheads to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getItems().freeSlots() < 1) {
				player.sendMessage("You need at least 1 free slot to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getItems().deleteItem2(53, 15);
			player.getItems().deleteItem2(a.getId(), 15);
			player.getItems().addItem(a.getReward(), 15);
			player.getPA().addSkillXP((int) a.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId(), true);
		});
	}
	
	public void fletchJavelin(int arrowId) {
		Optional<FletchableJavelin> arrow = JAVELINS.stream().filter(a -> a.getId() == arrowId).findFirst();
		arrow.ifPresent(a -> {
			player.getSkilling().stop();
			if (player.playerLevel[Skill.FLETCHING.getId()] < a.getLevelRequired()) {
				player.sendMessage("You need a fletching level of " + a.getLevelRequired() + " to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getSkilling().setSkill(Skill.FLETCHING);
			if (!player.getItems().playerHasItem(19584, 15)) {
				player.sendMessage("You need at least 15 javelin shafts to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (!player.getItems().playerHasItem(a.getId(), 15)) {
				player.sendMessage("You need at least 15 javelin heads to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getItems().freeSlots() < 1) {
				player.sendMessage("You need at least 1 free slot to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getItems().deleteItem2(19584, 15);
			player.getItems().deleteItem2(a.getId(), 15);
			player.getItems().addItem(a.getReward(), 15);
			player.getPA().addSkillXP((int) a.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId(), true);
		});
	}

	public void fletchDart(int dartId) {
		Optional<FletchableDart> dart = DARTS.stream().filter(a -> a.getId() == dartId).findFirst();
		dart.ifPresent(d -> {
			player.getSkilling().stop();
			if (player.playerLevel[Skill.FLETCHING.getId()] < d.getLevelRequired()) {
				player.sendMessage("You need a fletching level of " + d.getLevelRequired() + " to do this.");
				player.getPA().removeAllWindows();

				return;
			}
			player.getSkilling().setSkill(Skill.FLETCHING);
			if (!player.getItems().playerHasItem(314, 10)) {
				player.sendMessage("You need at least 10 feathers to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (!player.getItems().playerHasItem(d.getId(), 10)) {
				player.sendMessage("You need at least 10 dart tips to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getItems().freeSlots() < 1) {
				player.sendMessage("You need at least 1 free slot to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getItems().deleteItem2(314, 10);
			player.getItems().deleteItem2(d.getId(), 10);
			player.getItems().addItem(d.getReward(), 10);
			player.getPA().addSkillXP((int) (10 * d.getExperience()) * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId(), true);
		});
	}

	public boolean fletchBolt(int boltId, int tipId) {
		Optional<FletchableBolt> bolt = BOLTS.stream().filter(b -> b.getUnfinished() == boltId && b.getTip() == tipId).findFirst();
		bolt.ifPresent(b -> {
			player.getSkilling().stop();
			if (player.playerLevel[Skill.FLETCHING.getId()] < b.getLevel()) {
				player.sendMessage("You need a fletching level of " + b.getLevel() + " to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getSkilling().setSkill(Skill.FLETCHING);
			if (!player.getItems().playerHasItem(tipId, 15)) {
				player.sendMessage("You need at least 15 tips to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (!player.getItems().playerHasItem(boltId, 15)) {
				player.sendMessage("You need at least 15 bolts to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getItems().freeSlots() < 1) {
				player.sendMessage("You need at least 1 free slot to do this.");
				player.getPA().removeAllWindows();
				return;
			}
			player.getItems().deleteItem2(boltId, 15);
			player.getItems().deleteItem2(tipId, 15);
			player.getItems().addItem(b.getBolt(), 15);
			player.getPA().addSkillXP(b.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.FLETCHING_EXPERIENCE), Skill.FLETCHING.getId(), true);
		});
		return bolt.isPresent();
	}

	public Optional<FletchableLog> getSelectedFletchable() {
		return selectedFletchable;
	}

}
