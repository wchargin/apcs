package apcs.searchsort;


public class TimedRun implements TimeStorage {

	private long time;

	@Override
	public long getTime() {
		return time;
	}

	@Override
	public void setTime(long time) {
		this.time = time;
	}

	public TimedRun(long time) {
		super();
		this.time = time;
	}

	public TimedRun() {
		this(0);
	}

}