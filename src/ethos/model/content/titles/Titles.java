package ethos.model.content.titles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;

import ethos.model.items.ItemDefinition;
import ethos.model.players.Player;
import ethos.util.Misc;

/**
 * The purpose of this class is to maintain any and all titles a single player may have the rights to.
 * 
 * @author Jason MacKeigan
 * @date Jan 22, 2015, 3:36:20 PM
 */
public class Titles {

	/**
	 * A single instance of the {@linkplain TitleRequirement} interface that does not implement the <b>meetsRequirement(Client)</b> function.
	 */
	public static final TitleRequirement NO_REQUIREMENT = new TitleRequirement() {
	};

	/**
	 * A collection of all purcahsed titles by the player
	 */
	private List<Title> purchased = new ArrayList<>();

	/**
	 * A mapping that contains a title for each slot.
	 */
	private Map<TitleMenuSlot, Title> titles = new HashMap<>();

	/**
	 * The player the actions of this class refer to
	 */
	private Player player;

	/**
	 * The title selected by the player
	 */
	private Optional<Title> selected = Optional.empty();

	/**
	 * The players temporary custom title that is updated when typing into the field
	 */
	private String temporaryCustomTitle = "";

	/**
	 * The current title the player has
	 */
	private String currentTitle = "";

	/**
	 * Creates a new class
	 * 
	 * @param player
	 */
	public Titles(Player player) {
		this.player = player;
	}

	/**
	 * This function will sort all items in the menu in an order that is relative to the titles purchability or current ownership.
	 */
	public void display() {
		List<Title> bought = populateMenu(0, (byte) 2, t -> isPurchased(t) && t.getRequirement().meetsStandard(player));
		List<Title> undisplayable = populateMenu(bought.size(), (byte) 3, t -> isPurchased(t) && !t.getRequirement().meetsStandard(player));
		populateMenu(bought.size() + undisplayable.size(), (byte) 1, t -> !isPurchased(t));
		view(Title.NONE);
		player.getPA().showInterface(53501);
	}

	/**
	 * Sorts a certain portion of the menu starting at the offset.
	 * 
	 * @param offset the position on the menu to start at
	 * @param spriteId the id value of the menu slot sprite
	 * @param predicate some pretense as to what will populate this menu
	 * @return a list containing sorted titles
	 */
	private List<Title> populateMenu(int offset, byte spriteId, Predicate<Title> predicate) {
		List<Title> l = Title.TITLES.stream().filter(predicate).collect(Collectors.toList());
		Collections.sort(l);
		for (Title title : l) {
			if (offset > TitleMenuSlot.values().length - 1) {
				break;
			}
			TitleMenuSlot slot = TitleMenuSlot.values()[offset];
			player.getPA().sendChangeSprite(slot.getStringId() - 1, spriteId);
			player.getPA().sendString(title.getName(), slot.getStringId());
			titles.put(slot, title);
			offset++;
		}
		return l;
	}

	/**
	 * Manages button clicks from the ClickingButtons class.
	 * 
	 * @param buttonId the button
	 * @return true if the button clicked belongs to the title interface
	 */
	public boolean click(int buttonId) {
		if (buttonId == 209012) {
			player.sendMessage("<col=1B6607>Green</col> titles are purchased and displayable.");
			player.sendMessage("<col=ff6600>Orange</col> titles are purchased but undisplayable at this time.");
			player.sendMessage("<col=C10915>Red</col> titles haven't been bought yet, but may be if you meet requirements.");
			return true;
		}
		if (buttonId == 209004) {
			if (!selected.isPresent()) {
				player.sendMessage("You have not selected a title.");
				return true;
			}
			Title temp = selected.get();
			if (isPurchased(temp)) {
				select(temp);
			} else {
				purchase(temp);
			}
			return true;
		}
		TitleMenuSlot menuSlot = TitleMenuSlot.get(buttonId);
		if (menuSlot == null) {
			return false;
		}
		Title title = titles.get(menuSlot);
		if (title == null) {
			return false;
		}
		selected = Optional.of(title);
		view(title);
		return true;
	}

