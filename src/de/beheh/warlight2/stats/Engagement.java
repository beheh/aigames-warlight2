package de.beheh.warlight2.stats;

/**
 *
 * @author Benedict Etzel
 */
public abstract class Engagement {

	public final static double variance = 0.16d;

	public static boolean isGuaranteed(int attackers, int defenders) {
		int originalAttackers = attackers;
		int originalDefenders = defenders;
		defenders = dealMinimumDamage(originalAttackers, originalDefenders, 0.6);
		attackers = dealMaximumDamage(originalDefenders, originalAttackers, 0.7);
		return !(defenders > 0 || attackers < 1);
	}

	public static int dealMinimumDamage(int dealers, int receivers, double chance) {
		return dealDamage(dealers, receivers, chance, 0);
	}

	public static int dealMaximumDamage(int dealers, int receivers, double chance) {
		return dealDamage(dealers, receivers, chance, 1);
	}

	public static int dealDamage(int dealers, int receivers, double chance, double randomization) {
		double destroyed = (chance * dealers * (1d - variance)) + (0.16 * randomization * dealers);
		return Math.max(receivers - (int) Math.round(destroyed), 0);
	}
}
