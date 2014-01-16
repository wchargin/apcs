package edge;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

/**
 * A GUI wrapper for the {@link EdgeDetection} algorithms.
 * 
 * @author William Chargin
 * 
 */
public class EdgeDetectionGUI extends JFrame {

	/**
	 * A component that allows two images to be overlayed on top of each other.
	 * 
	 * @author William Chargin
	 * 
	 */
	public class JDoubleImageViewer extends JComponent {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(original, 0, 0, getWidth(), getHeight(), null);
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(processed, 0, 0, getWidth(), getHeight(), null);
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a label for a control widget.
	 * 
	 * @param string
	 *            the label text
	 * @return the formatted label
	 */
	private static JLabel createLabel(String string) {
		JLabel l = new JLabel(string);
		l.setHorizontalAlignment(JLabel.TRAILING);
		return l;
	}

	/**
	 * Starts the GUI application.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		JFrame gui = new EdgeDetectionGUI();
		gui.pack();
		gui.setLocationRelativeTo(null);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
	}

	/**
	 * The original, pre-detected image.
	 */
	private BufferedImage original;

	/**
	 * The pre-blurred, edge-detected image.
	 */
	private BufferedImage edgeDetected;

	/**
	 * The processed image.
	 */
	private BufferedImage processed;

	/**
	 * The alpha of the image overlay: {@code 0f} is full original image, while
	 * {@code 1f} is full processed image.
	 */
	private float alpha = 1f;

	/**
	 * The view displaying the image.
	 */
	private JDoubleImageViewer lblView;

	@SuppressWarnings("serial")
	public EdgeDetectionGUI() {
		super("Edge Detection");

		JPanel pnlContent = new JPanel(new MigLayout(new LC().flowY()));
		setContentPane(pnlContent);

		lblView = new JDoubleImageViewer();
		lblView.setPreferredSize(new Dimension(400, 300));
		add(lblView, new CC().grow().push());

		pnlContent.add(new JButton(new AbstractAction("Load Image") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter(
						"Image files", ImageIO.getReaderFileSuffixes()));
				chooser.setMultiSelectionEnabled(false);
				int ret = chooser.showOpenDialog(EdgeDetectionGUI.this);
				if (ret != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File f = chooser.getSelectedFile();
				try {
					BufferedImage image = ImageIO.read(f);
					original = image;
					processed = image;
					lblView.repaint();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(EdgeDetectionGUI.this,
							e.getMessage(), "Unexpected Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}), new CC().growX().pushX());

		JPanel pnlControls = new JPanel(new MigLayout());
		pnlContent.add(pnlControls, new CC().growX().pushX());

		final JSlider sldAlpha = new JSlider(0, 255, 255);
		sldAlpha.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				alpha = sldAlpha.getValue() / 255f;
				lblView.repaint();
			}
		});
		pnlControls.add(createLabel("Mixing alpha"), new CC().growX());
		pnlControls.add(sldAlpha, new CC().growX().pushX().wrap());

		final JSlider sldPreblur = new JSlider(0, 50, 2);
		pnlControls.add(createLabel("Pre-blur radius"), new CC().growX());
		pnlControls.add(sldPreblur, new CC().growX().pushX().wrap());

		final JSlider sldThreshold = new JSlider(0, 255, 60);
		pnlControls.add(createLabel("Edge detection threshold"),
				new CC().growX());
		pnlControls.add(sldThreshold, new CC().growX().pushX().wrap());

		final JSlider sldPostblur = new JSlider(0, 50, 0);
		pnlControls.add(createLabel("Post-blur radius"), new CC().growX());
		pnlControls.add(sldPostblur, new CC().growX().pushX().wrap());

		final ChangeListener clUpdate = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				edgeDetected = EdgeDetection.gaussianBlur(original,
						sldPreblur.getValue());
				edgeDetected = EdgeDetection.detectEdges(edgeDetected, 1,
						(float) Math.pow(sldThreshold.getValue() / 255f, 2));
				processed = EdgeDetection.gaussianBlur(edgeDetected,
						sldPostblur.getValue());
				lblView.repaint();
			}
		};
		sldPreblur.addChangeListener(clUpdate);
		sldThreshold.addChangeListener(clUpdate);
		sldPostblur.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (edgeDetected == null) {
					clUpdate.stateChanged(e);
				}
				processed = EdgeDetection.gaussianBlur(edgeDetected,
						sldPostblur.getValue());
				lblView.repaint();
			}
		});

		pnlContent.add(new JButton(new AbstractAction("Reset Transformations") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				processed = original;
				lblView.repaint();
			}
		}), new CC().growX().pushX());
	}
}
