package de.beheh.warlight2.io;

import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.bot.command.Command;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Benedict Etzel
 */
public class CommunicationHandler {

	private final Scanner scanner;
	private final PrintWriter writer;
	private final MapHandler mapHandler;
	private final GameState gameTracker;
	private final CommandProcessor commandProcessor;

	public CommunicationHandler(InputStream input, OutputStream output, MapHandler mapHandler, GameState gameTracker, CommandProcessor requestProcessor) {
		this.scanner = new Scanner(input);
		this.writer = new PrintWriter(output);
		this.mapHandler = mapHandler;
		this.gameTracker = gameTracker;
		this.commandProcessor = requestProcessor;
	}

	protected static void assertLength(String[] parts, int length) throws IOException {
		CommunicationHandler.assertLength(parts, length, 0);
	}

	protected static void assertLength(String[] parts, int length, int increments) throws IOException {
		if (parts.length < length || (increments != 0 && (parts.length - length) % increments != 0)) {
			String multiples = "";
			if (increments > 0) {
				multiples = " + multiples of " + increments + "";
			}
			throw new IOException("invalid parameter count (expected " + length + multiples + ", got " + parts.length + ")");
		}
	}

	protected static void unknownCommand(String command) throws IOException {
		throw new IOException("unknown command \"" + command + "\"");
	}

	protected static int[] castIntegerParameters(String[] array, int from) {
		int[] list = new int[array.length - from];
		for (int i = from; i < array.length; i++) {
			list[i - from] = Integer.valueOf(array[i]);
		}
		return list;
	}

	public void run() throws IOException {
		boolean mapReceived = false;

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.length() == 0) {
				continue;
			}

			List<Command> commands = null;
			String[] parts = line.split(" ");
			int[] list = null;
			try {
				switch (parts[0]) {
					case "setup_map":
						CommunicationHandler.assertLength(parts, 2, 1);
						switch (parts[1]) {
							case "super_regions":
								CommunicationHandler.assertLength(parts, 2, 1);
								mapHandler.setSuperRegions(CommunicationHandler.castIntegerParameters(parts, 2));
								break;
							case "regions":
								CommunicationHandler.assertLength(parts, 2, 1);
								mapHandler.setRegions(CommunicationHandler.castIntegerParameters(parts, 2));
								break;
							case "neighbors":
								CommunicationHandler.assertLength(parts, 2, 1);
								mapHandler.setNeighbors(Arrays.copyOfRange(parts, 2, parts.length));
								break;
							case "wastelands":
								CommunicationHandler.assertLength(parts, 2, 1);
								mapHandler.setWastelands(CommunicationHandler.castIntegerParameters(parts, 2));
								break;
							case "opponent_starting_regions":
								CommunicationHandler.assertLength(parts, 2, 1);
								mapHandler.setOpponentStartingRegions(CommunicationHandler.castIntegerParameters(parts, 2));
								break;
							default:
								CommunicationHandler.unknownCommand(parts[1]);
								break;
						}
						break;
					case "pick_starting_region":
						// we assume we received the map by now
						if (!mapReceived) {
							commandProcessor.mapComplete();
							mapReceived = true;
						}
						CommunicationHandler.assertLength(parts, 3, 1); // not valid without regions	
						writer.println(commandProcessor.pickStartingRegion(Long.valueOf(parts[1]), CommunicationHandler.castIntegerParameters(parts, 2)));
						break;
					case "settings":
						CommunicationHandler.assertLength(parts, 2, 1);
						switch (parts[1]) {
							case "time_bank": // bug in live version
							case "timebank":
								CommunicationHandler.assertLength(parts, 3);
								gameTracker.setTimebank(Integer.valueOf(parts[2]));
								break;
							case "time_per_move":
								CommunicationHandler.assertLength(parts, 3);
								gameTracker.setTimePerMove(Integer.valueOf(parts[2]));
								break;
							case "max_rounds":
								CommunicationHandler.assertLength(parts, 3);
								gameTracker.setMaxRounds(Integer.valueOf(parts[2]));
								break;
							case "your_bot":
								CommunicationHandler.assertLength(parts, 3);
								gameTracker.setBotName(parts[2]);
								break;
							case "opponent_bot":
								CommunicationHandler.assertLength(parts, 3);
								gameTracker.setOpponentName(parts[2]);
								break;
							case "starting_armies":
								CommunicationHandler.assertLength(parts, 3);
								gameTracker.setStartingArmies(Integer.valueOf(parts[2]));
								break;
							case "starting_regions":
								//@todo implement starting_regions
								break;
							case "starting_pick_amount":
								CommunicationHandler.assertLength(parts, 3);
								gameTracker.setStartingPickAmount(Integer.valueOf(parts[2]));
								break;
							default:
								CommunicationHandler.unknownCommand(parts[1]);
								break;
						}
						break;
					case "update_map":
						CommunicationHandler.assertLength(parts, 1, 3);
						mapHandler.updateMap(Arrays.copyOfRange(parts, 1, parts.length));
						break;
					case "opponent_moves":
						CommunicationHandler.assertLength(parts, 1, 1);
						// map changes are handled by update map
						mapHandler.opponentMoves(Arrays.copyOfRange(parts, 1, parts.length)); // check for lost regions
						commandProcessor.opponentMoves(Arrays.copyOfRange(parts, 1, parts.length)); // notify bot
						break;
					case "go":
						CommunicationHandler.assertLength(parts, 2, 1);
						long startTime = System.currentTimeMillis();
						long remainingTime = Long.valueOf(parts[2]);
						switch (parts[1]) {
							case "place_armies":
								CommunicationHandler.assertLength(parts, 3);
								// we assume new round
								gameTracker.nextRound();
								if (gameTracker.getRound() == 1) {
									commandProcessor.pickingComplete();
								} else {
									commandProcessor.roundComplete();
								}
								remainingTime -= System.currentTimeMillis() - startTime;
								writer.println(commandProcessor.placeArmies(remainingTime));
								break;
							case "attack/transfer":
								CommunicationHandler.assertLength(parts, 3);
								writer.println(commandProcessor.attackTransfer(remainingTime));
								break;
							default:
								writer.println("unknown command");
								CommunicationHandler.unknownCommand(parts[1]);
								break;
						}
						break;
					default:
						CommunicationHandler.unknownCommand(parts[0]);
						break;
				}
			} catch (IOException | RuntimeException e) {
				e.printStackTrace(System.err);
				System.err.println("line for previous exception was \"" + line + "\"");
			}
			writer.flush();
		}
	}
}
