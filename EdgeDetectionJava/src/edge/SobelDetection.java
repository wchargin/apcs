package edge;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class SobelDetection {

	public static float[] sobel(int width, int height, float[] vals) {
		// each axis: do two convolutions instead of one to get O(n) not O(n^2)
		float[] k1 = { 1, 2, 1 };
		float[] k2 = { 1, 0, -1 };
		// Sobel G_x = (vertical k1) * (horizontal k2 * A)
		// Sobel G_y = (vertical k2) * (horizontal k1 * A)

		float[] xmag = new float[vals.length];
		float[] ymag = new float[vals.length];
		float[] dum = new float[vals.length];

		convolve(width, height, vals, 3, k2, dum); // G_x step 1
		convolve(width, height, dum, 1, k1, xmag); // G_x step 2

		convolve(width, height, vals, 3, k1, dum); // G_y step 1
		convolve(width, height, dum, 1, k2, ymag); // G_y step 2

		float[] ret = new float[vals.length];
		float max = 0;
		for (int i = 0; i < ret.length; i++) {
			final float xx = xmag[i];
			final float yy = ymag[i];
			final float value = (float) Math.sqrt(Math.pow(xx, 2)
					+ Math.pow(yy, 2));
			ret[i] = value;
			if (value > max) {
				max = value;
			}
		}
		for (int i = 0; i < ret.length; i++) {
			ret[i] /= max;
		}
		return ret;
	}

	public static void main(String[] args) throws Exception {
		final boolean USE_COLOR = false;

		BufferedImage im = ImageIO.read(new File("H:\\stab.jpg"));
		final int w = im.getWidth(), h = im.getHeight();

		int[] rgb = new int[im.getWidth() * im.getHeight()];
		im.getRGB(0, 0, w, h, rgb, 0, w);
		float[] vals = new float[rgb.length];

		float[] r = new float[rgb.length], g = new float[rgb.length], b = new float[rgb.length];
		for (int i = 0; i < rgb.length; i++) {
			int z = rgb[i];
			final int rr = z & 0xFF, gg = z >> 8 & 0xFF, bb = z >> 16 & 0xFF;
			if (USE_COLOR) {
				r[i] = rr;
				g[i] = gg;
				b[i] = bb;
			} else {
				vals[i] = (rr + gg + bb) / (255f * 3f);
			}
		}
		float[] result;
		if (USE_COLOR) {
			r = sobel(w, h, r);
			g = sobel(w, h, g);
			b = sobel(w, h, b);
		} else {
			result = sobel(w, h, vals);
		}
		for (int i = 0; i < rgb.length; i++) {
			if (USE_COLOR) {
				rgb[i] = new Color(r[i], g[i], b[i]).getRGB();
			} else {
				float z = result[i];
				rgb[i] = new Color(z, z, z).getRGB();
			}
		}
		BufferedImage out = new BufferedImage(w, h, im.getType());
		out.setRGB(0, 0, w, h, rgb, 0, w);
		JOptionPane.showMessageDialog(null, new javax.swing.JLabel(
				new javax.swing.ImageIcon(out)));
	}

	/**
	 * Runs a convolution, of course.
	 * 
	 * @param wi
	 *            width of image
	 * @param hi
	 *            height of image
	 * @param vals
	 *            function values
	 * @param wk
	 *            width of kernel
	 * @param kernel
	 *            kernel contents
	 * @param out
	 *            output array
	 */
	private static void convolve(int wi, int hi, float[] vals, int wk,
			float[] kernel, float[] out) {
		final int lk = kernel.length;
		final int hk = lk / wk;
		final int offh = -(wk - 1) / 2;
		final int offv = -(hk - 1) / 2;
		for (int i = 0; i < wi; i++) {
			for (int j = 0; j < hi; j++) {
				// calculate convolution of pixel (i, j)
				int u = 0, v = 0;
				out[i + j * wi] = 0;
				for (int t = 0; t < kernel.length; t++) {
					float kval = kernel[u + v * wk]; // kernel value

					// calculate x and y values
					int x = i + u + offh;
					int y = (j + v + offv);
					// prevent underflow
					while (x < 0)
						x += wi;
					while (y < 0)
						y += hi;
					// prevent overflow
					x %= wi;
					y %= hi;
					// calculate index from x, y
					int z = x + y * wi;

					// get input and compute output
					float vval = vals[z];
					out[i + j * wi] += vval * kval;
					if (++u >= wk) {
						u = 0;
						v++;
					}
				}
			}
		}
	}
}
