package de.beheh.warlight2.game.map;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class SuperRegion extends AbstractRegion {

	protected int bonus;

	public SuperRegion(int id, int bonus) {
		super(id);
		this.bonus = bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getBonus() {
		return bonus;
	}

}
