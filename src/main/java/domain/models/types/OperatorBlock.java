package domain.models.types;

import java.util.Set;

import org.apache.batik.anim.dom.SVGOMGElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import domain.models.interfaces.Valuable;
import domain.values.AbstractLiteral;
import ui.renderers.IRenderer.DragableRenderer;
import ui.renderers.IRenderer.IRenderable;
import ui.renderers.LiteralRenderer.LiteralRenderable;
import ui.renderers.SimpleBlockRenderer;
import ui.renderers.SimpleBlockRenderer.SimpleRenderable;

public abstract class OperatorBlock<T, R> implements SimpleRenderable<R> {
	
	private SimpleBlockRenderer sbr;
	@Override
	public DragableRenderer getRenderer() {
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
	public final OperatorBlock<T, R> setLeft(Valuable<? extends T> v) {
		if(v == this)
			throw new IllegalArgumentException("Block cannot contain itself");
		this.values[0] = v;
		return this;
	}
	
	public final OperatorBlock<T, R> setRight(Valuable<? extends T> v) {
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
			setVariableAt(0, v);
			return (LiteralRenderable<T>)values[0];
		} else if (values[1].equals(v)) {
			setVariableAt(0, v);
			return (LiteralRenderable<T>)values[1];
		}
		return null;
	}
	@Override
	public void removeVariableAt(int i) {
		if (i == 0)
			setVariableAt(0, defs[0]);
		else
			setVariableAt(1, defs[1]);
	}
	
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		if(values[0].equals(old)) {
			setVariableAt(0, newValue);
		} else if (values[1].equals(old)) {
			setVariableAt(1, newValue);
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		// Backend variable change
		IRenderable original = values[i];
		values[i] = (Valuable<? extends T>) v;
		
		//Frontend variable change
		Element documentElement = getRenderer().getRenderableSVG().getOwnerDocument().getDocumentElement();
		NodeList nl = getRenderer().getRenderableSVG().getChildNodes();
		for(int j = 0; j < nl.getLength(); j++) {
			Node n = nl.item(j);
			if(n instanceof SVGOMGElement g) {
				nl = g.getChildNodes();
				for(j = 0; j < nl.getLength(); j++)
					if((n = nl.item(j)) instanceof Element e && e.getAttribute("id").equals(original.hashCode() + "_root")) {
						documentElement.appendChild(e);
						g.appendChild(v.getRenderer().getRenderableSVG());
						return;
					}
			}
		}
	}
	
	@Override
	public void reset() {
		for(Valuable<?> v : values) v.reset();
	}
	
	public OperatorBlock<T, R>setValues(Valuable<? extends T> left, Valuable<? extends T> right) {
		// Backend variable change
		values = new Valuable[] {left, right};
		
		//Frontend variable change
		Element documentElement = getRenderer().getRenderableSVG().getOwnerDocument().getDocumentElement();
		NodeList nl = getRenderer().getRenderableSVG().getChildNodes();
		for(int j = 0; j < nl.getLength(); j++) {
			Node n = nl.item(j);
			if(n instanceof SVGOMGElement g) {
				nl = g.getChildNodes();
				for(j = 0; j < nl.getLength(); j++) {
					if((n = nl.item(j)) instanceof Element e && e.getAttribute("id").endsWith("_root"))
						documentElement.appendChild(e);
				}
				g.appendChild(left.getRenderer().getRenderableSVG());
				g.appendChild(right.getRenderer().getRenderableSVG());
				return this;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}
}
