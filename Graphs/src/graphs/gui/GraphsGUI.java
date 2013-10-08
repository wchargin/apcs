package graphs.gui;

import java.awt.Color;

import jgame.GRootContainer;
import jgame.Game;

public class GraphsGUI extends Game {

	public GraphsGUI() {
		GRootContainer root = new GRootContainer(Color.WHITE);
		setRootContainer(root);

		GraphSettings gs = new GraphSettings();

		root.addView(Views.GRAPHS, new GraphsView(gs));
		root.addView(Views.SETTINGS, new SettingsView(gs));
		root.addView(Views.HELP, new HelpView());
	}

	public enum Views {
		GRAPHS, SETTINGS, HELP;
	}

	public class GraphSettings {
		public float textSize = 12;
		public boolean useDistanceWeight = true;
		public boolean realtime = true;
	}

	public static void main(String[] args) {
		new GraphsGUI().startGame("Graphs");
	}

}
