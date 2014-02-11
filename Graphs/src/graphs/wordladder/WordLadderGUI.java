package graphs.wordladder;

import graphs.wordladder.LadderGenerator.StringPair;
import graphs.wordladder.dictionary.FileDictionary;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import tools.customizable.FileProperty;
import tools.customizable.PropertyPanel;
import tools.customizable.TextProperty;

public class WordLadderGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The ladder generator used for this
	 */
	private LadderGenerator lg = new LadderGenerator();

	public WordLadderGUI() {
		super("WordLadder");

		JPanel pnlContent = new JPanel(new MigLayout(new LC().flowY()));
		setContentPane(pnlContent);

		final FileProperty fp = new FileProperty("Dictionary file", null);

		final TextProperty t1 = new TextProperty("Origin", "");
		final TextProperty t2 = new TextProperty("Target", "");

		pnlContent.add(
				new PropertyPanel(Arrays.asList(fp, t1, t2), true, false),
				new CC().growX().pushX());

		final JEditorPane txtrResults = new JEditorPane("text/html", "");
		Font oldFont = txtrResults.getFont();
		txtrResults.setFont(new Font(Font.SANS_SERIF, oldFont.getStyle(),
				oldFont.getSize()));
		txtrResults.setEditable(false);
		JScrollPane scpn = new JScrollPane(txtrResults);
		scpn.setMinimumSize(new Dimension(300, 400));
		pnlContent.add(scpn, new CC().grow().push());

		@SuppressWarnings("serial")
		final JButton btnGenerate = new JButton(new AbstractAction(
				"Generate Ladder") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				txtrResults.setText("<p>Working...</p>");
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						List<String> ladder = lg.generateLadder(new StringPair(
								t1.getValue(), t2.getValue()));
						if (ladder == null) {
							txtrResults.setText("<p>No path found.</p>");
						} else {
							StringBuffer sb = new StringBuffer();
							sb.append("<p>Path:</p>");
							sb.append("<ul>");
							for (String s : ladder) {
								sb.append("<li>");
								sb.append(s);
								sb.append("</li>");
							}
							sb.append("</ul>");
							txtrResults.setText(sb.toString());
						}
					}
				});
			}
		});
		pnlContent.add(btnGenerate, new CC().growX().pushX());

		final Runnable rUpdate = new Runnable() {
			@Override
			public void run() {
				btnGenerate.setEnabled(lg.getDictionary() != null);
			}
		};

		fp.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				lg.setDictionary(new FileDictionary(fp.getValue()));
				rUpdate.run();
			}
		});

	}

	public static void main(String[] args) {
		WordLadderGUI gui = new WordLadderGUI();
		gui.pack();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
	}

}
