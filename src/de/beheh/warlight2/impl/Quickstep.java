package de.beheh.warlight2.impl;

import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.AttackTransferCommand;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import de.beheh.warlight2.stats.Ranking;
import de.beheh.warlight2.stats.SuperRegionRank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/*
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Quickstep extends Bot {

	public Quickstep(GameTracker gameTracker) {
		super(gameTracker);
	}

	Ranking<SuperRegion> superRegionRanking = new Ranking<>();

	@Override
	public void onMapComplete() {
		Map map = gameTracker.getMap();
		System.err.println(map.toString());
		// calculate base value for all superregions
		for (SuperRegion superRegion : map.getSuperRegions()) {
			superRegionRanking.addObject(superRegion, new SuperRegionRank(superRegion.getBonus(), superRegion.getRegions()));
		}
	}

	List<Region> startingRegions = null;

	@Override
	public Region pickStartingRegion(Region[] regions) {
		Region pickRegion = null;
		Map map = gameTracker.getMap();
		// save all starting regions for later
		if (startingRegions == null) {
			startingRegions = new ArrayList<>(Arrays.asList(regions));
		}
		// pick highest ranked region
		for (SuperRegion superRegion : superRegionRanking.getRankList()) {
			for (Region region : regions) {
				if (region.getSuperRegion().equals(region)) {
					pickRegion = region;
				}
				break;
			}
			if (pickRegion != null) {
				break;
			}
		}
		// pick a random region
		if (pickRegion == null) {
			Random rand = new Random();
			pickRegion = regions[rand.nextInt(regions.length)];
		}
		return pickRegion;
	}

	@Override
	public void onPickingComplete() {
		// regions we didn't pick went our opponent
		Iterator<Region> iterator = startingRegions.iterator();
		while (iterator.hasNext()) {
			Region startingRegion = iterator.next();
			if (startingRegion.getOwner() == null || !startingRegion.getOwner().equals(gameTracker.getPlayer())) {
				startingRegion.setOwner(gameTracker.getOpponent());
				startingRegion.setLastUpdate(0); // updated at "round 0"
			}
		}
	}

	@Override
	public Command placeArmies(int armyCount) {
		Map map = gameTracker.getMap();
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameTracker);

		// pick a random owned region
		List<Region> ownRegions = map.getRegionsByPlayer(gameTracker.getPlayer());
		for (int remaining = armyCount; remaining > 0; remaining--) {
			Random rand = new Random();
			command.placeArmy(ownRegions.get(rand.nextInt(ownRegions.size())), 1);
		}
		return command;
	}

	@Override
	public Command attackTransfer() {
		Map map = gameTracker.getMap();
		AttackTransferCommand command = new AttackTransferCommand(gameTracker);

		// find secure areas to move away from
		// find easy targets from all owned regions
		List<Region> ownRegions = map.getRegionsByPlayer(gameTracker.getPlayer());
		for (Region region : ownRegions) {
			int armycount = region.getArmyCount();
			if(armycount < 2) {
				continue;
			}
			for (Region neighbor : region.getNeighbors()) {
				if (gameTracker.getPlayer().equals(neighbor.getOwner())) {
					continue;
				}
				if (armycount * 4 / 3 > neighbor.getArmyCount() + 1) {
					command.attack(region, neighbor, armycount - 1);
					region.setArmyCount(1);
					break;
				}
			}
		}
		// move to better places
		for (Region region : ownRegions) {
			Region closest = region;
			for (Region neighbor : region.getNeighbors()) {
				if (!gameTracker.getPlayer().equals(neighbor.getOwner())) {
					continue;
				}
				if(region.getArmyCount() < 2) {
					continue;
				}
				if (neighbor.playerDistance(gameTracker.getOpponent()) < closest.playerDistance(gameTracker.getOpponent())) {
					closest = neighbor;
				}
			}
			if (closest != region) {
				command.transfer(region, closest, region.getArmyCount() - 1);
				region.setArmyCount(1);
			}
		}
		return command;
	}

//	List<RegionRank> prioritizedRegions = null;
	@Override
	public void onRoundComplete() {
		rankRegions();
	}

	protected void rankRegions() {
	//	prioritizedRegions = new ArrayList<RegionRank>();
		//Map map = gameTracker.getMap();
		// check for filling of SuperRegions
		/*List<SuperRegion> superRegions = map.getSuperRegions();
		 for (SuperRegion superRegion : superRegions) {
		 List<Region> missingPlayerRegions = superRegion.missingRegions(gameTracker.getPlayer());
		 System.err.println(missingPlayerRegions.size() + " missing for SuperRegion " + superRegion);
		 if (missingPlayerRegions.size() > 0) {
		 float score = superRegion.getBonus() * (1.0f / missingPlayerRegions.size());
		 for (Region region : missingPlayerRegions) {
		 prioritizedRegions.add(new RegionRank(score, region));
		 }
		 }
		 }
		 // seep into neighbors
		 Iterator<RegionRank> iterator = prioritizedRegions.iterator();
		 while (iterator.hasNext()) {
		 RegionRank regionRank = iterator.next();
		 }*/

		// higher score for smaller superregion
		//Collections.sort(prioritizedRegions, new Comparator<RegionRank>() {
		//	@Override
		//	public int compare(RegionRank rr1, RegionRank rr2) {
		//		return Float.compare(rr1.getRank(), rr2.getRank());
		//	}
		//});
	}
}
