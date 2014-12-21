package de.beheh.warlight2.impl;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.AttackTransferCommand;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import de.beheh.warlight2.stats.Scorer;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Benedict Etzel
 */
public class Foxtrott extends Bot {

	public Foxtrott(GameTracker gameTracker) {
		super(gameTracker);
	}

	protected class RegionDesirabilityScorer extends Scorer<Region> {

		@Override
		public double score(Region region) {
			SuperRegion superRegion = region.getSuperRegion();
			// static rank => Bonus/TakeOverDifficulty of SuperRegion
			double staticScore = superRegion.getBonus() / (superRegion.getRegionCount());
			// dynamic rank =>
			double dynamicScore = 0;
			if (!region.isOwnedBy(gameTracker.getPlayer())) {
				dynamicScore = superRegion.getBonus() * (superRegion.getRegionCount() - superRegion.getMissingRegions(gameTracker.getPlayer()).size());
			}
			return staticScore * 1 + dynamicScore * 1;
		}
	}

	@Override
	public Region pickStartingRegion(Region[] regions) {
		// choose region with higher static/dynamic rank
		List<Region> startRegionList = Arrays.asList(regions);
		startRegionList.sort(new RegionDesirabilityScorer());
		return regions[0];
	}

	@Override
	public PlaceArmiesCommand placeArmies(int armyCount) {
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameTracker);

		Map map = gameTracker.getMap();
		List<Region> ownedRegionsList = map.getRegionsByPlayer(gameTracker.getPlayer());
		ownedRegionsList.sort(new Scorer<Region>() {

			@Override
			protected double score(Region region) {
				return region.getArmyCount();
			}
		});

		if (ownedRegionsList.size() > 0) {
			command.placeArmy(ownedRegionsList.get(0), armyCount);
		}

		return command;
	}

	protected class RegionAttackTransferDecisionScorer extends Scorer<Region> {

		private final Region origin;

		public RegionAttackTransferDecisionScorer(Region origin) {
			this.origin = origin;
		}

		@Override
		public double score(Region region) {
			return 0;
		}
	}

	@Override
	public AttackTransferCommand attackTransfer() {
		AttackTransferCommand command = new AttackTransferCommand(gameTracker);

		Map map = gameTracker.getMap();

		// rank surrounding regions
		for (Region region : map.getRegionsByPlayer(gameTracker.getPlayer())) {
			List<Region> neighbors = region.getNeighbors();
			neighbors.sort(new RegionAttackTransferDecisionScorer(region));
		}
		
		return command;
	}
}
