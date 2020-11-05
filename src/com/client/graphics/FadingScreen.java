package com.client.graphics;

import org.apache.commons.lang3.time.StopWatch;

import com.client.RSFont;

public abstract class FadingScreen {
	
	/**
	 * The state of the fade on the screen
	 */
	protected byte state;

	/**
	 * How many seconds the fade exists for
	 */
	protected byte seconds;

	/**
	 * The string of text that will be displayed on the screen
	 */
	protected final String text;

	/**
	 * Controls how long it's been since the screen started its last fade
	 */
	protected final StopWatch watch;
	
	/**
	 * The position on the screen we're drawing, along with the dimensions 
	 */
	protected final int x, y;
	
	/**
	 * The text when wrapped if the text length exceeds that of 
	 */
	protected final String[] wrapped;
	
	/**
	 * The font used to draw text on the screen
	 */
	protected final RSFont font;
	
	public FadingScreen(RSFont font, String text, byte state, byte seconds, int x, int y, int maximumWidth) {
		this.font = font;
		this.text = text;
		this.state = state;
		this.seconds = seconds;
		this.x = x;
		this.y = y;
		this.watch = new StopWatch();
		this.watch.start();
		this.wrapped = font.wrap(text, maximumWidth);
	}

	/**
	 * Draws the animation on the screen. If the state of the screen is
	 * currently 0 the animation will not be drawn.
	 */
	public abstract void draw();
	
	/**
	 * Modifies the current state of the fading screen to zero which 
	 * should be defined as preventing drawing operations in the overriden
	 * draw function of each sub class.
	 */
	public void stop() {
		state = 0;
	}
	
}