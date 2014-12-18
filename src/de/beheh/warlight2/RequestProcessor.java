package de.beheh.warlight2;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import java.util.Iterator;
import java.util.List;

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

	protected String mergeCommands(List<Command> commands) {
		StringBuilder builder = new StringBuilder();
		if (commands != null && commands.size() > 0) {
			Iterator<Command> iterator = commands.iterator();
			boolean first = true;
			while (iterator.hasNext()) {
				if (!first) {
					builder.append(", ");
				} else {
					first = false;
				}
				builder.append(iterator.next().toString());
			}
		} else {
			builder.append(new NoMovesCommand().toString());
		}
		return builder.toString();
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
		gameTracker.nextRound();
		return this.mergeCommands(bot.placeArmies());
	}

	public String attackTransfer(Long time) {
		return this.mergeCommands(bot.attackTransfer());
	}

	public void opponentMoves() {
		bot.onOpponentMoves();
	}

	public void mapReceived() {
		bot.onMapReceived();
	}

}
