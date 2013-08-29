package apcs.searchsort;

/**
 * A basic implementation of {@link TimeStorage}, used for profilers whose
 * intermediary output is only a time value.
 * 
 * @author William Chargin
 * 
 */
public class TimedRun implements TimeStorage {

	/**
	 * The time spent on this run.
	 */
	private long time;

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Creates the timed run with the given time.
	 * 
	 * @param time
	 *            the time
	 */
	public TimedRun(long time) {
		super();
		this.time = time;
	}

	/**
	 * Creates the timed run with a default time of zero.
	 */
	public TimedRun() {
		this(0);
	}

}