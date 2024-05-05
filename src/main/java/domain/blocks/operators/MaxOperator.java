package domain.blocks.operators;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.NumberLiteral;

public class MaxOperator  extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = 6618139741671057603L;

	@Override
	public MaxOperator create(Sprite s) {
		return new MaxOperator();
	}
	
	public MaxOperator() {
		super(new NumberLiteral<Double>(0.), new NumberLiteral<Double>(0.));
	}
	
	public MaxOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
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
					
					return Math.max(lft, rgt);
				
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
					return Math.max(lft, rgt);
				}
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
