package de.beheh.warlight2.io;

import de.beheh.warlight2.RequestProcessor;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.mock.MockBot;
import de.beheh.warlight2.mock.MockRequestProcessor;
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
		GameTracker gameTracker = new GameTracker();
		MockRequestProcessor requestProcessor = new MockRequestProcessor();
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
		CommunicationHandler instance = new CommunicationHandler(stream, System.out, new MapHandler(gameTracker), gameTracker, requestProcessor);
		instance.run();
		assertEquals(10000, gameTracker.getTimebank());
		assertEquals(500, gameTracker.getTimePerMove());
		assertEquals(50, gameTracker.getMaxRounds());
		assertEquals("player1", gameTracker.getPlayer().getName());
		assertEquals("player2", gameTracker.getOpponent().getName());
		Map map = gameTracker.getMap();
		assertEquals(0, map.getRegion(1).distanceTo(map.getRegion(1)));
		assertEquals(1, map.getRegion(1).distanceTo(map.getRegion(2)));
		assertEquals(1, map.getRegion(2).distanceTo(map.getRegion(1)));
		assertEquals(1, map.getRegion(1).distanceTo(map.getRegion(3)));
		assertEquals(1, map.getRegion(2).distanceTo(map.getRegion(3)));
		assertEquals(1, map.getRegion(4).distanceTo(map.getRegion(5)));
		assertEquals(2, map.getRegion(1).distanceTo(map.getRegion(5)));
		assertEquals(2, map.getRegion(5).distanceTo(map.getRegion(1)));
	}
	
}