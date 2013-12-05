package dyn;

import java.util.HashMap;
import java.util.Map;

/**
 * A test case for dynamic programming using Fibonacci numbers.
 * 
 * @author William Chargin
 * 
 */
public class Fibonacci {

	/**
	 * An interface to allow generation of Fibonacci numbers.
	 * 
	 * @author William Chargin
	 * 
	 */
	public static interface Fibber {

		/**
		 * Calculates the {@code n}th Fibonacci number, given by the recurrence
		 * <ul>
		 * <li>F<sub>1</sub> = F<sub>2</sub> = 1</li>
		 * <li>F<sub><em>n</em></sub> = F<sub><em>n</em>&minus;1</sub> + F<sub>
		 * <em>n</em>&minus;2</sub></li>
		 * </ul>
		 * 
		 * @param n
		 *            the index of the number to return
		 * @return the given Fibonacci number
		 */
		public long getFibonacci(int n);

	}

	/**
	 * A memoized Fibonacci implementation.
	 * 
	 * @author William Chargin
	 * 
	 */
	static class MemoizedFibber implements Fibber {

		/**
		 * The memo used for storage.
		 */
		private static final Map<Integer, Long> memo = new HashMap<>();

		@Override
		public long getFibonacci(int n) {
			Long f = memo.get(n);
			if (f != null) {
				return (long) f;
			}
			f = n <= 2 ? 1 : getFibonacci(n - 2) + getFibonacci(n - 1);
			memo.put(n, f);
			return f;
		}

	}

	/**
	 * A wasteful Fibonacci implementation.
	 * 
	 * @author William Chargin
	 * 
	 */
	static class WastefulFibber implements Fibber {

		@Override
		public long getFibonacci(int n) {
			if (n <= 2) {
				return 1;
			}
			return getFibonacci(n - 2) + getFibonacci(n - 1);
		}

	}

	/**
	 * A main driver to test both the {@link WastefulFibber} and
	 * {@link MemoizedFibber} number generator.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		Fibber bad = new WastefulFibber(), good = new MemoizedFibber();

		time(good, 100_000, 1_000_000);
		System.out.println();
		time(bad, 10, 100);
	}

	/**
	 * Times the execution of a Fibonacci number generator.
	 * 
	 * @param f
	 *            the Fibonacci generator to use
	 * @param step
	 *            the interval at which to print intermediate results
	 * @param upper
	 *            the upper bound of numbers to generate
	 */
	public static void time(Fibber f, int step, int upper) {
		long start = System.currentTimeMillis();

		for (int lower = 0; lower < upper; lower += step) {
			for (int i = lower; i < lower + step; i++) {
				f.getFibonacci(i);
			}
			long time = System.currentTimeMillis() - start;
			System.out.println(f.getClass().getSimpleName() + " :: " + time
					+ " ms for " + lower + ".." + (lower + step - 1));
			start = System.currentTimeMillis();
		}
	}

}