	/**
	 * Attempts to view the description, cost, and name of a title by clicking one of the menu slots.
	 * 
	 * @param title the title selected
	 */
	private void view(Title title) {
		String description = WordUtils.wrap(title.getDescription(), 32);
		description = description.replaceAll("\\n", "\\\\n");
		player.getPA().sendString(description, 53511);
		if (title.equals(Title.CUSTOM)) {
			player.getPA().sendString(currentTitle, 53536);
			player.getPA().sendFrame171(1, 53530);
			player.getPA().sendFrame171(0, 53535);
		} else {
			player.getPA().sendString(title.getName(), 53531);
			player.getPA().sendFrame171(0, 53530);
			player.getPA().sendFrame171(1, 53535);
		}
		player.getPA().sendString(isPurchased(title) ? "Select" : "Purchase", 53512);
		player.getPA().sendString(title.getCost() == 0 ? "FREE" : Misc.getValueRepresentation(title.getCost()), 53515);
		player.getPA().sendChangeSprite(53517, title.getCurrency().getSpriteIndex());
	}

	/**
	 * Confirms the purchase a title
	 * 
	 * @param title the title being selected
	 */
	private void purchase(Title title) {
		int item = title.getCurrency().getItemId();
		int cost = title.getCost();
		if (!player.getItems().playerHasItem(item, cost)) {
			ItemDefinition definition = ItemDefinition.forId(item);
			if (definition == null) {
				return;
			}
			player.sendMessage("You need at least " + cost + " " + definition.getName() + " to purchase this title.");
			return;
		}
		if (!title.getRequirement().meetsStandard(player)) {
			player.sendMessage("You don't have the requirements stated in the description to purchase this title.");
			return;
		}
		if (isPurchased(title)) {
			player.sendMessage("You have already obtained this title, you cannot purchase it again.");
			return;
		}
		player.getItems().deleteItem(item, cost);
		player.sendMessage("You have purchased the title '" + title + "', congratulations.");
		purchased.add(title);
		display();
	}

	/**
	 * After viewing a title you can choose to select it
	 * 
	 * @param title the title
	 */
	private void select(Title title) {
		if (!isPurchased(title)) {
			player.sendMessage("You must purcahse the title before your display it.");
			return;
		}
		if (currentTitle.equalsIgnoreCase(title.getName())) {
			player.sendMessage("You already have this title selected.");
			return;
		}
		if (!title.getRequirement().meetsStandard(player)) {
			player.sendMessage("You don't have the requirements stated in the description to purchase this title.");
			return;
		}
		if (title.equals(Title.CUSTOM)) {
			if (temporaryCustomTitle.length() < 1) {
				player.sendMessage("The length of your custom title must be at least one.");
				return;
			}
			if (temporaryCustomTitle.equalsIgnoreCase("admin") || temporaryCustomTitle.equalsIgnoreCase("mod") || temporaryCustomTitle.equalsIgnoreCase("staff")) {
				player.sendMessage("You cannot use this custom title because it represents a staff title.");
				return;
			}
			Optional<Title> illegalMatch = Title.TITLES.stream().filter(t -> t.getName().equalsIgnoreCase(temporaryCustomTitle)).findFirst();
			if (illegalMatch.isPresent()) {
				player.sendMessage("You cannot display a title that already exists within the menu.");
				return;
			}
			currentTitle = temporaryCustomTitle;
		} else {
			currentTitle = title.getName();
		}
		player.sendMessage("Your title has been changed to '" + title.name() + "'.");
		player.getPA().requestUpdates();
	}

	/**
	 * Determines if we have already purchased the title by determining if any of the titles we currently have match the name of that title.
	 * 
	 * @param title the title we're looking to determine has been purchased
	 * @return true if the title has been purchased
	 */
	public boolean isPurchased(Title title) {
		return purchased.stream().anyMatch(t -> t.getName().equals(title.getName()));
	}

	/**
	 * Returns the list of titles that have been already purchased
	 * 
	 * @return the list of purchased titles
	 */
	public List<Title> getPurchasedList() {
		return purchased;
	}

	/**
	 * The current temporary custom title
	 * 
	 * @return the temproary custom title
	 */
	public String getTemporaryCustomTitle() {
		return temporaryCustomTitle;
	}

	/**
	 * Modifies the current temporary custom title to the String we pass
	 * 
	 * @param temporaryCustomTitle the new temp custom title
	 */
	public void setTemporaryCustomTitle(String temporaryCustomTitle) {
		this.temporaryCustomTitle = temporaryCustomTitle;
	}

	/**
	 * The current title
	 * 
	 * @return custom title
	 */
	public String getCurrentTitle() {
		if (currentTitle == null) {
			return "";
		}
		return currentTitle;
	}

	/**
	 * Modifies the current title to that of the one we pass in the parameter
	 * 
	 * @param currentTitle the title
	 */
	public void setCurrentTitle(String currentTitle) {
		this.currentTitle = currentTitle;
	}

}
