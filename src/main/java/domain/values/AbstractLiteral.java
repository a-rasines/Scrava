package domain.values;

import java.util.Set;

import domain.models.interfaces.Valuable;
import ui.renderers.LiteralRenderer;

public abstract class AbstractLiteral<T> implements LiteralRenderer.LiteralRenderable<T>{
	
	private static final long serialVersionUID = -3640248192146519107L;
	public T value;
	private T initialValue;
	
	@SuppressWarnings("unchecked")
	public static <T> Valuable<T> getDefault(T type) {
		return (Valuable<T>) switch(type) {
			case Boolean b -> new BooleanLiteral(false);
			case Long l -> new NumberLiteral<Long>(0l);
			case Integer i -> new NumberLiteral<Integer>(0);
			case Short s -> new NumberLiteral<Short>((short)0);
			case Float f -> new NumberLiteral<Float>(0f);
			case Double d -> new NumberLiteral<Double>(0.);
			case Byte b -> new NumberLiteral<Byte>((byte)0);
			default -> new StringLiteral("");
		};
	}
	
	protected AbstractLiteral(T value) {
		this.value = value;
		this.initialValue = value;
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
