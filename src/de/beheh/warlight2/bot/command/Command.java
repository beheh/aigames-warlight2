package de.beheh.warlight2.bot.command;

import de.beheh.warlight2.bot.Bot;

/**
 *
 * @author benedict
 */
public abstract class Command {

	protected final String botName;

	public Command(Bot bot) {
		this.botName = bot.getBotName();
	}

	@Override
	abstract public String toString();

}
