package ethos.model.players.combat.effects;

import java.util.Objects;

import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.npcs.NPC;
import ethos.model.players.Player;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.Hitmark;
import ethos.util.Misc;

/**
 * @author Jason MacKeigan
 * @date Dec 11, 2014, 4:44:33 AM
 */
public class DragonfireShieldEffect implements DamageEffect {

	static final long ATTACK_DELAY_REQUIRED = 120_000;

	private int cycle;

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		CycleEventHandler.getSingleton().addEvent(attacker, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer eventContainer) {
				if (Objects.isNull(attacker) || Objects.isNull(defender)) {
					eventContainer.stop();
					return;
				}
				if (defender.getHealth().getAmount() <= 0 || defender.isDead) {
					eventContainer.stop();
					return;
				}
				cycle++;
				if (cycle == 1) {
					attacker.startAnimation(6696);
					attacker.gfx0(1165);
					attacker.setDragonfireShieldCharge(attacker.getDragonfireShieldCharge() - 1);
				} else if (cycle == 4) {
					attacker.getPA().createPlayersProjectile2(attacker.getX(), attacker.getY(), (attacker.getY() - defender.getY()) * -1, (attacker.getX() - defender.getX()) * -1,
							50, 50, 1166, 30, 30, -attacker.oldPlayerIndex - 1, 30, 5);
				} else if (cycle >= 5) {
					if (defender.playerEquipment[defender.playerShield] == 11283 || defender.playerEquipment[defender.playerShield] == 11284) {
						defender.appendDamage((damage.getAmount() / 2) + (Misc.random(damage.getAmount() / 2)), damage.getAmount() > 0 ? Hitmark.HIT : Hitmark.MISS);
						eventContainer.stop();
						return;
					}
					defender.appendDamage(damage.getAmount(), damage.getAmount() > 0 ? Hitmark.HIT : Hitmark.MISS);
					eventContainer.stop();
				}
			}
		}, 1);
	}

	@Override
	public boolean isExecutable(Player operator) {
		if (operator.getDragonfireShieldCharge() <= 0) {
			operator.sendMessage("Your dragonfire shield is out of charges, you need to refill it.");
			return false;
		}
		if (System.currentTimeMillis() - operator.getLastDragonfireShieldAttack() < ATTACK_DELAY_REQUIRED) {
			operator.sendMessage("You must let your dragonfire shield cool down before using it again.");
			return false;
		}
		return true;
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		CycleEventHandler.getSingleton().addEvent(attacker, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer eventContainer) {
				if (Objects.isNull(attacker) || Objects.isNull(defender)) {
					eventContainer.stop();
					return;
				}
				if (defender.getHealth().getAmount() <= 0 || defender.isDead || attacker.teleTimer > 0) {
					eventContainer.stop();
					return;
				}
				if (Misc.distanceToPoint(attacker.getX(), attacker.getY(), defender.getX(), defender.getY()) > 12) {
					eventContainer.stop();
					return;
				}
				cycle++;
				if (cycle == 1) {
					attacker.startAnimation(6696);
					attacker.gfx0(1165);
				} else if (cycle == 4) {
					attacker.getPA().createPlayersProjectile2(attacker.getX(), attacker.getY(), (attacker.getY() - defender.getY()) * -1, (attacker.getX() - defender.getX()) * -1,
							50, 50, 1166, 30, 30, -attacker.oldNpcIndex - 1, 30, 5);
				} else if (cycle >= 5) {
					defender.underAttack = true;
					defender.hitDiff = damage.getAmount();
					defender.getHealth().reduce(damage.getAmount());
					defender.hitUpdateRequired = true;
					defender.updateRequired = true;
					eventContainer.stop();
				}
			}
		}, 1);
	}

}
