package prodcons;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A basic implementation of the producer&ndash;consumer problem.
 * 
 * @author William Chargin
 * 
 */
public class BasicProducerConsumer {

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
		final ApplicationServer<Long> serv = new ApplicationServer<>(
				Integer.parseInt(args[0]));
		for (int i = 0; i < producers; i++) {
			final String name = "Produce" + nfProducer.format(i);
			ApplicationServer.Producer<Long> p = new ApplicationServer.Producer<Long>() {
				@Override
				public Long produce() {
					final long num = value.getAndIncrement();
					System.out.println(System.currentTimeMillis() - start
							+ "\tx" + name + "\t" + serv.size() + "\t" + num);
					try {
						int sleepMillis = (int) ((r.nextInt(range) + min) * Math
								.exp(-bias));
						System.out.println(System.currentTimeMillis() - start
								+ "\ts" + name + "\t" + serv.size() + "\t"
								+ sleepMillis);
						Thread.sleep(sleepMillis);
					} catch (InterruptedException e) {
						System.out.println(System.currentTimeMillis() - start
								+ "\ti" + name + "\t" + serv.size() + "\t-1");

					}
					return num;
				}
			};
			serv.registerProducer(p);
		}

		// Consumer Thread //
		NumberFormat nfConsumer = new DecimalFormat();
		nfConsumer.setMinimumIntegerDigits((int) (consumers > 0 ? Math
				.ceil(Math.log10(consumers)) : 0));
		for (int i = 0; i < consumers; i++) {
			final String name = "Consume" + nfConsumer.format(i);
			ApplicationServer.Consumer<Long> c = new ApplicationServer.Consumer<Long>() {
				@Override
				public void consume(Long l) {
					System.out.println(System.currentTimeMillis() - start
							+ "\tx" + name + "\t" + serv.size() + "\t" + l);
					try {
						int sleepMillis = (int) ((r.nextInt(range) + min) * Math
								.exp(bias));
						System.out.println(System.currentTimeMillis() - start
								+ "\ts" + name + "\t" + serv.size() + "\t"
								+ sleepMillis);
						Thread.sleep(sleepMillis);
					} catch (InterruptedException e) {
						System.out.println(System.currentTimeMillis() + "\ti"
								+ name + "\t" + serv.size() + "\t-1");
					}
				}
			};
			serv.registerConsumer(c);
		}
	}
}
