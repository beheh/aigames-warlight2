package de.beheh.warlight2.game;

import de.beheh.warlight2.game.map.Map;

/**
 *
 * @author benedict
 */
public class GameTracker {

	private long timebank;

	public void setTimebank(long timebank) {
		this.timebank = timebank;
	}

	public long getTimebank() {
		return timebank;
	}

	private long timePerMove;

	public void setTimePerMove(long timePerMove) {
		this.timePerMove = timePerMove;
	}

	public long getTimePerMove() {
		return timePerMove;
	}

	private int maxRounds;

	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	public int getMaxRounds() {
		return maxRounds;
	}

	private String botName;

	protected Player self = null;

	public void setBotName(String botName) {
		self = new Player(botName);
	}

	public Player getPlayer() {
		return self;
	}

	protected Player opponent = null;

	public void setOpponentName(String opponentName) {
		opponent = new Player(opponentName);
	}

	public Player getOpponent() {
		return opponent;
	}

	protected int startingArmies;

	public void setStartingArmies(int startingArmies) {
		this.startingArmies = startingArmies;
	}

	public int getStartingArmies() {
		return startingArmies;
	}

	protected Map map = new Map();

	public void setMap(Map map) {
		this.map = map;
	}

	public Map getMap() {
		return map;
	}

	public Player getPlayer(String playername) {
		if (self.getName().equals(playername)) {
			return self;
		} else if (opponent.getName().equals(playername)) {
			return opponent;
		}
		return null;
	}
}
