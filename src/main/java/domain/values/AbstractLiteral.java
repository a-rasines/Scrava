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
		if(type instanceof Boolean)
			return (Valuable<T>) new BooleanLiteral(false);
		else if (type instanceof Number) {
			if(type instanceof Long)
				return (Valuable<T>) new NumberLiteral<Long>(0l);
			else if (type instanceof Integer)
				return (Valuable<T>) new NumberLiteral<Integer>(0);
			else if (type instanceof Short)
				return (Valuable<T>) new NumberLiteral<Short>((short)0);				
			else if (type instanceof Float)
				return (Valuable<T>) new NumberLiteral<Float>(0f);
			else if (type instanceof Double)
				return (Valuable<T>) new NumberLiteral<Double>(0.);
			else
				return (Valuable<T>) new NumberLiteral<Byte>((byte)0);
		} else
			return (Valuable<T>) new StringLiteral("");
	}
	
	protected AbstractLiteral(T value) {
		this.value = value;
		this.initialValue = value;
	}
	
	/**
	 * This function updates the value of the literal
	 * @param value the new value
	 * @param update if true, the value at restart is also changed
	 */
	public void setValue(T value, boolean update) {
		this.value = value;
		if(update)
			this.initialValue = value;
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
