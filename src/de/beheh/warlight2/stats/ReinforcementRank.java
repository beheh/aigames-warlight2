package de.beheh.warlight2.stats;

import de.beheh.warlight2.game.map.Region;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class ReinforcementRank extends Rank {

	private Region region;

	public ReinforcementRank(Region region) {
		this.region = region;
	}

	@Override
	public double getScore() {
		return Math.max(region.getPotentialAttackers() - region.getArmyCount(), 0);
	}

}
