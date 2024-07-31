package domain.values;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Set;

import clickable.BlockClickable;
import domain.Sprite;
import domain.models.interfaces.Valuable;
import ui.renderers.IRenderer;
import ui.renderers.IRenderer.IRenderable;
import ui.renderers.LiteralRenderer;

public abstract class AbstractLiteral<T> implements LiteralRenderer.LiteralRenderable<T>{
	
	private LiteralRenderer lr;
	
	@Override
	public AbstractLiteral<T> create(Sprite s) {
		return null; //No need
	}
	
	@Override
	public IRenderer getRenderer() {
		if(lr == null && (parent == null || parent.getRenderer() != null))
			lr = LiteralRenderer.of(this, value, parent==null?null:(BlockClickable)parent.getRenderer().getClickable());
		return lr;
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		this.value = this.initialValue;
	}
	
	private static final long serialVersionUID = -3640248192146519107L;
	public transient T value;
	private T initialValue;
	
	@SuppressWarnings("unchecked")
	public static <T> Valuable<T> getDefault(T type, IRenderable parent) {
		return (Valuable<T>) switch(type) {
			case Boolean b -> new BooleanLiteral(false, parent);
			case Long l -> new NumberLiteral<Long>(0l ,parent);
			case Integer i -> new NumberLiteral<Integer>(0 ,parent);
			case Short s -> new NumberLiteral<Short>((short)0 ,parent);
			case Float f -> new NumberLiteral<Float>(0f ,parent);
			case Double d -> new NumberLiteral<Double>(0. ,parent);
			case Byte b -> new NumberLiteral<Byte>((byte)0 ,parent);
			default -> new StringLiteral("" ,parent);
		};
	}
	
	private IRenderable parent;
	protected AbstractLiteral(T value, IRenderable parent) {
		this.value = value;
		this.initialValue = value;
		this.parent = parent;
	}
	
	public IRenderable getParent() {
		return parent;
	}
	
	/**
	 * This function updates the value of the literal
	 * @param object the new value
	 * @param update if true, the value at restart is also changed
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object object, boolean update) {
		this.value = (T)object;
		if(update)
			this.initialValue = (T)object;
		getRenderer().update();
	}
	
	
	@Override
	public String getCode() {
		return ""+initialValue;
	}

	@Override
	public T value() {
		return value;
	}
	
	public abstract void setValue(String str);
	
	public T initialValue() {
		return initialValue;
	}
	
	public void reset() {
		this.value = initialValue;
	}
	
	/**
	 * Checks whether the value is the 0 equivalent in the corresponding class
	 * @return
	 */
	public abstract boolean isEmpty();

	@Override
	public void getImports(Set<String> imports) {}
	
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}
}
