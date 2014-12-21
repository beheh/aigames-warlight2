package de.beheh.warlight2;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.impl.Foxtrott;
import de.beheh.warlight2.impl.Quickstep;
import de.beheh.warlight2.io.CommunicationHandler;
import de.beheh.warlight2.io.MapHandler;
import de.beheh.warlight2.io.CommandProcessor;

/**
 *
 * @author Benedict Etzel
 */
public class Launcher {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			GameTracker gameTracker = new GameTracker();
			Bot bot = new Foxtrott(gameTracker);
			MapHandler mapHandler = new MapHandler(gameTracker);
			CommandProcessor requestProcessor = new CommandProcessor(gameTracker, bot);
			CommunicationHandler communicationHandler = new CommunicationHandler(System.in, System.out, mapHandler, gameTracker, requestProcessor);
			System.err.println("Launching " + bot.getClass().getSimpleName());
			communicationHandler.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
