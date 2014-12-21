package de.beheh.warlight2.stats;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Benedict Etzel
 */
public abstract class ScoreSorter {

	public static <E> void sort(List<E> list, Scorer<E> scorer) throws RuntimeException {
		// now sort list by Scorer
		Collections.sort(list, scorer);
		Collections.reverse(list);
	}

}
