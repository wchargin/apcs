package graphs.gui;

import graphs.gui.GraphsGUI.GraphSettings;
import graphs.gui.GraphsGUI.Views;
import jgame.Context;
import jgame.GButton;
import jgame.GContainer;
import jgame.GMessage;
import jgame.listener.ButtonListener;

public class GraphsView extends GContainer {

	public GraphsView(GraphSettings gs) {
		setSize(800, 600);
		
		GMessage msgSettings = new GMessage("Settings");
		GButton btnSettings = GradientRoundRectangle.createButton();
		btnSettings.setSize(100, 30);
		btnSettings.addAtCenter(msgSettings);
		msgSettings.setAlignmentX(0.5);
		addAtCenter(btnSettings);
		btnSettings.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				context.setCurrentGameView(Views.SETTINGS);
			}
		});
	}

}
