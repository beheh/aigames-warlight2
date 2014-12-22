package de.beheh.warlight2.stats;

import java.util.Comparator;

/**
 *
 * @author Benedict Etzel
 * @param <E>
 */
public abstract class Scorer<E> implements Comparator<E> {

	protected double score(E object) {
		return 0;
	}

	protected int group(E object) {
		return 1;
	}

	@Override
	public int compare(E o1, E o2) {
		int group1 = group(o1);
		int group2 = group(o2);
		if (group1 != group2) {
			return Integer.compare(group1, group2);
		}
		return Double.compare(score(o2), score(o1));
	}

}
