package domain.blocks.operators.parser;

import java.util.Set;

import domain.BlockInvokationException;
import domain.models.interfaces.Valuable;
import domain.models.types.ValuableModifier;

public class StringToDecimalNumberParser extends ValuableModifier<String, Double> {
	private static final long serialVersionUID = -2316860509271581027L;

	@Override
	public String getCode() {
		return "Double.parseDouble(\""+value.value()+"\")";
	}

	@Override
	public void getImports(Set<String> imports) {}

	@Override
	public Double value() {
		try {
			return Double.parseDouble(value.value());
		} catch (NumberFormatException e) {
			throw new BlockInvokationException("The text '"+value.value()+"' is not a number");
		}
	}

	@Override
	public String getTitle() {
		return "to decimal numer " + VARIABLE_STR;
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof String;
	}

}
