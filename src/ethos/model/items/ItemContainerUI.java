package ethos.model.items;

import ethos.model.players.Player;

public interface ItemContainerUI {

	/**
	 * Shows the user interface to the player by making it visible and performing any other updates that may be required.
	 * 
	 * @param player the player the user interface is being shown for
	 */
	public void show(Player player);

	/**
	 * Performs some logic to visually refresh or update the information on a user interface that is relevant to the container.
	 * 
	 * <p>
	 * Not all {@link ItemContainer} objects will be associated with a user interface. Only containers that are associated with a user interface should implement this interface.
	 * </p>
	 */
	public void update(Player player);

}
