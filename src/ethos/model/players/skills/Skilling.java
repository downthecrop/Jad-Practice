package ethos.model.players.skills;

import java.util.Optional;

import ethos.Server;
import ethos.model.players.Player;

public class Skilling {

	Player player;

	private Optional<Skill> skill = Optional.empty();

	public Skilling(Player player) {
		this.player = player;
	}

	public void stop() {
		Server.getEventHandler().stop(player, "skilling");
		skill = Optional.empty();
	}

	public boolean isSkilling() {
		return skill.isPresent();
	}

	public Skill getSkill() {
		return skill.orElse(null);
	}

	public void setSkill(Skill skill) {
		this.skill = Optional.of(skill);
	}

}
