package de.beheh.warlight2.game.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Benedict Etzel
 */
public class Route implements Iterable<Region> {

	protected final LinkedList<Region> route;

	public Route() {
		this.route = new LinkedList<Region>();
	}

	public Route(LinkedList<Region> route) {
		this.route = route;
	}

	public Region getFirst() {
		if (route.isEmpty()) {
			return null;
		}
		return route.getFirst();
	}

	public Region getLast() {
		if (route.isEmpty()) {
			return null;
		}
		return route.getLast();
	}

	public void addBefore(Region region) {
		route.addFirst(region);
	}

	public void add(Region region) {
		if (region == null) {
			throw new IllegalArgumentException("route can't contain null regions");
		}
		Region last = getLast();
		if (last != null && !last.isNeighbor(region)) {
			throw new IllegalArgumentException("route must connect neighbors");
		}
		route.add(region);
	}

	public LinkedList<Region> getRoute() {
		return route;
	}

	public int length() {
		return route.size();
	}

	@Override
	public Iterator<Region> iterator() {
		return getRoute().iterator();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Route route = (Route) obj;
		return route.getRoute().equals(getRoute());
	}

	@Override
	public int hashCode() {
		return route.hashCode();
	}

}
