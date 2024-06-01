package main;

import debug.DebugOut;
import domain.AppCache;
import ui.windows.ProjectSelectorFrame;

public class MainClass {
	public static void main(String[] args) {
		DebugOut.setup();
		AppCache.load();
		new ProjectSelectorFrame().setVisible(true);
	}
}
