package de.beheh.warlight2.game.map;

/**
 *
 * @author Benedict Etzel
 */
public class AbstractRegion {

	protected final int id;

	public AbstractRegion(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.valueOf(getId());
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (!object.getClass().equals(this.getClass())) {
			return false;
		}
		AbstractRegion region = (AbstractRegion) object;
		return region.getId() == getId();
	}

	@Override
	public int hashCode() {
		return getId();
	}

}
