package de.beheh.warlight2.stats;

import de.beheh.warlight2.game.map.SuperRegion;

/**
 *
 * @author Benedict Etzel
 */
public class SuperRegionRank extends Rank {

	protected SuperRegion superRegion;
	
	public SuperRegionRank(SuperRegion superRegion) {
		this.superRegion = superRegion;
	}

	@Override
	public double getScore() {
		return (2 * superRegion.getBonus()) - Math.pow(superRegion.getRegionCount(), 2);
	}

}
