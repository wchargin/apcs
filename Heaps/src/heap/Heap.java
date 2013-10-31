package heap;

/**
 * A max-heap implementation.
 * 
 * @author William Chargin
 * 
 * @param <E>
 *            the type of elements stored in this heap
 */
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class Heap<E> extends AbstractQueue<E> {

	public static <E extends Comparable<? super E>> Heap<E> buildHeap(
			Collection<? extends E> input) {
		return setupEmptyHeap(input, Heap.<E> fromNaturalOrder());
	}

	public static <E> Heap<E> buildHeap(Collection<? extends E> input,
			Comparator<? super E> comparator) {
		return setupEmptyHeap(input, Heap.<E> fromComparator(comparator));
	}

	/**
	 * Creates a heap ordered by the given comparator.
	 * 
	 * @param comparator
	 *            the comparator to use for ordering
	 * @return a new heap
	 */
	public static <E> Heap<E> fromComparator(Comparator<? super E> comparator) {
		return new Heap<E>(comparator);
	}

	/**
	 * Creates a heap ordered by the natural ordering of the keys.
	 * 
	 * @return a new heap
	 */
	public static <E extends Comparable<? super E>> Heap<E> fromNaturalOrder() {
		class NaturalOrderComparator implements Comparator<E> {
			@Override
			public int compare(E u, E v) {
				return u.compareTo(v);
			}
		}
		return new Heap<E>(new NaturalOrderComparator());
	}

	/**
	 * Finds the index of the left child of the element with the given index.
	 * 
	 * @param index
	 *            the index of the element whose left child to find
	 * @return the index of the left child
	 */
	private static final int left(int index) {
		return index * 2;
	}

	@Override
	public boolean contains(Object o) {
		System.out.println(Thread.currentThread().getStackTrace()[2]
				+ "\tContains " + o + "? " + super.contains(o));
		return super.contains(o);
	}

	/**
	 * Finds the index of the parent element of the element with the given
	 * index.
	 * 
	 * @param index
	 *            the index of the element whose parent to find
	 * @return the index of the parent element
	 */
	private static final int parent(int index) {
		return index / 2;
	}

	/**
	 * Finds the index of the right child of the element with the given index.
	 * 
	 * @param index
	 *            the index of the element whose right child to find
	 * @return the index of the right child
	 */
	private static final int right(int index) {
		return index * 2 + 1;
	}

	private static <E> Heap<E> setupEmptyHeap(Collection<? extends E> input,
			Heap<E> heap) {
		heap.initializeHeap(input.size() + 1);
		System.arraycopy(input.toArray(), 0, heap.array, 1, input.size());
		for (int i = heap.size / 2; i >= 1; i--) {
			heap.heapify(i);
		}
		return heap;
	}

	/**
	 * The storage for this heap.
	 */
	private E[] array;

	/**
	 * The size of this heap.
	 */
	private int size;

	/**
	 * The comparator used for this heap.
	 */
	private final Comparator<? super E> comparator;

	/**
	 * Creates a heap with the given comparator.
	 * 
	 * @param comparator
	 *            the comparator for this heap
	 */
	private Heap(Comparator<? super E> comparator) {
		this.comparator = comparator;
		initializeHeap(8);
	}

	/**
	 * Ensures that there is capacity to insert another element, and doubles the
	 * size of the array if there is not.
	 */
	private void ensureCapacity() {
		if (size > array.length - 1) {
			E[] old = array;
			initializeHeap(array.length * 2);
			System.arraycopy(old, 1, array, 1, old.length - 1);
		}
	}

	/**
	 * Maintains the heap property given a subtree root index.
	 * 
	 * @param index
	 *            the index of the root of the subtree
	 */
	private void heapify(int index) {
		int l = left(index), r = right(index);
		int largestIndex = index;
		if (l < size && comparator.compare(array[l], array[index]) > 0) {
			largestIndex = l;
		}
		if (r < size && comparator.compare(array[r], array[largestIndex]) > 0) {
			largestIndex = r;
		}
		if (largestIndex != index) {
			E largestValue = array[largestIndex];
			array[largestIndex] = array[index];
			array[index] = largestValue;
			heapify(largestIndex);
		}
	}

	/**
	 * Initializes a heap of the given length.
	 * 
	 * @param length
	 *            the length of the array
	 */
	@SuppressWarnings("unchecked")
	private void initializeHeap(int length) {
		array = (E[]) new Object[length];
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {

			/**
			 * The current index.
			 */
			int index = 1;

			boolean removed = false;

			@Override
			public boolean hasNext() {
				return index <= size;
			}

			@Override
			public E next() {
				removed = false;
				return array[index++];
			}

			@Override
			public void remove() {
				index--;
				if (removed || index < 1 || index > size) {
					throw new IllegalStateException();
				}
				removed = true;
				array[index] = array[size]; // replace with the last
				size--; // decrement size
				heapify(1); // maintain the heap property
			}

		};
	}

	@Override
	public boolean offer(E e) {
		int i = ++size;
		ensureCapacity();
		for (; i > 1 && comparator.compare(array[parent(i)], e) < 0; i = parent(i)) {
			array[i] = array[parent(i)];
		}
		array[i] = e;
		return true;
	}

	@Override
	public E peek() {
		return size > 0 ? array[1] : null;
	}

	@Override
	public E poll() {
		if (size() < 1) {
			return null;
		}
		E max = array[1]; // extract the max element
		array[1] = array[size]; // swap with the last
		size--; // decrement size
		heapify(1); // maintain the heap property
		return max;
	}

	@Override
	public int size() {
		return size;
	}

}
