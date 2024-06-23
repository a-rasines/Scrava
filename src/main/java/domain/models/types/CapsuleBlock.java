package domain.models.types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Supplier;

import domain.models.interfaces.InvocableBlock;
import domain.models.interfaces.Valuable;
import domain.models.interfaces.VariableHolder;
import ui.renderers.CapsuleBlockRenderer;
import ui.renderers.CapsuleRenderer;
import ui.windows.ProjectFrame;

/**
 * This class represents an abstract {@link domain.models.interfaces.InvocableBlock InvocableBlock} that can contain other {@link domain.models.interfaces.InvocableBlock InvocableBlock}s inside
 */
public abstract class CapsuleBlock extends LinkedList<InvocableBlock> implements InvocableBlock, VariableHolder {
	private static final long serialVersionUID = 3038954472222407623L;
	
	public CapsuleBlock() {
		cr = new CapsuleBlockRenderer(this);
	}
	
	/**
	 * Returns the title to render of the capsule block
	 * @return
	 */
	public abstract String getTitle();
	private CapsuleRenderer cr;
	@Override
	public CapsuleRenderer getRenderer() {
		return cr;
	}
	
	@Override
	public void invoke() {
		for(InvocableBlock ib : this)
			if(ProjectFrame.isStarted)
				ib.invoke();
			else
				return;
	}
	@Override
	public Supplier<Boolean> getTick() {
		return new Tick();
	}
	public class Tick implements Supplier<Boolean>{
		private static final Supplier<Boolean> END_TICK = () -> false;
		protected Iterator<InvocableBlock> it;
		private Supplier<Boolean> actualBlock = null;
		private Supplier<Boolean> endCondition;
		public Tick(Supplier<Boolean> endCondition) {
			this.it = iterator();
			this.endCondition = endCondition;
		}
		public Tick() {
			this(END_TICK);
		}
		@Override
		public Boolean get() {
			System.out.println(CapsuleBlock.this.toString());
			if(actualBlock == null)
				actualBlock = it.next().getTick();
			if(actualBlock.get())
				actualBlock = null;
			System.out.println("get:" + (actualBlock==null));
			if(!it.hasNext() && actualBlock == null)
				if(endCondition.get()) {
					System.out.println("reset");
					it = iterator();
				} else {
					System.out.println("end");
					return true;
				}
			return false;
		}
		
	}
	
	
	/**
	 * Returns the wrapper string in java code.
	 * 
	 * Example: if(*condition*) {
	 * @return
	 */
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
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}
	
	public abstract boolean attachable();
	
	@Override
	public void reset() {
		for(InvocableBlock ib : this)
			ib.reset();
		for(Valuable<?> v : getAllVariables())
			v.reset();
	}
	
	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}
}
