package apcs.searchsort.sort;

import java.util.List;

/**
 * Abstract quicksort algorithm. This class implements the partition step of
 * quicksort but allows subclasses to select the pivot.
 * 
 * @author William Chargin
 * 
 */
public abstract class AbstractQuickSort implements SortingAlgorithm {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		quicksort(list, 0, list.size() - 1);
		return list;
	}

	/**
	 * Helper method for quicksorting a sublist of the given list.
	 * 
	 * @param list
	 *            the list to sort
	 * @param start
	 *            the start index (inclusive)
	 * @param end
	 *            the end index (inclusive)
	 */
	private <T extends Comparable<? super T>> void quicksort(List<T> list,
			int start, int end) {
		// Declare variables to traverse until finding elements to swap
		int left = start;
		int right = end;

		// Delegate to subclasses to select pivot element
		Comparable<? super T> pivot = selectPivot(list, start, end);

		while (left <= right) {
			// Find the first value in the left list that's larger than the
			// pivot
			while (pivot.compareTo(list.get(left)) > 0) {
				left++;
			}

			// and v.v. for the right list
			while (pivot.compareTo(list.get(right)) < 0) {
				right--;
			}

			// Ensure validity of values, then perform a swap
			if (left <= right) {
				T leftValue = list.get(left);
				list.set(left, list.get(right));
				list.set(right, leftValue);
				left++;
				right--;
			}
		}
		// Recurse each half of the list
		if (start < right) {
			quicksort(list, start, right);
		}
		if (left < end) {
			quicksort(list, left, end);
		}
	}

	/**
	 * Determines the pivot index for the given sublist.
	 * 
	 * @param list
	 *            the list
	 * @param start
	 *            the start index for pivot (inclusive)
	 * @param end
	 *            the end index for pivot (inclusive)
	 * @return the pivot value
	 */
	protected abstract <T extends Comparable<? super T>> Comparable<? super T> selectPivot(
			List<T> list, int start, int end);

}
