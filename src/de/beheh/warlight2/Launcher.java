package de.beheh.warlight2;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.impl.Quickstep;
import de.beheh.warlight2.io.CommunicationHandler;
import de.beheh.warlight2.io.MapHandler;
import de.beheh.warlight2.map.Map;

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
			Bot bot = new Quickstep();
			Map map = new Map();
			MapHandler mapHandler = new MapHandler(map);
			bot.setMap(map);
			CommunicationHandler communicationHandler = new CommunicationHandler(System.in, System.out, mapHandler, bot);
			System.err.println("Launching " + bot.getClass().getSimpleName());
			communicationHandler.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
