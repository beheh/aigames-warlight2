package de.beheh.warlight2.game.map;

import de.beheh.warlight2.mock.MockSuperRegion;
import java.util.Arrays;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Benedict Etzel
 */
public class RouteTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test of length method, of class Route.
	 */
	@Test
	public void testLength() {
		System.out.println("length");
		Region region1 = new Region(1, new MockSuperRegion());
		Region region2 = new Region(2, new MockSuperRegion());
		Region region3 = new Region(3, new MockSuperRegion());
		region1.addNeighbor(region2);
		region2.addNeighbor(region1);
		region1.addNeighbor(region3);
		region3.addNeighbor(region1);
		Route instance = new Route();
		assertEquals("empty route should have length 0", 0, instance.length());
		instance.add(region1);
		assertEquals(1, instance.length());
		instance.add(region2);
		assertEquals(2, instance.length());
		instance.addFirst(region3);
		assertEquals(3, instance.length());
	}

	/**
	 * Test of equals method, of class Route.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		assertThat(new Route(), is(new Route()));
		assertThat(new Route(new LinkedList<Region>()), is(new Route(new LinkedList<Region>())));
		Region region1 = new Region(1, new MockSuperRegion());
		Region region2 = new Region(1, new MockSuperRegion());
		Route route = new Route();
		route.add(region2);
		route.addFirst(region1);
		assertThat(route, is(new Route(new LinkedList<Region>(Arrays.asList(new Region[]{region1, region2})))));
		route = new Route();
	}

}
