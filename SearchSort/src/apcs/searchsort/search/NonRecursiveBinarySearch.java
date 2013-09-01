package apcs.searchsort.search;

import java.util.List;

/**
 * A non-recursive implementation of binary search. Runs in O(log n) time.
 * 
 * @author William Chargin
 * 
 */
public class NonRecursiveBinarySearch implements SearchAlgorithm {

	@Override
	public <T extends Comparable<? super T>> int indexOf(List<T> list, T target) {
		int start = 0;
		int end = list.size() - 1;
		if (end < start) {
			// Something went wrong or it doesn't exist.
			return -1;
		}

		int middle, compared;
		T atMiddle;

		do {
			middle = start + (end - start) / 2;
			atMiddle = list.get(middle);

			compared = atMiddle.compareTo(target);

			// compared <=> 0 as atMiddle <=> target
			if (compared > 0) {
				// atMiddle > target; target in the left half
				end = middle - 1;
			} else if (compared < 0) {
				// atMiddle < target; target in the right half
				start = middle + 1;
			}
		} while (compared != 0);
		return middle;
	}

}
