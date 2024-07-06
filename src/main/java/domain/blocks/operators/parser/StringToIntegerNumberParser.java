package domain.blocks.operators.parser;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.ValuableModifier;
import domain.values.StringLiteral;

public class StringToIntegerNumberParser extends ValuableModifier<String, Long> {
	private static final long serialVersionUID = -2316860509271581027L;

	public StringToIntegerNumberParser() {
		value = new StringLiteral("0", this);
	}
	@Override
	public StringToIntegerNumberParser create(Sprite s) {
		return new StringToIntegerNumberParser();
	}
	
	@Override
	public String getCode() {
		return "Long.parseLong(\""+value.value()+"\")";
	}

	@Override
	public void getImports(Set<String> imports) {}

	@Override
	public Long value() {
		return Long.parseLong(value.value());
	}

	@Override
	public String getTitle() {
		return "to integer number " + VARIABLE_STR;
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof String;
	}

}
