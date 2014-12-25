package de.beheh.warlight2.game.map;

import java.util.Collection;
import java.util.Deque;

/**
 *
 * @author Benedict Etzel
 */
public class OrderedRegionGroup extends RegionGroup {

	public OrderedRegionGroup() {
		super();
	}

	public OrderedRegionGroup(Collection<Region> initialRegions) {
		super(initialRegions);
	}

	public Region getFirst() {
		if (regions.isEmpty()) {
			return null;
		}
		return regions.getFirst();
	}

	public Region getLast() {
		if (regions.isEmpty()) {
			return null;
		}
		return regions.getLast();
	}

	public void addFirst(Region region) {
		regions.addFirst(region);
	}

	public Deque<Region> getOrderedRegions() {
		return regions;
	}
}
