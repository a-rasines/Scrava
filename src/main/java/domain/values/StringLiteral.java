package domain.values;

import ui.renderers.IRenderer.IRenderable;

public class StringLiteral extends AbstractLiteral<String>{

	private static final long serialVersionUID = -7828034209905266775L;
	
	public StringLiteral(String value, IRenderable parent) {
		super(value, parent);
	}
	
	@Override
	public String getCode() {
		return '"'+super.getCode()+'"';
	}
	@Override
	public boolean isEmpty() {
		return value().equals("");
	}
	@Override
	public void setValue(String str) {
		this.setValue(str, true);
	}

}
