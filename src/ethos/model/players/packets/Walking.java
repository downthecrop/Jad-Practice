package ethos.model.players.packets;

import java.util.Optional;

import ethos.Server;
import ethos.model.multiplayer_session.MultiplayerSessionFinalizeType;
import ethos.model.multiplayer_session.MultiplayerSessionStage;
import ethos.model.multiplayer_session.MultiplayerSessionType;
import ethos.model.multiplayer_session.duel.DuelSession;
import ethos.model.multiplayer_session.duel.DuelSessionRules.Rule;
import ethos.model.players.Boundary;
import ethos.model.players.PacketType;
import ethos.model.players.Player;
import ethos.model.players.PlayerHandler;
import ethos.model.players.skills.SkillHandler;
import ethos.util.Misc;

/**
 * Walking packet
 **/
public class Walking implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.nextChat = 0;
		c.dialogueOptions = 0;
		c.homeTeleport = 50;
		if (!c.inWild() && c.teleBlockLength > 0) {
			c.getPA().resetTb();
		}
		if (c.isDead || c.getHealth().getAmount() <= 0) {
			c.sendMessage("You are dead you cannot walk.");
			return;
		}
		if (c.isIdle) {
			if (c.debugMessage)
				c.sendMessage("You are no longer in idle mode.");
			c.isIdle = false;
		}
		if (c.getPlayerAction().canWalk(false)) {
			return;
		}
		if (c.isForceMovementActive()) {
			return;
		}
		if (!c.inClanWars() && !c.inClanWarsSafe() && c.pkDistrict) {
			c.sendMessage("You did not leave the district properly, therefore your items have been deleted.");
			c.getItems().deleteAllItems();
		}
		if (c.rottenPotatoOption != "") {
			c.rottenPotatoOption = "";
		}
		if (c.getInferno() != null && c.getInferno().cutsceneWalkBlock)
			return;
		if (c.morphed) {
			c.sendMessage("You cannot do this now.");
			return;
		}
		if (c.getCurrentCombination().isPresent()) {
			c.setCurrentCombination(Optional.empty());
		}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		if (c.getTutorial().isActive()) {
			c.getTutorial().refresh();
			return;
		}
		if (c.isStuck) {
			c.isStuck = false;
			c.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
			return;
		}
		if (c.getBankPin().requiresUnlock()) {
			c.getBankPin().open(2);
			return;
		}
		
		if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			c.sendMessage("You must decline the trade to start walking.");
			return;
		}
		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (session != null && session.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERATION && !Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			if (session.getRules().contains(Rule.NO_MOVEMENT)) {
				Player opponent = session.getOther(c);
				if (Boundary.isIn(opponent, session.getArenaBoundary())) {
					c.getPA().movePlayer(opponent.getX(), opponent.getY() - 1, 0);
				} else {
					int x = session.getArenaBoundary().getMinimumX() + 6 + Misc.random(12);
					int y = session.getArenaBoundary().getMinimumY() + 1 + Misc.random(11);
					c.getPA().movePlayer(x, y, 0);
					opponent.getPA().movePlayer(x, y - 1, 0);
				}
			} else {
				c.getPA().movePlayer(session.getArenaBoundary().getMinimumX() + 6 + Misc.random(12), session.getArenaBoundary().getMinimumY() + 1 + Misc.random(11), 0);
			}
			return;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENA)) {
			if (session == null) {
				c.getPA().movePlayer(3362, 3264, 0);
				return;
			}
			if (session.getRules().contains(Rule.NO_MOVEMENT)) {
				c.sendMessage("Movement has been disabled for this duel.");
				return;
			}
		}
		if (session != null && session.getStage().getStage() > MultiplayerSessionStage.REQUEST && session.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
			c.sendMessage("You have declined the duel.");
			session.getOther(c).sendMessage("The challenger has declined the duel.");
			session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		// if (c.settingBet || c.settingMax || c.settingMin) {
		// Dicing.resetDicing(c);
		// }
//		if (c.hasNpc) {
//			if (c.isRunning || c.isRunning2) {
//				c.isRunning = false;
//				c.isRunning2 = false;
//				c.getPA().setConfig(173, 0);
//				c.sendMessage("You can not run with a pet. You are now walking.");
//				return;
//			}
//		}
		
		if (Boundary.isIn(c, Boundary.ICE_PATH)) {
			if (c.isRunning || c.isRunning2) {
				c.isRunning = false;
				c.isRunning2 = false;
				c.getPA().setConfig(173, 0);
				return;
			}
		}

		if (c.canChangeAppearance) {
			c.canChangeAppearance = false;
		}
		// if (c.getBarrows().cantWalk) {
		// c.getBarrows().challengeMinigame();
		// return;
		// }
		if (c.getSkilling().isSkilling()) {
			c.getSkilling().stop();
		}
		c.getPA().resetVariables();
		SkillHandler.isSkilling[12] = false;
		if (c.teleporting) {
			c.startAnimation(65535);
			c.teleporting = false;
			c.isRunning = false;
			c.gfx0(-1);
			c.startAnimation(-1);
		}
		c.walkingToItem = false;
		c.isWc = false;
		c.clickNpcType = 0;
		c.clickObjectType = 0;
		if (c.isBanking)
			c.isBanking = false;
		if (c.tradeStatus >= 0) {
			c.tradeStatus = 0;
		}
		if (packetType == 248 || packetType == 164) {
			c.clickObjectType = 0;
			c.clickNpcType = 0;
			c.faceUpdate(0);
			c.npcIndex = 0;
			c.playerIndex = 0;
			if (c.followId > 0 || c.followId2 > 0)
				c.getPA().resetFollow();
		}
		c.getPA().removeAllWindows();
		if (c.stopPlayerSkill) {
			SkillHandler.resetPlayerSkillVariables(c);
			c.stopPlayerSkill = false;
		}

		if (c.teleTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null
					&& c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.playerIndex].getX(), PlayerHandler.players[c.playerIndex].getY(), 1) && packetType != 98) {
				c.playerIndex = 0;
			} else {
				// c.sendMessage("Stop.");
				if (packetType != 98) {
					c.playerIndex = 0;
				}

			}
			return;
		}

		if (c.freezeTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null
					&& c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[c.playerIndex].getX(), PlayerHandler.players[c.playerIndex].getY(), 1) && packetType != 98) {
				c.playerIndex = 0;
			} else {
				c.sendMessage("A magical force stops you from moving.");
				if (packetType != 98) {
					c.playerIndex = 0;
				}

			}
			return;
		}
		if (System.currentTimeMillis() - c.lastSpear < 3000) {
			c.sendMessage("You have been stunned.");
			c.playerIndex = 0;
			return;
		}
		if (packetType == 98) {
			c.mageAllowed = true;
		}
		if (c.respawnTimer > 3) {
			return;
		}
		if (c.inTrade) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			c.getInterfaceEvent().draw();
			return;
		}
		c.setInterfaceOpen(-1);
		if (packetType == 248) {
			packetSize -= 14;
		}
		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.newWalkCmdSteps = 0;
			return;
		}
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int firstStepX = c.getInStream().readSignedWordBigEndianA() - c.getMapRegionX() * 8;
		for (int i = 1; i < c.newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = c.getInStream().readSignedByte();
			c.getNewWalkCmdY()[i] = c.getInStream().readSignedByte();
		}
		int firstStepY = c.getInStream().readSignedWordBigEndian() - c.getMapRegionY() * 8;
		c.setNewWalkCmdIsRunning((c.getInStream().readSignedByteC() == 1));
		for (int i1 = 0; i1 < c.newWalkCmdSteps; i1++) {
			c.getNewWalkCmdX()[i1] += firstStepX;
			c.getNewWalkCmdY()[i1] += firstStepY;
		}
	}

}
