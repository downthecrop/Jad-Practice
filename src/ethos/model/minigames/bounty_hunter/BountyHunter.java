package ethos.model.minigames.bounty_hunter;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ethos.Config;
import ethos.Server;
import ethos.clip.Region;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.minigames.bounty_hunter.events.TargetDelay;
import ethos.model.minigames.bounty_hunter.events.TargetSelector;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.util.Misc;

/**
 * @author Jason MacKeigan
 * @date Nov 12, 2014, 12:20:45 PM
 */
public class BountyHunter extends CycleEvent {
	/**
	 * The amount of game ticks a player has before they receive a warning and their target is removed.
	 */
	public static final int INITIAL_BOUNDARY_TICKS = 200;

	/**
	 * The maximum number of warnings you receive before the delay of receiving a target is 5 minutes
	 */
	public static final int MAXIMUM_WARNINGS = 5;

	/**
	 * The maximum number of cycle ticks before the delay is up
	 */
	public static final int MAXIMUM_WARNING_TICKS = 500;

	/**
	 * The amount of game cycles that must pass after a player has killed a target. This allows the player time to bank without being selected as a target, or given a target.
	 */
	public static final int KILLED_TARGET_SELECTOR_DELAY = 100;

	/**
	 * The player this class is associated with for the lifetime of the player
	 */
	private Player player;

	/**
	 * The state of the target selection phase
	 */
	private TargetState targetState = TargetState.NONE;

	/**
	 * The class that will manage selecting new targets when possible.
	 */
	private TargetSelector targetSelector = new TargetSelector(this);

	/**
	 * The target delay class used to manage when a target is delayed
	 */
	private TargetDelay targetDelay = new TargetDelay(this);

	/**
	 * The players bounty hunter skull. This determines their wealth.
	 */
	private BountyHunterSkull skull = BountyHunterSkull.NONE;

	/**
	 * The player target
	 */
	private Target target;

	/**
	 * The number of bounties the player has, these are received from trading in emblems
	 */
	private int bounties;

	/**
	 * The current number of warnings this player has for terminating fights with targets
	 */
	private int warnings;

	/**
	 * The ticks left in the warning received
	 */
	private int delayedTargetTicks;

	/**
	 * The number of ticks the player has been outside the boundary
	 */
	private int outsideBoundsTicks = INITIAL_BOUNDARY_TICKS;

	/**
	 * The visibility of the statistics interface
	 */
	private boolean statisticsVisible = true;

	/**
	 * Each variable represents either the players current kills, or record in a specific feature. Each are only alive for the time of the session.
	 */
	private int currentRogueKills, currentHunterKills, recordRogueKills, recordHunterKills;

	/**
	 * The total number of rogue, and hunter kills a player has received in their lifetime.
	 */
	private int totalRogueKills, totalHunterKills;

	/**
	 * Determines if the teleport to target spell is accessible
	 */
	private boolean spellAccessible;

	/**
	 * The time in milliseconds of the players last teleport
	 */
	long lastTeleport;

	/**
	 * Creates a new BountyHunter object for the player. Every player will have thier own instance of a BountyHunter class.
	 * 
	 * @param player the player this class is created for
	 */
	public BountyHunter(Player player) {
		this.player = player;
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (targetState.isPenalized()) {
			updateTargetUI();
			return;
		}
		if (hasTarget()) {
			Player target = PlayerHandler.getPlayer(this.target.getName());
			if (Objects.isNull(target) || target.disconnected) {
				player.sendMessage("Your target has logged out, you will be given a new target shortly.");
				removeTarget();
				updateTargetUI();
				return;
			}
			if (!player.inWild()) {
				if (outsideBoundsTicks == INITIAL_BOUNDARY_TICKS) {
					player.sendMessage("<col=FF0000>You have 2 minutes to return to the Wilderness before you lose your target.</col>");
				}
				outsideBoundsTicks--;
				if (outsideBoundsTicks <= 0) {
					target.getBH().removeTarget();
					target.getBH().updateTargetUI();
					removeTarget();
					updateTargetUI();
					warnings++;
					player.sendMessage("You have been outside of the wilderness for 2 minutes, you no longer have a target.");
					return;
				}
			}
			updateTargetUI();
			return;
		}
		if (targetState.isNone() && !CycleEventHandler.getSingleton().isAlive(targetSelector) && targetSelector.isExecutable()) {
			targetState = TargetState.SELECTING;
			CycleEventHandler.getSingleton().addEvent(targetSelector, targetSelector, 50);
			return;
		}
		if (!CycleEventHandler.getSingleton().isAlive(targetDelay)) {
			if (warnings >= MAXIMUM_WARNINGS) {
				player.sendMessage("You have been penalized for receiving too many warnings. You will not receive");
				player.sendMessage("a new bounty hunter target for a short period of time.");
				targetState = TargetState.PENALIZED;
				CycleEventHandler.getSingleton().addEvent(targetDelay, targetDelay, 1);
			} else if (targetState.hasKilledRecently()) {
				delayedTargetTicks = BountyHunter.KILLED_TARGET_SELECTOR_DELAY;
				player.sendMessage("<col=255>You have 60 seconds before you will be selected a target again.</col>");
				CycleEventHandler.getSingleton().addEvent(targetDelay, targetDelay, 1);
			}
			return;
		}
	}

