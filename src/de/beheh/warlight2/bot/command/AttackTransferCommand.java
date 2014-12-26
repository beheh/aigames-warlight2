package de.beheh.warlight2.bot.command;

import de.beheh.warlight2.game.GameState;
import de.beheh.warlight2.game.Player;
import de.beheh.warlight2.game.map.Region;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benedict Etzel
 */
public class AttackTransferCommand extends Command {

	public AttackTransferCommand(GameState gameTracker) {
		super(gameTracker);
	}

	List<Movement> movements = new ArrayList<>();

	public void attack(Region from, Region to, int armyCount) {
		if (from.getOwner() == to.getOwner()) {
			throw new RuntimeException("attempting to attack own region (from " + from + " to " + to + ")");
		}
		move(from, to, armyCount);
	}

	public void transfer(Region from, Region to, int armyCount) {
		if (from.getOwner() != to.getOwner()) {
			throw new RuntimeException("attempting to transfer to enemy or neutral region (from " + from + " to " + to + ")");
		}
		move(from, to, armyCount);
	}

	public void move(Region from, Region to, int armyCount) {
		if (from == null || to == null) {
			throw new IllegalArgumentException("from/to can't be null");
		}
		if (armyCount < 1) {
			throw new IllegalArgumentException("invalid armyCount " + armyCount);
		}
		if (!from.isNeighbor(to)) {
			throw new IllegalArgumentException("cannot attack/transfer if regions aren't neighbors (from " + from + " to " + to + ")");
		}
		movements.add(new Movement(from, to, armyCount));
	}

	protected class Movement {

		protected Region from;
		protected Region to;
		protected int armycount;

		public Movement(Region from, Region to, int armycount) {
			this.from = from;
			this.to = to;
			this.armycount = armycount;
		}

		public Region getFrom() {
			return from;
		}

		public Region getTo() {
			return to;
		}

		public int getArmycount() {
			return armycount;
		}
	}

	@Override
	public String toString() {
		if (movements.isEmpty()) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;
		for (Movement movement : movements) {
			if (!first) {
				stringBuilder.append(", ");
			} else {
				first = false;
			}
			stringBuilder.append(movement.getFrom().getOwner().getName()).append(" attack/transfer ");
			stringBuilder.append(movement.getFrom().getId()).append(" ");
			stringBuilder.append(movement.getTo().getId()).append(" ");
			stringBuilder.append(movement.getArmycount());
		}
		return stringBuilder.toString();
	}

}
