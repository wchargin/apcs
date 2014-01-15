package edge;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

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

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float[] min = null, max = null;
				for (int i = x - halfRadius; i < x + halfRadius; i++) {
					for (int j = y - halfRadius; j < y + halfRadius; j++) {
						if ((i < 0 || i >= width) || (j < 0 || j >= height)) {
							continue;
						}
						float[] components = new Color(in.getRGB(i, j))
								.getColorComponents(null);
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
}
