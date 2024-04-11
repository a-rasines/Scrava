package domain.models.types;

import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;
import ui.renderers.SimpleBlockRenderer.SimpleRenderable;

public abstract  class ValuableModifier<In, Out> implements Translatable, Valuable<Out>, SimpleRenderable {
	
	private static final long serialVersionUID = -5061705048710723500L;
	protected Valuable<In> value;
	
	@Override
	public Valuable<In> getVariableAt(int i) {
		return value;
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable<?>[] {value};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		value = (Valuable<In>) v;
		
	}

	@Override
	public void removeVariableAt(int i) {
		value = AbstractLiteral.getDefault(value.value());
		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		return (LiteralRenderable<?>) (value = AbstractLiteral.getDefault(value.value()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		value = (Valuable<In>) newValue;
		
	}

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.OPERATOR;
	}

}
