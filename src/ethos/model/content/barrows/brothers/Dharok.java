package ethos.model.content.barrows.brothers;

import java.util.ArrayList;

import ethos.model.content.barrows.RewardItem;
import ethos.model.content.barrows.RewardLevel;
import ethos.model.players.Boundary;
import ethos.model.players.Coordinate;
import ethos.model.players.Player;

public class Dharok extends Brother {

	public Dharok(Player player) {
		super(player);
	}

	@Override
	public int getId() {
		return DHAROK;
	}

	@Override
	public Boundary getMoundBoundary() {
		return new Boundary(3570, 3293, 3579, 3302);
	}

	@Override
	public int getStairsId() {
		return 20668;
	}

	@Override
	public int getFrameId() {
		return 27503;
	}

	@Override
	public Coordinate getStairsLocation() {
		return new Coordinate(3556, 9718, 3);
	}

	@Override
	public int getCoffinId() {
		return 20720;
	}

	@Override
	public String getName() {
		return "Dharok";
	}

	@Override
	public ArrayList<RewardItem> getRewards() {
		ArrayList<RewardItem> rewards = new ArrayList<>();
		rewards.add(new RewardItem(4716, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4718, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4720, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4722, 1, 1, RewardLevel.RARE));
		return rewards;
	}

	@Override
	public int getHP() {
		return 100;
	}

	@Override
	public int getMaxHit() {
		return 20;
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
		return new Coordinate(3555, 9716, 3);
	}
}
