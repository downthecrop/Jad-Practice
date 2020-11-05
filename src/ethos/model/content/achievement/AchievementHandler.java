package ethos.model.content.achievement;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import ethos.Server;
import ethos.model.content.achievement.Achievements.Achievement;
import ethos.model.npcs.NPC;
import ethos.model.players.Player;

/**
 * @author Jason MacKeigan (http://www.rune-server.org/members/Jason)
 */
public class AchievementHandler {
    Player player;
    public int currentInterface;
    private static final int MAXIMUM_TIER_ACHIEVEMENTS = 100;
    private static final int MAXIMUM_TIERS = 3;
    private int[][] amountRemaining = new int[MAXIMUM_TIERS][MAXIMUM_TIER_ACHIEVEMENTS];
    private boolean[][] completed = new boolean[MAXIMUM_TIERS][MAXIMUM_TIER_ACHIEVEMENTS];

    public int points;

    /**
     * WARNING: ADD TO THE END OF THE LIST.
     */
    private int boughtItems[][] = {{7409, -1}, {13659, -1}, {20120, -1}, {88, -1}, {13281, -1}, {2379, -1}, {20235, -1}, {13845, -1}, {13846, -1}, {13847, -1},
            {13848, -1}, {13849, -1}, {13850, -1}, {13851, -1}, {13852, -1}, {13853, -1}, {13854, -1}, {13855, -1}, {13856, -1}, {13857, -1}, {20220, -1},
            {20221, -1}, {20222, -1},};

    public AchievementHandler(Player player) {
        this.player = player;
    }

