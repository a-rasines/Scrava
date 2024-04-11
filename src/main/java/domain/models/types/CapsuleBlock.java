package domain.models.types;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.Set;

import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Translatable;
import domain.models.interfaces.VariableHolder;
import ui.renderers.CapsuleBlockRenderer;
import ui.renderers.IRenderer.IRenderable;

/**
 * This class represents an abstract {@link domain.models.interfaces.InvocableBlock InvocableBlock} that can contain other {@link domain.models.interfaces.InvocableBlock InvocableBlock}s inside
 */
public abstract class CapsuleBlock extends LinkedList<InvocableBlock> implements IRenderable, Translatable, InvocableBlock, VariableHolder {
	private static final long serialVersionUID = 3038954472222407623L;
	
	public abstract String getTitle();
	public Constructor<CapsuleBlockRenderer> getRenderer() throws NoSuchMethodException, SecurityException {
		return CapsuleBlockRenderer.class.getConstructor(CapsuleBlock.class);
	}
	
	@Override
	public void invoke() {
		for(InvocableBlock ib : this)
			ib.invoke();
	}
	
	public abstract String getHead();
	
	@Override
	public String getCode() {
		StringBuilder s = new StringBuilder(" {\n");
		forEach(v -> s.append("\t" + v.getCode().replaceAll("\n", "\n\t")
				+ "\n"));
		return getHead() + s.toString() + "}";
	}
	
	@Override
	public void getImports(Set<String> imports) {
		forEach(v -> v.getImports(imports));	
	}
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode());
	}
	
	public abstract boolean attachable();
}
