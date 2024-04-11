package domain.blocks.operators;

import java.util.Set;

import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;

public class MaxOperator  extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = 6618139741671057603L;

	public MaxOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
	}

	@Override
	public Number value(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		
		if (left.value() instanceof Float || left.value() instanceof Double) {
			
			if(right.value() instanceof Float || right.value() instanceof Double)
				return Math.max(left.value().doubleValue(), right.value().doubleValue());
			else
				return Math.max(left.value().doubleValue(), right.value().longValue());
			
		} else
			
			if(right.value() instanceof Float || right.value() instanceof Double)
				return Math.max(left.value().longValue(), right.value().doubleValue());
			else
				return Math.max(left.value().longValue(), right.value().longValue());
	}

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return "Math.max(" + left.getCode() + ", " + right.getCode()+")";
	}
	
	@Override
	public void getImports(Set<String> imports) {
		super.getImports(imports);
		imports.add("java.lang.Math");
	}

	@Override
	public String getTitle() {
		return "Maximun between " + VARIABLE_NUM + " and " + VARIABLE_NUM;
	}

}
