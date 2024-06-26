package domain.values;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Functions to solve problems with {@link java.lang.Number Number}'s lack of operators and adaptability
 */
public class NumberHelper {
	
	@SuppressWarnings("rawtypes")
	public static Class getEquivalent(Class in) {
		if(in.equals(int.class))
			return Integer.class;
		if(in.getSimpleName().charAt(0) <= 'Z')
			try {
				return (Class) in.getField("TYPE").get(null);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				return in;
			}
		else
			try {
				return Class.forName(in.getPackageName() + "." + in.getSimpleName().substring(0,1).toUpperCase() + in.getSimpleName().substring(1));
			} catch (ClassNotFoundException e) {
				return in;
			}
	}
	
	/**
	 * As the Number class has no method for it, has no operators and non-native Number derivates can't be casted between each other, casts a number from one format to another or just from generic class to specific
	 * @param <T> Class to cast it to
	 * @param n number to cast
	 * @param end class of the ending format
	 * @return The most similar number to the original in the final format as Java would cast it
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T castTo(Number n, Class<T> end) {
		try {
			Class<?> equivalent = getEquivalent(end);
			for(Method a : n.getClass().getDeclaredMethods()) {
				if(a.getName().endsWith("Value") && (a.getReturnType().equals(end) || a.getReturnType().equals(equivalent)))
						return (T) a.invoke(n);
			}
			for(Method a : getEquivalent(n.getClass()).getDeclaredMethods()) {
				if(a.getName().endsWith("Value") && (a.getReturnType().equals(end) || a.getReturnType().equals(equivalent)))
					return (T) a.invoke(n);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		throw new NullPointerException();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Number> T parse(String s, Class<T> end) {
		if(end.equals(Integer.class))
			return (T)(Integer)Integer.parseInt(s); // Integer always has to be the special one
		else
			try {
				return (T)end.getMethod("parse"+end.getSimpleName(), String.class).invoke(null, s);
			} catch (ReflectiveOperationException e) { // NumberFormatException it's not this function's problem
				e.printStackTrace();
				throw new RuntimeException(); // Pretty unlike, so better not force to catch it
			}
	}
}
