package ui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

public class DoubleKeyListener  extends KeyAdapter{
	
	private JTextComponent tf;
	
	public DoubleKeyListener(JTextComponent tf) {
		this.tf = tf;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() == '.' && (!tf.getText().contains(".") || tf.getSelectedText().contains(".")));
		else e.consume();
	}
}
