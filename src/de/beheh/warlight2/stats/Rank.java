package de.beheh.warlight2.stats;

/**
 *
 * @author Benedict Etzel
 */
public abstract class Rank {

	public abstract double getScore();

	public int compare(Rank rank) {
		return Double.compare(getScore(), rank.getScore());
	}
}
