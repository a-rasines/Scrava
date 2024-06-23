package domain.blocks.invocable.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.values.NumberHelper;

public class MoveYToBlock extends CoordinateBlock{
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

	@Override
	public void invoke() {
		this.getSprite().getY().setValue(NumberHelper.castTo(getY().value(), Long.class));
	}

	@Override
	public String getCode() {
		return "this.moveTo(this.getX(), " + (getY().value() instanceof Integer?getY().getCode():"(int)"+getY().getCode())+");";
	}

}
