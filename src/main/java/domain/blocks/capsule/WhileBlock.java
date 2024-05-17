package domain.blocks.capsule;

import java.util.function.Supplier;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.models.types.CapsuleBlock;
import domain.values.BooleanLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class WhileBlock extends CapsuleBlock{
	private static final long serialVersionUID = 6422542828725824534L;
	
	@Override
	public WhileBlock create(Sprite s) {
		return new WhileBlock();
	}

	private Valuable<Boolean> condition;
	private Valuable<Boolean> defCondition;
	
	public WhileBlock() {
		this(new BooleanLiteral(true));
	}
	
	public WhileBlock(Valuable<Boolean> condition) {
		this.condition = condition;
		this.defCondition = condition;
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
	public Supplier<Boolean> getTick() {
		return condition.value()? new Tick(()->condition.value()) : SKIP_TICK;
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
		return new Valuable[] {condition};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		condition = (Valuable<Boolean>) v;
	}

	@Override
	public void removeVariableAt(int i) {
		this.condition = this.defCondition;
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		this.condition = this.defCondition;
		return (LiteralRenderable<?>) this.condition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		this.condition = (Valuable<Boolean>) newValue;
		
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
