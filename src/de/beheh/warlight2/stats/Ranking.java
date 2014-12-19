package de.beheh.warlight2.stats;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Ranking<E> {

	protected List<E> rankList = null;
	protected HashMap<E, Rank> map = new HashMap<>();

	public void addObject(E key, Rank rank) {
		map.put(key, rank);
		rerank();
	}

	protected void rerank() {
		rankList = new LinkedList(map.keySet());
		Collections.sort(rankList, new Comparator<E>() {

			@Override
			public int compare(E object1, E object2) {
				// higher rank score first
				return map.get(object2).compare(map.get(object1));
			}
		});
	}

	public List<E> getRankList() {
		return rankList;
	}

}
