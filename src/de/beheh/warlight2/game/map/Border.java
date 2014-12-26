package de.beheh.warlight2.game.map;

/**
 *
 * @author Benedict Etzel
 */
public class Border extends RegionGroup {

	public int getPotentialAttackers() {
		int potentialAttackers = 0;
		for (Region region : regions) {
			potentialAttackers += region.getPotentialAttackers();
		}
		return potentialAttackers;
	}

}
