package de.beheh.warlight2.io;

import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import java.util.Arrays;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class MapHandler {
	
	protected GameTracker gameTracker;
	
	public MapHandler(GameTracker gameTracker) {
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
		for (String rawRegion : parameters) {
			String[] group = rawRegion.split(",");
			int region = Integer.valueOf(group[0]);
			if (group.length > 1) {
				int[] neighbors = new int[group.length - 1];
				for (int i = 0; i < group.length - 1; i++) {
					neighbors[i] = Integer.valueOf(group[i + 1]);
				}
				map.addNeighbors(region, neighbors);
			}
		}
	}
	
	public void setWastelands(int[] wastelands) {
		Map map = gameTracker.getMap();
		for (int wasteland : wastelands) {
			map.setRegionAsWasteland(wasteland);
		}
	}
	
	public void updateMap(String[] parameters) {
		Map map = gameTracker.getMap();
		for (int i = 0; i < parameters.length; i += 3) {
			int region = Integer.valueOf(parameters[i]);
			Player owner = gameTracker.getPlayer(parameters[i + 1]);
			int armies = Integer.valueOf(parameters[i + 2]);
			map.update(region, owner, armies, gameTracker.getRound());
		}
	}
	
}
