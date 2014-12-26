package de.beheh.warlight2.game.map;

import de.beheh.warlight2.game.Player;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Benedict Etzel
 */
public class Border extends RegionGroup {

	public int getPotentialAttackers() {
		int potentialAttackers = 0;
		for (Region region : regions) {
			potentialAttackers += region.getPotentialAttackers();
		}
		return potentialAttackers;
	}

	public Player getOwner() {
		if (regions.size() > 0) {
			return regions.get(0).getOwner();
		}
		return null;
	}

	public List<Region> getEnclosedRegions() {
		List<Region> regionList = new LinkedList<>();
		Queue<Region> searchQueue = new LinkedList<>();
		for (Region region : regions) {
			for (Region neighbor : region.getNeighbors()) {
				if (neighbor.isOwnedBy(getOwner())) {
					continue;
				}
				if (searchQueue.contains(neighbor)) {
					continue;
				}
				searchQueue.add(neighbor);
			}
		}
		while (!searchQueue.isEmpty()) {
			Region region = searchQueue.poll();
			regionList.add(region);
			for (Region neighbor : region.getNeighbors()) {
				if (neighbor.isOwnedBy(getOwner())) {
					continue;
				}
				if (searchQueue.contains(neighbor) || regionList.contains(neighbor)) {
					continue;
				}
				searchQueue.add(neighbor);
			}
		}
		return regionList;
	}

}
