package domain.values;

import domain.Sprite;
import domain.models.interfaces.Translatable;

public class StringLiteral extends AbstractLiteral<String>{

	private static final long serialVersionUID = -7828034209905266775L;
	
	@Override
	public Translatable create(Sprite s) {
		return nullValue();
	}
	
	public static StringLiteral nullValue() {
		return new StringLiteral("");
	}
	public StringLiteral(String value) {
		super(value);
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
