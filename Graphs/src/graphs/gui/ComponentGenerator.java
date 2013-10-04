package graphs.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import jgame.ButtonState;
import jgame.GButton;
import jgame.GContainer;
import jgame.GMessage;
import jgame.GObject;
import jgame.GSprite;

public final class ComponentGenerator {
	private ComponentGenerator() {
		throw new UnsupportedOperationException();
	}

	private static final RoundRectangle2D createRectangle(double w, double h) {
		return new RoundRectangle2D.Double(0, 0, w, h, 8, 8);
	}

	public static final GContainer createContainer(double w, double h) {
		class GradientContainer extends GContainer {
			private RoundRectangle2D r2d;

			public GradientContainer(double w, double h) {
				r2d = createRectangle(w, h);
				setSize(w, h);
			}

			@Override
			public void paint(Graphics2D g) {
				g.setPaint(new GradientPaint(0, 0, Color.WHITE, 0,
						getIntHeight(), Color.LIGHT_GRAY));
				g.fill(r2d);
				g.setColor(Color.BLACK);
				g.draw(r2d);
				super.paint(g);
			}

			@Override
			public void preparePaint(Graphics2D g) {
				super.preparePaint(g);
				GObject.antialias(g);
			}

		}
		return new GradientContainer(w, h);
	}

	public static final GButton createButton(double w, double h) {
		class GradientButton extends GButton {
			public GradientButton(double w, double h) {
				RoundRectangle2D r2d = createRectangle(w - 2, h - 2);
				setSize(w, h);

				BufferedImage iNone = new BufferedImage((int) Math.ceil(w),
						(int) Math.ceil(h), BufferedImage.TYPE_INT_ARGB);
				BufferedImage iHover = new BufferedImage((int) Math.ceil(w),
						(int) Math.ceil(h), BufferedImage.TYPE_INT_ARGB);
				BufferedImage iPress = new BufferedImage((int) Math.ceil(w),
						(int) Math.ceil(h), BufferedImage.TYPE_INT_ARGB);

				Graphics2D gNone = (Graphics2D) iNone.getGraphics();
				Graphics2D gHover = (Graphics2D) iHover.getGraphics();
				Graphics2D gPress = (Graphics2D) iPress.getGraphics();

				GObject.antialias(gNone);
				GObject.antialias(gHover);
				GObject.antialias(gPress);

				gNone.setPaint(new GradientPaint(0, 0, Color.WHITE, 0,
						(float) h, new Color(0.9f, 0.9f, 0.9f)));
				gHover.setPaint(new GradientPaint(0, 0, new Color(0.95f, 0.95f,
						0.95f), 0, (float) h, new Color(0.8f, 0.8f, 0.8f)));
				gPress.setPaint(new GradientPaint(0, 0, new Color(0.9f, 0.9f,
						0.9f), 0, (float) h, new Color(0.7f, 0.7f, 0.7f)));

				gNone.fill(r2d);
				gHover.fill(r2d);
				gPress.fill(r2d);

				gNone.setColor(Color.BLACK);
				gHover.setColor(Color.BLACK);
				gPress.setColor(Color.BLACK);

				gNone.draw(r2d);
				gHover.draw(r2d);
				gPress.draw(r2d);

				GSprite sNone = new GSprite(iNone);
				GSprite sHover = new GSprite(iHover);
				GSprite sPress = new GSprite(iPress);

				sNone.setPrimitive(sNone.new ShapePrimitive(r2d));
				sHover.setPrimitive(sHover.new ShapePrimitive(r2d));
				sPress.setPrimitive(sPress.new ShapePrimitive(r2d));

				setStateSprite(ButtonState.NONE, sNone);
				setStateSprite(ButtonState.HOVERED, sHover);
				setStateSprite(ButtonState.PRESSED, sPress);
			}

			@Override
			public void preparePaint(Graphics2D g) {
				super.preparePaint(g);
				GObject.antialias(g);
			}
		}

		return new GradientButton(w, h);
	}

	public static final GButton createButton(double w) {
		return createButton(w, 40);
	}
	
	public static final GButton createButton() {
		return createButton(100);
	}
	
	public static final GButton attachLabel(GButton button, String text) {
		GMessage label = new GMessage(text);
		label.setAlignmentX(0.5);
		label.setFontSize(16f);
		label.setFontStyle(Font.BOLD);
		button.setSize(button.getWidth(), button.getHeight());
		button.setAnchorWeight(0.5, 0.5);
		label.setAlignmentY(0.5);
		button.addAtCenter(label);
		return button;
	}

}
