package de.beheh.warlight2.stats;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benedict Etzel
 */
public class EngagementTest {

	/**
	 * Test of isGuaranteed method, of class Engagement.
	 */
	@Test
	public void testIsGuaranteed() {
		assertTrue("3v1 must be guaranteed", Engagement.isGuaranteed(3, 1));
		assertTrue("3v2 must be guaranteed", Engagement.isGuaranteed(3, 2));
		assertTrue("4v2 must be guaranteed", Engagement.isGuaranteed(4, 2));
		assertTrue("4v1 must be guaranteed", Engagement.isGuaranteed(4, 1));
		assertFalse("1v1 cannot be guaranteed", Engagement.isGuaranteed(1, 1));
		assertFalse("4v3 cannot be guaranteed", Engagement.isGuaranteed(4, 3));
	}

	/**
	 * Test of dealDamage method, of class Engagement.
	 */
	@Test
	public void testDealDamage() {
		assertEquals("3v2 must kill all defenders", 0, Engagement.dealDamage(3, 2, 0.6, 0));
		assertEquals("1v100 must kill at 1 defender", 99, Engagement.dealDamage(1, 100, 0.6, 0));
		assertEquals("1v1000 must kill at 1 defender", 999, Engagement.dealDamage(1, 1000, 0.6, 0));
	}

}
