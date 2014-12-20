package de.beheh.warlight2.stats;

import de.beheh.warlight2.game.map.Region;

/**
 *
 * @author benedict
 */
public class ReinforcementRank extends Rank {

	private Region region;

	public ReinforcementRank(Region region) {
		this.region = region;
	}

	@Override
	public double getScore() {
		return region.getPotentialAttackers() - region.getArmyCount();
	}

}
