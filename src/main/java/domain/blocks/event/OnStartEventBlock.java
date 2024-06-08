package domain.blocks.event;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.EventBlock;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class OnStartEventBlock extends EventBlock {

	private static final long serialVersionUID = -2162143069703779118L;

	@Override
	public EventBlock newInstance(Sprite s) {
		return new OnStartEventBlock();
	}

	@Override
	public String getTitle() {
		return "On start";
	}
	
	@Override
	public Valuable<?> getVariableAt(int i) {
		return null;
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[0];
	}

	@Override
	public void setVariableAt(int i, Valuable<?> v) {}

	@Override
	public void removeVariableAt(int i) {}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {return null;}

	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {}

	@Override
	public boolean isAplicable(Valuable<?> v) {return false;}
	
	@Override
	public String getHead() {
		return "public void onStart_" + Integer.toHexString(hashCode()) + "()";
	}

}
