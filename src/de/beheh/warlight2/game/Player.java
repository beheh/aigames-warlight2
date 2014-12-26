package de.beheh.warlight2.game;

import java.io.Serializable;

/**
 *
 * @author Benedict Etzel
 */
public class Player implements Serializable, Cloneable {

	protected String name;

	public Player(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (!object.getClass().equals(this.getClass())) {
			return false;
		}
		Player player = (Player) object;
		return player.getName().equals(getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public Object clone() {
		Player clone = new Player(name);
		return clone;
	}

}
