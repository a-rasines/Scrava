package domain.blocks.capsule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.types.MultipleOptionCapsuleBlock;
import domain.values.BooleanLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class IfElseBlock extends MultipleOptionCapsuleBlock{
	private static final long serialVersionUID = 3713535728781224292L;
	
	@Override
	public IfElseBlock create(Sprite s) {
		return new IfElseBlock();
	}
	
	public IfElseBlock() {
		setup("If "+VARIABLE_BOOL, "else");
	}
	
	public IfElseBlock(List<InvocableBlock> first, List<InvocableBlock> second) {
		this();
		get(0).addAll(first);
		get(1).addAll(second);
	}
	
	private Valuable<Boolean> defCondition = new BooleanLiteral(true, this);
	private Valuable<Boolean> condition = defCondition;

	@Override
	public void getImports(Set<String> imports) {
		forEach(v-> v.getImports(imports));
		condition.getImports(imports);
		
	}


	@Override
	public int getInvocationObjectIndex() {
		return condition.value()?0:1;
	}

	@Override
	public Valuable<Boolean> getVariableAt(int i) {
		return condition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		condition = (Valuable<Boolean>) v;		
	}

	@Override
	public void removeVariableAt(int i) {
		condition = defCondition;		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		removeVariableAt(0);
		return (LiteralRenderable<?>) defCondition;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		condition = (Valuable<Boolean>) newValue;
		
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof Boolean;
	}

	@Override
	public boolean attachable() {
		return true;
	}

	@Override
	public List<List<Valuable<?>>> getVariables() {
		return List.of(new ArrayList<>(List.of(condition)), new ArrayList<>());
	}

	@Override
	public String[] getHeads() {
		return new String[] {"if (" + condition.getCode() + ") ", " else"};
	}
}
