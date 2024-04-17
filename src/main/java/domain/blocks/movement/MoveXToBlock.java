package domain.blocks.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.values.NumberLiteral;

public class MoveXToBlock extends MoveToBlock{
	private static final long serialVersionUID = -2626208983124778899L;

	public MoveXToBlock(Sprite s, Valuable<? extends Number> x) {
		super(s, x, new NumberLiteral<Byte>((byte)0));
	}
	
	public MoveXToBlock(Sprite s) {
		this(s, new NumberLiteral<Integer>(0));
	}

	@Override
	public String getTitle() {
		return "Move to x: " + VARIABLE_NUM;
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {super.getAllVariables()[0]};
	}

}
