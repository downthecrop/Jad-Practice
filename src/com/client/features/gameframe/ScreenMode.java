package com.client.features.gameframe;


import com.client.Client;

public enum ScreenMode {

	FIXED(1, 765, 503, false, false),
	RESIZABLE(2, 902, 702, true, false),
	FULLSCREEN(3, (int) Client.MAXIMUM_SCREEN_BOUNDS.getWidth(), (int) Client.MAXIMUM_SCREEN_BOUNDS.getHeight(), false, true);

	private final int numericalValue;

	private final int width;

	private final int height;

	private final boolean resizable;

	private final boolean undecorated;

	public int getNumericalValue() {
		return numericalValue;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isResizable() {
		return resizable;
	}

	public boolean isUndecorated() {
		return undecorated;
	}

	ScreenMode(int numericalValue, int width, int height, boolean resizable, boolean undecorated) {
		this.numericalValue = numericalValue;
		this.width = width;
		this.height = height;
		this.resizable = resizable;
		this.undecorated = undecorated;
	}

}
