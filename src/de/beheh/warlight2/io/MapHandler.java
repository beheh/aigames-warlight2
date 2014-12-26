package de.beheh.warlight2.io;

import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.game.map.Map;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Benedict Etzel
 */
public class MapHandler {

	protected GameState gameTracker;

	public MapHandler(GameState gameTracker) {
		this.gameTracker = gameTracker;
	}

	public void setSuperRegions(int[] parameters) {
		Map map = gameTracker.getMap();
		for (int i = 0; i < parameters.length; i += 2) {
			map.addSuperRegion(parameters[i], parameters[i + 1]);
		}
	}

	public void setRegions(int[] parameters) {
		Map map = gameTracker.getMap();
		for (int i = 0; i < parameters.length; i += 2) {
			map.addRegion(parameters[i], parameters[i + 1]);
		}
	}

	public void setNeighbors(String[] parameters) {
		Map map = gameTracker.getMap();
		for (int i = 0; i < parameters.length; i += 2) {
			int region = Integer.valueOf(parameters[i]);
			String[] rawNeighbors = parameters[i + 1].split(",");
			int[] neighbors = new int[rawNeighbors.length];
			for (int j = 0; j < rawNeighbors.length; j++) {
				neighbors[j] = Integer.valueOf(rawNeighbors[j]);
			}
			map.addNeighbors(region, neighbors);
		}
	}

	public void setWastelands(int[] wastelands) {
		Map map = gameTracker.getMap();
		for (int wasteland : wastelands) {
			map.setRegionAsWasteland(wasteland);
		}
	}

	public void setOpponentStartingRegions(int[] regions) {
		Map map = gameTracker.getMap();
		for (int region : regions) {
			map.update(region, gameTracker.getOpponent(), gameTracker.getStartingArmies(), gameTracker.getRound());
		}
	}

	List<Integer> expectedRegionUpdates = new LinkedList<>();

	protected void expectRegionUpdate(int region) {
		expectedRegionUpdates.add(region);
	}

	public void updateMap(String[] parameters) {
		Map map = gameTracker.getMap();
		for (int i = 0; i < parameters.length; i += 3) {
			int region = Integer.valueOf(parameters[i]);
			Player owner = gameTracker.getPlayer(parameters[i + 1]);
			int armies = Integer.valueOf(parameters[i + 2]);
			expectedRegionUpdates.remove((Integer) region); // does not matter if it isn't expected
			map.update(region, owner, armies, gameTracker.getRound());
		}
		// probably lost control over any non-updated regions
		for (int region : expectedRegionUpdates) {
			map.verifyRegionLost(region, gameTracker.getOpponent(), gameTracker.getRound());
		}
	}

	public void opponentMoves(String[] parameters) {
		Map map = gameTracker.getMap();
		for (int i = 0; i < parameters.length; i += 4) {
			Player owner = gameTracker.getPlayer(parameters[i]);
			String command = parameters[i + 1];
			int from = Integer.valueOf(parameters[i + 2]);
			int to = Integer.valueOf(parameters[i + 3]);
			switch (command) {
				case "attack/transfer":
					int armyCount = Integer.valueOf(parameters[i + 4]);
					expectRegionUpdate(to);
					i++; // attack/transfer are longer by one parameter
					break;
				default:
					break;
			}
		}
	}

}
