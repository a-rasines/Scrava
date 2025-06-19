package domain.models.types;

import java.util.Set;

import org.apache.batik.anim.dom.SVGOMGElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import domain.models.interfaces.Valuable;
import domain.util.Pair;
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
	protected Pair<Valuable<? extends T>> values;
	protected Pair<Valuable<? extends T>> defs;
	
	protected OperatorBlock() {
		sbr = new SimpleBlockRenderer(this);
	}
	
	protected void setup(Valuable<? extends T> left, Valuable<? extends T> right) {
		this.values = new Pair<>(left, right);
		this.defs = this.values.clone();
	}
	public final OperatorBlock<T, R> setLeft(Valuable<? extends T> v) {
		if(v == this)
			throw new IllegalArgumentException("Block cannot contain itself");
		setVariableAt(0, v);
		return this;
	}
	
	public final OperatorBlock<T, R> setRight(Valuable<? extends T> v) {
		if(v == this)
			throw new IllegalArgumentException("Block cannot contain itself");
		setVariableAt(1, v);
		return this;
	}
	
	public boolean isAplicable(Valuable<?> a) {
		return checkVariable(values.getLeft(), a)
			   && checkVariable(values.getRight(), a)
			   || values.getLeft()  == null && values.getRight() == null;
	}
	
	private boolean checkVariable(Valuable<?> th, Valuable<?> in) {
		return th != null 
				&& (values.getLeft().value().getClass().isInstance(in.value()) ||
				   th instanceof AbstractLiteral<?> && ((AbstractLiteral<?>)th).isEmpty()); 
	}
	
	public abstract R value(Valuable<? extends T> left, Valuable<? extends T> right);
	
	@Override
	public final R value() {
		return (R) value(values.getLeft(), values.getRight());
	}
	
	public abstract String getCode(Valuable<? extends T> left, Valuable<? extends T> right);
	
	public final String getCode() {
		return getCode(values.getLeft(), values.getRight());
	}
	@Override
	public BlockCategory getCategory() {
		return BlockCategory.OPERATOR;
	}

	@Override
	public void getImports(Set<String> imports) {
		for (Valuable<? extends T> v : values)
			v.getImports(imports);
	}
	
	@Override
	public final Valuable<? extends T> getVariableAt(int q) {
		return values.get(q);
	}
	@Override
	public Iterable<Valuable<? extends T>> getAllVariables() {
		return values.clone();
	}
	
	public Valuable<? extends T> getLeft() {
		return values.getLeft();
	}
	
	public Valuable<? extends T> getRight() {
		return values.getRight();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LiteralRenderable<?> removeVariable(Valuable<?> v) {
		int index = values.indexOf((Valuable<? extends T>)v);
		setVariableAt(index, defs.get(index));
		return (LiteralRenderable<?>) values.get(index);
	}
	@Override
	public void removeVariableAt(int i) {
		setVariableAt(i, defs.get(i));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void replaceVariable(Valuable<?> old, Valuable<?> newValue) {
		setVariableAt(values.indexOf((Valuable<? extends T>)old), newValue);		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setVariableAt(int i, Valuable<?> v) {
		// Backend variable change
		
		IRenderable original = values.get(i);
		values.setAt(i, (Valuable<? extends T>) v);
		
		//Frontend variable change
		Element documentElement = getRenderer().getRenderableSVG().getOwnerDocument().getDocumentElement();
		NodeList thisChildNodeList = getRenderer().getRenderableSVG().getChildNodes();
		v.getRenderer().toDocument(getRenderer().getRenderableSVG().getOwnerDocument());
		
		for(int j = 0; j < thisChildNodeList.getLength(); j++) {
			Node thisChildNode = thisChildNodeList.item(j);
			if(thisChildNode instanceof SVGOMGElement childGElement) {
				thisChildNodeList = childGElement.getChildNodes();
				for(j = 0; j < thisChildNodeList.getLength(); j++)
					if((thisChildNode = thisChildNodeList.item(j)) instanceof Element thisChildElement && thisChildElement.getAttribute("id").equals(original.hashCode() + "_root")) {
						childGElement.insertBefore(v.getRenderer().getRenderableSVG(), thisChildElement);
						documentElement.appendChild(thisChildElement);
						getRenderer().updateSVG();
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
		values = new Pair<Valuable<? extends T>>(left, right);
		int child = 0;
		
		//Frontend variable change
		Element documentElement = getRenderer().getRenderableSVG().getOwnerDocument().getDocumentElement();
		NodeList nl = getRenderer().getRenderableSVG().getChildNodes();
		for(int j = 0; j < nl.getLength(); j++) {
			Node n = nl.item(j);
			if(n instanceof SVGOMGElement g) {
				nl = g.getChildNodes();
				for(j = 0; j < nl.getLength(); j++) {
					if((n = nl.item(j)) instanceof Element e && e.getAttribute("id").endsWith("_root")) {
						g.insertBefore((child == 0?left:right).getRenderer().getRenderableSVG(), e);
						child++;
						documentElement.appendChild(e);
					}
				}
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
