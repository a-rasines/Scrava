package domain.blocks.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.types.MultipleOptionCapsuleBlock;
import domain.values.BooleanLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class IfElseBlock extends MultipleOptionCapsuleBlock{
	private static final long serialVersionUID = 3713535728781224292L;
	
	public IfElseBlock() {
		super("If "+VARIABLE_BOOL, "else");
	}
	
	public IfElseBlock(List<InvocableBlock> first, List<InvocableBlock> second) {
		this();
		get(0).addAll(first);
		get(1).addAll(second);
	}
	
	private Valuable<Boolean> condition;
	private Valuable<Boolean> defCondition;

	@Override
	public void getImports(Set<String> imports) {
		forEach(v-> v.getImports(imports));
		
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
		if(defCondition == null) { //Constructor hierarchy, I don't have the patience to fix it
			defCondition = new BooleanLiteral(true);
			condition = defCondition;
		}
		return List.of(new ArrayList<>(List.of(condition)), new ArrayList<>());
	}

	@Override
	public String[] getHeads() {
		return new String[] {"if (" + condition.getCode() + ") ", " else"};
	}
}
