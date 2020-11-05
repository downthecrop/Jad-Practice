package ethos.model.content.titles;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents a single slot on the interface. Please note that although the slots are ordered in numberical order and that the button id's do have a pattern. However, in case of
 * change I have provided this enum.
 * 
 * @author Jason MacKeigan
 * @date Jan 22, 2015, 11:00:55 PM
 */
public enum TitleMenuSlot {
	SLOT_1(1, 53551, 209046),
	SLOT_2(2, 53553, 209048),
	SLOT_3(3, 53555, 209050),
	SLOT_4(4, 53557, 209052),
	SLOT_5(5, 53559, 209054),
	SLOT_6(6, 53561, 209056),
	SLOT_7(7, 53563, 209058),
	SLOT_8(8, 53565, 209060),
	SLOT_9(9, 53567, 209062),
	SLOT_10(10, 53569, 209064),
	SLOT_11(11, 53571, 209066),
	SLOT_12(12, 53573, 209068),
	SLOT_13(13, 53575, 209070),
	SLOT_14(14, 53577, 209072),
	SLOT_15(15, 53579, 209074),
	SLOT_16(16, 53581, 209076),
	SLOT_17(17, 53583, 209078),
	SLOT_18(18, 53585, 209080),
	SLOT_19(19, 53587, 209082),
	SLOT_20(20, 53589, 209084),
	SLOT_21(21, 53591, 209086),
	SLOT_22(22, 53593, 209088),
	SLOT_23(23, 53595, 209090),
	SLOT_24(24, 53597, 209092),
	SLOT_25(25, 53599, 209094),
	SLOT_26(26, 53601, 209096),
	SLOT_27(27, 53603, 209098),
	SLOT_28(28, 53605, 209100),
	SLOT_29(29, 53607, 209102),
	SLOT_30(30, 53609, 209104),
	SLOT_31(31, 53611, 209106),
	SLOT_32(32, 53613, 209108),
	SLOT_33(33, 53615, 209110),
	SLOT_34(34, 53617, 209112);

	/**
	 * The index on the menu this slot resides
	 */
	private final int index;

	/**
	 * The identification value of the component the string is displayed on
	 */
	private final int stringId;

	/**
	 * The button id that when clicked triggers an action
	 */
	private final int buttonId;

	/**
	 * Creates a new slot with an index and button id
	 * 
	 * @param index the index on the menu
	 * @param buttonId the button for triggering an action
	 */
	private TitleMenuSlot(int index, int stringId, int buttonId) {
		this.index = index;
		this.stringId = stringId;
		this.buttonId = buttonId;
	}

	/**
	 * Retrieves the index for this slot on the menu
	 * 
	 * @return the index on the menu
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * The identification value of the component the string is displayed on
	 * 
	 * @return the identification value
	 */
	public int getStringId() {
		return stringId;
	}

	/**
	 * The button id on the menu for this slot
	 * 
	 * @return the button id
	 */
	public int getButtonId() {
		return buttonId;
	}

	/**
	 * The slot with the equivellent button id
	 * 
	 * @param buttonId the button id
	 * @return A {@link TitleMenuSlot} object if the button matches any of the button values for any of the elements.
	 */
	public static TitleMenuSlot get(int buttonId) {
		return SLOTS.stream().filter(s -> s.buttonId == buttonId).findFirst().orElse(null);
	}

	/**
	 * A set of all elements in the {@linkplain TitleMenuSlot} enum.
	 */
	private static final Set<TitleMenuSlot> SLOTS = Collections.unmodifiableSet(EnumSet.allOf(TitleMenuSlot.class));

}
