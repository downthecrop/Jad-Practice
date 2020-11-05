package ethos.model.npcs;

import java.awt.Point;

import ethos.Server;
import ethos.model.entity.Entity;
import ethos.model.entity.HealthStatus;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.Position;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Hitmark;
import ethos.util.Location3D;
import ethos.util.Misc;
import ethos.util.Stream;

public class NPC extends Entity {
	// private Hitmark hitmark = null;
	// private Hitmark secondHitmark = null;
	public int npcType;
	public int summonedBy;
	public int absX, absY;
	public int prevX, prevY;
	public int heightLevel;
	public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction, walkingType;
	public int spawnX, spawnY;
	public int viewX, viewY;
	public int hp;
	public int lastX, lastY;
	public boolean summoner = false;
	public long singleCombatDelay = 0;
	public boolean teleporting = false;

	public long lastRandomlySelectedPlayer = System.currentTimeMillis();

	private boolean transformUpdateRequired;
	int transformId;
	public Location3D targetedLocation;

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	public long lastSpecialAttack;

	public boolean spawnedMinions;

	public CombatType attackType;

	public int projectileId, endGfx, spawnedBy, hitDelayTimer, hitDiff, animNumber, actionTimer, enemyX, enemyY;
	public boolean applyDead, isDead, needRespawn, respawns;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex, underAttackBy;
	public long lastDamageTaken;
	public boolean randomWalk;
	public boolean dirUpdateRequired;
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean updateRequired;
	public boolean forcedChatRequired;
	public boolean faceToUpdateRequired;
	public int firstAttacker;
	public String forcedText;
	public int stage;
	public int totalAttacks;
	private boolean facePlayer = true;
	private int projectileDelay = 0;

	private NPCDefinitions definition;

	private long lastRandomWalk;
	private long lastRandomWalkHome;

	private long randomWalkDelay;
	private long randomStopDelay;

	public NPC(int _npcId, int _npcType, NPCDefinitions definition) {
		super(_npcId, definition.getNpcName());
		this.definition = definition;
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
	}

	public Position getPosition() {
		return new Position(absX, absY, heightLevel);
	}

	public int followerMax() {
		if (npcType == 9)
			return 2;
		else
			return 1;
	}

	public int followerRange() {
		if (npcType == 9)
			return 2;
		else
			return 1;
	}

	public boolean AttackNPC() {// NPC VS NPC
		if (NPCHandler.npcs[index] == null)
			return false;
		int EnemyX = NPCHandler.npcs[index].absX;
		int EnemyY = NPCHandler.npcs[index].absY;
		int EnemyHP = NPCHandler.npcs[index].hp;

		int hitDiff = 0;
		turnNpc(EnemyX, EnemyY);
		hitDiff = Misc.random(followerMax());
		int hitTimer = 4000;
		int Player = 0;
		Player plr = (Player) PlayerHandler.players[Player];

		if (plr.goodDistance(EnemyX, EnemyY, absX, absY, followerRange()) == true) {
			if (System.currentTimeMillis() - lastHit > nextHit) {
				if (NPCHandler.npcs[index].isDead == true || NPCHandler.npcs[index].hp <= 0 || EnemyHP <= 0) {
					ResetAttackNPC();
				} else {
					if ((EnemyHP - hitDiff) < 0) {
						hitDiff = EnemyHP;
					}
					if (npcType == 9) {
						animNumber = 386;
						hitTimer = 2000;
					} else {
						hitTimer = 3500;
					}
					nextHit = hitTimer;
					lastHit = System.currentTimeMillis();
					animUpdateRequired = true;
					updateRequired = true;
					NPCHandler.npcs[index].hitDiff = hitDiff;
					NPCHandler.npcs[index].hp -= hitDiff;
					// Server.npcHandler.npcs[index].attackNpc = npcId;
					NPCHandler.npcs[index].updateRequired = true;
					NPCHandler.npcs[index].hitUpdateRequired = true;
					actionTimer = 7;
				}
				return true;
			}
		}
		return false;
	}

	public boolean ResetAttackNPC() {
		// isUnderAttackNpc = false;
		// isAttackingNPC = false;
		// attacknpc = -1;
		randomWalk = true;
		updateRequired = true;
		return true;
	}

	public long lastHit;
	public int nextHit;

