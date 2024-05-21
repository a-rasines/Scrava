package domain.values;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

import ui.renderers.IRenderer.IRenderable;

public class EnumLiteral<T> extends AbstractLiteral<T> {

	private static final long serialVersionUID = 5931981357494162699L;
	private ValueListener<T> valueListener = new DefaultListener<T>(); 
	private String name;
	private EnumCapable<T> behaviour;
	
	public static interface EnumCapable<T> extends Serializable {
		T valueof(String value);
		T[] getValues();
		String[] names();
	}
	
	public static interface ValueListener<T> extends BiConsumer<String, T>, Serializable {}
	
	private static class DefaultListener<T> implements ValueListener<T> {

		private static final long serialVersionUID = 4144235544702403961L;
		@Override public void accept(String t, T u) {}
		
	}
	
	private static class MapEnumCapable<T> implements EnumCapable<T> {

		private static final long serialVersionUID = -7759270095502208514L;
		
		private Map<String, T> map;
		
		public MapEnumCapable(Map<String, T> map) {
			this.map = map;
		}
		
		@Override
		public T valueof(String value) {
			return map.get(value);
		}

		@Override
		public T[] getValues() {
			@SuppressWarnings("unchecked")
			T[] output = (T[]) Array.newInstance(map.values().iterator().next().getClass(), map.size());
			Iterator<T> vs = map.values().iterator();
			for(int i = 0; i < map.size(); i++)
				output[i] = vs.next();
			return output;
		}

		@Override
		public String[] names() {
			return map.keySet().toArray(new String[map.size()]);
		}
		
	}
	
	/**
	 * Creates a pseudo enum literal from a map
	 * @param values
	 */
	public EnumLiteral(Map<String, T> values, IRenderable parent) {
		super(values.values().iterator().next(), parent);
		name = values.keySet().iterator().next();
		behaviour = new MapEnumCapable<>(values);
	}
	/**
	 * Creates a pseudo enum literal from functions
	 * @param values
	 */
	public EnumLiteral(EnumCapable<T> ec, IRenderable parent) {
		super(ec.getValues()[0], parent);
		name = ec.names()[0];
		behaviour = ec;
	}

	public void setValueListener(ValueListener<T> bc) {
		this.valueListener = bc;
	}
	
	@Override
	public void setValue(String str) {
		name = str;
		setValue(behaviour.valueof(str), true);
		valueListener.accept(str, value);
	}
	
	public T[] possibleValues() {
		return behaviour.getValues();
	}
	
	public String name() {
		return name;
	}
	
	public String[] names() {
		return behaviour.names();
	}

	@Override
	public boolean isEmpty() {
		return behaviour.getValues()[0] == value;
	}
	
	
	
}
