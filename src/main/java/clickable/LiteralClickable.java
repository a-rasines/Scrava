package clickable;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Function;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import domain.models.interfaces.Clickable;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import domain.values.AbstractLiteral;
import domain.values.BooleanLiteral;
import domain.values.EnumLiteral;
import ui.FlashThread;
import ui.ValueSelector;
import ui.renderers.IRenderer;
import ui.renderers.LiteralRenderer;

public class LiteralClickable implements Clickable{

	private static final long serialVersionUID = 1400645991321183219L;
	
	private final LiteralRenderer renderer;
	private final Rect hitbox;
	private AbstractLiteral<?> value;
	private final BlockClickable parent;
	
	private static class NumberKeyListener extends KeyAdapter {

		private Function<Character, Boolean> func;
		private JTextField container;
		
		public NumberKeyListener(boolean decimal, JTextField container) {
			
			this.container = container;
			this.func = decimal ? this::wDec : this::woDec; //This way the check is only done once
		}
		
		public boolean woDec(char c) {
			return (c >= '0' && c <= '9') || c == '-' && (container.getText().length() == 0 || container.getSelectedText().equals(container.getText()));
		}
		
		private boolean wDec(char c) {
			return woDec(c) //Number
				   || c == '.' && container.getText().indexOf(".") == -1 && container.getText().length() != 0 && !container.getSelectedText().equals(container.getText()); // decimal point
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			if(!func.apply(e.getKeyChar()))
				e.consume();
			
		}
		
	}
	
	public LiteralClickable(LiteralRenderer literalRenderer, AbstractLiteral<?> value, BlockClickable parent) {
		this.parent = parent;
		this.renderer = literalRenderer;
		this.hitbox = new Rect(0, 0, renderer.getHeight(), renderer.getWidth());
		this.value = value;
	}
	
	public void setValue(AbstractLiteral<?> value) {
		this.value = value;
	}
	
	@Override
	public Rect getPosition() {
		this.hitbox.w = getRenderer().getRenderable().getWidth(); //True / False have different width
		return this.hitbox;
	}

	@Override
	public void onClick(int x, int y) {
		System.out.println("literal clicked");
		try {
			ValueSelector vs;
			switch(value) {
				case BooleanLiteral b :
					b.setValue("" + !b.value);
					renderer.update();
					break;
				case EnumLiteral<?> e:
					@SuppressWarnings({ "unchecked", "rawtypes" }) 
					JComboBox cb = new JComboBox(e.possibleValues());
					vs = new ValueSelector(cb, e.value());
					vs.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
							value.setValue(cb.getSelectedItem().toString());
							renderer.update();
						}
					});
				default:
					JTextField textField = new JTextField(value.value().toString());
					if(value.value() instanceof Number) {
						textField.addKeyListener(new NumberKeyListener(value.value() instanceof Double || value.value() instanceof Float, textField));
						vs = new ValueSelector(textField, ValueSelector.DECIMAL_NUMBERS + ((value.value() instanceof Double || value.value() instanceof Float)?ValueSelector.DECIMAL_NUMBERS : 0));
					} else 
						vs = new ValueSelector(textField, ValueSelector.STRING);
					vs.setVisible(true);
					vs.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
							value.setValue(textField.getText());
							renderer.update();
						}
					});
			}
		
		} catch(NullPointerException e) {} //Cancel button pressed
	}

	@Override
	public void onDrag(int x, int y) {
		// No drag for literals		
	}

	@Override
	public void onClickEnd() {
		// No effect
	}

	@Override
	public Clickable getParent() {
		return parent;
	}

	@Override
	public void setPosition(int x, int y) {
		this.hitbox.x = x;
		this.hitbox.y = y;
	}
	@Override
	public void move(int x, int y) {
		this.hitbox.x += x;
		this.hitbox.y += y;
		
	}
	
	@Override
	public Translatable getBlock() {
		return renderer.getBlock();
	}

	@Override
	public IRenderer getRenderer() {
		return renderer;
	}

	@Override
	public void onHover(int x, int y, BlockClickable clicked) {
		if(clicked.getBlock() instanceof Valuable) {
			FlashThread.INSTANCE.setHovered(renderer);
		}
	}

	@Override
	public void onHoverEnd(boolean click, BlockClickable clicked) {
		System.out.println(toString().replaceAll(".*\\.", "") + " clicked:"+click);
		FlashThread.INSTANCE.setHovered(null);
		if(clicked.getBlock() instanceof Valuable vBlock && click) {
			System.out.println(toString().replaceAll(".*\\.", "") + " <- " + vBlock.toString().replaceAll(".*\\.", "") + " ?");
			if(((VariableHolder)parent.getRenderer().getBlock()).isAplicable(vBlock)) {
				System.out.println(toString().replaceAll(".*\\.", "") + " nesting " + clicked.getBlock().toString().replaceAll(".*\\.", ""));
				this.parent.replaceVariable(this, clicked);
				System.out.println("parent: (" + parent.getClass().getSimpleName() + ".java:0)");
				System.out.println("parent renderer: (" + parent.getRenderer().getClass().getSimpleName() + ".java:0)");
				getParent().getRenderer().update();
			}
		}
	}

	@Override
	public void delete() {
		getRenderer().delete();
	}

}
