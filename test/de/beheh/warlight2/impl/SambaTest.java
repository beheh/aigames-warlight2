package de.beheh.warlight2.impl;

import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benedict Etzel
 */
public class SambaTest {

	/**
	 * Test of pickStartingRegion method, of class Jive.
	 */
	@Test
	public void testPickStartingRegion() {
		System.out.println("pickStartingRegion");
		Samba jive = new Samba(new GameState());

		SuperRegion[] super1 = new SuperRegion[]{new SuperRegion(1, 10), new SuperRegion(2, 0)};
		Region[] regions1 = new Region[]{new Region(1, super1[0]), new Region(2, super1[1])};
		super1[0].addRegion(regions1[0]);
		super1[1].addRegion(regions1[1]);
		regions1[0].addNeighbor(regions1[1]);
		regions1[1].addNeighbor(regions1[0]);
		List<Region> regions1List = Arrays.asList(Arrays.copyOf(regions1, regions1.length)); // need to copy array, otherwise Collections.reverse will be reflected in array
		assertEquals("should prefer same-size region with higher bonus", regions1[0], jive.pickStartingRegion((Region[]) regions1List.toArray()));
		Collections.reverse(regions1List);
		assertEquals("should prefer same-size region with higher bonus", regions1[0], jive.pickStartingRegion((Region[]) regions1List.toArray()));

		SuperRegion[] super2 = new SuperRegion[]{new SuperRegion(1, 1), new SuperRegion(2, 1)};
		Region[] regions2 = new Region[]{new Region(1, super2[0]), new Region(2, super2[1])};
		Region region2 = new Region(3, super2[1]);
		super2[0].addRegion(regions2[0]);
		super2[1].addRegion(regions2[1]);
		super2[1].addRegion(region2);
		regions2[0].addNeighbor(regions2[1]);
		regions2[1].addNeighbor(regions2[0]);
		regions2[1].addNeighbor(region2);
		region2.addNeighbor(regions2[1]);
		List<Region> regions2List = Arrays.asList(Arrays.copyOf(regions2, regions2.length));
		assertEquals("should prefer smaller super region with same bonus", regions2[0], jive.pickStartingRegion((Region[]) regions2List.toArray()));
		Collections.reverse(regions2List);
		assertEquals("should prefer smaller super region with same bonus", regions2[0], jive.pickStartingRegion((Region[]) regions2List.toArray()));

		SuperRegion[] super3 = new SuperRegion[]{new SuperRegion(1, 5), new SuperRegion(2, 5)};
		Region[] regions3 = new Region[]{new Region(1, super2[0]), new Region(2, super2[1])};
		Region region3_1 = new Region(3, super3[0]);
		Region region3_2 = new Region(4, super3[1]);
		region3_2.setWasteland(true);
		super3[0].addRegion(regions3[0]);
		super3[0].addRegion(region3_1);
		super3[1].addRegion(regions3[1]);
		super3[1].addRegion(region3_2);
		List<Region> regions3List = Arrays.asList(Arrays.copyOf(regions3, regions3.length));
		assertEquals("should prefer lower bonus when same-size and wastelanded", regions3[0], jive.pickStartingRegion((Region[]) regions3List.toArray()));
		Collections.reverse(regions3List);
		assertEquals("should prefer lower bonus when same-size and wastelanded", regions3[0], jive.pickStartingRegion((Region[]) regions3List.toArray()));
	}

}
