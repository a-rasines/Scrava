package domain.values;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import domain.Sprite;
import domain.models.interfaces.Translatable;

public class EnumLiteral<T> extends AbstractLiteral<T> {

	private static final long serialVersionUID = 5931981357494162699L;
	private transient final Function<String, T> VALUE_OF;
	private transient final Supplier<T[]> VALUES;
	
	/**
	 * Creates an enum literal from an enum
	 * @param value MUST BE AN ENUM
	 */
	public EnumLiteral(T value) {
		super(value);
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
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates a pseudo enum literal from a map
	 * @param values
	 */
	public EnumLiteral(Map<String, T> values) {
		super(values.values().iterator().next());
		VALUE_OF = (s) -> values.get(s);
		VALUES = () -> {
			@SuppressWarnings("unchecked")
			T[] output = (T[]) Array.newInstance(values.values().iterator().next().getClass(), values.size());
			Iterator<T> vs = values.values().iterator();
			for(int i = 0; i < values.size(); i++)
				output[i] = vs.next();
			return output;
		};
	}

	@Override
	public Translatable create(Sprite s) { // N/A
		return null;
	}

	@Override
	public void setValue(String str) {
		setValue(VALUE_OF.apply(str), true);
	}
	
	public T[] possibleValues() {
		return VALUES.get();
	}

	@Override
	public boolean isEmpty() {
		return VALUES.get()[0] == value;
	}
	
}
