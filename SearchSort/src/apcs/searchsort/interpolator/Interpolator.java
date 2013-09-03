package apcs.searchsort.interpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import apcs.searchsort.ProfilingResult;
import apcs.searchsort.sort.MedianQuickSort;
import apcs.searchsort.sort.SortingAlgorithm;
import apcs.searchsort.sort.SortingProfiler;
import apcs.searchsort.sort.SortingProfiler.ListFiller;
import apcs.searchsort.sort.SortingProfiler.SortingSettings;

public class Interpolator {

	public static InterpolationResult interpolate(
			Map<? extends Number, ? extends Number> data) {
		ComplexityClass[] classes = ComplexityClass.values();
		double lowestVariance = Double.MAX_VALUE;
		ComplexityClass lowestClass = null;
		double k = 1;
		double each = 1d / data.size();

		for (int i = 0; i < classes.length; i++) {
			ComplexityClass cc = classes[i];
			double variance = 0;
			double[] coefficients = new double[data.size()];
			double averageCoefficient = 0;
			{
				int j = 0;
				for (Entry<? extends Number, ? extends Number> e : data
						.entrySet()) {
					double evaluated = cc.evaluate(e.getKey().doubleValue());
					double ratio = e.getValue().doubleValue() / evaluated;
					System.out.println(ratio);
					averageCoefficient += each * ratio;
					coefficients[j] = ratio;
					j++;
				}
			}
			for (int j = 0; j < coefficients.length; j++) {
				variance += each
						* Math.pow(coefficients[j] - averageCoefficient, 2);
			}

			if (variance < lowestVariance) {
				lowestVariance = variance;
				lowestClass = cc;
				k = averageCoefficient;
			}
			System.out.println(new InterpolationResult(cc, averageCoefficient,
					variance));
		}

		return new InterpolationResult(lowestClass, k, lowestVariance);
	}

	public static class InterpolationResult {
		public final ComplexityClass complexity;
		public final double k;
		public final double variance;

		public InterpolationResult(ComplexityClass complexity, double k,
				double variance) {
			super();
			this.complexity = complexity;
			this.k = k;
			this.variance = variance;
		}

		@Override
		public String toString() {
			return "InterpolationResult [complexity=" + complexity + ", k=" + k
					+ ", variance=" + variance + "]";
		}
	}

	/**
	 * A set of complexity classes. Exponential and logarithmic functions assume
	 * a base of two.
	 * 
	 * @author William Chargin
	 * 
	 */
	public enum ComplexityClass {
		CONSTANT, LOGARITHMIC, LINEAR, LINEARITHMIC, QUADRATIC, CUBIC;
		private static final double LOG_2 = Math.log(2);

		public double evaluate(double value) {
			switch (this) {
			case CONSTANT:
				return 1;
			case CUBIC:
				return Math.pow(value, 3);
			case LINEAR:
				return value;
			case LINEARITHMIC:
				return value * Math.log(value) / LOG_2;
			case LOGARITHMIC:
				return Math.log(value) / LOG_2;
			case QUADRATIC:
				return Math.pow(value, 2);
			default:
				return value;
			}
		}

	}

	public static void main(String[] args) {
		final SortingAlgorithm sa = new MedianQuickSort();

		final ListFiller<Integer> lf = new ListFiller<Integer>() {
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

		int[] size = { 100, 200, 400, 800, 1600, 3200, 6400, 12800 };
		Map<Integer, Double> data = new HashMap<>();

		SortingProfiler<Integer> sp = new SortingProfiler<Integer>();
		for (int i = 0; i < size.length; i++) {
			SortingSettings<Integer> ss = new SortingSettings<>(sa, lf, size[i]);
			ProfilingResult pr = sp.profile(ss, 500);
			data.put(size[i], pr.averageTime);
			System.out.println(pr.averageTime);
		}

		System.out.println(Interpolator.interpolate(data));
	}

}
