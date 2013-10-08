package disjoint.maze;

/**
 * An edge in a grid or maze.
 * 
 * @author William Chargin
 * 
 */
public class Edge {

	/**
	 * The x-coordinate of this edge.
	 */
	private final int x;

	/**
	 * The y-coordinate of this edge.
	 */
	private final int y;

	/**
	 * The direction of this edge.
	 */
	private final Direction direction;

	/**
	 * Creates an edge with the given parameters
	 * 
	 * @param x
	 *            the x-coordinate of this edge
	 * @param y
	 *            the y-coordinate of this edge
	 * @param direction
	 *            the direction of this edge
	 */
	public Edge(int x, int y, Direction direction) {
		super();
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	/**
	 * Gets this edge's direction.
	 * 
	 * @return this edge's directions
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Gets this edge's x-coordinate.
	 * 
	 * @return this edge's x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets this edge's y-coordinate.
	 * 
	 * @return this edge's y-coordinate
	 */
	public int getY() {
		return y;
	}

}
