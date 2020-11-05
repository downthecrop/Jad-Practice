package ethos.event.impl;

import ethos.event.Event;
import ethos.model.content.SkillcapePerks;
import ethos.model.players.Player;

public class SkillRestorationEvent extends Event<Player> {

	public SkillRestorationEvent(Player attachment) {
		super(attachment, 100);
	}

	@Override
	public void execute() {
		if (attachment.isDead || attachment.getHealth().getAmount() <= 0) {
			return;
		}
		attachment.getHealth().increase(SkillcapePerks.HITPOINTS.isWearing(attachment) || SkillcapePerks.isWearingMaxCape(attachment) ? 2 : 1);
		if (attachment.hasOverloadBoost) {
			return;
		}
		for (int index = 0; index < attachment.playerLevel.length; index++) {
			if (index == 3 || index == 5) {
				continue;
			}
			final int maximum = attachment.getLevelForXP(attachment.playerXP[index]);
			if (attachment.playerLevel[index] < maximum) {
				attachment.playerLevel[index]++;
			} else if (attachment.playerLevel[index] > maximum) {
				attachment.playerLevel[index]--;
			}
			attachment.getPA().refreshSkill(index);
		}
	}

}
