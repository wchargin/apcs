package apcs.searchsort.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apcs.searchsort.Profiler;
import apcs.searchsort.TimedRun;
import apcs.searchsort.sort.SortingProfiler.SortingSettings;

public class SortingProfiler<T extends Comparable<? super T>>
		extends
		Profiler<SortingSettings<T>, TimedRun, SortingProfiler<T>.SortingProfileResult> {

	@Override
	protected void beforeProfiling(SortingSettings<T> input) {
		input.filler.fillList(input.list, 0, input.size);
	}

	@Override
	protected void beforeRun(SortingSettings<T> input) {
		Collections.shuffle(input.list);
	}

	public static class SortingSettings<U extends Comparable<? super U>> {
		private final SortingAlgorithm sa;
		private final ListFiller<U> filler;
		private final int size;
		private final List<U> list;

		public SortingSettings(SortingAlgorithm sa, ListFiller<U> filler,
				int size) {
			super();
			this.sa = sa;
			this.filler = filler;
			this.size = size;
			this.list = new ArrayList<>(this.size);
		}
	}

	public class SortingProfileResult {
		public final double averageTime;
		public final double stddev;

		public SortingProfileResult(double averageTime, double stddev) {
			super();
			this.averageTime = averageTime;
			this.stddev = stddev;
		}
	}

	/**
	 * A generic interface for filling lists.
	 * 
	 * @author William Chargin
	 * 
	 * @param <T>
	 *            the type of objects with which this filler fills lists
	 */
	public interface ListFiller<T> {

		/**
		 * Fills the given range of the given list with values.
		 * 
		 * @param listToFill
		 *            the list to fill
		 * @param startIndex
		 *            the start index for filling (inclusive)
		 * @param stopIndex
		 *            the stop index for filling (exclusive)
		 */
		public void fillList(List<T> listToFill, int startIndex, int stopIndex);
	}

	@Override
	protected TimedRun run(SortingSettings<T> input) {
		input.sa.sort(input.list);
		return new TimedRun();
	}

	@Override
	protected SortingProfileResult synthesize(List<TimedRun> o) {
		double each = 1d / o.size();
		double mean = 0;
		double stddev = 0;
		for (TimedRun tr : o) {
			mean += each * tr.getTime();
		}
		for (int i = 0; i < o.size(); i++) {
			stddev += each * Math.pow(mean - o.get(i).getTime(), 2);
		}
		stddev = Math.sqrt(stddev);
		return new SortingProfileResult(mean, stddev);
	}
}
