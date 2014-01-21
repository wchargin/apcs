package edge;

import java.awt.Color;
import java.awt.Graphics;
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
	public static BufferedImage detectEdges(BufferedImage in, float radius,
			float threshold) {
		if (in == null) {
			throw new IllegalArgumentException("input image cannot be null");
		}
		final int width = in.getWidth();
		final int height = in.getHeight();
		BufferedImage out = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		int halfRadius = (int) Math.ceil((radius + 1) / 2);

		Graphics gr = out.getGraphics();

		long time = System.nanoTime();

		float[][] image = new float[width][height];
		int[] rgb = in.getRGB(0, 0, width, height, null, 0, width);
		{
			int z = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int c = rgb[z++] & 0x00FFFFFF;

					int b = c & 0xFF;
					int g = c & 0xFF00 >>> 8;
					int r = c & 0xFF0000 >>> 16;

					image[x][y] = (r + g + b) / 3f / 255f;
				}
			}
		}
		System.out.println(System.nanoTime() - time);

		boolean[][] isEdge = new boolean[width][height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float min = Float.MAX_VALUE, max = Float.MIN_VALUE;
				for (int i = x - halfRadius; i < x + halfRadius; i++) {
					for (int j = y - halfRadius; j < y + halfRadius; j++) {
						if ((i < 0 || i >= width) || (j < 0 || j >= height)) {
							continue;
						}
						float value = image[i][j];
						if (value < min) {
							min = value;
						}
						if (value > max) {
							max = value;
						}
					}
				}
				isEdge[x][y] = (max - min) > threshold;
			}
		}
		System.out.println(System.nanoTime() - time);
		gr.setColor(Color.BLACK);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (isEdge[x][y]) {
					gr.fillRect(x, y, 1, 1);
				}
			}
		}
		System.out.println(System.nanoTime() - time);
		gr.dispose();
		System.out.println();
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
