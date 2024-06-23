package domain.blocks.operators;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.NumberLiteral;

public class DivideOperator  extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = 1594693481058875785L;
	
	@Override
	public DivideOperator create(Sprite s) {
		return new DivideOperator();
	}
	
	public DivideOperator() {
		setup(new NumberLiteral<Double>(0., this), new NumberLiteral<Double>(0., this));
	}

	public DivideOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		setup(left, right);
	}

	@Override
	public Number value(Valuable<? extends Number> left, Valuable<? extends Number> right) {
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
		
		return lft / rgt;
	}

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return "(" + left.getCode() + " / " + right.getCode() + ")";
	}

	@Override
	public String getTitle() {
		return VARIABLE_NUM + " ÷ " + VARIABLE_NUM;
	}

}
