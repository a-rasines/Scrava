package domain.blocks.conditional.bool;

import domain.models.interfaces.Valuable;
import domain.models.types.ConditionalBlock;
import domain.models.types.OperatorBlock;

public class OrBlock extends ConditionalBlock<Boolean> {

	private static final long serialVersionUID = 8665747159545035777L;

	public OrBlock(Valuable<Boolean> left, Valuable<Boolean> right) {
		super(left, right);
	}

	@Override
	public String getCode(Valuable<? extends Boolean> left, Valuable<? extends Boolean> right) {
		return    (left instanceof OperatorBlock? "("+left.getCode()+")" : left.getCode())
				+ " || "
				+ (right instanceof OperatorBlock? "("+right.getCode()+")" : right.getCode());
	}

	@Override
	public Boolean value(Valuable<? extends Boolean> left, Valuable<? extends Boolean> right) {
		return left.value() || right.value();
	}

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.CONDITIONAL;
	}

	@Override
	public String getTitle() {
		return VARIABLE_BOOL + " or " + VARIABLE_BOOL;
	}

}
