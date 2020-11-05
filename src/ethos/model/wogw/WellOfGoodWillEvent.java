package ethos.model.wogw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;

import ethos.Server;
import ethos.event.CycleEvent;
import ethos.event.CycleEventContainer;
import ethos.model.items.GameItem;
import ethos.model.items.ItemAssistant;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.PlayerSave;

/**
 * @author Jason MacKeigan
 * @date Dec 21, 2014, 9:16:43 AM
 */
public class WellOfGoodWillEvent extends CycleEvent {

	@Override
	public void execute(CycleEventContainer container) {
		WellOfGoodWill wogw = Server.getServerData().getWellOfGoodWill();
		Calendar calendar = Server.getCalendar().getInstance();
		Date date = calendar.getTime();
		if (calendar.getTime().after(wogw.getDate())) {
			date = DateUtils.addDays(date, 7 - calendar.get(Calendar.DAY_OF_WEEK) + Calendar.SUNDAY);
			wogw.setDate(date);
			ArrayList<Entry<String, Integer>> winners = wogw.getSortedResults();
			Map<String, Integer> winnerMap = new HashMap<>();
			winners.forEach(entry -> winnerMap.put(entry.getKey(), entry.getValue()));
			if (winners.size() == 0) {
				wogw.setWinners(winnerMap);
			} else {
				wogw.setWinners(winnerMap);
				Optional<WellOfGoodWillReward> reward = WellOfGoodWillReward.getReward(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));
				if (reward.isPresent()) {
					StringBuilder s = new StringBuilder();
					GameItem item = reward.get().getReward();
					for (Entry<String, Integer> entry : winners) {
						s.append(entry.getKey() + ", ");
						if (!PlayerHandler.isPlayerOn(entry.getKey())) {
							PlayerSave.addItemToFile(entry.getKey(), item.getId(), item.getAmount());
							continue;
						}
						Player player = PlayerHandler.getPlayer(entry.getKey());
						if (!player.getItems().addItem(item.getId(), item.getAmount())) {
							player.getItems().sendItemToAnyTab(item.getId(), item.getAmount());
						}
					}
					PlayerHandler.executeGlobalMessage("The Well of good Will has finished for the week. Congratulations to the following;");
					PlayerHandler.executeGlobalMessage(s.toString() + " on winning the exclusive " + ItemAssistant.getItemName(item.getId()) + ".");
				}
			}
			wogw.clear();
			Server.getServerData().setWellOfGoodWill(wogw);
		}
	}

}
