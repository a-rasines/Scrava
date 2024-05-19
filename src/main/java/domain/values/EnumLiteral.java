package domain.values;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ui.renderers.IRenderer.IRenderable;

public class EnumLiteral<T> extends AbstractLiteral<T> {

	private static final long serialVersionUID = 5931981357494162699L;
	private transient final Function<String, T> VALUE_OF;
	private transient final Supplier<T[]> VALUES;
	private transient final Supplier<String[]> NAMES;
	private BiConsumer<String, T> valueListener = (a,b)->{}; 
	private String name;
	
	
	/**
	 * Creates an enum literal from an enum
	 * @param value MUST BE AN ENUM
	 */
	public EnumLiteral(T value, IRenderable parent) {
		super(value, parent);
		name = value.toString();
		try {
			VALUE_OF = new Function<String, T>(){
				private Method m; {m = value.getClass().getDeclaredMethod("valueOf", String.class);}
				
				@SuppressWarnings("unchecked")
				@Override 
				public T apply(String t) { 
					try { return (T) m.invoke(null, t); } 
					catch (Exception e) { e.printStackTrace();throw new RuntimeException(e);	}
			}};
			VALUES = new Supplier<T[]>() {
				private Method m; {m = value.getClass().getDeclaredMethod("values");}
				
				@SuppressWarnings("unchecked")
				@Override
				public T[] get() {
					try { return (T[]) m.invoke(null); } 
					catch (Exception e) { e.printStackTrace();throw new RuntimeException(e);	}
				}
			};
			NAMES = () -> {
				T[] vals = VALUES.get();
				String[] out = new String[vals.length];
				for(int i = 0; i < vals.length; i++)
					out[i] = vals[i].toString();
				return out;
			};
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates a pseudo enum literal from a map
	 * @param values
	 */
	public EnumLiteral(Map<String, T> values, IRenderable parent) {
		super(values.values().iterator().next(), parent);
		name = values.keySet().iterator().next();
		VALUE_OF = (s) -> values.get(s);
		VALUES = () -> {
			@SuppressWarnings("unchecked")
			T[] output = (T[]) Array.newInstance(values.values().iterator().next().getClass(), values.size());
			Iterator<T> vs = values.values().iterator();
			for(int i = 0; i < values.size(); i++)
				output[i] = vs.next();
			return output;
		};
		NAMES = () -> values.keySet().toArray(new String[values.size()]);
	}
	/**
	 * Creates a pseudo enum literal from functions
	 * @param values
	 */
	public EnumLiteral(Function<String, T> valueOf, Supplier<T[]> values, Supplier<String[]> names, IRenderable parent) {
		super(values.get()[0], parent);
		name = names.get()[0];
		VALUE_OF = valueOf;
		VALUES = values;
		NAMES = names;
	}

	public void setValueListener(BiConsumer<String, T> bc) {
		this.valueListener = bc;
	}
	
	@Override
	public void setValue(String str) {
		name = str;
		setValue(VALUE_OF.apply(str), true);
		valueListener.accept(str, value);
	}
	
	public T[] possibleValues() {
		return VALUES.get();
	}
	
	public String name() {
		return name;
	}
	
	public String[] names() {
		return NAMES.get();
	}

	@Override
	public boolean isEmpty() {
		return VALUES.get()[0] == value;
	}
	
	
	
}
