package com.client.features;

import java.text.NumberFormat;

public class ExperienceDrop {
	
	/**
	 * The amount of experience gained in the drop
	 */
	private final long amount;
	
	/**
	 * The skills the experience is gained in
	 */
	private final int[] skills;
	
	/**
	 * The position of the experience drop on y-axis of the game screen
	 */
	private int yPosition = START_Y;
	
	/**
	 * The point on the screen that signifies the final position
	 * the marker must be at for the drop to be finished
	 */
	public static final int END_Y = 41;
	
	/**
	 * The point on the y axis that we start end
	 */
	public static final int START_Y = 150;
	
	/**
	 * The distance in-between the chat box and the start of the experience drop.
	 */
	public static final int EXCESS_DISTANCE = 314 - START_Y;
	
	/**
	 * The amount at which we decrement by each pulse
	 */
	private static final int INTERVAL = 1;
	
	/**
	 * Created when experience is sent from the server to the client to show
	 * that a player has gained some amount of experience in a skill.
	 * 
	 * @param skills	the skills that we gained experience in
	 * @param amount	the amount of experience gained
	 */
	public ExperienceDrop(long amount, int... skills) {
		this.amount = amount;
		this.skills = skills;
	}
	
	/**
	 * Pulses the experience drop by decreasing the position on the y axis by the
	 * determined interval. This occurs every few graphics cycles.
	 */
	public void pulse() {
		yPosition -= INTERVAL;
		if (yPosition < END_Y) {
			yPosition = -1;
		}
	}
	
	/**
	 * Increases the position of this drop on the y axis by some amount.
	 * @param offset	the amount to offset the position
	 */
	public void increasePosition(int offset) {
		yPosition += offset;
	}
	
	public byte getTransparency() {
		int halfway = START_Y / 2;
		if (yPosition > halfway) {
			return 100;
		}
		byte percentile = (byte) ((1D / 55D) * 100);
		if (percentile < 0) {
			return 0;
		}
		return (byte) Math.abs((percentile * yPosition));
	}
	
	/**
	 * The amount of experience gained
	 * @return	the amount
	 */
	public final long getAmount() {
		return amount;
	}
	
	/**
	 * The skills the experience is gained in
	 * @return	the skills
	 */
	public final int[] getSkills() {
		return skills;
	}
	
	/**
	 * The position of the this drop on the y axis
	 * @return	the position 
	 */
	public final int getYPosition() {
		return yPosition;
	}
	
	@Override
	public String toString() {
		return NumberFormat.getInstance().format(amount);
	}

}
