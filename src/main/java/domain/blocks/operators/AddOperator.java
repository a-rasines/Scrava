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
			
			return lft + rgt;
		
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
			return lft + rgt;
		}
		
	
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
	
	public static void main(String[] args) {
		System.out.println(new AddOperator(new NumberLiteral<Float>(1.f), new NumberLiteral<Integer>(1)).value().getClass());
	}
}
