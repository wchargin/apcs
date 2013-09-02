package apcs.searchsort.sort;

import java.util.List;

/**
 * A quicksort implementation that uses a randomized pivot.
 * 
 * @author William Chargin
 * 
 */
public class RandomizedQuickSort extends AbstractQuickSort {

	@Override
	protected <T extends Comparable<? super T>> Comparable<? super T> selectPivot(
			List<T> list, int start, int end) {
		return list.get(start + (int) (Math.random() * (end - start)));
	}

}
