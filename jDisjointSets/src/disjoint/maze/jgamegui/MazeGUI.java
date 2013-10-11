package disjoint.maze.jgamegui;

import java.awt.Color;

import jgame.GContainer;
import jgame.GRootContainer;
import jgame.GSprite;
import jgame.Game;
import jgame.ImageCache;

public class MazeGUI extends Game {

	public static void main(String[] args) {
		new MazeGUI().startGame();
	}

	public MazeGUI() {
		GRootContainer root = new GRootContainer(Color.WHITE);

		MazeView mv = new MazeView();
		root.addView(Views.MAZE_VIEW, mv);

		ImageCache.create(MazeGUI.class, "/disjoint/maze/jgamegui/");
		root.addView(Views.WIN,
				new GContainer(new GSprite(ImageCache.forClass(MazeGUI.class)
						.get("cookie.png"))));

		setRootContainer(root);
	}

	public enum Views {
		MAZE_VIEW, WIN;
	}

}
