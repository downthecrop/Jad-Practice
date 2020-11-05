package ethos.model.npcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import com.client.Configuration;

import ethos.Config;
import ethos.Server;
import ethos.clip.PathChecker;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.DailyTaskKills;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.AchievementDiaryKills;
import ethos.model.content.barrows.Barrows;
import ethos.model.content.barrows.brothers.Brother;
import ethos.model.content.godwars.God;
import ethos.model.content.godwars.GodwarsNPCs;
import ethos.model.entity.Entity;
import ethos.model.entity.HealthStatus;
import ethos.model.minigames.Wave;
import ethos.model.minigames.inferno.InfernoWave;
import ethos.model.minigames.lighthouse.DagannothMother;
import ethos.model.minigames.lighthouse.DisposeType;
import ethos.model.minigames.rfd.DisposeTypes;
import ethos.model.minigames.rfd.RecipeForDisaster;
import ethos.model.minigames.warriors_guild.AnimatedArmour;
import ethos.model.npcs.animations.AttackAnimation;
import ethos.model.npcs.animations.DeathAnimation;
import ethos.model.npcs.bosses.CorporealBeast;
import ethos.model.npcs.bosses.GreatOlm;
import ethos.model.npcs.bosses.Scorpia;
import ethos.model.npcs.bosses.cerberus.Cerberus;
import ethos.model.npcs.bosses.raids.SkeletalMystic;
import ethos.model.npcs.bosses.raids.Tekton;
import ethos.model.npcs.bosses.skotizo.Skotizo;
import ethos.model.npcs.bosses.vorkath.Vorkath;
import ethos.model.npcs.bosses.wildypursuit.Glod;
import ethos.model.npcs.bosses.wildypursuit.IceQueen;
import ethos.model.npcs.bosses.zulrah.Zulrah;
import ethos.model.npcs.pets.PetHandler;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.combat.CombatType;
import ethos.model.players.combat.Damage;
import ethos.model.players.combat.DamageEffect;
import ethos.model.players.combat.Hitmark;
import ethos.model.players.combat.Special;
import ethos.model.players.combat.Specials;
import ethos.model.players.combat.effects.SerpentineHelmEffect;
import ethos.model.players.combat.monsterhunt.MonsterHunt;
import ethos.model.players.packets.commands.owner.Npc;
import ethos.model.players.skills.hunter.impling.PuroPuro;
import ethos.util.Location3D;
import ethos.util.Misc;
import ethos.world.objects.GlobalObject;

public class NPCHandler {

	public void checkMa(Player c,int i) {
				if (c.roundNpc == 2 && !c.spawned) {
					spawnNpc(c, 1606, 3106, 3934, 0, 1, 30, 24, 70, 60, true, true);
					c.roundNpc = 3;
					c.spawned = true;
				} else if (c.roundNpc == 3 && !c.spawned) {
					spawnNpc(c, 1607, 3106, 3934, 0, 1, 60, 24, 70, 60, true, true);
					c.roundNpc = 4;
					c.spawned = true;
				} else if (c.roundNpc == 4 && !c.spawned) {
					spawnNpc(c, 1608, 3106, 3934, 0, 1, 80, 15, 70, 60, true, true);
					c.roundNpc = 5;
					c.spawned = true;
				} else if (c.roundNpc == 5 && !c.spawned) {
					spawnNpc(c, 1609, 3106, 3934, 0, 1, 140, 19, 70, 60, true, true);
					c.roundNpc = 6;
					c.spawned = true;
				} else if (c.roundNpc == 6 && !c.spawned) {
					c.getPA().movePlayer(2541, 4716, 0);
					c.getDH().sendNpcChat3("Congratulations you have proved your self worthy!","Head back to the arena now to earn points!","Goodluck wizard!",1603,"Kolodion");
					c.roundNpc = 0;
					c.maRound = 2;
				}
			}
	
	public void jadFight(Player player) {
		jadPlayer = player;
	}
	
	public static Player jadPlayer;

