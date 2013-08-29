package apcs.searchsort.sort;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import apcs.searchsort.ProfilingResult;
import apcs.searchsort.sort.SortingProfiler.ListFiller;
import apcs.searchsort.sort.SortingProfiler.SortingSettings;

/**
 * A tool to analyze (validate and profile) sorting algorithms.
 * 
 * @author William Chargin
 * 
 */
public class SortingAnalysis {

	/**
	 * The list of sorting algorithms to be tested.
	 */
	private List<SortingAlgorithm> algos;

	/**
	 * Creates the analysis node with the given algorithms.
	 * 
	 * @param algos
	 *            the algorithms to analyze
	 */
	public SortingAnalysis(List<SortingAlgorithm> algos) {
		this.algos = new ArrayList<>(algos);
	}

	public static void main(String[] args) {
		int runs = parseArgsInt(args, 0, 10);
		int size = parseArgsInt(args, 1, 1_00_000);

		// Grab names from args + reflection
		List<SortingAlgorithm> algorithms = new ArrayList<>();
		if (args.length > 2) {
			for (int i = 2; i < args.length; i++) {
				String name = args[i].trim();
				try {
					Class<?> c = Class.forName(SortingAlgorithm.class
							.getPackage().getName() + '.' + name);
					if (!SortingAlgorithm.class.isAssignableFrom(c)) {
						continue;
					}
					Class<? extends SortingAlgorithm> csa = c
							.asSubclass(SortingAlgorithm.class);
					for (SortingAlgorithm sa : algorithms) {
						if (sa.getClass().equals(csa)) {
							// Duplicate.
							continue;
						}
					}
					SortingAlgorithm sa = csa.newInstance();
					algorithms.add(sa);
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException e) {
					continue;
				}
			}
		}

		// fallback if nothing
		if (algorithms.isEmpty()) {
			algorithms.addAll(Arrays.asList(new MergeSort(),
					new SelectionSort(), new InsertionSort()));
		}
		new SortingAnalysis(algorithms).analyze(runs, size);
	}

	/**
	 * Analyzes the stored algorithms with the given runs and list size.
	 * 
	 * @param runs
	 *            the number of runs per algorithm (to balance randomness)
	 * @param size
	 *            the list size to analyze with
	 */
	private void analyze(int runs, int size) {
		NumberFormat nf = NumberFormat.getIntegerInstance();
		System.out.println("Setting up analysis.");

		System.out.println("Generating list of " + nf.format(size)
				+ " integers.");
		List<Integer> list = new ArrayList<>(size);
		Random r = new Random();
		for (int i = 0; i < size; i++) {
			// Get a random integer from minimum to maximum
			// Play with bits to get this to work
			int random = (r.nextInt(Integer.MAX_VALUE)) << 1 + r.nextInt(1);
			list.add(random);
		}
		System.out.println("List generation complete.");
		System.out.println("Creating list filler.");
		ListFiller<Integer> filler = new ListFiller<Integer>() {
			@Override
			public void fillList(List<Integer> listToFill, int startIndex,
					int stopIndex) {
				Random r = new Random();
				for (int i = startIndex; i < stopIndex; i++) {
					int random = (r.nextInt(Integer.MAX_VALUE)) << 1 + r
							.nextInt(1);
					if (listToFill.size() > i) {
						listToFill.set(i, random);
					} else {
						listToFill.add(random);
					}
				}
			}
		};
		System.out.println("Setup complete. Beginning algorithm analysis.");
		System.out.println();
		for (SortingAlgorithm algorithm : algos) {
			final String algorithmName = algorithm.getClass().getSimpleName();
			System.out.println("Beginning analysis of algorithm "
					+ algorithmName + ".");
			System.out.println("Performing validation.");
			boolean isValid = SortingValidator.isValid(algorithm,
					new ArrayList<Integer>(list));
			if (!isValid) {
				System.err.println("Validation failed on " + algorithmName
						+ "!");
				continue;
			} else {
				System.out.println("Validation succeeded.");
			}

			System.out.println("Profiling.");
			SortingProfiler<Integer> profiler = new SortingProfiler<>();
			SortingSettings<Integer> settings = new SortingSettings<>(
					algorithm, filler, size);
			ProfilingResult result = profiler.profile(settings, runs);
			System.out.println("Profiling complete.");
			System.out.println("Average time: " + result.averageTime + " ns.");
			System.out.println("Standard deviation: " + result.stddev + " ns.");
			System.out.println();
		}

		System.out.println("Analysis complete.");
	}

	/**
	 * Parses an argument from the arguments list.
	 * 
	 * @param args
	 *            the arguments list
	 * @param index
	 *            the index of the argument in the list
	 * @param defaultValue
	 *            the defaultvalue
	 * @return the argument, if it is successfully parsed, or the default value
	 *         if (a) it is the string {@code "-"}, (b) it contains a non-digit
	 *         character other than {@code _}, or (c) the argument list's length
	 *         is greater than or equal to the index of the argument (i.e., the
	 *         argument does not exist in the list)
	 */
	private static int parseArgsInt(String[] args, int index, int defaultValue) {
		if (args.length <= index) {
			return defaultValue;
		}

		String text = args[index]
				.replace((CharSequence) "_", (CharSequence) "").trim();
		if (text.equals("-")) {
			// Use default
			return defaultValue;
		}
		if (text.matches("^[0-9]")) {
			return defaultValue;
		}
		return Integer.parseInt(text);
	}

}
