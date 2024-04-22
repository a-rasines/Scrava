package domain.blocks.operators;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.NumberLiteral;

public class MultiplyOperator extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = -4688568223164399232L;

	@Override
	public MultiplyOperator create(Sprite s) {
		return new MultiplyOperator();
	}
	
	public MultiplyOperator() {
		super(new NumberLiteral<Double>(0.), new NumberLiteral<Double>(0.));
	}
	
	public MultiplyOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
	}

	@Override
	public Number value(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		if (left.value() instanceof Float || left.value() instanceof Double) {
			
			if(right.value() instanceof Float || right.value() instanceof Double)
				return left.value().doubleValue() * right.value().doubleValue();
			else
				return left.value().doubleValue() * right.value().longValue();
			
		} else
			
			if(right.value() instanceof Float || right.value() instanceof Double)
				return left.value().longValue() * right.value().doubleValue();
			else
				return left.value().longValue() * right.value().longValue();
	}

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return left.getCode() + " * " + right.getCode();
	}

	@Override
	public String getTitle() {
		return VARIABLE_NUM + " X " + VARIABLE_NUM;
	}

}
