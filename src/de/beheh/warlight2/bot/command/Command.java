package de.beheh.warlight2.bot.command;

import de.beheh.warlight2.game.GameTracker;

/**
 *
 * @author benedict
 */
public abstract class Command {

	protected GameTracker gameTracker;

	public Command(GameTracker gameTracker) {
		this.gameTracker = gameTracker;
	}

	@Override
	abstract public String toString();

}
