package graphs.gui;

import graphs.gui.GraphsGUI.GraphSettings;
import graphs.gui.GraphsGUI.Views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import jgame.ButtonState;
import jgame.Context;
import jgame.GButton;
import jgame.GContainer;
import jgame.GMessage;
import jgame.GObject;
import jgame.GSprite;
import jgame.listener.ButtonListener;

public class SettingsView extends GContainer {

	/**
	 * The settings object controlled by this view.
	 */
	private GraphSettings settings;

	public SettingsView(GraphSettings gs) {
		setSize(800, 600);

		settings = gs;

		GObject cplTextSize = ComponentGenerator.createContainer(200, 65);
		GMessage lblTextSize = createLabel("Text size");
		final GMessage valTextSize = createValue(Integer
				.toString((int) settings.textSize));
		GButton btrTextSize = createDirectionButton(true);
		GButton btlTextSize = createDirectionButton(false);
		class TextAdjustListener extends ButtonListener {

			public final boolean increase;

			public TextAdjustListener(boolean increase) {
				super();
				this.increase = increase;
			}

			@Override
			public void mouseClicked(Context context) {
				settings.textSize = Math.max(4, settings.textSize
						+ (increase ? 1 : -1));
				valTextSize.setText(Integer.toString((int) settings.textSize));
			}

		}
		btrTextSize.addListener(new TextAdjustListener(true));
		btlTextSize.addListener(new TextAdjustListener(false));
		lblTextSize.setSize(200, 50);
		valTextSize.setSize(200, 25);
		cplTextSize.addAt(lblTextSize, 100, 40);
		cplTextSize.addAt(valTextSize, 100, 55);
		cplTextSize.addAt(btrTextSize, 122, 45);
		cplTextSize.addAt(btlTextSize, 72, 45);
		addAt(cplTextSize, 125, 58);

		GButton btnReturn = ComponentGenerator.attachLabel(
				ComponentGenerator.createButton(), "Return");
		btnReturn.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				context.setCurrentGameView(Views.GRAPHS);
			}
		});
		addAt(btnReturn, 400, 500);

		GObject cplDistanceWeight = ComponentGenerator.createContainer(200, 65);
		addAt(cplDistanceWeight, 350, 58);
		GMessage lblDistanceWeight = createLabel("Distance weight");
		lblDistanceWeight.setSize(200, 50);
		cplDistanceWeight.addAt(lblDistanceWeight, 100, 40);
		GButton btnDistanceWeight = ComponentGenerator.attachLabel(
				ComponentGenerator.createButton(100, 30), "Toggle");
		cplDistanceWeight.addAt(btnDistanceWeight, 100, 48);
		btnDistanceWeight.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				settings.useDistanceWeight = !settings.useDistanceWeight;
			}
		});
	}

	private GButton createDirectionButton(boolean right) {
		BufferedImage iNone = new BufferedImage(24, 32,
				BufferedImage.TYPE_INT_ARGB);
		BufferedImage iHover = new BufferedImage(24, 32,
				BufferedImage.TYPE_INT_ARGB);
		BufferedImage iPress = new BufferedImage(24, 32,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D gNone = (Graphics2D) iNone.getGraphics();
		Graphics2D gHover = (Graphics2D) iHover.getGraphics();
		Graphics2D gPress = (Graphics2D) iPress.getGraphics();

		Polygon p = new Polygon(right ? new int[] { 1, 23, 1 } : new int[] {
				23, 1, 23 }, new int[] { 1, 16, 31 }, 3);

		GObject.antialias(gNone);
		GObject.antialias(gHover);
		GObject.antialias(gPress);

		gNone.setColor(new Color(0, 255, 0));
		gHover.setColor(new Color(82, 255, 82));
		gPress.setColor(new Color(0, 212, 0));

		gNone.fill(p);
		gHover.fill(p);
		gPress.fill(p);

		gNone.setColor(Color.BLACK);
		gHover.setColor(Color.BLACK);
		gPress.setColor(Color.BLACK);

		gNone.draw(p);
		gHover.draw(p);
		gPress.draw(p);

		GSprite sNone = new GSprite(iNone);
		GSprite sHover = new GSprite(iHover);
		GSprite sPress = new GSprite(iPress);

		sNone.setPrimitive(sNone.new ShapePrimitive(p));
		sHover.setPrimitive(sHover.new ShapePrimitive(p));
		sPress.setPrimitive(sPress.new ShapePrimitive(p));

		GButton b = new GButton();
		b.setStateSprite(ButtonState.NONE, sNone);
		b.setStateSprite(ButtonState.HOVERED, sHover);
		b.setStateSprite(ButtonState.PRESSED, sPress);

		b.setSize(16, 32);

		return b;
	}

	private GMessage createLabel(String string) {
		GMessage label = new GMessage(string);
		label.setAlignmentX(0.5);
		label.setFontSize(18f);
		label.setFontStyle(Font.BOLD);
		return label;
	}

	private GMessage createValue(String string) {
		GMessage label = new GMessage(string);
		label.setAlignmentX(0.5);
		label.setFontSize(16f);
		label.setFontStyle(Font.BOLD);
		return label;
	}

}
