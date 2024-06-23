package domain.blocks.capsule;

import java.util.ArrayList;
import java.util.List;

import domain.Sprite;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.types.MultipleOptionCapsuleBlock;
import domain.values.BooleanLiteral;

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


	@Override
	public int getInvocationObjectIndex() {
		return (boolean) getVariableAt(0).value()?0:1;
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
		return List.of(new ArrayList<>(List.of(defCondition)), new ArrayList<>());
	}

	@Override
	public String[] getHeads() {
		return new String[] {"if (" + getVariableAt(0).getCode() + ") ", " else"};
	}
}
