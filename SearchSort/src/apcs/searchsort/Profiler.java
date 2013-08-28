package apcs.searchsort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wchargin
 * 
 * @param <I>
 *            input for the profiler
 * @param <O>
 *            output from each run
 * @param <F>
 *            final output
 */
public abstract class Profiler<I, O extends TimeStorage, F> {

	protected void beforeProfiling(I input) {
	}

	protected void beforeRun(I input) {
	}

	protected abstract O run(I input);

	protected void afterRun(I input) {
	}

	protected void afterProfiling(I input) {
	}

	protected abstract F synthesize(List<O> o);

	/**
	 * Runs the profiler.
	 * 
	 * @param input
	 *            the input parameters
	 * @param runs
	 *            the number of runs
	 * @return the final output
	 */
	public F profile(I input, int runs) {
		beforeProfiling(input);

		List<O> outputs = new ArrayList<O>(runs);

		long[] times = new long[runs];
		for (int i = 0; i < runs; i++) {
			beforeRun(input);

			long startTime = System.currentTimeMillis();
			O o = run(input);
			long time = System.currentTimeMillis() - startTime;

			outputs.add(o);
			o.setTime(time);

			times[i] = time;
			afterRun(input);
		}

		afterProfiling(input);

		return synthesize(outputs);
	}

}
