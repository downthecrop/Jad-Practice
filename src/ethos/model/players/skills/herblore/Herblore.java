package ethos.model.players.skills.herblore;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import ethos.Config;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.event.CycleEventHandler;
import ethos.model.content.SkillcapePerks;
import ethos.model.content.achievement.AchievementType;
import ethos.model.content.achievement.Achievements;
import ethos.model.content.achievement_diary.ardougne.ArdougneDiaryEntry;
import ethos.model.content.achievement_diary.desert.DesertDiaryEntry;
import ethos.model.content.achievement_diary.fremennik.FremennikDiaryEntry;
import ethos.model.content.achievement_diary.kandarin.KandarinDiaryEntry;
import ethos.model.content.achievement_diary.karamja.KaramjaDiaryEntry;
import ethos.model.content.achievement_diary.varrock.VarrockDiaryEntry;
import ethos.model.content.dailytasks.DailyTasks;
import ethos.model.content.dailytasks.DailyTasks.PossibleTasks;
import ethos.model.items.GameItem;
import ethos.model.items.Item;
import ethos.model.items.ItemDefinition;
import ethos.model.players.Boundary;
import ethos.model.players.Player;
import ethos.model.players.mode.ModeType;
import ethos.model.players.skills.Skill;
import ethos.model.players.skills.herblore.PotionData.FinishedPotions;
import ethos.model.players.skills.herblore.PotionData.UnfinishedPotions;
import ethos.util.Misc;

public class Herblore {

	/**
	 * A {@link Set} of all {@link Herb} elements from it's respective enumeration.
	 */
	private static final Set<Herb> HERBS = Collections.unmodifiableSet(EnumSet.allOf(Herb.class));

	/**
	 * A {@link Set} of all {@link Potion} elements from it's respective enumeration.
	 */
	private static final Set<FinishedPotions> FINISHED = Collections.unmodifiableSet(EnumSet.allOf(PotionData.FinishedPotions.class));

	/**
	 * The player that will be operating this skill
	 */
	private final Player player;

	/**
	 * A class for managing herblore operation
	 * 
	 * @param player the player
	 */
	public Herblore(Player player) {
		this.player = player;
	}

