package apcs.searchsort.sort;

import java.util.List;

/**
 * A quicksort implementation that uses the median of the start, end, and
 * midpoint elements as the pivot.
 * 
 * @author William Chargin
 * 
 */
public class MedianQuickSort extends AbstractQuickSort {

	@Override
	protected <T extends Comparable<? super T>> Comparable<? super T> selectPivot(
			List<T> list, int start, int end) {
		T a = list.get(start);
		T b = list.get(start + (end - start) / 2);
		T c = list.get(end);

		// For explanation of the following, see:
		// http://stackoverflow.com/a/1582524/732016

		if (a.compareTo(b) > 0) {
			if (b.compareTo(c) > 0) {
				return b;
			} else if (a.compareTo(c) > 0) {
				return c;
			} else {
				return a;
			}
		} else {
			if (a.compareTo(c) > 0) {
				return a;
			} else if (b.compareTo(c) > 0) {
				return c;
			} else {
				return b;
			}
		}

	}

}
