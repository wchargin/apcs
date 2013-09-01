package apcs.searchsort.sort;

import java.util.List;

/**
 * Basic insertion sort. Runs in O(n<sup>2</sup>) time.
 * 
 * @author William Chargin
 * 
 */
public class InsertionSort implements SortingAlgorithm {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		for (int i = 1; i < list.size(); i++) {
			int indexToInsert = 0;
			T item = list.get(i);
			while (indexToInsert < i
					&& list.get(indexToInsert).compareTo(item) < 0) {
				indexToInsert++;
			}
			list.remove(i);
			list.add(indexToInsert, item);
		}
		return list;
	}

}