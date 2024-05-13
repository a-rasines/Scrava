package domain.blocks.event;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.EventBlock;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class OnKeyPressEventBlock extends KeyEventBlock {

	private static final long serialVersionUID = 5804391234476797763L;
	
	
	@Override
	public Valuable<?> getVariableAt(int i) {
		return KEY;
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable<?>[] {KEY};
	}

	@Override
	public void setVariableAt(int i, Valuable<?> v) {} // N/A

	@Override
	public void removeVariableAt(int i) {}  // N/A

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {	return null;}

	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {} 

	@Override
	public boolean isAplicable(Valuable<?> v) {	return false;}

	@Override
	public EventBlock newInstance(Sprite s) {
		return new OnKeyPressEventBlock();
	}

	@Override
	public String getTitle() {
		return "On " + VARIABLE_ENUM + " press";
	}

}
