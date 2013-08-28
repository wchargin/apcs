package apcs.searchsort;

/**
 * A profiling result. All times in nanoseconds.
 * 
 * @author William Chargin
 * 
 */
public class ProfilingResult {

	public final long averageTime;
	public final long stddev;

	public ProfilingResult(long averageTime, long stddev) {
		super();
		this.averageTime = averageTime;
		this.stddev = stddev;
	}

}
