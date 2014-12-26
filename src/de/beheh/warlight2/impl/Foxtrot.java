package de.beheh.warlight2.impl;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.AttackTransferCommand;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.Route;
import de.beheh.warlight2.game.map.SuperRegion;
import de.beheh.warlight2.stats.Scorer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Benedict Etzel
 */
public class Foxtrot extends Bot {

	public Foxtrot(GameState gameTracker) {
		super(gameTracker);
	}

	protected class RegionDesirabilityScorer extends Scorer<Region> {

		@Override
		protected int group(Region region) {
			// highest priority: complete SuperRegions
			SuperRegion superRegion = region.getSuperRegion();
			if (superRegion.isOwnedBy(gameState.getPlayer())) {
				return 1;
			}
			return 2;
		}

		@Override
		public double score(Region region) {
			SuperRegion superRegion = region.getSuperRegion();
			double staticScore = superRegion.getBonus() / ((superRegion.getRegionCount() / 2) + superRegion.getWastelandCount() * 3);
			double takeoverScore = 0;
			if (!region.isOwnedBy(gameState.getPlayer())) {
				takeoverScore += superRegion.getBonus() * (superRegion.getRegionCount() - superRegion.getMissingRegions(gameState.getPlayer()).size()) / (superRegion.getWastelandCount() + 1);
				takeoverScore += superRegion.getBonus() * (superRegion.getRegionCount() - superRegion.getMissingRegions(gameState.getOpponent()).size()) / (superRegion.getWastelandCount() + 1) * 2 / 3;
			}
			return staticScore * 5 + takeoverScore * 1;
		}
	}

	@Override
	public Region pickStartingRegion(Region[] regions) {
		// choose region with higher static/dynamic rank
		List<Region> startRegionList = Arrays.asList(regions);
		Collections.sort(startRegionList, new RegionDesirabilityScorer());
		return regions[0];
	}

	@Override
	public void onPickingComplete() {
		updateBorder();
	}

	protected List<Region> border = new ArrayList<>();

	public void updateBorder() {
		Map map = gameState.getMap();

		border = new ArrayList<>(map.getRegionCount());
		for (Region region : map.getRegionsByPlayer(gameState.getPlayer())) {
			for (Region neighbor : region.getNeighbors()) {
				if (border.contains(neighbor)) {
					continue;
				}
				if (neighbor.isOwnedBy(gameState.getPlayer())) {
					continue;
				}
				border.add(neighbor);
			}
		}
		Collections.sort(border, new RegionDesirabilityScorer());
	}

	protected class RegionThreatScorer extends Scorer<Region> {

		@Override
		protected double score(Region region) {
			if (region.isNeutral()) {
				return 0;
			}
			return region.getArmyCount() - region.getPotentialAttackers();
		}

	}

	@Override
	public PlaceArmiesCommand placeArmies(int remainingArmies) {
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameState);

		updateBorder();

		Map map = gameState.getMap();

		// the border really should not be empty
		if (border.isEmpty()) {
			System.err.println("border is empty");
			return command;
		}

		Collections.sort(border, new RegionThreatScorer());

		// @todo spread armies out for multiple fronts
		for (Region borderNeighbor : border) {
			List<Region> ourRegions = borderNeighbor.getNeighborsByPlayer(gameState.getPlayer());
			Collections.sort(ourRegions, new RegionDesirabilityScorer());
			Region targetRegion = ourRegions.get(0);
			if (remainingArmies > 0) {
				targetRegion.increaseArmy(remainingArmies);
				command.placeArmy(targetRegion, remainingArmies);
				return command;
			}
		}

