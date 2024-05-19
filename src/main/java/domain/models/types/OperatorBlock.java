package domain.models.types;

import java.util.Set;

import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import ui.renderers.IRenderer;
import ui.renderers.LiteralRenderer.LiteralRenderable;
import ui.renderers.SimpleBlockRenderer;
import ui.renderers.SimpleBlockRenderer.SimpleRenderable;

public abstract class OperatorBlock<T, R> implements Valuable<R>, SimpleRenderable {
	
	private SimpleBlockRenderer sbr;
	@Override
	public IRenderer getRenderer() {
		return sbr;
	}

	private static final long serialVersionUID = 1979700927393729000L;
	@SuppressWarnings("rawtypes")
	protected Valuable[] values;
	@SuppressWarnings("rawtypes")
	protected Valuable[] defs;
	
	protected OperatorBlock() {
		sbr = new SimpleBlockRenderer(this);
	}
	
	protected void setup(Valuable<? extends T> left, Valuable<? extends T> right) {
		this.values = new Valuable[]{left, right};
		this.defs = new Valuable[]{left, right};
	}
	public final OperatorBlock<T, R> setLeft(Valuable<T> v) {
		if(v == this)
			throw new IllegalArgumentException("Block cannot contain itself");
		this.values[0] = v;
		return this;
	}
	
	public final OperatorBlock<T, R> setRight(Valuable<T> v) {
		this.values[1] = v;
		return this;
	}
	
	public boolean isAplicable(Valuable<?> a) {
		return checkVariable(values[0], a)
			   && checkVariable(values[1], a)
			   || values[0]  == null && values[1] == null;
	}
	
	private boolean checkVariable(Valuable<?> th, Valuable<?> in) {
		return th != null 
				&& (values[0].value().getClass().isInstance(in.value()) ||
				   th instanceof AbstractLiteral<?> && ((AbstractLiteral<?>)th).isEmpty()); 
	}
	
	public abstract R value(Valuable<? extends T> left, Valuable<? extends T> right);
	
	@SuppressWarnings("unchecked")
	@Override
	public final R value() {
		return (R) value(values[0], values[1]);
	}
	
	public abstract String getCode(Valuable<? extends T> left, Valuable<? extends T> right);
	
	@SuppressWarnings("unchecked")
	public final String getCode() {
		return getCode(values[0], values[1]);
	}
	@Override
	public BlockCategory getCategory() {
		return BlockCategory.OPERATOR;
	}

	@Override
	public void getImports(Set<String> imports) {
		values[0].getImports(imports);
		values[1].getImports(imports);
	}
	
	@Override
	public final Valuable<?> getVariableAt(int q) {
		return values[q];
	}
	@Override
	public Valuable<?>[] getAllVariables() {
		return values;
	}
	
	@SuppressWarnings("unchecked")
	public Valuable<T> getLeft() {
		return values[0];
	}
	@SuppressWarnings("unchecked")
	public Valuable<T> getRight() {
		return values[1];
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		if(values[0].equals(v)) {
			values[0] = defs[0];
			return (LiteralRenderable<T>)values[0];
		} else if (values[1].equals(v)) {
			values[1] = defs[1];
			return (LiteralRenderable<T>)values[1];
		}
		return null;
	}
	@Override
	public void removeVariableAt(int i) {
		if (i == 0)
			values[0] = defs[0];
		else
			values[1] = defs[1];
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		if(values[0].equals(old)) {
			values[0] = (Valuable<T>)newValue;
		} else if (values[1].equals(old)) {
			values[1] = (Valuable<T>)newValue;
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		if(i == 0)
			values[0] = (Valuable<? extends T>) v;
		else
			values[1] = (Valuable<? extends T>) v;
	}
	
	@Override
	public void reset() {
		for(Valuable<?> v : values) v.reset();
	}
	
	public OperatorBlock<T, R>setValues(Valuable<? extends T> left, Valuable<? extends T> right) {
		values = new Valuable[] {left, right};
		return this;
	}
}
