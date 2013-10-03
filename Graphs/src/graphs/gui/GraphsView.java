package graphs.gui;

import graphs.gui.GraphsGUI.GraphSettings;
import graphs.gui.GraphsGUI.Views;
import jgame.Context;
import jgame.GButton;
import jgame.GContainer;
import jgame.GObject;
import jgame.listener.ButtonListener;

public class GraphsView extends GContainer {

	public GraphsView(GraphSettings gs) {
		setSize(800, 600);
		
		GButton btnAddNode = createButton("Add Node");
		addButton(btnAddNode, 0);
		
		GButton btnClear = createButton("Clear");
		addButton(btnClear, 1);
		
		GButton btnDijkstra = createButton("Dijkstra");
		addButton(btnDijkstra, 2);
		
		GButton btnKruskal = createButton("Kruskal");
		addButton(btnKruskal, 3);
		
		GButton btnSettings = createButton("Settings");
		addButton(btnSettings, 6);
		btnSettings.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				context.setCurrentGameView(Views.SETTINGS);
			}
		});
	}
	
	private GButton createButton(String text) {
		return ComponentGenerator.attachLabel(ComponentGenerator.createButton(), text);
	}
	
	private void addButton(GObject button, int buttonIndex) {
		addAt(button, buttonIndex * 110 + 60, 560);
	}

}
