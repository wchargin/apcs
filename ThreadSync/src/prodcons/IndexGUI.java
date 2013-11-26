package prodcons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import tools.customizable.MessageProperty;
import tools.customizable.PropertyPanel;
import tools.customizable.PropertySet;
import tools.customizable.TextProperty;
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

	/**
	 * The list model.
	 */
	private DefaultListModel<Path> model = new DefaultListModel<>();

	/**
	 * The server.
	 */
	private ApplicationServer<Path> server = new ApplicationServer<>(1024 * 32);

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

		final TextProperty tpSearchQuery = new TextProperty("Search query",
				null);
		props.add(tpSearchQuery);
		tpSearchQuery.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				if (index != null) {
					String[] words = tpSearchQuery.getValue().split("\\W");
					if (words.length == 0) {
						return;
					}
					Set<Path> result = index.get(words[0].toLowerCase());
					Set<Path> matches = null;
					if (result == null) {
						return;
					}
					matches = new HashSet<>(result);
					for (int i = 1; i < words.length; i++) {
						result = index.get(words[i].toLowerCase());
						if (result == null) {
							matches.clear();
							break;
						}
						matches.retainAll(result);
					}
					model.clear();
					for (Path p : matches) {
						model.addElement(p);
					}
				}
			}
		});

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

		btnIndex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (index != null) {
					server.clear();
					index = null;
					model.clear();
				} else {
					index = new Index<>();
					server.registerProducer(new FileTreeProducer(dpSearch
							.getValue().toPath()));
					for (int i = 0; i < 1024; i++) {
						server.registerConsumer(new FileIndexingConsumer(index,
								Charset.forName("UTF-8"), "html"));
					}
				}
			}
		});

		JList<Path> list = new JList<>(model);
		JScrollPane pane = new JScrollPane(list);

		add(pane, new CC().grow().push());

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
