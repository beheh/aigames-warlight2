package de.beheh.warlight2.bot.command;

import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.map.Region;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author Benedict Etzel
 */
public class PlaceArmiesCommand extends Command {

	public PlaceArmiesCommand(GameState gameTracker) {
		super(gameTracker);
	}

	HashMap<Region, Integer> deployments = new HashMap<>();

	public void placeArmy(Region region, int armyCount) {
		if (armyCount == 0) {
			throw new IllegalArgumentException("invalid armyCount "+armyCount);
		}
		if (deployments.containsKey(region)) {
			// add to existing deployment
			deployments.put(region, armyCount + deployments.get(region));
		} else {
			// new deployment
			deployments.put(region, armyCount);
		}
	}

	@Override
	public String toString() {
		if(deployments.isEmpty()) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Entry<Region, Integer>> iterator = deployments.entrySet().iterator();
		boolean first = true;
		while (iterator.hasNext()) {
			if (!first) {
				stringBuilder.append(", ");
			} else {
				first = false;
			}
			stringBuilder.append(gameTracker.getPlayer().getName()).append(" place_armies ");
			Entry<Region, Integer> entry = iterator.next();
			stringBuilder.append(entry.getKey().getId()).append(" ").append(entry.getValue());
		}
		return stringBuilder.toString();
	}

}
