package ethos.model.content.barrows.brothers;

import java.util.ArrayList;

import ethos.model.content.barrows.RewardItem;
import ethos.model.content.barrows.RewardLevel;
import ethos.model.players.Boundary;
import ethos.model.players.Coordinate;
import ethos.model.players.Player;

public class Karil extends Brother {

	public Karil(Player player) {
		super(player);
	}

	@Override
	public int getId() {
		return KARIL;
	}

	@Override
	public Boundary getMoundBoundary() {
		return new Boundary(3562, 3273, 3569, 3279);
	}

	@Override
	public int getStairsId() {
		return 20670;
	}

	@Override
	public int getFrameId() {
		return 27505;
	}

	@Override
	public Coordinate getStairsLocation() {
		return new Coordinate(3546, 9684, 3);
	}

	@Override
	public int getCoffinId() {
		return 20771;
	}

	@Override
	public String getName() {
		return "Karil";
	}

	@Override
	public ArrayList<RewardItem> getRewards() {
		ArrayList<RewardItem> rewards = new ArrayList<>();
		rewards.add(new RewardItem(4732, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4734, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4736, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4738, 1, 1, RewardLevel.RARE));
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
		return 225;
	}

	@Override
	public int getDefense() {
		return 160;
	}

	@Override
	public double getMeleeEffectiveness() {
		return 1.2;
	}

	@Override
	public double getRangeEffectiveness() {
		return 0.9;
	}

	@Override
	public double getMagicEffectiveness() {
		return 1.0;
	}

	@Override
	public Coordinate getSpawn() {
		return new Coordinate(3549, 9683, 3);
	}
}
