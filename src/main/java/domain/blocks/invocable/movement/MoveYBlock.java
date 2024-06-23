package domain.blocks.invocable.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;

public class MoveYBlock extends MoveBlock{
	private static final long serialVersionUID = -2626208983124778899L;

	@Override
	public MoveYBlock create(Sprite s) {
		return new MoveYBlock(s);
	}
	
	public MoveYBlock(Sprite s, Valuable<? extends Number> y) {
		super(s, null, y);
	}
	
	public MoveYBlock(Sprite s) {
		super(s);
	}
	@Override
	public String getTitle() {
		return "Move y: " + VARIABLE_NUM;
	}
	
	@Override
	public Valuable<?>[] getAllVariables() {
		return new Valuable[] {super.getAllVariables()[1]};
	}
	
	

}

