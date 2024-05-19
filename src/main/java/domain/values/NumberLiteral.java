package domain.values;

import ui.renderers.IRenderer.IRenderable;

public class NumberLiteral<T extends Number> extends AbstractLiteral<T>{

	private static final long serialVersionUID = 8455810206707901226L;
	
	public NumberLiteral(T value, IRenderable parent) {
		super(value, parent);
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
