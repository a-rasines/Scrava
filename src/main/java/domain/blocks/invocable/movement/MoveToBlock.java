package domain.blocks.invocable.movement;

import domain.Sprite;
import domain.models.interfaces.Valuable;
import domain.values.NumberHelper;

public class MoveToBlock extends CoordinateBlock{
	private static final long serialVersionUID = 633807958335477657L;
	
	@Override
	public MoveToBlock create(Sprite s) {
		return new MoveToBlock(s);
	}
	
	public MoveToBlock(Sprite s) {
		super(s);
	}
	
	public MoveToBlock(Sprite s, Valuable<? extends Number> x, Valuable<? extends Number> y) {
		super(s, x, y);
	}

	@Override
	public String getTitle() {
		return "Move to x:"+VARIABLE_NUM+" y:"+VARIABLE_NUM;
	}

	@Override
	public String getCode() {
		return "this.moveTo("+getX().getCode()+", "+getY().getCode()+");";
	}

	@Override
	public void invoke() {
		this.getSprite().getX().setValue(NumberHelper.castTo(getX().value(), Long.class));
		this.getSprite().getY().setValue(NumberHelper.castTo(getY().value(), Long.class));
		
	}
	
}
