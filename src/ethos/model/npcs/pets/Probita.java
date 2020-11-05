package ethos.model.npcs.pets;

import java.util.stream.IntStream;

import ethos.model.players.Player;

public enum Probita {

	/**
	 * Invalid pet data
	 * Invalid id, valid id
	 */
	MOLE_JR(15571, 12646),
	VENENATIS_JR(8135, 13177),
	CALLISTO_JR(15572, 13178),
	CHAOS_ELEMENTAL_JR(15568, 11995),
	VETION_JR(15573, 13179),
	OCCULT_NECKLACE(11144, 12002);

	private int invalidPet;
	private int validPet;

	/**
	 * @return the invalid item id
	 */
	public int getinvalidPet() {
		return invalidPet;
	}

	/**
	 * @return the valid item id
	 */
	public int getValidPet() {
		return validPet;
	}

	/**
	 * Constructs an enumeration of invalid and valid id's
	 * @param invalidPet	the invalid item id
	 * @param validPet		the valid item id
	 */
	Probita(int invalidPet, int validPet) {
		this.invalidPet = invalidPet;
		this.validPet = validPet;
	}
	
	/**
	 * Attempts to cancel a players previously invalid pet
	 * @param player	the player to cancel the invalid pet
	 */
	final static int[] INVALID_PETS = new int[] { 15571, 15572, 8135, 15568 };

	public static void cancellationOfPreviousPet(final Player player) {
		if (IntStream.of(INVALID_PETS).anyMatch(id -> player.summonId == id)) {
			if (player.summonId == 15572) {
				player.getItems().addItem(13178, 1);
				player.sendMessage("A callisto cub have been added into your inventory.");
			}
			player.hasFollower = false;
			player.summonId = -1;
			player.sendMessage("You've cancelled your previous invalid pet, if this was incorrect; Contact staff");
			player.sendMessage("on the forums...");
		} else {
			player.sendMessage("You do not have a invalid pet spawned.");
			return;
		}
	}

	/**
	 * Replaces the invalid pets with valid pets
	 * @param player	the player who is exchanging
	 */
	public static void hasInvalidPet(final Player player) {
		for (Probita pet : Probita.values()) {
			String name = pet.name().toLowerCase().replaceAll("_", " ");
			if (player.getItems().playerHasItem(pet.getinvalidPet())) {
				player.getItems().deleteItem(pet.getinvalidPet(), 1);
				player.getItems().addItem(pet.getValidPet(), 1);
				player.sendMessage("Probita exchanged your invalid " + name + " to the valid one.");
			} else {
				player.sendMessage("You do not seem to have an invalid pet to exchange.");
				return;
			}
		}
	}
}
