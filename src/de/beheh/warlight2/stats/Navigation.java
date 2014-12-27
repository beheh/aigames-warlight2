package de.beheh.warlight2.stats;

import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Benedict Etzel
 */
public abstract class Navigation {

	public static int roundsToConquerSuperRegion(Region region, int maxRounds) {
		return roundsToConquerSuperRegion(region, maxRounds, 5);
	}

	/**
	 * Calculate the number of rounds to conquer a region using iterative
	 * deepening depth-first search.
	 */
	public static int roundsToConquerSuperRegion(Region region, int maxRounds, int wastelandCost) {
		SuperRegion superRegion = region.getSuperRegion();
		int regionCount = superRegion.getRegionCount();
		List<Region> conqueredRegions = new ArrayList<>(regionCount);
		Queue<Region> toConquer = new LinkedList<>();
		toConquer.add(region);
		HashMap<Region, Integer> wastelands = new HashMap<>();
		int round = 0;
		while (conqueredRegions.size() < regionCount && round < maxRounds) {
			// reduce wastelands
			for (java.util.Map.Entry<Region, Integer> wastelandEntry : wastelands.entrySet()) {
				Region wasteland = wastelandEntry.getKey();
				int remainingRounds = wastelandEntry.getValue() - 1;
				if (remainingRounds > 0) {
					wastelands.put(wasteland, remainingRounds);
				} else {
					wastelands.remove(wasteland);
					toConquer.add(wasteland);
				}
			}
			// look for now conqeruable regions
			Queue<Region> toConquerNext = new LinkedList<>();
			while (!toConquer.isEmpty()) {
				Region conquered = toConquer.poll();
				conqueredRegions.add(conquered);
				for (Region neighbor : conquered.getNeighbors()) {
					// don't revisit
					if (conqueredRegions.contains(neighbor) || toConquer.contains(neighbor)) {
						continue;
					}
					if (wastelands.containsKey(neighbor) || !superRegion.equals(neighbor.getSuperRegion())) {
						continue;
					}
					if (neighbor.isWasteland()) {
						wastelands.put(neighbor, wastelandCost);
						continue;
					}
					toConquerNext.add(neighbor);
				}
			}
			toConquer = toConquerNext;
			round++;
		}
		return round - 1;
	}
}
