package domain.blocks.conditional.bool;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.ConditionalBlock;
import domain.values.BooleanLiteral;

public class AndBlock extends ConditionalBlock<Boolean> {

	private static final long serialVersionUID = 2845362572824036201L;
	
	@Override
	public AndBlock create(Sprite s) {
		return new AndBlock();
	}
	
	public AndBlock() {
		setup(new BooleanLiteral(true, this), new BooleanLiteral(true, this));
	}
	
	public AndBlock(Valuable<Boolean> left, Valuable<Boolean> right) {
		setup(left, right);
	}

	@Override
	public String getCode(Valuable<? extends Boolean> left, Valuable<? extends Boolean> right) {
		return left.getCode() + " && " + right.getCode();
	}

	@Override
	public Boolean value(Valuable<? extends Boolean> left, Valuable<? extends Boolean> right) {
		return left.value() && right.value();
	}

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.OPERATOR;
	}

	@Override
	public String getTitle() {
		return VARIABLE_BOOL + " and " + VARIABLE_BOOL;
	}

}