	public static int maxNPCs = 30000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];
	private static NPCDef[] npcDef = new NPCDef[maxListedNPCs];

	public static boolean projectileClipping = true;

	/**
	 * Tekton variables
	 */
	public static String tektonAttack = "MELEE";
	public static Boolean tektonWalking = false;
	NPC TEKTON = NPCHandler.getNpc(7544);

	/**
	 * Ice demon variables
	 */
	NPC ICE_DEMON = NPCHandler.getNpc(7584);

	/**
	 * Skeletal mystics
	 */
	NPC SKELE_MYSTIC_ONE = NPCHandler.getNpc(7604);
	NPC SKELE_MYSTIC_TWO = NPCHandler.getNpc(7605);
	NPC SKELE_MYSTIC_THREE = NPCHandler.getNpc(7606);

	/**
	 * Glod variables
	 */
	public static String glodAttack = "MELEE";
	public static Boolean glodWalking = false;
	NPC GLOD = NPCHandler.getNpc(5219);

	/**
	 * Queen variables
	 */
	public static String queenAttack = "MAGIC";
	public static Boolean queenWalking = false;
	NPC QUEEN = NPCHandler.getNpc(4922);

	public NPCHandler() {
		for (int i = 0; i < maxNPCs; i++) {
			npcs[i] = null;
			NPCDefinitions.getDefinitions()[i] = null;
		}
		loadNPCList("./Data/CFG/npc_config.cfg");
		loadAutoSpawn("./Data/CFG/spawn_config.cfg");
		loadNPCSizes("./Data/cfg/npc_sizes.txt");
		startGame();
	}

	public boolean ringOfLife(Player c) {
		boolean defenceCape = SkillcapePerks.DEFENCE.isWearing(c);
		boolean maxCape = SkillcapePerks.isWearingMaxCape(c);
		if (c.getItems().isWearingItem(2570) || defenceCape || (maxCape && c.getRingOfLifeEffect())) {
			if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
				c.sendMessage("The ring of life effect does not work as you are teleblocked.");
				return false;
			}
			if (defenceCape || maxCape) {
				c.sendMessage("Your cape activated the ring of life effect and saved you!");
			} else {
				c.getItems().deleteEquipment(2570, c.playerRing);
				c.sendMessage("Your ring of life saved you!");
			}
			c.getPA().spellTeleport(3087, 3499, 0, false);
			return true;
		}
		return false;
	}

	public void spawnNpc3(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon, boolean summonFollow) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc3");
			return;
		}
		NPCDefinitions definition = NPCDefinitions.get(npcType);
		NPC newNPC = new NPC(slot, npcType, definition);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.getHealth().setMaximum(HP);
		newNPC.getHealth().reset();
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getIndex();
		newNPC.underAttack = true;
		newNPC.facePlayer(c.getIndex());
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (summonFollow) {
			newNPC.summoner = true;
			newNPC.summonedBy = c.getIndex();
			c.hasFollower = true;
		}
		if (attackPlayer) {
			newNPC.underAttack = true;
			newNPC.killerId = c.getIndex();
		}
		npcs[slot] = newNPC;
	}

	public void stepAway(int i) {
		int[][] points = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		for (int[] k : points) {
			int dir = NPCClipping.getDirection(k[0], k[1]);
			if (NPCDumbPathFinder.canMoveTo(npcs[i], dir)) {
				NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].getX() + NPCClipping.DIR[dir][0],
						npcs[i].getY() + NPCClipping.DIR[dir][1]);
				break;
			}
		}
	}

	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					int nX = NPCHandler.npcs[i].getX() + offset(i);
					int nY = NPCHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nX - pX) * -1;
					int offY = (nY - pY) * -1;
					int centerX = nX + npcs[i].getSize() / 2;
					int centerY = nY + npcs[i].getSize() / 2;
					c.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, getProjectileSpeed(i),
							npcs[i].projectileId, getProjectileStartHeight(npcs[i].npcType, npcs[i].projectileId),
							getProjectileEndHeight(npcs[i].npcType, npcs[i].projectileId), -c.getIndex() - 1, 65);
					if (npcs[i].npcType == 7554) {
						c.getPA().sendPlayerObjectAnimation(c, 3220, 5738, 7371, 10, 3, c.getHeight());
					}
				}
			}
		}
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 2205:
		case 963:
		case 965:
		case 3129:
		case 2215:
		case 3162:
		case 2208:
		case 239:
		case 6611:
		case 6612:
		case 494:
		case 319:
		case 7554:
		case 320:
		case 5535:
		case 2551:
		case 6609:
		case 2552:
		case 2553:
		case 2559:
		case 2560:
		case 2561:
		case 2563:
		case 2564:
		case 2565:
		case 2892:
		case 2894:
		case 1046:
		case 6615:
		case 6616:
		case 7604:
		case 7605:
		case 7606:
		case 7544:
		case 5129:
		case 4922:
			return true;

		}

		return false;
	}

	public static boolean isSpawnedBy(Player player, NPC npc) {
		if (player != null && npc != null)
			if (npc.spawnedBy == player.getIndex() || npc.killerId == player.getIndex())
				return true;
		return false;
	}

	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<>();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
					if (!Boundary.isIn(PlayerHandler.players[j], Boundary.CORPOREAL_BEAST_LAIR)) {
						npcs[i].killerId = 0;
						continue;
					}
				}
				/**
				 * Skips attacking a player if mode set to invisible
				 */
				if (PlayerHandler.players[j].isInvisible()) {
					continue;
				}
				if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
					if (!Boundary.isIn(PlayerHandler.players[j], Boundary.GODWARS_BOSSROOMS)) {
						npcs[i].killerId = 0;
						continue;
					}
				}
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);
				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		else
			return 0;
	}

	public int culinomancer = 0;

	/**
	 * Updated to support the new drop table checker
	 * 
	 * @param i
	 * @param searching
	 * @return
	 */
	public boolean isAggressive(int i, boolean searching) {
		if (!searching) {
			if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)
					|| Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
				return true;
			}
		}

		if (searching) {
			switch (i) {
			case 5916:
			case 690:
			case 963:
			case 965:
			case 955:
			case 957:
			case 959:
			case 5867:
			case 5868:
			case 5869:
			case 2042:
			case 239:
			case 7413:
			case 1739:
			case 1740:
			case 1741:
			case 1742:
			case 2044:
			case 2043:
			case 465:
				case 7706:
			case Zulrah.SNAKELING:
			case 5054:
			case 6611:
			case 6612:
			case 6610:
			case 494:
			case 5535:
			case 2550:
			case 2551:
			case 50:
			case 28:
			case 2552:
			case 6609:
			case 2553:
			case 2558:
			case 2559:
			case 2560:
			case 2561:
			case 2562:
			case 2563:
			case 2564:
			case 2565:
			case 2892:
			case 2894:
			case 2265:
			case 2266:
			case 2267:
			case 2035:
			case 5779:
			case 291:
			case 435:
			case 135:
			case 7276:
			case 5944: // Rock lobster

				// Godwars
			case 3138:
			case 2205:
			case 2206:
			case 2207:
			case 2208:
			case 2209:
			case 2211:
			case 2212:
			case 2215:
			case 2216:
			case 2217:
			case 2218:
			case 2233:
			case 2234:
			case 2235:
			case 2237:
			case 2242:
			case 2243:
			case 2244:
			case 2245:
			case 3129:
			case 3130:
			case 3131:
			case 3132:
			case 3133:
			case 3134:
			case 3135:
			case 3137:
			case 3139:
			case 3140:
			case 3141:
			case 3159:
			case 3160:
			case 3161:
			case 3162:
			case 3163:
			case 3164:
			case 3165:
			case 3166:
			case 3167:
			case 3168:
			case 3174:

				// Barrows tunnel monsters
			case 1678:
			case 1679:
			case 1683:
			case 1684:
			case 1685:

			case Skotizo.SKOTIZO_ID:
			case Skotizo.REANIMATED_DEMON:
			case Skotizo.DARK_ANKOU:
				// GWD
			case 6230:
			case 6231:
			case 6229:
			case 6232:
			case 6240:
			case 6241:
			case 6242:
			case 6233:
			case 6234:
			case 6243:
			case 6244:
			case 6245:
			case 6246:
			case 6238:
			case 6239:
			case 6625:
			case 122:// Npcs That Give BandosKC
			case 6278:
			case 6277:
			case 6276:
			case 6283:
			case 6282:
			case 6281:
			case 6280:
			case 6279:
			case 6271:
			case 6272:
			case 6273:
			case 6274:
			case 6269:
			case 6270:
			case 6268:
			case 6221:
			case 6219:
			case 6220:
			case 6217:
			case 6216:
			case 6215:
			case 6214:
			case 6213:
			case 6212:
			case 6211:
			case 6218:
			case 6275:
			case 6257:// Npcs That Give SaraKC
			case 6255:
			case 6256:
			case 6259:
			case 6254:
			case 1689:
			case 1694:
			case 1699:
			case 1704:
			case 1709:
			case 1714:
			case 1724:
			case 1734:
			case 6914: // Lizardman, Lizardman brute
			case 6915:
			case 6916:
			case 6917:
			case 6918:
			case 6919:
			case 6766:
			case 7573:
			case 7617: // Tekton magers
			case 7544: // Tekton
			case 7604: // Skeletal mystic
			case 7605: // Skeletal mystic
			case 7606: // Skeletal mystic
			case 7585: //
				case 7554:
			case 7563: // muttadiles
			case 5129:
			case 4922:

				return true;
			case 1524:
			case 6600:
			case 6601:
			case 7553:
			case 7555:
			case 6602:
			case 1049:
			case 6617:
			case 6620:
				return false;
			}
		} else {
			switch (npcs[i].npcType) {
			case 5916:
			case 690:
			case 963:
			case 965:
			case 955:
			case 957:
			case 959:
			case 5867:
			case 5868:
			case 5869:
			case 2042:
			case 239:
			case 7413:
			case 1739:
			case 1740:
			case 1741:
			case 1742:
			case 2044:
			case 2043:
			case 465:
			case Zulrah.SNAKELING:
			case 5054:
			case 6611:
			case 6612:
			case 6610:
			case 494:
			case 5535:
			case 2550:
			case 2551:
			case 50:
			case 28:
			case 2552:
			case 6609:
			case 2553:
			case 2558:
			case 2559:
			case 2560:
			case 2561:
			case 2562:
			case 2563:
			case 2564:
			case 2565:
			case 2892:
			case 2894:
			case 2265:
			case 2266:
			case 2267:
			case 2035:
			case 5779:
			case 291:
			case 435:
			case 135:
			case 484:
			case 7276:
			case 5944: // Rock lobster

				// Godwars
			case 3138:
			case 2205:
			case 2206:
			case 2207:
			case 2208:
			case 2209:
			case 2211:
			case 2212:
			case 2215:
			case 2216:
			case 2217:
			case 2218:
			case 2233:
			case 2234:
			case 2235:
			case 2237:
			case 2242:
			case 2243:
			case 2244:
			case 2245:
			case 3129:
			case 3130:
			case 3131:
			case 3132:
			case 3133:
			case 3134:
			case 3135:
			case 3137:
			case 3139:
			case 3140:
			case 3141:
			case 3159:
			case 3160:
			case 3161:
			case 3162:
			case 3163:
			case 3164:
			case 3165:
			case 3166:
			case 3167:
			case 3168:
			case 3174:

			case Skotizo.SKOTIZO_ID:
			case Skotizo.REANIMATED_DEMON:
			case Skotizo.DARK_ANKOU:

				// Barrows tunnel monsters
			case 1678:
			case 1679:
			case 1683:
			case 1684:
			case 1685:
				// GWD
			case 6230:
			case 6231:
			case 6229:
			case 6232:
			case 6240:
			case 6241:
			case 6242:
			case 6233:
			case 6234:
			case 6243:
			case 6244:
			case 6245:
			case 6246:
			case 6238:
			case 6239:
			case 6625:
			case 122:// Npcs That Give BandosKC
			case 6278:
			case 6277:
			case 6276:
			case 6283:
			case 6282:
			case 6281:
			case 6280:
			case 6279:
			case 6271:
			case 6272:
			case 6273:
			case 6274:
			case 6269:
			case 6270:
			case 6268:
			case 6221:
			case 6219:
			case 6220:
			case 6217:
			case 6216:
			case 6215:
			case 6214:
			case 6213:
			case 6212:
			case 6211:
			case 6218:
			case 6275:
			case 6257:// Npcs That Give SaraKC
			case 6255:
			case 6256:
			case 6259:
			case 6254:
			case 1689:
			case 1694:
			case 1699:
			case 1704:
			case 1709:
			case 1714:
			case 1724:
			case 1734:
			case 6914: // Lizardman, Lizardman brute
			case 6915:
			case 6916:
			case 6917:
			case 6918:
			case 6919:
			case 6766:
			case 7573:
			case 7617: // Tekton magers
			case 7544: // Tekton
			case 7604: // Skeletal mystic
			case 7605: // Skeletal mystic
			case 7606: // Skeletal mystic
			case 5129:
			case 4922:
			case 7388: // Start of superior
			case 7389:
			case 7390:
			case 7391:
			case 7392:
			case 7393:
			case 7394:
			case 7395:
			case 7396:
			case 7397:
			case 7398:
			case 7399:
			case 7400:
			case 7401:
			case 7402:
			case 7403:
			case 7404:
			case 7405:
			case 7406:
			case 7407:
			case 7409:
			case 7410:
			case 7411: // end of superior
				return true;
			case 1524:
			case 6600:
			case 6601:
			case 6602:
			case 1049:
			case 6617:
			case 6620:
				return false;

			case 8028:
				return true;
			}
			if (npcs[i].inWild() && npcs[i].getHealth().getMaximum() > 0)
				return true;
			if (npcs[i].inRaids() && npcs[i].getHealth().getMaximum() > 0)
				return true;
			return isFightCaveNpc(i);
		}
		return false;
	}

	public static boolean isDagannothMother(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case 983:
		case 984:
		case 985:
		case 986:
		case 987:
		case 988:
			return true;
		}
		return false;
	}

	public static boolean isFightCaveNpc(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case Wave.TZ_KEK_SPAWN:
		case Wave.TZ_KIH:
		case Wave.TZ_KEK:
		case Wave.TOK_XIL:
		case Wave.YT_MEJKOT:
		case Wave.KET_ZEK:
			// case Wave.TZTOK_JAD:
			return true;
		}
		return false;
	}

	public static boolean isInfernoNpc(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {

		case InfernoWave.JAL_NIB:
		case InfernoWave.JAL_MEJRAH:
		case InfernoWave.JAL_AK:
		case InfernoWave.JAL_AKREK_MEJ:
		case InfernoWave.JAL_AKREK_XIL:
		case InfernoWave.JAL_AKREK_KET:
		case InfernoWave.JAL_IMKOT:
		case InfernoWave.JAL_XIL:
		case InfernoWave.JAL_ZEK:
		case InfernoWave.JALTOK_JAD:
		case InfernoWave.YT_HURKOT:
		case InfernoWave.TZKAL_ZUK:
		case InfernoWave.ANCESTRAL_GLYPH:
		case InfernoWave.JAL_MEJJAK:

			return true;
		}
		return false;
	}

	public static boolean isSkotizoNpc(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case Skotizo.SKOTIZO_ID:
		case Skotizo.AWAKENED_ALTAR_NORTH:
		case Skotizo.AWAKENED_ALTAR_SOUTH:
		case Skotizo.AWAKENED_ALTAR_WEST:
		case Skotizo.AWAKENED_ALTAR_EAST:
		case Skotizo.REANIMATED_DEMON:
		case Skotizo.DARK_ANKOU:
			return true;
		}
		return false;
	}

	/**
	 * Summon npc, barrows, etc
	 **/
	public NPC spawnNpc(final Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc");
			return null;
		}
		NPCDefinitions definition = NPCDefinitions.get(npcType);
		final NPC newNPC = new NPC(slot, npcType, definition);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.getHealth().setMaximum(HP);
		newNPC.getHealth().reset();
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getIndex();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			newNPC.killerId = c.getIndex();
			c.underAttackBy = slot;
			c.underAttackBy2 = slot;
		}
		npcs[slot] = newNPC;
		if (newNPC.npcType == 1605) {
			newNPC.forceChat("You must prove yourself... now!");
			newNPC.gfx100(86);
		}
		if (newNPC.npcType == 1606) {
			newNPC.forceChat("This is only the beginning, you can't beat me!");
			newNPC.gfx100(86);
		}
		if (newNPC.npcType == 1607) {
			newNPC.forceChat("Foolish mortal, I am unstoppable.");
		}
		if (newNPC.npcType == 1608) {
			newNPC.forceChat("Now you feel it... The dark energy.");
		}
		if (newNPC.npcType == 1609) {
			newNPC.forceChat("Aaaaaaaarrgghhhh! The power!");
		}
		return newNPC;
	}

	/**
	 * Attack animations
	 * 
	 * @param i
	 *            the npc to perform the animation.
	 * @return the animation to be performed.
	 */
	public static int getAttackEmote(int i) {
		return AttackAnimation.handleEmote(i);
	}
	
	/**
	 * Death animations
	 * 
	 * @param i
	 *            the npc to perform the animation.
	 * @return the animation to be performed.
	 */
	public int getDeadEmote(int i) {
		return DeathAnimation.handleEmote(i);
	}

	/**
	 * Death delay
	 * 
	 * @param i
	 *            the npc whom were setting the delay to
	 * @return the delay were setting
	 */
	public int getDeathDelay(int i) {
		switch (npcs[i].npcType) {
		case 8028:
			return 8;
		case 5548:
		case 5549:
		case 5550:
		case 5551:
		case 5552:
		case 1505:
		case 2910:
		case 2911:
		case 2912:
		case 484:
		case 7276:
		case 3138:
		case 1635:
		case 1636:
		case 1637:
		case 1638:
		case 1639:
		case 1640:
		case 1641:
		case 1642:
		case 1643:
		case 1654:
		case 7302:
			return 1;
		case 2209:
		case 2211:
		case 2212:
		case 2233:
		case 2234:
		case 435:
		case 3137:
		case 3139:
		case 3140:
		case 3159:
		case 3160:
		case 3161:
		case 2241:
			return 2;
		case 3134:
		case 3141:
			return 3;
		case 3166:
		case 3167:
		case 3168:
		case 3174:
			return 5;
		case 3129:
		case 3130:
		case 3131:
		case 3132:
			return 6;
		case 2237:
		case 2242:
		case 2243:
		case 2244:
		case 3135:
			return 7;
		default:
			return 4;
		}
	}

	/**
	 * Attack delay
	 * 
	 * @param i
	 *            the npc whom were setting the delay to
	 * @return the delay were setting
	 */
	public int getNpcDelay(int i) {
		switch (npcs[i].npcType) {
		case 499:
			return 4;
		case 498:
			return 7;
		case 6611:
		case 6612:
			return npcs[i].attackType == CombatType.MAGE ? 6 : 5;
		case 319:
			return npcs[i].attackType == CombatType.MAGE ? 7 : 6;
		case 7554:
			return npcs[i].attackType == CombatType.MAGE ? 4 : 6;
		case 2025:
		case 2028:
		case 963:
		case 965:
			return 7;
		case 3127:
			case 7700:
			return 8;
		case 2205:
			return 4;
		case Brother.AHRIM:
			return 6;
		case Brother.DHAROK:
			return 7;
		case Brother.GUTHAN:
			return 5;
		case Brother.KARIL:
			return 4;
		case Brother.TORAG:
			return 5;
		case Brother.VERAC:
			return 5;
		case 3167:
		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2215:
			return 6;
		// saradomin gw boss
		case 2562:
			return 2;
		case 3162:
			return 7;
		default:
			return 5;
		}
	}

	/**
	 * Projectile start height
	 * 
	 * @param npcType
	 *            the npc to perform the projectile
	 * @param projectileId
	 *            the projectile to be performed
	 * @return
	 */
	private int getProjectileStartHeight(int npcType, int projectileId) {
		switch (npcType) {
		case 2044:
			return 60;
		case 3162:
			return 0;
		case 3127:
		case 3163:
			case 7700:
		case 3164:
		case 3167:
		case 3174:
			return 110;
		case 6610:
			switch (projectileId) {
			case 165:
				return 20;
			}
			break;
		}
		return 43;
	}

	/**
	 * Projectile end height
	 * 
	 * @param npcType
	 *            the npc to perform the projectile
	 * @param projectileId
	 *            the projectile to be performed
	 * @return
	 */
	private int getProjectileEndHeight(int npcType, int projectileId) {
		switch (npcType) {
		case 1605:
			return 0;
		case 3162:
			return 15;
		case 6610:
			switch (projectileId) {
			case 165:
				return 30;
			}
			break;
		}
		return 31;
	}

	/**
	 * Hit delay
	 * 
	 * @param i
	 *            the npc whom were setting the delay to
	 * @return the delay were setting
	 */
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 7706:
		return 7;
		case 1605:
		case 1606:
		case 1607:
		case 1608:
		case 1609:
		case 499:
			return 4;
		case 498:
			return 4;
		case 6611:
		case 6612:
			return npcs[i].attackType == CombatType.MAGE ? 3 : 2;
		case 1672:
		case 1675:
		case 1046:
		case 1049:
		case 6610:
		case 2265:
		case 2266:
		case 2054:
		case 2892:
		case 2894:
		case 3125:
		case 3121:
		case 2167:
		case 2558:
		case 2559:
		case 2560:
		case 2209:
		case 2211:
		case 2218:
		case 2242:
		case 2244:
		case 3160:
		case 3163:
		case 3167:
		case 3174:
		case 2028:
			return 3;
		case 2212:
		case 2217:
		case 3161:
		case 3162:
		case 3164:
		case 3168:
		case 6914: // Lizardman, Lizardman brute
		case 6915:
		case 6916:
		case 6917:
		case 6918:
		case 6919:
		case 2025:
			return 4;
		case 3127:
			case 7700:
			if (npcs[i].attackType == CombatType.RANGE || npcs[i].attackType == CombatType.MAGE) {
				return 5;
			} else {
				return 2;
			}

		default:
			return 2;
		}
	}

	/**
	 * Respawn time
	 * 
	 * @param i
	 *            the npc whom were setting the time to
	 * @return the time were setting
	 */
	public int getRespawnTime(int i) {
		switch (npcs[i].npcType) {
		case 6600:
		case 6601:
		case 6602:
		case 320:
		case 1049:
		case 6617:
		case 3118:
		case 3120:
		case 6768:
		case 5862:
		case 5054:
		case 2402:
		case 2401:
		case 2400:
		case 2399:
		case 5916:
		case 7604:
		case 7605:
		case 7606:
		case 7585:
		case 5129:
		case 4922:
		case 7563:
		case 7573:
		case 7544:
		case 7566:
		case 7559:
			case 7553:
			case 7554:
			case 7555:
		case 7560:
		case 7527:
		case 7528:
		case 7529:
			return -1;

		case 963:
		case 965:
			return 10;

		case 6618:
		case 6619:
		case 319:
		case 5890:
			return 30;

		case 1046:
		case 465:
			return 60;

		case 6609:
		case 2265:
		case 2266:
		case 2267:
			return 70;

		case 6611:
		case 6612:
		case 492:
			return 90;

		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2205:
		case 2206:
		case 2207:
		case 2208:
		case 2215:
		case 2216:
		case 2217:
		case 2218:
		case 3129:
		case 3130:
		case 3131:
		case 3132:
		case 3162:
		case 3163:
		case 3164:
		case 3165:
		case 1641:
		case 1642:
			return 100;

		case 1643:
			return 180;

		case 1654:
			return 250;

		case 3777:
		case 3778:
		case 3779:
		case 3780:
		case 7302:
			return 500;
		default:
			return 25;
		}
	}

	/**
	 * Spawn a new npc on the world
	 * 
	 * @param npcType
	 *            the npcType were spawning
	 * @param x
	 *            the x coordinate were spawning on
	 * @param y
	 *            the y coordinate were spawning on
	 * @param heightLevel
	 *            the heightLevel were spawning on
	 * @param WalkingType
	 *            the WalkingType were setting
	 * @param HP
	 *            the HP were setting
	 * @param maxHit
	 *            the maxHit were setting
	 * @param attack
	 *            the attack level were setting
	 * @param defence
	 *            the defence level were setting
	 */
	public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return;

		NPCDefinitions definition = NPCDefinitions.get(npcType);
		NPC newNPC = new NPC(slot, npcType, definition);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.getHealth().setMaximum(HP);
		newNPC.getHealth().reset();
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.resetDamageTaken();
		npcs[slot] = newNPC;
	}

	public boolean olmDead;
	public static boolean rightClawDead;
	public static boolean leftClawDead;

	/**
	 * Constructs a new npc list
	 * 
	 * @param npcType
	 *            npcType to be set
	 * @param npcName
	 *            npcName to be gathered
	 * @param combat
	 *            combat level to be set
	 * @param HP
	 *            HP level to be set
	 */
	public void newNPCList(int npcType, String npcName, int combat, int HP) {
		NPCDefinitions newNPCList = new NPCDefinitions(npcType);
		newNPCList.setNpcName(npcName.replaceAll("_", " ").toLowerCase());
		newNPCList.setNpcCombat(combat);
		newNPCList.setNpcHealth(HP);
		NPCDefinitions.getDefinitions()[npcType] = newNPCList;
	}

	public int olmStage;

	/**
	 * Handles processes for NPCHandler every 600ms
	 */
	public void process() {
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null)
				continue;
			npcs[i].clearUpdateFlags();
		}
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] != null) {
				NPC npc = npcs[i];
				int type = npcs[i].npcType;
				Player slaveOwner = (PlayerHandler.players[npcs[i].summonedBy]);
				if (npcs[i] != null && slaveOwner == null && npcs[i].summoner) {
					npcs[i].absX = 0;
					npcs[i].absY = 0;
				}
				if (npcs[i] != null && slaveOwner != null && slaveOwner.hasFollower
						&& (!slaveOwner.goodDistance(npcs[i].getX(), npcs[i].getY(), slaveOwner.absX, slaveOwner.absY,
								15) || slaveOwner.heightLevel != npcs[i].heightLevel)
						&& npcs[i].summoner) {
					npcs[i].absX = slaveOwner.absX;
					npcs[i].absY = slaveOwner.absY;
					npcs[i].heightLevel = slaveOwner.heightLevel;

				}
				if (npcs[i].actionTimer > 0) {
					npcs[i].actionTimer--;
				}

				if (npcs[i].freezeTimer > 0) {
					npcs[i].freezeTimer--;
				}
				if (npcs[i].hitDelayTimer > 0) {
					npcs[i].hitDelayTimer--;
				}
				if (npcs[i].hitDelayTimer == 1) {
					npcs[i].hitDelayTimer = 0;
					applyDamage(i);
				}
				if (npcs[i].attackTimer > 0) {
					npcs[i].attackTimer--;
				}
				if (npcs[i].npcType == 7553) {
					npc.walkingHome = true;
				}
				if (npcs[i].npcType == 7555) {
					npc.walkingHome = true;
				}
				if (npcs[i].npcType == 1143) {
					if (Misc.random(30) == 3) {
						npcs[i].updateRequired = true;
						npcs[i].forceChat("Sell your PvM items here for a limited time!");
					}
				}

				if (npcs[i].npcType == 306) {
					if (Misc.random(50) == 3) {
						npcs[i].forceChat("Speak to me if you wish to learn more about this land!");
					}
				}

				// /**
				// * Tekton walking
				// */
				// if (tektonWalking) {
				// if (npcs[i].npcType == 7544) {
				// if (npcs[i].getX() != 3308 && npcs[i].getX() != 5296) {
				// NPCDumbPathFinder.walkTowards(TEKTON, 3308, 5296);
				// } else {
				// tektonWalking = false;
				// }
				// }
				// }
				if (npcs[i].getHealth().getAmount() > 0 && !npcs[i].isDead) {
					if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
						if (npcs[i].getHealth().getAmount() < (npcs[i].getHealth().getMaximum() / 2)
								&& !npcs[i].spawnedMinions) {
							NPCHandler.spawnNpc(5054, npcs[i].getX() - 1, npcs[i].getY(), 0, 1, 175, 14, 100, 120);
							NPCHandler.spawnNpc(5054, npcs[i].getX() + 1, npcs[i].getY(), 0, 1, 175, 14, 100, 120);
							npcs[i].spawnedMinions = true;
						}
					}
				}
				if (npcs[i].npcType == 6600 && !npcs[i].isDead) {
					NPC runiteGolem = getNpc(6600);
					if (runiteGolem != null && !runiteGolem.isDead) {
						npcs[i].isDead = true;
						npcs[i].needRespawn = false;
						npcs[i].actionTimer = 0;
					}
				}
				if (npcs[i].spawnedBy > 0) { // delete summons npc
					Player spawnedBy = PlayerHandler.players[npcs[i].spawnedBy];
					if (spawnedBy == null || spawnedBy.heightLevel != npcs[i].heightLevel || spawnedBy.respawnTimer > 0
							|| !spawnedBy.goodDistance(npcs[i].getX(), npcs[i].getY(), spawnedBy.getX(),
									spawnedBy.getY(),
									NPCHandler.isFightCaveNpc(i) ? 60 : NPCHandler.isSkotizoNpc(i) ? 60 : 20)) {

						npcs[i] = null;
					}
				}
				if (npcs[i] == null)
					continue;
				if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
					npcs[i].lastX = npcs[i].getX();
					npcs[i].lastY = npcs[i].getY();
				}
				Player glyphSpawner = PlayerHandler.players[npcs[i].spawnedBy];
				if(type ==7707){
					glyphSpawner.getInferno().glyphX=npc.absX;
					glyphSpawner.getInferno().glyphY=npc.absY;
					//glyphSpawner.sendMessage("@red@"+glyphSpawner.getInferno().glyphX + " " +glyphSpawner.getInferno().glyphY);
				}
				if (type == 7707 && npc.absX == 2270 && npc.absY >= 5361 && glyphSpawner.getInferno().glyphCanMove) { // Move
																														// forward
					npc.moveX = GetMove(npc.absX, 2270);
					npc.moveY = GetMove(npc.absY, 5360);
					npc.updateRequired = true;
					npc.getNextNPCMovement();
					npc.walkingHome = false;
				} else if (type == 7707 && npc.absX == 2270 && npc.absY == 5360
						&& glyphSpawner.getInferno().glyphCanMove) { // From forward, start left
					glyphSpawner.getInferno().glyphCanMove = false;
					glyphSpawner.getInferno().glyphMoveLeft = true;
					npc.moveX = GetMove(npc.absX, 2257);
					npc.moveY = GetMove(npc.absY, 5360);
					npc.updateRequired = true;
					npc.getNextNPCMovement();
					npc.walkingHome = false;
				} else if (type == 7707 && npc.absY == 5360 && glyphSpawner.getInferno().glyphMoveLeft) { // Once all
																											// the way
																											// to the
																											// left,
																											// move all
																											// the way
																											// to the
																											// right
					if (npc.absX == 2257 && npc.absY == 5360) {
						glyphSpawner.getInferno().glyphMoveLeft = false;
						glyphSpawner.getInferno().glyphMoveRight = true;
					}
					npc.moveX = GetMove(npc.absX, 2257);
					npc.moveY = GetMove(npc.absY, 5360);
					npc.updateRequired = true;
					npc.getNextNPCMovement();
					npc.walkingHome = false;
				} else if (type == 7707 && npc.absY == 5360 && glyphSpawner.getInferno().glyphMoveRight) { // Once all
																											// the way
																											// to the
																											// right,
																											// move all
																											// the way
																											// to the
																											// left
					if (npc.absX == 2283 && npc.absY == 5360) {
						glyphSpawner.getInferno().glyphMoveLeft = true;
						glyphSpawner.getInferno().glyphMoveRight = false;
					}
					npc.moveX = GetMove(npc.absX, 2283);
					npc.moveY = GetMove(npc.absY, 5360);
					npc.updateRequired = true;
					npc.getNextNPCMovement();
					npc.walkingHome = false;
				}

				if (type == 6615) {
					if (npcs[i].walkingHome) {
						npcs[i].getHealth().setAmount(200);
					}
					Scorpia.spawnHealer();
				}

				if (type == 319) {
					if (npcs[i].walkingHome) {
						npcs[i].getHealth().setAmount(2000);
					}
					CorporealBeast.checkCore();
				}

				if (type == 8026 || type == 8027) {
					npcs[i].setFacePlayer(false);
				}
				if (type == 8028) {
					npcs[i].setFacePlayer(true);
				}
				if (type >= 2042 && type <= 2044 && npcs[i].getHealth().getAmount() > 0) {
					Player player = PlayerHandler.players[npcs[i].spawnedBy];
					if (player != null && player.getZulrahEvent().getNpc() != null
							&& npcs[i].equals(player.getZulrahEvent().getNpc())) {
						int stage = player.getZulrahEvent().getStage();
						if (type == 2042) {
							if (stage == 0 || stage == 1 || stage == 4 || stage == 9 && npcs[i].totalAttacks >= 20
									|| stage == 11 && npcs[i].totalAttacks >= 5) {
								continue;
							}
						}
						if (type == 2044) {
							if ((stage == 5 || stage == 8) && npcs[i].totalAttacks >= 5) {
								continue;
							}
						}
					}
				}
				/**
				 * Attacking player
				 **/
				if (isAggressive(i, false) && !npc.underAttack && npc.killerId <= 0 && !npc.isDead
						&& !switchesAttackers(i) && npc.inMulti() && !Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS)
						&& !Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
					Player closestPlayer = null;
					int closestDistance = Integer.MAX_VALUE;
					God god = GodwarsNPCs.NPCS.get(npc.npcType);

					for (Player player : PlayerHandler.players) {
						if (player == null) {
							continue;
						}
						if (player.isIdle)
							continue;

						if (god != null && player.inGodwars() && player.getEquippedGodItems() != null
								&& player.getEquippedGodItems().contains(god)) {
							continue;
						}
						/**
						 * Skips attacking a player if mode set to invisible
						 */
						if (player.isInvisible()) {
							continue;
						}

						int distance = Misc.distanceToPoint(npc.absX, npc.absY, player.absX, player.absY);
						if (distance < closestDistance && distance <= distanceRequired(i) + followDistance(i)) {
							closestDistance = distance;
							closestPlayer = player;
						}
					}
					if (closestPlayer != null) {
						npc.killerId = closestPlayer.getIndex();
						closestPlayer.underAttackBy = npc.getIndex();
						closestPlayer.underAttackBy2 = npc.getIndex();
					}
				} else if (isAggressive(i, false) && !npcs[i].underAttack && !npcs[i].isDead
						&& (switchesAttackers(i) || Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS))) {

					if (System.currentTimeMillis() - npcs[i].lastRandomlySelectedPlayer > 10000) {
						int player = getCloseRandomPlayer(i);

						if (player > 0) {
							npcs[i].killerId = player;
							PlayerHandler.players[player].underAttackBy = i;
							PlayerHandler.players[player].underAttackBy2 = i;
							npcs[i].lastRandomlySelectedPlayer = System.currentTimeMillis();
						}
					}
				}

				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000 && !npcs[i].underAttack) {
					npcs[i].underAttackBy = 0;
					npcs[i].underAttack = false;
					npcs[i].randomWalk = true;
				}
				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 10000) {
					npcs[i].underAttackBy = 0;
					npcs[i].underAttack = false;
					npcs[i].randomWalk = true;
				}
				if ((npcs[i].killerId > 0 || npcs[i].underAttack) && !npcs[i].walkingHome
						&& retaliates(npcs[i].npcType)) {
					if (!npcs[i].isDead) {
						int p = npcs[i].killerId;
						if (PlayerHandler.players[p] != null) {
							if (!npcs[i].summoner) {
								Player c = PlayerHandler.players[p];
								followPlayer(i, c.getIndex());
								if (npcs[i] == null)
									continue;
								if (npcs[i].attackTimer == 0) {
									attackPlayer(c, i);
								}
							} else {
								Player c = PlayerHandler.players[p];
								if (c.absX == npcs[i].absX && c.absY == npcs[i].absY) {
									stepAway(i);
									npcs[i].randomWalk = false;
									npcs[i].facePlayer(c.getIndex());
								} else {
									followPlayer(i, c.getIndex());
								}
							}
						} else {
							npcs[i].killerId = 0;
							npcs[i].underAttack = false;
							npcs[i].facePlayer(0);
						}
					}
				}

				/**
				 * 
				 * Random walking and walking home
				 **/
				if (npcs[i] == null)
					continue;
				if ((!npcs[i].underAttack) && !isFightCaveNpc(i) && npcs[i].randomWalk && !npcs[i].isDead) {
					npcs[i].facePlayer(0);
					npcs[i].killerId = 0;
					// handleClipping(i);
					if (npcs[i].spawnedBy == 0) {
						if ((npcs[i].absX > npcs[i].makeX + Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absX < npcs[i].makeX - Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY > npcs[i].makeY + Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY < npcs[i].makeY - Config.NPC_RANDOM_WALK_DISTANCE)
										&& npcs[i].npcType != 1635 && npcs[i].npcType != 1636 && npcs[i].npcType != 1637
										&& npcs[i].npcType != 1638 && npcs[i].npcType != 1639 && npcs[i].npcType != 1640
										&& npcs[i].npcType != 1641 && npcs[i].npcType != 1642 && npcs[i].npcType != 1643
										&& npcs[i].npcType != 1654 && npcs[i].npcType != 7302) {
							npcs[i].walkingHome = true;
						}
					}

					if (npcs[i].walkingType >= 0) {
						switch (npcs[i].walkingType) {
						case 5:
							npcs[i].turnNpc(npcs[i].absX - 1, npcs[i].absY);
							break;
						case 4:
							npcs[i].turnNpc(npcs[i].absX + 1, npcs[i].absY);
							break;
						case 3:
							npcs[i].turnNpc(npcs[i].absX, npcs[i].absY - 1);
							break;
						case 2:
							npcs[i].turnNpc(npcs[i].absX, npcs[i].absY + 1);
							break;
						}
					}

					if (npcs[i].walkingType == 1 && (!npcs[i].underAttack) && !npcs[i].walkingHome) {
						if (System.currentTimeMillis() - npcs[i].getLastRandomWalk() > npcs[i].getRandomWalkDelay()) {
							int direction = Misc.random3(8);
							int movingToX = npcs[i].getX() + NPCClipping.DIR[direction][0];
							int movingToY = npcs[i].getY() + NPCClipping.DIR[direction][1];
							if (npcs[i].npcType >= 1635 && npcs[i].npcType <= 1643 || npcs[i].npcType == 1654
									|| npcs[i].npcType == 7302) {
								NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].getX() - 1 + Misc.random(8),
										npcs[i].getY() - 1 + Misc.random(8));
							} else {
								if (Math.abs(npcs[i].makeX - movingToX) <= 1 && Math.abs(npcs[i].makeY - movingToY) <= 1
										&& NPCDumbPathFinder.canMoveTo(npcs[i], direction)) {
									NPCDumbPathFinder.walkTowards(npcs[i], movingToX, movingToY);
								}
							}
							npcs[i].setRandomWalkDelay(TimeUnit.SECONDS.toMillis(1 + Misc.random(2)));
							npcs[i].setLastRandomWalk(System.currentTimeMillis());
						}
					}
				}
				if (npcs[i].walkingHome) {
					if (!npcs[i].isDead) {
						NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].makeX, npcs[i].makeY);
						if (npcs[i].moveX == 0 && npcs[i].moveY == 0) {
							npcs[i].teleport(npcs[i].makeX, npcs[i].makeY, npcs[i].heightLevel);
						}
						if (npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
							npcs[i].walkingHome = false;
						}
					} else {
						npcs[i].walkingHome = false;
					}
				}
				/**
				 * Npc death
				 */

				if (npcs[i].isDead) {
					Player player = PlayerHandler.players[npcs[i].spawnedBy];

					if (npcs[i].actionTimer == 0 && !npcs[i].applyDead && !npcs[i].needRespawn) {
						if (npcs[i].npcType == 8028) {
							CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {
									Vorkath.drop(player);
									Vorkath.drop(player);
									int dropHead = Misc.random(50);
									//player.sendMessage(dropHead + " <- that");
									if (dropHead == 1 || player.getNpcDeathTracker().getTracker().get("vorkath") == 50) {
										Server.itemHandler.createGroundItem(player, 2425, Vorkath.lootCoordinates[0],
												Vorkath.lootCoordinates[1], player.heightLevel, 1, player.getIndex());
									}
									Server.npcHandler.spawnNpc(player, 8026, 2272, 4065, player.getHeight(), 0, 750,
											player.antifireDelay > 0 ? 0 : 61, 560, 114, true, false);
									container.stop();
								}

								@Override
								public void stop() {
								}
							}, 9);
						}
						if (npcs[i].npcType == 6618) {
							npcs[i].forceChat("Ow!");
						}
						if (npcs[i].npcType == 6611) {
							npcs[i].requestTransform(6612);
							npcs[i].getHealth().reset();
							npcs[i].isDead = false;
							npcs[i].spawnedMinions = false;
							npcs[i].forceChat("Do it again!!");
						} else {
							if (npcs[i].npcType == 6612) {
								npcs[i].npcType = 6611;
								npcs[i].spawnedMinions = false;
							}


							if (npcs[i].npcType == 1605) {
								CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer container) {
										spawnNpc(player, 1606, 3106, 3934, 0, 1, 30, 24, 70, 60, true, true);
										container.stop();
									}

									@Override
									public void stop() {
									}
								}, 5);
							}
							Player killer1 = PlayerHandler.players[npcs[i].spawnedBy];
							if (npcs[i].npcType == 1606) {
								killer1.roundNpc=3;
								killer1.spawned = false;
								checkMa(killer1,i);
							}
							if (npcs[i].npcType == 1607) {
								killer1.spawned = false;
								checkMa(killer1,i);
							}
							if (npcs[i].npcType == 1608) {
								killer1.spawned = false;
								checkMa(killer1,i);
							}

							if (npcs[i].npcType == 1609) {
								if (killer1 != null) {
									killer1.spawned = false;
									checkMa(killer1,i);
								}
							}
							/*
							 * if (npcs[i].npcType == 7554) { //TODO animate objec
							 * player.getRaids().finishRaids(); }
							 */
							npcs[i].updateRequired = true;
							npcs[i].facePlayer(0);
							Entity killer = npcs[i].calculateKiller();

							if (killer != null) {
								npcs[i].killedBy = killer.getIndex();
							}
							if (npcs[i].npcType == 963) {
								npcs[i].isDead = false;
								npcs[i].npcType = 965;
								npcs[i].requestTransform(965);
								npcs[i].getHealth().reset();
							} else {
								npcs[i].animNumber = getDeadEmote(i); // dead emote
								npcs[i].animUpdateRequired = true;
							}
							npcs[i].freezeTimer = 0;
							npcs[i].applyDead = true;

							if (npcs[i].npcType == 3118) {
								spawnNpc(3120, npcs[i].absX, npcs[i].absY, player.heightLevel, 10, 2, 15, 15, 0);
								spawnNpc(3120, npcs[i].absX, npcs[i].absY + 1, player.heightLevel, 10, 2, 15, 15, 0);
							}
							if (player != null) {
								this.tzhaarDeathHandler(player, i);
								this.infernoDeathHandler(player, i);
							}
							killedBarrow(i);
							npcs[i].actionTimer = getDeathDelay(i);
							resetPlayersInCombat(i);
						}
					} else if (npcs[i].actionTimer == 0 && npcs[i].applyDead && !npcs[i].needRespawn) {
						int killerIndex = npcs[i].killedBy;
						npcs[i].needRespawn = true;
						npcs[i].actionTimer = getRespawnTime(i); // respawn time
						dropItems(i);
						if (killerIndex < PlayerHandler.players.length - 1) {
							Player target = PlayerHandler.players[npcs[i].killedBy];

							if (target != null) {
								target.getSlayer().killTaskMonster(npcs[i]);
								/*
								 * if (target.getSlayer().isSuperiorNpc()) {
								 * target.getSlayer().handleSuperiorExp(npcs[i]); }
								 */
							}
						}
						if(npcs[i].inRaids()){
							Player killer = PlayerHandler.players[npcs[i].killedBy];
							killer.getRaids().raidLeader.getRaids().handleMobDeath(npcs[i].npcType);
						}
						appendBossKC(i);
						appendKillCount(i);
						handleGodwarsDeath(npc);
						handleDiaryKills(npc);
						handleDailyKills(npc);
						npcs[i].absX = npcs[i].makeX;
						npcs[i].absY = npcs[i].makeY;
						npcs[i].getHealth().reset();
						npcs[i].animNumber = 0x328;
						npcs[i].updateRequired = true;
						npcs[i].animUpdateRequired = true;

						/**
						 * Actions on certain npc deaths
						 */
						switch (npcs[i].npcType) {
						case 965:
							npcs[i].npcType = 963;
							break;
						case 5129:
							Glod.rewardPlayers(player);
							Glod.specialAmount = 0;
							break;
						case 4922:
							IceQueen.rewardPlayers(player);
							IceQueen.specialAmount = 0;
							break;
						case 3127:
							player.getFightCave().stop();
							break;
						/*
						 * case 5762: //Not in use case 5744: player.raidPoints += 1;
						 * player.sendMessage("You currently have @red@" + player.raidPoints +
						 * "@bla@ Assault Points."); break; case 7595: player.raidPoints += 2;
						 * player.sendMessage("You currently have @red@" + player.raidPoints +
						 * "@bla@ Assault Points."); break;
						 */
						case 6367:
						case 6369:
						case 6370:
						case 6371:
						case 6372:
						case 6373:
						case 6374:
						case 6375:
						case 6376:
						case 6377:
						case 6378:
							RecipeForDisaster rfd = player.getrecipeForDisaster();
							if (player.rfdWave < 5) {
								rfd.wave();
							}
							player.rfdWave++;
							player.rfdGloves++;

							switch (player.rfdWave) {
							case 0:
								player.getDH().sendNpcChat1("You DARE come HERE?!?!", 6368, "Culinaromancer");
								player.nextChat = -1;
								break;

							case 1:
							case 2:
							case 3:
							case 4:
							case 5:
								player.getDH().sendNpcChat1("NOOOooo...", 6368, "Culinaromancer");
								player.nextChat = -1;
								break;

							case 6:
								player.getDH().sendNpcChat2("You have caused me enough grief!",
										"I guess I'll have to finish you off myself!", 6368, "Culinaromancer");
								player.nextChat = -1;
								break;
							}
							break;
						case 6368:
							RecipeForDisaster rfdd = player.getrecipeForDisaster();
							if (rfdd != null) {
								rfdd.end(DisposeTypes.COMPLETE);
							}
							break;

						case 5862:
							Cerberus cerb = player.getCerberus();
							if (cerb != null) {
								cerb.end(DisposeTypes.COMPLETE);
							}
							break;

						case Skotizo.SKOTIZO_ID:
							if (player.getSkotizo() != null) {
								player.getSkotizo().end(DisposeTypes.COMPLETE);
							}
							break;

						case InfernoWave.TZKAL_ZUK:
							if (player.getInferno() != null) {
								player.getInferno().end(DisposeTypes.COMPLETE);
							}
							break;

						case Skotizo.AWAKENED_ALTAR_NORTH:
							Server.getGlobalObjects().remove(28923, 1694, 9904, player.getSkotizo().getHeight()); // Remove
																													// North
																													// -
																													// Awakened
																													// Altar
							Server.getGlobalObjects().add(new GlobalObject(28924, 1694, 9904,
									player.getSkotizo().getHeight(), 2, 10, -1, -1)); // North - Empty Altar
							player.getPA().sendChangeSprite(29232, (byte) 0);
							player.getSkotizo().altarCount--;
							player.getSkotizo().northAltar = false;
							player.getSkotizo().altarMap.remove(1);
							break;
						case Skotizo.AWAKENED_ALTAR_SOUTH:
							Server.getGlobalObjects().remove(28923, 1696, 9871, player.getSkotizo().getHeight()); // Remove
																													// South
																													// -
																													// Awakened
																													// Altar
							Server.getGlobalObjects().add(new GlobalObject(28924, 1696, 9871,
									player.getSkotizo().getHeight(), 0, 10, -1, -1)); // South - Empty Altar
							player.getPA().sendChangeSprite(29233, (byte) 0);
							player.getSkotizo().altarCount--;
							player.getSkotizo().southAltar = false;
							player.getSkotizo().altarMap.remove(2);
							break;
						case Skotizo.AWAKENED_ALTAR_WEST:
							Server.getGlobalObjects().remove(28923, 1678, 9888, player.getSkotizo().getHeight()); // Remove
																													// West
																													// -
																													// Awakened
																													// Altar
							Server.getGlobalObjects().add(new GlobalObject(28924, 1678, 9888,
									player.getSkotizo().getHeight(), 1, 10, -1, -1)); // West - Empty Altar
							player.getPA().sendChangeSprite(29234, (byte) 0);
							player.getSkotizo().altarCount--;
							player.getSkotizo().westAltar = false;
							player.getSkotizo().altarMap.remove(3);
							break;
						case Skotizo.AWAKENED_ALTAR_EAST:
							Server.getGlobalObjects().remove(28923, 1714, 9888, player.getSkotizo().getHeight()); // Remove
																													// East
																													// -
																													// Awakened
																													// Altar
							Server.getGlobalObjects().add(new GlobalObject(28924, 1714, 9888,
									player.getSkotizo().getHeight(), 3, 10, -1, -1)); // East - Empty Altar
							player.getPA().sendChangeSprite(29235, (byte) 0);
							player.getSkotizo().altarCount--;
							player.getSkotizo().eastAltar = false;
							player.getSkotizo().altarMap.remove(4);
							break;
						case Skotizo.DARK_ANKOU:
							player.getSkotizo().ankouSpawned = false;
							break;

						case 6615:
							Scorpia.stage = 0;
							break;

						case 319:
							CorporealBeast.stage = 0;
							break;


						case 6600:
							spawnNpc(6601, npcs[i].absX, npcs[i].absY, 0, 0, 0, 0, 0, 0);
							break;

						case 6601:
							spawnNpc(6600, npcs[i].absX, npcs[i].absY, 0, 0, 0, 0, 0, 0);
							npcs[i] = null;
							NPC golem = getNpc(6600);
							if (golem != null) {
								golem.actionTimer = 150;
							}
							break;
						}

						if (DagannothMother.RANGE_OF_IDS.contains(npcs[i].npcType)) {
							DagannothMother dm = player.getDagannothMother();

							if (dm != null) {
								dm.end(DisposeType.COMPLETE);
							}
						}
					} else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn && npcs[i].npcType != 1739
							&& npcs[i].npcType != 1740 && npcs[i].npcType != 1741 && npcs[i].npcType != 1742) {
						if (player != null) {
							npcs[i] = null;
						} else {
							int newType = npcs[i].npcType;
							int newX = npcs[i].makeX;
							int newY = npcs[i].makeY;
							int newH = npcs[i].heightLevel;
							int newWalkingType = npcs[i].walkingType;
							int newHealth = npcs[i].getHealth().getMaximum();
							int newMaxHit = npcs[i].maxHit;
							int newAttack = npcs[i].attack;
							int newDefence = npcs[i].defence;
							npcs[i] = null;
							newNPC(newType, newX, newY, newH, newWalkingType, newHealth, newMaxHit, newAttack,
									newDefence);
						}
						/*
						 * } else { for(MonsterHunt.Npcs hunt : MonsterHunt.Npcs.values()) {
						 * if(npcs[i].npcType == hunt.getNpcId()) { npcs[i] = null; return; } } }
						 */
					}
				}
			}
		}
	}

	/**
	 * Apply damage
	 * 
	 * @param i
	 *            the damage were applying towards a playable character
	 */
	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (PlayerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead) {
				return;
			}
			if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType == 7413) {
				return;
			}

			Player c = PlayerHandler.players[npcs[i].oldIndex];

			if(npcs[i].npcType == 7706 && c.getInferno().behindGlyph){
				c.getInferno().behindGlyph = false;
				return;
			}
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (c.playerIndex <= 0 && c.npcIndex <= 0)
				if (c.autoRet == 1)
					c.npcIndex = i;
			if (c.attackTimer <= 3) {
				if (!NPCHandler.isFightCaveNpc(i) || npcs[i].npcType != 319) {
					c.startAnimation(c.getCombat().getBlockEmote());
				}
			}
			if (c.getItems().isWearingItem(12931) || c.getItems().isWearingItem(13197)
					|| c.getItems().isWearingItem(13199)) {
				DamageEffect venom = new SerpentineHelmEffect();
				if (venom.isExecutable(c)) {
					venom.execute(c, npcs[i], new Damage(6));
				}
			}
			npcs[i].totalAttacks++;
			boolean protectionIgnored = prayerProtectionIgnored(i);
			if (c.respawnTimer <= 0) {
				int damage = 0;
				int secondDamage = -1;

				Optional<Brother> activeBrother = c.getBarrows().getActive();

				/**
				 * Handles all the different damage approaches npcs are dealing
				 */
				if (npcs[i].attackType != null) {
					switch (npcs[i].attackType) {

					/**
					 * Handles npcs who are dealing melee based attacks
					 */
					case MELEE:
						damage = Misc.random(getMaxHit(i));

						switch (npcs[i].npcType) {
						case 6374:
							secondDamage = Misc.random(getMaxHit(i));
							break;

						case 3116:
							c.playerLevel[5] -= 1;
							if (c.playerLevel[5] < 0) {
								c.playerLevel[5] = 0;
							}
							c.getPA().refreshSkill(Config.PRAYER);
							break;

						/**
						 * Summoned soul melee
						 */
						// case 5869:
						// damage = !c.protectingMelee() ? 30 : 0;
						// c.playerLevel[5] -= c.protectingMelee() || c.protectingMelee() &&
						// c.getItems().isWearingItem(12821) ? 30 : 0;
						// if (c.playerLevel[5] < 0) {
						// c.playerLevel[5] = 0;
						// }
						// break;
						}

						/**
						 * Calculate defence
						 */
						if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc
								.random(NPCHandler.npcs[i].attack)) {
							damage = 0;
						}

						if (npcs[i].npcType == 5869) {
							damage = !c.protectingMelee() ? 30 : 0;
							c.playerLevel[5] -= c.protectingMelee() && !c.getItems().isWearingItem(12821) ? 30
									: c.protectingMelee() && c.getItems().isWearingItem(12821) ? 15 : 0;
							if (c.playerLevel[5] < 0) {
								c.playerLevel[5] = 0;
							}
						}

						/**
						 * Zulrah
						 */
						if (npcs[i].npcType == 2043 && c.getZulrahEvent().getNpc() != null
								&& c.getZulrahEvent().getNpc().equals(npcs[i])) {
							Boundary boundary = new Boundary(npcs[i].targetedLocation.getX(),
									npcs[i].targetedLocation.getY(), npcs[i].targetedLocation.getX(),
									npcs[i].targetedLocation.getY());
							if (!Boundary.isIn(c, boundary)) {
								return;
							}
							damage = 20 + Misc.random(25);
						}

						/**
						 * Special attacks
						 */
						if (activeBrother.isPresent() && activeBrother.get().getId() == npcs[i].npcType) {
							double random = Math.random();
							if (random <= Barrows.SPECIAL_CHANCE) {
								switch (activeBrother.get().getId()) {
								case Brother.DHAROK:
									double healthRatio = Math.round(
											(npcs[i].getHealth().getAmount() / npcs[i].getHealth().getMaximum()) * 10)
											/ 10d;
									healthRatio = Double.max(0.1, healthRatio);
									damage *= -2 * healthRatio + 3;
									break;
								case Brother.GUTHAN:
									int addedHealth = c.protectingMelee() ? 0
											: Integer.min(damage,
													npcs[i].getHealth().getMaximum() - npcs[i].getHealth().getAmount());
									if (addedHealth > 0) {
										c.gfx0(398);
										npcs[i].getHealth().increase(addedHealth);
									}
									break;

								case Brother.TORAG:
									c.gfx0(399);
									break;

								case Brother.VERAC:
									protectionIgnored = true;
									damage /= 2;
									break;

								}
							}
						}

						/**
						 * Protection prayer
						 */
						if (c.protectingMelee() && !protectionIgnored) {
							if (npcs[i].npcType == 5890)
								damage /= 3;
							else if (npcs[i].npcType == 963 || npcs[i].npcType == 965 || npcs[i].npcType == 8349
									|| npcs[i].npcType == 8133 || npcs[i].npcType == 6342 || npcs[i].npcType == 2054
									|| npcs[i].npcType == 239 || npcs[i].npcType == 998 || npcs[i].npcType == 999
									|| npcs[i].npcType == 1000 || npcs[i].npcType == 7554 || npcs[i].npcType == 319
									|| npcs[i].npcType == 320 || npcs[i].npcType == 6615 || npcs[i].npcType == 5916
									|| npcs[i].npcType == 7544 || npcs[i].npcType == 5129)
								damage /= 2;
							else
								damage = 0;
						} else if (c.protectingMelee() && protectionIgnored) {
							damage /= 2;
						}

						/**
						 * Specials and defenders
						 */
						if (Server.getEventHandler().isRunning(c, "staff_of_the_dead")) {
							Special special = Specials.STAFF_OF_THE_DEAD.getSpecial();
							Damage d = new Damage(damage);
							special.hit(c, npcs[i], d);
							damage = d.getAmount();
						}
						if (c.playerEquipment[c.playerShield] == 12817) {
							if (Misc.random(100) > 30 && damage > 0) {
								damage *= .75;
							}
						}
						if (c.getHealth().getAmount() - damage < 0) {
							damage = c.getHealth().getAmount();
						}
						break;

					/**
					 * Handles npcs who are dealing range based attacks
					 */
					case RANGE:
						damage = Misc.random(getMaxHit(i));

						switch (npcs[i].npcType) {
						case 6377:
							secondDamage = Misc.random(getMaxHit(i));
							break;

						/**
						 * Summoned soul range
						 */
						// case 5867:
						// damage = !c.protectingRange() ? 30 : 0;
						// c.playerLevel[5] -= c.protectingRange() || c.protectingRange() &&
						// c.getItems().isWearingItem(12821) ? 30 : 0;
						// if (c.playerLevel[5] < 0) {
						// c.playerLevel[5] = 0;
						// }
						// break;
						}

						/**
						 * Range defence
						 */
						if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc
								.random(NPCHandler.npcs[i].attack)) {
							damage = 0;
						}

						if (npcs[i].npcType == 5867) {
							damage = !c.protectingRange() ? 30 : 0;
							c.playerLevel[5] -= c.protectingMelee() && !c.getItems().isWearingItem(12821) ? 30
									: c.protectingMelee() && c.getItems().isWearingItem(12821) ? 15 : 0;
							if (c.playerLevel[5] < 0) {
								c.playerLevel[5] = 0;
							}
						}

						/**
						 * Special attacks
						 */
						if (activeBrother.isPresent() && activeBrother.get().getId() == npcs[i].npcType) {
							double random = Math.random();
							if (random <= Barrows.SPECIAL_CHANCE) {
								switch (activeBrother.get().getId()) {
								case Brother.KARIL:
									c.playerLevel[Config.AGILITY] = Integer.max(0,
											(int) (c.playerLevel[Config.AGILITY] * 0.8));
									c.getPA().refreshSkill(Config.AGILITY);
									c.gfx0(401);
									break;
								}
							}
						}
						/**
						 * Protection prayer
						 */
						if (c.protectingRange() && !protectionIgnored) {
							if (npcs[i].npcType == 963 || npcs[i].npcType == 965 || npcs[i].npcType == 8349
									|| npcs[i].npcType == 8133 || npcs[i].npcType == 6342 || npcs[i].npcType == 2054
									|| npcs[i].npcType == 7554 || npcs[i].npcType == 239 || npcs[i].npcType == 319
									|| npcs[i].npcType == 499) {
								damage /= 2;
							} else {
								damage = 0;
							}
							if (c.getHealth().getAmount() - damage < 0) {
								damage = c.getHealth().getAmount();
							}
						} else if (c.protectingRange() && protectionIgnored) {
							damage /= 2;
						}
						if (npcs[i].npcType == 2042 || npcs[i].npcType == 2044) {
							c.getHealth().proposeStatus(HealthStatus.VENOM, 6, Optional.of(npcs[i]));
						}
						if (npcs[i].endGfx > 0 || npcs[i].npcType == 3127) {
							c.gfx100(npcs[i].endGfx);
						}
						if (npcs[i].endGfx > 0 || npcs[i].npcType == 7700) {
							c.gfx100(npcs[i].endGfx);
						}
						break;

					/**
					 * Handles npcs who are dealing mage based attacks
					 */
					case MAGE:
						damage = Misc.random(getMaxHit(i));
						boolean magicFailed = false;
						/**
						 * Attacks
						 */
						switch (npcs[i].npcType) {
						case 6373:
						case 6375:
						case 6376:
						case 6378:
							secondDamage = Misc.random(getMaxHit(i));
							break;

						/**
						 * Summoned soul mage
						 */
						// case 5868:
						// damage = !c.protectingMagic() ? 30 : 0;
						// c.playerLevel[5] -= c.protectingMagic() || c.protectingMagic() &&
						// c.getItems().isWearingItem(12821) ? 30 : 0;
						// if (c.playerLevel[5] < 0) {
						// c.playerLevel[5] = 0;
						// }
						// break;

						case 6371: // Karamel
							c.freezeTimer = 4;
							break;

						case 2205:
							secondDamage = Misc.random(27);
							break;

						case 6609:
							c.sendMessage("Callisto's fury sends an almighty shockwave through you.");
							break;
						}

						/**
						 * Magic defence
						 */
						if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(NPCHandler.npcs[i].attack)) {
							damage = 0;
							if (secondDamage > -1) {
								secondDamage = 0;
							}
							magicFailed = true;
						}

						if (npcs[i].npcType == 5868) {
							damage = !c.protectingMagic() ? 30 : 0;
							c.playerLevel[5] -= c.protectingMagic() && !c.getItems().isWearingItem(12821) ? 30
									: c.protectingMagic() && c.getItems().isWearingItem(12821) ? 15 : 0;
							if (c.playerLevel[5] < 0) {
								c.playerLevel[5] = 0;
							}
						}
						/**
						 * Protection prayer
						 */
						if (c.protectingMagic() && !protectionIgnored) {
							switch (npcs[i].npcType) {
							case 494:
							case 492:
							case 5535:
								int max = npcs[i].npcType == 494 ? 2 : 0;
								if (Misc.random(2) == 0) {
									damage = 1 + Misc.random(max);
								} else {
									damage = 0;
									if (secondDamage > -1) {
										secondDamage = 0;
									}
								}
								break;

							case 1677:
							case 963:
							case 965:
							case 8349:
							case 8133:
							case 6342:
							case 2054:
							case 239:
							case 1046:
							case 319:
							case 7554:
							case 7604: // Skeletal mystic
							case 7605: // Skeletal mystic
							case 7606: // Skeletal mystic
							case 7617:
							case 4922:
								damage /= 2;
								break;

							default:
								damage = 0;
								if (secondDamage > -1) {
									secondDamage = 0;
								}
								magicFailed = true;
								break;

							}

						} else if (c.protectingMagic() && protectionIgnored) {
							damage /= 2;
						}
						if (c.getHealth().getAmount() - damage < 0) {
							damage = c.getHealth().getAmount();
						}
						if (npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
							c.gfx100(npcs[i].endGfx);
						} else {
							c.gfx100(85);
						}
						c.getCombat().appendVengeanceNPC(damage + (secondDamage > 0 ? secondDamage : 0), i);
						break;

					/**
					 * Handles npcs who are dealing dragon fire based attacks
					 */
					case DRAGON_FIRE:
						int resistance = c.getItems().isWearingItem(1540) || c.getItems().isWearingItem(11283)
								|| c.getItems().isWearingItem(11284) ? 1 : 0;
						if (System.currentTimeMillis() - c.lastAntifirePotion < c.antifireDelay) {
							resistance++;
						}
						if (resistance == 0) {
							damage = Misc.random(getMaxHit(i));
							if (npcs[i].npcType == 465) {
								c.sendMessage("You are badly burnt by the cold breeze!");
							} else {
								c.sendMessage("You are badly burnt by the dragon fire!");
							}
						} else if (resistance == 1) {
							damage = Misc.random(15);
						} else if (resistance == 2) {
							damage = 0;
						}
						if (npcs[i].endGfx != 430 && resistance == 2) {
							damage = 5 + Misc.random(5);
						}

						/**
						 * Attacks
						 */
						switch (npcs[i].endGfx) {
						case 429:
							c.getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.of(npcs[i]));
							break;

						case 163:
							c.freezeTimer = 15;
							c.sendMessage("You have been frozen to the ground.");
							break;

						case 428:
							c.freezeTimer = 10;
							break;

						case 431:
							c.lastSpear = System.currentTimeMillis();
							break;
						}
						if (c.getHealth().getAmount() - damage < 0)
							damage = c.getHealth().getAmount();
						c.gfx100(npcs[i].endGfx);

						c.getCombat().appendVengeanceNPC(damage + 0, i);
						break;

					/**
					 * Handles npcs who are dealing special attacks
					 */
					case SPECIAL:
						damage = Misc.random(getMaxHit(i));

						/**
						 * Attacks
						 */
						switch (npcs[i].npcType) {
						case 3129:
							int prayerReduction = c.playerLevel[5] / 2;
							if (prayerReduction < 1) {
								break;
							}
							c.playerLevel[5] -= prayerReduction;
							if (c.playerLevel[5] < 0) {
								c.playerLevel[5] = 0;
							}
							c.getPA().refreshSkill(5);
							c.sendMessage(
									"K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
							break;
						case 1046:
						case 1049:
							prayerReduction = c.playerLevel[5] / 10;
							if (prayerReduction < 1) {
								break;
							}
							c.playerLevel[5] -= prayerReduction;
							if (c.playerLevel[5] < 0) {
								c.playerLevel[5] = 0;
							}
							c.getPA().refreshSkill(5);
							c.sendMessage("Your prayer has been drained drastically.");
							break;
						case 6609:
							damage = 3;
							c.gfx0(80);
							c.lastSpear = System.currentTimeMillis();
							c.getPA().getSpeared(npcs[i].absX, npcs[i].absY, 3);
							c.sendMessage("Callisto's roar sends your backwards.");
							break;
						case 6610:
							if (c.protectingMagic()) {
								damage *= .7;
							}
							secondDamage = Misc.random(getMaxHit(i));
							if (secondDamage > 0) {
								c.gfx0(80);
							}
							break;

						case 465:
							c.freezeTimer = 15;
							c.sendMessage("You have been frozen.");
							break;

						case 7144:
						case 7145:
						case 7146:
							if (gorillaBoulder.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								return;
							}
							break;

						case 5890:
							if (damage > 0 && Misc.random(2) == 0) {
								if (npcs[i].getHealth().getStatus() == HealthStatus.POISON) {
									c.getHealth().proposeStatus(HealthStatus.POISON, 15, Optional.of(npcs[i]));
								}
							}
							if (gorillaBoulder.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								return;
							}
							break;
						}
						break;
					}

					if (npcs[i].npcType == 320) {
						int distanceFromTarget = c.distanceToPoint(npcs[i].getX(), npcs[i].getY());

						if (distanceFromTarget <= 1) {
							NPC corp = NPCHandler.getNpc(319);
							Damage heal = new Damage(
									damage + Misc.random(15 + 5) + (secondDamage > 0 ? secondDamage : 0));
							if (corp != null && corp.getHealth().getAmount() < 2000) {
								corp.getHealth().increase(heal.getAmount());
							}
						}
					}
					if (npcs[i].npcType == 6617 || npcs[i].npcType == 6616 || npcs[i].npcType == 6615) {
						int distanceFromTarget = c.distanceToPoint(npcs[i].getX(), npcs[i].getY());

						List<NPC> healer = Arrays.asList(NPCHandler.npcs);

						if (distanceFromTarget <= 1 && Scorpia.stage > 0 && healer.stream().filter(Objects::nonNull)
								.anyMatch(n -> n.npcType == 6617 && !n.isDead && n.getHealth().getAmount() > 0)) {
							NPC scorpia = NPCHandler.getNpc(6615);
							Damage heal = new Damage(
									damage + Misc.random(45 + 5) + (secondDamage > 0 ? secondDamage : 0));
							if (scorpia != null && scorpia.getHealth().getAmount() < 150) {
								scorpia.getHealth().increase(heal.getAmount());
							}
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx100(npcs[i].endGfx);
					}
					int poisonDamage = getPoisonDamage(npcs[i]);
					if (poisonDamage > 0 && Misc.random(10) == 1) {
						c.getHealth().proposeStatus(HealthStatus.POISON, poisonDamage, Optional.of(npcs[i]));
					}
					if (c.getHealth().getAmount() - damage < 0
							|| secondDamage > -1 && c.getHealth().getAmount() - secondDamage < 0) {
						damage = c.getHealth().getAmount();
						if (secondDamage > -1) {
							secondDamage = 0;
						}
					}
					handleSpecialEffects(c, i, damage);
					c.logoutDelay = System.currentTimeMillis();
					if (damage > -1) {
						c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
						c.addDamageTaken(npcs[i], damage);
					}
					if (secondDamage > -1) {
						c.appendDamage(secondDamage, secondDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
						c.addDamageTaken(npcs[i], secondDamage);
					}
					if (damage > 0 || secondDamage > 0) {
						c.getCombat().appendVengeanceNPC(damage + (secondDamage > 0 ? secondDamage : 0), i);
						c.getCombat().applyRecoilNPC(damage + (secondDamage > 0 ? secondDamage : 0), i);
					}
					int rol = c.getHealth().getAmount() - damage;
					if (rol > 0 && rol < c.getHealth().getMaximum() / 10) {
						ringOfLife(c);
					}
					switch (npcs[i].npcType) {
					// Abyssal sire
					case 5890:
						int health = npcs[i].getHealth().getAmount();
						c.sireHits++;
						int randomAmount = Misc.random(5);
						switch (c.sireHits) {
						case 10:
						case 20:
						case 30:
						case 40:
							for (int id = 0; id < randomAmount; id++) {
								int x = npcs[i].absX + Misc.random(2);
								int y = npcs[i].absY - Misc.random(2);
								newNPC(5916, x, y, 0, 0, 15, 15, 100, 0);
							}
							break;

						case 45:
							c.sireHits = 0;
							break;

						}
						if (health < 400 && health > 329 || health < 100) {
							npcs[i].attackType = CombatType.MELEE;
						}
						if (health < 330 && health > 229) {
							npcs[i].attackType = CombatType.MAGE;
						}
						if (health < 230 && health > 99) {
							npcs[i].attackType = CombatType.SPECIAL;
							npcs[i].getHealth().increase(6);
						}
						break;

					case 8028:
						if (Vorkath.attackStyle == 0) {
							npcs[i].attackType = CombatType.MAGE;
						}
						break;
					/**
					 * Demonic Gorillas attack
					 */

						case 7554:
							npcs[i].attackType = CombatType.MAGE;
							npcs[i].projectileId = 970;
							npcs[i].endGfx = 971;
							npcs[i].hitDelayTimer = 3;
							break;
					case 7144:
					case 7145:
					case 7146:
						if (damage == 0) {
							if (c.totalMissedGorillaHits >= 6) {
								c.totalMissedGorillaHits = 0;
							}
							c.totalMissedGorillaHits++;
						}
						if (c.totalMissedGorillaHits == 6) {
							c.totalMissedGorillaHits = 0;

							switch (npcs[i].attackType) {
							case MELEE:
								switch (Misc.random(2)) {
								case 0:
									npcs[i].attackType = CombatType.MAGE;
									break;
								case 1:
									npcs[i].attackType = CombatType.SPECIAL;
									break;
								case 2:
									npcs[i].attackType = CombatType.RANGE;
									break;
								}
								break;
							case MAGE:
								switch (Misc.random(2)) {
								case 0:
									npcs[i].attackType = CombatType.MELEE;
									break;
								case 1:
									npcs[i].attackType = CombatType.SPECIAL;
									break;
								case 2:
									npcs[i].attackType = CombatType.RANGE;
									break;
								}
								break;
							case RANGE:
								switch (Misc.random(2)) {
								case 0:
									npcs[i].attackType = CombatType.MAGE;
									break;
								case 1:
									npcs[i].attackType = CombatType.SPECIAL;
									break;
								case 2:
									npcs[i].attackType = CombatType.MELEE;
									break;
								}
								break;
							case SPECIAL:
								switch (Misc.random(2)) {
								case 0:
									npcs[i].attackType = CombatType.MAGE;
									break;
								case 1:
									npcs[i].attackType = CombatType.MELEE;
									break;
								case 2:
									npcs[i].attackType = CombatType.RANGE;
									break;
								}
								break;

							default:
								break;
							}
							break;
						}
						c.updateRequired = true;
					}
				}
			}
		}
	}

	/**
	 * Poison damage
	 * 
	 * @param npc
	 *            the npc whom can be poisonous
	 * @return the amount of damage the poison will begin on
	 */
	private int getPoisonDamage(NPC npc) {
		switch (npc.npcType) {
		case 3129:
			return 16;

		case 3021:
			return 5;

		case 957:
			return 4;

		case 959:
			return 6;

		case 6615:
			return 10;
		}
		return 0;
	}

	/**
	 * Multi attacks from a distance
	 * 
	 * @param npc
	 *            the npc whom can pefrom multiattacks from a distance
	 * @return the distance that the npc can reach from
	 */
	private int multiAttackDistance(NPC npc) {
		if (npc == null) {
			return 0;
		}
		switch (npc.npcType) {
		case 239:
			return 35;
		}
		return 15;
	}

	/**
	 * Multi attack damage
	 * 
	 * @param i
	 *            the damage set
	 */
	public void multiAttackDamage(int i) {
		int damage = Misc.random(getMaxHit(i));
		Hitmark hitmark = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].isInvisible()) {
					continue;
				}
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY,
						multiAttackDistance(npcs[i]))) {
					if (npcs[i].attackType == CombatType.SPECIAL) {
						if (npcs[i].npcType == 5862) {
							if (cerberusGroundCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}
						if (npcs[i].npcType == 6618) {
							if (archSpellCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}
						if (npcs[i].npcType == 6766) {
							if (explosiveSpawnCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}

						if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
							if (!Boundary.isIn(c, Boundary.CORPOREAL_BEAST_LAIR)) {
								return;
							}
						}
						if (npcs[i].npcType == 6619) {
							if (fanaticSpellCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}
						if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
							if (!(c.absX > npcs[i].absX - 5 && c.absX < npcs[i].absX + 5 && c.absY > npcs[i].absY - 5
									&& c.absY < npcs[i].absY + 5)) {
								continue;
							}
							c.sendMessage(
									"Vet'ion pummels the ground sending a shattering earthquake shockwave through you.");
							createVetionEarthquake(c);
						}
						if (npcs[i].npcType == 319) {
							if (corpSpellCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}
						c.appendDamage(damage, hitmark);
					} else if (npcs[i].attackType == CombatType.DRAGON_FIRE) {
						int resistance = c.getItems().isWearingItem(1540) || c.getItems().isWearingItem(11283)
								|| c.getItems().isWearingItem(11284) ? 1 : 0;
						if (System.currentTimeMillis() - c.lastAntifirePotion < c.antifireDelay) {
							resistance++;
						}
					//	c.sendMessage("Resistance: " + resistance);
						if (resistance == 0) {
							damage = Misc.random(getMaxHit(i));
							c.sendMessage("You are badly burnt by the dragon fire!");
						} else if (resistance == 1)
							damage = Misc.random(15);
						else if (resistance == 2)
							damage = 0;
						if (c.getHealth().getAmount() - damage < 0)
							damage = c.getHealth().getAmount();
						c.gfx100(npcs[i].endGfx);
						c.appendDamage(damage, hitmark);
					} else if (npcs[i].attackType == CombatType.MAGE) {
						if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
							if (vetionSpellCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}
						if (npcs[i].npcType == 3162) {
							damage /= 3;
						}
						if (!c.protectingMagic()) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else {
								c.appendDamage(0, Hitmark.MISS);
							}
						} else {
							switch (npcs[i].npcType) {
							case 1046:
							case 3162:
							case 6610:
							case 6611:
							case 6612:
								damage *= .5;
								break;

							case 319:
								if (c.protectingMagic())
									damage /= 2;
								break;
							default:
								damage = 0;
								break;
							}
							c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
						}
					} else if (npcs[i].attackType == CombatType.RANGE) {
						if (!c.protectingRange()) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else {
								c.appendDamage(0, Hitmark.MISS);
							}
							if (npcs[i].npcType == 2215) {
								damage /= 2;
							}
						} else {
							switch (npcs[i].npcType) {
							default:
								damage = 0;
								break;
							}
							c.appendDamage(damage, Hitmark.MISS);
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);
					}
					c.getCombat().appendVengeanceNPC(damage, i);
				}
			}
		}
	}

	/**
	 * Gets pulled
	 * 
	 * @param i
	 *            the npc to be pulled
	 * @return return true if it can, false otherwise
	 */
	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
		case 2215:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	/**
	 * Multi attacks
	 * 
	 * @param i
	 *            the npc whom can perform multi attacks
	 * @return true if it can, false otherwise
	 */
	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
			case 7554:
				return true;
		case 6611:
		case 6612:
		case 6618:
		case 6619:
		case 319:
		case 6766:
		case 7617:
			return npcs[i].attackType == CombatType.SPECIAL || npcs[i].attackType == CombatType.MAGE;

		case 7604:
		case 7605:
		case 7606:
			return npcs[i].attackType == CombatType.SPECIAL;

		case 1046:
			return npcs[i].attackType == CombatType.MAGE
					|| npcs[i].attackType == CombatType.SPECIAL && Misc.random(3) == 0;
		case 6610:
			return npcs[i].attackType == CombatType.MAGE;
		case 2558:
			return true;
		case 2562:
			if (npcs[i].attackType == CombatType.MAGE)
				return true;
		case 2215:
			return npcs[i].attackType == CombatType.RANGE;
		case 3162:
			return npcs[i].attackType == CombatType.MAGE;
		case 963:
		case 965:
			return npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE;
		default:
			return false;
		}

	}

	/**
	 * Npc killer id
	 * 
	 * @param npcId
	 *            the npc whom were grabbing the id from
	 * @return the killeId to be printed
	 */
	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int p = 1; p < Config.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
						oldDamage = PlayerHandler.players[p].totalDamageDealt;
						killerId = p;
					}
					PlayerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * Barrows kills
	 * 
	 * @param i
	 *            the barrow brother whom been killed
	 */
	private void killedBarrow(int i) {
		Player player = PlayerHandler.players[npcs[i].killedBy];
		if (player != null && player.getBarrows() != null) {
			Optional<Brother> brother = player.getBarrows().getBrother(npcs[i].npcType);
			if (brother.isPresent()) {
				brother.get().handleDeath();
			} else if (Boundary.isIn(npcs[i], Barrows.TUNNEL)) {
				if (player.getBarrows().getKillCount() < 25) {
					player.getBarrows().increaseMonsterKilled();
				}
			}
		}
	}

	/**
	 * Godwars kill
	 * 
	 * @param npc
	 *            the godwars npc whom been killed
	 */
	private void handleGodwarsDeath(NPC npc) {
		Player player = PlayerHandler.players[npc.killedBy];
		if (!GodwarsNPCs.NPCS.containsKey(npc.npcType)) {
			return;
		}
		if (player != null && player.getGodwars() != null) {
			player.getGodwars().increaseKillcount(GodwarsNPCs.NPCS.get(npc.npcType));
			/*
			 * if (Misc.random(60 + 10 * player.getItems().getItemCount(Godwars.KEY_ID,
			 * true)) == 1) { /** Key will not drop if player owns more than 3 keys already
			 *
			 * int key_amount =
			 * player.getDiaryManager().getWildernessDiary().hasCompleted("ELITE") ? 6 : 3;
			 * if (player.getItems().getItemCount(Godwars.KEY_ID, true) > key_amount) {
			 * return; } Server.itemHandler.createGroundItem(player, Godwars.KEY_ID,
			 * npc.getX(), npc.getY(), player.heightLevel, 1, player.getIndex()); }
			 */
		}
	}

	/**
	 * Handles kills towards the achievement diaries
	 * 
	 * @param npc
	 *            The npc killed.
	 */
	private void handleDiaryKills(NPC npc) {
		Player player = PlayerHandler.players[npc.killedBy];

		if (player != null) {
			AchievementDiaryKills.kills(player, npc.npcType);
		}
	}

	/**
	 * Handles kills towards daily tasks
	 * 
	 * @param npc
	 *            The npc killed.
	 */
	private void handleDailyKills(NPC npc) {
		Player player = PlayerHandler.players[npc.killedBy];

		if (player != null) {
			DailyTaskKills.kills(player, npc.npcType);
		}
	}

	/**
	 * Tzhaar kill
	 * 
	 * @param player
	 *            the player who killed a tzhaar
	 * @param i
	 *            the tzhaar to be killed
	 */
	private void tzhaarDeathHandler(Player player, int i) {// hold a vit plz
		if (npcs[i] != null) {
			if (player != null) {
				if (player.getFightCave() != null) {
					if (isFightCaveNpc(i))
						killedTzhaar(player, i);
				}
			}
		}
	}

	private void infernoDeathHandler(Player player, int i) {// hold a vit plz
		if (npcs[i] != null) {
			if (player != null) {
				if (player.getInfernoMinigame() != null) {
					if (isInfernoNpc(i))
						killedInferno(player, i);
				}
			}
		}
	}

	/**
	 * Killed tzhaar
	 * 
	 * @param player
	 *            the player who killed a tzhaar
	 * @param i
	 *            the tzhaar to be killed
	 */
	private void killedTzhaar(Player player, int i) {
		if (player.getFightCave() != null) {
			player.getFightCave().setKillsRemaining(player.getFightCave().getKillsRemaining() - 1);
			if (player.getFightCave().getKillsRemaining() == 0) {
				player.waveId++;
				player.getFightCave().spawn();
			}
		}
	}

	private void killedInferno(Player player, int i) {
		if (player.getInfernoMinigame() != null) {
			player.getInfernoMinigame().setKillsRemaining(player.getInfernoMinigame().getKillsRemaining() - 1);
			if (player.getInfernoMinigame().getKillsRemaining() == 0) {
				player.infernoWaveId++;
				System.out.println("Inferno Wave ID: " + player.infernoWaveId);
				player.getInfernoMinigame().spawn();
			}
		}
	}

	/**
	 * Jad kill
	 * 
	 * @param i
	 */
	public void handleJadDeath(int i) {
		Player c = PlayerHandler.players[npcs[i].spawnedBy];
		c.getItems().addItem(6570, 1);
		c.getFightCave();
		// c.getDH().sendDialogues(69, 2617);
		c.getFightCave().stop();
		c.waveId = 300;
	}

	/**
	 * Dropping items
	 * 
	 * @param i
	 */
	public void dropItems(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			if (c.getTargeted() != null && npcs[i].equals(c.getTargeted())) {
				c.setTargeted(null);
				c.getPA().sendEntityTarget(0, npcs[i]);
			}
			c.getAchievements().kill(npcs[i]);
			PetHandler.receive(c, npcs[i]);
			if (npcs[i].npcType >= 1610 && npcs[i].npcType <= 1612) {
				c.setArenaPoints(c.getArenaPoints() + 1);
				c.refreshQuestTab(4);
				c.sendMessage("@red@You gain 1 point for killing the Mage! You now have " + c.getArenaPoints()
						+ " Arena Points.");
			}
			if (npcs[i].npcType == 5744 || npcs[i].npcType == 5762) {
				c.setShayPoints(c.getShayPoints() + 1);
				c.sendMessage("@red@You gain 1 point for killing the Penance! You now have " + c.getShayPoints()
						+ " Assault Points.");
			}
			for (MonsterHunt.Npcs hunt : MonsterHunt.Npcs.values()) {
				if (npcs[i].npcType == hunt.getNpcId() && npcs[i].getHealth().getAmount() <= 0) {
					// Player p = PlayerHandler.players[npcs[i].killedBy];
					int randomPkp = Misc.random(15) + 10;
					c.pkp += randomPkp;
					c.refreshQuestTab(0);
					MonsterHunt.setCurrentLocation(null);
					c.sendMessage("Well done! You killed the monster currently being hunted.");
					c.sendMessage("You received: " + randomPkp + " PK Points for killing the monster being hunted.");
				}
			}
			int dropX = npcs[i].absX;
			int dropY = npcs[i].absY;
			int dropHeight = npcs[i].heightLevel;
			/*
			 * if (npcs[i].npcType == 494) { if (Boundary.isIn(c, Boundary.KRAKEN_CAVE)) {
			 * dropX = c.absX; dropY = c.absY; } else { dropX = 1770; dropY = 3426; } }
			 */
			if (npcs[i].npcType == 492 || npcs[i].npcType == 494) {
				dropX = c.absX;
				dropY = c.absY;
			}
			if (npcs[i].npcType == 2042 || npcs[i].npcType == 2043 || npcs[i].npcType == 2044
					|| npcs[i].npcType == 6720) {
				dropX = 2268;
				dropY = 3069;
				c.getZulrahEvent().stop();
			}
			/**
			 * Warriors guild
			 */
			c.getWarriorsGuild().dropDefender(npcs[i].absX, npcs[i].absY);
			if (AnimatedArmour.isAnimatedArmourNpc(npcs[i].npcType)) {

				if (npcs[i].getX() == 2851 && npcs[i].getY() == 3536) {
					dropX = 2851;
					dropY = 3537;
					AnimatedArmour.dropTokens(c, npcs[i].npcType, npcs[i].absX, npcs[i].absY + 1);
				} else if (npcs[i].getX() == 2857 && npcs[i].getY() == 3536) {
					dropX = 2857;
					dropY = 3537;
					AnimatedArmour.dropTokens(c, npcs[i].npcType, npcs[i].absX, npcs[i].absY + 1);
				} else {
					AnimatedArmour.dropTokens(c, npcs[i].npcType, npcs[i].absX, npcs[i].absY);
				}
			}
			Location3D location = new Location3D(dropX, dropY, dropHeight);
			int amountOfDrops = 1;
			if (Config.DOUBLE_DROPS && c.getLastIncentive() > 0
					&& (System.currentTimeMillis() - c.getLastIncentive()) < TimeUnit.DAYS.toMillis(1)) {
				amountOfDrops++;
			}
			Server.getDropManager().create(c, npcs[i], location, amountOfDrops);
			if (NPCDefinitions.get(npcs[i].npcType).getNpcCombat() >= 100) {
				c.getNpcDeathTracker().add(NPCDefinitions.get(npcs[i].npcType).getNpcName());
			}
		}
	}

	public void appendKillCount(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] kcMonsters = { 122, 49, 2558, 2559, 2560, 2561, 2550, 2551, 2552, 2553, 2562, 2563, 2564, 2565 };
			for (int j : kcMonsters) {
				if (npcs[i].npcType == j) {
					if (c.killCount < 20) {
						// c.killCount++;
						// c.sendMessage("Killcount: " + c.killCount);
					} else {
						// c.sendMessage("You already have 20 kill count");
					}
					break;
				}
			}
		}
	}

	public void appendBossKC(int i) {
		Player player = PlayerHandler.players[npcs[i].killedBy];
		NPC npc = npcs[i];
		if (npc == null || player == null) {
			return;
		}
		if (npc.getDefinition().getNpcCombat() >= 170) {
			Achievements.increase(player, AchievementType.SLAY_BOSSES, 1);
		}
	}

	/**
	 * Resets players in combat
	 */
	public static NPC getNpc(int npcType) {
		for (NPC npc : npcs)
			if (npc != null && npc.npcType == npcType)
				return npc;
		return null;
	}

	public static void spawnNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
			return;
		}
		NPCDefinitions definition = NPCDefinitions.get(npcType);
		NPC newNPC = new NPC(slot, npcType, definition);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.getHealth().setMaximum(HP);
		newNPC.getHealth().reset();
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	public static NPC spawn(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence, boolean attackPlayer) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
			return null;
		}
		NPCDefinitions definition = NPCDefinitions.get(npcType);
		NPC newNPC = new NPC(slot, npcType, definition);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.getHealth().setMaximum(HP);
		newNPC.getHealth().reset();
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		return newNPC;
	}

	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null)
				if (PlayerHandler.players[j].underAttackBy2 == i)
					PlayerHandler.players[j].underAttackBy2 = 0;
		}
	}

	public static NPC getNpc(int npcType, int x, int y) {
		for (NPC npc : npcs)
			if (npc != null && npc.npcType == npcType && npc.absX == x && npc.absY == y)
				return npc;
		return null;
	}

	public static NPC getNpc(int npcType, int x, int y, int height) {
		for (NPC npc : npcs) {
			if (npc != null && npc.npcType == npcType && npc.absX == x && npc.absY == y && npc.heightLevel == height) {
				return npc;
			}
		}
		return null;
	}

	public static NPC getNpc(int npcType, int height) {
		for (NPC npc : npcs) {
			if (npc != null && npc.npcType == npcType && npc.heightLevel == height) {
				return npc;
			}
		}
		return null;
	}

	/**
	 * Npc names
	 **/

	public String getNpcName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return "None";
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcName();
	}

	/**
	 * Npc Follow Player
	 **/

	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {
		case 2042:
		case 2043:
		case 2044:
		case 492:
		case 494:
		case 5535:
		case 2892:
		case 2894:
		case 1739:
		case 7413:
		case 1740:
		case 1741:
		case 1742:
		case 7288:
		case 7290:
		case 7292:
		case 7294:
		case 5867:
		case 5868:
		case 5869:
		case 7560:
		case 7559:
			return false;
		}
		return true;
	}

	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		Player player = PlayerHandler.players[playerId];
		if (PlayerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}
		if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)) {
			if (!Boundary.isIn(player, Boundary.CORPOREAL_BEAST_LAIR)) {
				npcs[i].killerId = 0;
				return;
			}
		}
		if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
			if (!Boundary.isIn(player, Boundary.GODWARS_BOSSROOMS)) {
				npcs[i].killerId = 0;
				return;
			}
		}
		if (Boundary.isIn(npcs[i], Zulrah.BOUNDARY)
				&& (npcs[i].npcType >= 2042 && npcs[i].npcType <= 2044 || npcs[i].npcType == 6720)) {
			return;
		}
		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}

		npcs[i].facePlayer(playerId);

		if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType == 7413
				|| npcs[i].npcType >= 7288 && npcs[i].npcType <= 7294) {
			return;
		}
		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		int followDistance = followDistance(i);
		double distance = ((double) distanceRequired(i)) + (npcs[i].getSize() > 1 ? 0.5 : 0.0);

		if (player.absX == npcs[i].absX && player.absY == npcs[i].absY) {
			stepAway(i);
			npcs[i].randomWalk = false;
			npcs[i].facePlayer(player.getIndex());
		}

		if (npcs[i].getDistance(playerX, playerY) <= distance)
			return;

		if ((npcs[i].spawnedBy > 0) || (npcs[i].absX < npcs[i].makeX + followDistance)
				&& (npcs[i].absX > npcs[i].makeX - followDistance) && (npcs[i].absY < npcs[i].makeY + followDistance)
				&& (npcs[i].absY > npcs[i].makeY - followDistance)) {
			if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
				if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
					NPCDumbPathFinder.follow(npcs[i], player);
				}
			}
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}

	public void loadSpell(Player player, int i) {
		int chance = 0;
		switch (npcs[i].npcType) {
			case 7931:
			case 7932:
			case 7933:
			case 7934:
			case 7935:
			case 7936:
			case 7937:
			case 7938:
			case 7939:
			case 7940:
				if (player != null) {
					if (npcs[i].getHealth().getAmount() < npcs[i].getHealth().getMaximum() /2) {
						int healchance = Misc.random(100);
						if (healchance > 75) {
							npcs[i].gfx0(1196);
							npcs[i].getHealth().setAmount(npcs[i].getHealth().getAmount() + (npcs[i].getHealth().getMaximum() / 4));
							Player killer = PlayerHandler.players[npcs[i].underAttackBy];
							killer.sendMessage("The revenant drains power from within and heals.");
							return;
						}
					}
					int randomHit = Misc.random(15);
					boolean distance = !player.goodDistance(npcs[i].absX, npcs[i].absY, player.getX(), player.getY(), 5);
					if (randomHit <= 5 && !distance) {
						npcs[i].attackType = CombatType.MELEE;
						npcs[i].projectileId = -1;
						npcs[i].endGfx = -1;
					} else if (randomHit >5 && randomHit <= 10 || distance) {
						npcs[i].attackType = CombatType.MAGE;
						npcs[i].projectileId = 1415;
						npcs[i].endGfx =-1;
					} else if (randomHit >10 &&randomHit <= 15 || distance) {
						npcs[i].attackType = CombatType.RANGE;
						npcs[i].projectileId = 1415;
						npcs[i].endGfx =-1;
					}
				}

				break;
		case 1605:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].endGfx = 78;
			break;
		case 5890:

			if (npcs[i].attackType != null) {
				switch (npcs[i].attackType) {
				case MAGE:
					npcs[i].endGfx = -1;
					npcs[i].projectileId = 1274;
					break;
				case MELEE:
					npcs[i].attackType = CombatType.MELEE;
					npcs[i].endGfx = -1;
					npcs[i].projectileId = -1;
					break;
				case SPECIAL:
					npcs[i].attackType = CombatType.SPECIAL;
					groundAttack(npcs[i], player, -1, 1284, -1, 5);
					npcs[i].attackTimer = 8;
					npcs[i].hitDelayTimer = 5;
					npcs[i].endGfx = -1;
					npcs[i].projectileId = -1;
					break;

				default:
					npcs[i].attackType = CombatType.MELEE;
					npcs[i].endGfx = -1;
					npcs[i].projectileId = -1;
					break;

				}
			}
			break;
		case 8028:
			if (Vorkath.attackStyle == 0) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].hitDelayTimer = 5;
				npcs[i].projectileId = 393;
			}
			break;
			case 7706:
					npcs[i].attackType = CombatType.RANGE;
					npcs[i].hitDelayTimer = 5;
					npcs[i].projectileId = 1375;
					npcs[i].endGfx = -1;

				break;
			/**
			 * Demonic Gorillas attack
			 */
			case 7144:
			case 7145:
			case 7146:
				if (npcs[i].attackType != null)
					switch (npcs[i].attackType) {
						case MAGE:
							npcs[i].attackType = CombatType.MAGE;
							npcs[i].endGfx = 1304;
							npcs[i].projectileId = 1305;
							break;

						case MELEE:
							npcs[i].attackType = CombatType.MELEE;
							npcs[i].endGfx = -1;
							npcs[i].projectileId = -1;
							break;

						case RANGE:
							npcs[i].attackType = CombatType.RANGE;
							npcs[i].endGfx = 1303;
							npcs[i].projectileId = 1302;
							break;

						case SPECIAL:
							npcs[i].attackType = CombatType.SPECIAL;
							groundAttack(npcs[i], player, 304, 303, 305, 5);
							npcs[i].attackTimer = 8;
							npcs[i].hitDelayTimer = 5;
							npcs[i].endGfx = -1;
							npcs[i].projectileId = -1;
							break;

						default:
							break;
					}
				break;

		case 320:
			if (npcs[i].getHealth().getStatus() == HealthStatus.POISON) {
				npcs[i].attackTimer *= 2;
			}
			break;
		case 498:
		case 499:
			npcs[i].projectileId = 642;
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].endGfx = -1;
			break;

		// Zilyana
		case 2205:
			if (Misc.random(3) == 0) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].endGfx = -1;
				npcs[i].projectileId = -1;
			} else {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].endGfx = -1;
				npcs[i].projectileId = -1;
			}
			break;
		// Growler
		case 2207:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 1203;
			break;
		// Bree
		case 2208:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 9;
			break;
		// Saradomin priest
		case 2209:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].endGfx = 76;
			break;
		// Saradomin ranger
		case 2211:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 20;
			npcs[i].endGfx = -1;
			break;
		// Saradomin mage
		case 2212:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 163;
			npcs[i].setProjectileDelay(2);
			break;
		// Graardor
		case 2215:
			if (Misc.random(4) == 0) {
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].projectileId = 174;
				npcs[i].endGfx = -1;
			} else {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			}
			break;

		case 3428:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 249;
			npcs[i].endGfx = -1;
			break;
		// Steelwill
		case 2217:
			npcs[i].projectileId = 1217;
			npcs[i].endGfx = 1218;
			npcs[i].attackType = CombatType.MAGE;
			break;
		// Grimspike
		case 2218:
			npcs[i].projectileId = 1193;
			npcs[i].endGfx = -1;
			npcs[i].attackType = CombatType.RANGE;
			break;
		// Bandos ranger
		case 2242:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 1197;
			npcs[i].endGfx = -1;
			break;
		// Bandos mage
		case 2244:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 165;
			npcs[i].endGfx = 166;
			break;
		// Saradomin ranger
		case 3160:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 20;
			npcs[i].endGfx = -1;
			break;
		// Zammorak mage
		case 3161:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 156;
			npcs[i].endGfx = 157;
			npcs[i].setProjectileDelay(2);
			break;
		// Armadyl boss
		case 3162:
			if (Misc.random(2) == 0) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1200;
			} else {
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].projectileId = 1199;
			}
			npcs[i].setProjectileDelay(1);
			break;
		// Skree
		case 3163:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 1192;
			npcs[i].endGfx = -1;
			break;
		// Geerin
		case 3164:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 1192;
			npcs[i].endGfx = -1;
			break;
		// Armadyl ranger
		case 3167:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 1192;
			npcs[i].endGfx = -1;
			break;
		// Armadyl mage
		case 3168:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 159;
			npcs[i].endGfx = 160;
			break;
		// Aviansie
		case 3174:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 1193;
			npcs[i].endGfx = -1;
			break;

		case 1672: // Ahrim
			npcs[i].attackType = Math.random() <= Barrows.SPECIAL_CHANCE ? CombatType.SPECIAL : CombatType.MAGE;
			switch (npcs[i].attackType) {

			case SPECIAL:
				npcs[i].projectileId = 156;
				npcs[i].endGfx = 400;
				break;

			case MAGE:
				npcs[i].projectileId = 156;
				npcs[i].endGfx = 157;
				break;

			default:
				npcs[i].projectileId = 156;
				npcs[i].endGfx = 157;
				break;
			}
			break;
		case 1675: // Karil
			npcs[i].projectileId = 27;
			npcs[i].attackType = CombatType.RANGE;
			break;

		case 2042:
			chance = 1;
			if (player != null) {
				if (player.getZulrahEvent().getStage() == 9) {
					chance = 2;
				}
			}
			chance = Misc.random(chance);
			npcs[i].setFacePlayer(true);
			if (chance < 2) {
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].projectileId = 97;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			} else {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 156;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			}
			break;

		case 1610:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].endGfx = 78;
			break;

		case 1611:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].endGfx = 76;
			break;

		case 1612:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].endGfx = 77;
			break;

		case 2044:
			npcs[i].setFacePlayer(true);
			if (Misc.random(3) > 0) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1046;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			} else {
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].projectileId = 1044;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 3;
				npcs[i].attackTimer = 4;
			}
			break;

		case 983: // Dagannoth mother Air
		case 6373:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].attackTimer = 4;
			npcs[i].projectileId = 159;
			npcs[i].endGfx = 160;
			break;

		case 984: // Dagannoth mother Water
		case 6375:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].attackTimer = 4;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 163;
			break;

		case 985: // Dagannoth mother Fire
		case 6376:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].attackTimer = 4;
			npcs[i].projectileId = 156;
			npcs[i].endGfx = 157;
			break;

		case 6378: // Mother Earth
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].attackTimer = 4;
			npcs[i].projectileId = 165;
			npcs[i].endGfx = 166;
			break;

		case 987: // Dagannoth mother range
		case 6377:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].attackTimer = 4;
			npcs[i].projectileId = 996;
			npcs[i].endGfx = -1;
			break;

		case 6371: // Karamel
			if (Misc.random(10) > 6) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].attackTimer = 6;
				npcs[i].endGfx = 369;
				npcs[i].forceChat("Semolina-Go!");
			} else {
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].attackTimer = 3;
				npcs[i].endGfx = -1;
			}
			break;

		case 6372: // Dessourt
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].attackTimer = 4;
			npcs[i].projectileId = 866;
			npcs[i].endGfx = 865;
			if (Misc.random(10) > 6) {
				npcs[i].forceChat("Hssssssssss");
			}
			break;

		case 6368: // Culinaromancer
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].attackTimer = 4;
			break;

		case 2043:
			npcs[i].setFacePlayer(false);
			npcs[i].turnNpc(player.getX(), player.getY());
			npcs[i].targetedLocation = new Location3D(player.getX(), player.getY(), player.heightLevel);
			npcs[i].attackType = CombatType.MELEE;
			npcs[i].attackTimer = 9;
			npcs[i].hitDelayTimer = 6;
			npcs[i].projectileId = -1;
			npcs[i].endGfx = -1;
			break;

		case 6611:
		case 6612:
			chance = Misc.random(100);
			int distanceToVet = player.distanceToPoint(npcs[i].absX, npcs[i].absY);
			if (distanceToVet < 3) {
				if (chance < 25) {
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].attackTimer = 7;
					npcs[i].hitDelayTimer = 4;
					groundSpell(npcs[i], player, 280, 281, "vetion", 4);
				} else if (chance > 90 && System.currentTimeMillis() - npcs[i].lastSpecialAttack > 15_000) {
					npcs[i].attackType = CombatType.SPECIAL;
					npcs[i].attackTimer = 5;
					npcs[i].hitDelayTimer = 2;
					npcs[i].lastSpecialAttack = System.currentTimeMillis();
				} else {
					npcs[i].attackType = CombatType.MELEE;
					npcs[i].attackTimer = 5;
					npcs[i].hitDelayTimer = 2;
				}
			} else {
				if (chance < 71) {
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].attackTimer = 7;
					npcs[i].hitDelayTimer = 4;
					groundSpell(npcs[i], player, 280, 281, "vetion", 4);
				} else if (System.currentTimeMillis() - npcs[i].lastSpecialAttack > 15_000) {
					npcs[i].attackType = CombatType.SPECIAL;
					npcs[i].attackTimer = 5;
					npcs[i].hitDelayTimer = 2;
					npcs[i].lastSpecialAttack = System.currentTimeMillis();
				} else {
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].attackTimer = 7;
					npcs[i].hitDelayTimer = 4;
					groundSpell(npcs[i], player, 280, 281, "vetion", 4);
				}
			}
			break;

		case 6914: // Lizardman, Lizardman brute
		case 6915:
		case 6916:
		case 6917:
		case 6918:
		case 6919:
			int randomAtt = Misc.random(1);
			if (randomAtt == 1) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1291;
				npcs[i].endGfx = -1;
				if (Misc.random(10) == 5) {
					player.getHealth().proposeStatus(HealthStatus.POISON, 3, Optional.of(npcs[i]));
				}
			} else {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			}
			break;

		/**
		 * Lizardman Shaman<
		 */
		case 6766:
			int randomAttack3 = Misc.random(100);
			if (randomAttack3 > 9 && randomAttack3 < 90) {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].attackTimer = 5;
				npcs[i].hitDelayTimer = 2;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			} else if (randomAttack3 > 89) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].hitDelayTimer = 3;
				npcs[i].projectileId = 1293;
				npcs[i].endGfx = 1294;

				if (Misc.random(5) == 5) {
					player.getHealth().proposeStatus(HealthStatus.POISON, 10, Optional.of(npcs[i]));
				}
			} else {
				npcs[i].attackType = CombatType.SPECIAL;
				npcs[i].attackTimer = 10;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
				npcs[i].hitDelayTimer = 8;
				groundSpell(npcs[i], player, -1, 1295, "spawns", 10);
			}
			break;

		/**
		 * Crazy Archaeologist
		 */
		case 6618:
			int randomAttack = Misc.random(10);
			String[] shout = { "I'm Bellock - respect me!", "Get off my site!", "No-one messes with Bellock's dig!",
					"These ruins are mine!", "Taste my knowledge!", "You belong in a museum!" };
			String randomShout = (shout[new Random().nextInt(shout.length)]);

			if (player.distanceToPoint(npcs[i].absX, npcs[i].absY) < 2) {
				npcs[i].forceChat(randomShout);
				if (randomAttack > 2 && randomAttack < 7) {
					npcs[i].attackType = CombatType.MELEE;
					npcs[i].attackTimer = 5;
					npcs[i].hitDelayTimer = 2;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
				} else if (randomAttack > 6) {
					npcs[i].attackType = CombatType.RANGE;
					npcs[i].hitDelayTimer = 3;
					npcs[i].projectileId = 1259;
					npcs[i].endGfx = 140;
				} else {
					npcs[i].forceChat("Rain of knowledge!");
					npcs[i].attackType = CombatType.SPECIAL;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
					npcs[i].hitDelayTimer = 3;
					groundSpell(npcs[i], player, 1260, 131, "archaeologist", 4);
				}
			} else {
				if (randomAttack > 3) {
					npcs[i].forceChat(randomShout);
					npcs[i].attackType = CombatType.RANGE;
					npcs[i].projectileId = 1259;
					npcs[i].endGfx = 140;
				} else {
					npcs[i].forceChat("Rain of knowledge!");
					npcs[i].attackType = CombatType.SPECIAL;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
					npcs[i].hitDelayTimer = 3;
					groundSpell(npcs[i], player, 1260, 131, "archaeologist", 4);
				}
			}
			break;

		/**
		 * Chaos fanatic
		 */
		case 6619:
			int randomAttack2 = Misc.random(10);
			String[] shout_chaos = { "Burn!", "WEUGH!", "Develish Oxen Roll!",
					"All your wilderness are belong to them!", "AhehHeheuhHhahueHuUEehEahAH",
					"I shall call him squidgy and he shall be my squidgy!" };
			String randomShoutChaos = (shout_chaos[new Random().nextInt(shout_chaos.length)]);

			npcs[i].forceChat(randomShoutChaos);

			if (player.distanceToPoint(npcs[i].absX, npcs[i].absY) < 2) {
				if (randomAttack2 > 2) {
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].hitDelayTimer = 3;
					npcs[i].projectileId = 1044;
					npcs[i].endGfx = 140;
				} else {
					npcs[i].attackType = CombatType.SPECIAL;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
					npcs[i].hitDelayTimer = 3;
					groundSpell(npcs[i], player, 1045, 131, "fanatic", 4);
				}
			} else {
				if (randomAttack2 > 3) {
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].projectileId = 1044;
					npcs[i].endGfx = 140;
				} else {
					npcs[i].attackType = CombatType.SPECIAL;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
					npcs[i].hitDelayTimer = 3;
					groundSpell(npcs[i], player, 1045, 131, "fanatic", 4);
				}
			}
			break;

		case 465:
			boolean distanceToWyvern = player.goodDistance(npcs[i].absX, npcs[i].absY, player.getX(), player.getY(), 3);
			int newRandom = Misc.random(10);
			if (newRandom >= 2) {
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].projectileId = 258;
				npcs[i].endGfx = -1;
			} else if (distanceToWyvern && newRandom == 1) {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			} else {
				npcs[i].attackType = CombatType.DRAGON_FIRE;
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			break;

		case 1046:
		case 1049:
			if (Misc.random(10) > 0) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].gfx100(194);
				npcs[i].projectileId = 195;
				npcs[i].endGfx = 196;
			} else {
				npcs[i].attackType = CombatType.SPECIAL;
				npcs[i].gfx100(194);
				npcs[i].projectileId = 195;
				npcs[i].endGfx = 576;

			}
			break;
		case 6610:
			if (Misc.random(15) > 0) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			} else {
				npcs[i].attackType = CombatType.SPECIAL;
				npcs[i].gfx0(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			break;

		case 6609:
			if (player != null) {
				int randomHit = Misc.random(20);
				boolean distance = !player.goodDistance(npcs[i].absX, npcs[i].absY, player.getX(), player.getY(), 5);
				if (randomHit < 15 && !distance) {
					npcs[i].attackType = CombatType.MELEE;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
				} else if (randomHit >= 15 && randomHit < 20 || distance) {
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].projectileId = 395;
					npcs[i].endGfx = 431;
				} else {
					npcs[i].attackType = CombatType.SPECIAL;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
				}
			}
			break;
		case 5535:
		case 494:
		case 492:
			npcs[i].attackType = CombatType.MAGE;
			if (Misc.random(5) > 0 && npcs[i].npcType == 494 || npcs[i].npcType == 5535) {
				npcs[i].gfx0(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			} else {
				npcs[i].gfx0(155);
				npcs[i].projectileId = 156;
				npcs[i].endGfx = 157;
			}
			break;
		case 2892:
			npcs[i].projectileId = 94;
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].endGfx = 95;
			break;
		case 2894:
			npcs[i].projectileId = 298;
			npcs[i].attackType = CombatType.RANGE;
			break;
		case 264:
		case 259:
		case 247:
		case 268:
		case 270:
		case 274:
		case 6593:
		case 273:
		case 2919:
		case 7273:
		case 7274:
		case 2918:
		case 7275:
			int random2 = Misc.random(2);
			if (random2 == 0) {
				npcs[i].projectileId = 393;
				npcs[i].endGfx = 430;
				npcs[i].attackType = CombatType.DRAGON_FIRE;
			} else {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			}
			break;
		case 239:
			int random = Misc.random(100);
			int distance = player.distanceToPoint(npcs[i].absX, npcs[i].absY);
			if (random >= 60 && random < 65) {
				npcs[i].projectileId = 394; // green
				npcs[i].endGfx = 429;
				npcs[i].attackType = CombatType.DRAGON_FIRE;
			} else if (random >= 65 && random < 75) {
				npcs[i].projectileId = 395; // white
				npcs[i].endGfx = 431;
				npcs[i].attackType = CombatType.DRAGON_FIRE;
			} else if (random >= 75 && random < 80) {
				npcs[i].projectileId = 396; // blue
				npcs[i].endGfx = 428;
				npcs[i].attackType = CombatType.DRAGON_FIRE;
			} else if (random >= 80 && distance <= 4) {
				npcs[i].projectileId = -1; // melee
				npcs[i].endGfx = -1;
				npcs[i].attackType = CombatType.MELEE;
			} else {
				npcs[i].projectileId = 393; // red
				npcs[i].endGfx = 430;
				npcs[i].attackType = CombatType.DRAGON_FIRE;
			}
			break;
		// arma npcs
		case 2561:
			npcs[i].attackType = CombatType.MELEE;
			break;
		case 2560:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 1190;
			break;
		case 2559:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 1203;
			break;
		case 2558:
			random = Misc.random(1);
			npcs[i].attackType = CombatType.getRandom(CombatType.RANGE, CombatType.MAGE);
			if (npcs[i].attackType == CombatType.RANGE) {
				npcs[i].projectileId = 1197;
			} else {
				npcs[i].projectileId = 1198;
			}
			break;
		// sara npcs
		case 2562: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random == 1)
				npcs[i].attackType = CombatType.MELEE;
			break;
		case 2563: // star
			npcs[i].attackType = CombatType.MELEE;
			break;
		case 2564: // growler
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 1203;
			break;
		case 2565: // bree
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 9;
			break;
		case 2551:
			npcs[i].attackType = CombatType.MELEE;
			break;
		case 2552:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 1203;
			break;
		case 2553:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 1206;
			break;
		case 2025:
			npcs[i].attackType = CombatType.MAGE;
			int r = Misc.random(3);
			if (r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if (r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if (r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if (r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;
		case 2265:// supreme
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 298;
			break;

		case 2266:// prime
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 477;
			break;

		case 2028:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 27;
			break;

		case 2054:
			int r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
			} else {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
		case 6257:// saradomin strike
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].endGfx = 76;
			break;
		case 6221:// zamorak strike
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].endGfx = 78;
			break;
		case 6231:// arma
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 1199;
			break;


			case 7692:
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].projectileId = 1382;
				break;
			case 7693:
				int r3 = Misc.random(1);
				if (r3 == 0) {
					npcs[i].attackType = CombatType.MELEE;
				} else {
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].projectileId = 1380;

				}
				break;
			case 7694:
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1381;
				break;
			case 7702:
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].projectileId = 1378;
				break;
			case 7699:
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 449;
				break;
			case 7708:
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 660;
				break;



			// sara npcs
		case 3129:
			random = Misc.random(15);
			if (random > 0 && random < 7) {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
			} else if (random >= 7) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1211;
			} else if (random == 0) {
				npcs[i].attackType = CombatType.SPECIAL;
				npcs[i].projectileId = -1;
			}
			break;
		case 3209:// cave horror
			random = Misc.random(3);
			if (random == 0 || random == 1) {
				npcs[i].attackType = CombatType.MELEE;
			} else {
				npcs[i].attackType = CombatType.MAGE;
			}
			break;
		case 3127:
		case 7700:
			int r23 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].spawnedBy].absX,
					PlayerHandler.players[npcs[i].spawnedBy].absY, 1)) {
				r23 = Misc.random(2);
			} else {
				r23 = Misc.random(1);
			}
			if (r23 == 0) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 448;
				npcs[i].endGfx = 157;
			} else if (r23 == 1) {
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
				npcs[i].hitDelayTimer = 6;
				npcs[i].attackTimer = 9;
			} else if (r23 == 2) {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			}
			break;
		case 3125:
			if (player.distanceToPoint(npcs[i].absX, npcs[i].absY) > 2) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 445;
				npcs[i].endGfx = 446;
			} else {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
			}
			break;

		case 3121:
		case 2167:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].projectileId = 443;
			break;
		case 1678:
		case 1679:
		case 1680:
		case 1683:
		case 1684:
		case 1685:
			npcs[i].attackType = CombatType.MELEE;
			npcs[i].attackTimer = 4;
			break;

		case 319:
			int corpRandom = Misc.random(15);
			if (corpRandom >= 12) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].hitDelayTimer = 3;
				npcs[i].projectileId = Misc.random(1) == 0 ? 316 : 314;
				npcs[i].endGfx = -1;
			}
			if (corpRandom >= 3 && corpRandom <= 11) {
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].hitDelayTimer = 2;
				npcs[i].endGfx = -1;
			}
			if (corpRandom <= 2) {
				npcs[i].attackType = CombatType.SPECIAL;
				npcs[i].hitDelayTimer = 3;
				groundSpell(npcs[i], player, 315, 317, "corp", 4);
			}
			break;

		/**
		 * Kalphite Queen Stage One
		 */
		case 963:
		case 965:
			int kqRandom = Misc.random(2);
			switch (kqRandom) {
			case 0:
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].hitDelayTimer = 3;
				npcs[i].projectileId = 280;
				npcs[i].endGfx = 281;
				break;
			case 1:
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].hitDelayTimer = 3;
				npcs[i].projectileId = 473;
				npcs[i].endGfx = 281;
				break;
			case 2:
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
				break;
			}
			break;
		/**
		 * Tekton
		 */
		case 7544:
			if (Objects.equals(tektonAttack, "MELEE")) {
				npcs[i].attackType = CombatType.MELEE;
			} else if (Objects.equals(tektonAttack, "SPECIAL")) {
				npcs[i].attackType = CombatType.SPECIAL;
				Tekton.tektonSpecial(player);
				tektonAttack = "MELEE";
				npcs[i].hitDelayTimer = 4;
				npcs[i].attackTimer = 8;
			}
			break;
		/**
		 * Glod
		 */
		case 5129:
			if (Objects.equals(glodAttack, "MELEE")) {
				npcs[i].attackType = CombatType.MELEE;
			} else if (Objects.equals(glodAttack, "SPECIAL")) {
				npcs[i].attackType = CombatType.SPECIAL;
				Glod.glodSpecial(player);
				glodAttack = "MELEE";
				npcs[i].hitDelayTimer = 4;
				npcs[i].attackTimer = 8;
			}
			break;

		/**
		 * Ice Queen
		 */
		case 4922:
			if (Objects.equals(queenAttack, "MAGIC")) {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = 367;
				npcs[i].hitDelayTimer = 5;
			} else if (Objects.equals(queenAttack, "SPECIAL")) {
				npcs[i].attackType = CombatType.SPECIAL;
				IceQueen.queenSpecial(player);
				queenAttack = "MAGIC";
				npcs[i].hitDelayTimer = 4;
				npcs[i].attackTimer = 8;
			}
			break;

		/**
		 * Tekton magers
		 */
		case 7617:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 1348;
			npcs[i].endGfx = 1345;
			npcs[i].hitDelayTimer = 5;
			npcs[i].attackTimer = 15;
			break;
			case 7529:
				if (Misc.random(10) == 5) {
					npcs[i].attackType = CombatType.SPECIAL;
					npcs[i].projectileId = 1348;
					npcs[i].endGfx = 1345;
					npcs[i].hitDelayTimer = 3;
				} else {
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].projectileId = 1348;
					npcs[i].endGfx = 1345;
					npcs[i].hitDelayTimer = 3;
				}
				break;

			case 7566:
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1289;
				npcs[i].endGfx = 1295;
				npcs[i].hitDelayTimer = 3;
				break;

			case 7559: // deathly ranger
				npcs[i].attackType = CombatType.RANGE;
				npcs[i].projectileId = 1120;
				//npcs[i].endGfx = 1345;
				npcs[i].hitDelayTimer = 3;
				break;

			case 7560:// deathly mage
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1348;
				npcs[i].endGfx = 1345;
				npcs[i].hitDelayTimer = 3;
				break;

			case 7554:
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 970;
				npcs[i].endGfx = 971;
				npcs[i].hitDelayTimer = 3;
				break;
		case 7604:
		case 7605:
		case 7606:
			if (Misc.random(10) == 5) {
				npcs[i].attackType = CombatType.SPECIAL;
				npcs[i].forceChat("RAA!");
				npcs[i].projectileId = 1348;
				npcs[i].endGfx = 1345;
				npcs[i].hitDelayTimer = 3;
			} else {
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1348;
				npcs[i].endGfx = 1345;
				npcs[i].hitDelayTimer = 3;
			}
			break;

		/**
		 * Cerberus
		 */
		case 5862:
			if (Objects.equals(player.CERBERUS_ATTACK_TYPE, "GROUND_ATTACK")) {
				startAnimation(4492, i);
				npcs[i].forceChat("Grrrrrrrrrrrrrr");
				npcs[i].attackType = CombatType.SPECIAL;
				npcs[i].hitDelayTimer = 4;
				groundSpell(npcs[i], player, -1, 1246, "cerberus", 4);
				player.CERBERUS_ATTACK_TYPE = "MELEE";
			}
			if (Objects.equals(player.CERBERUS_ATTACK_TYPE, "GHOST_ATTACK")) {
				startAnimation(4494, i);
				// npcs[i].forceChat("Aaarrrooooooo");
				player.CERBERUS_ATTACK_TYPE = "MELEE";
			}
			if (Objects.equals(player.CERBERUS_ATTACK_TYPE, "FIRST_ATTACK")) {
				startAnimation(4493, i);
				npcs[i].attackTimer = 5;
				player.CERBERUS_ATTACK_TYPE = "MELEE";
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					int ticks = 0;

					@Override
					public void execute(CycleEventContainer container) {
						if (player.disconnected) {
							stop();
							return;
						}
						switch (ticks++) {
						case 0:
							npcs[i].attackType = CombatType.MELEE;
							npcs[i].projectileId = -1;
							npcs[i].endGfx = -1;
							break;

						case 2:
							npcs[i].attackType = CombatType.RANGE;
							npcs[i].projectileId = 1245;
							npcs[i].endGfx = 1244;
							break;

						case 4:
							npcs[i].attackType = CombatType.MAGE;
							npcs[i].projectileId = 1242;
							npcs[i].endGfx = 1243;
							container.stop();
							break;
						}
						// System.out.println("Ticks - cerb " + ticks);
					}

					@Override
					public void stop() {

					}
				}, 2);
			} else {
				int randomStyle = Misc.random(2);

				switch (randomStyle) {
				case 0:
					npcs[i].attackType = CombatType.MELEE;
					npcs[i].projectileId = -1;
					npcs[i].endGfx = -1;
					break;

				case 1:
					npcs[i].attackType = CombatType.RANGE;
					npcs[i].projectileId = 1245;
					npcs[i].endGfx = 1244;
					break;

				case 2:
					npcs[i].attackType = CombatType.MAGE;
					npcs[i].projectileId = 1242;
					npcs[i].endGfx = 1243;
					break;
				}
			}
			break;

		case Skotizo.AWAKENED_ALTAR_NORTH:
		case Skotizo.AWAKENED_ALTAR_SOUTH:
		case Skotizo.AWAKENED_ALTAR_WEST:
		case Skotizo.AWAKENED_ALTAR_EAST:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].projectileId = 1242;
			npcs[i].endGfx = 1243;
			break;

		case Skotizo.SKOTIZO_ID:
			int randomStyle;
			if (player.getSkotizo().firstHit) {
				randomStyle = 1;
				player.getSkotizo().firstHit = false;
			} else {
				randomStyle = Misc.random(1);
			}
			switch (randomStyle) {
			case 0:
				npcs[i].attackType = CombatType.MELEE;
				npcs[i].projectileId = -1;
				npcs[i].endGfx = -1;
				break;

			case 1:
				npcs[i].attackType = CombatType.MAGE;
				npcs[i].projectileId = 1242;
				npcs[i].endGfx = 1243;
				break;
			}
			break;

		case 5867:
			npcs[i].attackType = CombatType.RANGE;
			npcs[i].hitDelayTimer = 3;
			npcs[i].projectileId = 1230;
			npcs[i].attackTimer = 15;
			break;

		case 5868:
			npcs[i].attackType = CombatType.MAGE;
			npcs[i].hitDelayTimer = 3;
			npcs[i].projectileId = 127;
			npcs[i].attackTimer = 15;
			break;

		case 5869:
			npcs[i].attackType = CombatType.MELEE;
			npcs[i].hitDelayTimer = 3;
			npcs[i].projectileId = 1248;
			npcs[i].attackTimer = 15;
			break;
		}
	}

	/**
	 * Distanced required to attack
	 **/
	public int distanceRequired(int i) {
		switch (npcs[i].npcType) {

		case Skotizo.SKOTIZO_ID:
			return npcs[i].attackType == CombatType.MAGE ? 15 : 2;
		case 7706:
		return 80;
		case 8028:
			return 10;
		/**
		 * Cerberus
		 */
		case 5862:
		case 6766:
		case 7144:
		case 7145:
		case 7146:
			return npcs[i].attackType == CombatType.MELEE ? 1 : 7;

		case 5867:
		case 5868:
		case 5869:
		case 7617:
			return 30;

		case 7559:
		case 7560:
		return 10;

		case 7604: // Skeletal mystic
		case 7605: // Skeletal mystic
		case 7606: // Skeletal mystic
		case 4922:
			return 8;

		case 319:
			return npcs[i].attackType == CombatType.MAGE ? 10 : 7;

		case 5890:
		case 7544:
		case 5129:
			return npcs[i].attackType == CombatType.MELEE ? 3 : 7;

		case 6914: // Lizardman, Lizardman brute
		case 6915:
		case 6916:
		case 6917:
		case 6918:
		case 6919:
			return npcs[i].attackType == CombatType.MAGE ? 4 : 1;

		case 6618:
			return npcs[i].attackType == CombatType.RANGE || npcs[i].attackType == CombatType.SPECIAL ? 4 : 1;

		case 465:
			return npcs[i].attackType == CombatType.RANGE || npcs[i].attackType == CombatType.SPECIAL ? 6 : 2;

		case 6615: // Scorpia
		case 6619: // Chaos fanatic
			return 4;

		case 6367:
		case 6368:
		case 6369:
		case 6371:
		case 6372:
		case 6373:
		case 6374:
		case 6375:
		case 6376:
		case 6377:
		case 6378:
			if (npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE)
				return 8;
			else
				return 4;

		case 6370:
			return 10;

		case 498:
		case 499:
			return 6;
		case 1672: // Ahrim
		case 1675: // Karil
		case 983: // Dagannoth mother
		case 984:
		case 985:
		case 987:
			return 8;

		case 986:
		case 988:
			return 3;

		case 2209:
		case 2211:
		case 2212:

		case 2242:
		case 2244:
		case 3160:
		case 3161:
		case 3162:
		case 3167:
		case 3168:
		case 3174:
			return 4;

		case 1610:
		case 1611:
		case 1612:
			return 4;

		case 2205:
			return npcs[i].attackType == CombatType.MAGE ? 3 : 2;
		case Zulrah.SNAKELING:
			return 2;
		case 2208:
			return 8;
		case 2215:
			return npcs[i].attackType == CombatType.MELEE ? 4 : 8;
		case 2217:
			return 9;
		case 2218:
			return 6;
		case 2042:
		case 2043:
		case 2044:
			case 7554:
			return 25;
		case 3163:
			return 8;
		case 3164:
		case 1049:
			return 5;
		case 6611:
		case 6612:
			return npcs[i].attackType == CombatType.SPECIAL || npcs[i].attackType == CombatType.MAGE ? 12 : 3;
		case 1046:
		case 6610:
			return 8;
		case 494:
		case 492:
		case 6609:
		case 5535:
			return 10;
		case 2025:
		case 2028:
			return 6;
		case 2562:
		case 3131:
		case 3132:
		case 3130:
		case 2206:
		case 2207:
		case 2267:
			return 2;
		case 2054:// chaos ele
		case 3125:
		case 3121:
		case 2167:
		case 3127:
			case 7700:
			return 8;
		case 3129:
			return 5;
		case 2265:// dag kings
		case 2266:
			return 4;
		case 239:
			return npcs[i].attackType == CombatType.DRAGON_FIRE ? 18 : 4;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 2558:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
			return 9;
		// things around dags
		case 2892:
		case 2894:
			return 10;
		default:
			return 1;
		}
	}

	public int followDistance(int i) {
		if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS) || Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)
				|| Boundary.isIn(npcs[i], Boundary.CERBERUS_BOSSROOMS)) {
			return 20;
		}
		switch (npcs[i].npcType) {
		case 2045:
			return 20;
		case 6615:
			return 30;
		case 1739:
		case 1740:
		case 1741:
		case 1742:
		case 7413:
		case 7288:
		case 7290:
		case 7292:
		case 7294:
			return -1;
		case 1678: // Barrows tunnel NPCs
		case 1679:
		case 1680:
		case 1683:
		case 1684:
		case 1685:
		case 484:
		case 7276:
		case 135:
		case 6914: // Lizardman, Lizardman brute
		case 6915:
		case 6916:
		case 6917:
		case 6918:
		case 6919:
			return 4;
		case 2209:
		case 2211:
		case 2212:
		case 2233:
		case 2234:
		case 2235:
		case 2237:
		case 2241:
		case 2242:
		case 2243:
		case 2244:
		case 2245:
		case 3133:
		case 3134:
		case 3135:
		case 3137:
		case 3138:
		case 3139:
		case 3140:
		case 3141:
		case 3159:
		case 3160:
		case 3161:
		case 3166:
		case 3167:
		case 3168:
			return 3;
		case 2205:
		case 2206:
		case 2207:
		case 2208:
		case 2215:
		case 2216:
		case 2217:
		case 2218:
		case 3129:
		case 3130:
		case 3131:
		case 3132:
		case 3162:
		case 3163:
		case 3164:
		case 3165:
			return 20;
		case 239:
			return 40;
		case 6611:
		case 6612:
		case 963:
		case 965:
		case 7544:
			return 15;
		case 5129:
		case 4922:
			return 9;
		case 2551:
		case 2562:
		case 2563:
			return 8;
		case 2054:
		case 5890:
		case 5916:
			return 10;
		case 2265:
		case 2266:
		case 2267:
			return 7;
		default:
			return 7;

		}

	}

	public int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 498:
			return 120;
		case 499:
			return 105;
		case 2265:
		case 2266:
		case 2054:
			return 85;

		case 3127:
			case 7700:
		case 7617:
			return 130;
		case 1672:
		case 239:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;
		case 3162:
			return 100;

		default:
			return 85;
		}
	}

	/**
	 * Npcs who ignores projectile clipping to ensure no safespots
	 * 
	 * @param i
	 * @return true is the npc is using range, mage or special
	 */
	public static boolean ignoresProjectile(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case 6611:
		case 6612:
		case 319:
		case 6618:
		case 6766:
		case 5862:
		case 963:
		case 965:
		case 7706:
		case 7144:
		case 8028:
		case 7145:
		case 7146:
		case 5890:
			return true;
		}
		return false;
	}

	/**
	 * NPC Attacking Player
	 **/
	public void attackPlayer(Player c, int i) {
		if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
			return;
		}
		if (c.isInvisible()) {
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		if (npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != c.getIndex()) {
				npcs[i].killerId = 0;
				npcs[i].facePlayer(0);
				npcs[i].underAttack = false;
				npcs[i].randomWalk = true;
				return;
			}
			if (!npcs[i].inMulti() && ((c.underAttackBy > 0 && c.underAttackBy2 != i)
					|| (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				npcs[i].facePlayer(0);
				npcs[i].underAttack = false;
				npcs[i].randomWalk = true;
				return;
			}
			if (npcs[i].heightLevel != c.heightLevel) {
				npcs[i].killerId = 0;
				npcs[i].facePlayer(0);
				npcs[i].underAttack = false;
				npcs[i].randomWalk = true;
				return;
			}
			switch (npcs[i].npcType) {
			case 1739:
			case 7413:
			case 1740:
			case 1741:
			case 1742:
			case 6600:
			case 7288:
			case 7290:
			case 7292:
			case 7294:
			case 6602:
				npcs[i].killerId = 0;
				npcs[i].facePlayer(0);
				npcs[i].underAttack = false;
				npcs[i].randomWalk = true;
				break;

			// case 5862:
			// if (Cerberus.ATTACK_TYPE == "FIRST_ATTACK") {
			// loadSpell(c, i);
			//
			// Cerberus.ATTACK_TYPE = "MELEE";
			// }
			// break;
			}
			if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS) ^ Boundary.isIn(c, Boundary.GODWARS_BOSSROOMS)) {
				npcs[i].killerId = 0;
				npcs[i].facePlayer(0);
				npcs[i].underAttack = false;
				npcs[i].randomWalk = true;
				return;
			}
			if (Boundary.isIn(npcs[i], Boundary.CORPOREAL_BEAST_LAIR)
					^ Boundary.isIn(c, Boundary.CORPOREAL_BEAST_LAIR)) {
				npcs[i].killerId = 0;
				npcs[i].facePlayer(0);
				npcs[i].underAttack = false;
				npcs[i].randomWalk = true;
				return;
			}
			npcs[i].facePlayer(c.getIndex());

			int distance = c.distanceToPoint(npcs[i].absX, npcs[i].absY);
			boolean hasDistance = npcs[i].getDistance(c.getX(), c.getY()) <= ((double) distanceRequired(i))
					+ (npcs[i].getSize() > 1 ? 0.5 : 0.0);

			/**
			 * Npc's who will ignore projectile clipping
			 */
			if (ignoresProjectile(i)) {
				if (distance < 10) {
					c.getPA().removeAllWindows();
					npcs[i].oldIndex = c.getIndex();
					c.underAttackBy2 = i;
					c.singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					loadSpell(c, i);
					startAnimation(getAttackEmote(i), i);
				}
			}

			if (hasDistance) {
				if (projectileClipping) {
					if (npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE) {
						int x1 = npcs[i].absX;
						int y1 = npcs[i].absY;
						int z = npcs[i].heightLevel;
						if (!PathChecker.isProjectilePathClear(x1, y1, z, c.absX, c.absY)
								&& !PathChecker.isProjectilePathClear(c.absX, c.absY, z, x1, y1)) {
							return;
						}
					}
				}

				if (c.respawnTimer <= 0) {
					/**
					 * Npcs who follow projectile clipping
					 */
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					if (npcs[i].attackType == null) {
						npcs[i].attackType = CombatType.MELEE;
					}
					loadSpell(c, i);
					npcs[i].oldIndex = c.getIndex();
					c.underAttackBy2 = i;
					c.singleCombatDelay2 = System.currentTimeMillis();
					startAnimation(getAttackEmote(i), i);
					c.getPA().removeAllWindows();
					if (npcs[i].attackType == CombatType.DRAGON_FIRE) {
						npcs[i].hitDelayTimer += 2;
						c.getCombat().absorbDragonfireDamage();
					}
					if (multiAttacks(i)) {
						startAnimation(getAttackEmote(i), i);
						multiAttackGfx(i, npcs[i].projectileId);
						npcs[i].oldIndex = c.getIndex();
						return;
					}
					if (npcs[i].projectileId > 0) {
						if(npcs[i].npcType == 7706) {
							NPC glyph = getNpc(7707,c.getHeight());
							if (glyph == null){
								return;
							}

							if (c.getInferno().isBehindGlyph()) {
								c.getInferno().behindGlyph=true;
								//c.sendMessage("is behind glyph");
							}
						}
						int nX = NPCHandler.npcs[i].getX() + offset(i);
						int nY = NPCHandler.npcs[i].getY() + offset(i);
						int pX = c.getX();
						int pY = c.getY();
						//c.sendMessage(pX + " "+ pY);
						int offX = (nX - pX) * -1;
						int offY = (nY - pY) * -1;
						int centerX = nX + npcs[i].getSize() / 2;
						int centerY = nY + npcs[i].getSize() / 2;
							c.getPA().createPlayersProjectile(centerX, centerY, offX, offY, 50, getProjectileSpeed(i),
									npcs[i].projectileId, getProjectileStartHeight(npcs[i].npcType, npcs[i].projectileId),
									getProjectileEndHeight(npcs[i].npcType, npcs[i].projectileId), -c.getIndex() - 1, 65,
									npcs[i].getProjectileDelay());
					}
					if (c.teleporting) {
						c.startAnimation(65535);
						c.teleporting = false;
						c.isRunning = false;
						c.gfx0(-1);
						c.startAnimation(-1);
					}
				}
			}
		}
	}

	public int offset(int i) {
		switch (npcs[i].npcType) {
		case 2044:
			return 0;
		case 6611:
		case 6612:
			return 3;
		case 6610:
			return 2;
		case 239:
			return 2;
		case 2265:
		case 2266:
			return 1;
		case 3127:
		case 3125:
			case 7700:
			return 1;
		}
		return 0;
	}

	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780;
	}

	private boolean prayerProtectionIgnored(int npcId) {
		switch (npcs[npcId].npcType) {
		case 1610:
		case 1611:
		case 1612:
		case 2205:
		case 2206:
		case 2207:
		case 2208:
		case 2215:
		case 2216:
		case 2217:
		case 2218:
		case 3129:
		case 3130:
		case 3131:
		case 3132:
		case 3162:
		case 3163:
		case 3164:
		case 3165:
		case 7617:
			return true;
		case 1672:
			return false;
		case 6611:
		case 6612:
		case 6609:
			return npcs[npcId].attackType == CombatType.MAGE || npcs[npcId].attackType == CombatType.SPECIAL;
		case 465:
			return npcs[npcId].attackType == CombatType.DRAGON_FIRE;
		}
		return false;
	}

	public void handleSpecialEffects(Player c, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (c != null) {
					if (c.playerLevel[5] > 0) {
						c.playerLevel[5]--;
						c.getPA().refreshSkill(5);
					}
				}
			}
		}

	}
	
	public static void playWAV(File file) 
	{
	    try
	    {
	        final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

	        clip.addLineListener(new LineListener()
	        {
	            @Override
	            public void update(LineEvent event)
	            {
	                if (event.getType() == LineEvent.Type.STOP)
	                    clip.close();
	            }
	        });

	        clip.open(AudioSystem.getAudioInputStream(file));
	        clip.start();
	    }
	    catch (Exception exc)
	    {
	        exc.printStackTrace(System.out);
	    }
	}
	
	
	//Duplicate method from Signlink.java in the client.
	public static final String getCacheDirectory() {
		File file = null;
		String separator = System.getProperty("file.separator");
		String cacheName = Configuration.CACHE_NAME;
		StringBuilder sb = new StringBuilder("."+separator + cacheName + separator);
		String cacheDir = sb.toString();
		file = new File(cacheDir);
		if (file.exists() || file.mkdir()) {
			return cacheDir;
		}
		return null;
	}

	public static void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
		//System.out.println("animId "+animId+" +int i"+i);
		if (animId == 2656) {
			//System.out.println("Mage Packet");
			//jadPlayer.getPA().sendMid("mage"); << Send sound to be handled by client.
			File wavFile = new File(getCacheDirectory() + "Sounds/mage.wav");
			playWAV(wavFile);
			}
		if (animId == 2652) {
			//System.out.println("Range Packet");
			//jadPlayer.getPA().sendMid("range"); << Send sound to be handled by client.
			File wavFile = new File(getCacheDirectory() + "Sounds/range.wav");
			playWAV(wavFile);
			}
	}

	public NPC[] getNPCs() {
		return npcs;
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public int getMaxHit(int i) {
		if (npcs[i] == null) {
			return 0;
		}
		switch (npcs[i].npcType) {
		case 7706:
		return 120;
		case 3021: // KBD Spiders
			return 7;
		case Skotizo.SKOTIZO_ID:
			return 38;
		case Skotizo.AWAKENED_ALTAR_NORTH:
		case Skotizo.AWAKENED_ALTAR_SOUTH:
		case Skotizo.AWAKENED_ALTAR_WEST:
		case Skotizo.AWAKENED_ALTAR_EAST:
			return 15;
		case Skotizo.REANIMATED_DEMON:
		case Skotizo.DARK_ANKOU:
			return 8;
		case 6914: // Lizardman, Lizardman brute
		case 6915:
		case 6916:
		case 6917:
			return 7;
		case 7617:
			return 30;
		case 6918:
		case 6919:
			return 11;
		case 2042:
		case 2043:
		case 2044:
			return 41;
		case 5862:
			return 23;
		case 499:
			return 21;
		case 498:
			return 12;
		case 5867:
		case 5868:
		case 5869:
			return 30;
		case 239:
			return npcs[i].attackType == CombatType.DRAGON_FIRE ? 50 : 20;
		case 465:
			return npcs[i].attackType == CombatType.DRAGON_FIRE ? 55 : 13;
		case 2208:
		case 2207:
		case 2206:
			return 16;
		case 319:
			return npcs[i].attackType == CombatType.MELEE ? 55 : npcs[i].attackType == CombatType.SPECIAL ? 35 : 49;
		case 320:
			return 10;
		case 3129:
			return npcs[i].attackType == CombatType.MELEE ? 60 : npcs[i].attackType == CombatType.SPECIAL ? 49 : 30;
		case 6611:
		case 6612:
			return npcs[i].attackType == CombatType.MELEE ? 30 : npcs[i].attackType == CombatType.MAGE ? 34 : 46;
		case 1046:
			return npcs[i].attackType == CombatType.MAGE ? 40 : 50;
		case 6610:
		case 7144:
		case 7145:
		case 7146:
			return 30;
		case 6609:
			return npcs[i].attackType == CombatType.SPECIAL ? 3 : npcs[i].attackType == CombatType.MAGE ? 60 : 40;
		case 6618:
			return npcs[i].attackType == CombatType.SPECIAL ? 23 : 15;
		case 6619:
			return npcs[i].attackType == CombatType.SPECIAL ? 31 : 25;
		case 2558:
			return npcs[i].attackType == CombatType.MAGE ? 38 : 68;
		case 2562:
			return 31;
		case 2215:
			return npcs[i].attackType == CombatType.MELEE ? 65 : 35;
		case 3162:
			return npcs[i].attackType == CombatType.RANGE ? 71 : npcs[i].attackType == CombatType.MAGE ? 21 : 15;
		case 963:
			return npcs[i].attackType == CombatType.MAGE ? 30 : 21;
		case 965:
			return npcs[i].attackType == CombatType.MAGE || npcs[i].attackType == CombatType.RANGE ? 30 : 21;
		}
		return npcs[i].maxHit == 0 ? 1 : npcs[i].maxHit;
	}

	@SuppressWarnings("resource")
	public boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			return false;
		}
		while (!EndOfFile && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]), Integer.parseInt(token3[4]),
							getNpcListHP(Integer.parseInt(token3[0])), Integer.parseInt(token3[5]),
							Integer.parseInt(token3[6]), Integer.parseInt(token3[7]));

				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public void startGame() {
		for (int i = 0; i < PuroPuro.IMPLINGS.length; i++) {
			newNPC(PuroPuro.IMPLINGS[i][0], PuroPuro.IMPLINGS[i][1], PuroPuro.IMPLINGS[i][2], 0, 1, -1, -1, -1, -1);
		}

		/**
		 * Random spawns
		 */
		int random_spawn = Misc.random(2);
		int x = 0;
		int y = 0;
		switch (random_spawn) {
		case 0:
			x = 2620;
			y = 4347;
			break;

		case 1:
			x = 2607;
			y = 4321;
			break;

		case 2:
			x = 2589;
			y = 4292;
			break;
		}
		newNPC(7302, x, y, 0, 1, -1, -1, -1, -1);
	}

	public int getNpcListHP(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return 0;
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcHealth();

	}

	public String getNpcListName(int npcId) {
		if (npcId <= -1) {
			return "?";
		}
		if (NPCDefinitions.getDefinitions()[npcId] == null) {
			return "?";
		}
		return NPCDefinitions.getDefinitions()[npcId].getNpcName();
	}

	private void loadNPCSizes(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new RuntimeException("ERROR: " + fileName + " does not exist.");
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				int npcId, size;
				try {
					npcId = Integer.parseInt(line.split("\t")[0]);
					size = Integer.parseInt(line.split("\t")[1]);
					if (npcId > -1 && size > -1) {
						if (NPCDefinitions.getDefinitions()[npcId] == null) {
							NPCDefinitions.create(npcId, "None", 0, 0, size);
						} else {
							NPCDefinitions.getDefinitions()[npcId].setSize(size);
						}
					}
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean loadNPCList(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		File file = new File("./" + fileName);
		if (!file.exists()) {
			throw new RuntimeException("ERROR: NPC Configuration file does not exist.");
		}
		try (BufferedReader characterfile = new BufferedReader(new FileReader("./" + fileName))) {
			while ((line = characterfile.readLine()) != null && !line.equals("[ENDOFNPCLIST]")) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {
					token = line.substring(0, spot);
					token = token.trim();
					token2 = line.substring(spot + 1);
					token2 = token2.trim();
					token2_2 = token2.replaceAll("\t\t", "\t");
					token2_2 = token2_2.replaceAll("\t\t", "\t");
					token2_2 = token2_2.replaceAll("\t\t", "\t");
					token2_2 = token2_2.replaceAll("\t\t", "\t");
					token2_2 = token2_2.replaceAll("\t\t", "\t");
					token3 = token2_2.split("\t");
					if (token.equals("npc")) {
						newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]),
								Integer.parseInt(token3[3]));
					}
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
		return true;
	}

	public static void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPCDefinitions definition = NPCDefinitions.get(npcType);
		NPC newNPC = new NPC(slot, npcType, definition);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.getHealth().setMaximum(HP);
		newNPC.getHealth().reset();
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	public static NPCDef[] getNpcDef() {
		return npcDef;
	}

	private ArrayList<int[]> vetionSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> archSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> fanaticSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> corpSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> olmSpellCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> explosiveSpawnCoordinates = new ArrayList<>(3);
	private ArrayList<int[]> cerberusGroundCoordinates = new ArrayList<>(3);

	private void groundSpell(NPC npc, Player player, int startGfx, int endGfx, String coords, int time) {
		if (player == null) {
			return;
		}
		switch (coords) {
		case "vetion":
			player.coordinates = vetionSpellCoordinates;
			break;

		case "archaeologist":
			player.coordinates = archSpellCoordinates;
			break;

		case "fanatic":
			player.coordinates = fanaticSpellCoordinates;
			break;

		case "corp":
			player.coordinates = corpSpellCoordinates;
			break;

		case "olm":
			player.coordinates = olmSpellCoordinates;
			break;

		case "spawns":
			player.coordinates = explosiveSpawnCoordinates;

			List<NPC> exploader = Arrays.asList(NPCHandler.npcs);
			if (exploader.stream().filter(Objects::nonNull).anyMatch(n -> n.npcType == 6768 && !n.isDead)) {
				return;
			}
			break;

		case "cerberus":
			player.coordinates = cerberusGroundCoordinates;
			break;
		}
		int x = player.getX();
		int y = player.getY();
		player.coordinates.add(new int[] { x, y });
		for (int i = 0; i < 2; i++) {
			player.coordinates.add(new int[] { (x - 1) + Misc.random(3), (y - 1) + Misc.random(3) });
		}
		for (int[] point : player.coordinates) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;
			if (startGfx > 0) {
				player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, getProjectileSpeed(npc.getIndex()),
						startGfx, 31, 0, -1, 5);
			}
			if (Objects.equals(coords, "spawns")) {
				spawnNpc(6768, point[0], point[1], 0, 0, -1, -1, -1, -1);
			}

		}
		if (Objects.equals(coords, "spawns")) {
			CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					kill(6768, 0);
					container.stop();
				}

			}, 7);
		}
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : player.coordinates) {
					int x2 = point[0];
					int y2 = point[1];
					if (endGfx > 0) {
						player.getPA().createPlayersStillGfx(endGfx, x2, y2, player.heightLevel, 5);
					}
					if (Objects.equals(coords, "cerberus")) {
						player.getPA().createPlayersStillGfx(1247, x2, y2, player.heightLevel, 5);
					}
				}
				player.coordinates.clear();
				container.stop();
			}

		}, time);
	}

	private ArrayList<int[]> gorillaBoulder = new ArrayList<>(1);

	private void groundAttack(NPC npc, Player player, int startGfx, int endGfx, int explosionGfx, int time) {
		if (player == null) {
			return;
		}
		player.totalMissedGorillaHits = 3;
		player.coordinates = gorillaBoulder;
		int x = player.getX();
		int y = player.getY();
		player.coordinates.add(new int[] { x, y });

		for (int[] point : player.coordinates) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;
			if (startGfx > 0)
				player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, getProjectileSpeed(npc.getIndex()),
						startGfx, 31, 0, -1, 5); // 304
		}
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : player.coordinates) {
					int x2 = point[0];
					int y2 = point[1];
					if (endGfx > 0)
						player.getPA().createPlayersStillGfx(endGfx, x2, y2, player.heightLevel, 5); // 303
				}
				container.stop();
			}

		}, 3);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : player.coordinates) {
					int x2 = point[0];
					int y2 = point[1];
					if (explosionGfx > 0)
						player.getPA().createPlayersStillGfx(explosionGfx, x2, y2, player.heightLevel, 5); // 305
				}
				npc.attackType = CombatType.getRandom(CombatType.MELEE, CombatType.RANGE, CombatType.MAGE);
				player.coordinates.clear();
				container.stop();
			}

		}, time);
	}

	public static void kill(int npcType, int height) {
		Arrays.stream(npcs).filter(Objects::nonNull).filter(n -> n.npcType == npcType && n.heightLevel == height)
				.forEach(npc -> npc.isDead = true);
	}

	private void createVetionEarthquake(Player player) {
		player.getPA().shakeScreen(3, 2, 3, 2);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getPA().sendFrame107();
				container.stop();
			}

		}, 4);
	}

}
