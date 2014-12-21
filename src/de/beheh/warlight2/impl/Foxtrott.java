package de.beheh.warlight2.impl;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.AttackTransferCommand;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import de.beheh.warlight2.stats.ScoreSorter;
import de.beheh.warlight2.stats.Scorer;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
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
			double dynamicScore = superRegion.getBonus() * (superRegion.getRegionCount() - superRegion.getMissingRegions(gameTracker.getPlayer()).size());
			return staticScore * 1 + dynamicScore * 1;
		}
	}

	@Override
	public Region pickStartingRegion(Region[] regions) {
		// choose region with higher static/dynamic rank
		List<Region> startRegionList = Arrays.asList(regions);
		ScoreSorter.sort(startRegionList, new RegionDesirabilityScorer());
		return regions[0];
	}

	@Override
	public PlaceArmiesCommand placeArmies(int armyCount) {
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameTracker);

		Map map = gameTracker.getMap();
		List<Region> ownedRegionsList = map.getRegionsByPlayer(gameTracker.getPlayer());
		ScoreSorter.sort(ownedRegionsList, new Scorer<Region>() {

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

	@Override
	public AttackTransferCommand attackTransfer() {
		AttackTransferCommand command = new AttackTransferCommand(gameTracker);

		return command;
	}
}
