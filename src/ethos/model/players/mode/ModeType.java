package ethos.model.players.mode;

import org.apache.commons.lang3.text.WordUtils;

public enum ModeType {

	REGULAR, IRON_MAN, ULTIMATE_IRON_MAN, OSRS;

	@Override
	public String toString() {
		return WordUtils.capitalize(name().toLowerCase());
	}
}
