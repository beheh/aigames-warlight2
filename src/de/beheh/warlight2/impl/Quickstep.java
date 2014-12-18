package de.beheh.warlight2.impl;

import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Quickstep extends Bot {

	public Quickstep(GameTracker gameTracker) {
		super(gameTracker);
	}

	@Override
	public Region pickStartingRegion(Region[] regions) {
		return regions[0];
	}

	@Override
	public List<Command> placeArmies() {
		List<Command> commands = new ArrayList<>();
		commands.add(new NoMovesCommand());
		return commands;
	}

	@Override
	public List<Command> attackTransfer() {
		List<Command> commands = new ArrayList<>();
		commands.add(new NoMovesCommand());
		return commands;
	}

	@Override
	public void onMapReceived() {
		Map map = gameTracker.getMap();
	}

}
