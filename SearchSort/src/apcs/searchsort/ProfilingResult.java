package apcs.searchsort;

/**
 * A profiling result. All times in nanoseconds.
 * 
 * @author William Chargin
 * 
 */
public class ProfilingResult {

	/**
	 * The average time per run.
	 */
	public final double averageTime;

	/**
	 * The standard deviation of the run times.
	 */
	public final double stddev;

	/**
	 * Creates the result with the given arguments.
	 * 
	 * @param averageTime
	 *            the averaege time per run
	 * @param stddev
	 *            the standard deviation
	 */
	public ProfilingResult(double averageTime, double stddev) {
		super();
		this.averageTime = averageTime;
		this.stddev = stddev;
	}

}