	/**
	 * The player skull should be updated when the worth of the player has changed. Worth it determined by what it is they are risking in their inventory, and equipment.
	 */
	public void updateSkull() {
		skull = BountyHunterSkull.getSkull(player.getItems().getTotalRiskedWorth());
	}

	public void updateStatisticsUI() {
		player.getPA().sendFrame126(Integer.toString(currentRogueKills), 28025);
		player.getPA().sendFrame126(Integer.toString(currentHunterKills), 28026);
		player.getPA().sendFrame126(Integer.toString(recordRogueKills), 28027);
		player.getPA().sendFrame126(Integer.toString(recordHunterKills), 28028);
	}

	/**
	 * The target interface should be updated when the distance between both players has changed, or the target has been removed. It should also be updated under a few other
	 * circumstances.
	 */
	public void updateTargetUI() {
		updateStatisticsUI();
		player.getPA().sendFrame171(statisticsVisible ? 0 : 1, 28020);
		if (target == null) {
			player.getPA().sendFrame126("None", 28004);
			if (targetState.isPenalized()) {
				int minutes = 1 + Math.abs(delayedTargetTicks / 100);
				player.getPA().sendFrame126(minutes + " minute penalty", 28005);
			} else {
				player.getPA().sendFrame126("@yel@------", 28005);
			}
			player.getPA().sendFrame126("---", 28006);
			player.getPA().sendFrame171(0, 28030);
			for (int i = 28032; i <= 28040; i += 2) {
				player.getPA().sendFrame171(1, i);
			}
			return;
		}
		Player playerTarget = PlayerHandler.getPlayer(target.getName());
		int distance = Misc.distanceToPoint(player.getX(), player.getY(), playerTarget.getX(), playerTarget.getY());
		String color = distance < 15 ? "gre" : distance >= 15 && distance < 45 ? "blu" : "red";
		String display = playerTarget.wildLevel > 0 ? "Lvl " + playerTarget.wildLevel + "-" + (playerTarget.wildLevel + 3) : "Safe";
		updateSkull();
		player.getPA().sendFrame126(target.getName(), 28004);
		player.getPA().sendFrame126("@" + color + "@" + display + ", Cmb " + playerTarget.combatLevel, 28005);
		player.getPA().sendFrame126("Wealth: " + playerTarget.getBH().skull.getRepresentation(), 28006);
		for (int i = 28030; i <= 28040; i += 2) {
			if (i == playerTarget.getBH().skull.getFrameId()) {
				player.getPA().sendFrame171(0, i);
			} else {
				player.getPA().sendFrame171(1, i);
			}
		}
	}

	public void updateOutsideTimerUI() {
		long millis = (long) ((outsideBoundsTicks * .6) * 1000L);
		long second = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
		long minute = TimeUnit.MILLISECONDS.toMinutes(millis);
		String format = String.format("%2d:%02d", minute, second, millis);
		player.getPA().sendFrame126(format, 28072);
	}

	/**
	 * Physically upgrades the player emblem in their inventory if they have one. How it works is the getBest function will determine the best owned 'emblem' in the players
	 * inventory and attempt to upgrade it. In the case that it cannot, because the player has no emblems, or has a tier 10 emblem it will return null, that is why we perform the
	 * {@code java.util.Objects.nonNull(Object)} check.
	 */
	public void upgradePlayerEmblem() {
		Optional<BountyHunterEmblem> emblem = BountyHunterEmblem.getBest(player, true);
		emblem.ifPresent(e -> {
			player.getItems().deleteItem2(e.getItemId(), 1);
			player.getItems().addItem(e.getNextOrLast().getItemId(), 1);
			player.sendMessage("<col=255>Your emblem has been upgraded to " + Misc.capitalize(e.name().toLowerCase().replaceAll("_", " ")) + ".</col>");
		});
	}

