package de.beheh.warlight2.stats;

import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benedict Etzel
 */
public class NavigationTest {
	/**
	 * Test of roundsToConquerSuperRegion method, of class Navigation.
	 */
	@Test
	public void testRoundsToConquerSuperRegion() {
		System.out.println("roundsToConquerSuperRegion");

		SuperRegion superRegion = new SuperRegion(1, 1);
		Region[] regions = new Region[]{new Region(1, superRegion), new Region(2, superRegion), new Region(3, superRegion)};
		for (Region region : regions) {
			superRegion.addRegion(region);
		}
		for (int i = 0; i < regions.length - 1; i++) {
			regions[i].addNeighbor(regions[i + 1]);
			regions[i + 1].addNeighbor(regions[i]);
		}
		assertEquals(2, Navigation.roundsToConquerSuperRegion(regions[0], 5));
		assertEquals(1, Navigation.roundsToConquerSuperRegion(regions[1], 5));
		assertEquals(2, Navigation.roundsToConquerSuperRegion(regions[2], 5));
		regions[2].setWasteland(true);
		assertEquals(6, Navigation.roundsToConquerSuperRegion(regions[0], 20, 5));
		regions[1].setWasteland(true);
		assertEquals(10, Navigation.roundsToConquerSuperRegion(regions[0], 20, 5));
	}

}
