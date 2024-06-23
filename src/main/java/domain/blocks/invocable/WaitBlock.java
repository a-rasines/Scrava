package domain.blocks.invocable;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.types.FunctionBlock;
import domain.values.NumberLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class WaitBlock extends FunctionBlock{

	private static final long serialVersionUID = 8797563671898934528L;
	private LiteralRenderable<Long> initialValue = new NumberLiteral<>(0l, this);
	private Valuable<? extends Number> value = initialValue;
	
	public WaitBlock(Sprite s) {
		super(s);
	}
	
	public WaitBlock() {
		this(null);
	}

	@Override
	public String getTitle() {
		return "Wait for " + VARIABLE_NUM + " ms";
	}

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.CONTROL;
	}

	@Override
	public String getCode() {
		return "try { Thread.sleep(" + value.getCode() + "); } catch (InterruptedException e) {}";
	}

	@Override
	public void getImports(Set<String> imports) {
		value.getImports(imports);
	}

	@Override
	public Translatable create(Sprite s) {
		return new WaitBlock(s);
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
		return new Valuable<?>[] {value};
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		value = (Valuable<? extends Number>) v;
	}

	@Override
	public void removeVariableAt(int i) {
		value = initialValue;
		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		value = initialValue;
		return initialValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		value = (Valuable<? extends Number>) newValue;
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof Long || v.value() instanceof Integer || v.value() instanceof Short || v.value() instanceof Byte;
	}

	@Override
	public void invoke() {
		try { Thread.sleep(value.value().longValue()); } catch (InterruptedException e) {}
	}

}
