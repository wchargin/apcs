package prodcons;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * A buffer, the shared memory accessed by producers and consumers.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of object stored in this buffer
 */
class Buffer<T> {

	/**
	 * The buffer of items that have been produced and not yet consumed.
	 */
	private Queue<T> buffer;

	/**
	 * The mutual-exclusion binary semaphore used as a lock.
	 */
	private Semaphore mutex;

	/**
	 * The semaphore for the number of slots currently available.
	 */
	private Semaphore slots;

	/**
	 * The semaphore for the number of items currently produced.
	 */
	private Semaphore items;

	/**
	 * Creates a buffer of the given size.
	 * 
	 * @param size
	 *            the buffer size
	 */
	public Buffer(int size) {
		buffer = new LinkedList<T>();
		mutex = new Semaphore(1);
		slots = new Semaphore(size);
		items = new Semaphore(0);
	}

	/**
	 * Inserts an item into this buffer.
	 * 
	 * @param item
	 *            the new item
	 * @throws InterruptedException
	 *             if the thread is interrupted while waiting for a lock
	 */
	public void insert(T item) throws InterruptedException {
		slots.acquire();
		mutex.acquire();
		buffer.add(item);
		mutex.release();
		items.release();
	}

	/**
	 * Removes an item from this buffer to be consumed.
	 * 
	 * @return a previously produced item
	 * @throws InterruptedException
	 *             if the thread is interrupted while waiting for a lock
	 */
	public T remove() throws InterruptedException {
		items.acquire();
		mutex.acquire();
		T t = buffer.poll();
		mutex.release();
		slots.release();
		return t;
	}

	/**
	 * Returns the number of elements in the buffer.
	 * 
	 * @return the number of elements
	 * @see java.util.Queue#size()
	 */
	public int size() {
		return buffer.size();
	}
}