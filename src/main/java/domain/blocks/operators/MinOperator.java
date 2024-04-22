package domain.blocks.operators;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.NumberLiteral;

public class MinOperator  extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = -3518067417852159122L;

	@Override
	public MinOperator create(Sprite s) {
		return new MinOperator();
	}
	
	public MinOperator() {
		super(new NumberLiteral<Double>(0.), new NumberLiteral<Double>(0.));
	}
	
	public MinOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
	}

	@Override
	public Number value(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		
		if (left.value() instanceof Float || left.value() instanceof Double) {
			
			if(right.value() instanceof Float || right.value() instanceof Double)
				return Math.min(left.value().doubleValue(), right.value().doubleValue());
			else
				return Math.min(left.value().doubleValue(), right.value().longValue());
			
		} else
			
			if(right.value() instanceof Float || right.value() instanceof Double)
				return Math.min(left.value().longValue(), right.value().doubleValue());
			else
				return Math.min(left.value().longValue(), right.value().longValue());
	}

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return "Math.min(" + left.getCode() + ", " + right.getCode()+")";
	}
	
	@Override
	public void getImports(Set<String> imports) {
		super.getImports(imports);
		imports.add("java.lang.Math");
	}


	@Override
	public String getTitle() {
		return "Minimun between " + VARIABLE_NUM + " and " + VARIABLE_NUM;
	}

}
