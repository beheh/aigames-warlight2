package de.beheh.warlight2.bot;

import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.game.map.Region;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public abstract class Bot {

	public final GameTracker gameTracker;

	public Bot(GameTracker gameTracker) {
		this.gameTracker = gameTracker;
	}

	public final Player getOwner() {
		return gameTracker.getPlayer();
	}

	public abstract Region pickStartingRegion(Region[] regions);

	public abstract List<Command> placeArmies();

	public abstract List<Command> attackTransfer();

	public void onOpponentMoves() {

	}
}
