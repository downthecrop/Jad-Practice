package com.client;

import java.util.Arrays;

public class ItemBonusDefinition {

	private short id;
	private short[] bonuses;

	public short[] getBonuses() {
		return bonuses;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "ItemBonusDefinition [id=" + id + ", bonuses=" + Arrays.toString(bonuses) + "]";
	}
}