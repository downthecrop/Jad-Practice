package ethos.model.content;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import ethos.model.players.Player;
import ethos.model.players.Right;
import ethos.model.players.mode.Mode;
import ethos.model.players.mode.ModeType;

public class IronManInterface {

	/**
	 * The state of an action being unselected
	 */
	private static final byte UNSELECTED = 0;

	/**
	 * The state of an action being selected
	 */
	private static final byte SELECTED = 2;

	/**
	 * The state of an action being unselectable
	 */
	private static final byte UNSELECTABLE = 1;

	/**
	 * An immutable set of {@link Action}'s to be used as reference
	 */
	private static final Set<Action> ACTIONS = ImmutableSet.copyOf(EnumSet.allOf(Action.class));

	/**
	 * An immutable set of {@link ActionNegation}'s
	 */
	private static final Set<ActionNegation> NEGATIONS = ImmutableSet.copyOf(EnumSet.allOf(ActionNegation.class));

	/**
	 * The player this is being created for
	 */
	private final Player player;

	/**
	 * A mapping of each action and the state of that action
	 */
	private final Map<Action, Byte> actions = new HashMap<>();

	/**
	 * Creates a new class for the player
	 */
	public IronManInterface(Player player) {
		this.player = player;

		actions.put(Action.NONE, SELECTED);
		actions.put(Action.STANDARD_IRON_MAN, UNSELECTED);
		actions.put(Action.ULTIMATE_IRON_MAN, UNSELECTED);
		actions.put(Action.OSRS, UNSELECTED);
		actions.put(Action.NPC, UNSELECTABLE);
		actions.put(Action.PERMANENT, UNSELECTABLE);
	}

	/**
	 * Used to manage interface button actions
	 * 
	 * @param buttonId the button clicked
	 */
	public void click(int buttonId) {
		Optional<Action> clicked = ACTIONS.stream().filter(a -> a.buttonId == buttonId).findFirst();
		clicked.ifPresent(action -> {
			if (actions.containsKey(action)) {
				byte state = actions.get(action);
				if (state == UNSELECTABLE) {
					player.sendMessage("This option cannot be selected with your current mode choice.");
					return;
				}
				if (state == SELECTED) {
					player.sendMessage("This option has already been selected.");
					return;
				}
			}
			Optional<ActionNegation> negated = NEGATIONS.stream().filter(neg -> neg.trigger == action).findFirst();
			negated.ifPresent(neg -> {
				actions.put(action, SELECTED);
				List<Action> unselected = Arrays.asList(neg.unselected);
				List<Action> unselectable = Arrays.asList(neg.unselectable);

				for (Action u : unselected) {
					actions.put(u, UNSELECTED);
				}

				for (Action un : unselectable) {
					actions.put(un, UNSELECTABLE);
				}
				refresh();
			});
		});
	}

	/**
	 * Refreshes the interface by letting the client know which options you have chosen.
	 */
	public void refresh() {
		for (Entry<Action, Byte> entry : actions.entrySet()) {
			Action action = entry.getKey();
			Byte state = entry.getValue();
			player.getPA().sendChangeSprite(action.componentId, state);
		}
	}