    public void print(BufferedWriter writer, int tier) {
        try {
            for (Achievements.Achievement achievement : Achievement.ACHIEVEMENTS) {
                if (achievement.getTier().ordinal() == tier) {
                    if (amountRemaining[tier][achievement.getId()] > 0) {
                        writer.write(achievement.name().toLowerCase() + " = " + amountRemaining[tier][achievement.getId()] + "\t" + completed[tier][achievement.getId()]);
                        writer.newLine();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void read(String name, int tier, int amount, boolean state) {
        for (Achievements.Achievement achievement : Achievements.Achievement.ACHIEVEMENTS) {
            if (achievement.getTier().ordinal() == tier) {
                if (achievement.name().toLowerCase().equals(name)) {
                    this.setComplete(tier, achievement.getId(), state);
                    this.setAmountRemaining(tier, achievement.getId(), amount);
                    break;
                }
            }
        }
    }

    /**
     * Draws the achievement interface
     *
     * @param tier
     */
    public void drawInterface(int tier) {
        String tab = "";
        switch (tier) {
            case 0://Easy
                tab = "Easy";
                break;
            case 1://Medium
                tab = "Medium";
                break;
            case 2://Hard
                tab = "Hard";
                break;
        }

        //Send the name of the tab
        player.getPA().sendFrame126(tab, 36026);

        //Counter, to allow us to sift through all achievements and properly send their frame
        int count = 36037;

        //Views the first achievement of that tier
        viewAchievement(0, tier);

        //Looping through all achievements of the tier currently being viewed
        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
            if (achievement.getTier().ordinal() == tier) {
                int amount = getAmountRemaining(achievement.getTier().ordinal(), achievement.getId());
                if (amount > achievement.getAmount())
                    amount = achievement.getAmount();
                player.getPA().sendFrame126(achievement.name().toUpperCase().replaceAll("_", " "), count);
                count++;
            }
        }

        //Clearing the left over achievement interface slots
        for (int i = count; i < 36137; i++) {
            player.getPA().sendFrame126("", count);
            count++;
        }

        player.getPA().showInterface(36000);

        //TODO: player.getPA().sendFrame126(Integer.toString(achievement.getPoints()), count);

    }

    /**
     * Responsive clicking for viewing an achievement
     *
     * @param achievement
     */
    public void viewAchievement(int achievement, int tier) {
        int toView;

        if (achievement > 0) {
            toView = 36037 + (achievement - 140197);
        } else {
            toView = 36037;
        }

        //Counter, to allow us to sift through all achievements and properly send their frame
        int count = 36037;

        //Looping through all achievements of the tier currently being viewed
        for (Achievement ach : Achievement.ACHIEVEMENTS) {
            if (ach.getTier().ordinal() == tier) {
                //Send the first achievement to view by default
                if (count == toView) {
                    int descLines;
                    player.getPA().sendFrame126(ach.name().toUpperCase().replaceAll("_", " "), 36501);//Title
                    int amount = getAmountRemaining(ach.getTier().ordinal(), ach.getId());//Grabbing the amount completed
                    int percentage = (int) ((100 * (double) amount) / (double) ach.getAmount());//percentage calculator
                    player.getPA().sendFrame126("<col=ffffff>Progress:</col><col=00ff00> " + percentage + "% (" + amount + "/" + ach.getAmount() + ")</col>", 36502);//Progress

                    //Checks if the achievement description needs to be cut into two lines or one
                    if (ach.getDescription().length() > 40) {
                        player.getPA().sendFrame126(ach.getDescription().substring(0, 20), 36503);
                        player.getPA().sendFrame126(ach.getDescription().substring(20), 36504);
                        descLines = 2;
                    } else {
                        player.getPA().sendFrame126(ach.getDescription(), 36503);
                        descLines = 1;
                    }

                    //Clears the rest of the achievement description lines that aren't used
                    for (int i = descLines; i < 6; i++) {
                        player.getPA().sendFrame126("", 36503 + i);
                    }

                    for (int i = 0; i < 12; i++) {
                        if (i < 5)
                            player.getPA().itemOnInterface(-1, 0, 36521, i);
                        player.getPA().sendFrame126("", 36511 + i);
                    }
                    //Sends the items on the reward interface @Placeholder - 4151
                    for (int i = 0; i < ach.getRewards().length; i++) {
                        //Original code ->	//player.getPA().itemOnInterface(4153, 1, 36521, i);

                        player.getPA().itemOnInterface(ach.getRewards()[i].getId(), ach.getRewards()[i].getAmount() == 0 ? 1 : ach.getRewards()[i].getAmount(), 36521, i);

                        //UN COMMENT THE BELOW 2 LINES TO ENABLE ITEM DESC
                        //player.getPA().sendFrame126(ItemDefinition.forId(ach.getRewards()[i].getId()) == null ? "Unknown" :
                        //             ItemDefinition.forId(ach.getRewards()[i].getId()).getName() + " x " + ach.getRewards()[1].getAmount(), (36511 + i));

                    }
                }
                count++;
            }
        }
    }

    public void kill(NPC npc) {
        String name = Server.npcHandler.getNpcListName(npc.npcType);
        if (name.length() <= 0) {
            return;
        } else {
            name = name.toLowerCase().replaceAll("_", " ");
        }
        Achievements.increase(player, AchievementType.SLAY_ANY_NPCS, 1);
        if (name.contains("dragon") && !name.contains("baby"))
            Achievements.increase(player, AchievementType.SLAY_DRAGONS, 1);
        else if (name.contains("dragon") && name.contains("baby"))
            Achievements.increase(player, AchievementType.SLAY_BABY_DRAGONS, 1);
        else if (name.contains("crab"))
            Achievements.increase(player, AchievementType.SLAY_ROCK_CRABS, 1);
        else if (name.contains("chicken"))
            Achievements.increase(player, AchievementType.SLAY_CHICKENS, 1);
        List<String> checked = new ArrayList<>();
        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
            if (!achievement.getType().name().toLowerCase().contains("kill"))
                continue;
            if (achievement.getType().name().toLowerCase().replaceAll("_", " ").replaceAll("kill ", "").equalsIgnoreCase(name)) {
                if (checked.contains(achievement.getType().name().toLowerCase().replaceAll("_", " ").replaceAll("kill ", "")))
                    continue;
                Achievements.increase(player, achievement.getType(), 1);
                checked.add(achievement.getType().name().toLowerCase().replaceAll("_", " ").replaceAll("kill ", ""));
            }
        }
    }

    public void claimCape() {
        if (!hasCompletedAll()) {
            player.getDH().sendDialogues(1, 6071); //5527
            return;
        }
        if (!player.getItems().playerHasItem(995, 99_000)) {
            player.getDH().sendDialogues(2, 6071); //5527
            return;
        }
        player.getItems().addItemUnderAnyCircumstance(13069, 1);
        player.getItems().addItemUnderAnyCircumstance(13070, 1);
        player.getItems().deleteItem(995, 99_000);
        player.sendMessage("You've successfully purchased the achievement diary cape, congratulations.");
    }

    public boolean hasCompletedAll() {
        int amount = 0;
        for (Achievement achievement : Achievement.ACHIEVEMENTS) {
            if (isComplete(achievement.getTier().ordinal(), achievement.getId()))
                amount++;
        }
        return amount == Achievements.getMaximumAchievements();
    }

    public boolean completedTier(AchievementTier tier) {
        for (Achievement achievement : Achievement.ACHIEVEMENTS)
            if (achievement.getTier() == tier)
                if (!isComplete(achievement.getTier().ordinal(), achievement.getId()))
                    return false;
        return true;
    }

    public boolean isComplete(int tier, int index) {
        return completed[tier][index];
    }

    public boolean setComplete(int tier, int index, boolean state) {
        return this.completed[tier][index] = state;
    }

    public int getAmountRemaining(int tier, int index) {
        return amountRemaining[tier][index];
    }

    public void setAmountRemaining(int tier, int index, int amountRemaining) {
        this.amountRemaining[tier][index] = amountRemaining;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isAchievementItem(int itemId) {
        for (int i = 0; i < boughtItems.length; i++)
            if (boughtItems[i][0] == itemId)
                return true;
        return false;
    }

    public boolean hasBoughtItem(int itemId) {
        for (int i = 0; i < boughtItems.length; i++)
            if (boughtItems[i][0] == itemId)
                if (boughtItems[i][1] != -1)
                    return true;
        return false;
    }

    public void setBoughtItem(int itemId) {
        for (int i = 0; i < boughtItems.length; i++)
            if (boughtItems[i][0] == itemId)
                boughtItems[i][1] = 1;
    }

    public int[][] getBoughtItems() {
        return this.boughtItems;
    }

    public void setBoughtItem(int index, int value) {
        if (index > this.boughtItems.length - 1)
            return;
        this.boughtItems[index][1] = value;
    }

}
