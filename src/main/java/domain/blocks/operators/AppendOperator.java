package domain.blocks.operators;

import domain.models.interfaces.Valuable;
import domain.models.types.OperatorBlock;
import domain.values.StringLiteral;

public class AppendOperator extends OperatorBlock<Object, String>{

	private static final long serialVersionUID = 4634547651800943320L;

	public AppendOperator() {
		super(StringLiteral.nullValue(), StringLiteral.nullValue());
	}
	@Override
	public String value(Valuable<? extends Object> left, Valuable<? extends Object> right) {
		return left.value().toString() + right.value().toString();
	
	}

	@Override
	public String getCode(Valuable<? extends Object> left, Valuable<? extends Object> right) {
		return "\"\" + " + left.getCode() + " + " + right.getCode();
	}
	
	@Override
	public boolean isAplicable(Valuable<?> a) {
		return true;
	}

	@Override
	public String getTitle() {
		return "Join " + VARIABLE_ANY + ", " + VARIABLE_ANY;
	}
}
