package de.beheh.warlight2.stats;

import de.beheh.warlight2.game.map.Region;

/**
 *
 * @author benedict
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
