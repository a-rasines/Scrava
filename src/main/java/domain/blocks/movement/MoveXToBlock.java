package domain.blocks.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;

public class MoveXToBlock extends MoveToBlock{
	private static final long serialVersionUID = -2626208983124778899L;

	@Override
	public MoveXToBlock create(Sprite s) {
		return new MoveXToBlock(s);
	}
	
	public MoveXToBlock(Sprite s, Valuable<? extends Number> x) {
		super(s, x, null);
	}
	
	public MoveXToBlock(Sprite s) {
		super(s);
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