	/**
	 * Cleans a single her
	 * 
	 * @param itemId the herb attempting to be cleaned
	 */
	public void clean(int itemId) {
		Optional<Herb> herb = HERBS.stream().filter(h -> h.getGrimy() == itemId).findFirst();
		herb.ifPresent(h -> {
			player.getSkilling().stop();
			player.getSkilling().setSkill(Skill.HERBLORE);
			if (!player.getItems().playerHasItem(h.getGrimy())) {
				player.sendMessage("You need the grimy herb to do this.");
				return;
			}
			if (player.playerLevel[Skill.HERBLORE.getId()] < h.getLevel()) {
				player.sendMessage("You need a herblore level of " + h.getLevel() + " to clean this grimy herb.");
				return;
			}
			ItemDefinition definition = ItemDefinition.forId(h.getClean());
			player.getPA().addSkillXP(h.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.HERBLORE_EXPERIENCE), Skill.HERBLORE.getId(), true);
			player.getItems().deleteItem2(h.getGrimy(), 1);
			player.getItems().addItem(h.getClean(), 1);
			player.sendMessage("You identify the herb as " + definition.getName() + ".");
		});
	}

	public void mix(int primary) {
		Optional<FinishedPotions> potion = FINISHED.stream().filter(p -> p.getPrimary().getId() == primary && containsSecondaries(p)).findFirst();
		potion.ifPresent(p -> {
			
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (player == null || player.disconnected || player.getSession() == null) {
						stop();
						return;
					}
					if (player.getItems().playerHasItem(p.getPrimary().getId(), p.getPrimary().getAmount()) && containsSecondaries(p)) {
					player.getSkilling().stop();
					player.getSkilling().setSkill(Skill.HERBLORE);
					ItemDefinition definition = ItemDefinition.forId(p.getResult().getId());
					if (player.playerLevel[Skill.HERBLORE.getId()] < p.getLevel()) {
						player.sendMessage("You need a herblore level of " + p.getLevel() + " to make " + definition.getName() + ".");
						container.stop();
						return;
					}
					player.startAnimation(363);
					Arrays.asList(p.getIngredients()).stream().forEach(ing -> player.getItems().deleteItem2(ing.getId(), ing.getAmount()));
					
					/**
					 * Chance of saving a herb while wearing herblore or max cape
					 */
					if (SkillcapePerks.HERBLORE.isWearing(player) || SkillcapePerks.isWearingMaxCape(player)) {
						if (Misc.random(4) == 2) {
							player.sendMessage("You manage to save an ingredient.");
						} else {
							player.getItems().deleteItem2(p.getPrimary().getId(), p.getPrimary().getAmount());
						}
					} else {
						player.getItems().deleteItem2(p.getPrimary().getId(), p.getPrimary().getAmount());
					}
					
					player.getItems().addItem(p.getResult().getId(), p.getResult().getAmount());
					player.getPA().addSkillXP(p.getExperience() * (player.getMode().getType().equals(ModeType.OSRS) ? 1 : Config.HERBLORE_EXPERIENCE), Skill.HERBLORE.getId(), true);
					player.sendMessage("You combine all of the ingredients and make a " + definition.getName() + ".");
					Achievements.increase(player, AchievementType.HERB, 1);
					switch (p) {
					case PRAYER:
						DailyTasks.increase(player, PossibleTasks.PRAYER_POTIONS);
						break;
					case SUPER_DEFENCE:
						if (Boundary.isIn(player, Boundary.RELLEKKA_BOUNDARY)) {
							player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.MIX_SUPER_DEFENCE);
						}
						break;
					case SUPER_COMBAT:
						if (Boundary.isIn(player, Boundary.ARDOUGNE_ZOO_BRIDGE_BOUNDARY)) {
							player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.SUPER_COMBAT_ARD);
						}
						if (Boundary.isIn(player, Boundary.VARROCK_BOUNDARY)) {
							player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.SUPER_COMBAT);
						}
						break;
					case ANTI_VENOM:
						if (Boundary.isIn(player, Boundary.BRIMHAVEN_BOUNDARY)) {
							player.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.ANTI_VENOM);
						}
						break;
					case WEAPON_POISON_PLUS_PLUS:
						if (Boundary.isIn(player, Boundary.CATHERBY_BOUNDARY)) {
							player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.WEAPON_POISON_PLUS_PLUS);
						}
						break;
					case COMBAT:
						if (Boundary.isIn(player, Boundary.DESERT_BOUNDARY)) {
							player.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.COMBAT_POTION);
						}
						break;
					default:
						break;
					}
					} else {
						player.sendMessage("You have run out of supplies to do this.");
						container.stop();
					}
				}
				@Override
				public void stop() {
				}
			}, 2);
		});
	}
	
	public boolean makeUnfinishedPotion(final Player player, final GameItem itemUsed) {
		final UnfinishedPotions unf = UnfinishedPotions.forId(itemUsed.getId());
		if (unf == null) {
			return false;
		}
		if (player.getLevelForXP(player.playerXP[player.playerHerblore]) < unf.getLevelReq()) {
			player.sendMessage("You need a Herblore level of " + unf.getLevelReq() + " to make this potion.");
			return false;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (player == null || player.disconnected || player.getSession() == null) {
					stop();
					return;
				}
				if (player.getItems().playerHasItem(227) && player.getItems().playerHasItem(unf.getHerb().getId())) {
					player.getItems().deleteItem(227, player.getItems().getItemSlot(227), 1);
					player.getItems().deleteItem2(unf.getHerb().getId(), 1);
					player.getItems().addItem(unf.getPotion().getId(), 1);
					player.sendMessage("You put the " + Item.getItemName(unf.getHerb().getId()) + " into the vial of water and create a " + Item.getItemName(unf.getPotion().getId()) + ".");
				} else {
					player.sendMessage("You have run out of supplies to do this.");
					container.stop();
				}
			}
			@Override
			public void stop() {
			}
		}, 1);
		return false;
	}

	/**
	 * Determines if the player has all of the ingredients required for the potion.
	 * 
	 * @param p the potion we're determining this for
	 * @return {@code true} if we have all of the ingredients, otherwise {@code false}
	 */
	private boolean containsSecondaries(FinishedPotions p) {
		int required = p.getIngredients().length;
		System.out.println(""+required);

		for (GameItem ingredient : p.getIngredients()) {
			if (player.getItems().playerHasItem(ingredient.getId(), ingredient.getAmount())) {
				required--;
			}
		}
		return required == 0;
	}

	public void crushItem(int itemid) {

	}
}
