package apcs.searchsort.search;

import java.util.List;

/**
 * A basic implementation of binary search. Runs in O(log n) time.
 * 
 * @author William Chargin
 * 
 */
public class BinarySearch implements SearchAlgorithm {

	@Override
	public <T extends Comparable<? super T>> int indexOf(List<T> list, T target) {
		return binarySearch(list, target, 0, list.size() - 1);
	}

	private static <T extends Comparable<? super T>> int binarySearch(
			List<T> list, T target, int start, int end) {
		if (end < start) {
			// Something went wrong or it doesn't exist.
			return -1;
		}

		int middle = start + (end - start) / 2;
		T atMiddle = list.get(middle);

		int compared = atMiddle.compareTo(target);
		// compared <=> 0 as atMiddle <=> target

		// shrink bounds by one to prevent infinite loop
		// (it looks like it still works :/ )
		if (compared > 0) {
			// atMiddle > target; target in the left half
			return binarySearch(list, target, start, middle - 1);
		} else if (compared < 0) {
			// atMiddle < target; target in the right half
			return binarySearch(list, target, middle + 1, end);
		} else {
			// All done!
			return middle;
		}

	}

}