		return command;
	}

	protected class FriendlyRegionGrouper extends Scorer<Region> {

		protected final Player player;

		public FriendlyRegionGrouper(Player player) {
			this.player = player;
		}

		@Override
		protected int group(Region region) {
			if (region.isOwnedBy(player)) {
				return 1;
			}
			return 2;
		}

	}

	protected boolean shouldAttack(int attackers, int defenders) {
		float factor = 0.7f;
		factor += Math.min(gameState.getRound() - lastAttackTransferRound, 5) * (0.3f / 5);
		return Math.round(attackers * factor) > defenders;
	}

	protected int requiredDefenders(int potentialAttackers) {
		float factor = Math.min(gameState.getRound() - lastAttackTransferRound, 5) * 0.1f;
		return Math.round((1.5f - factor) * potentialAttackers);
	}

	protected int lastAttackTransferRound = 0;

	protected void updateLastAttackTransferRound() {
		lastAttackTransferRound = gameState.getRound();
	}

	protected int predictAttackers(Region region) {
		List<Region> neighbors = region.getNeighbors();
		int attackers = 0;
		Player hostilePlayer = null;
		for (Region neighbor : neighbors) {
			if (neighbor.isHostile(region.getOwner())) {
				attackers += neighbor.getArmyCount() - 1;
			}
		}
		if (hostilePlayer != null) {
			attackers += predictPlayerBonus(gameState.getPlayer());
		}
		return attackers;
	}

	protected int predictPlayerBonus(Player player) {
		Map map = gameState.getMap();
		int bonus = 5;
		for (SuperRegion superRegion : map.getSuperRegions()) {
			if (predictSuperRegionOwned(superRegion, player)) {
				bonus += superRegion.getBonus();
			}
		}
		return bonus;
	}

	protected boolean predictSuperRegionOwned(SuperRegion superRegion, Player player) {
		if (superRegion.isOwnedBy(player)) {
			return true;
		}
		boolean owned = true;
		for (Region region : superRegion.getRegions()) {
			// assume the enemy has taken neutral regions it if we haven't seen them for a while
			int lastUpdate = 0;
			if (region.getLastUpdate() != null) {
				lastUpdate = region.getLastUpdate();
			}
			if (region.isNeutral() && lastUpdate < gameState.getRound() - superRegion.getRegionCount()) {
				continue;
			}
			if (!region.isOwnedBy(player)) {
				owned = false;
				break;
			}
		}
		return owned;
	}

	@Override
	public AttackTransferCommand attackTransfer() {
		AttackTransferCommand command = new AttackTransferCommand(gameState);

		updateBorder();

		Map map = gameState.getMap();

		for (Region borderRegion : border) {

			// border regions "pull" our border armies towards them
			List<Region> ourRegions = borderRegion.getNeighborsByPlayer(gameState.getPlayer());

			// prefer to take from lesser threatened regions
			Collections.sort(ourRegions, new RegionDesirabilityScorer());
			Collections.reverse(ourRegions);

			// worst case: reinforcements exactly where we try to attack
			int expectedDefenders = borderRegion.getArmyCount();
			if (borderRegion.isHostile(gameState.getPlayer())) {
				expectedDefenders += predictPlayerBonus(gameState.getOpponent());
			}

			// count our armies
			int totalFreeArmies = 0;
			HashMap<Region, Integer> reservedArmies = new HashMap<>(ourRegions.size());
			for (Region region : ourRegions) {
				int potentialAttackers = predictAttackers(region);
				// if we attack into a hostile region, the destroyed armies won't attack our region next turn
				if (borderRegion.isHostile(gameState.getPlayer())) {
					potentialAttackers -= expectedDefenders;
				}
				int requiredDefenders = requiredDefenders(Math.max(potentialAttackers, 0));
				int freeArmies = Math.max(region.getArmyCount() - requiredDefenders - 1, 0);
				totalFreeArmies += freeArmies;
				reservedArmies.put(region, freeArmies);
			}

			// go for it only if we have a slight advantage
			if (shouldAttack(totalFreeArmies, expectedDefenders)) {

				List<Entry<Region, Integer>> reservationList = new ArrayList<>(reservedArmies.entrySet());

				// start with biggest armies first
				Collections.sort(reservationList, new Comparator<Entry<Region, Integer>>() {

					@Override
					public int compare(Entry<Region, Integer> entry1, Entry<Region, Integer> entry2) {
						return Integer.compare(entry2.getValue(), entry1.getValue());
					}
				});

				for (Entry<Region, Integer> entry : reservationList) {
					Region region = entry.getKey();
					int reserved = entry.getValue();

					int attackers = Math.min(reserved, region.getArmyCount() - 1);
					if (attackers > 0) {
						command.attack(region, borderRegion, attackers);
						region.decreaseArmy(attackers);
						updateLastAttackTransferRound();
					}
				}
			}
		}

		for (Region region : map.getRegionsByPlayer(gameState.getPlayer())) {
			int freeArmies = region.getArmyCount() - 1; // can use all but one army

			// move remaining armies towards border
			Collections.sort(border, new RegionDesirabilityScorer());
			freeArmies -= requiredDefenders(region.getPotentialAttackers());

			if (freeArmies < 1) {
				continue;
			}

			Route bestRoute = null;
			boolean alreadyAtBorder = false;
			for (Region borderRegion : border) {
				if (region.isNeighbor(borderRegion)) {
					alreadyAtBorder = true;
					break;
				}
				Route route = region.routeTo(borderRegion, gameState.getPlayer());
				if (route == null) {
					continue;
				}
				if (bestRoute == null || route.length() < bestRoute.length() - 1) {
					bestRoute = route;
				}
			}

			if (alreadyAtBorder) {

				// check if neighboring regions need support
				List<Region> neighbors = region.getNeighborsByPlayer(gameState.getPlayer());
				if (neighbors.isEmpty()) {
					continue;
				}

				Collections.sort(neighbors, new Scorer<Region>() {

					@Override
					protected double score(Region region) {
						return region.getPotentialAttackers();
					}

				});
				for (Region neighbor : neighbors) {
					if (freeArmies < 1) {
						break;
					}

					int requiredDefenders = requiredDefenders(predictAttackers(neighbor)) - neighbor.getScheduledArmyCount();
					int transferArmies = Math.min(Math.max(requiredDefenders, 0), region.getArmyCount() - 1);
					if (transferArmies < 1) {
						continue;
					}

					command.transfer(region, neighbor, transferArmies);
					region.decreaseArmy(transferArmies);
					neighbor.scheduleIncreaseArmy(transferArmies);
					updateLastAttackTransferRound();
				}
				continue;
			}
			if (bestRoute != null) {
				// move along route towards border
				Region target = bestRoute.getFirst();
				command.transfer(region, target, freeArmies);
				region.decreaseArmy(freeArmies);
				target.scheduleIncreaseArmy(freeArmies);
				updateLastAttackTransferRound();
			} else {
				System.err.println("error: cannot reach any border from " + region);
			}
		}

		return command;
	}

	@Override
	public void onRoundComplete() {
		Map map = gameState.getMap();
		for (Region region : map.getRegionsByPlayer(gameState.getPlayer())) {
			region.commitSchedule();
		}
	}
}
