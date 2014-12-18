package de.beheh.warlight2.game.map;

import de.beheh.warlight2.game.Player;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
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
		if (!neighbors.contains(neighbor)) {
			neighbors.add(neighbor);
		}
	}

	public List<Region> getNeighbors() {
		return neighbors;
	}

	public boolean isNeighbor(Region neighbor) {
		return neighbors.contains(neighbor);
	}

	protected int armycount = 0;

	public void setArmyCount(int armies) {
		this.armycount = armies;
	}

	public int getArmyCount() {
		return armycount;
	}

	protected Player owner;

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Player getOwner() {
		return owner;
	}

	public int distanceTo(Region region) {
		if (region.equals(this)) {
			return 0;
		}
		return -1;
	}

	protected int lastUpdate;

	public void setLastUpdate(int lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getLastUpdate() {
		return lastUpdate;
	}

}
