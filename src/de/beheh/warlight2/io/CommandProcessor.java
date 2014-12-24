package de.beheh.warlight2.io;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;

/**
 * Processes requests from the CommunicationHandler and returns the answer from
 * the bot.
 *
 * @author Benedict Etzel
 */
public class CommandProcessor {

	protected GameTracker gameTracker;
	protected Bot bot;

	public CommandProcessor(GameTracker gameTracker, Bot bot) {
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
		gameTracker.setTime(time);
		Map map = gameTracker.getMap();
		Region[] regions = new Region[regionIds.length];
		for (int i = 0; i < regionIds.length; i++) {
			regions[i] = map.getRegion(regionIds[i]);
		}
		try {
			Region region = bot.pickStartingRegion(regions);
			System.err.println("pickStartingRegion took " + (System.currentTimeMillis() - start) + "ms (had " + time + "ms)");
			return region.toString();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.err.println("defaulting to region " + regionIds[0]);
			return Integer.toString(regionIds[0]);
		}
	}

	public String placeArmies(Long time) {
		Long start = System.currentTimeMillis();
		gameTracker.setTime(time);
		Command command = null;
		try {
			command = bot.placeArmies(gameTracker.getStartingArmies());
			System.err.println("Round #" + gameTracker.getRound() + ": placeArmies took " + (System.currentTimeMillis() - start) + "ms (had " + time + "ms)");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return commandToString(command);
	}

	public String attackTransfer(Long time) {
		Long start = System.currentTimeMillis();
		gameTracker.setTime(time);
		Command command = null;
		try {
			command = bot.attackTransfer();
			System.err.println("Round #" + gameTracker.getRound() + ": attackTransfer took " + (System.currentTimeMillis() - start) + "ms (had " + time + "ms)");
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
