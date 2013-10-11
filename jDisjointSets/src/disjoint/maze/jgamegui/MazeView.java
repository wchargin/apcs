package disjoint.maze.jgamegui;

import java.awt.Color;
import java.util.List;

import jgame.Context;
import jgame.GButton;
import jgame.GContainer;
import jgame.GObject;
import jgame.listener.ButtonListener;
import jgame.listener.FrameListener;
import disjoint.maze.MazeGenerator;
import disjoint.maze.MazeSolver;
import disjoint.maze.MazeGenerator.MazeNode;

public class MazeView extends GContainer {

	private MazeGenerator gen;
	private MazePanel mazePanel;
	private List<MazeNode> solved;
	private FrameListener stepper = new FrameListener() {
		@Override
		public void invoke(GObject target, Context context) {
			if (gen != null && !gen.isFinished()) {
				for (int i = 0; i <= gen.getWidth() * gen.getHeight() / 300; i++) {
					mazePanel.step();
				}
			} else {
				target.removeListener(this);
			}
		}
	};
	private FrameListener solver = new FrameListener() {
		@Override
		public void invoke(GObject target, Context context) {
			if (!gen.isFinished() || solved == null || solved.isEmpty()) {
				target.removeListener(this);
			} else {
				mazePanel.getHighlighted().add(solved.remove(0));
			}
		}
	};

	public MazeView() {
		setBackgroundColor(Color.WHITE);
		setSize(701, 601);

		mazePanel = new MazePanel();
		mazePanel.setSize(600, 600);
		mazePanel.setGenerator(gen);
		mazePanel.setAnchorTopLeft();
		addAt(mazePanel, 0, 0);

		GButton btn10 = createGenerateButton(10);
		GButton btn20 = createGenerateButton(20);
		GButton btn30 = createGenerateButton(30);
		GButton btn40 = createGenerateButton(40);

		addAt(btn10, 625, 25);
		addAt(btn20, 670, 25);
		addAt(btn30, 625, 70);
		addAt(btn40, 670, 70);

		GButton btnPlay = createButton("Play");
		addAt(btnPlay, 647, 115);
		btnPlay.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				addListener(stepper);
			}
		});

		GButton btnStop = createButton("Stop");
		addAt(btnStop, 647, 160);
		btnStop.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				removeListener(stepper);
			}
		});

		GButton btnFinish = createButton("Finish");
		addAt(btnFinish, 647, 205);
		btnFinish.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				if (gen != null) {
					gen.finish();
				}
			}
		});

		GButton btnStep1 = createButton("Step");
		addAt(btnStep1, 647, 250);
		stepX(btnStep1, 1);

		GButton btnStep5 = createStepButton(5);
		addAt(btnStep5, 625, 295);

		GButton btnStep10 = createStepButton(10);
		addAt(btnStep10, 670, 295);

		GButton btnStep20 = createStepButton(20);
		addAt(btnStep20, 625, 340);

		GButton btnStep50 = createStepButton(50);
		addAt(btnStep50, 670, 340);

		GButton btnSolve = createButton("Solve");
		addAt(btnSolve, 647, 385);
		btnSolve.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				startSolve();
			}
		});
	}

	private void startSolve() {
		if (gen != null && gen.getMaze() != null && gen.isFinished()) {
			if (solved == null || solved.isEmpty()) {
				solved = MazeSolver.solve(gen.getMaze());
				addListener(solver);
			}
		} else {
			removeListener(solver);
		}
	}

	private void initializeMaze(int s) {
		mazePanel.setGenerator(gen = (s > 0 ? new MazeGenerator(s, s) : null));
	}

	private void stepX(GObject o, final int count) {
		o.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				if (gen != null) {
					for (int i = 0; i < count; i++) {
						mazePanel.step();
					}
				}
			}
		});
	}

	private GButton createGenerateButton(final int s) {
		GButton btn = createSmallButton(Integer.toString(s));
		btn.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				initializeMaze(s);
			}
		});
		return btn;
	}

	private GButton createStepButton(int num) {
		GButton btn = createSmallButton("\u00d7" + num); // times sign
		stepX(btn, num);
		return btn;
	}

	private GButton createSmallButton(String text) {
		GButton btn = ComponentGenerator.createButton(40, 40);
		ComponentGenerator.attachLabel(btn, text);
		return btn;
	}

	private GButton createButton(String text) {
		GButton btn = ComponentGenerator.createButton(85, 40);
		ComponentGenerator.attachLabel(btn, text);
		return btn;
	}

}
