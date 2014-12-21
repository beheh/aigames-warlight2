package de.beheh.warlight2.stats;

import de.beheh.warlight2.game.map.Region;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class RegionRank extends Rank {

	protected Region region;

	public RegionRank(Region region) {
		this.region = region;
	}

	@Override
	public double getScore() {
		return region.getSuperRegion().getBonus();
	}

}
