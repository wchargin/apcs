package apcs.searchsort.sort;

import java.util.List;

/**
 * Recursive selection sort. Runs in O(n<sup>2</sup>) time.
 * 
 * @author William Chargin
 * 
 */
public class RecursiveSelectionSort implements SortingAlgorithm {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		return selectionSort(list, 0);
	}

	/**
	 * Helper method for recursive selection sort.
	 * 
	 * @param list
	 *            the list to sort
	 * @param start
	 *            the index at which to start selecting; the sublist given by
	 *            <code>list.{@link List#subList(int, int) subList}(0, start)</code>
	 *            should be already sorted for this method to work properly
	 * @return the final sorted list
	 */
	private static <T extends Comparable<? super T>> List<T> selectionSort(
			List<T> list, int start) {
		if (start == list.size()) {
			return list;
		}

		int minimum = start;
		T minimumValue = list.get(minimum);
		int i = start + 1;
		while (i < list.size()) {
			T value = list.get(i);
			minimumValue = list.get(minimum);
			if (value.compareTo(minimumValue) < 0) {
				// smaller than the minimum
				minimum = i;
				minimumValue = value;
			}
			i++;
		}

		list.set(minimum, list.get(start));
		list.set(start, minimumValue);

		return selectionSort(list, start + 1);
	}
}