	public void followNPC() {
		// int follow = followPlayer;
		int enemyX = NPCHandler.npcs[index].absX;
		int enemyY = NPCHandler.npcs[index].absY;
		turnNpc(enemyX, enemyY);
		randomWalk = false;
		if (enemyY == absY && enemyX == absX) {
			moveX = Server.npcHandler.GetMove(absX, enemyX);
			moveY = Server.npcHandler.GetMove(absY, enemyY + 1);
		}
		if (enemyY < absY) {
			moveX = Server.npcHandler.GetMove(absX, enemyX);
			moveY = Server.npcHandler.GetMove(absY, enemyY + 1);
		} else if (enemyY > absY) {
			moveX = Server.npcHandler.GetMove(absX, enemyX);
			moveY = Server.npcHandler.GetMove(absY, enemyY - 1);
		} else if (enemyX < absX) {
			moveX = Server.npcHandler.GetMove(absX, enemyX + 1);
			moveY = Server.npcHandler.GetMove(absY, enemyY);
		} else if (enemyX > absX) {
			moveX = Server.npcHandler.GetMove(absX, enemyX - 1);
			moveY = Server.npcHandler.GetMove(absY, enemyY);
		}
		getNextNPCMovement();
		updateRequired = true;
	}

	/**
	 * Teleport
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void teleport(int x, int y, int z) {
		teleporting = true;
		absX = x;
		absY = y;
		heightLevel = z;
	}

	public boolean insideOf(int x, int y) {
		for (Point p : getActorTiles()) {
			if (p.x == x && p.y == y) {
				return true;
			}
		}

		return false;
	}

	public Point[] getActorTiles() {
		Point[] tiles = new Point[getSize() == 1 ? 1 : (int) Math.pow(getSize(), 2)];
		int index = 0;

		for (int i = 1; i < getSize() + 1; i++) {
			for (int k = 0; k < NPCClipping.SIZES[i].length; k++) {
				int x3 = getX() + NPCClipping.SIZES[i][k][0];
				int y3 = getY() + NPCClipping.SIZES[i][k][1];
				tiles[index] = new Point(x3, y3);
				index++;
			}
		}
		return tiles;
	}

	/**
	 * Gets the exact distance from this actor.
	 * 
	 * @param x
	 * @param y
	 * @return the exact distance between ponits.
	 */
	public double getDistance(int x, int y) {
		double low = 9999;

		if (insideOf(x, y))
			return 0;

		for (Point p : getBorder()) {
			double dist = Misc.distance(x, y, p.x, p.y);
			if (dist < low) {
				low = dist;
			}
		}

		return low;
	}

