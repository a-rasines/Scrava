package domain.blocks.capsule;

import java.util.Set;
import java.util.function.Supplier;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.types.CapsuleBlock;
import domain.values.NumberLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class RepeatBlock extends CapsuleBlock{

	private static final long serialVersionUID = -1027255896386562621L;

	LiteralRenderable<? extends Number> defVariable = new NumberLiteral<Long>(0l, this);
	Valuable<? extends Number> variable = defVariable;
	
	@Override
	public Translatable create(Sprite s) {
		return new RepeatBlock();
	}

	@Override
	public Valuable<?> getVariableAt(int i) {
		return variable;
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable<?>[] {variable};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		variable = (Valuable<Long>) v;
		
	}

	@Override
	public void removeVariableAt(int i) {
		variable = defVariable;
		
	}

	@Override
	public LiteralRenderable<? extends Number> removeVariable(Valuable<?> v) {
		variable = defVariable;
		return defVariable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		variable = (Valuable<Long>) newValue;
		
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof Number && !((v.value() instanceof Float) || (v.value() instanceof Double));
	}

	@Override
	public String getTitle() {
		return "Repeat " + VARIABLE_NUM + " times";
	}

	@Override
	public String getHead() {
		return "for(" + variable.value().getClass().getSimpleName() + " i = 0; i < " + variable.value() + "; i++) {";
	}
	
	@Override
	public Supplier<Boolean> getTick() {
		return variable.value().longValue() < 1 ? SKIP_TICK : new Tick(new RepeatCondition());
	}
	
	public class RepeatCondition implements Supplier<Boolean>{
		long count = variable.value().longValue();
		
		public Boolean get() {
			return --count > 0;

		}
	}
	@Override
	public void getImports(Set<String> imports) {
		super.getImports(imports);
		variable.getImports(imports);
	}

	@Override
	public boolean attachable() {
		return true;
	}

}
