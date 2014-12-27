package de.beheh.warlight2.impl;

import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benedict Etzel
 */
public class JiveTest {

	/**
	 * Test of pickStartingRegion method, of class Jive.
	 */
	@Test
	public void testPickStartingRegion() {
		Jive jive = new Jive(new GameState());

		SuperRegion[] super1 = new SuperRegion[]{new SuperRegion(1, 10), new SuperRegion(2, 0)};
		Region[] regions1 = new Region[]{new Region(1, super1[0]), new Region(2, super1[1])};
		super1[0].addRegion(regions1[0]);
		super1[1].addRegion(regions1[1]);
		assertEquals("should prefer same-size region with higher bonus", regions1[0], jive.pickStartingRegion(regions1));

		SuperRegion[] super2 = new SuperRegion[]{new SuperRegion(1, 10), new SuperRegion(2, 0)};
		Region[] regions2 = new Region[]{new Region(2, super2[1]), new Region(1, super2[0])};
		super2[0].addRegion(regions2[0]);
		super2[1].addRegion(regions2[1]);
		assertEquals("should prefer same-size region with higher bonus", regions2[1], jive.pickStartingRegion(regions2));

		SuperRegion[] super3 = new SuperRegion[]{new SuperRegion(1, 1), new SuperRegion(2, 1)};
		Region[] regions3 = new Region[]{new Region(1, super3[0]), new Region(2, super3[1])};
		Region region3 = new Region(3, super3[1]);
		super3[0].addRegion(regions3[0]);
		super3[1].addRegion(regions3[1]);
		super3[1].addRegion(region3);
		assertEquals("should prefer smaller super region with same bonus", regions3[0], jive.pickStartingRegion(regions3));

		SuperRegion[] super4 = new SuperRegion[]{new SuperRegion(1, 1), new SuperRegion(2, 1)};
		Region[] regions4 = new Region[]{new Region(1, super3[1]), new Region(2, super3[0])};
		Region region4 = new Region(3, super4[0]);
		super4[0].addRegion(regions4[0]);
		super4[0].addRegion(region4);
		super4[1].addRegion(regions4[1]);
		assertEquals("should prefer smaller super region with same bonus", regions4[1], jive.pickStartingRegion(regions4));

	}

}
