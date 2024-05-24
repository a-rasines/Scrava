package ui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NameKeyListener extends KeyAdapter{

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z' || e.getKeyChar() >= 'A' && e.getKeyChar() <= 'Z' || e.getKeyChar() == '_');
		else e.consume();
	}

}
