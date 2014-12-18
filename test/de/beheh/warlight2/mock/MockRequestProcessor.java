package de.beheh.warlight2.mock;

import de.beheh.warlight2.RequestProcessor;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class MockRequestProcessor extends RequestProcessor {

	public MockRequestProcessor() {
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
	public void onOpponentMoves() {
	}

}
