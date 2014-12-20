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
import de.beheh.warlight2.stats.ReinforcementRank;
import de.beheh.warlight2.stats.SuperRegionRank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

	protected void deadline(String message) {
		System.err.println("Deadline constraint: terminating after ");
	}

	Ranking<SuperRegion> superRegionRanking = new Ranking<>();

	@Override
	public void onMapComplete() {
		Map map = gameTracker.getMap();
		System.err.println(map.toString());
		// calculate base value for all superregions
		for (SuperRegion superRegion : map.getSuperRegions()) {
			superRegionRanking.addObject(superRegion, new SuperRegionRank(superRegion));
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

	protected int getRequiredArmies(int armyCount) {
		return Math.round(armyCount * 1.6f);
	}

	@Override
	public PlaceArmiesCommand placeArmies(int armyCount) {
		Map map = gameTracker.getMap();
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameTracker);

		// pick a random owned region
		Ranking<Region> regionRanking = new Ranking<>();
		List<Region> ownRegions = map.getRegionsByPlayer(gameTracker.getPlayer());
		for (Region region : ownRegions) {
			regionRanking.addObject(region, new ReinforcementRank(region));
		}

		List<Region> rankList = regionRanking.getRankList();
		if (rankList.size() > 0) {
			Region target = rankList.get(0);
			command.placeArmy(target, armyCount);
			target.increaseArmyCount(armyCount);
		} else {
			// pick a random region
			Random rand = new Random();
			for (int remaining = armyCount; armyCount > 0; armyCount--) {
				Region target = ownRegions.get(rand.nextInt(ownRegions.size()));
				command.placeArmy(target, armyCount);
				target.increaseArmyCount(armyCount);
			}
		}
		return command;
	}

	@Override
	public AttackTransferCommand attackTransfer() {
		Map map = gameTracker.getMap();
		AttackTransferCommand command = new AttackTransferCommand(gameTracker);

		try {
			// find secure areas to move away from

			List<Region> ownRegions = map.getRegionsByPlayer(gameTracker.getPlayer());

			for (Region region : ownRegions) {
				// can attack/transfer all but the last one
				int freeArmies = region.getArmyCount() - 1;

				//how many armies can attack us?
				int potentialAttackers = region.getPotentialAttackers();
				// how many armies do we need to defend this region?
				potentialAttackers = potentialAttackers * 2 / 3; // let's be optimistic
				// we want do defend with some, so don't move them anywhere
				if (potentialAttackers <= freeArmies) {
					freeArmies -= potentialAttackers;
				}

				// can we attack something with the rest?
				for (Region neighbor : region.getNeighbors()) {
					if (gameTracker.getOpponent().equals(neighbor.getOwner())) {
						int requiredArmies = getRequiredArmies(neighbor.getArmyCount());
						if (freeArmies >= requiredArmies) {
							command.attack(region, neighbor, requiredArmies);
							region.decreaseArmyCount(requiredArmies);
							neighbor.increaseArmyCount(requiredArmies);
							freeArmies -= requiredArmies;
						}
					}
				}

				// we're done
				if (freeArmies < 2) {
					continue;
				}

				// anything neutral to capture?
				for (Region neighbor : region.getNeighbors()) {
					if (neighbor.getOwner() == null) {
						int requiredArmies = getRequiredArmies(neighbor.getArmyCount());
						if (freeArmies >= requiredArmies) {
							command.attack(region, neighbor, requiredArmies);
							region.decreaseArmyCount(requiredArmies);
							neighbor.increaseArmyCount(requiredArmies);
							freeArmies -= requiredArmies;
						}
					}
				}

				// can't move the last remaining army away
				if (freeArmies < 2) {
					continue;
				}

				// nothing worth attacking or defending for some armies, so move to best region
				Region target = region;
				int targetEnemyDistance = -1;
				for (Region neighbor : region.getNeighbors()) {
					if (region.getOwner() == neighbor.getOwner()) {
						// awesome! lets go there
						int neighborDistance = neighbor.playerDistance(gameTracker.getOpponent(), 4);
						if (neighbor.getPotentialAttackers() > target.getPotentialAttackers() || (neighborDistance != -1 && (targetEnemyDistance == -1 || neighborDistance < targetEnemyDistance))) {
							target = neighbor;
							targetEnemyDistance = neighborDistance;
						}
					}
				}
				// move all but one
				if (target != region) {
					command.transfer(region, target, freeArmies);
					region.decreaseArmyCount(freeArmies);
					target.increaseArmyCount(freeArmies);
					freeArmies = 0;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
			return command;
		}

		return command;
	}
}
