package domain.blocks.conditional;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.ComparatorBlock;
import domain.values.NumberLiteral;

public class BiggerOrEqualThanBlock extends ComparatorBlock<Number>{

	private static final long serialVersionUID = -257749190617555891L;

	@Override
	public boolean compare(Number left, Number right) {
		return left.longValue() == right.longValue()? 
			   left.doubleValue() >= right.doubleValue() 
			 : left.longValue() >= right.longValue();
	}
	
	@Override
	public BiggerOrEqualThanBlock create(Sprite s) {
		return new BiggerOrEqualThanBlock();		
	}
	
	public BiggerOrEqualThanBlock() {
		super(new NumberLiteral<Double>(0.),new NumberLiteral<Double>(0.));
	}

	public BiggerOrEqualThanBlock(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
	}
	
	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return left.getCode() + " >= " + right.getCode();
	}

	@Override
	public boolean isAplicable(@SuppressWarnings("rawtypes") Valuable val) {
		return val.value() instanceof Number;
	}

	@Override
	public String getTitle() {
		return VARIABLE_NUM+" is bigger or equal than "+VARIABLE_NUM;
	}

}
