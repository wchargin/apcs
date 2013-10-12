package disjoint.maze.jgamegui;

import java.awt.Color;

import jgame.GRootContainer;
import jgame.Game;
import jgame.ImageCache;

public class MazeGUI extends Game {

	public static void main(String[] args) {
		ImageCache.create(MazeGUI.class, "/disjoint/maze/jgamegui/");
		new MazeGUI().startGame();
	}

	public MazeGUI() {
		GRootContainer root = new GRootContainer(Color.WHITE);

		root.addView(Views.MAZE_VIEW, new MazeView());
		root.addView(Views.WIN, new WinView());
		
		setRootContainer(root);
	}

	public enum Views {
		MAZE_VIEW, WIN;
	}

}
