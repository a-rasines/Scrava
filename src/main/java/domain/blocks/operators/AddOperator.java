package domain.blocks.operators;

import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;

public class AddOperator<T extends Number> extends OperatorBlock<T, T>{

	private static final long serialVersionUID = 4634547651800943320L;

	public AddOperator(Valuable<? extends T> left, Valuable<? extends T> right) {
		super(left, right);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T value(Valuable<? extends T> left, Valuable<? extends T> right) {
		if (left.value() instanceof Float || left.value() instanceof Double) {
			if(right.value() instanceof Float || right.value() instanceof Double)
				return (T)(Double)(((Number)left.value()).doubleValue() + ((Number)right.value()).doubleValue());
			else if(right.value() instanceof Number)
				return (T)(Double)(((Number)left.value()).doubleValue() + ((Number)right.value()).longValue());
		} else if (left.value() instanceof Number)
			if(right.value() instanceof Float || right.value() instanceof Double)
				return (T)(Double)(((Number)left.value()).longValue() + ((Number)right.value()).doubleValue());
			else if(right.value() instanceof Number)
				return (T)(Long)(((Number)left.value()).longValue() + ((Number)right.value()).longValue());
		return null;
	
	}

	@Override
	public String getCode(Valuable<? extends T> left, Valuable<? extends T> right) {
		return left.getCode() + " + " + right.getCode();
	}
	
	@Override
	public boolean isAplicable(Valuable<?> a) {
		return a.value() instanceof Number;
	}

	@Override
	public String getTitle() {
		return VARIABLE_NUM + " + " + VARIABLE_NUM;
	}
}
