package de.beheh.warlight2.impl;

import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.AttackTransferCommand;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.game.map.Border;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Benedict Etzel
 */
public class Jive extends Bot {

	public Jive(GameState gameTracker) {
		super(gameTracker);
	}

	public int getArmyBonus() {
		return getArmyBonus(gameState.getPlayer());
	}

	public int getArmyBonus(Player player) {
		int armyBonus = 5;
		for (SuperRegion superRegion : gameState.getMap().getSuperRegions()) {
			if (superRegion.isOwnedBy(player)) {
				armyBonus += superRegion.getBonus();
			}
		}
		return armyBonus;
	}

	/**
	 * Returns the chance of a successful region takeover.
	 *
	 * @param attackers
	 * @param defenders
	 * @return
	 */
	public boolean simulateTakeover(int attackers, int defenders) {
		defenders = (int) Math.round(simulateDefence(attackers, defenders));
		attackers = (int) Math.round(simulateAttack(attackers, defenders));
		if (defenders > 0 || attackers < 1) {
			return false;
		}
		return true;
	}

	public double simulateAttack(int attackers, int defenders) {
		// on average, 70% of defenders destroy an attacker
		return Math.max(attackers - simulateDamage(defenders, 0.7d), 0);
	}

	public double simulateDefence(int attackers, int defenders) {
		// on average, 60% of attackers destroy a defender
		return Math.max(defenders - simulateDamage(attackers, 0.6d), 0);
	}

	public double simulateDamage(int dealers, double chance) {
		double something = 0.5;
		double baseDamage = (chance * dealers * (1 - 0.16d));
		double bestCase = (dealers * 0.16d);
		double worstCase = 0;
		return baseDamage + (bestCase - worstCase) / 2;
	}

	@Override
	public Region pickStartingRegion(Region[] regions) {
		// first, pick an easy bonus region with a high distance to other possibilites

		return null;
	}

	@Override
	public PlaceArmiesCommand placeArmies(int armyCount) {
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameState);

		return command;
	}

	@Override
	public AttackTransferCommand attackTransfer() {
		AttackTransferCommand command = new AttackTransferCommand(gameState);

		Map map = gameState.getMap();

		// if worst case is favorable -> attack
		for (Region region : map.getRegionsByPlayer(gameState.getPlayer())) {

		}
		return command;
	}

	// determine maximum amount of troops needed per border segment
	// prioritize
	// also: ranking with percentages
	// if we have Scores 40, 40, 20, split our units
	// find highest target along border
	// predict threats
	protected List<Border> borderGroups = null;

	public void updateBorders() {
		Map map = gameState.getMap();

		// determine all border regions
		Queue<Region> allBorders = new LinkedList<>();
		for (Region region : map.getRegionsByPlayer(gameState.getPlayer())) {
			for (Region neighbor : region.getNeighbors()) {
				if (!neighbor.isOwnedBy(gameState.getPlayer())) {
					allBorders.add(region);
					break;
				}
			}
		}

		// merge border groups
		borderGroups = new LinkedList<>();
		while (!allBorders.isEmpty()) {

			// add next element as new border root
			Region borderRoot = allBorders.poll();
			Border border = new Border();
			border.add(borderRoot);
			Queue<Region> searchQueue = new LinkedList<>();
			searchQueue.add(borderRoot);

			// find all connected border regions with breadth-first search
			while (!searchQueue.isEmpty()) {
				Region currentRegion = searchQueue.poll();
				for (Region neighbor : currentRegion.getNeighbors()) {
					if (!allBorders.contains(neighbor)) {
						continue;
					}
					// handle this border region as part of our current region
					border.add(neighbor);
					allBorders.remove(neighbor);
					// queue for next depth
					searchQueue.add(neighbor);
				}
			}

			borderGroups.add(border);
		}

		// logging
		StringBuilder builder = new StringBuilder();
		for (Border border : borderGroups) {
			if (builder.length() > 0) {
				builder.append(", ");
			}
			builder.append(border);
			builder.append(" (encloses " + border.getEnclosedRegions().size() + " regions)");
		}
		System.err.println(getClass().getSimpleName() + ": detected " + borderGroups.size() + " border group(s): " + builder.toString());
	}

	@Override
	public void onPickingComplete() {
		updateBorders();
	}

	@Override
	public void onRoundComplete() {
		updateBorders();
	}
}
