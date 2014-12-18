package de.beheh.warlight2.impl;

import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import de.beheh.warlight2.game.map.Map;
import de.beheh.warlight2.game.map.Region;
import de.beheh.warlight2.game.map.SuperRegion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

	@Override
	public Region pickStartingRegion(Region[] regions) {
		Region pickRegion = null;
		Map map = gameTracker.getMap();
		// pick highest ranked region
		List<Region> regionList = Arrays.asList(regions);
		if (prioritizedRegions != null && prioritizedRegions.size() > 0) {
			for (Region region : (Region[]) prioritizedRegions.toArray()) {
				if (regionList.contains(region)) {
					pickRegion = region;
					break;
				}
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
	public List<Command> placeArmies() {
		List<Command> commands = new ArrayList<>();
		commands.add(new NoMovesCommand());
		return commands;
	}

	@Override
	public List<Command> attackTransfer() {
		List<Command> commands = new ArrayList<>();
		commands.add(new NoMovesCommand());
		return commands;
	}

	@Override
	public void onMapReceived() {
		Map map = gameTracker.getMap();
		System.err.println(map.toString());
		rankRegions();
	}

	List<RegionRank> prioritizedRegions = null;

	protected void rankRegions() {
		
	}

	protected class RegionRank {

		protected float rank;
		protected Region region;

		public RegionRank(float rank, Region region) {
			this.rank = rank;
			this.region = region;
		}

		public float getRank() {
			return rank;
		}

		public Region getRegion() {
			return region;
		}

		@Override
		public String toString() {
			return rank + ": " + region.toString();
		}
	}
}
