package de.beheh.warlight2.game.map;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Benedict Etzel
 */
public class RegionGroup implements Iterable<Region> {

	protected final LinkedList<Region> regions;

	public RegionGroup() {
		this.regions = new LinkedList<>();
	}

	public RegionGroup(Collection<Region> initialRegions) {
		this.regions = new LinkedList<>(initialRegions);
	}

	public final Collection<Region> getRegions() {
		return regions;
	}

	public void add(Region region) {
		regions.add(region);
	}

	@Override
	public final Iterator<Region> iterator() {
		return regions.iterator();
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		RegionGroup regionGroup = (RegionGroup) obj;
		return regions.equals(regionGroup.getRegions());
	}

	@Override
	public final int hashCode() {
		return regions.hashCode();
	}

	public final int size() {
		return regions.size();
	}

	@Override
	public String toString() {
		return Arrays.toString(regions.toArray());
	}

}
