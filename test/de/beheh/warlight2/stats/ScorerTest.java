package de.beheh.warlight2.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Benedict Etzel
 */
public class ScorerTest {

	/**
	 * Test of score method, of class Scorer.
	 */
	@Test
	public void testScore() {
		System.out.println("compare");
		List<Integer> list = new ArrayList<>(Arrays.asList(new Integer[]{2, 3, 1, 4, 5}));

		Collections.sort(list, new Scorer<Integer>() {

			@Override
			protected double score(Integer integer) {
				return integer;
			}
		});
		assertArrayEquals(new Integer[]{5, 4, 3, 2, 1}, list.toArray());

		Collections.sort(list, new Scorer<Integer>() {

			@Override
			protected double score(Integer integer) {
				return 5 - integer;
			}
		});
		assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, list.toArray());

		Collections.sort(list, new Scorer<Integer>() {

			@Override
			protected double score(Integer integer) {
				return -integer;
			}
		});
		assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, list.toArray());
	}

	/**
	 * Test of group method, of class Scorer.
	 */
	@Test
	public void testGroup() {
		System.out.println("group");
		List<Integer> list = new ArrayList<Integer>(Arrays.asList(new Integer[]{2, 3, 1, 4, 5}));

		Collections.sort(list, new Scorer<Integer>() {

			@Override
			protected int group(Integer integer) {
				return integer <= 3 ? 1 : 2;
			}

			@Override
			protected double score(Integer integer) {
				return integer;
			}
		});
		assertArrayEquals(new Integer[]{3, 2, 1, 5, 4}, list.toArray());

		Collections.sort(list, new Scorer<Integer>() {

			@Override
			protected int group(Integer integer) {
				return integer > 3 ? 1 : 2;
			}

			@Override
			protected double score(Integer integer) {
				return -integer;
			}
		});
		assertArrayEquals(new Integer[]{4, 5, 1, 2, 3}, list.toArray());

	}

	/**
	 * Test of compare method, of class Scorer.
	 */
	@Test
	public void testCompare() {
		System.out.println("compare");

		Scorer scorer = new Scorer<Integer>() {

			@Override
			protected double score(Integer integer) {
				return integer;
			}
		};

		assertEquals(0, scorer.compare(0, 0));
		assertEquals(0, scorer.compare(1, 1));
		assertEquals(0, scorer.compare(-1, -1));
		assertEquals(1, scorer.compare(1, 2));
		assertEquals(1, scorer.compare(-1, 1));
		assertEquals(-1, scorer.compare(2, 1));
		assertEquals(-1, scorer.compare(1, -1));
	}

}
