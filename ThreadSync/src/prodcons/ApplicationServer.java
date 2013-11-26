package prodcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationServer<T> {

	/**
	 * An object that can consume items.
	 * 
	 * @author William Chargin
	 * 
	 * @param <T>
	 *            the type of items consumed by this object
	 */
	public interface Consumer<T> {
		public void consume(T t);
	}

	/**
	 * An object that can produce items.
	 * 
	 * @author William Chargin
	 * 
	 * @param <T>
	 *            the type of items produced by this object
	 */
	public interface Producer<T> {
		public T produce();
	}

	/**
	 * Registers a given item-thread pair in the given map.
	 * 
	 * @param map
	 *            the map to mutate
	 * @param item
	 *            the item to use as key
	 * @param thread
	 *            the thread to start and use as list-value
	 */
	private static <T> void registerThread(Map<? super T, List<Thread>> map,
			T item, Thread thread) {
		List<Thread> threadList = map.get(item);
		if (threadList == null) {
			map.put(item, threadList = new ArrayList<>());
		}
		threadList.add(thread);
		thread.start();
	}

	/**
	 * Stops (interrupts) all threads in a map, and then clears the map. This
	 * method locks on {@code map}.
	 * 
	 * @param map
	 *            the map to flush
	 */
	private static <T> void unregisterAll(Map<? super T, List<Thread>> map) {
		synchronized (map) {
			for (List<Thread> threadList : map.values())
				for (Thread t : threadList)
					t.interrupt();
			map.clear();
		}
	}

	/**
	 * Gets the current size of the buffer.
	 * 
	 * @return the buffer size
	 */
	public int size() {
		return buf.size();
	}

	/**
	 * Interrupts and registers all threads of a given key from a map.
	 * 
	 * @param map
	 *            the map to mutate
	 * @param t
	 *            the key to flush
	 */
	private static <T> void unregisterThreads(Map<? super T, List<Thread>> map,
			T t) {
		List<Thread> threadList = map.get(t);
		if (threadList == null) {
			return;
		}
		for (Thread thread : threadList) {
			thread.interrupt();
		}
		threadList.clear();
		map.remove(t);
	}

	/**
	 * The buffer used for storage.
	 */
	private Buffer<T> buf;

	/**
	 * The list of producers and their corresponding threads.
	 */
	private Map<Producer<? extends T>, List<Thread>> producers;

	/**
	 * The list of consumers and their corresponding threads.
	 */
	private Map<Consumer<? super T>, List<Thread>> consumers;

	/**
	 * Creates an application server with a given buffer size.
	 * 
	 * @param size
	 *            the initial buffer size
	 */
	public ApplicationServer(int size) {
		buf = new Buffer<>(size);
		producers = new HashMap<>();
		consumers = new HashMap<>();
	}

	/**
	 * Registers the given consumer on this server.
	 * 
	 * @param consumer
	 *            thes consumer to add
	 */
	public void registerConsumer(final Consumer<? super T> consumer) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						if (Thread.interrupted()) {
							return;
						}
						consumer.consume(buf.remove());
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		});
		registerThread(consumers, consumer, t);
	}
	
	/**
	 * Unregisters and stops everything.
	 */
	public void clear() {
		unregisterAllProducers();
		unregisterAllConsumers();
	}

	/**
	 * Registers the given producer on this server.
	 * 
	 * @param producer
	 */
	public void registerProducer(final Producer<? extends T> producer) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						buf.insert(producer.produce());
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		});
		registerThread(producers, producer, t);
	}

	/**
	 * Unregisters all consumers from this server.
	 */
	public void unregisterAllConsumers() {
		unregisterAll(consumers);
	}

	/**
	 * Unregisters all producers from this server.
	 */
	public void unregisterAllProducers() {
		unregisterAll(producers);
	}

	/**
	 * Unregisters the given consumer from this server.
	 * 
	 * @param consumer
	 */
	public void unregisterConsumer(Consumer<? super T> consumer) {
		unregisterThreads(consumers, consumer);
	}

	/**
	 * Unregisters the given producer from this server.
	 * 
	 * @param producer
	 */
	public void unregisterProducer(Producer<? extends T> producer) {
		unregisterThreads(producers, producer);
	}
}
