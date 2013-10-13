package disjoint.maze;

import java.util.ArrayList;
import java.util.List;

public class MazeTimer {

	private static class Timer {
		private static final double NANOS = 1_000_000_000d;
		private long s = System.nanoTime();
		private long e;

		public void z() {
			e = System.nanoTime();
		}

		public long nanotime() {
			return e - s;
		}

		public double secondtime() {
			return nanotime() / NANOS;
		}

		@Override
		public String toString() {
			return toString(1);
		}

		public String toString(double factor) {
			return (nanotime() * factor) + " ns = " + (secondtime() * factor)
					+ " s";
		}
	}

	/**
	 * @param args
	 *            the first argument should be the size of the maze to build
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException(
					"Usage: java disjoint.maze.MazeTimer side_length [num_trials=5]");
		}
		int sideLength = 0;
		int numTrials = 5;
		try {
			sideLength = Integer.parseInt(sanitize(args[0]));
		} catch (NumberFormatException nfe) {
			System.err.println("Invalid number. Stack trace:");
			throw nfe;
		}
		if (sideLength <= 0) {
			throw new IllegalArgumentException(
					"Invalid side length; must be positive.");
		}
		if (args.length > 1) {
			try {
				numTrials = Math.max(1, Integer.parseInt(sanitize(args[1])));
			} finally {
			}
		}
		System.out.println("Side length: " + sideLength);
		System.out.println("Initializing JVM (warmup stage)...");
		List<Maze> mazes = new ArrayList<>();
		generateMaze(10);
		generateMaze(20);
		generateMaze(30);

		{
			System.out.println("Warmup complete. Generating maze.");
			System.out.println();
			Timer gen = new Timer();
			System.out.println("Start epoch time (nanoseconds): " + gen.s);
			System.out.println("Running " + numTrials + " trials...");
			for (int i = numTrials; i > 0; i--) {
				mazes.add(generateMaze(sideLength));
			}
			gen.z();
			System.out.println("Done. End epoch time (nanoseconds): " + gen.e);
			System.out.println("  Total time: " + gen);
			System.out.println("Average time: " + gen.toString(1d / numTrials));
		}

		{
			System.out.println();
			System.out.println("Solving mazes...");
			Timer solve = new Timer();
			for (Maze m : mazes) {
				MazeSolver.solve(m);
			}
			solve.z();
			System.out
					.println("Done. End epoch time (nanoseconds): " + solve.e);
			System.out.println("  Total time: " + solve);
			System.out.println("Average time: "
					+ solve.toString(1d / numTrials));
		}

		System.out.println();
		System.out.println("Tests complete, exiting.");
	}

	private static String sanitize(String number) {
		return number.replaceAll("\\D", "");
	}

	private static Maze generateMaze(int sideLength) {
		final MazeGenerator mazeGenerator = new MazeGenerator(sideLength,
				sideLength);
		mazeGenerator.finish();
		return mazeGenerator.getMaze();
	}

}
