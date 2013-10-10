package disjoint.maze.jgamegui;

import java.awt.Color;

import jgame.GRootContainer;
import jgame.Game;

public class MazeGUI extends Game {

	public static void main(String[] args) {
		new MazeGUI().startGame();
	}

	public MazeGUI() {
		GRootContainer root = new GRootContainer(Color.WHITE);
		
		MazeView mv = new MazeView();
		root.addView(Views.MAZE_VIEW, mv);
		
		setRootContainer(root);
	}
	
	public enum Views {
		MAZE_VIEW;
	}

}
