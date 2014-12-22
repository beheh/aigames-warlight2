package de.beheh.warlight2.game.map;

import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.stats.Scorer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Benedict Etzel
 */
public class Region extends AbstractRegion {

	protected SuperRegion superRegion;

	public Region(int id, SuperRegion superRegion) {
		super(id);
		this.superRegion = superRegion;
	}

	public void setSuperRegion(SuperRegion superRegion) {
		this.superRegion = superRegion;
	}

	public SuperRegion getSuperRegion() {
		return superRegion;
	}

	protected boolean wasteland = false;

	public void setWasteland(boolean wasteland) {
		this.wasteland = wasteland;
	}

	public boolean isWasteland() {
		return wasteland;
	}

	protected List<Region> neighbors = new ArrayList<>();

	public void addNeighbor(Region neighbor) {
		if (!equals(neighbor) && !neighbors.contains(neighbor)) {
			neighbors.add(neighbor);
		}
	}

	public List<Region> getNeighbors() {
		return neighbors;
	}

	public List<Region> getNeutralNeighbors() {
		return getNeighborsByPlayer(null);
	}

	public List<Region> getNeighborsByPlayer(Player player) {
		List<Region> playerNeighbors = new ArrayList<>(neighbors.size());
		for (Region neighbor : neighbors) {
			if (neighbor.isOwnedBy(player)) {
				playerNeighbors.add(neighbor);
			}
		}
		return playerNeighbors;
	}

	public boolean isNeighbor(Region neighbor) {
		return neighbors.contains(neighbor);
	}

	protected int armycount = 0;

	public void setArmyCount(int armies) {
		this.armycount = armies;
	}

	protected int schedule = 0;

	public void scheduleIncreaseArmy(int by) {
		schedule += by;
	}

	public void commitSchedule() {
		armycount += schedule;
		schedule = 0;
	}

	public void increaseArmy(int by) {
		armycount += by;
	}

	public void decreaseArmy(int by) {
		armycount -= by;
	}

	public int getArmyCount() {
		return armycount;
	}

	public int getScheduledArmyCount() {
		return armycount + schedule;
	}

	public void resetSchedule() {
		schedule = 0;
	}

	protected Player owner;

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Player getOwner() {
		return owner;
	}

	public boolean isOwnedBy(Player player) {
		if (player == null) {
			return owner == null;
		}
		return player.equals(owner);
	}

	public boolean isNeutral() {
		return owner == null;
	}

	public boolean isHostile(Player player) {
		return !isNeutral() && !isOwnedBy(player);
	}

	public Route routeTo(Region region) {
		return routeTo(region, null, null);
	}

	public Route routeTo(Region region, Player player) {
		return routeTo(region, player, null);
	}

	public Route routeTo(Region region, Scorer<Region> scorer) {
		return routeTo(region, null, scorer);
	}

	public Route routeTo(Region region, Player player, Scorer<Region> scorer) {
		if (region == null) {
			throw new IllegalArgumentException("region cannot be null");
		}
		HashMap<Region, Region> parents = new HashMap<>();
		Queue<Region> searchQueue = new LinkedList<>();
		searchQueue.add(this);
		while (!searchQueue.isEmpty()) {
			Region currentRegion = searchQueue.poll();
			if (currentRegion.equals(region)) {
				LinkedList<Region> routeList = new LinkedList<>();
				Region parent = currentRegion;
				while (!parent.equals(this)) {
					routeList.addFirst(parent);
					parent = parents.get(parent);
				}
				return new Route(routeList);
			}
			List<Region> sortedNeighbors = new ArrayList<>(currentRegion.getNeighbors());
			if (scorer != null) {
				Collections.sort(neighbors, scorer);
			}
			for (Region neighbor : sortedNeighbors) {
				if (neighbor.equals(this) || parents.containsKey(neighbor)) {
					continue;
				}
				if (player != null && !neighbor.isOwnedBy(player) && !neighbor.equals(region)) {
					continue;
				}
				parents.put(neighbor, currentRegion);
				searchQueue.add(neighbor);
			}
		}

		return null;

		/*
		 if (isSearching) {
		 return null;
		 }

		 Route bestRoute = null;
		 isSearching = true;
		 List<Region> sortedNeighbors = new ArrayList<>(neighbors);
		 if (scorer != null) {
		 Collections.sort(neighbors, scorer);
		 }
		 for (Region neighbor : sortedNeighbors) {
		 Route neighborRoute = neighbor.routeTo(region);
		 if (neighborRoute == null) {
		 continue;
		 }
		 neighborRoute.addBefore(region);
		 //System.out.println("from " + neighbor + " to " + region + ": got route length " + neighborRoute.length());
		 if (bestRoute == null || neighborRoute.length() < bestRoute.length()) {
		 bestRoute = neighborRoute;
		 }
		 }
		 isSearching = false;
		 //System.out.println(Arrays.toString(route.getRoute().toArray()));
		 return bestRoute;*/
	}

	public int distanceTo(Region region) {
		Route route = routeTo(region);
		if (route == null) {
			return -1;
		}
		return route.length();
	}

	public int playerDistance(Player player) {
		return playerDistance(player, 5);
	}

	protected boolean isSearching = false;

	public int playerDistance(Player player, int maxDepth) {
		if (maxDepth == 0) {
			return -1;
		}
		if (owner != null && owner.equals(player)) {
			return 0;
		}
		if (isSearching) {
			return -1;
		}
		Iterator<Region> iterator = neighbors.iterator();
		int shortestDistance = -1;
		isSearching = true;
		while (iterator.hasNext()) {
			int distance = iterator.next().playerDistance(player, maxDepth - 1);
			if (distance != -1 && (shortestDistance == -1 || distance < shortestDistance)) {
				shortestDistance = distance + 1;
			}
		}
		isSearching = false;
		return shortestDistance;
	}

	protected Integer lastUpdate = null;

	public void setLastUpdate(int lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Integer getLastUpdate() {
		return lastUpdate;
	}

	public int getPotentialAttackers() {
		int potentialAttackers = 0;
		for (Region neighbor : neighbors) {
			if (neighbor.isHostile(owner)) {
				potentialAttackers += neighbor.getArmyCount() - 1;
			}
		}
		return potentialAttackers;
	}

}
