package de.beheh.warlight2.mock;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.map.Region;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class MockBot extends Bot {

	public MockBot(GameTracker gameTracker) {
		super(gameTracker);
	}

	@Override
	public Region pickStartingRegion(Region[] regions) {
		return regions[0];
	}

	@Override
	public Command placeArmies(int armyCount) {
		return null;
	}

	@Override
	public Command attackTransfer() {
		return null;
	}

}
