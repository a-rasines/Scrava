package domain.blocks.operators;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.NumberLiteral;

public class AddOperator extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = 4634547651800943320L;

	public AddOperator create(Sprite s) {
		return new AddOperator();
	}
	public AddOperator() {
		super(new NumberLiteral<Double>(0.), new NumberLiteral<Double>(0.));
	}
	
	public AddOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
	}

	@Override
	public Number value(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		if (left.value() instanceof Float || left.value() instanceof Double) {
			if(right.value() instanceof Float || right.value() instanceof Double)
				return (Double)(((Number)left.value()).doubleValue() + ((Number)right.value()).doubleValue());
			else if(right.value() instanceof Number)
				return (Double)(((Number)left.value()).doubleValue() + ((Number)right.value()).longValue());
		} else if (left.value() instanceof Number)
			if(right.value() instanceof Float || right.value() instanceof Double)
				return (Double)(((Number)left.value()).longValue() + ((Number)right.value()).doubleValue());
			else if(right.value() instanceof Number)
				return (Long)(((Number)left.value()).longValue() + ((Number)right.value()).longValue());
		return null;
	
	}

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
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
