package domain.values;

import ui.renderers.IRenderer.IRenderable;

public class BooleanLiteral extends AbstractLiteral<Boolean> {
	
	private static final long serialVersionUID = 6494348552216911637L;
	
	public BooleanLiteral(IRenderable ir) {
		super(true, ir);
	}
	
	public BooleanLiteral(boolean val, IRenderable ir) {
		super(val, ir);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void setValue(String str) {
		this.setValue(Boolean.parseBoolean(str), true);
		
	}
	
	
	

}
