package de.beheh.warlight2;

import de.beheh.warlight2.io.RequestProcessor;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.impl.Quickstep;
import de.beheh.warlight2.io.CommunicationHandler;
import de.beheh.warlight2.io.MapHandler;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Launcher {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			GameTracker gameTracker = new GameTracker();
			Bot bot = new Quickstep(gameTracker);
			MapHandler mapHandler = new MapHandler(gameTracker);
			RequestProcessor requestProcessor = new RequestProcessor(gameTracker, bot);
			CommunicationHandler communicationHandler = new CommunicationHandler(System.in, System.out, mapHandler, gameTracker, requestProcessor);
			System.err.println("Launching " + bot.getClass().getSimpleName());
			communicationHandler.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
