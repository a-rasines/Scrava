package domain.blocks.operators;

import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;

public class ModulusOperator  extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = -5942033124848216838L;

	public ModulusOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
	}

	@Override
	public Number value(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		if (left.value() instanceof Float || left.value() instanceof Double) {
			
			if(right.value() instanceof Float || right.value() instanceof Double)
				return left.value().doubleValue() % right.value().doubleValue();
			else
				return left.value().doubleValue() % right.value().longValue();
			
		} else
			
			if(right.value() instanceof Float || right.value() instanceof Double)
				return left.value().longValue() % right.value().doubleValue();
			else
				return left.value().longValue() % right.value().longValue();
	}

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return left.getCode() + " % " + right.getCode();
	}

	@Override
	public String getTitle() {
		return "Modulus of " + VARIABLE_NUM + " when divided by " + VARIABLE_NUM;
	}
}
