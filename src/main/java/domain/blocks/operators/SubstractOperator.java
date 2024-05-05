package domain.blocks.operators;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.NumberLiteral;

public class SubstractOperator extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = -4532995180329907254L;

	@Override
	public SubstractOperator create(Sprite s) {
		return new SubstractOperator();
	}
	
	public SubstractOperator() {
		super(new NumberLiteral<Double>(0.), new NumberLiteral<Double>(0.));
	}
	
	public SubstractOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
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
			
			return lft - rgt;
		
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
			return lft - rgt;
		}
	}

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return left.getCode() + " - " + right.getCode();
	}

	@Override
	public String getTitle() {
		return VARIABLE_NUM + " - " + VARIABLE_NUM;
	}

}
