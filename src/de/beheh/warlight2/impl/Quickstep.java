package de.beheh.warlight2.impl;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.AttackTransferCommand;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
 * @author Benedict Etzel
 */
public class Quickstep extends Bot {

	public Quickstep(GameState gameTracker) {
		super(gameTracker);
	}

	protected void deadline(String message) {
		System.err.println("Deadline constraint: terminating after ");
	}

	Ranking<SuperRegion> superRegionRanking = new Ranking<>();

	@Override
	public void onMapComplete() {
		Map map = gameState.getMap();
		System.err.println(map.toString());
		// calculate base value for all superregions
		for (SuperRegion superRegion : map.getSuperRegions()) {
			superRegionRanking.addObject(superRegion, new SuperRegionRank(superRegion));
		}
	}

	@Override
	public Region pickStartingRegion(Region[] regions) {
		Region pickRegion = null;
		Map map = gameState.getMap();
		// pick highest ranked region
		for (SuperRegion superRegion : superRegionRanking.getRankList()) {
			for (Region region : regions) {
				if (region.getSuperRegion().equals(superRegion)) {
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

	protected int getRequiredArmies(int armyCount) {
		return Math.round(armyCount * 1.65f);
	}

	@Override
	public PlaceArmiesCommand placeArmies(int armyCount) {
		Map map = gameState.getMap();
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameState);

		// pick a random owned region
		Ranking<Region> regionRanking = new Ranking<>();
		List<Region> ownRegions = map.getRegionsByPlayer(gameState.getPlayer());
		for (Region region : ownRegions) {
			regionRanking.addObject(region, new ReinforcementRank(region));
		}

		int remaining = armyCount;

		List<Region> rankList = regionRanking.getRankList();
		if (rankList.size() > 0) {
			Region target = rankList.get(0);
			int placing = remaining / 3;
			command.placeArmy(target, placing);
			target.scheduleIncreaseArmy(placing);
			remaining -= placing;
		}

		// pick a random region
		Random rand = new Random();
		for (; remaining > 0; remaining--) {
			Region target = ownRegions.get(rand.nextInt(ownRegions.size()));
			command.placeArmy(target, 1);
			target.scheduleIncreaseArmy(1);
		}

		return command;
	}

	@Override
	public AttackTransferCommand attackTransfer() {
		Map map = gameState.getMap();
		AttackTransferCommand command = new AttackTransferCommand(gameState);

		try {
			// find secure areas to move away from

			List<Region> ownRegions = map.getRegionsByPlayer(gameState.getPlayer());

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

				// rank neighbors
				Ranking<Region> neighborRanking = new Ranking<>();
				for (Region neighbor : region.getNeighbors()) {
					neighborRanking.addObject(neighbor, new RegionRank(region));
				}
				List<Region> neighborRankList = neighborRanking.getRankList();

				// can we attack something with the rest?
				for (Region neighbor : neighborRankList) {
					if (gameState.getOpponent().equals(neighbor.getOwner())) {
						int requiredArmies = getRequiredArmies(neighbor.getArmyCount());
						// force attack if we really have enough
						if (neighbor.getPotentialAttackers() > 3 * freeArmies) {
							requiredArmies = freeArmies;
						}
						if (freeArmies >= requiredArmies && requiredArmies > 0) {
							int armiesToSend = requiredArmies + (freeArmies - requiredArmies / 2) + 1;
							command.attack(region, neighbor, armiesToSend);
							region.decreaseArmy(armiesToSend);
							neighbor.scheduleIncreaseArmy(armiesToSend);
							freeArmies -= armiesToSend;
						}
					}
				}

				// we're done
				if (freeArmies < 2) {
					continue;
				}

				// anything neutral to capture?
				for (Region neighbor : neighborRankList) {
					if (neighbor.getOwner() == null) {
						int requiredArmies = getRequiredArmies(neighbor.getArmyCount());
						if (freeArmies >= requiredArmies) {
							command.attack(region, neighbor, requiredArmies);
							region.decreaseArmy(requiredArmies);
							neighbor.scheduleIncreaseArmy(requiredArmies);
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
				int targetEnemyDistance = region.playerDistance(gameState.getOpponent(), 4);
				for (Region neighbor : region.getNeighbors()) {
					if (region.getOwner() == neighbor.getOwner()) {
						// awesome! lets go there
						int neighborDistance = neighbor.playerDistance(gameState.getOpponent(), 4);
						if (neighbor.getPotentialAttackers() > target.getPotentialAttackers() || (neighborDistance != -1 && (targetEnemyDistance == -1 || neighborDistance < targetEnemyDistance))) {
							target = neighbor;
							targetEnemyDistance = neighborDistance;
						}
					}
				}
				// move all but one
				if (target != region) {
					command.transfer(region, target, freeArmies);
					region.decreaseArmy(freeArmies);
					target.scheduleIncreaseArmy(freeArmies);
					freeArmies = 0;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
			return command;
		}

		for (Region region : map.getRegions()) {
			region.commitSchedule();
		}

		return command;
	}

	protected abstract class Rank {

		public abstract double getScore();

		public int compare(Rank rank) {
			return Double.compare(getScore(), rank.getScore());
		}
	}

	protected class Ranking<E> {

		protected List<E> rankList = null;
		protected HashMap<E, Rank> map = new HashMap<>();

		public void addObject(E key, Rank rank) {
			map.put(key, rank);
			rerank();
		}

		protected void rerank() {
			rankList = new LinkedList(map.keySet());
			Collections.sort(rankList, new Comparator<E>() {

				@Override
				public int compare(E object1, E object2) {
					// higher rank score first
					return map.get(object2).compare(map.get(object1));
				}
			});
		}

		public List<E> getRankList() {
			return rankList;
		}

	}

	protected class RegionRank extends Rank {

		protected final Region region;

		public RegionRank(Region region) {
			this.region = region;
		}

		@Override
		public double getScore() {
			return region.getSuperRegion().getBonus();
		}

	}

	protected class ReinforcementRank extends Rank {

		private final Region region;

		public ReinforcementRank(Region region) {
			this.region = region;
		}

		@Override
		public double getScore() {
			return Math.max(region.getPotentialAttackers() - region.getArmyCount(), 0);
		}

	}

	protected class SuperRegionRank extends Rank {

		protected SuperRegion superRegion;

		public SuperRegionRank(SuperRegion superRegion) {
			this.superRegion = superRegion;
		}

		@Override
		public double getScore() {
			return (2 * superRegion.getBonus()) - Math.pow(superRegion.getRegionCount(), 2);
		}

	}

}
