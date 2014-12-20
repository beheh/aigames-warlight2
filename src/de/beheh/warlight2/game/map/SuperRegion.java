package de.beheh.warlight2.game.map;

import de.beheh.warlight2.game.Player;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class SuperRegion extends AbstractRegion {

	protected int bonus;

	public SuperRegion(int id, int bonus) {
		super(id);
		this.bonus = bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

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

	public boolean belongsTo(Player player) {
		Iterator<Region> iterator = regions.iterator();
		while (iterator.hasNext()) {
			if (!iterator.next().getOwner().equals(player)) {
				return false;
			}
		}
		return true;
	}

	public int getOwnedRegionCount(Player player) {
		// @todo optimize with direct counting
		return getOwnedRegions(player).size();
	}

	public List<Region> getOwnedRegions(Player player) {
		List<Region> ownedRegions = new ArrayList<>();
		Iterator<Region> iterator = ownedRegions.iterator();
		while (iterator.hasNext()) {
			Region region = iterator.next();
			if (region.getOwner().equals(player)) {
				ownedRegions.add(region);
			}
		}
		return ownedRegions;
	}

	public int getMissingRegionCount(Player player) {
		// @todo optimize with direct counting
		return getMissingRegions(player).size();
	}

	public List<Region> getMissingRegions(Player player) {
		List<Region> missingRegions = new ArrayList<>();
		Iterator<Region> iterator = regions.iterator();
		while (iterator.hasNext()) {
			Region region = iterator.next();
			if (!player.equals(region.getOwner())) {
				missingRegions.add(region);
			}
		}
		return missingRegions;
	}
}
