package de.beheh.warlight2.impl;

import de.beheh.warlight2.bot.GenericBot;
import de.beheh.warlight2.bot.command.Command;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Quickstep extends GenericBot {
	
	@Override
	public void onOpponentMoves() {
		System.err.println("onOpponentMoves");
	}
	
	@Override
	public void onUpdateMap() {
		System.err.println("onUpdateMap");
	}
	
	@Override
	public int onPickStartingRegion(long time, int[] regions) {
		System.err.println("onPickStartingRegion: " + Arrays.toString(regions));
		return regions[0];
	}
	
	@Override
	public List<Command> onPlaceArmies(long time) {
		System.err.println("onPlaceArmies");
		return null;
	}
	
	@Override
	public List<Command> onAttackTransfer(long time) {
		System.err.println("onAttackTransfer");
		return null;
	}
}
