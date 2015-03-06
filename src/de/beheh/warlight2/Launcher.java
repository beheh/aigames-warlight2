package de.beheh.warlight2;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.io.CommunicationHandler;
import de.beheh.warlight2.io.MapHandler;
import de.beheh.warlight2.io.CommandProcessor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
			String botName = "de.beheh.warlight2.impl.Foxtrot";
			if (args.length >= 1) {
				botName = args[0];
			}
			GameState gameState = new GameState();
			Bot bot = Launcher.createBot(botName, gameState);
			MapHandler mapHandler = new MapHandler(gameState);
			CommandProcessor requestProcessor = new CommandProcessor(gameState, bot);
			CommunicationHandler communicationHandler = new CommunicationHandler(System.in, System.out, mapHandler, gameState, requestProcessor);
			System.err.println("Launching " + bot.getClass().getSimpleName());
			communicationHandler.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public static Bot createBot(String botName, GameState gameState) throws ClassNotFoundException, NoSuchMethodException, ReflectiveOperationException, IllegalAccessException, InvocationTargetException {
		Class botClass = Class.forName(botName);
		
		Class[] types =	{GameState.class};
		Constructor<Bot> botConstructor = botClass.getConstructor(types);

		Object[] params = {gameState};
		return botConstructor.newInstance(params);
	}

}
