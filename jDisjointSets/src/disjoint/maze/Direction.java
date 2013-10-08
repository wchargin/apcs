package disjoint.maze;

import static disjoint.maze.Direction.Sign.*;

/**
 * A list of direction constants.
 * 
 * @author William Chargin
 * 
 */
public enum Direction {

	/**
	 * The eastern direction.
	 */
	EAST(POSITIVE, ZERO),

	/**
	 * The northern direction.
	 */
	NORTH(ZERO, POSITIVE),

	/**
	 * The western direction.
	 */
	WEST(NEGATIVE, ZERO),

	/**
	 * The southern direction.
	 */
	SOUTH(ZERO, NEGATIVE),

	/**
	 * The northeastern direction.
	 */
	NORTHEAST(POSITIVE, POSITIVE),

	/**
	 * The northwestern direction.
	 */
	NORTHWEST(NEGATIVE, POSITIVE),

	/**
	 * The southweestern direction.
	 */
	SOUTHWEST(NEGATIVE, NEGATIVE),

	/**
	 * The southeastern direction.
	 */
	SOUTHEAST(POSITIVE, NEGATIVE),

	/**
	 * The zero vector.
	 */
	NONE(ZERO, ZERO);

	/**
	 * The sign of a component.
	 * 
	 * @author William Chargin
	 * 
	 */
	public enum Sign {

		/**
		 * Indicates a negative sign.
		 */
		NEGATIVE,

		/**
		 * Indicates a zero sign.
		 */
		ZERO,

		/**
		 * Indicates a positive sign.
		 */
		POSITIVE;

		/**
		 * Adds the two signs. A zero sign added with anything will return the
		 * other addend. A nonzero sign added with its {@link #opposite()
		 * opposite} will return {@link #ZERO}, and added with anything else
		 * will return itself.
		 * 
		 * @param other
		 *            the sign to add to this sign
		 * @return the sum of the two signs
		 */
		public Sign add(Sign other) {
			switch (this) {
			case ZERO:
				return other;
			case POSITIVE:
				return other == NEGATIVE ? ZERO : POSITIVE;
			case NEGATIVE:
				return other == POSITIVE ? ZERO : NEGATIVE;
			}
			throw invalidValue();
		}

		/**
		 * Creates an exception indicating that there is no valid value or that
		 * an invalid value has been input. Typical usage is
		 * {@code throw invalidValue()}.
		 * 
		 * @return a new exception
		 */
		private IllegalArgumentException invalidValue() {
			return new IllegalArgumentException("Invalid value.");
		}

		/**
		 * Gets the opposite of this sign.
		 * 
		 * @return the opposite
		 */
		public Sign opposite() {
			switch (this) {
			case ZERO:
				return ZERO;
			case POSITIVE:
				return NEGATIVE;
			case NEGATIVE:
				return POSITIVE;
			}
			throw invalidValue();
		}

		/**
		 * Subtracts the other sign from this sign. This is the same as calling
		 * {@link #add(Sign)} with the {@link #opposite} of the other sign.
		 * 
		 * @param other
		 *            the sign to subtract
		 * @return the difference of the two signs
		 */
		public Sign subtract(Sign other) {
			return add(other.opposite());
		}
	}

	/**
	 * The x-component of this direction.
	 */
	private final Sign x;

	/**
	 * The y-component of this direction.
	 */
	private final Sign y;

	/**
	 * Creates the direction with the given {@code x} and {@code y} components.
	 * 
	 * @param x
	 *            the x component
	 * @param y
	 *            the y component
	 */
	private Direction(Sign x, Sign y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Performs a signed vector addition with the two components.
	 * 
	 * @param other
	 *            the direction to add
	 * @return the added direction
	 */
	public Direction add(Direction other) {
		return fromComponents(this.x.add(other.x), this.y.add(other.y));
	}

	/**
	 * Takes the opposite of this direction in each component.
	 * 
	 * @return the opposite direction
	 */
	public Direction opposite() {
		return fromComponents(this.x.opposite(), this.y.opposite());
	}

	/**
	 * Gets the direction with the given {@code x} and {@code y} components.
	 * 
	 * @param x
	 *            the x-component of the desired direction
	 * @param y
	 *            the y-component of the desired direction
	 * @return your most deeply desired direction
	 */
	public static Direction fromComponents(Sign x, Sign y) {
		for (Direction direction : values()) {
			if (direction.x == x && direction.y == y) {
				return direction;
			}
		}
		return null;
	}

}
