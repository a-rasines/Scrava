package domain.blocks.conditional;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.ComparatorBlock;
import domain.values.StringLiteral;

@SuppressWarnings("rawtypes")
public class EqualsBlock extends ComparatorBlock<Object> {

	private static final long serialVersionUID = 1788835095568306325L;
	
	@Override
	public EqualsBlock create(Sprite s) {
		return new EqualsBlock();
	}
	
	public EqualsBlock() {
		super(new StringLiteral(""), new StringLiteral(""));
	}
	
	public EqualsBlock(Valuable<? extends Object> left, Valuable<? extends Object> right) {
		super(left, right);
	}
	
	@Override
	public String getCode(Valuable<? extends Object> left, Valuable<? extends Object> right) {
		if(left.value() instanceof Number || left.value() instanceof Boolean)
			return left.getCode() + " == " + right.getCode();
		else
			return left.getCode() + ".equals(" + right.getCode() + ") /*The == operator doesn't work well for the variable's type*/";
	}

	@Override
	public boolean isAplicable(Valuable val) {
		return getLeft()  != null && ((val.value() instanceof Number) == (getLeft().value() instanceof Number)) //Double and Long can interact
			|| getRight() != null && ((val.value() instanceof Number) == (getRight().value() instanceof Number))
			|| getLeft()  != null && getLeft().value().getClass().isInstance(val.value())
			|| getRight() != null && getRight().value().getClass().isInstance(val.value())
			|| getLeft()  == null && getRight() == null;
	}

	@Override
	public String getTitle() {
		return VARIABLE_ANY + " = " + VARIABLE_ANY;
	}
	
	@Override
	public boolean compare(Object left, Object right) {
		return left.equals(right) 
			   || left instanceof Number 
			   	  && ((Number)left).longValue() == ((Number)right).longValue()
	   			  && ((Number)left).doubleValue() == ((Number)right).doubleValue();
	}
}
