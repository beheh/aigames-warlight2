package de.beheh.warlight2.stats;

import de.beheh.warlight2.game.map.Region;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class SuperRegionRank extends Rank {

	protected int bonus;
	protected List<Region> regions;

	public SuperRegionRank(int bonus, List<Region> regions) {
		this.bonus = bonus;
		this.regions = regions;
	}

	@Override
	public double getScore() {
		return (2 * bonus) - Math.pow(regions.size(), 2);
	}

}
