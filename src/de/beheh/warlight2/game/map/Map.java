package de.beheh.warlight2.game.map;

import de.beheh.warlight2.game.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Map {

	protected HashMap<Integer, SuperRegion> superRegions = new HashMap<>();
	protected HashMap<Integer, Region> regions = new HashMap<>();

	public SuperRegion addSuperRegion(int id, int bonus) {
		SuperRegion superRegion = new SuperRegion(id, bonus);
		superRegions.put(id, superRegion);
		return superRegion;
	}

	public Region addRegion(int id, int superRegionId) {
		SuperRegion superRegion = superRegions.get(superRegionId);
		Region region = new Region(id, superRegion);
		regions.put(id, region);
		superRegion.addRegion(region);
		return region;
	}

	public void addNeighbors(int id, int[] neighbors) {
		Region region = regions.get(id);
		for (int neighbor : neighbors) {
			Region linked = regions.get(neighbor);
			region.addNeighbor(linked);
			linked.addNeighbor(region);
			System.out.println(region + " <-> " + linked);
		}
	}

	public void setRegionAsWasteland(int id) {
		regions.get(id).setWasteland(true);
	}

	public void update(int id, Player owner, int armycount, int round) {
		Region region = regions.get(id);
		region.setOwner(owner);
		region.setArmyCount(armycount);
		region.setLastUpdate(round);
	}

	public Region getRegion(int id) {
		return regions.get(id);
	}

	public SuperRegion getSuperRegion(int id) {
		return superRegions.get(id);
	}

	public List<SuperRegion> getSuperRegions() {
		return new ArrayList<>(superRegions.values());
	}

	public List<Region> getRegionsByPlayer(Player player) {
		List<Region> playerRegions = new ArrayList<>();
		Iterator<Region> iterator = regions.values().iterator();
		while (iterator.hasNext()) {
			Region region = iterator.next();
			if (region.getOwner().equals(player)) {
				playerRegions.add(region);
			}
		}
		return playerRegions;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Map(SuperRegions(");
		Iterator<Region> regionIterator = regions.values().iterator();
		boolean first = true;
		while (regionIterator.hasNext()) {
			if (!first) {
				builder.append(", ");
			} else {
				first = false;
			}
			Region region = regionIterator.next();
			builder.append(region.toString());
		}
		builder.append("), Regions(");
		Iterator<SuperRegion> superRegionItrator = superRegions.values().iterator();
		first = true;
		while (superRegionItrator.hasNext()) {
			if (!first) {
				builder.append(", ");
			} else {
				first = false;
			}
			SuperRegion superRegion = superRegionItrator.next();
			builder.append(superRegion.toString());
		}
		builder.append(")");
		return builder.toString();
	}
}
