package ethos.model.players.skills.prayer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import ethos.Config;
import ethos.Server;
import ethos.event.Event;
import ethos.model.items.ItemDefinition;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Skill;

/**
 * A class that manages a single player training the {@code Skill.PRAYER} skill.
 * 
 * @author Jason MacKeigan
 * @date Mar 10, 2015, 2015, 2:48:38 AM
 */
public class Prayer {

	/**
	 * The current bone being used on the altar
	 */
	private Optional<Bone> altarBone = Optional.empty();

	/**
	 * The time that must pass before two bones can be buried consecutively.
	 */
	private static final int BURY_DELAY = 1_200;

	/**
	 * A set of all bones that cannot be modified at any time to ensure consistency
	 */
	private static final Set<Bone> BONES = Collections.unmodifiableSet(EnumSet.allOf(Bone.class));

	/**
	 * The player that will be training the {@code Skill.PRAYER} skill.
	 */
	private final Player player;

	/**
	 * Tracks the time in milliseconds of the last bury or use of bone on an altar
	 */
	private Stopwatch lastAction = Stopwatch.createStarted();

	/**
	 * Creates a new class that will manage training the prayer skill for an individual player.
	 * 
	 * @param player the player training the skill
	 */
	public Prayer(Player player) {
		this.player = player;
	}

	/**
	 * Attempts to bury a single bone into the dirt
	 * 
	 * @param bone the bone being burried
	 */
	public void bury(Bone bone) {
		player.getSkilling().stop();
		if (!player.getItems().playerHasItem(bone.getItemId())) {
			return;
		}
		if (lastAction.elapsed(TimeUnit.MILLISECONDS) < BURY_DELAY) {
			return;
		}
		player.getSkilling().setSkill(Skill.PRAYER);
		ItemDefinition definition = ItemDefinition.forId(bone.getItemId());
		int prayerRestore=0;
		if(player.getItems().isWearingItem(22111)){
			switch(bone.getItemId()){
				case 526:
					prayerRestore+=1;
					break;
				case 532:
					prayerRestore+=2;
					break;
				case 534:
					prayerRestore+=3;
					break;
				case 536:
				case 6812:
				case 6729:
					prayerRestore+=4;
					break;
				case 22124:
					prayerRestore+=5;
					break;

			}
			player.playerLevel[5] += prayerRestore;
			if (player.playerLevel[5] > player.getLevelForXP(player.playerXP[5])) {
				player.playerLevel[5] = player.getLevelForXP(player.playerXP[5]);
			}
			player.getPA().refreshSkill(5);
		}
		player.sendMessage("You bury the " + (definition == null ? "bone" : definition.getName()) + ".");
		player.getPA().addSkillXP(bone.getExperience() * (Boundary.isIn(player, Boundary.LAVA_DRAGON_ISLE) && bone.getItemId() == 11943 ? (player.getMode().getType().equals(ModeType.OSRS) ? 4 * 4 : Config.PRAYER_EXPERIENCE * 4) : (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.PRAYER_EXPERIENCE)), Skill.PRAYER.getId(), true);
		player.getItems().deleteItem2(bone.getItemId(), 1);
		player.startAnimation(827);
		lastAction.reset();
		lastAction.start();
	}

	public void alter(final int amount) {
		if (!altarBone.isPresent()) {
			return;
		}
		Bone bone = altarBone.get();
		player.boneOnAltar = false;
		player.getSkilling().stop();
		if (!player.getItems().playerHasItem(bone.getItemId())) {
			return;
		}
		if (lastAction.elapsed(TimeUnit.MILLISECONDS) < BURY_DELAY) {
			return;
		}
		ItemDefinition definition = ItemDefinition.forId(bone.getItemId());
		player.getPA().stillGfx(624, player.getX(), player.getY(), player.heightLevel, 1);
		player.getPA().addSkillXP(bone.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 * 3 : Config.PRAYER_EXPERIENCE * 3), Skill.PRAYER.getId(), true);
		player.getItems().deleteItem2(bone.getItemId(), 1);
		player.startAnimation(713);
		lastAction.reset();
		lastAction.start();
		player.getSkilling().setSkill(Skill.PRAYER);
		Server.getEventHandler().submit(new Event<Player>("skilling", player, 3) {
			int remaining = amount - 1;

			@Override
			public void execute() {
				if (player == null || player.disconnected || player.getSession() == null) {
					super.stop();
					return;
				}
				if (!player.getItems().playerHasItem(bone.getItemId())) {
					super.stop();
					player.sendMessage("You have run out of " + (definition == null ? "bones" : definition.getName()) + ".");
					return;
				}
				if (remaining <= 0) {
					super.stop();
					return;
				}
				remaining--;
				player.turnPlayerTo(player.objectX, player.objectY);
				player.getPA().stillGfx(624, player.getX(), player.getY(), player.heightLevel, 1);
				player.getPA().addSkillXP(bone.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 * 3 : Config.PRAYER_EXPERIENCE * 3), Skill.PRAYER.getId(), true);
				player.getItems().deleteItem2(bone.getItemId(), 1);
				player.startAnimation(713);
				lastAction.reset();
				lastAction.start();
			}

		});
	}

	/**
	 * The bone last used on the altar
	 * 
	 * @return the bone on the altar
	 */
	public Optional<Bone> getAltarBone() {
		return altarBone;
	}

	/**
	 * Modifies the last bone used on the altar to the parameter
	 * 
	 * @param altarBone the bone on the altar
	 */
	public void setAltarBone(Optional<Bone> altarBone) {
		this.altarBone = altarBone;
	}

	/**
	 * Determines if the {@code itemId} matches any of the {@link Bone} itemId values.
	 * 
	 * @param itemId the item id we're comparing
	 * @return {@code true} if a bone exists with a matching itemId.
	 */
	public static Optional<Bone> isOperableBone(int itemId) {
		return BONES.stream().filter(bone -> bone.getItemId() == itemId).findFirst();
	}
}
