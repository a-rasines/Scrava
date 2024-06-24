package ui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.text.JTextComponent;

public class NumberKeyListener  extends KeyAdapter{
	
	private JTextComponent tf;
	private boolean decimal;
	public NumberKeyListener(JTextComponent tf, boolean decimal) {
		this.tf = tf;
		this.decimal = decimal;
		System.out.println("a");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(!check(e.getKeyChar()))
			e.consume();
	}
	
	public boolean check(char c) {
		return (c >= '0' && c <= '9' 
		   || decimal && c == '.' && (!tf.getText().contains(".") || tf.getSelectedText() != null && tf.getSelectedText().contains("."))
		   || c == '-' && (tf.getCaret().getDot() == 0 && !tf.getText().contains("-") || tf.getSelectedText() != null && tf.getSelectedText().contains("-")));
	}
}
