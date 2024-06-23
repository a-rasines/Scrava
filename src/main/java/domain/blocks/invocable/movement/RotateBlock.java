package domain.blocks.invocable.movement;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.types.FunctionBlock;
import domain.values.NumberLiteral;
import domain.values.StaticVariable;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class RotateBlock extends FunctionBlock {
	
	private static final long serialVersionUID = -6261983534566408161L;
	private NumberLiteral<Double> initialValue = new NumberLiteral<Double>(0., this);
	private Valuable<Double> value = initialValue;

	public RotateBlock(Sprite s) {
		super(s);
	}

	@Override
	public String getTitle() {
		return "Rotate " + VARIABLE_NUM + "º";
	}

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.MOVEMENT;
	}

	@Override
	public String getCode() {
		return "this.rotateDeg(" + value.getCode() + ");";
	}

	@Override
	public void getImports(Set<String> imports) {
		value.getImports(imports);
		
	}

	@Override
	public Translatable create(Sprite s) {
		return new RotateBlock(s);
	}

	@Override
	public void reset() {
		value.reset();
	}

	@Override
	public Valuable<?> getVariableAt(int i) {
		return value;
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {value};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		value = (Valuable<Double>) v;
	}

	@Override
	public void removeVariableAt(int i) {
		value = initialValue;
		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		removeVariableAt(0);
		return initialValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		value = (Valuable<Double>) newValue;
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof Double;
	}

	@Override
	public void invoke() {
		StaticVariable<Double> sv = getSprite().getRotation(); 
		sv.setValue(sv.value() + value.value() % 360);		
	}

}
