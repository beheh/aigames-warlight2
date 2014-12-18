package de.beheh.warlight2;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.impl.Quickstep;
import de.beheh.warlight2.io.CommunicationHandler;

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
			System.err.println("Initializing " + bot.getClass().getSimpleName());
			CommunicationHandler com = new CommunicationHandler(System.in, System.out, bot);
			com.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
