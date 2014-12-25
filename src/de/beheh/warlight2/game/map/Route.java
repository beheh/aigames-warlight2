package de.beheh.warlight2.game.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Benedict Etzel
 */
public class Route extends OrderedRegionGroup {

	public Route() {
		super();
	}

	public Route(Collection<Region> initialRegions) {
		super(initialRegions);
	}

	@Override
	public void add(Region region) {
		if (region == null) {
			throw new IllegalArgumentException("route can't contain null regions");
		}
		Region last = getLast();
		if (last != null && !last.isNeighbor(region)) {
			throw new IllegalArgumentException("route must connect neighbors");
		}
		regions.add(region);
	}

	public int length() {
		return size();
	}

}
