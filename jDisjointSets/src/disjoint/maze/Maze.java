package disjoint.maze;

import disjoint.maze.MazeGenerator.MazeNode;

public class Maze {
	private final int width;
	private final int height;

	public Maze(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	private MazeNode start;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private MazeNode end;

	public MazeNode getStart() {
		return start;
	}

	public void setStart(MazeNode start) {
		this.start = start;
	}

	public MazeNode getEnd() {
		return end;
	}

	public void setEnd(MazeNode end) {
		this.end = end;
	}
}