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
		setup(new NumberLiteral<Double>(0., null), new NumberLiteral<Double>(0., null));
	}
	
	public MinOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		setup(left, right);
	}

	@Override
	public Number value(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		
		if(left.value() instanceof Float || left.value() instanceof Double ||
				   right.value() instanceof Float || right.value() instanceof Double) {
					
					var lft = switch(left.value()) {
						case Double dl -> dl;
						case Float fl -> fl;
						case Long ll -> ll;
						case Integer il -> il;
						case Short sl -> sl;
						case Byte bl -> bl;
						default -> 0;
					};
					
					var rgt = switch(right.value()) {
						case Double dl -> dl;
						case Float fl -> fl;
						case Long ll -> ll;
						case Integer il -> il;
						case Short sl -> sl;
						case Byte bl -> bl;
						default -> 0;
					};
					
					return Math.min(lft, rgt);
				
				} else {
					
					var lft = switch(left.value()) {
						case Long ll -> ll;
						case Integer il -> il;
						case Short sl -> sl;
						case Byte bl -> bl;
						default -> 0;
					};
					
					var rgt = switch(right.value()) {
						case Long ll -> ll;
						case Integer il -> il;
						case Short sl -> sl;
						case Byte bl -> bl;
						default -> 0;
					};
					return Math.min(lft, rgt);
				}
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
