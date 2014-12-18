package de.beheh.warlight2.bot;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public abstract class GenericBot implements Bot {

	@Override
	public boolean shouldFailHard() {
		return false;
	}

	/* Settings */
	private long timebank;

	@Override
	public void setTimebank(long timebank) {
		this.timebank = timebank;
	}

	public long getTimebank() {
		return timebank;
	}

	private long timePerMove;

	@Override
	public void setTimePerMove(long timePerMove) {
		this.timePerMove = timePerMove;
	}

	public long getTimePerMove() {
		return timePerMove;
	}

	private int maxRounds;

	@Override
	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	public int getMaxRounds() {
		return maxRounds;
	}

	private String botName;

	@Override
	public void setBotName(String botName) {
		this.botName = botName;
	}

	@Override
	public String getBotName() {
		return botName;
	}

	private String opponentName;

	@Override
	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public String getOpponentName() {
		return opponentName;
	}

	private int startingArmies;

	@Override
	public void setStartingArmies(int startingArmies) {
		this.startingArmies = startingArmies;
	}

	public int getStartingArmies() {
		return startingArmies;
	}

}
