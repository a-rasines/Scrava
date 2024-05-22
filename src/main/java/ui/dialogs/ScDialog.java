package ui.dialogs;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JDialog;

import ui.components.ProjectFrame;

public abstract class ScDialog extends JDialog implements WindowFocusListener, WindowListener {

	private static final long serialVersionUID = -9152131285115146626L;
	
	protected static boolean isAlreadyOpen = false;
	public final static boolean isAlreadyOpen() {
		return isAlreadyOpen;
	}
	
	protected ScDialog() {
		isAlreadyOpen = true;
		addWindowFocusListener(this);
		addWindowListener(this);
	}
	
	@Override
	public void windowLostFocus(WindowEvent e) {
		if(!ProjectFrame.isFocus())requestFocus();	
	}

	@Override
	public void windowClosing(WindowEvent e) {
		isAlreadyOpen = false;
	}
	
	@Override
	public void dispose() {
		windowClosing(null);
		super.dispose();
	}
	
	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowGainedFocus(WindowEvent e) {}
	@Override public void windowOpened(WindowEvent e) {}
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowActivated(WindowEvent e) {}
	@Override public void windowDeactivated(WindowEvent e) {}
	

}
