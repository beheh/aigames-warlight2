package de.beheh.warlight2.game;

/**
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Player {

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
}
