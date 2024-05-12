package domain.blocks.operators;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.types.FunctionBlock;
import domain.values.AbstractLiteral;
import domain.values.EnumLiteral;
import domain.values.Variable;
import ui.renderers.IRenderer;
import ui.renderers.LiteralRenderer;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class SetValueBlock extends FunctionBlock {

	private static final long serialVersionUID = -1072262290642640507L;
	
	private EnumLiteral<Variable<?>> variables = Variable.getEnumLiteral();
	private Valuable<?> value;

	public SetValueBlock(Sprite s) {
		super(s);
		value = (AbstractLiteral<?>) AbstractLiteral.getDefault(variables.value());
		variables.setValueListener((_s, v) -> {
			if(value instanceof AbstractLiteral al)LiteralRenderer.of(al, al.value(), null).delete();
			else IRenderer.getDragableRendererOf(value).delete();
			value = (AbstractLiteral<?>) AbstractLiteral.getDefault(variables.value());
		});
	}

	@Override
	public String getTitle() {
		String type = switch(variables.value().value()) {
			case String s -> VARIABLE_STR;
			case Number n -> VARIABLE_NUM;
			case Boolean b -> VARIABLE_BOOL;
			default -> VARIABLE_ANY;
		};
		return "Set " + VARIABLE_ENUM + " to " + type;
	}

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.VARIABLE;
	}

	@Override
	public String getCode() {
		Variable<?> var = variables.value();
		return var.isGlobal()?"GlobalVariables." + var.name + " = " + value.getCode()+";":"this." + var.name + " = " + value.getCode()+";";
	}

	@Override
	public void getImports(Set<String> imports) {
		value.getImports(imports);
		
	}

	@Override
	public Translatable create(Sprite s) {
		return new SetValueBlock(s);
	}

	@Override
	public Valuable<?> getVariableAt(int i) {
		return i == 0? variables : value;
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable<?>[] {variables, value};
	}

	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		value = v;
		
	}

	@Override
	public void removeVariableAt(int i) {
		LiteralRenderer.of((LiteralRenderable<?>)value, value.value(), null).delete();
		value = (AbstractLiteral<?>) AbstractLiteral.getDefault(variables.value());
		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		LiteralRenderer.of((LiteralRenderable<?>)value, value.value(), null).delete();
		return (LiteralRenderable<?>) (value = (AbstractLiteral<?>) AbstractLiteral.getDefault(variables.value()));
	}

	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		if(old instanceof AbstractLiteral al)LiteralRenderer.of(al, al.value(), null).delete();
		value = newValue;
		
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return value.value().getClass().isInstance(v.value()) || value.value() instanceof Number && v.value() instanceof Number;
	}

	@Override
	public void invoke() {
		variables.value().setValue(value.value(), false);
	}

	@Override
	public void reset() {
		value.reset();
	}

}
