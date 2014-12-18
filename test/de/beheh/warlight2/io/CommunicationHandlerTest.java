package de.beheh.warlight2.io;

import de.beheh.warlight2.map.Map;
import de.beheh.warlight2.mock.MockBot;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class CommunicationHandlerTest {

	/**
	 * Test of assertLength method, of class CommunicationHandler.
	 *
	 * @throws java.io.IOException
	 */
	@Test
	public void testAssertLength_StringArr_int() throws IOException {
		System.out.println("assertLength");
		CommunicationHandler.assertLength("foo bar baz".split(" "), 3);
	}

	/**
	 * Test of assertLength method, of class CommunicationHandler.
	 *
	 * @throws java.io.IOException
	 */
	@Test
	public void testAssertLength_3args() throws IOException {
		System.out.println("assertLength");
		CommunicationHandler.assertLength("foo bar baz".split(" "), 3, 0);
		CommunicationHandler.assertLength("foo bar baz".split(" "), 2, 1);
		CommunicationHandler.assertLength("foo bar baz".split(" "), 1, 2);
		CommunicationHandler.assertLength("foo bar baz".split(" "), 1, 1);
	}

	/**
	 * Test of unknownCommand method, of class CommunicationHandler.
	 *
	 * @throws java.io.IOException
	 */
	@Test(expected = IOException.class)
	public void testUnknownCommand() throws IOException {
		System.out.println("unknownCommand");
		CommunicationHandler.unknownCommand("test");
	}

	/**
	 * Test of run method, of class CommunicationHandler.
	 */
	@Test
	public void testRun() throws IOException {
		System.out.println("run");
		MockBot bot = new MockBot();
		byte[] input = ("settings timebank 10000\n"
				+ "settings time_per_move 500\n"
				+ "settings max_rounds 50\n"
				+ "settings your_bot player1\n"
				+ "settings opponent_bot player2\n"
				+ "setup_map super_regions 1 2 2 5\n"
				+ "setup_map regions 1 1 2 1 3 2 4 2 5 2\n"
				+ "setup_map neighbors 1 2,3,4 2 3 4 5\n"
				+ "setup_map wastelands 3\n"
				+ "pick_starting_region 10000 2 4\n").getBytes();
		InputStream stream = new ByteArrayInputStream(input);
		CommunicationHandler instance = new CommunicationHandler(stream, System.out, new MapHandler(new Map()), bot);
		instance.run();
		assertEquals(10000, bot.getTimebank());
		assertEquals(500, bot.getTimePerMove());
		assertEquals(50, bot.getMaxRounds());
		assertEquals("player1", bot.getBotName());
		assertEquals("player2", bot.getOpponentName());
	}

}
