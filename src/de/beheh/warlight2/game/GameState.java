package de.beheh.warlight2.game;

import de.beheh.warlight2.game.map.Map;
import java.io.Serializable;

/**
 *
 * @author Benedict Etzel
 */
public class GameState implements Serializable, Cloneable {

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

	public void setPlayer(Player player) {
		this.self = player;
	}

	public Player getPlayer() {
		return self;
	}

	protected Player opponent = null;

	public void setOpponentName(String opponentName) {
		opponent = new Player(opponentName);
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
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

	protected int startingPickAmount;

	public void setStartingPickAmount(int startingPickAmount) {
		this.startingPickAmount = startingPickAmount;
	}

	public int getStartingPickAmount() {
		return startingPickAmount;
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
		} else if (playername.equals("neutral")) {
			return null;
		}
		return null;
	}

	protected int round = 0;

	public void nextRound() {
		round++;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getRound() {
		return round;
	}
}
