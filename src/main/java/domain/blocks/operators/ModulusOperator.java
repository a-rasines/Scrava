package domain.blocks.operators;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.NumberLiteral;

public class ModulusOperator  extends OperatorBlock<Number, Number>{

	private static final long serialVersionUID = -5942033124848216838L;

	@Override
	public ModulusOperator create(Sprite s) {
		return new ModulusOperator();
	}
	
	public ModulusOperator() {
		super(new NumberLiteral<Double>(0.), new NumberLiteral<Double>(0.));
	}
	
	public ModulusOperator(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
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
		
		return lft % rgt;
	}

	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return left.getCode() + " % " + right.getCode();
	}

	@Override
	public String getTitle() {
		return "Modulus of " + VARIABLE_NUM + " ÷ " + VARIABLE_NUM;
	}
}
