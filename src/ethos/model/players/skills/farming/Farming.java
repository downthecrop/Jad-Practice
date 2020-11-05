package ethos.model.players.skills.farming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.entity.HealthStatus;
import ethos.model.players.Boundary;
import ethos.model.players.ClientGameTimer;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Skill;
import ethos.util.Misc;


/**
 * 
 * @author Jason http://www.rune-server.org/members/jason
 * @Revamped majorly by Tyler or imtyler7 on Rune-Server
 * @date Oct 27, 2013
 */

public class Farming {
	
	public static int[] farmersOutfit = { 13640, 13642, 13644, 13646 };
	
	public static final int MAX_PATCHES = 8;
	private Player player;
	private int weeds;
	
	public Farming(Player player) {
		this.player = player;
	}
	private long lastPoisonBerryFarm;
	
	private boolean hasMagicSecateurs() {
		return player.getItems().playerHasItem(7409) || player.getItems().isWearingItem(7409, 3) || SkillcapePerks.FARMING.isWearing(player) || SkillcapePerks.isWearingMaxCape(player);
	}
	public void handleCompostAdd() {
			if (player.getItems().playerHasItem(FarmingConstants.WEED)) {
				player.compostBin += player.getItems().getItemAmount(FarmingConstants.WEED);
				player.sendMessage("You have added "+player.getItems().getItemAmount(FarmingConstants.WEED)+" weed(s) to the compost bin.");
				player.sendMessage("You have "+player.compostBin+" buckets worth of compost left in the compost bin.");
				player.getItems().deleteItem2(FarmingConstants.WEED, player.getItems().getItemAmount(FarmingConstants.WEED));
			} else {
				player.sendMessage("You have no weeds to add to the compost bin. You currently have "+player.compostBin+" buckets worth of compost.");
			}
	}
	public void handleCompostRemoval() {
		if (!player.getItems().playerHasItem(FarmingConstants.BUCKET)) {
			
			player.sendMessage("You must have a bucket to take compost out of the bin!");
			
		} else if (player.compostBin <= 0) {
			
			player.sendMessage("You have no compost in the compost bin!");
			
		} else if (player.getItems().playerHasItem(FarmingConstants.BUCKET) && player.getItems().getItemAmount(FarmingConstants.BUCKET) <= player.compostBin) {
			
			//int bucketsTotal = player.getItems().getItemAmount(FarmingConstants.BUCKET);
			player.compostBin -= 1;
			player.sendMessage("You have filled a bucket of compost.");
			if (player.compostBin % 5 == 0)
				player.sendMessage("You have "+player.compostBin+" buckets worth of compost left in the compost bin.");
			player.getItems().deleteItem2(FarmingConstants.BUCKET, 1);
			player.getItems().addItem(FarmingConstants.COMPOST, 1);
			if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
				player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.COMPOST_BUCKET);
			}
			
		} else if (player.getItems().playerHasItem(FarmingConstants.BUCKET) && player.getItems().getItemAmount(FarmingConstants.BUCKET) > player.compostBin) {
			
			int compostTotal = player.compostBin;
			player.compostBin -= compostTotal;
			player.sendMessage("You have filled "+compostTotal+" buckets of compost.");
			player.sendMessage("You have ran out of compost in the compost bin!");
			player.getItems().deleteItem2(FarmingConstants.BUCKET, compostTotal);
			player.getItems().addItem(FarmingConstants.COMPOST, compostTotal);
			if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
				player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.COMPOST_BUCKET);
			}
			
		} else {
			
			player.sendMessage("Code Error - Please report this!");
			
		}
	}
	public boolean patchObjectInteraction(final int objectId, final int itemId, final int x, final int y) {
		
		/**
		 * Skilling outfit pieces
		 */
		int pieces = 0;
		for (int aFarmersOutfit : farmersOutfit) {
			if (player.getItems().isWearingItem(aFarmersOutfit)) {
				pieces++;
			}
		}
		
		Patch patch = Patch.get(x, y);
		if(patch == null)
			return false;
		final int id = patch.getId();
		player.turnPlayerTo(x, y);
		if(objectId == FarmingConstants.GRASS_OBJECT || objectId == FarmingConstants.HERB_PATCH_DEPLETED //Object Ids
		|| objectId == FarmingConstants.HERB_GROWING_OBJECT1 || objectId == FarmingConstants.HERB_GROWING_OBJECT2
		|| objectId == FarmingConstants.HERB_GROWING_OBJECT3 || objectId == FarmingConstants.HERB_GROWING_OBJECT4) {
			if(player.getFarmingState(id) < State.RAKED.getId()) {
				if(!player.getItems().playerHasItem(FarmingConstants.RAKE, 1)) {
					player.sendMessage("You need to rake this patch to remove all the weeds.", 600000);
					return true;
				}
/*				if (id >= 4 && player.amountDonated < 150) {
					player.sendMessage("You must be a Legendary Donator to use the 4 middle patches.", 600000);
					return true;
				}*/
				else if (itemId == FarmingConstants.RAKE || player.getItems().playerHasItem(FarmingConstants.RAKE)) {
					if (player.stopPlayerSkill)
						return true;
					player.stopPlayerSkill = true;
					player.startAnimation(FarmingConstants.RAKING_ANIM);
					player.turnPlayerTo(x, y);
					if(weeds <= 0)
						weeds = 3;
					CycleEventHandler.getSingleton().stopEvents(this);
					CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if(player == null) {
								container.stop();
								return;
							}
							if (!player.stopPlayerSkill) {
								container.stop();
								return;
							}
							if(weeds > 0) {
								weeds--;
								player.turnPlayerTo(x, y);
								player.getItems().addItem(FarmingConstants.WEED, 1);
								player.startAnimation(FarmingConstants.RAKING_ANIM);
							} else if(weeds == 0) {
								player.setFarmingState(id, State.RAKED.getId());
								player.sendMessage("You raked the patch of all it's weeds, now the patch is ready for compost.", 255);
								player.startAnimation(65535);
								updateObjects();
								container.stop();
							}
						}
	
						@Override
						public void stop() {
							player.stopPlayerSkill = false;
						}
						
					}, 3);
				}
			} else if(player.getFarmingState(id) >= State.RAKED.getId() && player.getFarmingState(id) < State.COMPOST.getId()) {
				if(!player.getItems().playerHasItem(FarmingConstants.COMPOST, 1))
					player.sendMessage("You need to put compost on this to enrich the soil.", 600000);
				else if (itemId == FarmingConstants.COMPOST || player.getItems().playerHasItem(FarmingConstants.COMPOST) && itemId == -1) {
					player.turnPlayerTo(x, y);
					player.startAnimation(FarmingConstants.PUTTING_COMPOST);
					player.getItems().deleteItem2(FarmingConstants.COMPOST, 1);
					player.getItems().addItem(FarmingConstants.BUCKET, 1);
					player.setFarmingState(id, State.COMPOST.getId());
					player.sendMessage("You put compost on the soil, it is now time to seed it.", 255);
				}
			} else if(player.getFarmingState(id) >= State.COMPOST.getId() && player.getFarmingState(id) < State.SEEDED.getId()) {
				if(!player.getItems().playerHasItem(FarmingConstants.SEED_DIBBER, 1)) {
					player.sendMessage("You need to use a seed on this patch with a seed dibber in your inventory.", 600000);
					return true;
				}
				final FarmingHerb.Herb herb = FarmingHerb.getHerbForSeed(itemId);
				if (herb == null) {
					player.sendMessage("You must use an appropriate seed on the patch at this stage.", 600000);
					return true;
				}
				if (player.getLevelForXP(player.playerXP[Skill.FARMING.getId()]) < herb.getLevelRequired()) {
					player.sendMessage("You need a farming level of "+herb.getLevelRequired()+" to grow "+herb.getSeedName().replaceAll(" seed", "")+".", 600000);
					return true;
				}
				if (itemId == herb.getSeedId() && player.getItems().playerHasItem(FarmingConstants.SEED_DIBBER)) {
					if (player.stopPlayerSkill)
						return true;
					player.stopPlayerSkill = true;
					player.turnPlayerTo(x, y);
					player.startAnimation(FarmingConstants.SEED_DIBBING);
					/**
					 * Calculate experience
					 */
					double osrsExperience = herb.getPlantingXp() + herb.getPlantingXp() / 20 * pieces;
					double regExperience = herb.getPlantingXp() * (Config.FARMING_EXPERIENCE + (hasMagicSecateurs() ? 1 : 0)) + herb.getPlantingXp() * Config.FARMING_EXPERIENCE / 20 * pieces;
					if (player.debugMessage)
					System.out.println("Plant xp: " + herb.getPlantingXp() * (Config.FARMING_EXPERIENCE + (hasMagicSecateurs() ? 5 : 0)) + herb.getPlantingXp() + ", Pieces: " + pieces);
					CycleEventHandler.getSingleton().stopEvents(this);
					CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

						@Override
						public void execute(CycleEventContainer container) {
							if(player == null) {
								container.stop();
								return;
							}
							if (!player.stopPlayerSkill) {
								container.stop();
								return;
							}
							if(!player.getItems().playerHasItem(herb.getSeedId())) {
								container.stop();
								return;
							}
							player.getItems().deleteItem2(herb.getSeedId(), 1);
							player.setFarmingState(id, State.SEEDED.getId());
							player.setFarmingSeedId(id, herb.getSeedId());
							player.setFarmingTime(id, hasMagicSecateurs() ? herb.getGrowthTime() / 2 : herb.getGrowthTime());
							player.setOriginalFarmingTime(id, hasMagicSecateurs() ? herb.getGrowthTime() / 2 : herb.getGrowthTime());
							player.setFarmingHarvest(id, 3 + Misc.random(hasMagicSecateurs() ? 7 : 4));
							player.getPA().addSkillXP((int) (player.getMode().getType().equals(ModeType.OSRS) ? osrsExperience : regExperience), 19, true);
							player.sendMessage("You dib a seed into the soil, it is now time to water it.", 255);
							updateObjects();
							container.stop();
						}

						@Override
						public void stop() {
							player.stopPlayerSkill = false;
						}
						
					}, 3);
				}
			} else if(player.getFarmingState(id) >= State.SEEDED.getId() && player.getFarmingState(id) < State.GROWTH.getId()) {
				if(!player.getItems().playerHasItem(FarmingConstants.WATERING_CAN, 1))
					player.sendMessage("You need to water the herb before you can harvest it.", 600000);
				else if (itemId == FarmingConstants.WATERING_CAN || player.getItems().playerHasItem(FarmingConstants.WATERING_CAN) && itemId == -1) {
					int time = (int) Math.round(player.getFarmingTime(id) * .6);
					player.turnPlayerTo(x, y);
					player.startAnimation(FarmingConstants.WATERING_CAN_ANIM);
					player.setFarmingState(id, State.GROWTH.getId());
					player.sendMessage("You water the herb, wait "+((int) (player.getFarmingTime(id) * .6))+" seconds for the herb to mature.", 255);
					player.getPA().sendGameTimer(ClientGameTimer.FARMING, TimeUnit.SECONDS, time);
					return true;
				}
			} else if(player.getFarmingState(id) == State.GROWTH.getId()) {
				if(player.getFarmingTime(id) > 0) {
					player.sendMessage("You need to wait another "+((int) (player.getFarmingTime(id) * .6))+" seconds until the herb is mature.", 600000);
					return true;
				}
			}
			return true;
		} else if(objectId == FarmingConstants.HERB_OBJECT) {
			if(player.getFarmingState(id) == State.HARVEST.getId()) {
				if(player.getItems().freeSlots() < 1) {
					player.getDH().sendStatement("You need atleast 1 free space to harvest some herbs.");
					player.nextChat = -1;
					return true;
				}
				if(player.getFarmingHarvest(id) == 0 || player.getFarmingState(id) != State.HARVEST.getId()) {
					resetValues(id);
					updateObjects();
					return true;
				}
				final FarmingHerb.Herb herb = FarmingHerb.getHerbForSeed(player.getFarmingSeedId(id));
				/**
				 * Experience calculation
				 */
				double osrsHarvestExperience = herb.getHarvestingXp() + herb.getHarvestingXp() / 5 * pieces;
				double regHarvestExperience = herb.getHarvestingXp() * Config.FARMING_EXPERIENCE + herb.getHarvestingXp() * Config.FARMING_EXPERIENCE / 5 * pieces;
				if(player.debugMessage)
					System.out.println("Harvest xp: " + herb.getHarvestingXp() * Config.FARMING_EXPERIENCE + herb.getHarvestingXp() + ", Pieces: " + pieces);
				if(herb != null) {
					if (player.stopPlayerSkill)
						return true;
					player.stopPlayerSkill = true;
					player.startAnimation(FarmingConstants.PICKING_HERB_ANIM);
					player.sendMessage("You start picking your herbs...", 600000);
					CycleEventHandler.getSingleton().stopEvents(this);
					CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

						@Override
						public void execute(CycleEventContainer container) {
							if(player == null) {
								container.stop();
								return ;
							}
							if (!player.stopPlayerSkill) {
								container.stop();
								return;
							}
							if(player.getItems().freeSlots() < 1) {
								player.getDH().sendStatement("You need atleast 1 free space to harvest some herbs.");
								player.nextChat = -1;
								player.startAnimation(65535);
								container.stop();
								return;
							}
							if(player.getFarmingHarvest(id) <= 0) {
								player.sendMessage("The herb patch has completely depleted...", 600000);
								Achievements.increase(player, AchievementType.FARM, 1);
								player.startAnimation(65535);
								resetValues(id);
								updateObjects();
								container.stop();
								return;
							}
							switch (herb) {
							case AVANTOE:
								break;
							case CADANTINE:
								break;
							case DWARF_WEED:
								break;
							case GUAM:
								break;
							case HARRALANDER:
								break;
							case IRIT:
								break;
							case KWUARM:
								break;
							case LANTADYME:
								break;
							case MARRENTIL:
								break;
							case RANARR:
								break;
							case SNAP_DRAGON:
								DailyTasks.increase(player, PossibleTasks.SNAPDRAGONS);
								break;
							case TARROMIN:
								break;
							case TOADFLAX:
								break;
							case TORSTOL:
								if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
									player.getDiaryManager().getFaladorDiary()
											.progress(FaladorDiaryEntry.HARVEST_TORSTOL);
								}
								break;
							default:
								break;
							
							}
							player.startAnimation(FarmingConstants.PICKING_HERB_ANIM);
							player.setFarmingHarvest(id , player.getFarmingHarvest(id) - 1);
							//if (perk)
								//player.getItems().addItem(herb.getGrimyId()+1, 1);
							//else
							player.getItems().addItem(herb.getGrimyId(), 1);
							player.getPA().addSkillXP((int) (player.getMode().getType().equals(ModeType.OSRS) ? osrsHarvestExperience : regHarvestExperience), 19, true);
							if (Misc.random(herb.getPetChance()) == 20 && player.getItems().getItemCount(20661, false) == 0 && player.summonId != 20661) {
								 PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + player.playerName + "</col> harvested some crops and found <col=CC0000>Tangleroot</col> pet!");
								 player.getItems().addItemUnderAnyCircumstance(20661, 1);
							 }
						}

						@Override
						public void stop() {
							player.stopPlayerSkill = false;
						}
						
					}, 3);
				}
			}
			return true;
		}
		return false;
	}
	
	public void farmingProcess() {
		for(int i = 0; i < Farming.MAX_PATCHES; i++) {
	        if (player.getFarmingTime(i) > 0 && player.getFarmingState(i) == Farming.State.GROWTH.getId()) {
	        	player.setFarmingTime(i, player.getFarmingTime(i) - 1);
 				if (player.getFarmingTime(i) > player.getOriginalFarmingTime(i) * .75) {
					if (!player.farmingLagReducer[i]) {
						updateObjects();
						player.farmingLagReducer[i] = true;
					}
				} else if (player.getFarmingTime(i) > player.getOriginalFarmingTime(i) * .50 && player.getFarmingTime(i) < player.getOriginalFarmingTime(i) * .75) {
					player.farmingLagReducer[i] = false;
					if (!player.farmingLagReducer2[i]) {
						updateObjects();
						player.farmingLagReducer2[i] = true;
					}
				} else if (player.getFarmingTime(i) > player.getOriginalFarmingTime(i) * .25 && player.getFarmingTime(i) < player.getOriginalFarmingTime(i) * .50) {
					player.farmingLagReducer2[i] = false;
					if (!player.farmingLagReducer3[i]) {
						updateObjects();
						player.farmingLagReducer3[i] = true;
					}
				} else if (player.getFarmingTime(i) < player.getOriginalFarmingTime(i) * .25 && player.getFarmingTime(i) != 0) {
					player.farmingLagReducer[i] = false;
					if (!player.farmingLagReducer4[i]) {
						updateObjects();
						player.farmingLagReducer4[i] = true;
					}
				} else
	        	if(player.getFarmingTime(i) == 0) {
					player.farmingLagReducer4[i] = false;
	        		FarmingHerb.Herb herb = FarmingHerb.getHerbForSeed(player.getFarmingSeedId(i));
	        		if(herb != null)
	        			player.sendMessage("Your farming patch of "+herb.getSeedName().replaceAll(" seed", "")+" is ready to be harvested.", 255);
	        		player.setFarmingState(i, Farming.State.HARVEST.getId());
					updateObjects();
	        	}
	        }
		}
	}
	
	public void farmPoisonBerry() {
		if (System.currentTimeMillis() - lastPoisonBerryFarm < TimeUnit.MINUTES.toMillis(5)) {
			player.sendMessage("You can only pick berries from this bush every 5 minutes.");
			return;
		}
		int level = player.playerLevel[Skill.FARMING.getId()];
		if (level < 70) {
			player.sendMessage("You need a farming level of 70 to get this.");
			return;
		}
		if (player.getItems().freeSlots() < (hasMagicSecateurs() ? 4 : 2)) {
			player.sendMessage("You need at least " + (hasMagicSecateurs() ? 4 : 2) + " free slots to do this.");
			return;
		}
		int maximum = player.getLevelForXP(player.playerXP[Skill.FARMING.getId()]);
		if (Misc.random(100) < (10 + (maximum - level))) {
			player.getHealth().proposeStatus(HealthStatus.POISON, 6, Optional.empty());
		}
		if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
			player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.PICK_POSION_BERRY);
		}
		player.startAnimation(881);
		lastPoisonBerryFarm = System.currentTimeMillis();
		player.getItems().addItem(6018, hasMagicSecateurs() ? 4 : 2);
		player.getPA().addSkillXP(player.getMode().getType().equals(ModeType.OSRS) ? 50 : 2_000, Skill.FARMING.getId(), true);

	}
	
	public void resetValues(int id) {
		player.setFarmingHarvest(id, 0);
		player.setFarmingSeedId(id, 0);
		player.setFarmingState(id, 0);
		player.setFarmingTime(id, 0);
		player.setOriginalFarmingTime(id, 0);
	}
	
	public void updateObjects() {
		for(int i = 0; i < Farming.MAX_PATCHES; i++) {
			Patch patch = Patch.get(i);
			if(patch == null)
				continue;
			if(player.distanceToPoint(patch.getX(), patch.getY()) > 60)
				continue;
			if(player.getFarmingState(i) < State.RAKED.getId()) {
				player.getPA().checkObjectSpawn(FarmingConstants.GRASS_OBJECT, patch.getX(), patch.getY(), 0, 10);
			} else if(player.getFarmingState(i) >= State.RAKED.getId() && player.getFarmingState(i) <= State.SEEDED.getId()) {
				player.getPA().checkObjectSpawn(FarmingConstants.HERB_PATCH_DEPLETED, patch.getX(), patch.getY(), 0, 10);
			} else if(player.getFarmingState(i) == State.GROWTH.getId()) {// 4 stages of herbs
				if (player.getFarmingTime(i) >= player.getOriginalFarmingTime(i) * .75)
					player.getPA().checkObjectSpawn(FarmingConstants.HERB_GROWING_OBJECT1, patch.getX(), patch.getY(), 0, 10);
				else if (player.getFarmingTime(i) >= player.getOriginalFarmingTime(i) * .50 && player.getFarmingTime(i) < player.getOriginalFarmingTime(i) * .75)
					player.getPA().checkObjectSpawn(FarmingConstants.HERB_GROWING_OBJECT2, patch.getX(), patch.getY(), 0, 10);
				else if (player.getFarmingTime(i) > player.getOriginalFarmingTime(i) * .25 && player.getFarmingTime(i) < player.getOriginalFarmingTime(i) * .50)
					player.getPA().checkObjectSpawn(FarmingConstants.HERB_GROWING_OBJECT3, patch.getX(), patch.getY(), 0, 10);
				else if (player.getFarmingTime(i) <= player.getOriginalFarmingTime(i) * .25)
					player.getPA().checkObjectSpawn(FarmingConstants.HERB_GROWING_OBJECT4, patch.getX(), patch.getY(), 0, 10);
			}  else if(player.getFarmingState(i) >= State.HARVEST.getId()) {
				player.getPA().checkObjectSpawn(FarmingConstants.HERB_OBJECT, patch.getX(), patch.getY(), 0, 10);
			}
		}
	}
	
	public boolean isHarvestable(int id) {
		return player.getFarmingState(id) == State.HARVEST.getId();
	}
	public long getLastBerryFarm() {
		return lastPoisonBerryFarm;
	}

	public void setLastBerryFarm(long millis) {
		this.lastPoisonBerryFarm = millis;
	}
	public enum State {
		NONE(0),
		RAKED(1),
		COMPOST(2),
		SEEDED(3),
		WATERED(4),
		GROWTH(5),
		HARVEST(6);
		
		private int id;
		State(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
	}
	
	enum Patch {
		ENTRANA1(0, 3003, 3372),
		ENTRANA2(1, 3003, 3374),
		ENTRANA3(2, 3005, 3372),
		ENTRANA4(3, 3005, 3374);
/*		DONATOR1(4, 2810, 3338),
		DONATOR2(5, 2811, 3338),
		DONATOR3(6, 2810, 3335),
		DONATOR4(7, 2811, 3335);*/
		
		
		int id, x, y;
		Patch(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
		
		public int getId() {
			return this.id;
		}
		
		public int getX() {
			return this.x;
		}
		
		public int getY() {
			return this.y;
		}
		
		static List<Patch> patches = new ArrayList<>();
		
		static {
			Collections.addAll(patches, Patch.values());
		}
		
		public static Patch get(int x, int y) {
			for(Patch patch : patches)
				if(patch.getX() == x && patch.getY() == y)
					return patch;
			return null;
		}
		
		public static Patch get(int id) {
			for(Patch patch : patches)
				if(patch.getId() == id)
					return patch;
			return null;
		}
	}
}