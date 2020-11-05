package ethos.model.content.barrows.brothers;

import java.util.ArrayList;

import ethos.Server;
import ethos.model.content.barrows.BrotherEvent;
import ethos.model.content.barrows.RewardItem;
import ethos.model.npcs.NPC;
import ethos.model.players.Boundary;
import ethos.model.players.Coordinate;
import ethos.model.players.Player;
import ethos.model.players.combat.CombatType;

public abstract class Brother {

	public static final int AHRIM = 1672;
	public static final int DHAROK = 1673;
	public static final int GUTHAN = 1674;
	public static final int KARIL = 1675;
	public static final int TORAG = 1676;
	public static final int VERAC = 1677;

	protected Player player;

	private boolean active;
	private boolean defeated;
	private boolean finalBrother;

	private NPC npc;

	public Brother(Player player) {
		this.player = player;
	}

	public void handleSpawn() {
		if (finalBrother) {
			player.getDH().sendDialogues(2900, 2026);
		} else {
			spawnBrother();
		}
	}

	public void spawnBrother() {
		if (defeated) {
			if (finalBrother) {
				player.sendMessage("Something went wrong with the final Barrows brother. Please report this issue on the forums.");
				return;
			} else {
				player.sendMessage("You have already searched this sarcophagus.");
				return;
			}
		}
		if (active) {
			player.sendMessage("You are already fighting a brother.");
			return;
		}
		active = true;
		if (finalBrother) {
			npc = Server.npcHandler.spawnNpc(player, getId(), 3551, 9694, 0, 0, getHP(), getMaxHit(), getAttack(), getDefense(), true, true);
		} else {
			npc = Server.npcHandler.spawnNpc(player, getId(), getSpawn().getX(), getSpawn().getY(), player.getHeight(), 0, getHP(), getMaxHit(), getAttack(), getDefense(), true,
					true);
			Server.getEventHandler().submit(new BrotherEvent(player, 1));
		}
		if (npc != null) {
			npc.forceChat("You dare disturb my rest!");
		}
	}

	public void handleDeath() {
		active = false;
		defeated = true;
	}

	public boolean digDown() {
		if (Boundary.isIn(player, getMoundBoundary())) {
			player.getPA().movePlayer(getStairsLocation());
			return true;
		}
		return false;
	}

	public void moveUpStairs() {
		player.getPA().movePlayer(Boundary.centre(getMoundBoundary()));
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isDefeated() {
		return defeated;
	}

	public void setDefeated(boolean defeated) {
		this.defeated = defeated;
	}

	public boolean isFinal() {
		return finalBrother;
	}

	public NPC getNPC() {
		return npc;
	}

	public void setFinalBrother(boolean finalBrother) {
		this.finalBrother = finalBrother;
	}

	public double getEffectiveness(CombatType type) {
		switch (type) {
		case MELEE:
			return getMeleeEffectiveness();
		case RANGE:
			return getRangeEffectiveness();
		case MAGE:
			return getMagicEffectiveness();
		default:
			return 1;
		}
	}

	public abstract int getId();

	public abstract Boundary getMoundBoundary();

	public abstract int getStairsId();

	public abstract int getFrameId();

	public abstract Coordinate getStairsLocation();

	public abstract int getCoffinId();

	public abstract Coordinate getSpawn();

	public abstract String getName();

	public abstract ArrayList<RewardItem> getRewards();

	public abstract int getHP();

	public abstract int getMaxHit();

	public abstract int getAttack();

	public abstract int getDefense();

	public abstract double getMeleeEffectiveness();

	public abstract double getRangeEffectiveness();

	public abstract double getMagicEffectiveness();

}
