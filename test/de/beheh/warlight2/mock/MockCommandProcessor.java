package de.beheh.warlight2.mock;

import de.beheh.warlight2.io.CommandProcessor;

/**
 *
 * @author Benedict Etzel
 */
public class MockCommandProcessor extends CommandProcessor {

	public MockCommandProcessor() {
		super(null, null);
	}

	@Override
	public String pickStartingRegion(Long time, int[] regionIds) {
		return "nop";
	}

	@Override
	public String placeArmies(Long time) {
		return "nop";
	}

	@Override
	public String attackTransfer(Long time) {
		return "nop";
	}

	@Override
	public void opponentMoves(String[] parameters) {
	}

	@Override
	public void mapComplete() {
	}

	@Override
	public void pickingComplete() {
	}

}
