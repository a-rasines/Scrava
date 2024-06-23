package domain.blocks.invocable.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;

public class MoveYToBlock extends MoveToBlock{
	private static final long serialVersionUID = -2626208983124778899L;

	@Override
	public MoveYToBlock create(Sprite s) {
		return new MoveYToBlock(s);
	}
	
	public MoveYToBlock(Sprite s, Valuable<? extends Number> y) {
		super(s, null, y);
	}
	
	public MoveYToBlock(Sprite s) {
		super(s);
	}
	
	@Override
	public String getTitle() {
		return "Move to y: " + VARIABLE_NUM;
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {super.getAllVariables()[1]};
	}

}
