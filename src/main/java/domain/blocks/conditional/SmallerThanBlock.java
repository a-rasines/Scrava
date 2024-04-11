package domain.blocks.conditional;

import domain.models.interfaces.Valuable;
import domain.models.types.ComparatorBlock;

public class SmallerThanBlock extends ComparatorBlock<Number>{

	private static final long serialVersionUID = 831842678755473037L;

	@Override
	public boolean compare(Number left, Number right) {
		return left.longValue() == right.longValue()? 
				   left.doubleValue() < right.doubleValue() 
				 : left.longValue() < right.longValue();
	}

	public SmallerThanBlock(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		super(left, right);
	}
	
	@Override
	public String getCode(Valuable<? extends Number> left, Valuable<? extends Number> right) {
		return left.getCode() + " < " + right.getCode();
	}
	
	@Override
	public boolean isAplicable(@SuppressWarnings("rawtypes") Valuable val) {
		return val.value() instanceof Number;
	}

	@Override
	public String getTitle() {
		return VARIABLE_NUM + " is smaller than " + VARIABLE_NUM;
	}

}
