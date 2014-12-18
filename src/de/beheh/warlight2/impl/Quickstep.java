package de.beheh.warlight2.impl;

import de.beheh.warlight2.game.GameTracker;
import de.beheh.warlight2.bot.Bot;
import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.command.NoMovesCommand;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
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
	public Command placeArmies(int armyCount) {
		Map map = gameTracker.getMap();
		PlaceArmiesCommand command = new PlaceArmiesCommand(gameTracker);
		// pick a random owned region
		List<Region> ownRegions = map.getRegionsByPlayer(gameTracker.getPlayer());
		for (int remaining = armyCount; remaining > 0; remaining--) {
			Random rand = new Random();
			command.placeArmy(ownRegions.get(rand.nextInt(ownRegions.size())), 1);
		}
		return command;
	}
	
	@Override
	public Command attackTransfer() {
		Map map = gameTracker.getMap();
		return null;
	}
	
	@Override
	public void onMapReceived() {
		Map map = gameTracker.getMap();
		System.err.println(map.toString());
		rankRegions();
	}
	
	List<RegionRank> prioritizedRegions = null;
	
	protected void rankRegions() {
		prioritizedRegions = new ArrayList<RegionRank>();
		Map map = gameTracker.getMap();
		// check for filling of SuperRegions
		List<SuperRegion> superRegions = map.getSuperRegions();
		for (SuperRegion superRegion : superRegions) {
			List<Region> missingPlayerRegions = superRegion.missingRegions(gameTracker.getPlayer());
			if (missingPlayerRegions.size() > 0) {
				float score = superRegion.getBonus() * (1.0f / missingPlayerRegions.size());
				for (Region region : (Region[]) missingPlayerRegions.toArray()) {
					prioritizedRegions.add(new RegionRank(score, region));
				}
			}
		}
		// seep into neighbors
		Iterator<RegionRank> iterator = prioritizedRegions.iterator();
		while (iterator.hasNext()) {
			RegionRank regionRank = iterator.next();
		}
		
		Collections.sort(prioritizedRegions, new Comparator<RegionRank>() {
			@Override
			public int compare(RegionRank rr1, RegionRank rr2) {
				return Float.compare(rr1.getRank(), rr2.getRank());
			}
		});
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
