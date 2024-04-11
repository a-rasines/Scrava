package domain.models.types;

import domain.models.interfaces.Valuable;

public abstract class ComparatorBlock<T> extends ConditionalBlock<T>{

	private static final long serialVersionUID = 2912259798813095766L;
	
	protected ComparatorBlock(Valuable<? extends T> left, Valuable<? extends T> right) {
		super(left, right);
	}
	
	@Override
	public BlockCategory getCategory() {
		return BlockCategory.CONDITIONAL;
	}
	
	public Boolean value(Valuable<? extends T> left, Valuable<? extends T> right) {
		return compare(left.value(), right.value());
	}
	public abstract boolean compare(T left, T right);
	
}
