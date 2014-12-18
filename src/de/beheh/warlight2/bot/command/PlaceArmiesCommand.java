package de.beheh.warlight2.bot.command;

import de.beheh.warlight2.bot.Bot;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class PlaceArmiesCommand extends Command {

	public PlaceArmiesCommand(Bot bot) {
		super(bot);
	}

	@Override
	public String toString() {
		return botName + " place_armies";
	}

}
