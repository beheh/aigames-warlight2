package de.beheh.warlight2.mock;

import de.beheh.warlight2.bot.GenericBot;
import de.beheh.warlight2.bot.command.Command;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class MockBot extends GenericBot {

	@Override
	public boolean shouldFailHard() {
		return true;
	}
	
	@Override
	public void onOpponentMoves() {
	}
	
	@Override
	public void onUpdateMap() {
	}
	
	@Override
	public int onPickStartingRegion(long time, int[] regions) {
		System.out.println("onPickStartingRegion: " + Arrays.toString(regions));
		return 0;
	}
	
	@Override
	public List<Command> onPlaceArmies(long time) {
		return null;
	}
	
	@Override
	public List<Command> onAttackTransfer(long time) {
		return null;
	}
}
