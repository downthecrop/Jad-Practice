package ethos.model.content.barrows.brothers;

import java.util.ArrayList;

import ethos.model.content.barrows.RewardItem;
import ethos.model.content.barrows.RewardLevel;
import ethos.model.players.Boundary;
import ethos.model.players.Coordinate;
import ethos.model.players.Player;

public class Guthan extends Brother {

	public Guthan(Player player) {
		super(player);
	}

	@Override
	public int getId() {
		return GUTHAN;
	}

	@Override
	public Boundary getMoundBoundary() {
		return new Boundary(3571, 3278, 3582, 3285);
	}

	@Override
	public int getStairsId() {
		return 20669;
	}

	@Override
	public int getFrameId() {
		return 27504;
	}

	@Override
	public Coordinate getStairsLocation() {
		return new Coordinate(3534, 9704, 3);
	}

	@Override
	public int getCoffinId() {
		return 20722;
	}

	@Override
	public String getName() {
		return "Guthan";
	}

	@Override
	public ArrayList<RewardItem> getRewards() {
		ArrayList<RewardItem> rewards = new ArrayList<>();
		rewards.add(new RewardItem(4724, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4726, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4728, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4730, 1, 1, RewardLevel.RARE));
		return rewards;
	}

	@Override
	public int getHP() {
		return 100;
	}

	@Override
	public int getMaxHit() {
		return 26;
	}

	@Override
	public int getAttack() {
		return 200;
	}

	@Override
	public int getDefense() {
		return 200;
	}

	@Override
	public double getMeleeEffectiveness() {
		return 0.8;
	}

	@Override
	public double getRangeEffectiveness() {
		return 0.6;
	}

	@Override
	public double getMagicEffectiveness() {
		return 1.25;
	}

	@Override
	public Coordinate getSpawn() {
		return new Coordinate(3539, 9705, 3);
	}
}
