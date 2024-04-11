package domain.values;

public class BooleanLiteral extends AbstractLiteral<Boolean> {
	
	private static final long serialVersionUID = 6494348552216911637L;
	
	public BooleanLiteral(boolean val) {
		super(val);
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
