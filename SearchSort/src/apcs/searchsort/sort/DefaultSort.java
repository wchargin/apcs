package apcs.searchsort.sort;

import java.util.Collections;
import java.util.List;

/**
 * A delegation to Java's default {@link Collections#sort(List)} method for
 * benchmarking.
 * 
 * @author William Chargin
 * 
 */
public class DefaultSort implements SortingAlgorithm {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		Collections.sort(list);
		return list;
	}

}
