package disjoint.maze.jgamegui;

import java.awt.Font;

import disjoint.maze.jgamegui.MazeGUI.Views;
import jgame.Context;
import jgame.GContainer;
import jgame.GMessage;
import jgame.GObject;
import jgame.GSprite;
import jgame.GSprite.PrimitiveShape;
import jgame.ImageCache;
import jgame.controller.AlphaTween;
import jgame.controller.ConstantRotationController;
import jgame.controller.Interpolation;
import jgame.controller.PulsateController;
import jgame.controller.ScaleTween;
import jgame.listener.ButtonListener;
import jgame.listener.DelayListener;

public class WinView extends GContainer {

	public WinView() {
		setSize(701, 601);
	}

	@Override
	public void viewShown() {
		super.viewShown();
		removeAllChildren();

		GSprite bg = ImageCache.getSprite("circle.png");
		addAtCenter(bg);
		bg.addController(new ConstantRotationController(-0.8));

		GMessage congrats = new GMessage("CONGRATULATIONS, YOU WIN");
		congrats.setFontStyle(Font.BOLD);
		congrats.setFontSize(40);
		congrats.setSize(701, 50);
		congrats.setAlignmentX(0.5);
		congrats.setAlignmentY(0.5);
		enter(350.5, 75, congrats, 0);

		GMessage prize = new GMessage("enjoy a cookie");
		prize.setFontSize(36);
		prize.setSize(701, 40);
		prize.setAlignmentX(0.5);
		prize.setAlignmentY(0.5);
		enter(350.5, 120, prize, 5);

		final GSprite cookie = ImageCache.getSprite("cookie.png");
		cookie.addController(new ConstantRotationController(1));
		final PulsateController pulse = new PulsateController(0.8, 0.2, 120);
		pulse.setProperties(PulsateController.SCALE);
		enter(350.5, 350, cookie, 20);
		cookie.setPrimitive(PrimitiveShape.CIRCLE);
		cookie.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				context.setCurrentGameView(Views.MAZE_VIEW);
			}
		});
	}

	private void enter(double x, double y, final GObject o, int delay) {
		addListener(new DelayListener(delay) {
			@Override
			public void invoke(GObject target, Context context) {
				add(o);
			}
		});
		o.setLocation(x, y);
		ScaleTween st = new ScaleTween(15, Interpolation.EASE_IN, 0, 1.1);
		ScaleTween bounce = new ScaleTween(3, Interpolation.EASE_OUT, 1.1, 1);
		AlphaTween fade = new AlphaTween(15, Interpolation.EASE_OUT, 0, 1);
		st.chain(bounce);
		st.with(fade);
		o.setScale(0);
		o.addController(st);
	}
}
