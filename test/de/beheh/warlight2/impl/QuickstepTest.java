/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.beheh.warlight2.impl;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.io.CommunicationHandler;
import de.beheh.warlight2.io.MapHandler;
import de.beheh.warlight2.io.RequestProcessor;
import de.beheh.warlight2.mock.MockRequestProcessor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author benedict
 */
public class QuickstepTest {

	public QuickstepTest() {
	}

	@BeforeClass
	public static void setUpClass() throws IOException {
		GameTracker gameTracker = new GameTracker();
		Bot bot = new Quickstep(gameTracker);
		RequestProcessor requestProcessor = new RequestProcessor(gameTracker, bot);
		byte[] input = ("settings timebank 10000\n"
				+ "settings time_per_move 500\n"
				+ "settings max_rounds 50\n"
				+ "settings your_bot player1\n"
				+ "settings opponent_bot player2\n"
				+ "setup_map super_regions 1 2 2 5\n"
				+ "setup_map regions 1 1 2 1 3 2 4 2 5 2\n"
				+ "setup_map neighbors 1 2,3,4 2 3 4 5\n"
				+ "setup_map wastelands 3\n"
				+ "pick_starting_region 10000 2 4\n"
				+ "settings starting_armies 7\n"
				+ "update_map 1 player1 2 2 player1 4 3 neutral 10 4 player2 5\n"
				+ "go place_armies 10000\n"
				+ "go attack/transfer 10000\n").getBytes();
		InputStream stream = new ByteArrayInputStream(input);
		CommunicationHandler instance = new CommunicationHandler(stream, System.out, new MapHandler(gameTracker), gameTracker, requestProcessor);
		instance.run();
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of onMapComplete method, of class Quickstep.
	 */
	@Test
	public void testOnMapComplete() {

	}

	/**
	 * Test of pickStartingRegion method, of class Quickstep.
	 */
	@Test
	public void testPickStartingRegion() {
		System.out.println("pickStartingRegion");
		Region[] regions = null;
		Quickstep instance = null;
		Region expResult = null;
		Region result = instance.pickStartingRegion(regions);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of onPickingComplete method, of class Quickstep.
	 */
	@Test
	public void testOnPickingComplete() {
		System.out.println("onPickingComplete");
		Quickstep instance = null;
		instance.onPickingComplete();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of placeArmies method, of class Quickstep.
	 */
	@Test
	public void testPlaceArmies() {
		System.out.println("placeArmies");
		int armyCount = 0;
		Quickstep instance = null;
		Command expResult = null;
		Command result = instance.placeArmies(armyCount);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of attackTransfer method, of class Quickstep.
	 */
	@Test
	public void testAttackTransfer() {
		System.out.println("attackTransfer");
		Quickstep instance = null;
		Command expResult = null;
		Command result = instance.attackTransfer();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of onRoundComplete method, of class Quickstep.
	 */
	@Test
	public void testOnRoundComplete() {
		System.out.println("onRoundComplete");
		Quickstep instance = null;
		instance.onRoundComplete();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of rankRegions method, of class Quickstep.
	 */
	@Test
	public void testRankRegions() {
		System.out.println("rankRegions");
		Quickstep instance = null;
		instance.rankRegions();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
