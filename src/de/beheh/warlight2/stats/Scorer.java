package de.beheh.warlight2.stats;

import java.util.Comparator;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 * @param <E>
 */
public abstract class Scorer<E> implements Comparator<E> {

	protected abstract double score(E object);

	@Override
	public int compare(E o1, E o2) {
		return Double.compare(score(o1), score(o2));
	}

}
