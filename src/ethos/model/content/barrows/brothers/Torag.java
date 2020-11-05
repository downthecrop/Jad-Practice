package ethos.model.content.barrows.brothers;

import java.util.ArrayList;

import ethos.model.content.barrows.RewardItem;
import ethos.model.content.barrows.RewardLevel;
import ethos.model.players.Boundary;
import ethos.model.players.Coordinate;
import ethos.model.players.Player;

public class Torag extends Brother {

	public Torag(Player player) {
		super(player);
	}

	@Override
	public int getId() {
		return TORAG;
	}

	@Override
	public Boundary getMoundBoundary() {
		return new Boundary(3550, 3278, 3557, 3287);
	}

	@Override
	public int getStairsId() {
		return 20671;
	}

	@Override
	public int getFrameId() {
		return 27506;
	}

	@Override
	public Coordinate getStairsLocation() {
		return new Coordinate(3568, 9683, 3);
	}

	@Override
	public int getCoffinId() {
		return 20721;
	}

	@Override
	public String getName() {
		return "Torag";
	}

	@Override
	public ArrayList<RewardItem> getRewards() {
		ArrayList<RewardItem> rewards = new ArrayList<>();
		rewards.add(new RewardItem(4745, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4747, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4749, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4751, 1, 1, RewardLevel.RARE));
		return rewards;
	}

	@Override
	public int getHP() {
		return 100;
	}

	@Override
	public int getMaxHit() {
		return 23;
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
		return new Coordinate(3568, 9686, 3);
	}
}
