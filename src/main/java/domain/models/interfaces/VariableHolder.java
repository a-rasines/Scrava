package domain.models.interfaces;

import ui.renderers.LiteralRenderer.LiteralRenderable;

public interface VariableHolder {
	public Valuable<?> getVariableAt(int i);
	public Iterable<? extends Valuable<?>> getAllVariables();
	public void setVariableAt(int i, Valuable<?> v);
	public void removeVariableAt(int i);
	public LiteralRenderable<?> removeVariable(Valuable<?>v);
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue);
	public boolean isAplicable(Valuable<?> v);
}
