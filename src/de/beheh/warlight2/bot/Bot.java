package de.beheh.warlight2.bot;

import de.beheh.warlight2.bot.command.Command;
import de.beheh.warlight2.bot.map.Region;
import java.util.List;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public interface Bot {
	
	/* Internal */
	
	public boolean shouldFailHard();
	
	/* Settings */

	public void setTimebank(long timebank);

	public void setTimePerMove(long timePerMove);

	public void setMaxRounds(int maxRounds);

	public String getBotName();

	public void setBotName(String botName);

	public void setOpponentName(String opponentName);

	public void setStartingArmies(int startingArmies);

	/* Passive callbacks */
	
	public void onOpponentMoves();

	public void onUpdateMap();

	/* Active callbacks */
	
	public int onPickStartingRegion(long time, int[] regions);
	
	public List<Command> onPlaceArmies(long time);

	public List<Command> onAttackTransfer(long time);
}
