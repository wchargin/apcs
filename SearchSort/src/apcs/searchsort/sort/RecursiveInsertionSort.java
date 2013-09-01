package apcs.searchsort.sort;

import java.util.List;

/**
 * Recursive insertion sort. Runs in O(n<sup>2</sup>) time.
 * 
 * @author William Chargin
 * 
 */
public class RecursiveInsertionSort implements SortingAlgorithm {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		return insertionSort(list, 0);
	}

	private static <T extends Comparable<? super T>> List<T> insertionSort(
			List<T> list, int index) {
		if (index == list.size()) {
			return list;
		}
		T itemToInsert = list.get(index);
		int where = 0;
		while (where < index && list.get(where).compareTo(itemToInsert) < 0) {
			where++;
		}
		list.remove(index);
		list.add(where, itemToInsert);

		return insertionSort(list, index + 1);
	}
}
