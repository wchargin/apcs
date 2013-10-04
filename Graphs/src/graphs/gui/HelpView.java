package graphs.gui;

import graphs.gui.GraphsGUI.Views;

import java.awt.Font;

import jgame.Context;
import jgame.GButton;
import jgame.GContainer;
import jgame.GMessage;
import jgame.GObject;
import jgame.listener.ButtonListener;

public class HelpView extends GContainer {

	public HelpView() {
		setSize(800, 600);

		GMessage gm = new GMessage("Help");
		gm.setFontSize(24);
		gm.setSize(800, 50);
		gm.setAnchorTopLeft();
		addAt(gm, 15, 15);

		addAt(generateShortcutInfo("I", "Increase node y-scale"), 50, 100);
		addAt(generateShortcutInfo("K", "Decrease node y-scale"), 50, 130);
		addAt(generateShortcutInfo("L", "Increase node x-scale"), 50, 160);
		addAt(generateShortcutInfo("J", "Decrease node x-scale"), 50, 190);

		GButton btnReturn = ComponentGenerator.attachLabel(
				ComponentGenerator.createButton(), "Return");
		addAt(btnReturn, 60, 560);
		btnReturn.addListener(new ButtonListener() {
			@Override
			public void mouseClicked(Context context) {
				context.setCurrentGameView(Views.GRAPHS);
			}
		});
	}

	private GObject generateShortcutInfo(String key, String description) {
		GContainer box = new GContainer();
		box.setAnchorTopLeft();
		box.setSize(800, 20);

		GMessage lblKey = new GMessage(key);
		lblKey.setFontStyle(Font.BOLD);
		lblKey.setFontSize(18);
		lblKey.setSize(20, 20);
		lblKey.setAnchorTopLeft();
		box.add(lblKey);

		GMessage lblDescription = new GMessage(description);
		lblDescription.setFontSize(16);
		lblDescription.setSize(600, 20);
		lblDescription.setAnchorTopLeft();
		box.addAt(lblDescription, 50, 0);

		return box;

	}
}
