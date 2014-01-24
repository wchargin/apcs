package edge;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.codecs.h264.H264Encoder;
import org.jcodec.codecs.h264.H264Utils;
import org.jcodec.common.NIOUtils;
import org.jcodec.common.SeekableByteChannel;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.Brand;
import org.jcodec.containers.mp4.MP4Packet;
import org.jcodec.containers.mp4.TrackType;
import org.jcodec.containers.mp4.muxer.FramesMP4MuxerTrack;
import org.jcodec.containers.mp4.muxer.MP4Muxer;
import org.jcodec.scale.AWTUtil;
import org.jcodec.scale.RgbToYuv420;

public class SobelVideo {

	/**
	 * From {@code http://stackoverflow.com/a/14126316}.
	 * 
	 * @author Stanislav Vitvitskyy
	 * 
	 */
	public static class SequenceEncoder {
		private SeekableByteChannel ch;
		private Picture toEncode;
		private RgbToYuv420 transform;
		private H264Encoder encoder;
		private ArrayList<ByteBuffer> spsList;
		private ArrayList<ByteBuffer> ppsList;
		private FramesMP4MuxerTrack outTrack;
		private ByteBuffer _out;
		private int frameNo;
		private MP4Muxer muxer;

		public SequenceEncoder(File out) throws IOException {
			this.ch = NIOUtils.writableFileChannel(out);

			// Transform to convert between RGB and YUV
			transform = new RgbToYuv420(0, 0);

			// Muxer that will store the encoded frames
			muxer = new MP4Muxer(ch, Brand.MP4);

			// Add video track to muxer
			outTrack = muxer.addTrackForCompressed(TrackType.VIDEO, 25);

			// Allocate a buffer big enough to hold output frames
			_out = ByteBuffer.allocate(1920 * 1080 * 6);

			// Create an instance of encoder
			encoder = new H264Encoder();

			// Encoder extra data ( SPS, PPS ) to be stored in a special place
			// of
			// MP4
			spsList = new ArrayList<ByteBuffer>();
			ppsList = new ArrayList<ByteBuffer>();

		}

		public void encodeImage(BufferedImage bi) throws IOException {
			if (toEncode == null) {
				toEncode = Picture.create(bi.getWidth(), bi.getHeight(),
						ColorSpace.YUV420);
			}

			// Perform conversion
			for (int i = 0; i < 3; i++)
				Arrays.fill(toEncode.getData()[i], 0);
			transform.transform(AWTUtil.fromBufferedImage(bi), toEncode);

			// Encode image into H.264 frame, the result is stored in '_out'
			// buffer
			_out.clear();
			ByteBuffer result = encoder.encodeFrame(_out, toEncode);

			// Based on the frame above form correct MP4 packet
			spsList.clear();
			ppsList.clear();
			H264Utils.encodeMOVPacket(result, spsList, ppsList);

			// Add packet to video track
			outTrack.addFrame(new MP4Packet(result, frameNo, 25, 1, frameNo,
					true, null, frameNo, 0));

			frameNo++;
		}

		public void finish() throws IOException {
			// Push saved SPS/PPS to a special storage in MP4
			outTrack.addSampleEntry(H264Utils.createMOVSampleEntry(spsList,
					ppsList));

			// Write MP4 header and finalize recording
			muxer.writeHeader();
			NIOUtils.closeQuietly(ch);
		}

	}

	public static void main(String[] args) throws IOException, JCodecException {
		videoEdgeDetect(new File("H:\\DragonFire.mp4"), false, new File(
				"H:\\EdgeDragon.mp4"));
	}

	public static void videoEdgeDetect(File inFile, final boolean useColor,
			File outFile) throws IOException, JCodecException {
		SeekableByteChannel channel = NIOUtils.readableFileChannel(inFile);
		FrameGrab grab = new FrameGrab(channel);
		grab.seekToFramePrecise(0);

		SequenceEncoder encoder = new SequenceEncoder(outFile);
		try {
			int count = 0;
			while (true) {
				System.out.println(count++);
				BufferedImage im = grab.getFrame();
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
						int rr = (int) (r[i] * 255);
						int gg = (int) (g[i] * 255);
						int bb = (int) (b[i] * 255);
						rgb[i] = (rr) | (gg << 8) | (bb << 16) | (0xFF000000);
					} else {
						int z = (int) (result[i] * 255);
						rgb[i] = (z << 8 | z) << 8 | z | 0xFF000000;
					}
				}
				BufferedImage out = new BufferedImage(w, h, im.getType());
				out.setRGB(0, 0, w, h, rgb, 0, w);

				encoder.encodeImage(out);
			}
		} finally {
			encoder.finish();
		}
	}

}
