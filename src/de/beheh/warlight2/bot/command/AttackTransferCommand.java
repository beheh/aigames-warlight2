package de.beheh.warlight2.bot.command;

import de.beheh.warlight2.game.GameTracker;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class AttackTransferCommand extends Command {

	public AttackTransferCommand(GameTracker gameTracker) {
		super(gameTracker);
	}

	@Override
	public String toString() {
		return "attack/transfer " + gameTracker.getPlayer();
	}

}
