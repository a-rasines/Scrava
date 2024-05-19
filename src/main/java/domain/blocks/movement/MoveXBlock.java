package domain.blocks.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;

public class MoveXBlock extends MoveBlock{
	private static final long serialVersionUID = -2626208983124778899L;

	@Override
	public MoveXBlock create(Sprite s) {
		return new MoveXBlock(s);
	}
	
	public MoveXBlock(Sprite s) {
		super(s);
	}
	
	public MoveXBlock(Sprite s, Valuable<? extends Number> x) {
		super(s, x, null);
	}
	
	@Override
	public String getTitle() {
		return "Move x: " + VARIABLE_NUM;
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {super.getAllVariables()[0]};
	}

}
