package ethos.model.players.skills.farming;

import ethos.event.Event;
import ethos.model.content.achievement_diary.falador.FaladorDiaryEntry;
import ethos.model.players.Boundary;
import ethos.model.players.Player;

public class FarmingCompostEvent extends Event<Player> {

	public FarmingCompostEvent(Player attachment, int ticks) {
		super("skilling", attachment, ticks);
	}

	@Override
	public void execute() {
		if (attachment == null || attachment.disconnected) {
			this.stop();
			return;
		}
		if (!attachment.getItems().playerHasItem(995, 250)) {
			attachment.getDH().sendDialogues(662, 3257);
			this.stop();
			return;
		}
		if (!attachment.getItems().playerHasItem(1925)) {
			attachment.getDH().sendStatement("You have run out of buckets to fill.");
			attachment.nextChat = -1;
			this.stop();
			return;
		}
		attachment.startAnimation(2283);
		attachment.getItems().deleteItem2(995, 250);
		attachment.getItems().deleteItem2(1925, 1);
		attachment.getItems().addItem(FarmingConstants.COMPOST, 1);
		if (Boundary.isIn(attachment, Boundary.FALADOR_BOUNDARY)) {
			attachment.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.COMPOST_BUCKET);
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (attachment == null || attachment.disconnected) {
			return;
		}
		attachment.stopAnimation();
	}

}
