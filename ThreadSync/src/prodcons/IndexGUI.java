package prodcons;

import java.awt.Dimension;
import java.io.File;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import tools.customizable.MessageProperty;
import tools.customizable.PropertyPanel;
import tools.customizable.PropertySet;
import util.DirectoryProperty;
import util.LAFOptimizer;

public class IndexGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The index for searching.
	 */
	private Index<String, Path> index;

	public IndexGUI() {
		super("Index-Search");
		setLayout(new MigLayout(new LC().flowY()));

		PropertySet props = new PropertySet();

		final DirectoryProperty dpSearch = new DirectoryProperty(
				"Index directory", null);
		props.add(dpSearch);

		final MessageProperty mpIndexSize = new MessageProperty(
				"Search index size", null);
		props.add(mpIndexSize);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mpIndexSize.setValue(index == null ? "No index built" : Integer
						.toString(index.getValueSize()));
			}
		}, 0, 1000);

		add(new PropertyPanel(props, true, false), new CC().growX().pushX());

		final JButton btnIndex = new JButton(" ");
		btnIndex.setEnabled(false);
		dpSearch.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				File value = dpSearch.getValue();
				if (value != null) {
					btnIndex.setEnabled(true);
					btnIndex.setText(index == null ? "Build Index"
							: "Destroy Index");
				} else {
					btnIndex.setEnabled(false);
					btnIndex.setText(" ");
				}
			}
		});
		add(btnIndex, new CC().growX().pushX());

		setMinimumSize(new Dimension(600, 400));
	}

	public static void main(String[] args) {
		LAFOptimizer.optimizeSwing();
		IndexGUI gui = new IndexGUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.pack();
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
	}
}
