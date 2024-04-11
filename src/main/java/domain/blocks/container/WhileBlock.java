package domain.blocks.container;

import domain.models.interfaces.Valuable;
import domain.models.types.CapsuleBlock;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class WhileBlock extends CapsuleBlock{
private static final long serialVersionUID = 6422542828725824534L;
	
	private Valuable<Boolean> condition;
	public WhileBlock(Valuable<Boolean> condition) {
		this.condition = condition;
	}
	
	public Valuable<Boolean> getCondition() {
		return condition;
	}

	public void setCondition(Valuable<Boolean> condition) {
		this.condition = condition;
	}

	@Override
	public void invoke() {
		while(condition.value())
			super.invoke();
		
	}

	@Override
	public String getTitle() {
		return "While "+VARIABLE_BOOL+" do";
	}

	@Override
	public Valuable<Boolean> getVariableAt(int q) {
		return condition;
	}

	@Override
	public String getHead() {
		return "while("+condition.getCode()+")";
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeVariableAt(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean attachable() {
		return true;
	}
}
