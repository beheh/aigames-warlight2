package de.beheh.warlight2.map;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Map {

	protected HashMap<Integer, SuperRegion> superRegions = new HashMap<>();
	protected HashMap<Integer, Region> regions = new HashMap<>();

	public void addSuperRegion(int id, int bonus) {
		superRegions.put(id, new SuperRegion(id, bonus));
	}

	public void addRegion(int id, int superRegion) {
		regions.put(id, new Region(id, superRegions.get(superRegion)));
	}

	public void addNeighbors(int id, int[] neighbors) {
		Region region = regions.get(id);
		for (int neighbor : neighbors) {
			Region linked = regions.get(neighbor);
			region.addNeighbor(linked);
			linked.addNeighbor(region);
		}
	}

	public void setRegionAsWasteland(int id) {
		regions.get(id).setWasteland(true);
	}
}
