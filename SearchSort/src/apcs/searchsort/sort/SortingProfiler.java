package apcs.searchsort.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import apcs.searchsort.Profiler;
import apcs.searchsort.ProfilingResult;
import apcs.searchsort.TimedRun;
import apcs.searchsort.sort.SortingProfiler.SortingSettings;

/**
 * A class for profiling sorting algorithms.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the class of sorting algorithms
 */
public class SortingProfiler<T extends Comparable<? super T>> extends
		Profiler<SortingSettings<T>, TimedRun, ProfilingResult> {

	@Override
	protected void beforeProfiling(SortingSettings<T> input) {
		input.filler.fillList(input.list, 0, input.size);
	}

	@Override
	protected void beforeRun(SortingSettings<T> input) {
		Collections.shuffle(input.list);
	}

	/**
	 * Settings provided to a profiler, including the algorithm to profile and a
	 * list-generating method.
	 * 
	 * @author William Chargin
	 * 
	 * @param <U>
	 *            the type of object in the list to be sorted by the algorithm
	 *            to be profiled
	 */
	public static class SortingSettings<U extends Comparable<? super U>> {
		private final SortingAlgorithm sa;
		private final ListFiller<U> filler;
		private final int size;
		private final List<U> list;

		/**Creates the settings.
		 * @param sa the algorithm to profile
		 * @param filler the list-filling method
		 * @param size the size of the list to generate
		 */
		public SortingSettings(SortingAlgorithm sa, ListFiller<U> filler,
				int size) {
			super();
			this.sa = sa;
			this.filler = filler;
			this.size = size;
			this.list = new ArrayList<>(this.size);
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
	protected ProfilingResult synthesize(List<TimedRun> o) {
		return SortingProfiler.generateStandardResult(o);
	}
}
