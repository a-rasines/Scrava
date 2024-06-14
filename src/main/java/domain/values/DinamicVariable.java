package domain.values;

import domain.Sprite;
import ui.renderers.IRenderer;
import ui.renderers.SimpleBlockRenderer;

public abstract class DinamicVariable<T> implements IVariable<T>{

	private static final long serialVersionUID = -1329483820079287997L;
	private String name;
	private Sprite s;
	
	protected DinamicVariable(String name, Sprite s) {
		this.name = name;
		this.s = s;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String s) {
		this.name = s;
	}
	SimpleBlockRenderer sbr = new SimpleBlockRenderer(this);
	@Override
	public IRenderer getRenderer() {
		return sbr;
	}
	@Override
	public Sprite getSprite() {
		return s;
	}
}
