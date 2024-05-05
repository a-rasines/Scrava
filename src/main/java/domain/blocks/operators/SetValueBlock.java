package domain.blocks.operators;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.types.FunctionBlock;
import ui.renderers.LiteralRenderer.LiteralRenderable;

public class SetValueBlock extends FunctionBlock {

	private static final long serialVersionUID = -1072262290642640507L;

	protected SetValueBlock(Sprite s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockCategory getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getImports(Set<String> imports) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Translatable create(Sprite s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Valuable<?> getVariableAt(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeVariableAt(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		
	}

}
