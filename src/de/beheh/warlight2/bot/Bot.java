package de.beheh.warlight2.bot;

import de.beheh.warlight2.bot.command.AttackTransferCommand;
import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.bot.command.PlaceArmiesCommand;
import de.beheh.warlight2.game.map.Region;

/**
 *
 * @author Benedict Etzel
 */
public abstract class Bot {

	public final GameState gameState;

	public Bot(GameState gameState) {
		this.gameState = gameState;
	}

	public final Player getOwner() {
		return gameState.getPlayer();
	}

	public abstract Region pickStartingRegion(Region[] regions);

	public abstract PlaceArmiesCommand placeArmies(int armyCount);

	public abstract AttackTransferCommand attackTransfer();

	/* Time */
	
	long deadline = 0;

	public void setTime(long time) {
		deadline = System.currentTimeMillis() + time;
	}

	public long getRemainingTime() {
		long current = System.currentTimeMillis();
		if (current >= deadline) {
			return 0;
		}
		return deadline - current;
	}

	public boolean hasRemainingTime() {
		return getRemainingTime() > 0;
	}

	/* Callbacks */
	public void onMapComplete() {
	}

	public void onOpponentMoves() {
	}

	public void onPickingComplete() {
	}

	public void onRoundStart() {
	}

	public void onRoundComplete() {
	}
}
