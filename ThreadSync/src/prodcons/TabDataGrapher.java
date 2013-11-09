package prodcons;

import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * A real-time grapher for tabular data.
 * 
 * @author William Chargin
 * 
 */
public class TabDataGrapher {

	/**
	 * Starts the grapher in a mode designed to work with the output from
	 * {@link BasicProducerConsumer}. This will graph the first column on the
	 * x-axis and the third column on the y-axis, and ignore the first row
	 * (assumed to contain headers). Yes, it skips the second column. Deal with
	 * it.
	 * 
	 * @param args
	 *            none needed; accepts input from stdin
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Realtime TSV Grapher");

		XYSeriesCollection xysc = new XYSeriesCollection();
		final XYSeries series = new XYSeries(0);
		xysc.addSeries(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Buffer size over time", "Time (ms since start)",
				"Buffer size (#)", xysc, PlotOrientation.VERTICAL, false, true,
				false);
		final ChartPanel panel = new ChartPanel(chart);

		frame.setContentPane(panel);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try (Scanner sc = new Scanner(System.in)) {
					// Skip headers
					sc.nextLine();
					String line;
					while ((line = sc.nextLine()) != null) {
						String[] cols = line.split(Pattern.quote("\t"));
						// we want column 0 as X, column 2 as Y
						series.add(Double.parseDouble(cols[0]),
								Double.parseDouble(cols[2]));
					}
				}
			}
		}).start();
	}
}
