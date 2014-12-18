package de.beheh.warlight2.io;

import de.beheh.warlight2.map.Map;
import java.util.Arrays;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class MapHandler {

	protected Map map;

	public MapHandler(Map map) {
		this.map = map;
	}

	public void setSuperRegions(int[] parameters) {
		for (int i = 0; i < parameters.length; i += 2) {
			map.addSuperRegion(parameters[i], parameters[i + 1]);
		}
	}

	public void setRegions(int[] parameters) {
		for (int i = 0; i < parameters.length; i += 2) {
			map.addRegion(parameters[i], parameters[i + 1]);
		}
	}

	public void setNeighbors(String[] parameters) {
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
		for (int wasteland : wastelands) {
			map.setRegionAsWasteland(wasteland);
		}
	}

}
