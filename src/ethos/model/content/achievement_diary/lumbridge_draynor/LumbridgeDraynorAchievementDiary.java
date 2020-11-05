package ethos.model.content.achievement_diary.lumbridge_draynor;

import static ethos.model.content.achievement_diary.lumbridge_draynor.LumbridgeDraynorDiaryEntry.*;

import java.util.EnumSet;
import java.util.Set;

import ethos.model.content.achievement_diary.StatefulAchievementDiary;
import ethos.model.players.Player;

public final class LumbridgeDraynorAchievementDiary extends StatefulAchievementDiary<LumbridgeDraynorDiaryEntry>{
	
	public static final Set<LumbridgeDraynorDiaryEntry> EASY_TASKS = EnumSet.of(TELEPORT_ESSENCE_LUM, CRAFT_WATER, HANS, PICKPOCKET_MAN_LUM, BURN_OAK, COOK_SHRIMP, MINE_IRON_LUM);
	
	public static final Set<LumbridgeDraynorDiaryEntry> MEDIUM_TASKS = EnumSet.of(LUMBRIDGE_TELEPORT, RIVER_LUM_SHORTCUT, ATTRACTOR, CHOP_WILLOW_DRAY, PICKPOCKET_FARMER_DRAY);
	
	public static final Set<LumbridgeDraynorDiaryEntry> HARD_TASKS = EnumSet.of(BONES_TO_PEACHES, CRAFT_COSMIC, PURCHASE_BARROW_GLOVES);
	
	public static final Set<LumbridgeDraynorDiaryEntry> ELITE_TASKS = EnumSet.of(CHOP_MAGIC_AL, RUNE_PLATE_LUM, ACHIEVEMENT_EMOTE);
	
	public static final String NAME = "Lumbridge & Draynor area";

	public LumbridgeDraynorAchievementDiary(Player player) {
		super(NAME, player);
	}
	
	int REWARD =  13125;
	public void claimReward() {
		//EASY
		if (!hasDone(EntryDifficulty.EASY)) {
			npcDialogue("Come back when you've completed the easy tasks of this area.");
			return;
		} else {
			if (!hasClaimed(EntryDifficulty.EASY)) {
				npcDialogue("Nice job, here have the tier 1 reward.");
				addReward(REWARD);
				claim(EntryDifficulty.EASY);
				return;
			} else {
				if (getCount(REWARD) == 0 && !hasClaimed(EntryDifficulty.MEDIUM)) {
					npcDialogue("Oh, you lost your reward? Don't worry, here you go.");
					addReward(REWARD);
					return;
				}
			}
		}
		
		//MEDIUM
		if (hasDone(EntryDifficulty.EASY) && hasDone(EntryDifficulty.MEDIUM) && hasClaimed(EntryDifficulty.EASY)) {
			if (hasClaimed(EntryDifficulty.MEDIUM)) {
				if (getCount(REWARD + 1) == 0) {
					if (!hasClaimed(EntryDifficulty.HARD)) {
						npcDialogue("Oh, you lost your reward? Don't worry, here you go.");
						addReward(REWARD + 1);
						return;
					}
				}
			} else {
				if (player.getItems().playerHasItem(REWARD)) {
					npcDialogue("Nice one, I will upgrade that for you..");
					upgradeReward(REWARD, REWARD + 1);
					claim(EntryDifficulty.MEDIUM);
					return;
				} else {
					npcDialogue("Bring me the previous tier reward and I will upgrade it for you!");
					return;
				}
			}
		}
		
		//HARD
		if (hasDone(EntryDifficulty.EASY) && hasDone(EntryDifficulty.MEDIUM) && hasDone(EntryDifficulty.HARD) && hasClaimed(EntryDifficulty.MEDIUM)) {
			if (hasClaimed(EntryDifficulty.HARD)) {
				if (getCount(REWARD + 2) == 0) {
					if (!hasClaimed(EntryDifficulty.ELITE)) {
						npcDialogue("Oh, you lost your reward? Don't worry, here you go.");
						addReward(REWARD + 2);
						return;
					}
				}
			} else {
				if (player.getItems().playerHasItem(REWARD + 1)) {
					npcDialogue("Nice one, I will upgrade that for you..");
					upgradeReward(REWARD + 1, REWARD + 2);
					claim(EntryDifficulty.HARD);
					return;
				} else {
					npcDialogue("Bring me the previous tier reward and I will upgrade it for you!");
					return;
				}
			}
		}
		
		//ELITE
		if (hasDone(EntryDifficulty.EASY) && hasDone(EntryDifficulty.MEDIUM) && hasDone(EntryDifficulty.HARD) && hasDone(EntryDifficulty.ELITE) && hasClaimed(EntryDifficulty.HARD)) {
			if (hasClaimed(EntryDifficulty.ELITE)) {
				if (getCount(REWARD + 3) == 0) {
					npcDialogue("Oh, you lost your reward? Don't worry, here you go.");
					addReward(REWARD + 3);
					return;
				}
			} else {
				if (player.getItems().playerHasItem(REWARD + 2)) {
					npcDialogue("Nice one, I will upgrade that for you..");
					upgradeReward(REWARD + 2, REWARD + 3);
					claim(EntryDifficulty.ELITE);
					return;
				} else {
					npcDialogue("Bring me the previous tier reward and I will upgrade it for you!");
					return;
				}
			}
		}
		
	}
	
