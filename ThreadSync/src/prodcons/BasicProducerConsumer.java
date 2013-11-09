package prodcons;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A basic implementation of the producer&ndash;consumer problem.
 * 
 * @author William Chargin
 * 
 */
public class BasicProducerConsumer {

	/**
	 * A buffer, the shared memory accessed by producers and consumers.
	 * 
	 * @author William Chargin
	 * 
	 * @param <T>
	 *            the type of object stored in this buffer
	 */
	private static class Buffer<T> {
		Queue<T> buffer;
		Semaphore mutex;
		Semaphore slots;
		Semaphore items;

		public Buffer(int size) {
			buffer = new LinkedList<T>();
			mutex = new Semaphore(1);
			slots = new Semaphore(size);
			items = new Semaphore(0);
		}

		public void insert(T item) throws InterruptedException {
			slots.acquire();
			mutex.acquire();
			buffer.add(item);
			mutex.release();
			items.release();
		}

		public T remove() throws InterruptedException {
			items.acquire();
			mutex.acquire();
			T t = buffer.poll();
			mutex.release();
			slots.release();
			return t;
		}

		public int size() {
			return buffer.size();
		}
	}

	private interface Consumer<T> {
		public void consume(T t);
	}

	private interface Producer<T> {
		public T produce();
	}

	/**
	 * Starts the producer-consumer demo with the given parameters, and prints
	 * tab-separated output to stdout. Columns are:
	 * <ul>
	 * <li><strong>Time</strong> in milliseconds since start</li>
	 * <li><strong>Operation</strong> with prefix {@code x} execute, {@code s}
	 * sleep, or {@code i} interrupted, followed by the relevant thread name</li>
	 * <li><strong>Buffer</strong> size</li>
	 * <li><strong>Value</strong> produced or consumed, or the time to sleep</li>
	 * </ul>
	 * 
	 * @param args
	 *            four required, two optional: (0) integer, size of buffer; (1)
	 *            double, operational bias (positive for frequent production,
	 *            negative for frequent consumption); (2) integer, minimum sleep
	 *            time (milliseconds); (3) integer, range of sleep times above
	 *            minimum (milliseconds); [4] integer, number of producer
	 *            threads to use; [5] integer, number of consumer threads to use
	 */
	public static void main(String[] args) {
		if (args.length < 4 || args.length == 5) {
			System.err
					.println("Arguments: <bufsize> <bias> <minms> <rangems> [<producers> <consumers>]");
			System.err
					.println("  <bufsize>\n\tnon-negative integer; maximum size of buffer");
			System.err
					.println("  <bias>\n\treal number; for production (positive) or consumption (negative)");
			System.err
					.println("  <minms>\n\tnon-negative integer; minimum milliseconds to sleep (before bias)");
			System.err
					.println("  <rangems>\n\tnon-negative integer; range more than <minms> to sleep (before bias)");
			System.err
					.println("  [ <producers>  \n\tnon-negative integer; number of producer threads to use");
			System.err
					.println("    <consumers> ]\n\tnon-negative integer; number of consumer threads to use");
			System.exit(1);
		}
		final Buffer<Long> buf = new Buffer<>(Integer.parseInt(args[0]));
		final Random r = new Random();

		final int min = Integer.parseInt(args[2]), range = Integer
				.parseInt(args[3]);
		final double bias = Double.parseDouble(args[1]);

		final long start = System.currentTimeMillis();
		System.out.println("Time\tOperation\tBuffer\tValue");

		int producers = args.length < 6 ? 1 : Integer.parseInt(args[4]);
		int consumers = args.length < 6 ? 1 : Integer.parseInt(args[5]);

		/** the value to add to the queue */
		final AtomicLong value = new AtomicLong(0);

		// Producer Thread //
		NumberFormat nfProducer = new DecimalFormat();
		nfProducer.setMinimumIntegerDigits((int) (producers > 0 ? Math
				.ceil(Math.log10(producers)) : 0));
		for (int i = 0; i < producers; i++) {
			final String name = "Produce" + nfProducer.format(i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Producer<Long> p = new Producer<Long>() {
						@Override
						public Long produce() {
							final long num = value.getAndIncrement();
							System.out.println(System.currentTimeMillis()
									- start + "\tx" + name + "\t" + buf.size()
									+ "\t" + num);
							return num;
						}
					};
					try {
						while (true) {
							buf.insert(p.produce());
							int sleepMillis = (int) ((r.nextInt(range) + min) * Math.exp(-bias));
							System.out.println(System.currentTimeMillis()
									- start + "\ts" + name + "\t" + buf.size()
									+ "\t" + sleepMillis);
							Thread.sleep(sleepMillis);
						}
					} catch (InterruptedException e) {
						System.out.println(System.currentTimeMillis() - start
								+ "\ti" + name + "\t" + buf.size() + "\t-1");
					}
				}
			}, name).start();
		}

		// Consumer Thread //
		NumberFormat nfConsumer = new DecimalFormat();
		nfConsumer.setMinimumIntegerDigits((int) (consumers > 0 ? Math
				.ceil(Math.log10(consumers)) : 0));
		for (int i = 0; i < consumers; i++) {
			final String name = "Consume" + nfConsumer.format(i);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Consumer<Long> c = new Consumer<Long>() {
						@Override
						public void consume(Long l) {
							System.out.println(System.currentTimeMillis()
									- start + "\tx" + name + "\t" + buf.size()
									+ "\t" + l);
						}

					};
					try {
						while (true) {
							c.consume(buf.remove());
							int sleepMillis = (int) ((r.nextInt(range) + min) * Math.exp(bias));
							System.out.println(System.currentTimeMillis()
									- start + "\ts" + name + "\t" + buf.size()
									+ "\t" + sleepMillis);
							Thread.sleep(sleepMillis);
						}
					} catch (InterruptedException e) {
						System.out.println(System.currentTimeMillis() + "\ti"
								+ name + "\t" + buf.size() + "\t-1");
					}
				}
			}, name).start();
		}
	}
}
