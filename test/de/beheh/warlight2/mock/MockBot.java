package de.beheh.warlight2.mock;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.map.Region;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class MockBot extends Bot {

	public MockBot(GameTracker gameTracker) {
		super(gameTracker);
	}

	public Region pickStartingRegion(Region[] regions) {
		return regions[0];
	}

	public List<Command> placeArmies() {
		List<Command> commands = new ArrayList<>();
		commands.add(new NoMovesCommand());
		return commands;
	}

	public List<Command> attackTransfer() {
		List<Command> commands = new ArrayList<>();
		commands.add(new NoMovesCommand());
		return commands;
	}
}