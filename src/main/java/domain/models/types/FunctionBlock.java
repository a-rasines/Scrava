package domain.models.types;

import java.util.function.Supplier;

import domain.Sprite;
import ui.renderers.InvocableBlockRenderer;
import ui.renderers.InvocableBlockRenderer.InvocableBlockRenderable;

public abstract class FunctionBlock implements InvocableBlockRenderable {
	private static final long serialVersionUID = -8575456667649092252L;
	
	private InvocableBlockRenderer ibr;
	
	@Override
	public InvocableBlockRenderer getRenderer() {
		return ibr;
	}
	
	private Sprite s;
	protected FunctionBlock(Sprite s) {
		this.s = s;
		ibr = new InvocableBlockRenderer(this);
	}
	
	public Sprite getSprite() {
		return s;
	}
	
	@Override
	public Supplier<Boolean> getTick() {
		return () -> {
			invoke();
			return true;
		};
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}

}
