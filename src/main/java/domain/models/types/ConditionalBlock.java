package domain.models.types;

import domain.models.interfaces.Valuable;

public abstract class ConditionalBlock<T> extends OperatorBlock<T, Boolean>{
	
	private static final long serialVersionUID = 6168622507519736294L;

	protected ConditionalBlock(Valuable<? extends T> left, Valuable<? extends T> right) {
		super(left, right);
	}
}
