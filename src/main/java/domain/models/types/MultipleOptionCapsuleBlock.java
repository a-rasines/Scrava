package domain.models.types;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import domain.blocks.capsule.OptionCapsuleBlock;
import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import ui.renderers.IRenderer.IRenderable;
import ui.renderers.LiteralRenderer.LiteralRenderable;
import ui.renderers.MultipleChoiceRenderer;

public abstract class MultipleOptionCapsuleBlock extends ArrayList<OptionCapsuleBlock> implements IRenderable, InvocableBlock, VariableHolder {
	private static final long serialVersionUID = 8958382657668628005L;

	private MultipleChoiceRenderer mcr;
	@Override
	public MultipleChoiceRenderer getRenderer() {
		return mcr;
	}
	
	protected void setup(String... titles) {
		for(int i = 0; i < titles.length; i++) {
			add(new OptionCapsuleBlock(titles[i], attachable(), getVariables().get(i)));
		}
		mcr = new MultipleChoiceRenderer(this);
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		List<Valuable<?>> variables = new LinkedList<>();
		for(OptionCapsuleBlock ocb : this)
			variables.addAll(List.of(ocb.getAllVariables()));
		return variables.toArray(new Valuable<?>[variables.size()]);
	}

	@Override
	public String getCode() {
		String res = "";
		for(int i = 0; i < size(); i++) {
			res += getHeads()[i] + get(i).getCode();
		}
		return res;
	}
	
	@Override
	public void getImports(Set<String> imports) {
		forEach(v -> v.getImports(imports));
	}

	@Override
	public void invoke() {
		Integer o = getInvocationObjectIndex();
		if(o == null || o < 0) return;
		get(o).invoke();
	}	
	
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		for(OptionCapsuleBlock ocb : this)
			ocb.replaceVariable(old, newValue);
	}
	
	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		LiteralRenderable<?> out;
		for(OptionCapsuleBlock ocb : this) {
			out = ocb.removeVariable(v);
			if(out != null)
				return out;
		}
		return null;
	}
	
	@Override
	public void removeVariableAt(int i) {
		removeVariable(getAllVariables()[i]);
	}
	
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		replaceVariable(getAllVariables()[i], v);
	}
	
	@Override
	public Valuable<?> getVariableAt(int i) {
		return getAllVariables()[i];
	}
	
	
	@Override
	public Supplier<Boolean> getTick() {
		return get(getInvocationObjectIndex()).getTick();
	}
	
	@Override
	public void reset() {
		for(Valuable<?> v : getAllVariables())
			v.reset();
		for(OptionCapsuleBlock ocb : this) ocb.reset();
	}
	
	/**
	 * Returns the index of the capsule to execute
	 * @return
	 */
	public abstract int getInvocationObjectIndex();
	
	/**
	 * Returns whether this block can have blocks before and after or it's an event block
	 * @return
	 */
	public abstract boolean attachable();
	
	public abstract List<List<Valuable<?>>> getVariables();
	
	public abstract String[] getHeads();
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}

}
