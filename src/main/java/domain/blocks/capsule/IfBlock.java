package domain.blocks.capsule;

import java.util.function.Supplier;

import domain.Sprite;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.types.CapsuleBlock;
import domain.values.BooleanLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class IfBlock extends CapsuleBlock {
	private static final long serialVersionUID = 6422542828725824534L;
	
	private Valuable<Boolean> defVal = new BooleanLiteral(true);
	private Valuable<Boolean> condition;
	
	@Override
	public IfBlock create(Sprite s) {
		return new IfBlock();
	}
	
	public IfBlock() {
		this.condition = new BooleanLiteral(true);
	}
	
	public IfBlock(Valuable<Boolean> condition) {
		this.condition = condition;
	}
	
	public IfBlock(Valuable<Boolean> condition, InvocableBlock... initialValues) {
		this.condition = condition;
		addAll(initialValues);
	}
	
	public IfBlock(InvocableBlock... initialValues) {
		this.condition = new BooleanLiteral(true);
		addAll(initialValues);
	}

	public void addAll(InvocableBlock...blocks) {
		for(InvocableBlock ib : blocks)
			add(ib);
	}
	
	public Valuable<Boolean> getCondition() {
		return condition;
	}

	public void setCondition(Valuable<Boolean> condition) {
		this.condition = condition;
	}

	@Override
	public void invoke() {
		if(condition.value())
			super.invoke();
		
	}

	@Override
	public String getTitle() {
		return "If "+VARIABLE_BOOL+" then";
	}

	@Override
	public Valuable<Boolean> getVariableAt(int q) {
		return condition;
	}

	@Override
	public String getHead() {
		return "if("+condition.getCode()+")";
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {condition};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		condition = (Valuable<Boolean>) v;		
	}

	@Override
	public void removeVariableAt(int i) {
		condition = defVal;		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		System.out.println("a");
		condition = defVal;
		return (LiteralRenderable<?>) condition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		condition = (Valuable<Boolean>) newValue;
		
	}
	
	@Override
	public Supplier<Boolean> getTick() {
		return condition.value()?new Tick() : () -> true;
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof Boolean;
	}

	@Override
	public boolean attachable() {
		return true;
	}

}
