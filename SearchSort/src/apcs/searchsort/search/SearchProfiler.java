package apcs.searchsort.search;

import java.util.ArrayList;
import java.util.List;

import apcs.searchsort.Profiler;
import apcs.searchsort.ProfilingResult;
import apcs.searchsort.TimedRun;
import apcs.searchsort.search.SearchProfiler.SearchSettings;

/**
 * A class for profiling sorting algorithms.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the class of sorting algorithms
 */
public class SearchProfiler<T extends Comparable<? super T>> extends
		Profiler<SearchSettings<T>, TimedRun, ProfilingResult> {

	@Override
	protected void beforeRun(SearchSettings<T> input) {
		input.nextKey = input.list
				.get((int) (Math.random() * input.list.size()));
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
	public static class SearchSettings<U extends Comparable<? super U>> {
		private final SearchAlgorithm sa;
		private final int size;
		private final List<U> list;
		private U nextKey;

		/**
		 * Creates the settings.
		 * 
		 * @param sa
		 *            the algorithm to profile
		 * @param filler
		 *            the list-filling method
		 * @param size
		 *            the size of the list to generate
		 */
		public SearchSettings(SearchAlgorithm sa, int size) {
			super();
			this.sa = sa;
			this.size = size;
			this.list = new ArrayList<>(this.size);
		}
	}

	@Override
	protected TimedRun run(SearchSettings<T> input) {
		input.sa.indexOf(input.list, input.nextKey);
		return new TimedRun();
	}

	@Override
	protected ProfilingResult synthesize(List<TimedRun> o) {
		return SearchProfiler.generateStandardResult(o);
	}
}
