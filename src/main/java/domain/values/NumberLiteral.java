package domain.values;

import domain.Sprite;

public class NumberLiteral<T extends Number> extends AbstractLiteral<T>{

	private static final long serialVersionUID = 8455810206707901226L;
	
	@Override
	public NumberLiteral<Double> create(Sprite s) {
		return new NumberLiteral<Double>(0.);
	}
	
	public NumberLiteral(T value) {
		super(value);
	}
	
	@Override
	public String getCode() {
		if(initialValue() instanceof Long)
			return super.getCode() + "l";
		else if (value instanceof Float)
			return super.getCode() + "f";
		else
			return super.getCode();
	}

	@Override
	public boolean isEmpty() {
		return value().equals(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(String str) {
		this.setValue((T) NumberHelper.parse(str, value.getClass()), true);
		
	}
}
