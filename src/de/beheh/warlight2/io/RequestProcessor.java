package de.beheh.warlight2.io;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;

/**
 * Processes requests from the CommunicationHandler and returns the answer from
 * the bot.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class RequestProcessor {

	protected GameTracker gameTracker;
	protected Bot bot;

	public RequestProcessor(GameTracker gameTracker, Bot bot) {
		this.gameTracker = gameTracker;
		this.bot = bot;
	}

	protected String commandToString(Command command) {
		if (command != null) {
			return command.toString();
		} else {
			return new NoMovesCommand().toString();
		}
	}

	public String pickStartingRegion(Long time, int[] regionIds) {
		Map map = gameTracker.getMap();
		Region[] regions = new Region[regionIds.length];
		for (int i = 0; i < regionIds.length; i++) {
			regions[i] = map.getRegion(regionIds[i]);
		}
		Region region = bot.pickStartingRegion(regions);
		return region.toString();
	}

	public String placeArmies(Long time) {
		return commandToString(bot.placeArmies(gameTracker.getStartingArmies()));
	}

	public String attackTransfer(Long time) {
		return commandToString(bot.attackTransfer());
	}

	public void opponentMoves() {
		bot.onOpponentMoves();
	}

	public void mapComplete() {
		bot.onMapComplete();
	}
	
	public void pickingComplete() {
		bot.onPickingComplete();
	}

	public void roundComplete() {
		bot.onRoundComplete();
	}

}
