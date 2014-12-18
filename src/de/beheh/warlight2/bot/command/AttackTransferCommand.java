package de.beheh.warlight2.bot.command;

import de.beheh.warlight2.bot.Bot;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class AttackTransferCommand extends Command {

	public AttackTransferCommand(Bot bot) {
		super(bot);
	}

	@Override
	public String toString() {
		return botName + " attack/transfer";
	}

}
