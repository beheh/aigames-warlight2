package de.beheh.warlight2.game.map;

import de.beheh.warlight2.game.Player;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Benedict Etzel
 */
public class Map implements Cloneable, Serializable {

	protected final HashMap<Integer, SuperRegion> superRegions;
	protected final HashMap<Integer, Region> regions;

	public Map() {
		this.superRegions = new HashMap<>();
		this.regions = new HashMap<>();
	}

	public Map(HashMap<Integer, SuperRegion> superRegions, HashMap<Integer, Region> regions) {
		this.superRegions = superRegions;
		this.regions = regions;
	}

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
		}
	}

	public void setRegionAsWasteland(int id) {
		Region region = regions.get(id);
		region.setWasteland(true);
	}

	public void update(int id, Player owner, Integer armycount, int round) {
		Region region = regions.get(id);
		region.setOwner(owner);
		region.setArmyCount(armycount);
		region.resetSchedule();
		region.setLastUpdate(round);
	}

	public Region getRegion(int id) {
		return regions.get(id);
	}

	public Collection<Region> getRegions() {
		return regions.values();
	}

	public int getRegionCount() {
		return regions.size();
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
			if (region.getOwner() != null && region.getOwner().equals(player)) {
				playerRegions.add(region);
			}
		}
		return playerRegions;
	}

	public void verifyRegionLost(int id, Player attacker, int round) {
		Region region = regions.get(id);
		if (region.isNeutral() || region.isOwnedBy(attacker)) {
			return;
		}
		if (region.getLastUpdate() == round) {
			return;
		}
		System.err.println(getClass().getSimpleName() + ": lost region " + region + " during round #" + round + " (expected an update, was last updated " + region.getLastUpdate() + ")");
		region.setOwner(attacker);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Map(Regions(");
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
		builder.append("), SuperRegions(");
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
