package disjoint.maze.jgamegui;

import java.awt.Color;

import jgame.Context;
import jgame.GContainer;
import jgame.GObject;
import jgame.listener.FrameListener;
import disjoint.maze.MazeGenerator;

public class MazeView extends GContainer {

	private MazeGenerator gen;

	public MazeView() {
		setBackgroundColor(Color.WHITE);
		setSize(401, 401);
		gen = new MazeGenerator(50, 50);
		addListener(new FrameListener() {
			@Override
			public void invoke(GObject target, Context context) {
				gen.step();
				if (gen.isFinished()) {
					target.removeListener(this);
					setBackgroundColor(Color.YELLOW);
				}
			}
		});
		
		MazePanel mp = new MazePanel();
		mp.setSize(401, 401);
		addAtCenter(mp);
		mp.setGen(gen);
	}

}
