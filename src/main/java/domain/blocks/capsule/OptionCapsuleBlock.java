package domain.blocks.capsule;

import java.util.ArrayList;
import java.util.List;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.Valuable;
import domain.models.types.CapsuleBlock;
import ui.renderers.LiteralRenderer.LiteralRenderable;

/**
 * This class represents the capsule block of a choice in the multiple 
 */
public class OptionCapsuleBlock extends CapsuleBlock {

	private static final long serialVersionUID = 1L;
	private String title;
	public final List<Valuable<?>> variables;
	public final List<Valuable<?>> defVariables;
	private final boolean attachable;
	
	public OptionCapsuleBlock(String title, boolean attachable, List<Valuable<?>> variables) {
		this.title = title;
		this.variables = variables;
		this.defVariables = new ArrayList<>(variables);
		this.attachable = attachable;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Valuable<?> getVariableAt(int q) {
		return variables.get(q);
	}

	@Override
	public String getHead() {
		return "";
	}

	@Override
	public Valuable<?>[] getAllVariables() {
		Valuable<?>[] output = new Valuable[variables.size()];
		for(int i = 0; i < variables.size(); i++)
			output[i] = variables.get(i);
		return output;
	}

	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		variables.set(i, v);			
	}

	@Override
	public void removeVariableAt(int i) {
		variables.set(i, defVariables.get(i));
	}
	

	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		int i = variables.indexOf(v);
		if(i < 0) return null;
		variables.set(i, defVariables.get(i));
		return (LiteralRenderable<?>) variables.get(i);
	}

	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		if(variables.contains(old))
			variables.set(variables.indexOf(old), newValue);
		
	}

	@Override
	public boolean isAplicable(Valuable<?> v) {
		return v.value() instanceof Boolean;
	}

	@Override
	public boolean attachable() {
		return attachable;
	}

	@Override
	public Translatable create(Sprite s) {return null;} // N/A
}