	public void npcDialogue(String dialogue) {
		player.getDH().sendNpcChat1(dialogue, player.npcType, "Diary Manager");
		player.nextChat = -1;
	}
	public void addReward(int reward) {
		player.getItems().addItem(reward, 1);
		player.getDH().sendNpcChat1("Here you go, upgraded and ready to be used.", player.npcType, "Diary Manager");
	}
	public void upgradeReward(int reward, int upgrade) {
		player.getItems().replaceItem(player, reward, upgrade);
		player.getDH().sendNpcChat1("Here you go, upgraded and ready.", player.npcType, "Diary Manager");
	}
	public int getCount(int id) {
		return player.getItems().getItemCount(id, false);
	}
	
	public boolean hasCompleted(String difficulty) {
		switch (difficulty) {
		case "EASY":
			return achievements.containsAll(EASY_TASKS);
			
		case "MEDIUM":
			return achievements.containsAll(MEDIUM_TASKS);
			
		case "HARD":
			return achievements.containsAll(HARD_TASKS);
			
		case "ELITE":
			return achievements.containsAll(ELITE_TASKS);
		}
		return achievements.containsAll(EASY_TASKS);
	}

	@Override
	public Set<LumbridgeDraynorDiaryEntry> getEasy() {
		return EASY_TASKS;
	}

	@Override
	public Set<LumbridgeDraynorDiaryEntry> getMedium() {
		return MEDIUM_TASKS;
	}

	@Override
	public Set<LumbridgeDraynorDiaryEntry> getHard() {
		return HARD_TASKS;
	}

	@Override
	public Set<LumbridgeDraynorDiaryEntry> getElite() {
		return ELITE_TASKS;
	}
	
	int frameIndex = 0;
	public final void display() {
		Set<LumbridgeDraynorDiaryEntry> all = getAll();
		int[] frames = { 8148, 8149, 8150, 8151, 8152, 8153, 8154, 8156, 8157, 8158, 8159, 8160, 8162, 8163, 8164,
				8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175, 8176, 8178, 8179, 8180, 8181,
				8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193, 8194 };
		
		for (int i = 8144; i < 8195; i++) {
			player.getPA().sendFrame126("", i);
		}
		frameIndex = 0;
		
		player.getPA().sendFrame126("@dre@Lumbridge & Draynor Diary", 8144);
		player.getPA().sendFrame126("", 8145);
		player.getPA().sendFrame126(hasCompleted("EASY") ? "@blu@<str=1>Easy</str>" : "@blu@Easy", 8147);
		player.getPA().sendFrame126(hasCompleted("MEDIUM") ? "@blu@<str=1>Medium</str>" : "@blu@Medium", 8155);
		player.getPA().sendFrame126(hasCompleted("HARD") ? "@blu@<str=1>Hard</str>" : "@blu@Hard", 8161);
		player.getPA().sendFrame126(hasCompleted("ELITE") ? "@blu@<str=1>Elite</str>" : "@blu@Elite", 8165);
		
		all.forEach(entry -> {
			String description = entry.getDescription();
			
			/* %stage gets the current stage (e.g. 1)
			 * %maximumstage gets the maximum stage (e.g. 5)
			 * %totalstage gets both of these (e.g. 1/5)
			 */
			description = description.replace("%stagej", Integer.toString(getAbsoluteAchievementStage(entry)));
			description = description.replace("%maximumstage", Integer.toString(getMaximum(entry)));
			description = description.replace("%totalstage", (getAbsoluteAchievementStage(entry)) + "/" + getMaximum(entry));
			
			player.getPA().sendFrame126(hasDone(entry) ? "<str=0>" +description+ "</str>" : description, frames[frameIndex]);
			frameIndex++;
		});
		
		player.getPA().showInterface(8134);
	}

	@Override
	public int getMaximum(LumbridgeDraynorDiaryEntry achievement) {
		return achievement.getMaximumStages();
	}

}