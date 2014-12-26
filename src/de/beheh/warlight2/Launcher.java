package de.beheh.warlight2;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.impl.Foxtrot;
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
			GameState gameState = new GameState();
			Bot bot = new Foxtrot(gameState);
			MapHandler mapHandler = new MapHandler(gameState);
			CommandProcessor requestProcessor = new CommandProcessor(gameState, bot);
			CommunicationHandler communicationHandler = new CommunicationHandler(System.in, System.out, mapHandler, gameState, requestProcessor);
			System.err.println("Launching " + bot.getClass().getSimpleName());
			communicationHandler.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