	public void dropPlayerEmblem(Player killer) {
		Optional<BountyHunterEmblem> emblem = BountyHunterEmblem.getBest(player, false);
		BountyHunterEmblem.EMBLEMS.forEach(e -> player.getItems().deleteItem2(e.getItemId(), player.getItems().getItemAmount(e.getItemId())));
		if (Misc.random(9) != 0 && killer.getBH().hasTarget() && hasTarget() && killer.playerName.equalsIgnoreCase(target.getName())) {
			if (emblem.isPresent()) {
				Server.itemHandler.createGroundItem(killer, emblem.get().getPreviousOrFirst().getItemId(), player.getX(), player.getY(), player.heightLevel, 1, killer.getIndex());
			} else {
				if (Misc.random(3) != 0) {
					Server.itemHandler.createGroundItem(killer, BountyHunterEmblem.TIER_1.getItemId(), player.getX(), player.getY(), player.heightLevel, Config.DOUBLE_PKP ? 2 : 1,
							killer.getIndex());
				}
			}
		}
	}

	/**
	 * Calculates the total networth for the emblems in a players inventory.
	 * 
	 * @return the total networth of all emblems in a players inventory
	 */
	public int getNetworthForEmblems() {
		int worth = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			int itemId = player.playerItems[i] - 1;
			Optional<BountyHunterEmblem> containsItem = BountyHunterEmblem.EMBLEMS.stream().filter(emblem -> emblem.getItemId() == itemId).findFirst();
			if (containsItem.isPresent()) {
				worth += containsItem.get().getBounties();
			}
		}
		return worth;
	}

	/**
	 * Teleports the player the their target so long as both players meet certain conditions such as level requirement, item requirements, etcetera.
	 */
	public void teleportToTarget() {
		if (player.inClanWars()) {
			player.sendMessage("@cr10@You can not do this here.");
			return;
		}
		if (!spellAccessible) {
			player.sendMessage("You do not have access to this spell, you must learn about it first.");
			return;
		}
		if (player.playerLevel[player.playerMagic] < 85) {
			player.sendMessage("You need a magic level of 85 to use this spell.");
			return;
		}
		if (!hasTarget()) {
			player.sendMessage("You need to have a target to use this spell.");
			return;
		}
		if (System.currentTimeMillis() - lastTeleport < 30_000) {
			player.sendMessage("You can only use this spell every 30 seconds.");
			return;
		}
		if (!player.getPA().canTeleport("glory")) {
			return;
		}
		Player target = PlayerHandler.getPlayer(this.target.getName());
		if (Objects.isNull(target)) {
			player.sendMessage("Your target cannot be found.");
			return;
		}
		if (!target.inWild()) {
			player.sendMessage("Your target is not in the wilderness, they must be to be teleported to.");
			return;
		}
		if (player.playerIndex > 0 && target.getIndex() == player.playerIndex) {
			player.sendMessage("You cannot use this spell whilst in combat with your target.");
			return;
		}
		int targetX = target.getX();
		int targetY = target.getY();
		for (int teleX = targetX - 1; teleX < targetX + 2; teleX++) {
			for (int teleY = targetY - 1; teleY < targetY + 2; teleY++) {
				if (!Region.canMove(teleX, teleY, teleX + 1, teleY + 1, target.heightLevel, 1, 1)) {
					player.sendMessage("Your target is in a blocked in area, you cannot teleport to them right now.");
					return;
				}
			}
		}
		player.getPA().startTeleport(targetX, targetY - 1, target.heightLevel, "modern", false);
		lastTeleport = System.currentTimeMillis();
	}

	/**
	 * Sets the current number of warnings to that of the paramater
	 * 
	 * @param warnings the number of warnings
	 */
	public void setWarnings(int warnings) {
		this.warnings = warnings;
	}

	/**
	 * The number of warnings a player has received for terminating fights with targets
	 * 
	 * @return the number of warnings
	 */
	public int getWarnings() {
		return warnings;
	}

	/**
	 * The player that owns this BountyHunter instance
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * The state of selecting a target
	 * 
	 * @return the state
	 */
	public TargetState getTargetState() {
		return targetState;
	}

	/**
	 * Sets the targets state
	 * 
	 * @param targetState the state
	 */
	public void setTargetState(TargetState targetState) {
		this.targetState = targetState;
	}

	/**
	 * The target selector event
	 * 
	 * @return the event
	 */
	public TargetEvent getTargetSelector() {
		return targetSelector;
	}

	/**
	 * The target selector event
	 * 
	 * @return the event
	 */
	public TargetEvent getTargetDelay() {
		return targetDelay;
	}

	/**
	 * Sets the ticks remaining in the delayed target selection
	 * 
	 * @param ticks the ticks
	 */
	public void setDelayedTargetTicks(int ticks) {
		delayedTargetTicks = ticks;
	}

	/**
	 * Returns the amount of ticks remaining until the delay is up
	 * 
	 * @return the amount of ticks
	 */
	public int getDelayedTargetTicks() {
		return delayedTargetTicks;
	}

	/**
	 * Determines if the player has a target
	 * 
	 * @return true if they have a target
	 */
	public boolean hasTarget() {
		return target != null;
	}

	/**
	 * Removes the target
	 */
	public void removeTarget() {
		player.getPA().createPlayerHints(10, -1);
		targetState = TargetState.NONE;
		outsideBoundsTicks = INITIAL_BOUNDARY_TICKS;
		target = null;
	}

	/**
	 * Sets the new target
	 * 
	 * @param target the player target
	 */
	public void setTarget(Target target) {
		this.target = target;
	}

	/**
	 * Determines the players record in bounty hunter kills
	 * 
	 * @return
	 */
	public int getRecordHunterKills() {
		return recordHunterKills;
	}

	/**
	 * Sets the number of record hunter kills for this player
	 * 
	 * @param recordHunterKills the number of hunter kills
	 */
	public void setRecordHunterKills(int recordHunterKills) {
		this.recordHunterKills = recordHunterKills;
	}

	/**
	 * Determines the number of rogue kills the player has
	 * 
	 * @return the number of kills
	 */
	public int getCurrentRogueKills() {
		return currentRogueKills;
	}

	/**
	 * Sets the current number of rogue kills
	 * 
	 * @param currentRogueKills the number of rogue kills
	 */
	public void setCurrentRogueKills(int currentRogueKills) {
		this.currentRogueKills = currentRogueKills;
	}

	/**
	 * Determines the record of rogue kilks the player has received
	 * 
	 * @return number of rogue kills
	 */
	public int getRecordRogueKills() {
		return recordRogueKills;
	}

	/**
	 * Sets the number of record rogue kills
	 * 
	 * @param recordRogueKills the number of rogue kills
	 */
	public void setRecordRogueKills(int recordRogueKills) {
		this.recordRogueKills = recordRogueKills;
	}

	/**
	 * Determines our current number of hunter kills
	 * 
	 * @return the number of hunter kills
	 */
	public int getCurrentHunterKills() {
		return currentHunterKills;
	}

	/**
	 * Sets our current number of hunter kills
	 * 
	 * @param currentHunterKills the hunter kills
	 */
	public void setCurrentHunterKills(int currentHunterKills) {
		this.currentHunterKills = currentHunterKills;
	}

	/**
	 * Determines our total number of hunter kills
	 * 
	 * @return total hunter kills
	 */
	public int getTotalHunterKills() {
		return totalHunterKills;
	}

	/**
	 * Sets the total number of hunter kills
	 * 
	 * @param totalHunterKills the number of hunter kills
	 */
	public void setTotalHunterKills(int totalHunterKills) {
		this.totalHunterKills = totalHunterKills;
	}

	/**
	 * Determines the total number of rogue kills
	 * 
	 * @return the total number of rogue kills
	 */
	public int getTotalRogueKills() {
		return totalRogueKills;
	}

	/**
	 * Sets the total number of rogue kills
	 * 
	 * @param totalRogueKills the number of rigue kills
	 */
	public void setTotalRogueKills(int totalRogueKills) {
		this.totalRogueKills = totalRogueKills;
	}

	/**
	 * The target for the player, if they have one.
	 * 
	 * @return the target
	 */
	public Target getTarget() {
		return target;
	}

	/**
	 * Bounties received from emblems
	 * 
	 * @return the number of bounties
	 */
	public int getBounties() {
		return bounties;
	}

	/**
	 * Set the number of bounties to that of the parameter.
	 * 
	 * @param bounties the number of bounties
	 */
	public void setBounties(int bounties) {
		this.bounties = bounties;
	}

	/**
	 * Determines if the statistics interface is visible
	 * 
	 * @return true if it is, otherwise false
	 */
	public boolean isStatisticsVisible() {
		return statisticsVisible;
	}

	/**
	 * Sets the visibility of the bounty hunter interface to either visible or invisible
	 * 
	 * @param statisticsVisible the state of visbility
	 */
	public void setStatisticsVisible(boolean statisticsVisible) {
		this.statisticsVisible = statisticsVisible;
	}

	/**
	 * Determines if the spell to teleport to a target is accessible
	 * 
	 * @return true if we can
	 */
	public boolean isSpellAccessible() {
		return spellAccessible;
	}

	/**
	 * Sets the state of the ability to teleport to a target
	 * 
	 * @param spellAccessible the state
	 */
	public void setSpellAccessible(boolean spellAccessible) {
		this.spellAccessible = spellAccessible;
	}

}
