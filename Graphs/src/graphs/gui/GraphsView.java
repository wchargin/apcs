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
		
		
		
		GButton btnSettings = ComponentGenerator.attachLabel(ComponentGenerator.createButton(), "Settings");
		addButton(btnSettings, 5);
		btnSettings.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				context.setCurrentGameView(Views.SETTINGS);
			}
		});
	}
	
	private void addButton(GObject button, int buttonIndex) {
		addAt(button, buttonIndex * 110 + 60, 560);
	}

}