	/**
	 * Confirms the choice you have made
	 */
	public void confirm() {
		if (!player.getTutorial().isActive()) {
			player.getPA().closeAllWindows();
			return;
		}
		List<Entry<Action, Byte>> selected = actions.entrySet().stream().filter(e -> e.getValue() == SELECTED).collect(Collectors.toList());
		if (selected.stream().noneMatch(entry -> entry.getKey() == Action.NONE)) {
			Optional<Entry<Action, Byte>> revert = selected.stream().filter(e -> e.getKey() == Action.NPC || e.getKey() == Action.PERMANENT).findFirst();
			Optional<Entry<Action, Byte>> mode = selected.stream().filter(e -> e.getKey() == Action.STANDARD_IRON_MAN || e.getKey() == Action.ULTIMATE_IRON_MAN || e.getKey() == Action.OSRS).findFirst();

			if (!mode.isPresent()) {
				player.sendMessage("You must select a mode.");
				return;
			}
			if (!revert.isPresent()) {
				player.sendMessage("You must select one of the two options below to continue.");
				return;
			}
			Action modeAction = mode.get().getKey();
			Action revertAction = revert.get().getKey();
			switch (modeAction) {
			case STANDARD_IRON_MAN:
				player.setMode(Mode.forType(ModeType.IRON_MAN));
				player.getRights().setPrimary(Right.IRONMAN);
				player.setRevertOption(revertAction.name());
				break;
				
			case ULTIMATE_IRON_MAN:
				player.setMode(Mode.forType(ModeType.ULTIMATE_IRON_MAN));
				player.getRights().setPrimary(Right.ULTIMATE_IRONMAN);
				player.setRevertOption(revertAction.name());
				break;
				
			case OSRS:
				player.setMode(Mode.forType(ModeType.OSRS));
				player.getRights().setPrimary(Right.OSRS);
				break;
				
			default:
				break;
			}
		} else {
			player.setMode(Mode.forType(ModeType.REGULAR));
		}
		player.getPA().requestUpdates();
		player.getTutorial().proceed();
	}

	public enum Action {
		STANDARD_IRON_MAN(165162, 42402), 
		ULTIMATE_IRON_MAN(165163, 42403), 
		NONE(165164, 42404), 
		NPC(165165, 42405), 
		PERMANENT(165166, 42406), 
		OSRS(165183, 42423);

		/**
		 * The button clicked to perform the action
		 */
		private final int buttonId;

		/**
		 * The id of the component or button
		 */
		private final int componentId;

		/**
		 * Creates a new action for a button on the interface
		 * 
		 * @param buttonId the button
		 * @param componentId the component
		 */
		private Action(int buttonId, int componentId) {
			this.buttonId = buttonId;
			this.componentId = componentId;
		}
	}

	enum ActionNegation {
		STANDARD_IRON_MAN(Action.STANDARD_IRON_MAN, new Action[] { 
				Action.ULTIMATE_IRON_MAN, 
				Action.NONE, 
				Action.OSRS, 
				Action.NPC, 
				Action.PERMANENT 
		}, new Action[] {
				
		}), 
		ULTIMATE_IRON_MAN(Action.ULTIMATE_IRON_MAN, new Action[] { 
				Action.STANDARD_IRON_MAN, 
				Action.NONE, 
				Action.OSRS, 
				Action.NPC, 
				Action.PERMANENT 
		},new Action[] {
				
		}), 
		NONE(Action.NONE, new Action[] { 
				Action.ULTIMATE_IRON_MAN, 
				Action.STANDARD_IRON_MAN,
				Action.OSRS
		}, new Action[] { 
				Action.NPC, 
				Action.PERMANENT 
		}), 
		NPC(Action.NPC, new Action[] { 
				Action.PERMANENT 
		}, new Action[] {
						
		}), 
		PERMANENT(Action.PERMANENT, new Action[] { 
				Action.NPC 
		}, new Action[] {
				
		}), 
		OSRS(Action.OSRS, new Action[] { 
				Action.ULTIMATE_IRON_MAN, 
				Action.STANDARD_IRON_MAN, 
				Action.NONE,
				Action.PERMANENT 
		}, new Action[] {
				Action.NPC
		});

		/**
		 * The action that triggers another group of actions negation
		 */
		private final Action trigger;

		/**
		 * An array of {@link Action}'s that are deemed 'unselected' at the time of the action being performed
		 */
		private final Action[] unselected;

		/**
		 * An array of {@link Action}'s that are deemed 'unselectable' at the time of the action being performed
		 */
		private final Action[] unselectable;

		/**
		 * Every {@link Action} also performs some form of negations on other actions.
		 * 
		 * @param unselected the actions being unselected
		 * @param unselectable the actions being unselectable
		 */
		private ActionNegation(Action trigger, Action[] unselected, Action[] unselectable) {
			this.trigger = trigger;
			this.unselectable = unselectable;
			this.unselected = unselected;
		}
	}

}
