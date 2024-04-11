package domain.values;

/**
 * Functions to solve problems with {@link java.lang.Number Number}'s lack of operators and adaptability
 */
public class NumberHelper {

	/**
	 * As the Number class has no method for it, has no operators and non-native Number derivates can't be casted between each other, casts a number from one format to another or just from generic class to specific
	 * @param <T> Class to cast it to
	 * @param n number to cast
	 * @param end class of the ending format
	 * @return The most similar number to the original in the final format as Java would cast it
	 */
	public static <T extends Number> T castTo(Number n, Class<T> end) {
		if(n instanceof Long)
			return end.cast((long)n);
		else if(n instanceof Integer)
			return end.cast((int)n);
		else if(n instanceof Short)
			return end.cast((short)n);
		else if(n instanceof Byte)
			return end.cast((byte)n);
		else if(n instanceof Float)
			return end.cast((float)n);
		else if(n instanceof Double)
			return end.cast((double)n);
		else
			throw new IllegalArgumentException("The class " +n.getClass().getName() +" is not implemented for this method");
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
