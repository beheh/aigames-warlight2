package de.beheh.warlight2.impl;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.AttackTransferCommand;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.Route;
import de.beheh.warlight2.game.map.SuperRegion;
import de.beheh.warlight2.stats.Scorer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
		protected int group(Region region) {
			// highest priority: complete SuperRegions
			SuperRegion superRegion = region.getSuperRegion();
			if (superRegion.isOwnedBy(gameTracker.getPlayer())) {
				return 1;
			}
			// high priority: complete enemy SuperRegion
			if (superRegion.isOwnedBy(gameTracker.getOpponent())) {
				return 2;
			}
			// @todo: lower priority: any amount of enemies around?
			return 3;
		}

		@Override
		public double score(Region region) {
			SuperRegion superRegion = region.getSuperRegion();
			double staticScore = superRegion.getBonus() / (superRegion.getRegionCount() + superRegion.getWastelandCount() * 5);
			double takeoverScore = 0;
			if (!region.isOwnedBy(gameTracker.getPlayer())) {
				takeoverScore += superRegion.getBonus() * (superRegion.getRegionCount() - superRegion.getMissingRegions(gameTracker.getPlayer()).size()) / (superRegion.getWastelandCount() + 1);
			}
			return staticScore * 3 + takeoverScore * 1;
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
		Map map = gameTracker.getMap();

		border = new ArrayList<>(map.getRegionCount());
		for (Region region : map.getRegionsByPlayer(gameTracker.getPlayer())) {
			for (Region neighbor : region.getNeighbors()) {
				if (border.contains(neighbor)) {
					continue;
				}
				if (neighbor.isOwnedBy(gameTracker.getPlayer())) {
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
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameTracker);

		updateBorder();

		Map map = gameTracker.getMap();

		// the border really should not be empty
		if (border.isEmpty()) {
			System.err.println("border is empty");
			return command;
		}

		Collections.sort(border, new RegionThreatScorer());

		// @todo spread armies out for multiple fronts
		for (Region borderNeighbor : border) {
			List<Region> ourRegions = borderNeighbor.getNeighborsByPlayer(gameTracker.getPlayer());
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
		float factor = 0.8f;
		factor += Math.min(gameTracker.getRound() - lastAttackTransferRound, 5) * 0.1f;
		return Math.round(attackers * factor) > defenders;
	}

	protected int requiredDefenders(int potentialAttackers) {
		float factor = Math.min(gameTracker.getRound() - lastAttackTransferRound, 5) * 0.1f;
		return Math.round((1.5f - factor) * potentialAttackers);
	}

	protected int lastAttackTransferRound = 0;

	protected void updateLastAttackTransferRound() {
		lastAttackTransferRound = gameTracker.getRound();
	}

	@Override
	public AttackTransferCommand attackTransfer() {
		AttackTransferCommand command = new AttackTransferCommand(gameTracker);

		updateBorder();

		Map map = gameTracker.getMap();

		for (Region borderRegion : border) {

			// border regions "pull" our border armies towards them
			List<Region> ourRegions = borderRegion.getNeighborsByPlayer(gameTracker.getPlayer());

			// prefer to take from lesser threatened regions
			Collections.sort(ourRegions, new RegionDesirabilityScorer());
			Collections.reverse(ourRegions);

			// count our armies
			int totalFreeArmies = 0;
			HashMap<Region, Integer> reservedArmies = new HashMap<>(ourRegions.size());
			for (Region region : ourRegions) {
				int potentialAttackers = 0;
				if (borderRegion.isHostile(gameTracker.getPlayer())) {
					potentialAttackers = Math.max(region.getPotentialAttackers() - borderRegion.getArmyCount() - 1, 0);
				} else {
					potentialAttackers = Math.max(region.getPotentialAttackers(), 0);
				}
				int requiredDefenders = requiredDefenders(potentialAttackers);
				int freeArmies = Math.max(region.getArmyCount() - requiredDefenders - 1, 0);
				totalFreeArmies += freeArmies;
				reservedArmies.put(region, freeArmies);
			}

			// go for it only if we have a slight advantage
			if (shouldAttack(totalFreeArmies, borderRegion.getArmyCount())) {
				// prevent overkill
				int overkill = totalFreeArmies - borderRegion.getArmyCount();
				if (!borderRegion.isNeutral()) {
					overkill -= 2; //@todo enemy reinforcement heuristics
				}
				for (java.util.Map.Entry<Region, Integer> entry : reservedArmies.entrySet()) {
					Region region = entry.getKey();
					// lets take about 1.5x the amount the enemy has
					int attackers = Math.min(Math.round(entry.getValue() * 1.5f) - Math.round(overkill / reservedArmies.size()) + 1, region.getArmyCount() - 1);
					if (attackers > 0) {
						command.attack(region, borderRegion, attackers);
						region.decreaseArmy(attackers);
						borderRegion.decreaseArmy(attackers);
						updateLastAttackTransferRound();
					}
				}
			}
		}

		for (Region region : map.getRegionsByPlayer(gameTracker.getPlayer())) {
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
				Route route = region.routeTo(borderRegion, gameTracker.getPlayer());
				if (route == null) {
					continue;
				}
				if (bestRoute == null || route.length() < bestRoute.length()) {
					bestRoute = route;
				}
			}

			if (alreadyAtBorder) {

				// check if neighboring regions need support
				List<Region> neighbors = region.getNeighborsByPlayer(gameTracker.getPlayer());
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

					int requiredDefenders = requiredDefenders(neighbor.getPotentialAttackers()) - neighbor.getScheduledArmyCount();
					int transferArmies = Math.max(requiredDefenders, 0);
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
				target.increaseArmy(freeArmies);
				updateLastAttackTransferRound();
			} else {
				System.err.println("error: cannot reach any border from " + region);
			}
		}

		return command;
	}
}
