package de.beheh.warlight2.bot.command;

import de.beheh.warlight2.game.GameState;

/**
 *
 * @author Benedict Etzel
 */
public abstract class Command {

	protected GameState gameTracker;

	public Command(GameState gameTracker) {
		this.gameTracker = gameTracker;
	}

	@Override
	abstract public String toString();

}
