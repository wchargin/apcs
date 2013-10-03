package graphs.gui;

import graphs.gui.GraphsGUI.GraphSettings;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import jgame.GContainer;
import jgame.GMessage;
import jgame.GObject;

public class SettingsView extends GContainer {

	/**
	 * The settings object controlled by this view.
	 */
	private GraphSettings settings;

	public SettingsView(GraphSettings gs) {
		setSize(800, 600);

		settings = gs;

		GObject cplTextSize = createControlPanel(200, 100);
		GMessage lblTextSize = createLabel("Text size");
		GMessage valTextSize = createValue(Integer
				.toString((int) settings.textSize));
		lblTextSize.setSize(200, 75);
		valTextSize.setSize(200, 25);
//		cplTextSize.addAt(lblTextSize, 200, 50);
//		cplTextSize.addAt(valTextSize, 0, 50);
		cplTextSize.addAtCenter(valTextSize);
		cplTextSize.addAtCenter(lblTextSize);
		addAtCenter(cplTextSize);
	}

	private GObject createControlPanel(double w, double h) {
		class ControlPanel extends GContainer {
			private RoundRectangle2D r2d;

			public ControlPanel(double w, double h) {
				r2d = new RoundRectangle2D.Double(0, 0, w, h, 16, 16);
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
		}
		return new ControlPanel(w, h);
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
