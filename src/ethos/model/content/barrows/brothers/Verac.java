package ethos.model.content.barrows.brothers;

import java.util.ArrayList;

import ethos.model.content.barrows.RewardItem;
import ethos.model.content.barrows.RewardLevel;
import ethos.model.players.Boundary;
import ethos.model.players.Coordinate;
import ethos.model.players.Player;

public class Verac extends Brother {

	public Verac(Player player) {
		super(player);
	}

	@Override
	public int getId() {
		return VERAC;
	}

	@Override
	public Boundary getMoundBoundary() {
		return new Boundary(3553, 3291, 3561, 3301);
	}

	@Override
	public int getStairsId() {
		return 20672;
	}

	@Override
	public int getFrameId() {
		return 27507;
	}

	@Override
	public Coordinate getStairsLocation() {
		return new Coordinate(3578, 9706, 3);
	}

	@Override
	public int getCoffinId() {
		return 20772;
	}

	@Override
	public String getName() {
		return "Verac";
	}

	@Override
	public ArrayList<RewardItem> getRewards() {
		ArrayList<RewardItem> rewards =new ArrayList<>();
		rewards.add(new RewardItem(4753, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4755, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4757, 1, 1, RewardLevel.RARE));
		rewards.add(new RewardItem(4759, 1, 1, RewardLevel.RARE));
		return rewards;
	}

	@Override
	public int getHP() {
		return 100;
	}

	@Override
	public int getMaxHit() {
		return 25;
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
		return new Coordinate(3575, 9706, 3);
	}
}
