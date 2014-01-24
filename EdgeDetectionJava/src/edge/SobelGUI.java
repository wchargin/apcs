package edge;

import java.awt.AlphaComposite;
import java.awt.Color;
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
import javax.swing.JCheckBox;
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
 * A GUI wrapper for the {@link SobelDetection} algorithms.
 * 
 * @author William Chargin
 * 
 */
public class SobelGUI extends JFrame {

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
		JFrame gui = new SobelGUI();
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
	public SobelGUI() {
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
				int ret = chooser.showOpenDialog(SobelGUI.this);
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
					JOptionPane.showMessageDialog(SobelGUI.this,
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

		final JCheckBox chkbxColor = new JCheckBox();
		pnlControls.add(createLabel("Use color"), new CC().growX());
		pnlControls.add(chkbxColor, new CC().growX().pushX().wrap());

		final JButton btnPerform = new JButton(new AbstractAction(
				"Run Sobel algorithm") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				processed = doEdgeDetection(original, chkbxColor.isSelected());
				lblView.repaint();
			}
		});
		pnlControls
				.add(createLabel("Perform edge detection"), new CC().growX());
		pnlControls.add(btnPerform, new CC().growX().pushX().wrap());
	}

	protected BufferedImage doEdgeDetection(BufferedImage im,
			final boolean useColor) {
		final int w = im.getWidth(), h = im.getHeight();

		int[] rgb = new int[im.getWidth() * im.getHeight()];
		im.getRGB(0, 0, w, h, rgb, 0, w);
		float[] vals = new float[rgb.length];

		float[] r = new float[rgb.length], g = new float[rgb.length], b = new float[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			int z = rgb[i];
			final int rr = z & 0xFF, gg = z >> 8 & 0xFF, bb = z >> 16 & 0xFF;
			if (useColor) {
				r[i] = rr;
				g[i] = gg;
				b[i] = bb;
			} else {
				vals[i] = (rr + gg + bb) / (255f * 3f);
			}
		}
		float[] result = null;
		if (useColor) {
			r = SobelDetection.sobel(w, h, r);
			g = SobelDetection.sobel(w, h, g);
			b = SobelDetection.sobel(w, h, b);
		} else {
			result = SobelDetection.sobel(w, h, vals);
		}
		for (int i = 0; i < rgb.length; i++) {
			if (useColor) {
				rgb[i] = new Color(r[i], g[i], b[i]).getRGB();
			} else {
				float z = result[i];
				rgb[i] = new Color(z, z, z).getRGB();
			}
		}
		BufferedImage out = new BufferedImage(w, h, im.getType());
		out.setRGB(0, 0, w, h, rgb, 0, w);
		return out;
	}
}
