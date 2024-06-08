package domain.models.types;

import java.util.Set;

import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import ui.renderers.IRenderer;
import ui.renderers.LiteralRenderer.LiteralRenderable;
import ui.renderers.SimpleBlockRenderer;
import ui.renderers.SimpleBlockRenderer.SimpleRenderable;

public abstract  class ValuableModifier<In, Out> implements Valuable<Out>, SimpleRenderable {
	
	private static final long serialVersionUID = -5061705048710723500L;
	protected Valuable<In> value;
	
	@Override
	public IRenderer getRenderer() {
		return new SimpleBlockRenderer(this);
	}
	
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
		value = AbstractLiteral.getDefault(value.value(), this);
		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		return (LiteralRenderable<?>) (value = AbstractLiteral.getDefault(value.value(), this));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		value = (Valuable<In>) newValue;
		
	}
	
	@Override
	public void reset() {
		value.reset();
	}

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.OPERATOR;
	}
	
	@Override
	public void getImports(Set<String> imports) {
		value.getImports(imports);
	}

}
