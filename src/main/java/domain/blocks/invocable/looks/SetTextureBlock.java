package domain.blocks.invocable.looks;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.types.FunctionBlock;
import domain.values.NumberLiteral;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class SetTextureBlock extends FunctionBlock {
	private static final long serialVersionUID = 8719151648387710351L;

	private LiteralRenderable<Integer> defaultValue = new NumberLiteral<>(0, this);
	private Valuable<? extends Number> value = defaultValue;
	public SetTextureBlock(Sprite s) {
		super(s);
	}

	@Override
	public String getTitle() {
		return "Change to the " + VARIABLE_NUM + "th texture";
	}

	@Override
	public BlockCategory getCategory() {
		return BlockCategory.LOOKS;
	}

	@Override
	public String getCode() {
		return "this.changeTexture(" + value.getCode() + ");";
	}

	@Override
	public void getImports(Set<String> imports) {
		value.getImports(imports);
	}

	@Override
	public Translatable create(Sprite s) {
		return new SetTextureBlock(s);
	}

	@Override
	public void reset() {
		getSprite().setSelectedTexture(0);
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
		value = defaultValue;
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		value = defaultValue;
		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		value = (Valuable<? extends Number>) newValue;
		
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		Object o = v.value();
		return o instanceof Integer || o instanceof Short || o instanceof Byte;
	}

	@Override
	public void invoke() {
		getSprite().setSelectedTexture(value.value().intValue());
	}

}
