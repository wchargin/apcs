package apcs.searchsort.search;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apcs.searchsort.ProfilingResult;
import apcs.searchsort.search.SearchProfiler.SearchSettings;

/**
 * A tool to analyze (validate and profile) sorting algorithms.
 * 
 * @author William Chargin
 * 
 */
public class SearchAnalysis {

	/**
	 * The list of sorting algorithms to be tested.
	 */
	private List<SearchAlgorithm> algos;

	/**
	 * Whether the validation should be run.
	 */
	private boolean validate = true;

	/**
	 * Whether the profiling should be run.
	 */
	private boolean profile = true;

	/**
	 * Creates the analysis node with the given algorithms.
	 * 
	 * @param algos
	 *            the algorithms to analyze
	 */
	public SearchAnalysis(List<SearchAlgorithm> algos) {
		this.algos = new ArrayList<>(algos);
	}

	public static void main(String[] args) {
		int runs = parseArgsInt(args, 0, 10);
		int size = parseArgsInt(args, 1, 100_000);
		boolean skipValidate = parseArgsBoolean(args, "-v", "--skip-validation");
		boolean skipProfile = parseArgsBoolean(args, "-p", "--skip-profiling");

		// Grab names from args + reflection
		List<SearchAlgorithm> algorithms = new ArrayList<>();
		if (args.length > 2) {
			for (int i = 2; i < args.length; i++) {
				String name = args[i].trim();
				try {
					Class<?> c = Class.forName(SearchAlgorithm.class
							.getPackage().getName() + '.' + name);
					if (!SearchAlgorithm.class.isAssignableFrom(c)) {
						continue;
					}
					Class<? extends SearchAlgorithm> csa = c
							.asSubclass(SearchAlgorithm.class);
					SearchAlgorithm sa = csa.newInstance();
					algorithms.add(sa);
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException e) {
					continue;
				}
			}
		}

		// fallback if nothing
		if (algorithms.isEmpty()) {
			algorithms.addAll(Arrays.asList(new BinarySearch(),
					new IterativeBinarySearch(), new LinearSearch()));
		}
		SearchAnalysis analyzer = new SearchAnalysis(algorithms);
		analyzer.validate = !skipValidate;
		analyzer.profile = !skipProfile;
		analyzer.analyze(runs, size);
	}

	private static boolean parseArgsBoolean(String[] args, String... target) {
		List<String> arglist = Arrays.<String> asList(args);
		for (String t : target) {
			if (arglist.contains(t)) {
				return true;
			}
		}
		return false;
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
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		System.out.println("List generation complete.");
		System.out.println("Setup complete. Beginning algorithm analysis.");
		System.out.println();
		for (SearchAlgorithm algorithm : algos) {
			final String algorithmName = algorithm.getClass().getSimpleName();
			System.out.println("Beginning analysis of algorithm "
					+ algorithmName + ".");

			if (validate) {
				System.out.println("Performing validation.");
				boolean isValid = SearchValidator.isValid(algorithm,
						new ArrayList<Integer>(list));
				if (!isValid) {
					System.err.println("Validation failed on " + algorithmName
							+ "!");
					continue;
				} else {
					System.out.println("Validation succeeded.");
				}
			}

			if (profile) {
				System.out.println("Profiling.");
				SearchProfiler<Integer> profiler = new SearchProfiler<>();
				SearchSettings<Integer> settings = new SearchSettings<Integer>(
						algorithm, list);
				ProfilingResult result = profiler.profile(settings, runs);
				System.out.println("Profiling complete.");
				System.out.println("Average time: " + result.averageTime
						+ " ns.");
				System.out.println("Standard deviation: " + result.stddev
						+ " ns.");
			}
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
