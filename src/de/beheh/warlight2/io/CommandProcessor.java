package de.beheh.warlight2.io;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;

/**
 * Processes requests from the CommunicationHandler and returns the answer from
 * the bot.
 *
 * @author Benedict Etzel
 */
public class CommandProcessor {

	protected GameState gameTracker;
	protected Bot bot;

	public CommandProcessor(GameState gameTracker, Bot bot) {
		this.gameTracker = gameTracker;
		this.bot = bot;
	}

	protected String commandToString(Command command) {
		if (command != null && command.toString() != null) {
			return command.toString();
		} else {
			return new NoMovesCommand().toString();
		}
	}

	public String pickStartingRegion(Long time, int[] regionIds) {
		Long start = System.currentTimeMillis();
		bot.setTime(time);
		Map map = gameTracker.getMap();
		Region[] regions = new Region[regionIds.length];
		for (int i = 0; i < regionIds.length; i++) {
			regions[i] = map.getRegion(regionIds[i]);
		}
		try {
			Region region = bot.pickStartingRegion(regions);
			System.err.println(getClass().getSimpleName() + ": pickStartingRegion took " + (System.currentTimeMillis() - start) + "ms/" + gameTracker.getTimePerMove() + "ms (timebank = " + time + "ms)");
			if (region != null) {
				return region.toString();
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		System.err.println(getClass().getSimpleName() + ": no region picked, defaulting to region " + regionIds[0]);
		return Integer.toString(regionIds[0]);
	}

	public String placeArmies(Long time) {
		Long start = System.currentTimeMillis();
		bot.setTime(time);
		Command command = null;
		try {
			command = bot.placeArmies(gameTracker.getStartingArmies());
			System.err.println(getClass().getSimpleName() + ": placeArmies in round #" + gameTracker.getRound() + " took " + (System.currentTimeMillis() - start) + "ms/" + gameTracker.getTimePerMove() + "ms (timebank - setup = " + time + "ms)");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return commandToString(command);
	}

	public String attackTransfer(Long time) {
		Long start = System.currentTimeMillis();
		bot.setTime(time);
		Command command = null;
		try {
			command = bot.attackTransfer();
			System.err.println(getClass().getSimpleName() + ": attackTransfer in round #" + gameTracker.getRound() + " took " + (System.currentTimeMillis() - start) + "ms/" + gameTracker.getTimePerMove() + "ms (timebank - setup = " + time + "ms)");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return commandToString(command);
	}

	public void opponentMoves(String[] parameters) {
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
		bot.onRoundStart();
	}

}
