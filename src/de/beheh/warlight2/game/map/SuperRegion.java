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
}
