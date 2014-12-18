package de.beheh.warlight2.io;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class CommunicationHandler {

	private final Scanner scanner;
	private final PrintWriter writer;
	private final Bot bot;

	public CommunicationHandler(InputStream input, OutputStream output, Bot bot) {
		this.scanner = new Scanner(input);
		this.writer = new PrintWriter(output);
		this.bot = bot;
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

	public void run() throws IOException {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.length() == 0) {
				continue;
			}

			List<Command> commands = null;
			String[] parts = line.split(" ");
			try {
				switch (parts[0]) {
					case "setup_map":
						CommunicationHandler.assertLength(parts, 2, 1);
						switch (parts[1]) {
							case "super_regions":
								CommunicationHandler.assertLength(parts, 2, 1);
								break;
							case "regions":
								CommunicationHandler.assertLength(parts, 2, 1);
								break;
							case "neighbors":

								break;
							case "wastelands":
								CommunicationHandler.assertLength(parts, 2, 1);
								break;
							default:
								CommunicationHandler.unknownCommand(parts[1]);
								break;
						}
						break;
					case "pick_starting_region":
						CommunicationHandler.assertLength(parts, 3, 1); // not valid without regions	
						int[] regions = new int[parts.length - 2];
						for (int i = 2; i < parts.length; i++) {
							regions[i - 2] = Integer.valueOf(parts[i]);
						}
						bot.onPickStartingRegion(Long.valueOf(parts[1]), regions);
						break;
					case "settings":
						CommunicationHandler.assertLength(parts, 2, 1);
						switch (parts[1]) {
							case "time_bank": // bug in live version
							case "timebank":
								CommunicationHandler.assertLength(parts, 3);
								bot.setTimebank(Integer.valueOf(parts[2]));
								break;
							case "time_per_move":
								CommunicationHandler.assertLength(parts, 3);
								bot.setTimePerMove(Integer.valueOf(parts[2]));
								break;
							case "max_rounds":
								CommunicationHandler.assertLength(parts, 3);
								bot.setMaxRounds(Integer.valueOf(parts[2]));
								break;
							case "your_bot":
								CommunicationHandler.assertLength(parts, 3);
								bot.setBotName(parts[2]);
								break;
							case "opponent_bot":
								CommunicationHandler.assertLength(parts, 3);
								bot.setOpponentName(parts[2]);
								break;
							case "starting_armies":
								CommunicationHandler.assertLength(parts, 3);
								bot.setStartingArmies(Integer.valueOf(parts[2]));
								break;
							default:
								CommunicationHandler.unknownCommand(parts[1]);
								break;
						}
						break;
					case "update_map":
						CommunicationHandler.assertLength(parts, 1, 3);
						bot.onUpdateMap();
						break;
					case "opponent_moves":
						CommunicationHandler.assertLength(parts, 1, 1);
						bot.onOpponentMoves();
						break;
					case "go":
						CommunicationHandler.assertLength(parts, 2, 1);
						switch (parts[1]) {
							case "place_armies":
								CommunicationHandler.assertLength(parts, 3);
								commands = bot.onPlaceArmies(Long.valueOf(parts[2]));
								if (commands == null) {
									writer.println(new NoMovesCommand(bot));
								}
								break;
							case "attack/transfer":
								CommunicationHandler.assertLength(parts, 3);
								commands = bot.onAttackTransfer(Long.valueOf(parts[2]));
								if (commands == null) {
									writer.println(new NoMovesCommand(bot));
								}
								break;
							default:
								writer.println(new NoMovesCommand(bot));
								CommunicationHandler.unknownCommand(parts[1]);
								break;
						}
						break;
					default:
						CommunicationHandler.unknownCommand(parts[0]);
						break;
				}
			} catch (IOException e) {
				if (bot.shouldFailHard()) {
					throw e;
				}
				e.printStackTrace(System.err);
				System.err.println("line for previous exception was \"" + line + "\"");
			}
		}
	}
}
