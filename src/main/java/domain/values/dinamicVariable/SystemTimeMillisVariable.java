package domain.values.dinamicVariable;

import java.util.Set;

import domain.Sprite;
import domain.models.interfaces.Translatable;
import domain.values.DinamicVariable;

public class SystemTimeMillisVariable extends DinamicVariable<Long>{

	private static final long serialVersionUID = 4103883490560571104L;

	public SystemTimeMillisVariable() {
		super("epoch time (ms)", null);
	}

	@Override
	public String getCode() {
		return "System.currentTimeMillis()";
	}

	@Override
	public void getImports(Set<String> imports) {}

	@Override
	public Translatable create(Sprite s) {
		return new SystemTimeMillisVariable();
	}

	@Override
	public void reset() {}

	@Override
	public Long value() {
		return System.currentTimeMillis();
	}

}
