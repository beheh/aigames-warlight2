package de.beheh.warlight2.game.map;

import de.beheh.warlight2.game.Player;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Benedict Etzel
 */
public class SuperRegion extends AbstractRegion implements Cloneable {

	protected int bonus;

	public SuperRegion(int id, int bonus) {
		super(id);
		this.bonus = bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	/**
	 * Returns the regions bonus.
	 *
	 * @return the regions bonus
	 */
	public int getBonus() {
		return bonus;
	}

	protected List<Region> regions = new ArrayList<>();

	public void addRegion(Region region) {
		regions.add(region);
	}

	public List<Region> getRegions() {
		return regions;
	}

	public int getRegionCount() {
		return regions.size();
	}

	public boolean isOwnedBy(Player player) {
		for(Region region : regions) {
			if(!region.isOwnedBy(player)) {
				return false;
			}
		}
		return true;
	}

	public int getOwnedRegionCount(Player player) {
		return getOwnedRegions(player).size();
	}

	public List<Region> getOwnedRegions(Player player) {
		List<Region> ownedRegions = new ArrayList<>();
		for (Region region : regions) {
			if (region.isOwnedBy(player)) {
				ownedRegions.add(region);
			}
		}
		return ownedRegions;
	}

	public List<Region> getNeutralRegions() {
		return getOwnedRegions(null);
	}

	public int getMissingRegionCount(Player player) {
		return getMissingRegions(player).size();
	}

	public List<Region> getMissingRegions(Player player) {
		List<Region> missingRegions = new ArrayList<>();
		for (Region region : regions) {
			if (!region.isOwnedBy(player)) {
				missingRegions.add(region);
			}
		}
		return missingRegions;
	}

	public int getWastelandCount() {
		int wastelandCount = 0;
		for (Region region : regions) {
			if (region.isWasteland()) {
				wastelandCount++;
			}
		}
		return wastelandCount;
	}

}
