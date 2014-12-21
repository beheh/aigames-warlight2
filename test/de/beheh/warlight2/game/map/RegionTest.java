package de.beheh.warlight2.game.map;

import de.beheh.warlight2.mock.MockSuperRegion;
import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benedict Etzel
 */
public class RegionTest {

	/**
	 * Test of routeTo method, of class Region.
	 */
	@Test
	public void testRouteTo() {
		System.out.println("routeTo");
		Region[] regions = new Region[5];
		SuperRegion superRegion = new MockSuperRegion();
		for (int i = 0; i < 5; i++) {
			regions[i] = new Region(i + 1, superRegion);
			if (i > 0) {
				regions[i - 1].addNeighbor(regions[i]);
				regions[i].addNeighbor(regions[i - 1]);
			}
		}
		assertEquals("route to same region should return empty route", new Route(new LinkedList<Region>()), regions[0].routeTo(regions[0]));
		assertNull("unconnected regions should return null", new Region(1, new MockSuperRegion()).routeTo(new Region(99, new MockSuperRegion())));
	}

	/**
	 * Test of distanceTo method, of class Region.
	 */
	@Test
	public void testDistanceTo() {
		System.out.println("distanceTo");
		Region[] regions = new Region[5];
		SuperRegion superRegion = new MockSuperRegion();
		for (int i = 0; i < 5; i++) {
			regions[i] = new Region(i + 1, superRegion);
			if (i > 0) {
				regions[i - 1].addNeighbor(regions[i]);
				regions[i].addNeighbor(regions[i - 1]);
			}
		}
		assertEquals(0, regions[0].distanceTo(regions[0]));
		assertEquals(0, regions[1].distanceTo(regions[1]));
		assertEquals(1, regions[1].distanceTo(regions[2]));
		assertEquals(1, regions[2].distanceTo(regions[1]));
		assertEquals(2, regions[3].distanceTo(regions[1]));
		assertEquals(3, regions[1].distanceTo(regions[4]));
		assertEquals(4, regions[4].distanceTo(regions[0]));
	}

}
