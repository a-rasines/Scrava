package domain.models.types;

import domain.Sprite;
import ui.renderers.InvocableBlockRenderer.InvocableBlockRenderable;

public abstract class FunctionBlock implements InvocableBlockRenderable{
	private static final long serialVersionUID = -8575456667649092252L;
	
	private Sprite s;
	protected FunctionBlock(Sprite s) {
		this.s = s;
	}
	
	public Sprite getSprite() {
		return s;
	}
	@Override
	public boolean tick() {
		invoke();
		return true;
	}
	@Override
	public void firstTick() {
		invoke();
	}

}