	/**
	 * Gets the border around the edges of the actor.
	 * 
	 * @return the border around the edges of the actor, depending on the actor's
	 *         size.
	 */
	public Point[] getBorder() {
		int x = getX();
		int y = getY();
		int size = getSize();
		if (size <= 1) {
			return new Point[] { new Point(absX, absY) };
		}

		Point[] border = new Point[(size) + (size - 1) + (size - 1) + (size - 2)];
		int j = 0;

		border[0] = new Point(x, y);

		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < (i < 3 ? (i == 0 || i == 2 ? size : size) - 1
					: (i == 0 || i == 2 ? size : size) - 2); k++) {
				if (i == 0)
					x++;
				else if (i == 1)
					y++;
				else if (i == 2)
					x--;
				else if (i == 3) {
					y--;
				}
				border[(++j)] = new Point(x, y);
			}
		}

		return border;
	}

	/**
	 * Determines if the npc can face another player
	 * 
	 * @return {@code true} if the npc can face players
	 */
	public boolean canFacePlayer() {
		return facePlayer;
	}

	/**
	 * Makes the npcs either able or unable to face other players
	 * 
	 * @param facePlayer
	 *            {@code true} if the npc can face players
	 */
	public void setFacePlayer(boolean facePlayer) {
		this.facePlayer = facePlayer;
	}

	/**
	 * Sends the request to a client that the npc should be transformed into
	 * another.
	 * 
	 * @param Id
	 *            the id of the new npc
	 */
	public void requestTransform(int id) {
		transformId = id;
		npcType = id;
		transformUpdateRequired = true;
		updateRequired = true;
	}

	public void appendTransformUpdate(Stream str) {
		str.writeWordBigEndianA(transformId);
	}

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {

			if (updateRequired) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	 * Text update
	 **/

	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}

	public int mask80var1 = 0;
	public int mask80var2 = 0;
	protected boolean mask80update = false;

	public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
		str.writeDWord(mask80var2);
	}

	public void gfx100(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 6553600;
		mask80update = true;
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 65536;
		mask80update = true;
		updateRequired = true;
	}

	public int getOffset() {
		return (int) Math.floor(NPCHandler.getNpcDef()[this.npcType].size / 2);
	}

	public static int getSpeedForDistance(int distance) {
		switch (distance) {
		case 1:
			return 90;
		case 2:
			return 95;
		case 3:
			return 100;
		case 4:
			return 105;
		case 5:
			return 115;
		case 6:
			return 125;
		case 7:
			return 135;
		case 8:
			return 150;
		default:
			return 150;
		}
	}

	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}

	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0;

	private void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(FocusPointX);
		str.writeWordBigEndian(FocusPointY);
	}

	public void turnNpc(int i, int j) {
		FocusPointX = 2 * i + 1;
		FocusPointY = 2 * j + 1;
		updateRequired = true;

	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	public void facePlayer(int player) {
		if (!facePlayer) {
			if (face == -1) {
				return;
			}
			face = -1;
		} else {
			face = player + 32768;
		}
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public void appendNPCUpdateBlock(Stream str) {
		if (!updateRequired)
			return;
		int updateMask = 0;
		if (animUpdateRequired)
			updateMask |= 0x10;
		if (hitUpdateRequired2)
			updateMask |= 8;
		if (mask80update)
			updateMask |= 0x80;
		if (dirUpdateRequired)
			updateMask |= 0x20;
		if (forcedChatRequired)
			updateMask |= 1;
		if (hitUpdateRequired)
			updateMask |= 0x40;
		if (transformUpdateRequired)
			updateMask |= 2;
		if (FocusPointX != -1)
			updateMask |= 4;

		str.writeByte(updateMask);

		if (animUpdateRequired)
			appendAnimUpdate(str);
		if (hitUpdateRequired2)
			appendHitUpdate2(str);
		if (mask80update)
			appendMask80Update(str);
		if (dirUpdateRequired)
			appendFaceEntity(str);
		if (forcedChatRequired) {
			str.writeString(forcedText);
		}
		if (hitUpdateRequired)
			appendHitUpdate(str);
		if (transformUpdateRequired)
			appendTransformUpdate(str);
		if (FocusPointX != -1)
			appendSetFocusDestination(str);
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		transformUpdateRequired = false;
		mask80update = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
		teleporting = false;
	}

	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (dir == -1)
			return -1;
		if (teleporting)
			return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void startAnimation(int animationId) {
		animNumber = animationId;
		animUpdateRequired = true;
		updateRequired = true;
	}

	public void getNextNPCMovement() {
		direction = -1;
		if (freezeTimer == 0) {
			direction = getNextWalkingDirection();
		}
	}

	@Override
	public void appendHitUpdate(Stream str) {
		if (getHealth().getAmount() <= 0) {
			isDead = true;
		}
		if (hitmark1 != null && !hitmark1.isMiss() && hitDiff == 0) {
			hitDiff = 0;
			hitmark1 = Hitmark.MISS;
		}
		str.writeByteC(hitDiff);
		if (hitmark1 != null) {
			str.writeByteS(hitmark1.getId());
		} else {
			str.writeByteS(0);
		}
		str.writeWord(health.getAmount());
		str.writeWord(health.getMaximum());
	}

	public int hitDiff2 = 0;
	public boolean hitUpdateRequired2 = false;
	public int walkHomeAttempts;
	public int npcId;

	@Override
	public void appendHitUpdate2(Stream str) {
		if (getHealth().getAmount() <= 0) {
			isDead = true;
		}
		if (hitmark2 != null && !hitmark2.isMiss() && hitDiff2 == 0) {
			hitDiff2 = 0;
			hitmark2 = Hitmark.MISS;
		}
		str.writeByteA(hitDiff2);
		if (hitmark2 != null) {
			str.writeByteC(hitmark2.getId());
		} else {
			str.writeByteC(0);
		}
		str.writeWord(getHealth().getAmount());
		str.writeWord(getHealth().getMaximum());
	}

	public int appendDamage(Player player, int damage, Hitmark h) {
		appendDamage(damage, h);
		addDamageTaken(player, damage);
		return damage;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	/*
	 * public NPCDefinition getDefinition() { return definition; }
	 */

	public boolean inMulti() {
		if (Boundary.isIn(this, Zulrah.BOUNDARY) || Boundary.isIn(this, Boundary.CORPOREAL_BEAST_LAIR)
				|| Boundary.isIn(this, Boundary.KRAKEN_CAVE) || Boundary.isIn(this, Boundary.SCORPIA_LAIR)
				|| Boundary.isIn(this, Boundary.CERBERUS_BOSSROOMS) || Boundary.isIn(this, Boundary.INFERNO)
				|| Boundary.isIn(this, Boundary.SKOTIZO_BOSSROOM) || Boundary.isIn(this, Boundary.LIZARDMAN_CANYON)
				|| Boundary.isIn(this, Boundary.BANDIT_CAMP_BOUNDARY) || Boundary.isIn(this, Boundary.ABYSSAL_SIRE)
				|| Boundary.isIn(this, Boundary.COMBAT_DUMMY) || Boundary.isIn(this, Boundary.TEKTON)
				|| Boundary.isIn(this, Boundary.SKELETAL_MYSTICS) || Boundary.isIn(this, Boundary.RAID_MAIN)
				|| Boundary.isIn(this, Boundary.ICE_DEMON) || Boundary.isIn(this, Boundary.CATACOMBS)) {
			return true;
		}

		if (Boundary.isIn(this, Boundary.KALPHITE_QUEEN) && heightLevel == 0) {
			return true;
		}

		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 2824 && absX <= 2944 && absY >= 5258 && absY <= 5369)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
				|| (absX >= 2962 && absX <= 3006 && absY >= 3621 && absY <= 3659)
				|| (absX >= 3155 && absX <= 3214 && absY >= 3755 && absY <= 3803)
				|| (absX >= 1889 && absX <= 1912 && absY >= 4396 && absY <= 4413)
				|| (absX >= 3717 && absX <= 3772 && absY >= 5765 && absY <= 5820)) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	public boolean inRaids() {
		return (absX > 3210 && absX < 3368 && absY > 5137 && absY < 5759);
	}

	public int getSize() {
		if (definition == null)
			return 1;
		return definition.getSize();
	}

	/**
	 * An object containing specific information about the NPC such as the combat
	 * level, default maximum health, the name, etcetera.
	 * 
	 * @return the {@link NPCDefintions} object associated with this NPC
	 */
	public NPCDefinitions getDefinition() {
		return definition;
	}

	public int getProjectileDelay() {
		return projectileDelay;
	}

	public void setProjectileDelay(int delay) {
		projectileDelay = delay;
	}

	@Override
	public void appendDamage(int damage, Hitmark hitmark) {
		if (damage < 0) {
			damage = 0;
			hitmark = Hitmark.MISS;
		}
		if (getHealth().getAmount() - damage < 0) {
			damage = getHealth().getAmount();
		}
		getHealth().reduce(damage);
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
			hitmark1 = hitmark;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
			hitmark2 = hitmark;
		}
		updateRequired = true;
	}

	@Override
	public boolean susceptibleTo(HealthStatus status) {
		switch (npcType) {
		case 2042:
		case 2043:
		case 2044:
		case 6720:
		case 7413:
		case 7544:
		case 5129:
		case 4922:
		case 7604:
		case 7605:
		case 7606:
			return false;
		}
		return true;
	}

	public long getLastRandomWalk() {
		return lastRandomWalk;
	}

	public long getLastRandomWalkhome() {
		return lastRandomWalkHome;
	}

	public void setLastRandomWalkHome(long lastRandomWalkHome) {
		this.lastRandomWalkHome = lastRandomWalkHome;
	}

	public long getRandomStopDelay() {
		return randomStopDelay;
	}

	public void setRandomStopDelay(long randomStopDelay) {
		this.randomStopDelay = randomStopDelay;
	}

	public void setLastRandomWalk(long lastRandomWalk) {
		this.lastRandomWalk = lastRandomWalk;
	}

	public long getRandomWalkDelay() {
		return randomWalkDelay;
	}

	public void setRandomWalkDelay(long randomWalkDelay) {
		this.randomWalkDelay = randomWalkDelay;
	}
}
