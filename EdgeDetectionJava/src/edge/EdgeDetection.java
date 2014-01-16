package edge;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Utility class for edge detection algorithms.
 * 
 * @author William Chargin
 * 
 */
public class EdgeDetection {

	/**
	 * Detects edges in an image and returns a new image with the edges drawn.
	 * 
	 * @param in
	 *            the input image
	 * @param radius
	 *            the radius for searching
	 * @param threshold
	 *            the threshold of difference
	 * @return the edge image
	 */
	public static BufferedImage detectEdges(BufferedImage in, int radius,
			float threshold) {
		if (in == null) {
			throw new IllegalArgumentException("input image cannot be null");
		}
		final int width = in.getWidth();
		final int height = in.getHeight();
		BufferedImage out = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		int halfRadius = (radius + 1) / 2;

		Graphics g = out.getGraphics();

		float[][][] image = new float[width][height][];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				image[x][y] = new Color(in.getRGB(x, y))
						.getColorComponents(null);
			}
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float[] min = null, max = null;
				for (int i = x - halfRadius; i < x + halfRadius; i++) {
					for (int j = y - halfRadius; j < y + halfRadius; j++) {
						if ((i < 0 || i >= width) || (j < 0 || j >= height)) {
							continue;
						}
						float[] components = image[i][j];
						if (min == null) {
							min = components.clone();
						}
						if (max == null) {
							max = components.clone();
						}
						for (int c = 0; c < components.length; c++) {
							min[c] = Math.min(min[c], components[c]);
							max[c] = Math.max(max[c], components[c]);
						}
					}
				}
				if (min.length != max.length) {
					throw new AssertionError(
							"component arrays min and max have unequal length");
				}
				float[] color = new float[min.length];
				for (int c = 0; c < min.length; c++) {
					boolean isEdge = (max[c] - min[c] > threshold);
					color[c] = isEdge ? 1 : 0;
				}
				Color paint = new Color(
						ColorSpace.getInstance(ColorSpace.CS_sRGB), color, 1);
				g.setColor(paint);
				g.fillRect(x, y, 1, 1);
			}
		}
		g.dispose();
		return out;
	}

	/**
	 * Performs a Gaussian blur of the given radius.
	 * 
	 * @param in
	 *            the image to blur
	 * @param radius
	 *            the radius for the blur
	 * @return the blurred image
	 */
	public static BufferedImage gaussianBlur(BufferedImage in, int radius) {
		if (in == null) {
			throw new IllegalArgumentException("input image cannot be null");
		}
		if (radius <= 0) {
			return in;
		}

		int rt2p1 = radius * 2 + 1;

		// Generate a convolution matrix
		float[] convolution = new float[rt2p1];

		// Create a 1D gaussian
		// Initial population
		float sum = 0;
		float sigma = radius / 2.5f; // seems to work nicely
		float sigmasq = (float) (Math.pow(sigma, 2));
		float fac = (float) (1 / (Math.sqrt(2 * Math.PI)) * sigma);

		for (int i = 0; i < convolution.length; i++) {
			double radiusq = Math.pow(radius - i, 2);
			double value = fac * Math.exp(-radiusq / (2 * sigmasq));
			sum += (convolution[i] = (float) value);
		}

		// Normalize to have sum of 1.0
		float scale = 1f / sum;
		for (int i = 0; i < convolution.length; i++) {
			convolution[i] *= scale;
		}

		Kernel hor = new Kernel(rt2p1, 1, convolution);
		Kernel ver = new Kernel(1, rt2p1, convolution);
		ConvolveOp ohor = new ConvolveOp(hor, ConvolveOp.EDGE_NO_OP, null);
		ConvolveOp over = new ConvolveOp(ver, ConvolveOp.EDGE_NO_OP, null);

		return ohor.filter(over.filter(in, null), null);
	}
}
